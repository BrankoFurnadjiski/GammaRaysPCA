package lab05;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class PCAtest {
	
	private List<Double> fLength;
	private List<Double> fWidth;
	private List<Double> fSize;
	private List<Double> fConc;
	private List<Double> fConc1;
	private List<Double> fAsym;
	private List<Double> fM3Long;
	private List<Double> fM3Trans;
	private List<Double> fAlpha;
	private List<Double> fDist;
	
	private List<Double> averages;
	
	private List<List<Double>> attributes;
	
	private double[][] matrix;
	private ReducedInfo[] reducedArray;
	private List<ReducedInfo> testArray;
	private List<ReducedInfo> setArray;
	
	private int characteristics;
	
	public static void main(String[] args) {
		double[][] array;
		
		Matrix finalData = null;
		
		PCAtest test = new PCAtest();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("magic04.data.txt")));
			List<Info> list = Info.readFromFile(reader);
			
			test.getCovariance(list);
			
			test.calculateMatrix();
			
			finalData = test.getFinalData(test);
			
			array = finalData.getArray();
			
			test.divideSet(test, array, list);
			
			test.writeData(array, test);
			
			System.out.println(test.characteristics);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void findBestK() {
	}
	
	private void divideSet(PCAtest test, double[][] array, List<Info> info) {

		reducedArray = new ReducedInfo[attributes.get(0).size()];
		
		for(int i = 0; i < test.attributes.get(0).size(); ++ i) {
			reducedArray[i] = new ReducedInfo(array[i][0], array[i][1], array[i][2], info.get(i).getClassName());
		}
		
		RandomGenerator generator = new RandomGenerator();
		testArray = new ArrayList<>();
		setArray = new ArrayList<>();
		
		for(int i = 0; i < test.attributes.get(0).size(); ++ i) {
			if(generator.getNumber() == 0) {
				testArray.add(reducedArray[i]);
			} else {
				setArray.add(reducedArray[i]);
			}
		}
		
	}

	private void writeData(double[][] array, PCAtest test) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		for(int i  = 0; i < test.attributes.get(0).size(); ++i) {
	    	for(int j = 0; j < 3; ++j) {
	    		writer.write(String.format(Locale.ROOT, "%.2f", array[i][j]));
	    		if(j != 2) {
	    			writer.write(", ");
	    		}
	    	}
	    	if(i !=  test.attributes.get(0).size()-1){
	    		writer.newLine();
	    	}
	    }
		writer.flush();
		writer.close();
	}

	private Matrix getFinalData(PCAtest test) {
		EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(new Matrix(test.matrix));
		
		double[] realEigenvalues = eigenvalueDecomposition.getRealEigenvalues();
        Matrix v = eigenvalueDecomposition.getV();
        
        double[][] matrixV = v.getArray();
 
	    List<EigenVectorValue> eigenVectorValuesList = new ArrayList<>();
	    for(int i = 0; i < realEigenvalues.length; ++i) {
	    	EigenVectorValue eigenVectorValue = new EigenVectorValue(realEigenvalues[i], i, matrixV);
	        eigenVectorValuesList.add(eigenVectorValue);
	    }
		
	    eigenVectorValuesList = eigenVectorValuesList.stream().sorted().limit(3).collect(Collectors.toList());
	    
	    double[][] featureVector = new double[3][test.characteristics];
	    
	    for(int i  = 0; i < 3; ++i) {
	    	for(int j = 0; j < test.characteristics; ++j) {
	    		featureVector[i][j] = eigenVectorValuesList.get(i).getVector().get(j);
	    	}
	    }
	    
	    Matrix featureMatrix = new Matrix(featureVector).transpose();
	    double[][] rawData = transformToMatrix(test.attributes);
	    Matrix rawMatrix = new Matrix(rawData).transpose();
	    return rawMatrix.times(featureMatrix);
	}
	
	private double[][] transformToMatrix(List<List<Double>> attributes2) {
		double[][] rawData = new double[attributes2.size()][attributes2.get(0).size()];
		for(int i = 0; i < attributes2.size(); ++i) {
			for(int j = 0; j < attributes2.get(0).size(); ++j) {
				rawData[i][j] = attributes2.get(i).get(j);
			}
		}
		return rawData;
		
	}

	private void getCovariance(List<Info> list) {
		
		characteristics = list.get(0).getSize();
		
		fLength = list.stream()
				.map(Info::getfLength)
				.collect(Collectors.toList());
		
		fWidth = list.stream()
				.map(Info::getfWidth)
				.collect(Collectors.toList());
		
		fSize = list.stream()
				.map(Info::getfSize)
				.collect(Collectors.toList());
		
		fConc = list.stream()
				.map(Info::getfConc)
				.collect(Collectors.toList());
		
		fConc1 = list.stream()
				.map(Info::getfConc1)
				.collect(Collectors.toList());
		
		fAsym = list.stream()
				.map(Info::getfAsym)
				.collect(Collectors.toList());
		
		fM3Long = list.stream()
				.map(Info::getfM3Long)
				.collect(Collectors.toList());
		
		fM3Trans = list.stream()
				.map(Info::getfM3Trans)
				.collect(Collectors.toList());
		
		fAlpha = list.stream()
				.map(Info::getfAlpha)
				.collect(Collectors.toList());
		
		fDist = list.stream()
				.map(Info::getfDist)
				.collect(Collectors.toList());
		
		getAverages();
			
	}

	private  void getAverages() {
		averages = new ArrayList<>();
		Double fLengthAvg = fLength.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fWidthAvg = fWidth.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fSizeAvg = fSize.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fConcAvg = fConc.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fConc1Avg = fConc1.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fAsymAvg = fAsym.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fM3LongAvg = fM3Long.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fM3TransAvg = fM3Trans.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fAlphaAvg = fAlpha.stream().mapToDouble(att -> (double) att).average().orElse(0);
		Double fDistAvg = fDist.stream().mapToDouble(att -> (double) att).average().orElse(0);
		
		averages.add(fLengthAvg);
		averages.add(fWidthAvg);
		averages.add(fSizeAvg);
		averages.add(fConcAvg);
		averages.add(fConc1Avg);
		averages.add(fAsymAvg);
		averages.add(fM3LongAvg);
		averages.add(fM3TransAvg);
		averages.add(fAlphaAvg);
		averages.add(fDistAvg);
		
		normalize();
		
	}

	private void normalize() {
		
		List<Double> fLengthNew = fLength.stream().map(attr -> attr - averages.get(0)).collect(Collectors.toList());
		List<Double> fWidthNew = fWidth.stream().map(attr -> attr - averages.get(1)).collect(Collectors.toList());
		List<Double> fSizeNew = fSize.stream().map(attr -> attr - averages.get(2)).collect(Collectors.toList());
		List<Double> fConcNew = fConc.stream().map(attr -> attr - averages.get(3)).collect(Collectors.toList());
		List<Double> fConc1New = fConc1.stream().map(attr -> attr - averages.get(4)).collect(Collectors.toList());
		List<Double> fAsymNew = fAsym.stream().map(attr -> attr - averages.get(5)).collect(Collectors.toList());
		List<Double> fM3LongNew = fM3Long.stream().map(attr -> attr - averages.get(6)).collect(Collectors.toList());
		List<Double> fM3TransNew = fM3Trans.stream().map(attr -> attr - averages.get(7)).collect(Collectors.toList());
		List<Double> fAlphaNew = fAlpha.stream().map(attr -> attr - averages.get(8)).collect(Collectors.toList());
		List<Double> fDistNew = fDist.stream().map(attr -> attr - averages.get(9)).collect(Collectors.toList());
		
		attributes = new ArrayList<>();
		attributes.add(fLengthNew);
		attributes.add(fWidthNew);
		attributes.add(fSizeNew);
		attributes.add(fConcNew);
		attributes.add(fConc1New);
		attributes.add(fAsymNew);
		attributes.add(fM3LongNew);
		attributes.add(fM3TransNew);
		attributes.add(fAlphaNew);
		attributes.add(fDistNew);
		
	}
	
	private void calculateMatrix() {
		int size = attributes.size();
		matrix = new double[size][size];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				matrix[i][j] = calcualteBetweenTwoCovaruance(attributes.get(i), attributes.get(j), averages.get(i), averages.get(j));
			}
		}
		
	}
	
	private double calcualteBetweenTwoCovaruance(List<Double> x, List<Double> y, double averagex, double averagey) {
		int n = x.size();
		double sum = 0;
		
		for(int i = 0; i < n; ++i) {
			sum += (x.get(i) - averagex) * (y.get(i) - averagey);
		}
		
		sum = sum/(n-1);
		
		return sum;
	}
}
