import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Log {
	private long CurrentLogFileNumber=0;
	private File file=new File("log");
	private FileWriter fileWriter;
	
	Log(){
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
