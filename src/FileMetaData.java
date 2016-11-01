
public class FileMetaData  {
	
	private final long number;
	
	private final long fileSize;
	
	private final InternalKey smallest;
	
	private final InternalKey largest;
	
	private final int metaDataLength;
	
	public long getNumber() {
		return number;
	}
	
	public long getFileSize() {
		return fileSize;
	}

	public InternalKey getSmallest() {
		return smallest;
	}

	public InternalKey getLargest() {
		return largest;
	}
	
	public int getMetaDataLength() {
		return metaDataLength;
	}
	
	public FileMetaData(long number, long fileSize, InternalKey smallest,
			InternalKey largest) {
		this.number = number;
		this.fileSize = fileSize;
		this.smallest = smallest;
		this.largest = largest;
		this.metaDataLength = SizeOf.SIZE_OF_VAR_LONG_MAX * 2
				+ SizeOf.SIZE_OF_VAR_INT_MAX * 2
				+ smallest.getUserKey().getlength()
				+ largest.getUserKey().getlength();
	}
}
