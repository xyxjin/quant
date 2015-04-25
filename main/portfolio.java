package main;

import java.util.Currency;

import model.BareDate;
import model.IContract;
import model.IContractDesc;
import model.IdentifierType;
import model.OptionRight;
import model.SecurityType;

public class portfolio implements IContract{
	private String symbol;
	private Currency currency;
	
	public portfolio(String symbol, Currency currency) {
		this.symbol = symbol;
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public SecurityType getSecurityType() {
		return null;
	}

	public BareDate getExpiryDate() {
		return null;
	}

	public Double getStrike() {
		return null;
	}

	public OptionRight getOptionRight() {
		return null;
	}

	public Integer getMultiplier() {
		return null;
	}

	public String getExchange() {
		return null;
	}

	public String getPrimaryExchange() {
		return null;
	}

	public Currency getCurrency() {
		return currency;
	}

	public IdentifierType getIdentifierType() {
		return null;
	}

	public String getIdentifier() {
		return null;
	}

	public IContractDesc getContractDescription() {
		return null;
	}

	public String getBrokerID() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
