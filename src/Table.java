import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


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
		
	}
	
	public Slice getKey(InternalKey key){
		
		while(readEntry()!=null){
			if(new InternalKey(curEntry.getKey()).getUserKey().equals(key.getUserKey())){
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

        // read key
        Slice key =new Slice(sharedKeyLength + nonSharedKeyLength);
        if(curEntry!=null){
        	Slice curKey=curEntry.getKey();
        	byte[] sharedKey=curKey.getBytes(sharedKeyLength);
        	key.writeBytes(sharedKey);
        }
        key.writeBytes(table.getBytes(nonSharedKeyLength));

        // read value
        Slice value = new Slice(table.getBytes(valueLength));

        curEntry=new BlockEntry(key.offsetReset(), value.offsetReset());
        
        return curEntry;
	}

}
