package nz.co.wing.tvmanager.config;

import java.io.File;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilesConfiguration {
	@Valid
	@NotNull
	private File torrentDir;
	@Valid
	@NotNull
	private File intermediateDir;
	@Valid
	@NotNull
	private File destinationDir;

	@JsonProperty
	public File getTorrentDir() {
		return torrentDir;
	}

	public void setTorrentDir(final File torrentDir) {
		this.torrentDir = torrentDir;
	}

	@JsonProperty
	public File getIntermediateDir() {
		return intermediateDir;
	}

	public void setIntermediateDir(final File intermediateDir) {
		this.intermediateDir = intermediateDir;
	}

	@JsonProperty
	public File getDestinationDir() {
		return destinationDir;
	}

	public void setDestinationDir(final File destinationDir) {
		this.destinationDir = destinationDir;
	}
}
