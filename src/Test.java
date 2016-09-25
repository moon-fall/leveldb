import java.util.BitSet;
import java.util.Random;


public class Test {
	public static void main(String args[]){
		DbImpl db=new DbImpl();
		for(int i = 0;i<1000;i++){
			db.put(String.valueOf(i),"1");
		}
	}
}
