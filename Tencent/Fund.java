package Tencent;

public class Fund implements IFund{
	String date;
	double netValue;
	double assessmentValue;
	double assets;
	
	public Fund(String date, double netValue, double assessmentValue,
			double assets) {
		this.date = date;
		this.netValue = netValue;
		this.assessmentValue = assessmentValue;
		this.assets = assets;
	}

	public String getDate() {
		return date;
	}

	public double getNetValue() {
		return netValue;
	}

	public double getAssets() {
		return assets;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setNetValue(double netValue) {
		this.netValue = netValue;
	}

	public void setAssets(double assets) {
		this.assets = assets;
	}

	public double getAssessmentValue() {
		return assessmentValue;
	}

	public void setAssessmentValue(double assessmentValue) {
		this.assessmentValue = assessmentValue;
	}

	@Override
	public String toString() {
		return "[date=" + date + ", netValue=" + netValue
				+ ", assessmentValue=" + assessmentValue + ", assets=" + assets
				+ "]";
	}
}
