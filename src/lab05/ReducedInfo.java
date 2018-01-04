package lab05;

public class ReducedInfo {
	private double PCA1;
	private double PCA2;
	private double PCA3;
	private String className;
	
	public ReducedInfo(double PCA1, double PCA2, double PCA3, String className) {
		this.PCA1 = PCA1;
		this.PCA2 = PCA2;
		this.PCA3 = PCA3;
		this.className = className;
	}
	
	public String toString() {
		return String.format("%.2f %.2f %.2f", PCA1, PCA2, PCA3);
	}
	
	public String getName() {
		return this.className;
	}
	
	public double distance(ReducedInfo reducedInfo) {
		return Math.sqrt((this.PCA1-reducedInfo.PCA1) * (this.PCA1-reducedInfo.PCA1) +
				(this.PCA2-reducedInfo.PCA2) * (this.PCA2-reducedInfo.PCA2) +
				(this.PCA3-reducedInfo.PCA3) * (this.PCA3-reducedInfo.PCA3));
	}
	
}
