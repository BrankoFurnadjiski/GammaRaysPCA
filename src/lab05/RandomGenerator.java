package lab05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator {
	private Random random;
	private List<Integer> array;
	
	public RandomGenerator(int zeroes, int ones){
		random = new Random();
		array = new ArrayList<>();
		for(int i = 0; i < zeroes; ++i)
			array.add(0);
		for(int i = 0; i < ones; ++i)
			array.add(1);
		
	}
	
	public int getNumber() {
		int next = random.nextInt(10);
		return array.get(next);
	}
			
}
