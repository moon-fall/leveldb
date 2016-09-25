import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream.PutField;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DbImpl {
	private final MemTable memTable=new MemTable();
	private final Log log=new Log();
	
	private final ExecutorService compactionExecutor;
	
	public DbImpl()
    {
		compactionExecutor = Executors.newSingleThreadExecutor();
    }
	
	public void put(String key,String value){	
		log.put(key, value);
		memTable.put(key, value);
	}

	public String get(String key) {
		return memTable.get(key);
	}
	
	
}
