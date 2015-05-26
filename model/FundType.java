package model;

public enum FundType {
	BOND(1),MONEY(2),INDEX(3),STOCK(4);
	
	private int value = 0;
	
	private FundType(int value) {
        this.value = value;
    }
	
	public int value() {
        return this.value;
    }
}
