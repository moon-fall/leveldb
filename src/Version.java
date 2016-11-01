import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Version {
	
	//private Long logNumber;
	
	private long manifestFileNumber = 1;
	
	private File file;
	
	private final List<FileMetaData> files=new ArrayList<FileMetaData>();
	
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
	
	public Slice get(InternalKey key) throws FileNotFoundException, IOException{
		List<FileMetaData> maybeFileList=new ArrayList<FileMetaData>();
		Slice value = null;
		
		for(FileMetaData file : files){
			if((key.compareTo(file.getSmallest())>=0)&&(key.compareTo(file.getLargest())<=0)){
				maybeFileList.add(file);
			}
		}
		
		System.out.println("maybeFileList:"+maybeFileList.size());
		
		for(FileMetaData file : maybeFileList){
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
