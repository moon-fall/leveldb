import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream.PutField;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DbImpl {
	private MemTable memTable;
	private MemTable immutableMemTable;
	private Log log;
	 
	private final ExecutorService compactionExecutor;
	private Future<?> backgroundCompaction;
	
	public DbImpl()
    {
		memTable=new MemTable();
		log=new Log();
		compactionExecutor = Executors.newSingleThreadExecutor();
    }
	
	public void put(String key,String value){
		if(memTable.approximateMemoryUsage()>=Options.writeBufferSize()){
			System.out.println("版本更新!"+memTable.approximateMemoryUsage());
			log=new Log();
			immutableMemTable=memTable;
			memTable=new MemTable();
			backgroundCompaction=compactionExecutor.submit(new Runnable(){
				@Override
				public void run() {
					System.out.println(Thread.currentThread()+" start!");
					writeLevel0Table(immutableMemTable);
					System.out.println(Thread.currentThread()+" complete!");
				}
				
			});
		}
		log.put(key, value);  
		memTable.put(key,value);
	}

	public String get(String key) {
		if(memTable.get(key)!=null){
			return memTable.get(key);
		}else if(immutableMemTable.get(key)!=null){
			return immutableMemTable.get(key);
		}
		return null;
	}
	
	private void writeLevel0Table(MemTable mem){
		TableBuilder tableBuilder = new TableBuilder();  
		Set<Entry<String,String>> entrySet=mem.getEntrySet();
		for(Entry entry : entrySet){
			tableBuilder.add((String)entry.getKey(), (String)entry.getValue());
		}
	}
}
