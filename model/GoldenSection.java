package model;

public enum GoldenSection {
	LEVEL_ONE(0.191),LEVEL_TWO(0.382),LEVEL_THREE(0.5),LEVEL_FOUR(0.618),LEVEL_FIVE(0.809);
	
	private double value = 0;
	
	private GoldenSection(double value) {
        this.value = value;
    }
	
	public double value() {
        return this.value;
    }
}
