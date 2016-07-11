package AMOSCache;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import AMOSTwitterBluemix.TwitterBluemixPost;

public class AMOSCacheImpl extends AMOSCache {
	
	private Map<String, AMOSCacheEntry> cacheMap;
	private final long CACHE_TIME = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
	
	AMOSCacheImpl(){
		cacheMap = new ConcurrentHashMap<String, AMOSCacheEntry>();
	}

	public void put(String key, Object result) {
		
		if(result instanceof Map<?,?>){
			System.out.println("Put map with size: " + ((Map<String, String>)result).size());
			cacheMap.put(key, new AMOSCacheEntry(new ConcurrentHashMap<String, String>((Map<String, String>)result)));
		}
		else if(key.contains("TwitterBluemixCrawler+crawlPosts"))
			cacheMap.put(key, new AMOSCacheEntry(new ArrayList<TwitterBluemixPost>((List<TwitterBluemixPost>)result)));
		else if(result instanceof List<?>)
			cacheMap.put(key, new AMOSCacheEntry(new ArrayList<String>((List<String>)result)));
		else
			cacheMap.put(key, new AMOSCacheEntry(result));
	}

	public Object get(String key) {
		
		if(cacheMap.containsKey(key)){
			AMOSCacheEntry entry = cacheMap.get(key);
			if((System.currentTimeMillis()-entry.getTimestamp()) > CACHE_TIME){
				cacheMap.remove(key);
			}
			else
				// temporal solution
				/*if((entry.getValue() instanceof Map<?, ?> && ((Map<?,?>)entry.getValue()).size() < 1 )
				  || (entry.getValue() instanceof List<?> && ((List<?>)entry.getValue()).size() < 1 ))
					return null;*/
				
			
				//System.out.println("Return cache entry...");
				
				if(entry.getMap() != null)
					return new HashMap<String, String>((Map<String, String>)entry.getMap());
				else
					return entry.getValue();
		
		}
		
		return null;
	}

	@Override
	public Object getCurrentMethodCache(Object... args) {
		String key = Thread.currentThread().getStackTrace()[2].getClassName() + "+" + Thread.currentThread().getStackTrace()[2].getMethodName();
		
		for(Object arg : args){
			key = key + "+" + arg.toString();
		}
		
		System.out.println("getcurrentMethodCache: " + key);
		
		if(cacheMap.containsKey(key)){
			System.out.println("Contains key...");
			return this.get(key);
		}
		else
			return null;
	}

	@Override
	public synchronized void putCurrentMethodCache(Object... args) {
		String key = Thread.currentThread().getStackTrace()[2].getClassName() + "+" + Thread.currentThread().getStackTrace()[2].getMethodName();
		
		for(int i = 0; i < args.length-1; i++){
			key = key + "+" + args[i].toString();
		}
		
		this.put(key, args[args.length-1]);
	}

	@Override
	public String toString() {
		String result = "";
		
		for(Entry<String, AMOSCacheEntry> entry : this.cacheMap.entrySet()){
			result = result + "###" + entry.getKey() + ";;;";
			if(entry.getKey().equals("AMOSDBPedia.DBpedia+getCompanyLocationCoordonates+Apple Inc."))
				System.out.println("Got coordonates cache entry");
			Object val = entry.getValue().getValue();
			//format lists
			if(val instanceof List<?>){
				List<?> l = (List<?>)val;
				for(int i = 0; i < l.size(); i++){
					if(i == l.size()-1)
						result = result + l.get(i).toString();
					else
						result = result + l.get(i).toString() + ",,,";
				}
			}
			else if(entry.getValue().getMap() != null){
				Map<?, ?> map = entry.getValue().getMap();
				int i = 0;
				int size = map.entrySet().size();
				System.out.println("Coordonate entries size: " + size + "; Map size: " + map.size());
				for(Entry<?, ? > e : map.entrySet()){
					if(i == size-1)
						result = result + e.getKey().toString() + "===" + e.getValue().toString();
					else
						result = result + e.getKey().toString() + "===" + e.getValue().toString() + ",,,";
					
					i++;
				}
					
			}
			else{
				result = result + val.toString();
			}
			
			result = result + ";;;" + entry.getValue().getTimestamp() + "\n";
		}
		
		return result;
	}

	@Override
	public synchronized void setCache(String dump) {
		System.out.println(Charset.defaultCharset().name());
		String[] lines = dump.split("###");
		this.cacheMap.clear();
		
		for(String line : lines){
			String[] split = line.split(";;;");
			if(split.length > 2){
				String key = split[0];
				long timestamp = Long.parseLong(split[2].replaceAll("[^\\d.]", ""));
				
				String[] classInfo = key.split("\\+");
				if(classInfo.length < 2)
					continue;
				
				Method method = null;
				try {
					for(Method m : Class.forName(classInfo[0]).getMethods()){
						if(m.getName().equals(classInfo[1])){
							method = m;
							break;
						}
					}
				} catch (SecurityException e) {
					continue;
				} catch (ClassNotFoundException e) {
					continue;
				}
				
				if(method == null)
					continue;
				
				Class<?> returnType = method.getReturnType();
				try{
					if(returnType.equals(String.class)){
						this.cacheMap.put(key, new AMOSCacheEntry(split[1], timestamp));
					}
					else if(returnType.equals(boolean.class)){
						this.cacheMap.put(key, new AMOSCacheEntry(Boolean.parseBoolean(split[1]), timestamp));
					}
					else if(returnType.equals(double.class)){
						//System.out.println("Setting double: " + key + " " + Double.parseDouble(split[1]));
						this.cacheMap.put(key, new AMOSCacheEntry(Double.parseDouble(split[1]), timestamp));
					}
					else if(returnType.equals(List.class)){
						
						//check for list of twitter posts
						if(method.getName() == "crawlPosts"){
							String[] listElems = split[1].split(",,,");
							List<TwitterBluemixPost> list = new ArrayList<TwitterBluemixPost>();
							for(String s : listElems){
								TwitterBluemixPost post = TwitterBluemixPost.parse(s);
								if(post != null)
									list.add(post);
							}

							this.cacheMap.put(key, new AMOSCacheEntry(new ArrayList<TwitterBluemixPost>(list), timestamp));
						
						}
						//Assumes that List is List of Strings otherwise
						else{
							String[] listElems = split[1].split(",,,");
							List<String> list = new ArrayList<String>();
							for(String s : listElems)
								list.add(s);

							this.cacheMap.put(key, new AMOSCacheEntry(new ArrayList<String>(list), timestamp));
						}
					}
					//Assumes Map<String, String>
					else if(returnType.equals(Map.class)){
						String[] mapEntries = split[1].split(",,,");
						Map<String, String> map = new HashMap<String, String>();
						
						for(String s : mapEntries){
							String[] entry = s.split("===");
							if(entry.length < 2)
								continue;
							else
								map.put(entry[0], entry[1]);
						}
						
						this.cacheMap.put(key, new AMOSCacheEntry(new ConcurrentHashMap<String, String>((Map<String, String>)map), timestamp));
					}
					else{
						System.out.println("Unsupported return type: " + returnType.getName());
					}
				} catch(Exception e){
					e.printStackTrace();
					continue;
				}
			}
		}
		System.out.println("Done...");
		System.out.println(this.cacheMap.toString());
		
	}

}
