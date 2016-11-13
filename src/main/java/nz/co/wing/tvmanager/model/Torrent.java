package nz.co.wing.tvmanager.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "torrent")
// @JsonInclude(Include.NON_DEFAULT)
public class Torrent {
	@Column
	private String title;
	@Column
	private int seeds;
	@Column
	private int leechers;
	@Column
	private long size;
	@Id
	private String hash;
	@Column(length = 4096)
	private String url;
	@Column
	private Date pubDate;
	@ElementCollection
	@CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "torrent_id"))
	@Column(name = "category")
	private final Set<String> categories = new HashSet<>();
	@Column
	private TorrentState state;
	@Column
	private float percentComplete;
	@Column
	private int upSpeed = 0;
	@Column
	private int downSpeed = 0;
	@Column
	private int eta = 0;

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public int getSeeds() {
		return seeds;
	}

	public void setSeeds(final int seeds) {
		this.seeds = seeds;
	}

	public int getLeechers() {
		return leechers;
	}

	public void setLeechers(final int leechers) {
		this.leechers = leechers;
	}

	public long getSize() {
		return size;
	}

	public void setSize(final long size) {
		this.size = size;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(final String hash) {
		this.hash = hash;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(final Date pubDate) {
		this.pubDate = pubDate;
	}

	public float getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(final float percentComplete) {
		this.percentComplete = percentComplete;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public TorrentState getState() {
		return state;
	}

	public void setState(final TorrentState state) {
		this.state = state;
	}

	public int getUpSpeed() {
		return upSpeed;
	}

	public void setUpSpeed(final int upSpeed) {
		this.upSpeed = upSpeed;
	}

	public int getDownSpeed() {
		return downSpeed;
	}

	public void setDownSpeed(final int downSpeed) {
		this.downSpeed = downSpeed;
	}

	public int getEta() {
		return eta;
	}

	public void setEta(final int eta) {
		this.eta = eta;
	}
}
