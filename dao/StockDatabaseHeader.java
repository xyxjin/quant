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

import java.util.TimeZone;
import model.BarSize;
import model.DataType;
import model.IContract;
import dao.IStockDatabase;
//import model.beans.ImmutableContractBean;

public class StockDatabaseHeader {
	public static StockDatabaseHeader fromStockDatabase(IStockDatabase stockDatabase) {
		StockDatabaseHeader bean = new StockDatabaseHeader();
		bean.id = stockDatabase.getPersistentID();
		bean.timestamp = stockDatabase.getTimestamp();
		bean.timeZone = stockDatabase.getTimeZone();
		//bean.contract = new ImmutableContractBean(stockDatabase.getContract());
		bean.dataType = stockDatabase.getDataType();
		bean.barSize = stockDatabase.getBarSize();
		bean.includeAfterHours = stockDatabase.isIncludeAfterHours();
		return bean;
	}
	
	public String id;
	public Long timestamp;
	public TimeZone timeZone;
	public IContract contract;
	public DataType dataType;
	public BarSize barSize;
	public Boolean includeAfterHours;
}
