import java.util.ArrayList;
import java.util.List;

import crawler.Page;
import crawler.Spider;
import scorers.BFSScorer;
import scorers.DFSScorer;
import scorers.KeyWordScorer;
import scorers.TFIDF;
import scorers.TFIDFKeyWordScorer;
import scorers.TFIDFPageSimilarityScorer;

public class Main {
	
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
	        public void uncaughtException(Thread t, Throwable e) {
	            System.out.println("exception " + e + " from thread " + t);
	        }
	    });
		
		Spider bfsSpider = new Spider(new BFSScorer());
		bfsSpider.search("https://en.wikipedia.org/wiki/English_Wikipedia", "sport");
		
	//	Spider dfsSpider = new Spider(new DFSScorer());
	//	dfsSpider.search("https://en.wikipedia.org/wiki/Serbia", "belgrade");
		
	//	List<String> keyWords = new ArrayList<>();
	//	keyWords.add("kobe");
	//	keyWords.add("kobe bryant");
	//	keyWords.add("los angeles");

	//	Spider keyWordSpider = new Spider(new KeyWordScorer(keyWords));
	//	keyWordSpider.search("https://en.wikipedia.org/wiki/Basketball", "algorithm");

	//	Spider tfidfKeyWordSpider = new Spider(new TFIDFKeyWordScorer(keyWords));
	//	tfidfKeyWordSpider.search("https://en.wikipedia.org/wiki/Basketball", "");
		
	//	Spider tfidfPageSimilartyScorer = new Spider(new TFIDFPageSimilarityScorer(new Page("https://en.wikipedia.org/wiki/Novak_Djokovic", 0)));
	//	tfidfPageSimilartyScorer.search("https://en.wikipedia.org/wiki/Tennis", "");
	}
	
}
