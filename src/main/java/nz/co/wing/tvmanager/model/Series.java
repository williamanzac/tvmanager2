package nz.co.wing.tvmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "series")
// @JsonInclude(Include.NON_DEFAULT)
public class Series {
	@Id
	private String id;
	@Column
	private String seriesId;
	@Column
	private String language;
	@Column
	private String seriesName;
	@Column
	private String banner;
	@Column(length = 65536)
	private String overview;
	@Column
	private String firstAired;
	@Column
	private String imdbId;
	@Column
	private String zap2ItId;
	@ElementCollection
	@CollectionTable(name = "actors", joinColumns = @JoinColumn(name = "series_id"))
	@Column(name = "actor")
	private List<String> actors = new ArrayList<>();
	@Column
	private String airsDayOfWeek;
	@Column
	private String airsTime;
	@Column
	private String contentRating;
	@ElementCollection
	@CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "series_id"))
	@Column(name = "genre")
	private List<String> genres = new ArrayList<>();
	@Column
	private String network;
	@Column
	private String rating;
	@Column
	private String runtime;
	@Column
	private String status;
	@Column
	private String fanart;
	@Column
	private String lastUpdated;
	@Column
	private String poster;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(final String seriesId) {
		this.seriesId = seriesId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(final String seriesName) {
		this.seriesName = seriesName;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(final String overview) {
		this.overview = overview;
	}

	public String getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(final String firstAired) {
		this.firstAired = firstAired;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(final String imdbId) {
		this.imdbId = imdbId;
	}

	public String getZap2ItId() {
		return zap2ItId;
	}

	public void setZap2ItId(final String zap2ItId) {
		this.zap2ItId = zap2ItId;
	}

	public List<String> getActors() {
		return actors;
	}

	public void setActors(final List<String> actors) {
		this.actors = actors;
	}

	public String getAirsDayOfWeek() {
		return airsDayOfWeek;
	}

	public void setAirsDayOfWeek(final String airsDayOfWeek) {
		this.airsDayOfWeek = airsDayOfWeek;
	}

	public String getAirsTime() {
		return airsTime;
	}

	public void setAirsTime(final String airsTime) {
		this.airsTime = airsTime;
	}

	public String getContentRating() {
		return contentRating;
	}

	public void setContentRating(final String contentRating) {
		this.contentRating = contentRating;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(final List<String> genres) {
		this.genres = genres;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(final String network) {
		this.network = network;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(final String rating) {
		this.rating = rating;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(final String runtime) {
		this.runtime = runtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getFanart() {
		return fanart;
	}

	public void setFanart(final String fanart) {
		this.fanart = fanart;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(final String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(final String poster) {
		this.poster = poster;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void update(final Series that) {
		getActors().clear();
		getActors().addAll(that.actors);
		setAirsDayOfWeek(that.airsDayOfWeek);
		setBanner(that.banner);
		setAirsTime(that.airsTime);
		setContentRating(that.contentRating);
		setFanart(that.fanart);
		setFirstAired(that.firstAired);
		getGenres().clear();
		getGenres().addAll(that.genres);
		setImdbId(that.imdbId);
		setLanguage(that.language);
		setLastUpdated(that.lastUpdated);
		setNetwork(that.network);
		setOverview(that.overview);
		setPoster(that.poster);
		setRating(that.rating);
		setRuntime(that.runtime);
		setSeriesId(that.seriesId);
		setSeriesName(that.seriesName);
		setStatus(that.status);
		setZap2ItId(that.zap2ItId);
	}
}
