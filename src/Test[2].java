import java.util.BitSet;
import java.util.Random;


public class Test {
	public static void main(String args[]){
		DbImpl db=new DbImpl();
		Random random=new Random();
		
		for(int i = 0;i<1000000;i++){
			db.put(String.valueOf(random.nextInt()),"1");
		}
	}
}
