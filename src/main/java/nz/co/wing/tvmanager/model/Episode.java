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
@Table(name = "episode")
// @JsonInclude(Include.NON_DEFAULT)
public class Episode {
	@Id
	private String id;
	@Column
	private String combinedEpisodeNumber;
	@Column
	private String combinedSeason;
	@Column
	private String dvdChapter;
	@Column
	private String dvdDiscId;
	@Column
	private String dvdEpisodeNumber;
	@Column
	private String dvdSeason;
	@ElementCollection
	@CollectionTable(name = "directors", joinColumns = @JoinColumn(name = "episode_id"))
	@Column(name = "director")
	private List<String> directors = new ArrayList<>();
	@Column
	private String epImgFlag;
	@Column
	private String episodeName;
	@Column
	private int episodeNumber;
	@Column
	private String firstAired;
	@ElementCollection
	@CollectionTable(name = "guestStars", joinColumns = @JoinColumn(name = "episode_id"))
	@Column(name = "guestStar")
	private List<String> guestStars = new ArrayList<>();
	@Column
	private String imdbId;
	@Column
	private String language;
	@Column(length = 65536)
	private String overview;
	@Column
	private String productionCode;
	@Column
	private String rating;
	@Column
	private int seasonNumber;
	@ElementCollection
	@CollectionTable(name = "writers", joinColumns = @JoinColumn(name = "episode_id"))
	@Column(name = "writer")
	private List<String> writers = new ArrayList<>();
	@Column
	private String absoluteNumber;
	@Column
	private int airsAfterSeason;
	@Column
	private int airsBeforeSeason;
	@Column
	private int airsBeforeEpisode;
	@Column
	private String filename;
	@Column
	private String lastUpdated;
	@Column
	private String seriesId;
	@Column
	private String seasonId;
	@Column
	private String hash;
	@Column
	private EpisodeState state;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getCombinedEpisodeNumber() {
		return combinedEpisodeNumber;
	}

	public void setCombinedEpisodeNumber(final String combinedEpisodeNumber) {
		this.combinedEpisodeNumber = combinedEpisodeNumber;
	}

	public String getCombinedSeason() {
		return combinedSeason;
	}

	public void setCombinedSeason(final String combinedSeason) {
		this.combinedSeason = combinedSeason;
	}

	public String getDvdChapter() {
		return dvdChapter;
	}

	public void setDvdChapter(final String dvdChapter) {
		this.dvdChapter = dvdChapter;
	}

	public String getDvdDiscId() {
		return dvdDiscId;
	}

	public void setDvdDiscId(final String dvdDiscId) {
		this.dvdDiscId = dvdDiscId;
	}

	public String getDvdEpisodeNumber() {
		return dvdEpisodeNumber;
	}

	public void setDvdEpisodeNumber(final String dvdEpisodeNumber) {
		this.dvdEpisodeNumber = dvdEpisodeNumber;
	}

	public String getDvdSeason() {
		return dvdSeason;
	}

	public void setDvdSeason(final String dvdSeason) {
		this.dvdSeason = dvdSeason;
	}

	public List<String> getDirectors() {
		return directors;
	}

	public void setDirectors(final List<String> directors) {
		this.directors = directors;
	}

	public String getEpImgFlag() {
		return epImgFlag;
	}

	public void setEpImgFlag(final String epImgFlag) {
		this.epImgFlag = epImgFlag;
	}

	public String getEpisodeName() {
		return episodeName;
	}

	public void setEpisodeName(final String episodeName) {
		this.episodeName = episodeName;
	}

	public int getEpisodeNumber() {
		return episodeNumber;
	}

	public void setEpisodeNumber(final int episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	public String getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(final String firstAired) {
		this.firstAired = firstAired;
	}

	public List<String> getGuestStars() {
		return guestStars;
	}

	public void setGuestStars(final List<String> guestStars) {
		this.guestStars = guestStars;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(final String imdbId) {
		this.imdbId = imdbId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(final String overview) {
		this.overview = overview;
	}

	public String getProductionCode() {
		return productionCode;
	}

	public void setProductionCode(final String productionCode) {
		this.productionCode = productionCode;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(final String rating) {
		this.rating = rating;
	}

	public int getSeasonNumber() {
		return seasonNumber;
	}

	public void setSeasonNumber(final int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}

	public List<String> getWriters() {
		return writers;
	}

	public void setWriters(final List<String> writers) {
		this.writers = writers;
	}

	public String getAbsoluteNumber() {
		return absoluteNumber;
	}

	public void setAbsoluteNumber(final String absoluteNumber) {
		this.absoluteNumber = absoluteNumber;
	}

	public int getAirsAfterSeason() {
		return airsAfterSeason;
	}

	public void setAirsAfterSeason(final int airsAfterSeason) {
		this.airsAfterSeason = airsAfterSeason;
	}

	public int getAirsBeforeSeason() {
		return airsBeforeSeason;
	}

	public void setAirsBeforeSeason(final int airsBeforeSeason) {
		this.airsBeforeSeason = airsBeforeSeason;
	}

	public int getAirsBeforeEpisode() {
		return airsBeforeEpisode;
	}

	public void setAirsBeforeEpisode(final int airsBeforeEpisode) {
		this.airsBeforeEpisode = airsBeforeEpisode;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(final String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(final String seriesId) {
		this.seriesId = seriesId;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(final String seasonId) {
		this.seasonId = seasonId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getHash() {
		return hash;
	}

	public void setHash(final String hash) {
		this.hash = hash;
	}

	public EpisodeState getState() {
		return state;
	}

	public void setState(final EpisodeState state) {
		this.state = state;
	}

	public void update(final Episode that) {
		setAbsoluteNumber(that.absoluteNumber);
		setAirsAfterSeason(that.airsAfterSeason);
		setAirsBeforeEpisode(that.airsBeforeEpisode);
		setAirsBeforeSeason(that.airsBeforeSeason);
		setCombinedEpisodeNumber(that.combinedEpisodeNumber);
		setCombinedSeason(that.combinedSeason);
		getDirectors().clear();
		getDirectors().addAll(that.directors);
		setDvdChapter(that.dvdChapter);
		setDvdDiscId(that.dvdDiscId);
		setDvdEpisodeNumber(that.dvdEpisodeNumber);
		setDvdSeason(that.dvdSeason);
		setEpImgFlag(that.epImgFlag);
		setEpisodeName(that.episodeName);
		setEpisodeNumber(that.episodeNumber);
		setFilename(that.filename);
		setFirstAired(that.firstAired);
		getGuestStars().clear();
		getGuestStars().addAll(that.guestStars);
		setImdbId(that.imdbId);
		setLanguage(that.language);
		setLastUpdated(that.lastUpdated);
		setOverview(that.overview);
		setProductionCode(that.productionCode);
		setRating(that.rating);
		setSeasonId(that.seasonId);
		setSeasonNumber(that.seasonNumber);
		setSeriesId(that.seriesId);
		getWriters().clear();
		getWriters().addAll(that.writers);
	}
}
