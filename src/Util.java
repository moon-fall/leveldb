

public class Util {
	static int min(int a,int b){
		return a<b?a:b;
	}
	
	static String makeFileName(long number, String suffix)
    {
        return String.format("%06d.%s", number, suffix);
    }
	
    public static String logFileName(long number)
    {
        return makeFileName(number, "log");
    }

    public static String tableFileName(long number)
    {
        return makeFileName(number, "sst");
    }
}
