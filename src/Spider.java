import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {
	private static final int SEARCH_LIMIT = 20;
	private Set<String> visitedPages; 
	private List<String> pagesToVisit;
	
	public Spider() {
		visitedPages = new HashSet<>();
		pagesToVisit = new LinkedList<>();
	}
	
	public void search(String url, String keyWord) {
		initSearch(url);
		while (visitedPages.size() < SEARCH_LIMIT) {
			String currentUrl = nextUrl();
			visitedPages.add(currentUrl);
			PageCrawler pageCrawler = new PageCrawler(currentUrl);
			if (!pageCrawler.crawl()) {
				continue;
			}
			if (pageCrawler.containsWord(keyWord)) {
				System.out.println("**SUCCESS**: " + currentUrl);
			}
			AddLinks(pageCrawler);
		}
	}

	private void AddLinks(PageCrawler pageCrawler) {
		List<String> links = pageCrawler.getLinks();
		for (String link : links) {
			if (!visitedPages.contains(link))
				pagesToVisit.add(link);
		}
	}
	
	private void initSearch(String url) {
		visitedPages = new HashSet<>();
		pagesToVisit = new LinkedList<>();
		pagesToVisit.add(url);
	}
	
	private String nextUrl() {
		String url = pagesToVisit.remove(0);
		while (visitedPages.contains(url)) {
			url = pagesToVisit.remove(0);
		}
		return url;
	}
}
