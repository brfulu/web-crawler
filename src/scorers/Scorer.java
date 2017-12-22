package scorers;

import crawler.Page;

public interface Scorer {
	double getScore(Page page);
	
}
