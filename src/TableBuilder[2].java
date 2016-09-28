import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class TableBuilder {
	private static long CurrentSSTableFileNumber=0;
	private File file;
	private FileWriter fileWriter;
	
	
	TableBuilder(){
		file=new File(GetCurrentLogFileName());
		try {
			if(file.exists()&&file.isFile()){
				file.delete();
			}else{
				file.createNewFile();
			}
			CurrentSSTableFileNumber++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void add(String key,String value){
		try {
			fileWriter=new FileWriter(file,true);
			fileWriter.write(key+":"+value+"\r\n");
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String GetCurrentLogFileName(){
		return String.format("%06d", CurrentSSTableFileNumber)+".sst";
	}

}
