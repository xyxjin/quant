package util;

import java.util.List;

public class DoubleUtils {

	public static double[] collection2DoubleArray(List<Double> list){
		double[] data = new double[list.size()];
		for(int i=0; i<list.size(); i++){
			data[i] = list.get(i);
		}
		return data;
	}
	
	public static double[] Longcollection2DoubleArray(List<Long> list){
		double[] data = new double[list.size()];
		for(int i=0; i<list.size(); i++){
			data[i] = (double)list.get(i);
		}
		return data;
	}
}
