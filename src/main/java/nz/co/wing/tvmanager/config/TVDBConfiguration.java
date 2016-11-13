package nz.co.wing.tvmanager.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TVDBConfiguration {
	@Valid
	@NotNull
	private String apikey;

	@JsonProperty
	public String getApikey() {
		return apikey;
	}

	public void setApikey(final String apikey) {
		this.apikey = apikey;
	}
}
