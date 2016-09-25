import java.util.BitSet;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;


public class Bloom {
	private static final int DEFAULT_SIZE = Integer.MAX_VALUE;
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	
	public void add(String value) {
		bits.set(value.hashCode(), true);
	}
	
	public boolean contains(String value) {
		return bits.get(value.hashCode());
	}
	
	public static void main(String args[]){
		Bloom bloom=new Bloom();
		bloom.add("a");
		
		System.out.println(bloom.contains("a"));
		System.out.println(bloom.contains("b"));
		
	}
	
	
}
