package crawler;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Timer;

import scorers.Scorer;

public class Spider {
	private static final int SEARCH_LIMIT = 1000;
	private Set<Page> visitedPages; 
	private PriorityQueue<Page> pagesToVisit;
	private Scorer scorer;
	
	public Spider(Scorer scorer) {
		this.scorer = scorer;
	}
	
	public void search(String url, String keyWord) {
		initSearch(url);
		while (visitedPages.size() < SEARCH_LIMIT) {
			Page currentPage = nextPage();
			visitedPages.add(currentPage);
			PageCrawler pageCrawler = new PageCrawler(currentPage);
			if (!pageCrawler.crawl()) {
				continue;
			}
			System.out.println(LocalDateTime.now());
			if (pageCrawler.containsWord(keyWord)) {
				System.out.println("**SUCCESS**: " + currentPage.getUrl());
			}
			System.out.println(LocalDateTime.now());
			AddLinks(currentPage, pageCrawler);
			System.out.println(visitedPages.size());
		}
		System.out.println(visitedPages.size());
	}

	private void AddLinks(Page currentPage, PageCrawler pageCrawler) {
		Set<String> links = pageCrawler.getLinks();
		List<Thread> threads = new ArrayList<>();
		for (String link : links) {
			if (!visitedPages.contains(link)) {
				if (!link.contains("en")) continue;
				startThread(currentPage, link, threads);
				if (threads.size() > 100) break;
			}
		}
		waitForThreads(threads);
	}
	
	private void startThread(Page currentPage, String link, List<Thread> threads) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (pagesToVisit) {
					Page nextPage = new Page(link, currentPage.getDepth() + 1);
					System.out.println(nextPage.getUrl());
					nextPage.setScore(scorer.getScore(nextPage));
					pagesToVisit.add(nextPage);
				}
			}
		});
		threads.add(thread);
		thread.start();
	}
	
	private void waitForThreads(List<Thread> threads) {
		for (Thread thread : threads) {
			try {
				thread.join();
			}
			catch (Exception e) {
				System.out.println("error in thread");
			}
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
