package crawler;

public class Page {
	private String url;
	private int depth;
	
	public Page(String url, int depth) {
		super();
		this.url = url;
		this.depth = depth;
		
	}

	public String getUrl() {
		return url;
	}

	public int getDepth() {
		return depth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
