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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.pureblue.quant.model.TimePeriod;

public class CapitalPointDao implements ICapitalPointDao {
    public static final String TABLE_NAME_SUFFIX = "CAPITAL";
    public static final String SELECT_FIELDS = "DATE_TIME, MAIN_INFLOW, MAIN_OUTFLOW, MAIN_NETFLOW, MAIN_RATE, RETAIL_INFLOW, RETAIL_OUTFLOW, RETAIL_NETFLOW, RETAIL_RATE, VOLUME, P_PRICE, TURNOVER_RATE, PER, MARKET_VALUE, TATAL_VALUE, PBR, LAST_UPDATE";
    public static final String INSERT_FIELDS = "SDB_ID, " + SELECT_FIELDS;
    private final Connection connection;
    private final String stockDatabaseId;
    private final String tableName;
    private Logger logger;

    public CapitalPointDao(Connection connection, String stockDatabaseId) {
        this.connection = connection;
        this.stockDatabaseId = stockDatabaseId;
        this.tableName = stockDatabaseId.replace(".", "_") + "_" + TABLE_NAME_SUFFIX;
        this.logger = Logger.getLogger(CapitalPointDao.class);
        logger.info("CapitalPointDao::constructor: Dao of capital point for " + stockDatabaseId);
    }

    @Override
    public void delete(String id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName
                + " WHERE ID = ?");
        stmt.setString(1, id);
        stmt.execute();
    }

    @Override
    public void deleteAll() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName
                + " WHERE SDB_ID = ?");
        stmt.setString(1, stockDatabaseId);
        stmt.execute();
    }

    @Override
    public List<ICapitalPoint> findall() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM "
                + tableName + " WHERE SDB_ID = ? ORDER BY DATE_TIME");
        stmt.setString(1, stockDatabaseId);
        List<ICapitalPoint> result = new LinkedList<ICapitalPoint>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Date date = new Date(rs.getTimestamp(2).getTime());
            Double mainInflow = rs.getDouble(3);
            Double mainOutflow = rs.getDouble(4);
            Double mainNetflow = rs.getDouble(5);
            Double mainRate = rs.getDouble(6);
            Double retailInflow = rs.getDouble(7);
            Double retailOutflow = rs.getDouble(8);
            Double retailNetflow = rs.getDouble(9);
            Double retailRate = rs.getDouble(10);
            Double volumn = rs.getDouble(11);
            Double price = rs.getDouble(12);
            Double turnoverRate = rs.getDouble(13);
            Double PER = rs.getDouble(14);
            Double marketValue = rs.getDouble(15);
            Double totalValue = rs.getDouble(16);
            Double PBR = rs.getDouble(17);
            CapitalPoint point = new CapitalPoint(date, mainInflow, mainOutflow, mainNetflow,
                    mainRate, retailInflow, retailOutflow, retailNetflow, retailRate, volumn,
                    price, turnoverRate, PER, marketValue, totalValue, PBR);
            Timestamp lastUpdateTs = rs.getTimestamp(18);
            if (lastUpdateTs != null) {
                point.setLastUpdate(new Date(lastUpdateTs.getTime()));
            }
            result.add(point);
        }
        rs.close();
        return result;
    }

    public List<ICapitalPoint> findTick(Date wantDate) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String wantDateString = formatter.format(wantDate);
        PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM "
                + tableName + " WHERE SDB_ID = ? and DATE_TIME = ? ORDER BY DATE_TIME");

        System.out.println(stmt.toString());

        stmt.setString(1, stockDatabaseId);
        stmt.setString(2, wantDateString);
        List<ICapitalPoint> result = new LinkedList<ICapitalPoint>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Date date = new Date(rs.getTimestamp(2).getTime());
            Double mainInflow = rs.getDouble(3);
            Double mainOutflow = rs.getDouble(4);
            Double mainNetflow = rs.getDouble(5);
            Double mainRate = rs.getDouble(6);
            Double retailInflow = rs.getDouble(7);
            Double retailOutflow = rs.getDouble(8);
            Double retailNetflow = rs.getDouble(9);
            Double retailRate = rs.getDouble(10);
            Double volumn = rs.getDouble(11);
            Double price = rs.getDouble(12);
            Double turnoverRate = rs.getDouble(13);
            Double PER = rs.getDouble(14);
            Double marketValue = rs.getDouble(15);
            Double totalValue = rs.getDouble(16);
            Double PBR = rs.getDouble(17);
            CapitalPoint point = new CapitalPoint(date, mainInflow, mainOutflow, mainNetflow,
                    mainRate, retailInflow, retailOutflow, retailNetflow, retailRate, volumn,
                    price, turnoverRate, PER, marketValue, totalValue, PBR);
            Timestamp lastUpdateTs = rs.getTimestamp(18);
            if (lastUpdateTs != null) {
                point.setLastUpdate(new Date(lastUpdateTs.getTime()));
            }
            result.add(point);
        }
        rs.close();
        return result;
    }

    public List<ICapitalPoint> findTicker(Date wantDate, Date priorDate)
            throws SQLException {
        String wantDateString = TimePeriod.formatDateToString(wantDate);
        String priorDateString = TimePeriod.formatDateToString(priorDate);
        PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM "
                + tableName
                + " WHERE SDB_ID = ? and DATE_TIME >= ? and DATE_TIME <= ? ORDER BY DATE_TIME");
        stmt.setString(1, stockDatabaseId);
        stmt.setString(2, priorDateString);
        stmt.setString(3, wantDateString);
        List<ICapitalPoint> result = new LinkedList<ICapitalPoint>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Date date = new Date(rs.getTimestamp(2).getTime());
            Double mainInflow = rs.getDouble(3);
            Double mainOutflow = rs.getDouble(4);
            Double mainNetflow = rs.getDouble(5);
            Double mainRate = rs.getDouble(6);
            Double retailInflow = rs.getDouble(7);
            Double retailOutflow = rs.getDouble(8);
            Double retailNetflow = rs.getDouble(9);
            Double retailRate = rs.getDouble(10);
            Double volumn = rs.getDouble(11);
            Double price = rs.getDouble(12);
            Double turnoverRate = rs.getDouble(13);
            Double PER = rs.getDouble(14);
            Double marketValue = rs.getDouble(15);
            Double totalValue = rs.getDouble(16);
            Double PBR = rs.getDouble(17);
            CapitalPoint point = new CapitalPoint(date, mainInflow, mainOutflow, mainNetflow,
                    mainRate, retailInflow, retailOutflow, retailNetflow, retailRate, volumn,
                    price, turnoverRate, PER, marketValue, totalValue, PBR);
            Timestamp lastUpdateTs = rs.getTimestamp(18);
            if (lastUpdateTs != null) {
                point.setLastUpdate(new Date(lastUpdateTs.getTime()));
            }
            result.add(point);
        }
        rs.close();
        return result;
    }

    @Override
    public void flush() throws SQLException {
        connection.commit();
    }

    @Override
    public void initDb() throws SQLException {
        logger.debug("CapitalPointDao::initDb: Dao of capital point table " + tableName
                + " init entry.");
        PreparedStatement stmt;
        int numberOfTables = 0;
        stmt = connection.prepareStatement("SHOW TABLES");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            if (tableName.equalsIgnoreCase(rs.getString(1))) {
                numberOfTables++;
                logger.info("CapitalPointDao::initDb: Dao of capital point table " + tableName
                        + " already been created.");
            }
        }
        if (numberOfTables == 0) {
            stmt = connection.prepareStatement("CREATE TABLE " + tableName
                    + " (ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT ," + "SDB_ID VARCHAR(200),"
                    + "DATE_TIME TIMESTAMP," + "MAIN_INFLOW DECIMAL(30,10),"
                    + "MAIN_OUTFLOW DECIMAL(30,10)," + "MAIN_NETFLOW DECIMAL(30,10),"
                    + "MAIN_RATE DECIMAL(30,10)," + "RETAIL_INFLOW DECIMAL(30,10),"
                    + "RETAIL_OUTFLOW DECIMAL(30,10)," + "RETAIL_NETFLOW DECIMAL(30,10),"
                    + "RETAIL_RATE DECIMAL(30,3)," + "VOLUME DECIMAL(30,10),"
                    + "P_PRICE DECIMAL(30,3)," + "TURNOVER_RATE DECIMAL(30,10),"
                    + "PER DECIMAL(30,3)," + "MARKET_VALUE DECIMAL(30,10),"
                    + "TATAL_VALUE DECIMAL(30,10)," + "PBR DECIMAL(30,10),"
                    + "LAST_UPDATE TIMESTAMP)");
            logger.info("CapitalPointDao::initDb: TDao of capital point table " + tableName
                    + " create successfully with " + stmt.toString());
            stmt.execute();
            flush();
        }
        rs.close();
        logger.debug("CapitalPointDao::initDb: Dao of capital point table " + tableName
                + " init exit!");
    }

    @Override
    public void initDb(String tableName) throws SQLException {
        // TODO Auto-generated method stub

    }

    public Date lastDate() throws SQLException {
        Date result = TimePeriod.formatStringToDate("2014-1-1");
        PreparedStatement stmt = connection.prepareStatement("SELECT " + SELECT_FIELDS + " FROM "
                + tableName + " WHERE SDB_ID = ? ORDER BY DATE_TIME");
        stmt.setString(1, stockDatabaseId);
        ResultSet rs = stmt.executeQuery();
        if (rs.last()) {

            result = new Date(rs.getTimestamp(1).getTime());
/*            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(result);
            rightNow.add(Calendar.DAY_OF_YEAR, 1);
            result = rightNow.getTime();*/
        }
        rs.close();
        return result;
    }

    @Override
    public void save(ICapitalPoint item) throws SQLException {
        if(null==item)
            return;
        PreparedStatement stmt = connection
                .prepareStatement("INSERT INTO " + tableName + " (" + INSERT_FIELDS
                        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        stmt.setString(1, stockDatabaseId);
        stmt.setTimestamp(2, new Timestamp(item.getIndex().getTime()));
        stmt.setDouble(3, item.getMainInflow());
        stmt.setDouble(4, item.getMainOutflow());
        stmt.setDouble(5, item.getMainNetflow());
        stmt.setDouble(6, item.getMainRate());
        stmt.setDouble(7, item.getRetailInflow());
        stmt.setDouble(8, item.getRetailOutflow());
        stmt.setDouble(9, item.getRetailNetflow());
        stmt.setDouble(10, item.getRetailRate());
        stmt.setDouble(11, item.getVolumn());
        stmt.setDouble(12, item.getPrice());
        stmt.setDouble(13, item.getTurnoverRate());
        stmt.setDouble(14, item.getPER());
        stmt.setDouble(15, item.getMarketValue());
        stmt.setDouble(16, item.getTotalValue());
        stmt.setDouble(17, item.getPBR());
        if (item.getLastUpdate() != null) {
            stmt.setTimestamp(18, new Timestamp(item.getLastUpdate().getTime()));
        } else {
            stmt.setNull(18, Types.TIMESTAMP);
        }
        logger.info(stmt.toString());
        // System.out.println(stmt.toString());
        stmt.execute();
    }

    @Override
    public void update(ICapitalPoint existingItem, ICapitalPoint newItem)
            throws SQLException {
        PreparedStatement stmt = connection
                .prepareStatement("UPDATE "
                        + tableName
                        + " SET "
                        + "	MAIN_INFLOW = ?, MAIN_OUTFLOW = ?, MAIN_NETFLOW = ?, MAIN_RATE = ?, RETAIL_INFLOW = ?, RETAIL_OUTFLOW = ?, RETAIL_NETFLOW = ?, RETAIL_RATE = ?, VOLUME = ?, P_PRICE = ?, TURNOVER_RATE = ?, PER = ?, MARKET_VALUE = ?, TATAL_VALUE = ?, PBR = ? LAST_UPDATE = ?"
                        + " WHERE SDB_ID = ? AND DATE_TIME = ?");
        // updated fields
        stmt.setDouble(1, newItem.getMainInflow());
        stmt.setDouble(2, newItem.getMainOutflow());
        stmt.setDouble(3, newItem.getMainNetflow());
        stmt.setDouble(4, newItem.getMainRate());
        stmt.setDouble(5, newItem.getRetailInflow());
        stmt.setDouble(6, newItem.getRetailOutflow());
        stmt.setDouble(7, newItem.getRetailNetflow());
        stmt.setDouble(8, newItem.getRetailRate());
        stmt.setDouble(9, newItem.getVolumn());
        stmt.setDouble(10, newItem.getPrice());
        stmt.setDouble(11, newItem.getTurnoverRate());
        stmt.setDouble(12, newItem.getPER());
        stmt.setDouble(13, newItem.getMarketValue());
        stmt.setDouble(14, newItem.getTotalValue());
        stmt.setDouble(15, newItem.getPBR());
        if (newItem.getLastUpdate() != null) {
            stmt.setTimestamp(16, new Timestamp(newItem.getLastUpdate().getTime()));
        } else {
            stmt.setNull(16, Types.TIMESTAMP);
        }
        // selection criteria
        stmt.setString(17, stockDatabaseId);
        stmt.setTimestamp(18, new Timestamp(newItem.getIndex().getTime()));
        int rows = stmt.executeUpdate();
        if (rows == 0) {
            throw new IllegalArgumentException("This item is not stored in the database: "
                    + stockDatabaseId + ";" + newItem.getIndex());
        }
        if (rows > 1) {
            throw new IllegalArgumentException("Multiple items for these parameters: "
                    + stockDatabaseId + ";" + newItem.getIndex());
        }
    }

}
