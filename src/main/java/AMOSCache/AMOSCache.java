package AMOSCache;

public abstract class AMOSCache {
	private static AMOSCache cache = null;
	
	public static AMOSCache getInstance(){
		if(cache == null){
			synchronized(AMOSCache.class){
				if(cache == null){
					System.out.println("New cache instance...");
					cache = new AMOSCacheImpl();
				}
			}
		}
		
		return cache;
	}

	public abstract Object getCurrentMethodCache(Object... args);
	public abstract void putCurrentMethodCache(Object... args);
	public abstract String toString();
	public abstract void setCache(String dump);

	public abstract void put(String key, Object result);
}
