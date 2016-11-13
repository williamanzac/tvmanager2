package nz.co.wing.tvmanager.config;

import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppConfiguration extends Configuration {
	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();
	@Valid
	@NotNull
	private TVDBConfiguration tvdb = new TVDBConfiguration();
	@Valid
	@NotNull
	@JsonProperty
	private final HttpClientConfiguration httpClient = new HttpClientConfiguration();
	@Valid
	@NotNull
	private UTorrentConfiguration uTorrent = new UTorrentConfiguration();
	@Valid
	@NotNull
	private FilesConfiguration files = new FilesConfiguration();
	@Valid
	@NotNull
	private XBMCConfiguration xbmc = new XBMCConfiguration();

	public HttpClientConfiguration getHttpClientConfiguration() {
		return httpClient;
	}

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(final DataSourceFactory factory) {
		database = factory;
	}

	@JsonProperty
	public TVDBConfiguration getTvdb() {
		return tvdb;
	}

	public void setTvdb(final TVDBConfiguration tvdb) {
		this.tvdb = tvdb;
	}

	@JsonProperty
	public UTorrentConfiguration getuTorrent() {
		return uTorrent;
	}

	public void setuTorrent(final UTorrentConfiguration uTorrent) {
		this.uTorrent = uTorrent;
	}

	@JsonProperty
	public FilesConfiguration getFiles() {
		return files;
	}

	public void setFiles(final FilesConfiguration files) {
		this.files = files;
	}

	@JsonProperty
	public XBMCConfiguration getXbmc() {
		return xbmc;
	}

	public void setXbmc(final XBMCConfiguration xbmc) {
		this.xbmc = xbmc;
	}
}
