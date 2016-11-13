package nz.co.wing.tvmanager.model;

public class TorrentFile {
	private String name;
	private int size;
	private int downloaded;
	private TorrentPriority priority;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(final int downloaded) {
		this.downloaded = downloaded;
	}

	public TorrentPriority getPriority() {
		return priority;
	}

	public void setPriority(final TorrentPriority priority) {
		this.priority = priority;
	}
}
