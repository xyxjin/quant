package com.pureblue.quant.TencentAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pureblue.quant.model.TimePeriod;

public class TencentFundDailyDao {
	public static final String INSERT_FIELDS = "SDB_ID, DATE_TIME, NET_VALUE, ASSESSMENT_VALUE, TOTAL_ASSETS";
	private final Connection connection;
	
	public TencentFundDailyDao(Connection connection) {
		this.connection = connection;
	}

	public void initDb(String tableName) throws SQLException {
		PreparedStatement stmt;
		int numberOfTables = 0;
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
					"DATE_TIME TIMESTAMP," +
					"NET_VALUE DECIMAL(30,10)," +
					"ASSESSMENT_VALUE DECIMAL(30,10)," +
					"TOTAL_ASSETS DECIMAL(30,10))");
			stmt.execute();
			flush();
		}
		rs.close();
	}
	
	public void save(String symbolId, Fund item) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + symbolId +
				"(" + INSERT_FIELDS + ") VALUES (?, ?, ?, ?, ?)");
		stmt.setString(1, symbolId);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		ts = Timestamp.valueOf(item.getDate());
		stmt.setTimestamp(2, ts);
		stmt.setDouble(3, item.getNetValue());
		stmt.setDouble(4, item.getAssessmentValue());
		stmt.setDouble(5, item.getAssets());
		stmt.execute();
		flush();
	}
	
	public List<IFund> findTicker(String symbolId, Date wantDate, Date priorDate) throws SQLException {
		String wantDateString = TimePeriod.formatDateToString(wantDate);
		String priorDateString = TimePeriod.formatDateToString(priorDate);
		PreparedStatement stmt = connection.prepareStatement("SELECT " + INSERT_FIELDS + " FROM " + symbolId + " WHERE DATE_TIME >= ? and DATE_TIME <= ? ORDER BY DATE_TIME");
		stmt.setString(1, priorDateString);
		stmt.setString(2, wantDateString);
		List<IFund> result = new LinkedList<IFund>();
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String date = TimePeriod.formatDateToString(new Date(rs.getTimestamp(2).getTime()));
			double netValue = rs.getDouble(3);
			double assessmentValue = rs.getDouble(4);
			double assets = rs.getDouble(5);
			Fund point = new Fund(date, netValue, assessmentValue, assets);
			result.add(point);
		}
		rs.close();
		return result;
	}
	
	public void deleteAll(String symbolId) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + symbolId + " WHERE SDB_ID = ?");
		stmt.setString(1, symbolId);
		stmt.execute();
		flush();
	}

	public void flush() throws SQLException {
		connection.commit();
	}
}
