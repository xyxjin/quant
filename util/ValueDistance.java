package util;

public class ValueDistance {

	public static double distance(double A, double B){
		return (A-B)/B;
	}
	
	public static double distanceABS(double A, double B){
		return Math.abs(A-B)/B;
	}
	
	public static double trunRoundRatio(double A, double B, double C){
		return Math.abs((C-B)/(A-B));
	}
}
