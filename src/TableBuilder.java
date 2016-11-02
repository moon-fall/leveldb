import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;


public class TableBuilder {
	private File file;
	private Slice lastKey=new Slice(new byte[]{});
	private OutputStream out = null;
	
	TableBuilder(long SSTableFileNumber){
		file=new File(Util.tableFileName(SSTableFileNumber));
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
	
	public void add(Slice key,Slice value){
		try {
			out = new FileOutputStream(file,true);
			Slice slice=new Slice(key.getlength()+value.getlength()+SizeOf.SIZE_OF_INT);
			int sharedlength=calculateSharedBytes(key,lastKey);
			int unsharedlength=key.getlength()-sharedlength;
			
			//System.out.println("sharedlength:"+sharedlength);
			//System.out.println("unsharedlength:"+unsharedlength);
			//System.out.println("valuelength:"+value.getlength());
			
			
			slice.writeVariableLengthInt(sharedlength);
			slice.writeVariableLengthInt(unsharedlength);
			slice.writeVariableLengthInt(value.getlength());
			
			//System.out.println("sliceoffset:"+slice.getOffset());
			
			slice.writeBytes(key.getBytes(), sharedlength, unsharedlength);
			
			//System.out.println("sliceoffset:"+slice.getOffset());
			
			slice.writeBytes(value.getBytes());
			
			//System.out.println("sliceoffset:"+slice.getOffset());
			
			//System.out.println("slicelength:"+slice.getBytes(0,slice.getOffset()).length);
			
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
	
	long getFileSize(){
		return file.length();
	}
	
	public static void main(String args[]){
//		int a = calculateSharedBytes(new Slice(new byte[]{12,32,23,54}), new Slice(new byte[]{12,32,34}));
//		System.out.println(a);
		
		TableBuilder tb=new TableBuilder(3254315);
		tb.add(new Slice(new byte[]{3}), new Slice(new byte[]{1}));
		tb.add(new Slice(new byte[]{2,3}), new Slice(new byte[]{1}));
		tb.add(new Slice(new byte[]{5,2}), new Slice(new byte[]{1}));
		tb.add(new Slice(new byte[]{6}), new Slice(new byte[]{1}));
		
	}

}
