package nz.co.wing.tvmanager.model.xbmc;

public class List {
	public static class Limit {
		private int end = -1;
		private int start = 0;

		public int getEnd() {
			return end;
		}

		public void setEnd(final int end) {
			this.end = end;
		}

		public int getStart() {
			return start;
		}

		public void setStart(final int start) {
			this.start = start;
		}
	}

	public static class Sort {
		private String order = "ascending";
		private boolean ignorearticle = false;
		private String method = "none";

		public String getOrder() {
			return order;
		}

		public void setOrder(final String order) {
			this.order = order;
		}

		public boolean isIgnorearticle() {
			return ignorearticle;
		}

		public void setIgnorearticle(final boolean ignorearticle) {
			this.ignorearticle = ignorearticle;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(final String method) {
			this.method = method;
		}
	}

	public static class Item {
		public static class Base {

		}
	}
}
