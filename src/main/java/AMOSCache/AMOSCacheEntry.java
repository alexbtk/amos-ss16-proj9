package AMOSCache;

public class AMOSCacheEntry {
	private Object value;
	private long timestamp;
	
	AMOSCacheEntry(Object value){
		this.value = value;
		this.timestamp = System.currentTimeMillis();
	}
	
	AMOSCacheEntry(Object value, long timestamp){
		this.value = value;
		this.timestamp = timestamp;
	}

	public Object getValue() {
		return value;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
