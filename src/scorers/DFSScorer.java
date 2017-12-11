package scorers;

import crawler.Page;

public class DFSScorer implements Scorer {

	public int getScore(Page page) {
		return -page.getDepth();
	}
	
}
