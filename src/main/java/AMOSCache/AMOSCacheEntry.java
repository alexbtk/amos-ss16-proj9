package AMOSCache;

import java.util.Map;

public class AMOSCacheEntry {
	private Object value;
	private Map<?,?> map;
	private long timestamp;
	
	AMOSCacheEntry(Object value){
		this.map = null;
		this.value = value;
		this.timestamp = System.currentTimeMillis();
	}
	
	AMOSCacheEntry(Map<?,?> value){
		this.map = value;
		this.value = null;
		this.timestamp = System.currentTimeMillis();
	}
	
	AMOSCacheEntry(Object value, long timestamp){
		this.map = null;
		this.value = value;
		this.timestamp = timestamp;
	}
	
	AMOSCacheEntry(Map<?, ?> value, long timestamp){
		this.map = value;
		this.value = null;
		this.timestamp = timestamp;
	}

	public Object getValue() {
		return value;
	}
	
	public Map<?,?> getMap(){
		return map;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
