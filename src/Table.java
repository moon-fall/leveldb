import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;


public class Table {
	private final Slice table;
	private final FileChannel fileChannel;
	FileMetaData fileMetaData;
	
	BlockEntry curEntry;
	
	public Table(FileChannel fileChannel) throws IOException{
		
		long size = fileChannel.size();
		
		this.fileChannel = fileChannel;
		
		
		ByteBuffer buffer = ByteBuffer.allocate((int) size);
		fileChannel.read(buffer, 0);
		
		table=new Slice(buffer.array());
		
		//System.out.println(Arrays.toString(table.getBytes(5)));
		table.offsetReset();
		
	}
	
	public Slice getKey(InternalKey key){
		
		int i=0;
		
		while(readEntry()!=null){
			
			InternalKey curkey=new InternalKey(curEntry.getKey());
			
			if(i++<200){
			//System.out.println("curkey:"+Arrays.toString(curkey.getUserKey().getBytes()));
			}
			
			if(curkey.getUserKey().equals(key.getUserKey())){
				return curEntry.getValue();
			}
		}
		return null;
	}
	
	public BlockEntry getNextEntry(){
		curEntry=readEntry();
		return curEntry;
	}
	
	public BlockEntry readEntry(){
		
		if(!table.isReadable()){
			return  null;
		}
		
		// read entry header
        int sharedKeyLength = table.readVariableLengthInt();
        int nonSharedKeyLength = table.readVariableLengthInt();
        int valueLength = table.readVariableLengthInt();
        
        //System.out.println("sharedKeyLength:"+sharedKeyLength);
        //System.out.println("nonSharedKeyLength:"+nonSharedKeyLength);
        //System.out.println("valueLength:"+valueLength);

        // read key
        Slice key =new Slice(sharedKeyLength + nonSharedKeyLength);
        if(curEntry!=null){
        	
        	curEntry.getKey().offsetReset();
        	curEntry.getValue().offsetReset();
        	
        	Slice curKey=curEntry.getKey();
        	//System.out.println("Offset:"+curKey.getOffset());
        	
        	byte[] sharedKey=curKey.getBytes(sharedKeyLength);
        	//key.writeBytes(curEntry.getKey().getBytes(sharedKeyLength));
        	key.writeBytes(sharedKey);
        }
        key.writeBytes(table.getBytes(nonSharedKeyLength));

        // read value
        Slice value = new Slice(table.getBytes(valueLength));

        curEntry=new BlockEntry(key.offsetReset(), value.offsetReset());
        
        return curEntry;
	}
	
	public static void main(String args[]) throws FileNotFoundException, IOException{
		Table table=new Table(new FileInputStream("000000.sst").getChannel());
		
	}
	

}
