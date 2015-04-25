/*******************************************************************************
 * Copyright (c) 2013 Luigi Sgro. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luigi Sgro - initial API and implementation
 ******************************************************************************/
package dao;

import java.io.Serializable;
import java.util.Date;

import model.DataType;

/**
 * Implementation of {@link ITickPoint}
 */
public class CapitalPoint implements Serializable, ICapitalPoint {
	private static final long serialVersionUID = 1420587711067980277L;
	private final Date index;
	private final double mainInflow;
	private final double mainOutflow;
	private final double mainNetflow;
	private final double mainRate;
	private final double retailInflow;
	private final double retailOutflow;
	private final double retailNetflow;
	private final double retailRate;
	private final double volumn;
	private final double price;
	private final double turnoverRate;
	private final double PER;
	private final double marketValue;
	private final double totalValue;
	private final double PBR;
	private Date lastUpdate;

	
	public CapitalPoint(Date index, double mainInflow, double mainOutflow,
			double mainNetflow, double mainRate, double retailInflow,
			double retailOutflow, double retailNetflow, double retailRate,
			double volumn, double price, double turnoverRate, double pER,
			double marketValue, double totalValue, double pBR) {
		super();
		this.index = index;
		this.mainInflow = mainInflow;
		this.mainOutflow = mainOutflow;
		this.mainNetflow = mainNetflow;
		this.mainRate = mainRate;
		this.retailInflow = retailInflow;
		this.retailOutflow = retailOutflow;
		this.retailNetflow = retailNetflow;
		this.retailRate = retailRate;
		this.volumn = volumn;
		this.price = price;
		this.turnoverRate = turnoverRate;
		this.PER = pER;
		this.marketValue = marketValue;
		this.totalValue = totalValue;
		this.PBR = pBR;
	}

	@Override
	public Date getIndex() {
		return index;
	}

	@Override
	public Date getStartIndex() {
		return getIndex();
	}

	@Override
	public Date getEndIndex() {
		return getIndex();
	}

	public double getMainInflow() {
		return mainInflow;
	}

	public double getMainOutflow() {
		return mainOutflow;
	}

	public double getMainNetflow() {
		return mainNetflow;
	}

	public double getMainRate() {
		return mainRate;
	}

	public double getRetailInflow() {
		return retailInflow;
	}

	public double getRetailOutflow() {
		return retailOutflow;
	}

	public double getRetailNetflow() {
		return retailNetflow;
	}

	public double getRetailRate() {
		return retailRate;
	}

	
	public double getVolumn() {
		return volumn;
	}

	public double getPrice() {
		return price;
	}

	public double getTurnoverRate() {
		return turnoverRate;
	}

	public double getPER() {
		return PER;
	}

	public double getMarketValue() {
		return marketValue;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public double getPBR() {
		return PBR;
	}

	@Override
	public Double getBottomValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTopValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "CapitalPoint [index=" + index + ", mainInflow=" + mainInflow
				+ ", mainOutflow=" + mainOutflow + ", mainNetflow="
				+ mainNetflow + ", mainRate=" + mainRate + ", retailInflow="
				+ retailInflow + ", retailOutflow=" + retailOutflow
				+ ", retailNetflow=" + retailNetflow + ", retailRate="
				+ retailRate + ", volumn=" + volumn + ", price=" + price
				+ ", turnoverRate=" + turnoverRate + ", PER=" + PER
				+ ", marketValue=" + marketValue + ", totalValue=" + totalValue
				+ ", PBR=" + PBR + "]";
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;		
	}

	@Override
	public Date getLastUpdate() {
		// TODO Auto-generated method stub
		return lastUpdate;
	}
}
