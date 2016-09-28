import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.annotation.Generated;


public class Log {
	private static long CurrentLogFileNumber=0;
	private File file;
	private FileWriter fileWriter;
	
	Log(){
		file=new File(GetCurrentLogFileName());
		try {
			if(file.exists()&&file.isFile()){
				file.delete();
			}else{
				file.createNewFile();
			}
		CurrentLogFileNumber++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String GetCurrentLogFileName(){
		return String.format("%06d", CurrentLogFileNumber)+".log";
	}
	
	
	public void put(String key,String value){
		try {
			fileWriter=new FileWriter(file,true);
			fileWriter.write(key+":"+value+"\r\n");
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		new Log().put("1","1");

	}

}
