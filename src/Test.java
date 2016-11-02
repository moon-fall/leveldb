import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;


public class Test {
	public static void main(String args[]) throws FileNotFoundException, IOException{
		DbImpl db=new DbImpl();
		Random random=new Random(6);
		
		for(int i = 0;i<400000;i++){
			db.put(String.valueOf(i).getBytes(),String.valueOf(i).getBytes());
			//db.put(new byte[]{ 1,(byte) (i+1),2},String.valueOf(i).getBytes());
		}
		
		//db.writeLevel0Table();
		long t1=System.currentTimeMillis();
		System.out.println(new String(db.get(String.valueOf(random.nextInt(10000)).getBytes())));
		System.out.println(new String(db.get(String.valueOf(random.nextInt(10000)).getBytes())));
		System.out.println(new String(db.get(String.valueOf(random.nextInt(10000)).getBytes())));
		long t2=System.currentTimeMillis();
		System.out.println(t2-t1);
		//System.out.println(new String(db.get(new byte[]{1,4,2})));
	}
}
