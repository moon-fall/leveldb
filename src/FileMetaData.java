
public class FileMetaData {
	
	private final long number;
	
	private final long fileSize;
	
	private final String smallest;
	
	private final String largest;
	
	public long getNumber() {
		return number;
	}
	
	public long getFileSize() {
		return fileSize;
	}

	public String getSmallest() {
		return smallest;
	}

	public String getLargest() {
		return largest;
	}
	
	public FileMetaData(long number, long fileSize, String smallest,
			String largest) {
		this.number = number;
		this.fileSize = fileSize;
		this.smallest = smallest;
		this.largest = largest;
	}
}
