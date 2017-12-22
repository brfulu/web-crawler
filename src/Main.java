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
	//	bfsSpider.search("https://en.wikipedia.org/wiki/English_Wikipedia", "crvena zvezda");
		
		Spider dfsSpider = new Spider(new DFSScorer());
	//	dfsSpider.search("http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/", "crvena zvezda");
		
		List<String> keyWords = new ArrayList<>();
		keyWords.add("novak");
		keyWords.add("djokovic");
		keyWords.add("tennis");
		keyWords.add("serbia");
//		Spider keyWordSpider = new Spider(new KeyWordScorer(keyWords));
//		keyWordSpider.search("https://en.wikipedia.org/wiki/Serbia", "algorithm");

		Spider tfidfKeyWordSpider = new Spider(new TFIDFKeyWordScorer(keyWords));
		tfidfKeyWordSpider.search("https://en.wikipedia.org/wiki/Sports", "");
		
		Spider tfidfPageSimilartyScorer = new Spider(new TFIDFPageSimilarityScorer(new Page("https://en.wikipedia.org/wiki/Novak_Djokovic", 0)));
	//	tfidfPageSimilartyScorer.search("https://en.wikipedia.org/wiki/Tennis", "");
	}
	
}
