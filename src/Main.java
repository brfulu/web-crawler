import crawler.Spider;
import scorers.BFSScorer;
import scorers.DFSScorer;

public class Main {
	
	public static void main(String[] args) {
		Spider bfsSpider = new Spider(new BFSScorer());
	//	bfsSpider.search("http://www.blic.rs/", "crvena zvezda");
		
		Spider dfsSpider = new Spider(new DFSScorer());
		dfsSpider.search("https://www.google.ba/", "crvena zvezda");
	}
	
}
