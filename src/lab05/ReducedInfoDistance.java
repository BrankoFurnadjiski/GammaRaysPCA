package lab05;

public class ReducedInfoDistance {
	
	private ReducedInfo reducedInfo;
	private double distance;
	
	public ReducedInfoDistance(ReducedInfo reducedInfo, double distance) {
		this.reducedInfo = reducedInfo;
		this.distance = distance;
	}

	public ReducedInfo getReducedInfo() {
		return reducedInfo;
	}

	public void setReducedInfo(ReducedInfo reducedInfo) {
		this.reducedInfo = reducedInfo;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public String toString() {
		return String.format("$s %.2f", reducedInfo, distance); 
	}
	
}
