import java.io.File;
import java.io.FileNotFoundException;
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
	private final Version version=new Version();
	 
	private final ExecutorService compactionExecutor;
	private Future<?> backgroundCompaction;
	
	public DbImpl()
    {
		memTable=new MemTable();
		log=new Log();
		compactionExecutor = Executors.newSingleThreadExecutor();
    }
	
	public void put(byte[] key,byte[] value){
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
					version.logAndApply();
					System.out.println(Thread.currentThread()+" complete!");
				}
				
			});
		}
		long sequenceBegin = version.getLastSequence() + 1;
		version.setLastSequence(sequenceBegin+1);
		log.addRecord(new WriteBatchImpl(key,value),sequenceBegin);
		memTable.put(key,value,sequenceBegin);
	}
	
	private Slice writeWriteBatch(WriteBatchImpl writeBatchImpl,String key,String value, long sequenceBegin)
    {
        Slice record = new Slice(SizeOf.SIZE_OF_LONG + SizeOf.SIZE_OF_INT+key.length()+value.length()+10);
        record.setLong(sequenceBegin);
        record.setInt(1);
        record.writeVariableLengthInt(key.length());
        record.writeBytes(key.getBytes());
        record.writeVariableLengthInt(value.length()); 
        record.writeBytes(value.getBytes());
        return record;
    }
	
	public byte[] get(byte[] key) throws FileNotFoundException, IOException{
		InternalKey lookupKey=new InternalKey(new Slice(key),version.getLastSequence());
		Slice result = null;
		if(memTable!=null){
			result=memTable.get(lookupKey);
		}
		
		if(result==null){
			if(immutableMemTable!=null){
				result=immutableMemTable.get(lookupKey);
			}	
		}
		
		if(result==null){
			result=version.get(lookupKey);
		}
		
		if(result!=null){
			return result.getBytes();
		}
		
		return null;
		
	}
	
	
//	public Slice get(InternalKey key) throws FileNotFoundException, IOException {
//		if(memTable.get(key)!=null){  
//			return memTable.get(key);
//		}else if(immutableMemTable.get(key)!=null){
//			return immutableMemTable.get(key);
//		}
//		
//		return version.get(key);
//	}
	
	private void writeLevel0Table(MemTable mem){
		long nextFileNumber=version.getNextFileNumber();
		TableBuilder tableBuilder = new TableBuilder(nextFileNumber);  
		Set<Entry<InternalKey,Slice>> entrySet=mem.getEntrySet();
		for(Entry<InternalKey,Slice> entry : entrySet){
			tableBuilder.add(entry.getKey().encode(), entry.getValue());
		}
		FileMetaData fileMetaData = new FileMetaData(nextFileNumber,
				tableBuilder.getFileSize(), mem.getFirstKey(), mem.getLastKey());
		version.addFile(fileMetaData);
	}
	
	public void writeLevel0Table(){
		writeLevel0Table(memTable);
	}
	
	
}
