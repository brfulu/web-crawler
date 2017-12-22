package scorers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.print.Doc;

import org.jsoup.nodes.Element;

import crawler.Page;
import javafx.scene.control.TreeCell;

public class TFIDFPageSimilarityScorer implements Scorer {
	private List<String> keyWords;
	
	public TFIDFPageSimilarityScorer(Page page) {
		keyWords = new ArrayList<>();
		try {
			Element titleElement = page.getDocument().selectFirst("title");
			addTextToKeyWords(titleElement.text());
			addMostFrequentWords(page);
			removeDuplicates();
			System.out.println("Liked page: " + page.getUrl());
			System.out.println("Page keywords = " + keyWords);
		}
		catch (Exception e) {
			System.out.println("Parsing starting page to keywords error!");
		}
		TFIDF.getInstance().setKeyWords(keyWords);
	}
	
	private void addMostFrequentWords(Page page) {
		Map<String, Integer> frequency = new TreeMap<>();
		calculateTermFrequency(page, frequency);
		List<Integer> values = new ArrayList<>(frequency.values());
		Collections.sort(values);
		Set<String> used = new HashSet<>();
		for (int i = values.size() - 1; i >= 0; i--) {
			for (String key : frequency.keySet()) {
				if (frequency.get(key) == values.get(i) && !used.contains(key)) {
					used.add(key);
					break;
				}
			}
			if (used.size() >= 20) break;
		}
		StringBuilder sb = new StringBuilder();
		for (String term : used) {
			sb.append(term);
			sb.append(" ");
		}
		addTextToKeyWords(sb.toString());
	}

	private void calculateTermFrequency(Page page, Map<String, Integer> count) {
		String[] terms = page.getDocument().body().text().toLowerCase().split("\\s+");
		for (String term : terms) {
			if (term.length() < 4) continue;
			if (count.containsKey(term)) {
				count.put(term, count.get(term) + 1);
			}
			else {
				count.put(term, 1);
			}
		}
	}

	private void removeDuplicates() {
		Set<String> set = new HashSet<>();
		set.addAll(keyWords);
		keyWords.clear();
		keyWords.addAll(set);
	}
	
	private void addTextToKeyWords(String text) {
		String[] terms = new String[0];
		terms = text.split("\\s+");
		for (String term : terms) {
			term = term.toLowerCase();
			Pattern p = Pattern.compile("[^a-zA-Z0-9]");
			boolean hasSpecialChar = p.matcher(term).find();
			if(!hasSpecialChar){
				keyWords.add(term);
			}
			if (keyWords.size() > 13) break;
		}
	}
	
	@Override
	public double getScore(Page page) {
		return -TFIDF.getInstance().getScore(page);
	}
}
