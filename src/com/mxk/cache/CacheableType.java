package com.mxk.cache;

public enum CacheableType {

	CACHE_FOR_INSTER() {
		public String getString() {
			return "inster";
		}
	},
	CACHE_FOR_UPDATE() {
		public String getString() {
			return "update";
		}
	},
	CACHE_FOR_DELETE() {
		public String getString() {
			return "delete";
		}
	},
	CACHE_FOR_SELECT() {
		public String getString() {
			return "select";
		}
	};

	public abstract String getString();

}
