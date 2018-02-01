package lab05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private List<ReducedInfo> reducedArray;
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
			
			test.reduceSet(test, array, list);
			
			test.reducedArray = test.normalizeData(test.reducedArray);
			
			test.divideSet(test, list);
			
//			System.out.print("TESTSET ");
//			System.out.println(test.testArray.size());
//			test.testArray.stream().limit(10).forEach(System.out::println);
//			
//			System.out.print("DATASET ");
//			System.out.println(test.setArray.size());
//			test.setArray.stream().limit(10).forEach(System.out::println);
			
			test.writeData(test.reducedArray, test);
			
			//int best = 2;
			int best = test.findBestK(test);
			
			int error = test.calculateErrorForPredefinedK(best, test);
			
			System.out.println(error);		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private int calculateErrorForPredefinedK(int best, PCAtest test) {

		int testSize = test.testArray.size();
		int setSize = test.setArray.size();
		int counterG;
		int counterH;
	    int counter = 0;
		
		for(int i = 0; i < testSize; ++i) {
			counterG = 0;
			counterH = 0;
			ReducedInfo current = test.testArray.get(i);
			List<ReducedInfoDistance> reducedInfoDistances = new ArrayList<>();
			
			for(int j = 0; j < setSize; ++j) {
				ReducedInfo comparingTo = test.setArray.get(j);
				double distance = current.distance(comparingTo);
				reducedInfoDistances.add(new ReducedInfoDistance(comparingTo, distance));
			}
			
			Collections.sort(reducedInfoDistances, Comparator.comparing(ReducedInfoDistance::getDistance));
			reducedInfoDistances = reducedInfoDistances.stream().limit(best).collect(Collectors.toList());
			
			for(int l = 0; l < best; ++l) {
				if(reducedInfoDistances.get(l).getReducedInfo().getName().compareTo("g") == 0) {
					counterG++;
				} else {
					counterH++;
				}
			}
			
			if(counterG > counterH && current.getName().compareTo("h") == 0) {
				counter++;
			} else if(counterG < counterH && current.getName().compareTo("g") == 0) {
				counter++;
			} else if(counterG == counterH){
				RandomGenerator generator = new RandomGenerator(5, 5);
				int where = generator.getNumber();
				if(where == 0 && current.getName().compareTo("h") == 0) {
					counter++;
				} else if (where == 1 && current.getName().compareTo("g") == 0) {
					counter++;
				}
			}
			
		}
		
		return counter;
	}

	private void reduceSet(PCAtest test, double[][] array,List<Info> info) {

		reducedArray = new ArrayList<>();
		
		for(int i = 0; i < test.attributes.get(0).size(); ++ i) {
			reducedArray.add(new ReducedInfo(array[i][0], array[i][1], array[i][2], info.get(i).getClassName()));
		}
		
	}

	private List<ReducedInfo> normalizeData(List<ReducedInfo> reducedArray) {
		
		double maxPCA1 = reducedArray.stream().map(ReducedInfo::getPCA1).max(Comparator.naturalOrder()).orElse((double) 0);
		double maxPCA2 = reducedArray.stream().map(ReducedInfo::getPCA2).max(Comparator.naturalOrder()).orElse((double) 0);
		double maxPCA3 = reducedArray.stream().map(ReducedInfo::getPCA3).max(Comparator.naturalOrder()).orElse((double) 0);
		
		double minPCA1 = reducedArray.stream().map(ReducedInfo::getPCA1).min(Comparator.naturalOrder()).orElse((double) 0);
		double minPCA2 = reducedArray.stream().map(ReducedInfo::getPCA2).min(Comparator.naturalOrder()).orElse((double) 0);
		double minPCA3 = reducedArray.stream().map(ReducedInfo::getPCA3).min(Comparator.naturalOrder()).orElse((double) 0);
		
		return reducedArray.stream().map(info -> normalizedInfo(info, maxPCA1, minPCA1, maxPCA2, minPCA2, maxPCA3, minPCA3)).collect(Collectors.toList());
		
	}
	
	private ReducedInfo normalizedInfo(ReducedInfo info, double max1, double min1, double max2, double min2, double max3, double min3) {
		return new ReducedInfo((info.getPCA1() - min1)/(max1-min1),(info.getPCA2() - min2)/(max2-min2),(info.getPCA3() - min2)/(max2-min2),info.getName());
	}

	private int findBestK(PCAtest test) {
		int n = reducedArray.size();
		int bestK = 1;
		int minError = Integer.MAX_VALUE;
		
		for(int i = 1; i < Math.sqrt(n); i *= 2) {
			int currentError = calculateError(i);
			System.out.print("Current k: " + i);
			System.out.println(" Missclasiffication: " + currentError);
			if(currentError < minError) { 
				minError = currentError;
				bestK = i;
			}
		}
		
		return bestK;
		
	}
	
	private int calculateError(int k) {
		int n = reducedArray.size();
		int counterG;
		int counterH;
	    int counter = 0;
		
		for(int i = 0; i < n; ++i) {
			counterG = 0;
			counterH = 0;
			ReducedInfo current = reducedArray.get(i);
			List<ReducedInfoDistance> reducedInfoDistances = new ArrayList<>();
			
			for(int j = 0; j < n; ++j) {
				if(i != j) {
					ReducedInfo comparingTo = reducedArray.get(j);
					double distance = current.distance(comparingTo);
					reducedInfoDistances.add(new ReducedInfoDistance(comparingTo, distance));
				}
			}
			
			Collections.sort(reducedInfoDistances, Comparator.comparing(ReducedInfoDistance::getDistance));
			reducedInfoDistances = reducedInfoDistances.stream().limit(k).collect(Collectors.toList());
			
			for(int l = 0; l < k; ++l) {
				if(reducedInfoDistances.get(l).getReducedInfo().getName().compareTo("g") == 0) {
					counterG++;
				} else {
					counterH++;
				}
			}
			
			if(counterG > counterH && current.getName().compareTo("h") == 0)
				counter++;
			if(counterH > counterG && current.getName().compareTo("g") == 0)
				counter++;
			
		}
		
		return counter;
	}
	
	private void divideSet(PCAtest test, List<Info> info) {
		RandomGenerator generator = new RandomGenerator(3,7);
		
		test.testArray = new ArrayList<>();
		test.setArray = new ArrayList<>();
		
		for(int i = 0; i < test.attributes.get(0).size(); ++ i) {
			if(generator.getNumber() == 0) {
				testArray.add(test.reducedArray.get(i));
			} else {
				setArray.add(test.reducedArray.get(i));
			}
		}
		
	}

	private void writeData(List<ReducedInfo> array, PCAtest test) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		for(int i  = 0; i < test.attributes.get(0).size(); ++i) {
			writer.write(String.format(Locale.ROOT, "%s", array.get(i)));
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
