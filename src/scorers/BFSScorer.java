package scorers;

import crawler.Page;

public class BFSScorer implements Scorer {

	public double getScore(Page page) {
		return page.getDepth();
	}

}
