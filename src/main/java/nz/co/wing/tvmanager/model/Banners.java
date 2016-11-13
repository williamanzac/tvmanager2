package nz.co.wing.tvmanager.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "banners")
// @JsonInclude(Include.NON_DEFAULT)
public class Banners {
	@Id
	private int seriesId;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "BANNER_ID")
	private List<Banner> seriesList = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "BANNER_ID")
	private List<Banner> seasonList = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "BANNER_ID")
	private List<Banner> posterList = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "BANNER_ID")
	private List<Banner> fanartList = new ArrayList<>();

	public int getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(final int seriesId) {
		this.seriesId = seriesId;
	}

	public List<Banner> getSeriesList() {
		return seriesList;
	}

	public void setSeriesList(final List<Banner> seriesList) {
		this.seriesList = seriesList;
	}

	public List<Banner> getSeasonList() {
		return seasonList;
	}

	public void setSeasonList(final List<Banner> seasonList) {
		this.seasonList = seasonList;
	}

	public List<Banner> getPosterList() {
		return posterList;
	}

	public void setPosterList(final List<Banner> posterList) {
		this.posterList = posterList;
	}

	public List<Banner> getFanartList() {
		return fanartList;
	}

	public void setFanartList(final List<Banner> fanartList) {
		this.fanartList = fanartList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
