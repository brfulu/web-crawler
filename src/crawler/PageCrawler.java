package crawler;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageCrawler {
	private Document document;
	private Page page;
	private List<String> links;
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

	public PageCrawler(Page page) {
		this.page = page;
		links = new LinkedList<>();
	}
	
	public boolean crawl() {
		if (!getPage()) {
			return false;
		}
		if (!parseLinks()) {
			return false;
		}
		return true;
	}
	
	public boolean getPage() {
		try {
			Connection connection = Jsoup.connect(page.getUrl()).userAgent(USER_AGENT);
			document = connection.get();
			if (connection.response().statusCode() == 200) {
			//	System.out.println("Visiting web page at: " + page.getUrl());
			//	System.out.println("Depth = " + page.getDepth());
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("Retrieved something other than html!");
				return false;
			}
			return true;
		}
		catch (Exception e) {
			System.out.println("Error while getting page at: " + page.getUrl());
			return false;
		}
	}
	
	public boolean parseLinks() {
		try {
			Elements pageLinks = document.select("a[href]");
			System.out.println("Found (" + pageLinks.size() + ") links at: " + page.getUrl());
			for (Element link : pageLinks) {
				this.links.add(link.absUrl("href"));
			}
			return true;
		}
		catch (Exception e) {
			System.out.println("Error while getting links from: " + page.getUrl());
			return false;
		}
	}
	
	public List<String> getLinks() {
		return links;
	}
	
	public boolean containsWord(String keyWord) {
		try {
			String body = document.body().text().toLowerCase();
			return body.contains(keyWord.toLowerCase());
		}
		catch (Exception e) {
			return false;
		}
	}
}
