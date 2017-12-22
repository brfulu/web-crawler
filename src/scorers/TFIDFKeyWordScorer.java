package scorers;

import java.util.List;

import crawler.Page;

public class TFIDFKeyWordScorer implements Scorer {
	private List<String> keyWords;
	
	public TFIDFKeyWordScorer(List<String> keyWords) {
		this.keyWords = keyWords;
		TFIDF.getInstance().setKeyWords(keyWords);
	}
	
	@Override
	public double getScore(Page page) {
		return -TFIDF.getInstance().getScore(page);
	}
}
