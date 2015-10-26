package com.pureblue.quant.signal.technical.movingaverage;

import org.joda.time.DateTime;

import com.pureblue.quant.signal.timed.DoubleSignal;

import java.math.BigDecimal;

public class MACD implements DoubleSignal<Double> {
	EMA shortEMA;
	EMA longEMA;
	EMA meanEMA;
	private double dif = Double.NaN;
	private double dea = Double.NaN;
	private double macd = Double.NaN;
		
    public MACD(int shortN, int longN, int meanN) {
    	shortEMA = new EMA(shortN);
    	longEMA = new EMA(longN);
    	meanEMA = new EMA(meanN);
    }
	
	@Override
	public void update(DateTime t, Double data) {
		shortEMA.update(t, data);
		longEMA.update(t, data);
		dif = shortEMA.value() - longEMA.value();
		meanEMA.update(t, dif);
		dea = meanEMA.value();
		macd = dif-dea>=0 ? Math.pow(dif-dea, 2):-1.0*(Math.pow(dif-dea, 2));
		BigDecimal bd = new BigDecimal(macd*10);
		bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
		macd = bd.doubleValue();
	}

	@Override
	public Double value() {
		return macd;
	}

	public double getDif() {
		return dif;
	}

	public double getDea() {
		return dea;
	}

	@Override
	public String toString() {
		return "MACD [dif=" + dif + ", dea=" + dea + ", macd=" + macd + "]";
	}
}
