package scorers;

import crawler.Page;

public interface Scorer {
	int getScore(Page page);
}
