package crawler;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import scorers.BFSScorer;
import scorers.DFSScorer;
import scorers.Scorer;
import scorers.TFIDF;

public class Spider {
	private static final int SEARCH_LIMIT = 1000;
	private Set<Page> visitedPages; 
	private PriorityQueue<Page> pagesToVisit;
	private List<Page> frontier;
	private Scorer scorer;
	
	public Spider(Scorer scorer) {
		this.scorer = scorer;
	}
	
	public void search(String url, String keyWord) {
		initSearch(url);
		while (!pagesToVisit.isEmpty() && visitedPages.size() < SEARCH_LIMIT) {
			Page currentPage = nextPage();
			visitedPages.add(currentPage);
			PageCrawler pageCrawler = new PageCrawler(currentPage);
			if (!pageCrawler.crawl()) {
				continue;
			}
			if (keyWord.length() > 0 && pageCrawler.containsWord(keyWord)) {
				System.out.println("**SUCCESS**: " + currentPage.getUrl());
			}
			AddLinks(currentPage, pageCrawler);
		}
	}
	
	private void retreivePages(Set<String> links, Page currentPage) {
		int poolSize = links.size();
		if (poolSize == 0) return;
		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		List<Future<Runnable>> futures = new ArrayList<>();
		for (String link : links) {
			if (!visitedPages.contains(link)) {
				Executors.newFixedThreadPool(poolSize);
				submitTask(currentPage, link, futures, service);
			}
		}
		waitForThreads(futures, service);
	}

	private void AddLinks(Page currentPage, PageCrawler pageCrawler) {
		frontier = new ArrayList<>();
		Set<String> links = pageCrawler.getLinks();
		if ((scorer instanceof DFSScorer) || (scorer instanceof BFSScorer)) {
			for (String link : links) {
				if (!visitedPages.contains(link)) {
					Page nextPage = new Page(link, currentPage.getDepth() + 1);
					nextPage.setScore(scorer.getScore(nextPage));
					frontier.add(nextPage);
				}
			}
		}
		else {
			retreivePages(links, currentPage);
		}
		addPagesToQueue();
	}
	
	private void addPagesToQueue() {
		synchronized (frontier) {
			for (Page page : frontier) {
				TFIDF.getInstance().addFrontierPage(page);
			}
			TFIDF.getInstance().clearIDFCache();
			TFIDF.getInstance().clearScoreCache();
			for (Page page : frontier) {
				page.setScore(scorer.getScore(page));
				pagesToVisit.add(page);
			}
		}
	}
	
	private void submitTask(Page currentPage, String link, List<Future<Runnable>> futures, ExecutorService service) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				synchronized (frontier) {
					Page nextPage = new Page(link, currentPage.getDepth() + 1);
					nextPage.getDocument();
					frontier.add(nextPage);
				}
				Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			        public void uncaughtException(Thread t, Throwable e) {
			            System.out.println("Exception " + e + " from thread " + t);
			        }
			    });
			}
		};
		Future f = service.submit(task);
		futures.add(f);
	}
	
	private void waitForThreads(List<Future<Runnable>> futures, ExecutorService service) {
		try {
			for (Future<Runnable> f : futures) {
		        f.get();
		     }
		     service.shutdownNow();
		}
		catch (Exception e) {
			System.out.println("Thread: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void initSearch(String url) {
		visitedPages = new HashSet<>();
		pagesToVisit = new PriorityQueue<>();
		Page source = new Page(url, 0);
		pagesToVisit.add(source);
	}
	
	private Page nextPage() {
		Page page = pagesToVisit.poll();
		while (visitedPages.contains(page)) {
			page = pagesToVisit.poll();
		}
		return page;
	}
	
}
