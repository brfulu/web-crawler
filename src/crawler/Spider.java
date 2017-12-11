package crawler;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import scorers.Scorer;

public class Spider {
	private static final int SEARCH_LIMIT = 20;
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
			System.out.println(currentPage.getUrl() + " " + currentPage.getDepth());
			visitedPages.add(currentPage);
			PageCrawler pageCrawler = new PageCrawler(currentPage);
			if (!pageCrawler.crawl()) {
				continue;
			}
			if (pageCrawler.containsWord(keyWord)) {
				System.out.println("**SUCCESS**: " + currentPage.getUrl());
			}
			AddLinks(currentPage, pageCrawler);
			System.out.println();
		}
		System.out.println(visitedPages.size());
	}

	private void AddLinks(Page currentPage, PageCrawler pageCrawler) {
		List<String> links = pageCrawler.getLinks();
		for (String link : links) {
			if (!visitedPages.contains(link)) {
				Page nextPage = new Page(link, currentPage.getDepth() + 1);
				pagesToVisit.add(nextPage);
			}
		}
	}
	
	private void initSearch(String url) {
		visitedPages = new HashSet<>();
		pagesToVisit = new PriorityQueue<>(10, new PageComparator());
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
	
	private class PageComparator implements Comparator<Page> {
		@Override
		public int compare(Page a, Page b) {
			return Integer.compare(scorer.getScore(a), scorer.getScore(b));
		}
	}
}
