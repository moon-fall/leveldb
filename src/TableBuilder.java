import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;


public class TableBuilder {
	private static long CurrentSSTableFileNumber=0;
	private File file;
	private Slice lastKey=new Slice(new byte[]{});
	private OutputStream out = null;
	
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
	
	public void add(Slice key,Slice value){
		try {
			out = new FileOutputStream(file,true);
			Slice slice=new Slice(key.getlength()+value.getlength()+SizeOf.SIZE_OF_INT);
			int sharedlength=calculateSharedBytes(key,lastKey);
			int unsharedlength=key.getlength()-sharedlength;
			slice.writeVariableLengthInt(sharedlength);
			slice.writeVariableLengthInt(unsharedlength);
			slice.writeVariableLengthInt(value.getlength());
			
			slice.writeBytes(key.getBytes(), sharedlength, unsharedlength);
			slice.writeBytes(value.getBytes());
			
			out.write(slice.getBytes(0,slice.getOffset()));
			out.close();
			lastKey=key;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int calculateSharedBytes(Slice leftKey, Slice rightKey) {
		int sharedlength = 0;
		//先判断公共字节长度是否小于最小长度,在判断该字节是否相等
		while (sharedlength < Util.min(leftKey.getlength(),
				rightKey.getlength())
				&& leftKey.getBytes()[sharedlength] == rightKey.getBytes()[sharedlength]) {
			sharedlength++;
		}
		return sharedlength;
	}

	String GetCurrentLogFileName(){
		return String.format("%06d", CurrentSSTableFileNumber)+".sst";
	}
	
	long GetCurrentLogFileNumber(){
		return CurrentSSTableFileNumber;
	}
	
	long getFileSize(){
		return file.length();
	}
	
	public static void main(String args[]){
//		int a = calculateSharedBytes(new Slice(new byte[]{12,32,23,54}), new Slice(new byte[]{12,32,34}));
//		System.out.println(a);
		
		TableBuilder tb=new TableBuilder();
		tb.add(new Slice(new byte[]{3}), new Slice(new byte[]{1}));
		tb.add(new Slice(new byte[]{2,3}), new Slice(new byte[]{1}));
		tb.add(new Slice(new byte[]{5,2}), new Slice(new byte[]{1}));
		tb.add(new Slice(new byte[]{6}), new Slice(new byte[]{1}));
		
	}

}
