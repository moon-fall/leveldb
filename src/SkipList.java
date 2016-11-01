import java.util.Random;

public class SkipList {
	final private int MAX_LAVEL=12;
	
	class Node{
		int data;
		Node right;
		Node down;
		Node(int data,Node right,Node down){
			data=data;
			right=right;
			down=down;
		}
	}
	
	class LevelHead{
		int level;
		Node right;
		LevelHead down;
		LevelHead(int level,Node right,Node down){
			level=level;
			right=right;
			down=down;
		}
	}
	
	
	public void Insert(int data){
		
	}
	
	public int randomLevel(){
		Random random=new Random(6);
		int r=random.nextInt(1<<12);
		int level=12;
		while(r>>1>0){
			r=r>>1;
			level--;
		}
		return level;
	}
	
	public static void main(String[] args){
		System.out.print(2<<12);
	}
	

}
