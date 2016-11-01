import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;


public class MemTable {
	private final ConcurrentSkipListMap<InternalKey, Slice> table=new ConcurrentSkipListMap<InternalKey, Slice>();
	
	private long approximateMemoryUsage=0l;
	
	public long approximateMemoryUsage()
    {
        return approximateMemoryUsage;
    }
	
	public void put(byte[] key,byte[] value,long sequence){
		table.put(new InternalKey(new Slice(key),sequence), new Slice(value));
		approximateMemoryUsage=approximateMemoryUsage+key.length+value.length+8;
	}
	
	public void delete(String key){
		table.remove(key);
	}
	
	public boolean containsKey(InternalKey internalKey){
		return table.containsKey(internalKey);
	}

	
	public Set<Entry<InternalKey,Slice>> getEntrySet(){
		return table.entrySet();
	}

	
	public Slice get(InternalKey key) {
		//返回table中key值大于等于key的第一个entry,即userKey大于等于key.userKey,sequenceNumber小于等于key.sequenceNumber
		Entry<InternalKey, Slice> entry=table.ceilingEntry(key);
		if (entry == null) {
            return null;
        }
		
		InternalKey entryKey = entry.getKey();
		
		if (entryKey.getUserKey().equals(key.getUserKey())) {
			return entry.getValue();
		}
		
		return null;
	}

	public InternalKey getFirstKey() {
		return table.firstKey();
	}
	
	public InternalKey getLastKey() {
		return table.lastKey();
	}
	
}
