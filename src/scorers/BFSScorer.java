package scorers;

import crawler.Page;

public class BFSScorer implements Scorer {

	public int getScore(Page page) {
		return page.getDepth();
	}

}
