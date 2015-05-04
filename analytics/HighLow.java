package analytics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class HighLow {

	public static void main(String[] args) {
		System.out.println("start!!!!");
//		double[] data1 = {9.49 ,9.20 ,8.98 ,9.23 ,9.28 ,9.09 ,9.13 ,9 ,9.80};
		double[] data1 = {5,4,3,2,1,5};
		List<Double> values = new ArrayList<Double>();
        List<Map<Integer, Double>> peaks = new ArrayList<Map<Integer, Double>>();
        for(int i=0; i<data1.length; i++){
        	values.add(data1[i]);
        }
        peaks = peak_detection(values, 0.03);
        System.out.println(peaks.toString());
        System.out.println(peaks.get(0).keySet());
        
        DescriptiveStatistics stat = new DescriptiveStatistics();
        for(double item:data1){
        	stat.addValue(item);
        }
        System.out.println(stat.getSortedValues()[0]);
        System.out.println("done!!!!");
	}
	
	public static List<Map<Integer, Double>> peak_detection(List<Double> values, Double delta)
	{
		List<Integer> indices = new ArrayList<Integer>();
		for (int i=0; i<values.size(); i++) {
			indices.add(i);
		}
		return peak_detection(values, delta, indices);
	}
	
	public static <U> List<Map<U, Double>> peak_detection(List<Double> values, Double delta, List<U> indices)
	{
		assert(indices != null);
		assert(values.size() != indices.size());
		
		Map<U, Double> maxima = new HashMap<U, Double>();
		Map<U, Double> minima = new HashMap<U, Double>();
		List<Map<U, Double>> peaks = new ArrayList<Map<U, Double>>();
		peaks.add(maxima);
		peaks.add(minima);
		
		Double maximum = null;
		Double minimum = null;
		U maximumPos = null;
		U minimumPos = null;
		
		boolean lookForMax = true;
		int pos = 0;
		for (Double value : values) {
			if (maximum == null ||value > maximum ) {
				maximum = value;
				maximumPos = indices.get(pos);
			}
			
			if (minimum == null || value < minimum) {
				minimum = value;
				minimumPos = indices.get(pos);
			}
			
			if (lookForMax) {
				if ((maximum-value)/maximum > delta) {
					maxima.put(maximumPos, maximum);
					minimum = value;
					minimumPos = indices.get(pos);
					lookForMax = false;
				}
			} else {
				if ((value-minimum)/minimum > delta) {
					minima.put(minimumPos, minimum);
					maximum = value;
					maximumPos = indices.get(pos);
					lookForMax = true;
				}
			}
			
			pos++;
			
		}
		if(lookForMax)
			maxima.put(maximumPos, maximum);
		else
			minima.put(minimumPos, minimum);
		
		return peaks;
	}
	
}
