import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


public class Log {
	private static long CurrentLogFileNumber=0;
	private File file;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
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

	public void addRecord(WriteBatchImpl record,long sequenceBegin){
		try {
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(file,true)));
			out.writeInt(SizeOf.SIZE_OF_LONG+SizeOf.SIZE_OF_INT+record.getSize());
			out.writeLong(sequenceBegin);
			out.writeInt(1);
			out.write(record.getBatch().getBytes(),0,record.getSize());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Slice readRecord(){
		byte[] b = null;
		DataInputStream in;
		try {
			in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(file)));
			int length = in.readInt();
			b=new byte[length];
			System.out.println(length);
			long sequence = in.readLong();
			System.out.println(sequence);
			int count = in.readInt();
			System.out.println(count);
			in.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Slice batch=new Slice(b);		
		return batch;
	}
	
	public static void main(String [] args){
		Log log=new Log();
		byte[] key={1,2,3};
		byte[] value={4,5,6};
		log.addRecord(new WriteBatchImpl(key,value),1);
		Slice batch=log.readRecord();
		int keylength=batch.readVariableLengthInt();
		System.out.println(Arrays.toString(batch.getBytes(keylength)));
		int valuelength=batch.readVariableLengthInt();
		System.out.println(Arrays.toString(batch.getBytes(valuelength)));
	}

}
