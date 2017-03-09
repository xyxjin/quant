package com.pureblue.quant.selectionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.pureblue.quant.algo.IManagedRunnable.RunningStatus;
import com.pureblue.quant.analytics.HighLow;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.IOHLCTimeSeries;
import com.pureblue.quant.model.ISeries;
import com.pureblue.quant.model.ISeriesAugmentable;
import com.pureblue.quant.model.ISeriesPoint;

public class CANSLIMAgent implements ISelectionAgent{
	private static final int MAXIMUM_MAP_INDEX = 0;
	private static final int MINIMUM_MAP_INDEX = 1;
	private final String symbol;
	private final DateTime startDate;
	private volatile List<IOHLCPoint> stockTimeSeries;
	private boolean selection = false;
	private Double delta;
	private volatile RunningStatus runningStatus = RunningStatus.NEW;

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
		List<Integer> indices = new ArrayList<Integer>();
		peaks = HighLow.peak_detection(values, delta, indices);
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

    @Override
    public void pause() {
        synchronized (runningStatus) {
            if (runningStatus == RunningStatus.RUNNING) {
                runningStatus = RunningStatus.PAUSED;
            }
        }
    }

    @Override
    public void resume() {
        synchronized (runningStatus) {
            if (runningStatus == RunningStatus.PAUSED) {
                runningStatus = RunningStatus.RUNNING;
            }
        }
    }

    @Override
    public synchronized void kill() {
        synchronized (runningStatus) {
            runningStatus = RunningStatus.TERMINATED;
        }
        notify();
    }

    @Override
    public RunningStatus getRunningStatus() {
        return runningStatus;
    }

    @Override
    public void run() {
        runningStatus = RunningStatus.RUNNING;
    }

    @Override
    public void wire(Map<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> input, ISeriesAugmentable<Date, Double, ISeriesPoint<Date, Double>> output) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unwire() {
        kill();
    }

    @Override
    public void inputComplete() {
        kill();
    }
}
