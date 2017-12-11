package crawler;

import java.util.concurrent.Executors;

import javax.print.attribute.standard.MediaSize.Other;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Page implements Comparable<Page> {
	private String url;
	private int depth;
	private Document document;
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private int score;
	
	public Page(String url, int depth) {
		super();
		this.url = url;
		this.depth = depth;
		document = null;
	}
	
	public Document getDocument() {
		if (document != null) return document;
		try {
			Connection connection = Jsoup.connect(getUrl()).userAgent(USER_AGENT);
			connection.timeout(2 * 1000);
			document = connection.get();
			if (!connection.response().contentType().contains("text/html")) {
				document = null;
			}
		}
		catch (Exception e) {
			document = null;
		}
		return document;
	}

	public String getUrl() {
		return url;
	}

	public int getDepth() {
		return depth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public int compareTo(Page other) {
		return Integer.compare(score, other.getScore());
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
