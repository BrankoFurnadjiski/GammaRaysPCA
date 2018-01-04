package lab05;

import java.util.ArrayList;
import java.util.List;


public class EigenVectorValue implements Comparable<EigenVectorValue>{
	private double value;
	private List<Double> vector;
	
	public EigenVectorValue(double value, int index, double[][] matrix) {
		this.value = value;
		vector = new ArrayList<>();
		initVector(matrix, index);
	}

	private void initVector(double[][] matrix, int index) {
		for(int i = 0; i < matrix.length; ++i) {
			vector.add(matrix[index][i]);
		}
	}
	
	public double getValue() {
		return value;
	}
	
	public List<Double> getVector() {
		return vector;
	}

	@Override
	public int compareTo(EigenVectorValue o) {
		if(this.value > o.value) {
			return -1;
		} else if(this.value < o.value) {
			return 1;
		}
		return 0;
	}
}
