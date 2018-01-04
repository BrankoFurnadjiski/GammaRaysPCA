package lab05;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
 
import java.util.Arrays;

public class govna {
	 
    public static void main(String[] args) {
 
        double[][] cov = {{1, 2, 3}, {4, 1, 2}, {2, 0, 1}};
 
        EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(new Matrix(cov));
 
        double[] realEigenvalues = eigenvalueDecomposition.getRealEigenvalues();
        Matrix v = eigenvalueDecomposition.getV();
 
        v.print(5, 5);
 
        System.out.println(Arrays.toString(realEigenvalues));
    }
	
}
