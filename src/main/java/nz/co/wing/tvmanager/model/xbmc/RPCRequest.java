package nz.co.wing.tvmanager.model.xbmc;

import java.util.HashMap;
import java.util.Map;

public class RPCRequest {
	private String jsonrpc = "2.0";
	private String method;
	private final Map<String, Object> params = new HashMap<>();
	private int id = 1;

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(final String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(final String method) {
		this.method = method;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void addParam(final String key, final Object value) {
		params.put(key, value);
	}
}
