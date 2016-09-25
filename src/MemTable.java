import java.util.concurrent.ConcurrentSkipListMap;

public class MemTable {
	private final ConcurrentSkipListMap<String, String> table=new ConcurrentSkipListMap<String, String>();
	
	public void put(String key,String value){
		table.put(key, value);
	}
	
	public void delete(String key){
		table.remove(key);
	}
	
	public String get(String key){
		return table.get(key);
	}

}
