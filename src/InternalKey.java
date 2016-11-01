

public class InternalKey implements Comparable<InternalKey>{
	private final Slice userKey;
    private final long sequenceNumber;
    //private final ValueType valueType;

    public InternalKey(Slice userKey, long sequenceNumber)
    {
        this.userKey = userKey;
        this.sequenceNumber = sequenceNumber;
        //this.valueType = valueType;
    }
    
    public InternalKey(Slice InternalKey)
    {
    	this.userKey=new Slice(InternalKey.getBytes(InternalKey.getlength()-8));
        this.sequenceNumber = InternalKey.getLong();
        //this.valueType = valueType;
    }
    
    public Slice getUserKey(){
    	return userKey;
    }
    
    public Slice encode()
    {
        Slice slice = new Slice(userKey.getlength()+SizeOf.SIZE_OF_LONG);
        slice.writeSlice(userKey);
        slice.setLong(sequenceNumber);
        return slice;
    }
    
	@Override
	public int compareTo(InternalKey o) {
		int minLength=Util.min(userKey.getlength(),o.userKey.getlength());

		for(int index=0;index<minLength;index++){
			if(userKey.getBytes()[index]!=o.userKey.getBytes()[index]){
				return userKey.getBytes()[index]-o.userKey.getBytes()[index];
			}
		}
		if(userKey.getlength()!=o.userKey.getlength()){
			return userKey.getlength()-o.userKey.getlength();
		}
		
		return (int)(o.sequenceNumber-sequenceNumber);
	}

}
