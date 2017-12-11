import java.util.ArrayList;
import java.util.List;

import crawler.Spider;
import scorers.BFSScorer;
import scorers.DFSScorer;
import scorers.KeyWordScorer;

public class Main {
	
	public static void main(String[] args) {
		Spider bfsSpider = new Spider(new BFSScorer());
	//	bfsSpider.search("https://en.wikipedia.org/wiki/English_Wikipedia", "crvena zvezda");
		
		Spider dfsSpider = new Spider(new DFSScorer());
	//	dfsSpider.search("https://www.google.ba/", "crvena zvezda");
		
		List<String> keyWords = new ArrayList<>();
		keyWords.add("algorithm");
		Spider keyWordSpider = new Spider(new KeyWordScorer(keyWords));
		keyWordSpider.search("https://en.wikipedia.org/wiki/Master_theorem", "algorithm");
	}
	
}
