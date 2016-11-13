package nz.co.wing.tvmanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.omertron.thetvdbapi.model.BannerListType;
import com.omertron.thetvdbapi.model.BannerType;

@Entity
@Table(name = "banner")
// @JsonInclude(Include.NON_DEFAULT)
public class Banner {
	@Id
	private int id;
	@Column
	private String url;
	@Column
	private BannerListType bannerType;
	@Column
	private BannerType bannerType2;
	@Column
	private String colours;
	@Column
	private Float rating;
	@Column
	private int ratingCount;
	@Column
	private String language;
	@Column
	private boolean seriesName;
	@Column
	private String thumb;
	@Column
	private String vignette;
	@Column
	private int season = 0;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public BannerListType getBannerType() {
		return bannerType;
	}

	public void setBannerType(final BannerListType bannerType) {
		this.bannerType = bannerType;
	}

	public BannerType getBannerType2() {
		return bannerType2;
	}

	public void setBannerType2(final BannerType bannerType2) {
		this.bannerType2 = bannerType2;
	}

	public String getColours() {
		return colours;
	}

	public void setColours(final String colours) {
		this.colours = colours;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(final Float rating) {
		this.rating = rating;
	}

	public int getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(final int ratingCount) {
		this.ratingCount = ratingCount;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public boolean isSeriesName() {
		return seriesName;
	}

	public void setSeriesName(final boolean seriesName) {
		this.seriesName = seriesName;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(final String thumb) {
		this.thumb = thumb;
	}

	public String getVignette() {
		return vignette;
	}

	public void setVignette(final String vignette) {
		this.vignette = vignette;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(final int season) {
		this.season = season;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
