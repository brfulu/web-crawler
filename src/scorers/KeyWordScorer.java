package scorers;

import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import crawler.Page;

public class KeyWordScorer implements Scorer {
	private List<String> keyWords;
	
	public KeyWordScorer(List<String> keyWords) {
		this.keyWords = keyWords;
	}
	
	public double getScore(Page page) {
		int score = 0;
		Document document = page.getDocument();
		if (document != null) {
			String body = document.body().text().toLowerCase();
			for (String key : keyWords) {
				score += countOccurences(body, key);
			}
		}
		return -score;
	}

	private int countOccurences(String text, String pattern) {
		return text.split(pattern.toLowerCase()).length - 1;
	}
}
