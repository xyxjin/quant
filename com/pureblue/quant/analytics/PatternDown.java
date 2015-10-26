package com.pureblue.quant.analytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pureblue.quant.model.GoldenSection;
import com.pureblue.quant.model.ISeriesPoint;
import com.pureblue.quant.model.SimplePoint;
import com.pureblue.quant.ta.IPattern;
import com.pureblue.quant.ta.IPatternFinder;
import com.pureblue.quant.util.ValueDistance;

public class PatternDown implements IPatternFinder<Date, Double, PatternType> {
	public final static int PATTERN_LEN = 4;
	final static int PATTERN_FIRST = 0;
	final static int PATTERN_SECOND = 1;
	final static int PATTERN_THIRD = 2;
	final static int PATTERN_FOURTH = 3;
	
	double correctionOnce;
	double rebound;
	double correctionMore;
	
	public static void main(String[] args){
		ArrayList<ISeriesPoint<Date, Double>> points = new ArrayList<ISeriesPoint<Date, Double>>();
		Date day = new Date();
		points.add(new SimplePoint(day, 3.0));
		points.add(new SimplePoint(day, 10.0));
		points.add(new SimplePoint(day, 5.0));
		points.add(new SimplePoint(day, 10.0));
		points.add(new SimplePoint(day, 4.0));
		PatternM p = new PatternM(0, 0.07, 0.03);
		IPattern<Date, Double, PatternType> found = p.findPattern(points.subList(0, 0+PATTERN_LEN));
		System.out.println(found.getType());
	}
	
	public PatternDown(double correctionOnce, double rebound,
			double correctionMore) {
		this.correctionOnce = correctionOnce;
		this.rebound = rebound;
		this.correctionMore = correctionMore;
	}

	public IPattern<Date, Double, PatternType> findPattern(List<ISeriesPoint<Date, Double>> points){
//		for(int i=0; i<points.size(); i++)
//			System.out.println(points.get(i).getValue());
		TrendMode mode = new TrendMode();
		mode.setType(PatternType.NONE);
		if(points.size() < PATTERN_LEN){
			System.out.println("less");
			return mode;
		}

		double A = ValueDistance.distance(points.get(PATTERN_FIRST).getValue(), points.get(PATTERN_SECOND).getValue());
		double B = ValueDistance.distance(points.get(PATTERN_THIRD).getValue(), points.get(PATTERN_SECOND).getValue());
		double C = ValueDistance.distance(points.get(PATTERN_THIRD).getValue(), points.get(PATTERN_FOURTH).getValue());
		double D = ValueDistance.trunRoundRatio(points.get(PATTERN_FIRST).getValue(), points.get(PATTERN_SECOND).getValue(), points.get(PATTERN_THIRD).getValue());
//		System.out.println(A);
//		System.out.println(B);
//		System.out.println(C);
		
		if(A >= correctionOnce && B >= rebound && C >= correctionMore && D <= GoldenSection.LEVEL_FIVE.value()) {
			mode.setType(PatternType.Mode_DOWN);
			mode.setPoints(points);
			return mode;
		}
		
//		System.out.println("no found");
		return mode;
	}
	
	@Override
	public List<IPattern<Date, Double, PatternType>> findPatterns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPattern<Date, Double, PatternType>> findPatterns(
			ISeriesPoint<Date, Double> addedPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPattern<Date, Double, PatternType>> findPatterns(
			ISeriesPoint<Date, Double> oldPoint,
			ISeriesPoint<Date, Double> updatedPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
