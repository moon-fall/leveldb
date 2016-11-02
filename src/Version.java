import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Version {
	
	//private Long logNumber;
	private long sstFileNumber=0;
	
	private long manifestFileNumber = 1;
	
	private File file;
	
	private final List<FileMetaData> files=new CopyOnWriteArrayList<FileMetaData>();
	
	private long lastSequence;
	
	
	
	Version(){
		file=new File(GetCurrentManifestFileName());
		try {
			if(file.exists()&&file.isFile()){
				file.delete();
			}else{
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addFile(FileMetaData fileMetaData)
    {
		files.add(fileMetaData);
    }

	public long getLastSequence() {
		return lastSequence;
	}

	public void setLastSequence(long lastSequence) {
		this.lastSequence = lastSequence;
	}
	
	String GetCurrentManifestFileName(){
		return String.format("MANIFEST-%06d", manifestFileNumber);
	}
	
	public long getNextFileNumber()
    {
        return ++sstFileNumber;
    }
	
	public Slice get(InternalKey key) throws FileNotFoundException, IOException{
		List<FileMetaData> maybeFileList=new ArrayList<FileMetaData>();
		Slice value = null;
		
		
		
		for(FileMetaData file : files){
			System.out.println("FileName:"+file.getNumber());
			System.out.println("getSmallest:"+Arrays.toString(file.getSmallest().getUserKey().getBytes()));
			System.out.println("getLargest:"+Arrays.toString(file.getLargest().getUserKey().getBytes()));
			System.out.println("getlookup:"+Arrays.toString(key.getUserKey().getBytes()));
			
			if((key.compareTo(file.getSmallest())>=0)&&(key.compareTo(file.getLargest())<=0)){
				maybeFileList.add(file);
			}
		}
		
		System.out.println("maybeFileList:"+maybeFileList.size());
		
		for(FileMetaData file : maybeFileList){
			System.out.println("FileName:"+file.getNumber());
			 Table table=new Table(new FileInputStream(Util.tableFileName(file.getNumber())).getChannel());
			 value=table.getKey(key);
			 if(value!=null){
				 return value;
			 }
		}
		
		return value;
	}
	
	public void addSlice(Slice record){
		try {
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(file,true)));
			out.writeInt(record.getOffset());
			out.write(record.getBytes(),0,record.getOffset());
			out.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 public void logAndApply(){
		 
		 for(FileMetaData file : files){
			 Slice record=new Slice(file.getMetaDataLength());
			 record.setLong(file.getNumber());
			 record.setLong(file.getFileSize());
			 record.writeLengthPrefixedBytes(file.getSmallest().encode().getBytes());
			 record.writeLengthPrefixedBytes(file.getLargest().encode().getBytes());
			 addSlice(record);
		 }
	 }
	
	
}
