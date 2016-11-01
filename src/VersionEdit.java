import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class VersionEdit implements Serializable {
	
	private Long logNumber;
	
	private final List<FileMetaData> newFiles =new ArrayList<FileMetaData>();
	
	
	private static long CurrentLogFileNumber=0;
	private File file;
	private FileWriter fileWriter;
	
	public void writeEdit(String key,String value){
		try {
			fileWriter=new FileWriter(file,true);
			fileWriter.write("1 "+logNumber+",2");
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public void addFile(FileMetaData fileMetaData){
		newFiles.add(fileMetaData);
	}
	
	@Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("1 ").append(logNumber);   
        for(FileMetaData fileMetaData : newFiles){
        	sb.append(",2");
        	sb.append(" "+fileMetaData.getNumber());
        	sb.append(" "+fileMetaData.getFileSize());
        	sb.append(" "+fileMetaData.getSmallest());
        	sb.append(" "+fileMetaData.getLargest());
        	
        }
        sb.append(", newFiles=").append(newFiles);
        sb.append('}');
        return sb.toString();
    }
	
	
}
