/*******************************************************************************
 * Copyright (c) 2013 Luigi Sgro. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luigi Sgro - initial API and implementation
 ******************************************************************************/
package com.pureblue.quant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.OHLCPoint;
import com.pureblue.quant.model.BarSize;
import com.pureblue.quant.model.TimePeriod;

public class OHLCPointDao implements IOHLCPointDao {
	public static final String TABLE_NAME = "OHLC";
	public static final String SELECT_FIELDS = "BAR_SIZE, DATE_TIME," +
			" P_OPEN, P_HIGH, P_LOW, P_CLOSE, VOLUME, P_ADJ_CLOSE, P_WAP, TICK_COUNT, LAST_UPDATE";
	public static final String INSERT_FIELDS = "SDB_ID, " + SELECT_FIELDS;
	private final Connection connection;
	private Logger logger;
	
	public OHLCPointDao(Connection connection) {
		this.connection = connection;
		this.logger = Logger.getLogger(getClass());
	}

	@Override
	public void initDb(String tableName) throws SQLException {
	    logger.debug("OHLCPointDao::initDb: init SQL table:" + tableName + " entry.");
		PreparedStatement stmt;
		int numberOfTables = 0;
		tableName = "tb"+tableName;
		stmt = connection.prepareStatement("SHOW TABLES");
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			if (tableName.equalsIgnoreCase(rs.getString(1))){
				numberOfTables++;
			}
		}
		if (numberOfTables == 0) {
			stmt = connection.prepareStatement("CREATE TABLE " + tableName +
					" (ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT," + 
					"SDB_ID VARCHAR(200)," +
					"BAR_SIZE VARCHAR(20)," +
					"DATE_TIME TIMESTAMP," +
					"P_OPEN DECIMAL(30,10)," +
					"P_HIGH DECIMAL(30,10)," +
					"P_LOW DECIMAL(30,10)," +
					"P_CLOSE DECIMAL(30,10)," +
					"VOLUME BIGINT," +
					"P_ADJ_CLOSE DECIMAL(30,10),"+
					"P_WAP DECIMAL(30,10)," +
					"TICK_COUNT INT," +
					"LAST_UPDATE TIMESTAMP)");
			stmt.execute();
			connection.commit();
		}
		rs.close();
		logger.debug("OHLCPointDao::initDb: init SQL table:" + tableName + " exit!");
	}

	@Override
	public void save(String stockDatabaseId, IOHLCPoint item) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + "tb" + stockDatabaseId +
				" (" + INSERT_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		stmt.setString(1, stockDatabaseId);
		stmt.setString(2, item.getBarSize().name());
		stmt.setTimestamp(3, new Timestamp(item.getIndex().getTime()));
		stmt.setDouble(4, item.getOpen());
		stmt.setDouble(5, item.getHigh());
		stmt.setDouble(6, item.getLow());
		stmt.setDouble(7, item.getClose());
		stmt.setLong(8, item.getVolume());
		stmt.setDouble(9, item.getAdjClose());
		stmt.setDouble(10, item.getWAP());
		stmt.setInt(11, item.getCount());
		if (item.getLastUpdate() != null) {
			stmt.setTimestamp(12, new Timestamp(item.getLastUpdate().getTime()));
		} else {
			stmt.setNull(12, Types.TIMESTAMP);
		}
		stmt.execute();
		logger.info(stmt.toString());
	}

	@Override
	public void update(String stockDatabaseId, IOHLCPoint existingItem, IOHLCPoint newItem) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("UPDATE " + "tb" + stockDatabaseId + " SET " +
				"P_OPEN = ?, P_HIGH = ?, P_LOW = ?, P_CLOSE = ?, VOLUME = ?, P_ADJ_CLOSE = ?, P_WAP = ?, TICK_COUNT = ?, LAST_UPDATE = ?" +
				" WHERE SDB_ID = ? AND DATE_TIME = ?");
		// updated fields
		stmt.setDouble(1, newItem.getOpen());
		stmt.setDouble(2, newItem.getHigh());
		stmt.setDouble(3, newItem.getLow());
		stmt.setDouble(4, newItem.getClose());
		stmt.setLong(5, newItem.getVolume());
		stmt.setDouble(6, newItem.getAdjClose());
		stmt.setDouble(7, newItem.getWAP());
		stmt.setInt(8, newItem.getCount());
		if (newItem.getLastUpdate() != null) {
			stmt.setTimestamp(9, new Timestamp(newItem.getLastUpdate().getTime()));
		} else {
			stmt.setNull(9, Types.TIMESTAMP);
		}
		// selection criteria
		stmt.setString(10, stockDatabaseId);
		stmt.setTimestamp(11, new Timestamp(newItem.getIndex().getTime()));
		int rows = stmt.executeUpdate();
		if (rows == 0) {
			throw new IllegalArgumentException("This item is not stored in the database: " + "tb" + stockDatabaseId + ";" + newItem.getIndex());
		}
		if (rows > 1) {
			throw new IllegalArgumentException("Multiple items for these parameters: " + "tb" + stockDatabaseId + ";" + newItem.getIndex());
		}
	}

	@Override
	public List<IOHLCPoint> find(String stockDatabaseId) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM " + "tb" + stockDatabaseId + " WHERE SDB_ID = ? ORDER BY DATE_TIME");
		stmt.setString(1, stockDatabaseId);
		List<IOHLCPoint> result = new LinkedList<IOHLCPoint>();
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			BarSize barSize = BarSize.valueOf(rs.getString(1));
			Date date = new Date(rs.getTimestamp(2).getTime());
			Double open = rs.getDouble(3);
			Double high = rs.getDouble(4);
			Double low = rs.getDouble(5);
			Double close = rs.getDouble(6);
			Long volume = rs.getLong(7);
			Double adjClose = rs.getDouble(8);
			Double wap = rs.getDouble(9);
			Integer count = rs.getInt(10);
			OHLCPoint point = new OHLCPoint(barSize, date, open, high, low, close, volume, adjClose, wap, count);
			Timestamp lastUpdateTs = rs.getTimestamp(11);
			if (lastUpdateTs != null) {
				point.setLastUpdate(new Date(lastUpdateTs.getTime()));
			}
			result.add(point);
		}
		rs.close();
		return result;
	}
	
	public List<IOHLCPoint> findTick(String stockDatabaseId, Date wantDate) throws SQLException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String wantDateString = formatter.format(wantDate);
		PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM " + "tb" + stockDatabaseId + " WHERE SDB_ID = ? and DATE_TIME = ? ORDER BY DATE_TIME");
		stmt.setString(1, stockDatabaseId);
		stmt.setString(2, wantDateString);
		List<IOHLCPoint> result = new LinkedList<IOHLCPoint>();
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			BarSize barSize = BarSize.valueOf(rs.getString(1));
			Date date = new Date(rs.getTimestamp(2).getTime());
			Double open = rs.getDouble(3);
			Double high = rs.getDouble(4);
			Double low = rs.getDouble(5);
			Double close = rs.getDouble(6);
			Long volume = rs.getLong(7);
			Double adjClose = rs.getDouble(8);
			Double wap = rs.getDouble(9);
			Integer count = rs.getInt(10);
			OHLCPoint point = new OHLCPoint(barSize, date, open, high, low, close, volume, adjClose, wap, count);
			Timestamp lastUpdateTs = rs.getTimestamp(11);
			if (lastUpdateTs != null) {
				point.setLastUpdate(new Date(lastUpdateTs.getTime()));
			}
			result.add(point);
		}
		rs.close();
		return result;
	}
	
	public List<IOHLCPoint> findTicker(String stockDatabaseId, Date wantDate, Date priorDate) throws SQLException {
		String wantDateString = TimePeriod.formatDateToString(wantDate);
		String priorDateString = TimePeriod.formatDateToString(priorDate);
		PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM " + "tb" + stockDatabaseId + " WHERE SDB_ID = ? and DATE_TIME >= ? and DATE_TIME <= ? ORDER BY DATE_TIME");
		stmt.setString(1, stockDatabaseId);
		stmt.setString(2, priorDateString);
		stmt.setString(3, wantDateString);
		List<IOHLCPoint> result = new LinkedList<IOHLCPoint>();
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			BarSize barSize = BarSize.valueOf(rs.getString(1));
			Date date = new Date(rs.getTimestamp(2).getTime());
			Double open = rs.getDouble(3);
			Double high = rs.getDouble(4);
			Double low = rs.getDouble(5);
			Double close = rs.getDouble(6);
			Long volume = rs.getLong(7);
			Double adjClose = rs.getDouble(8);
			Double wap = rs.getDouble(9);
			Integer count = rs.getInt(10);
			OHLCPoint point = new OHLCPoint(barSize, date, open, high, low, close, volume, adjClose, wap, count);
			Timestamp lastUpdateTs = rs.getTimestamp(11);
			if (lastUpdateTs != null) {
				point.setLastUpdate(new Date(lastUpdateTs.getTime()));
			}
			result.add(point);
		}
		rs.close();
		return result;
	}

	public Date lastDate(String stockDatabaseId) throws SQLException {
		Date result = TimePeriod.formatStringToDate("2003-1-1");		
		PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM " + "tb" + stockDatabaseId + " WHERE SDB_ID = ? ORDER BY DATE_TIME");
		stmt.setString(1, stockDatabaseId);
		ResultSet rs = stmt.executeQuery();
		if (rs.last()){
			result = new Date(rs.getTimestamp(2).getTime());
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(result);
			rightNow.add(Calendar.DAY_OF_YEAR, 1);
			result = rightNow.getTime();
		}
		rs.close();
		return result;
	}
	
	public Date realLastDate(String stockDatabaseId) throws SQLException {
		Date result = TimePeriod.formatStringToDate("2003-1-1");		
		PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM " + "tb" + stockDatabaseId + " WHERE SDB_ID = ? ORDER BY DATE_TIME");
		stmt.setString(1, stockDatabaseId);
		ResultSet rs = stmt.executeQuery();
		if (rs.last()){
			result = new Date(rs.getTimestamp(2).getTime());
		}
		rs.close();
		return result;
	}
	
	@Override
	public void deleteAll(String stockDatabaseId) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + "tb" + stockDatabaseId + " WHERE SDB_ID = ?");
		stmt.setString(1, stockDatabaseId);
		stmt.execute();
	}

	@Override
	public void flush() throws SQLException {
		connection.commit();
	}

	@Override
	public void initDb() throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
