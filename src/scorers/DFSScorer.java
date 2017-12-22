package scorers;

import crawler.Page;

public class DFSScorer implements Scorer {

	public double getScore(Page page) {
		return -page.getDepth();
	}
}
