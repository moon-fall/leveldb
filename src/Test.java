import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;


public class Test {
	public static void main(String args[]) throws FileNotFoundException, IOException{
		DbImpl db=new DbImpl();
		Random random=new Random();
		
		for(int i = 0;i<200000;i++){
			db.put(String.valueOf(i).getBytes(),String.valueOf(i).getBytes());
		}
		
		System.out.println(new String(db.get(String.valueOf(1000).getBytes())));
	}
}
