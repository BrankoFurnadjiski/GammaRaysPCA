package lab05;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;

public class Info {
	
	public double fLength;
	public double fWidth;
	public double fSize;
	public double fConc;
	public double fConc1;
	public double fAsym;
	public double fM3Long;
	public double fM3Trans;
	public double fAlpha;
	public double fDist;
	public String className;
	
	public Info(double fLength, double fWidth, double fSize, double fConc, double fConc1, double fAsym, double fM3Long, double fM3Trans, double fAlpha, double fDist, String className) {
		this.fLength = fLength;
		this.fWidth = fWidth;
		this.fSize = fSize;
		this.fConc = fConc;
		this.fConc1 = fConc1;
		this.fAsym = fAsym;
		this.fM3Long = fM3Long;
		this.fM3Trans = fM3Trans;
		this.fAlpha = fAlpha;
		this.fDist = fDist;
		this.className = className;
				
	}
	
	
	
	public double getfLength() {
		return fLength;
	}



	public void setfLength(double fLength) {
		this.fLength = fLength;
	}



	public double getfWidth() {
		return fWidth;
	}



	public void setfWidth(double fWidth) {
		this.fWidth = fWidth;
	}



	public double getfSize() {
		return fSize;
	}



	public void setfSize(double fSize) {
		this.fSize = fSize;
	}



	public double getfConc() {
		return fConc;
	}



	public void setfConc(double fConc) {
		this.fConc = fConc;
	}



	public double getfConc1() {
		return fConc1;
	}



	public void setfConc1(double fConc1) {
		this.fConc1 = fConc1;
	}



	public double getfAsym() {
		return fAsym;
	}



	public void setfAsym(double fAsym) {
		this.fAsym = fAsym;
	}



	public double getfM3Long() {
		return fM3Long;
	}



	public void setfM3Long(double fM3Long) {
		this.fM3Long = fM3Long;
	}



	public double getfM3Trans() {
		return fM3Trans;
	}



	public void setfM3Trans(double fM3Trans) {
		this.fM3Trans = fM3Trans;
	}



	public double getfAlpha() {
		return fAlpha;
	}



	public void setfAlpha(double fAlpha) {
		this.fAlpha = fAlpha;
	}



	public double getfDist() {
		return fDist;
	}



	public void setfDist(double fDist) {
		this.fDist = fDist;
	}



	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public String toString() {
		return String.format("%.2f %.2f %.2f %.2f %.2f %.2f %.2f %.2f %.2f %.2f %s", fLength, fWidth, fSize, fConc, fConc1, fAsym, fM3Long, fM3Trans, fAlpha, fDist, className);
	}
	
	public static List<Info> readFromFile(BufferedReader stream) {
		return  stream.lines().map(line ->  {
												String parts[] = line.split(",");
												double fLendth = Double.parseDouble(parts[0]);
												double fWidth = Double.parseDouble(parts[1]);
												double fSize = Double.parseDouble(parts[2]);
												double fConc = Double.parseDouble(parts[3]);
												double fConc1 = Double.parseDouble(parts[4]);
												double fAsym = Double.parseDouble(parts[5]);
												double fM3Long = Double.parseDouble(parts[6]);
												double fM3Trans = Double.parseDouble(parts[7]);
												double fAlpha = Double.parseDouble(parts[8]);
												double fDist = Double.parseDouble(parts[9]);
												String className = parts[10];
												return new Info(fLendth, fWidth, fSize, fConc, fConc1, fAsym, fM3Long, fM3Trans, fAlpha, fDist, className);
												}
										).collect(Collectors.toList());
	}
	
	public int getSize() {
		return 10;
	}
 
}
