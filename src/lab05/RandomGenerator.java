package lab05;

import java.util.Random;

public class RandomGenerator {
	private Random random;
	private int[] array;
	
	public RandomGenerator(){
		random = new Random();
		array = new int[]{0, 1, 0, 1, 1, 1, 1, 0, 1, 1};
	}
	
	public int getNumber() {
		int next = random.nextInt(10);
		return array[next];
	}
			
}
