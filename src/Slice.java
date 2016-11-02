import java.util.Arrays;

public class Slice {
	private byte[] data;
	private int offset;
	private int length;
	
	public Slice(int length)
    {
        data = new byte[length];
        this.offset = 0;
        this.length = length;
    }
	
	public Slice(byte[] data) {
		this.data = data;
		this.offset = 0;
		this.length = data.length;
	}
	
	public Slice wrapSlice(){
		Slice wrapSlice=new Slice(SizeOf.SIZE_OF_INT+offset);
		wrapSlice.setInt(offset);
		wrapSlice.writeBytes(getBytes(),offset);
		return wrapSlice;
	}
	
	public Slice offsetReset(){
		offset=0;
		return this;
	}
	
	public void setOffset(int offset){
		this.offset=offset;
	}
	
	public int getOffset(){
		return offset;
	}
	
	public int getlength(){
		return length;
	}
	
	
	public byte[] getBytes(){
		return data;
	}
	
	public byte[] getBytes(int length){
		byte[] bytes=new byte[length];
		System.arraycopy(data,offset,bytes,0,length);
		offset=offset+length;
		return bytes;
	}
	
	public byte[] getBytes(int index,int length){
		byte[] bytes=new byte[length];
		System.arraycopy(data,index,bytes,0,length);
		return bytes;
	}
	
	
	public int getInt(int index)
    {
		return (data[index] & 0xff) |
                (data[index + 1] & 0xff) << 8 |
                (data[index + 2] & 0xff) << 16 |
                (data[index + 3] & 0xff) << 24;
    }
	
	public int getInt()
    {
         return (data[offset++]& 0xff) |
                (data[offset++]& 0xff) << 8  |
                (data[offset++]& 0xff) << 16 |
                (data[offset++]& 0xff) << 24; 
    }
	
	//int强转byte,舍去高三位，保留最低的一位
	public void setInt(int value)
    {
        data[offset++] = (byte) (value);
        data[offset++] = (byte) (value >>> 8);
        data[offset++] = (byte) (value >>> 16);
        data[offset++] = (byte) (value >>> 24);
    }
	
	public void setInt(Integer index,int value)
    {
        data[index++] = (byte) (value);
        data[index++] = (byte) (value >>> 8);
        data[index++] = (byte) (value >>> 16);
        data[index++] = (byte) (value >>> 24);
    }
	
	public void setLong(long value)
    {
        data[offset++] = (byte) (value);
        data[offset++] = (byte) (value >>> 8);
        data[offset++] = (byte) (value >>> 16);
        data[offset++] = (byte) (value >>> 24);
        data[offset++] = (byte) (value >>> 32);
        data[offset++] = (byte) (value >>> 40);
        data[offset++] = (byte) (value >>> 48);
        data[offset++] = (byte) (value >>> 56);
    }
	
	public long getLong()
    {
        return  (long)data[offset++] |
        		(long)data[offset++] << 8  |
        		(long)data[offset++] << 16 |
        		(long)data[offset++] << 24 |
        		(long)data[offset++] << 32 |
        		(long)data[offset++] << 40 |
        		(long)data[offset++] << 48 |
        		(long)data[offset++] << 56 ;
    }
	
	
	
	public short getShort(int index)
    {
        return (short) ((data[index] & 0xff) |
                (data[index + 1] & 0xff) << 8);
    }
	
	public short getShort()
    {
        return (short) (data[offset++] | data[offset++] << 8);
    }
	
	public void setShort(short value)
    {
        data[offset++] = (byte) (value);
        data[offset++] = (byte) (value >>> 8);
    }
	
	public void writeSlice(Slice slice) {
		this.writeBytes(slice.getBytes());
	}
	
	public void writeByte(int value)
	{
		data[offset++]=(byte) (value);
	}
	
	public void writeByte(int value,int index)
	{
		data[++index]=(byte) (value);
	}
	
	public void writeBytes(byte[] value)
	{
		System.arraycopy(value,0,data,offset, value.length);
		offset+=value.length;
	}
	
	public void writeBytes(byte[] value,int length)
	{
		System.arraycopy(value,0,data,offset,length);
		offset+=length;
	}
	
	public void writeBytes(byte[] value,int startIndex,int length)
	{
		System.arraycopy(value,startIndex,data,offset,length);
		offset+=length;
	}
	
	public void readBytes(int index,int length)
	{

	}
	
	
	
	//返回变长存储整型的字节长度
	public int writeVariableLengthInt(int value)
    {
        int highBitMask = 0x80;
        if (value < (1 << 7) && value >= 0) {
            this.writeByte(value);
            return 1;
        }
        else if (value < (1 << 14) && value > 0) {
        	this.writeByte(value | highBitMask);
        	this.writeByte(value >>> 7);
        	return 2;
        }
        else if (value < (1 << 21) && value > 0) {
        	this.writeByte(value | highBitMask);
        	this.writeByte((value >>> 7) | highBitMask);
        	this.writeByte(value >>> 14);
        	return 3;
        }
        else if (value < (1 << 28) && value > 0) {
        	this.writeByte(value | highBitMask);
        	this.writeByte((value >>> 7) | highBitMask);
        	this.writeByte((value >>> 14) | highBitMask);
        	this.writeByte(value >>> 21);
        	return 4;
        }
        else {
        	this.writeByte(value | highBitMask);
        	this.writeByte((value >>> 7) | highBitMask);
        	this.writeByte((value >>> 14) | highBitMask);
            this.writeByte((value >>> 21) | highBitMask);
            this.writeByte(value >>> 28);
            return 5;
        }
    }
	
	public int readVariableLengthInt(){
		int result=0;
		for(int i=0;i<=5;i++){
			result+=((data[offset]&0x7f) << (i*7));
			if((data[offset++]&0x80)==0){
				return result;
			}
		}
		return result;
	}
	
	public int readVariableLengthInt(int index){
		int result=0;
		for(int i=0;i<=5;i++){
			result+=((data[index]&0x7f) << (i*7));
			if((data[index++]&0x80)==0){
				return result;
			}
		}
		return result;
	}
	
	public void writeLengthPrefixedBytes(byte[] value)
    {
		writeVariableLengthInt(value.length);
		writeBytes(value);
    }
	
	public boolean isReadable()
    {
        return available() > 0;
    }
	
	public int available()
	{
		return data.length-offset;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Slice slice = (Slice) o;

		// do lengths match
		if (length != slice.length) {
			return false;
		}
		
		for (int i = 0; i < length; i++) {
			if (data[i] != slice.data[i]) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Slice slice=new Slice(10);
//		slice.writeVariableLengthInt(65533);
		slice.writeVariableLengthInt(9);
		slice.offsetReset();
//		System.out.println(Arrays.toString(slice.data));
		System.out.println(slice.readVariableLengthInt());
//		System.out.println(slice.readVariableLengthInt());
		
//		Slice slice=new Slice(16);
//		slice.setInt(1256);
//		slice.offsetReset();
//		System.out.println(slice.getInt(0));

	}

}
