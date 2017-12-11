import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageCrawler {
	private Document document;
	private String url;
	private List<String> links;
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

	public PageCrawler(String url) {
		this.url = url;
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
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			document = connection.get();
			if (connection.response().statusCode() == 200) {
				System.out.println("Visiting web page at: " + url);
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("Retrieved something other than html!");
				return false;
			}
			return true;
		}
		catch (Exception e) {
			System.out.println("Error while getting page at: " + url);
			return false;
		}
	}
	
	public boolean parseLinks() {
		try {
			Elements pageLinks = document.select("a[href]");
			System.out.println("Found (" + pageLinks.size() + ") links at: " + url);
			for (Element link : pageLinks) {
				this.links.add(link.absUrl("href"));
			}
			return true;
		}
		catch (Exception e) {
			System.out.println("Error while getting links from: " + url);
			return false;
		}
	}
	
	public List<String> getLinks() {
		return links;
	}
	
	public boolean containsWord(String keyWord) {
		String body = document.body().text().toLowerCase();
		return body.contains(keyWord.toLowerCase());
	}
}
