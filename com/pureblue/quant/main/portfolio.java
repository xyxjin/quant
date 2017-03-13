package com.pureblue.quant.main;

import java.util.Currency;

import com.pureblue.quant.model.BareDate;
import com.pureblue.quant.model.IContract;
import com.pureblue.quant.model.IContractDesc;
import com.pureblue.quant.model.IdentifierType;
import com.pureblue.quant.model.OptionRight;
import com.pureblue.quant.model.SecurityType;

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
