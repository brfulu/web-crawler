package crawler;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageCrawler {
	private Document document;
	private Page page;
	private Set<String> links;

	public PageCrawler(Page page) {
		this.page = page;
		links = new HashSet<>();
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
			document = page.getDocument();
			System.out.println();
			if (document != null) {
				System.out.println("Visiting web page at: " + page.getUrl());
				System.out.println("Score = " + page.getScore());
			}
			else {
				System.out.println("Error with page at: " + page.getUrl());
				return false;
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public boolean parseLinks() {
		try {
			Elements pageLinks = document.select("a[href]");
			for (int i = 0; i < Math.min(100, pageLinks.size()); i++) {
				String link = pageLinks.get(i).absUrl("href");
				if (link.contains("en.") && !link.contains("#") && !link.contains("=edit")){
					this.links.add(link);
				}
			}
			System.out.println("Found (" + links.size() + ") links at: " + page.getUrl());
			return true;
		}
		catch (Exception e) {
			System.out.println("Error while getting links from: " + page.getUrl());
			return false;
		}
	}
	
	public Set<String> getLinks() {
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
