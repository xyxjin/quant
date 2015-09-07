package selectionService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import analytics.HighLow;
import dao.IOHLCPoint;
import dao.IOHLCTimeSeries;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class CANSLIMAgent implements ISelectionAgent{
	private static final int MAXIMUM_MAP_INDEX = 0;
	private static final int MINIMUM_MAP_INDEX = 1;
	private final String symbol;
	private final DateTime startDate;
	private volatile List<IOHLCPoint> stockTimeSeries;
	private boolean selection = false;
	private Double delta;

	public CANSLIMAgent(String symbol, DateTime startDate, Double delta){
		this.symbol = symbol;
		this.startDate = startDate;
		this.delta = delta;
	}
	
	public void wire(List<IOHLCPoint> inputSeries) {
		if (!(inputSeries instanceof IOHLCTimeSeries)) {
			throw new IllegalArgumentException("Only '" + IOHLCTimeSeries.class.getName() + "' instances can be passed as input series");
		}
		stockTimeSeries = inputSeries;
	}
	
	public boolean evaluate(){
		List<Map<Integer, Double>> peaks;
		Map<Integer, Double> maxima;
		Map<Integer, Double> minima;
		List<Double> values = new ArrayList<Double>();
		Iterator<IOHLCPoint> ohlciter = stockTimeSeries.iterator();
		while(ohlciter.hasNext()){
			IOHLCPoint point = ohlciter.next();
			values.add(point.getClose());
		}
		peaks = HighLow.peak_detection(values, delta);
		maxima = peaks.get(MAXIMUM_MAP_INDEX);
		minima = peaks.get(MINIMUM_MAP_INDEX);
		if(maxima.size() < 2)
			return false;
		if(minima.size() < 2)
			return false;
		
		DescriptiveStatistics stat = new DescriptiveStatistics();
		for(int item:maxima.keySet()){
			stat.addValue((double)item);
		}
		if((int)stat.getMax() != stockTimeSeries.size())
			return false;
		
		double[] peakIndex = stat.getSortedValues();
		stat.clear();
		for(int item:minima.keySet()){
			stat.addValue((double)item);
		}
		double[] toughtIndex = stat.getSortedValues();
		
		if((peakIndex[peakIndex.length-1] > peakIndex[peakIndex.length-2]) || (toughtIndex[toughtIndex.length-1] > toughtIndex[toughtIndex.length-2])){
			if((peakIndex[peakIndex.length-1] - peakIndex[peakIndex.length-2])/peakIndex[peakIndex.length-2] < delta){
				return true;
			}
		}
		
		return false;
	}
}
