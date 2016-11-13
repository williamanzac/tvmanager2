package nz.co.wing.tvmanager.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UTorrentConfiguration {
	@Valid
	@NotNull
	private String username;
	@Valid
	@NotNull
	private String password;
	@Valid
	@NotNull
	private String api;

	@JsonProperty
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@JsonProperty
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@JsonProperty
	public String getApi() {
		return api;
	}

	public void setApi(final String api) {
		this.api = api;
	}
}
