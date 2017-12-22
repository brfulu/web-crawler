package scorers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import crawler.Page;

public class TFIDF {
	private List<String> keyWords;
	private List<String> allDocuments;
	private Map<String, Double> scoreCache;
	private Map<String, Double> idfCache;
	private List<Double> baseVector;
	private String baseDocument;
	
	private static TFIDF instance = null;
	
	public static TFIDF getInstance() {
		if (instance == null) {
			instance = new TFIDF();
		}
		return instance;
	}
	
	private TFIDF() {
		allDocuments = new ArrayList<>();
		scoreCache = new TreeMap<>();
		idfCache = new TreeMap<>();
		baseDocument = "";
	}
	
	public double getScore(Page page) {
		try {
			if (scoreCache.containsKey(page.getUrl())) {
				return scoreCache.get(page.getUrl());
			}
			List<Double> curVector = getVector(page.getDocument().body().text());
			baseVector = getVector(baseDocument);
			double score = cosineSimilarity(baseVector, curVector);
			scoreCache.put(page.getUrl(), score);
			return score;
		}
		catch (Exception e) {
			return -1.0;
		}
	}
	
	private double termFrequency(String term, String document) {
		String[] terms = (document.split("\\s+"));
		int termCount = terms.length;
		int matchCount = 0;
		matchCount = document.toLowerCase().split(term.toLowerCase()).length - 1;
		return  matchCount / (double)(termCount);
	}
	
	private double inverseDocumentFrequenency(String term) {
		if (idfCache.containsKey(term)) {
			return idfCache.get(term);
		}
		int numDocumentsWithTerm = countDocumentsWithTerm(term);
		double idf = 1.0;
		if (numDocumentsWithTerm > 0) {
			idf = 1.0 + Math.log(allDocuments.size() / (double)numDocumentsWithTerm);
		}
		idfCache.put(term, idf);
		return idf;
	}
	
	private int countDocumentsWithTerm(String term) {
		int result = 0;
		for (String document : allDocuments) {
			int count = document.split(term.toLowerCase()).length - 1;
			if (count > 0) {
				result++;
			}
		}
		return result;
	}
		
	private List<Double> getVector(String document) {
		if (!allDocuments.contains(document)) {
			allDocuments.add(document);
		}
		List<Double> vector = new ArrayList<>();
		for (String term : keyWords) {
			vector.add(termFrequency(term, document) * inverseDocumentFrequenency(term));
		}
		return vector;
	}
	
	private double cosineSimilarity(List<Double> a, List<Double> b) {
		return dotProduct(a, b) / (intensity(a) * intensity(b));
	}
	
	private double dotProduct(List<Double> a, List<Double> b) {
		double result = 0.0;
		for (int i = 0; i < a.size(); i++) {
			result += a.get(i) * b.get(i);
		}
		return result;
	}
	
	private double intensity(List<Double> v) {
		double result = 0.0;
		for (double val : v) {
			result += val * val;
		}
		return Math.sqrt(result);
	}
	
	public void addFrontierPage(Page page) {
		synchronized (allDocuments) {
			try {
				allDocuments.add(page.getDocument().body().text());
			}
			catch (Exception e) {
				System.out.println("Page timeouted.");
			}
		}
	}
	
	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		StringBuilder sb = new StringBuilder();
		for (String word : keyWords) {
			sb.append(word);
			sb.append(" ");
		}
		baseDocument = sb.toString();
	}
	
	public void clearIDFCache() {
		idfCache = new TreeMap<>();
	}

	public void clearScoreCache() {
		scoreCache = new TreeMap<>();
	}

}
