import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemTable {
	private final ConcurrentSkipListMap<String, String> table=new ConcurrentSkipListMap<String, String>();
	
	private long approximateMemoryUsage=0l;
	
	public long approximateMemoryUsage()
    {
        return approximateMemoryUsage;
    }
	
	public void put(String key,String value){
		table.put(key, value);
		approximateMemoryUsage=approximateMemoryUsage+key.length()+value.length()+8;
	}
	
	public void delete(String key){
		table.remove(key);
	}
	
	
	public Set<Entry<String,String>> getEntrySet(){
		return table.entrySet();
	}

	public String get(String key) {
		return table.get(key);
	}

}
