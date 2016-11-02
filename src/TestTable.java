import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class TestTable {
	
	static Slice lastKey=new Slice(new byte[]{});
	
	static Slice curKey=new Slice(new byte[]{});
	
	public static void add(Slice key,Slice value,Slice table){
		
		Slice slice = new Slice(key.getlength() + value.getlength()
				+ SizeOf.SIZE_OF_INT);
		int sharedlength = TableBuilder.calculateSharedBytes(key, lastKey);
		int unsharedlength = key.getlength() - sharedlength;
		
		System.out.println("unsharedlength:"+unsharedlength);
		
		slice.writeVariableLengthInt(sharedlength);
		slice.writeVariableLengthInt(unsharedlength);
		slice.writeVariableLengthInt(value.getlength());

		System.out.println("slice offset:" + slice.getOffset());
		
		slice.writeBytes(key.getBytes(), sharedlength, unsharedlength);
		
		System.out.println("slice offset:" + slice.getOffset());
		
		slice.writeBytes(value.getBytes());

		table.writeBytes(slice.getBytes(0, slice.getOffset()));
		
		System.out.println("table offset:" + table.getOffset());
		
		lastKey = key;

	}
	
	public static void readEntry(Slice table) {

		// read entry header
		int sharedKeyLength = table.readVariableLengthInt();
		int nonSharedKeyLength = table.readVariableLengthInt();
		int valueLength = table.readVariableLengthInt();

		System.out.println("sharedKeyLength:" + sharedKeyLength);
		System.out.println("nonSharedKeyLength:" + nonSharedKeyLength);
		System.out.println("valueLength:" + valueLength);

		// read key
		Slice key = new Slice(sharedKeyLength + nonSharedKeyLength);
		
	

		byte[] sharedKey = curKey.getBytes(sharedKeyLength);
		key.writeBytes(sharedKey);
		key.writeBytes(table.getBytes(nonSharedKeyLength));

		// read value
		Slice value = new Slice(table.getBytes(valueLength));

		curKey = key.offsetReset();

		System.out.println(Arrays.toString(key.getBytes()));
		System.out.println(Arrays.toString(value.getBytes()));
	}
	
	public static void main(String[] args) {
		Slice table=new Slice(1024);
		add(new Slice(new byte[]{1,2}),new Slice(new byte[]{1,2}),table);
		add(new Slice(new byte[]{1,2,3}),new Slice(new byte[]{1,2,3}),table);
		add(new Slice(new byte[]{1,2,3,4}),new Slice(new byte[]{1,2,3,4}),table);
		add(new Slice(new byte[]{1,2,3,4,5}),new Slice(new byte[]{1,2,3,4,5}),table);
		
		table.offsetReset();
		System.out.println(Arrays.toString(table.getBytes()));
		table.offsetReset();
		
		readEntry(table);
		readEntry(table);
		readEntry(table);
		readEntry(table);
		
		
		
		

	}

}
