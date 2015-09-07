package analytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.GoldenSection;
import model.ISeriesPoint;
import model.SimplePoint;
import ta.IPattern;
import ta.IPatternFinder;
import util.ValueDistance;

public class PatternUp implements IPatternFinder<Date, Double, PatternType> {
	public final static int PATTERN_LEN = 4;
	final static int PATTERN_FIRST = 0;
	final static int PATTERN_SECOND = 1;
	final static int PATTERN_THIRD = 2;
	final static int PATTERN_FOURTH = 3;
	
	double reboundOnce;
	double correction;
	double reboundMore;
	
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
	
	public PatternUp(double reboundOnce, double correction, double reboundMore) {
		this.reboundOnce = reboundOnce;
		this.correction = correction;
		this.reboundMore = reboundMore;
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

		double A = ValueDistance.distance(points.get(PATTERN_SECOND).getValue(), points.get(PATTERN_FIRST).getValue());
		double B = ValueDistance.distance(points.get(PATTERN_THIRD).getValue(), points.get(PATTERN_FIRST).getValue());
		double C = ValueDistance.distance(points.get(PATTERN_FOURTH).getValue(), points.get(PATTERN_THIRD).getValue());
		double D = ValueDistance.trunRoundRatio(points.get(PATTERN_FIRST).getValue(), points.get(PATTERN_SECOND).getValue(), points.get(PATTERN_THIRD).getValue());
//		System.out.println(A);
//		System.out.println(B);
//		System.out.println(C);
		
		if(A >= reboundOnce && B >= correction && C >= reboundMore && D <= GoldenSection.LEVEL_FIVE.value()) {
			mode.setType(PatternType.Mode_UP);
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
