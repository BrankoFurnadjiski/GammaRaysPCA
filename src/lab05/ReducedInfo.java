package lab05;

import java.util.Locale;

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
		return String.format(Locale.ROOT,"%.5f, %.5f, %.5f", PCA1, PCA2, PCA3);
	}
	
	public String getName() {
		return this.className;
	}
	
	public double distance(ReducedInfo reducedInfo) {
		return Math.sqrt((this.PCA1-reducedInfo.PCA1) * (this.PCA1-reducedInfo.PCA1) +
				(this.PCA2-reducedInfo.PCA2) * (this.PCA2-reducedInfo.PCA2) +
				(this.PCA3-reducedInfo.PCA3) * (this.PCA3-reducedInfo.PCA3));
	}

	public double getPCA1() {
		return PCA1;
	}

	public void setPCA1(double pCA1) {
		PCA1 = pCA1;
	}

	public double getPCA2() {
		return PCA2;
	}

	public void setPCA2(double pCA2) {
		PCA2 = pCA2;
	}

	public double getPCA3() {
		return PCA3;
	}

	public void setPCA3(double pCA3) {
		PCA3 = pCA3;
	}
	
}
