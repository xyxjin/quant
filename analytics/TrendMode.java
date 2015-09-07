package analytics;

import java.util.Date;
import java.util.List;

import model.ISeriesPoint;
import ta.IPattern;
import ta.ITrendLine;

public class TrendMode implements IPattern<Date, Double, PatternType> {
	private PatternType type;
	private List<ISeriesPoint<Date, Double>> points;

	public TrendMode() { }

	public TrendMode(PatternType type, List<ISeriesPoint<Date, Double>> points) {
		this.type = type;
		this.points = points;
	}
	
	public PatternType getType() {
		return type;
	}

	public void setType(PatternType type) {
		this.type = type;
	}

	public void setPoints(List<ISeriesPoint<Date, Double>> points) {
		this.points = points;
	}

	@Override
	public Date getBeginIndex() {
		return points.get(0).getIndex();
	}

	@Override
	public Date getEndIndex() {
		int endIndex = points.size()-1;
		return points.get(endIndex).getIndex();
	}

	@Override
	public List<ISeriesPoint<Date, Double>> getPoints() {
		return points;
	}

	@Override
	public List<ITrendLine<Date, Double>> getTrendLines() {
		return null;
	}

	@Override
	public String toString() {
		return "TrendMode [type=" + type + ", points=" + points + "]";
	}
}
