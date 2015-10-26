package com.pureblue.quant.analytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;





import com.pureblue.quant.model.ISeriesPoint;
import com.pureblue.quant.model.SimplePoint;
import com.pureblue.quant.ta.IPattern;
import com.pureblue.quant.ta.IPatternFinder;
import com.pureblue.quant.util.ValueDistance;


//import model.GoldenSection;


public class PatternW implements IPatternFinder<Date, Double, PatternType> {
	public final static int PATTERN_LEN = 4;
	final static int PATTERN_FIRST = 0;
	final static int PATTERN_SECOND = 1;
	final static int PATTERN_THIRD = 2;
	final static int PATTERN_FOURTH = 3;
	
	double delta;
	double latitude;
	double extent;
	
	public static void main(String[] args){
		ArrayList<ISeriesPoint<Date, Double>> points = new ArrayList<ISeriesPoint<Date, Double>>();
		Date day = new Date();
		points.add(new SimplePoint(day, 3.0));
		points.add(new SimplePoint(day, 5.0));
		points.add(new SimplePoint(day, 10.0));
		points.add(new SimplePoint(day, 5.0));
		points.add(new SimplePoint(day, 13.0));
		PatternM p = new PatternM(0, 0.07, 0.03);
		IPattern<Date, Double, PatternType> found = p.findPattern(points.subList(1, 1+PATTERN_LEN));
		System.out.println(found.getType());
	}
	
	public PatternW(double delta, double latitude, double extent) {
		this.delta = delta;
		this.latitude = latitude;
		this.extent = extent;
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
		double B = ValueDistance.distanceABS(points.get(PATTERN_FOURTH).getValue(), points.get(PATTERN_SECOND).getValue());
		double C = ValueDistance.distance(points.get(PATTERN_THIRD).getValue(), points.get(PATTERN_FOURTH).getValue());
		double D = ValueDistance.trunRoundRatio(points.get(PATTERN_SECOND).getValue(), points.get(PATTERN_THIRD).getValue(), points.get(PATTERN_FOURTH).getValue());
//		double D = ValueDistance.trunRoundRatio(points.get(PATTERN_FIRST).getValue(), points.get(PATTERN_SECOND).getValue(), points.get(PATTERN_THIRD).getValue());
//		System.out.println(A);
//		System.out.println(B);
//		System.out.println(C);
		
		if(A >= latitude && B <= delta && C >= extent) {
			mode.setType(PatternType.Mode_W);
			mode.setPoints(points);
			System.out.println(points.get(0).toString());
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
