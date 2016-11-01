
public class WriteBatchImpl {
	private Slice batch;
	private int size;
	
	public int getSize(){
		return size;
	}
	
	public Slice getBatch(){
		return batch;
	}
	
	public WriteBatchImpl(byte[] key, byte[] value)
    {
		batch=new Slice(key.length+value.length+10);
		int keyLength=batch.writeVariableLengthInt(key.length);
		batch.writeBytes(key);
		int valueLength=batch.writeVariableLengthInt(value.length); 
		batch.writeBytes(value);
		size=keyLength+key.length+valueLength+value.length;
    }
}
