package nz.co.wing.tvmanager.model.xbmc;

public class RPCResponse<T> {
	private String jsonrpc = "2.0";
	private int id = 1;
	private T result;

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(final String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public T getResult() {
		return result;
	}

	public void setResult(final T result) {
		this.result = result;
	}
}
