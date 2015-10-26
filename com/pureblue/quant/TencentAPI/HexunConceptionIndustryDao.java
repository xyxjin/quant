package com.pureblue.quant.TencentAPI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.sql.Connection;

public class HexunConceptionIndustryDao {
	public static final String tableName = "conceptionIndustry";
	private final Connection connection;
	
	public HexunConceptionIndustryDao(Connection connection) {
		this.connection = connection;
	}
	
	public void initDb() throws SQLException {
		PreparedStatement stmt;
		int numberOfTables = 0;
		stmt = connection.prepareStatement("SHOW TABLES");
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			if (tableName.equalsIgnoreCase(rs.getString(1))){
				System.out.println(rs.getString(1));
				numberOfTables++;
			}
		}
		if (numberOfTables == 0) {
			stmt = connection.prepareStatement("CREATE TABLE " + tableName + " (SDB_ID VARCHAR(200)," + "GN_NAME VARCHAR(20))");
			System.out.println(stmt.toString());
			stmt.execute();
			flush();
		}
		rs.close();
	}
	
	public void save(Map<String, ArrayList<String>> industry) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + tableName + " (SDB_ID, GN_NAME) VALUES (?, ?)");
		for(Map.Entry<String, ArrayList<String>> entry:industry.entrySet()){
			stmt.setString(2, entry.getKey());
			for(String stockId:entry.getValue()){
				stmt.setString(1, stockId);
				stmt.execute();
			}
		}
		flush();
	}
	
	public void update(Map<String, ArrayList<String>> industry) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("REPLACE INTO " + tableName + " (SDB_ID, GN_NAME) VALUES (?, ?)");
		for(Map.Entry<String, ArrayList<String>> entry:industry.entrySet()){
			stmt.setString(2, entry.getKey());
			for(String stockId:entry.getValue()){
				stmt.setString(1, stockId);
				stmt.execute();
			}
		}
		flush();
	}
	
	public void deleteAll() throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName);
		stmt.execute();
		flush();
	}

	public void flush() throws SQLException {
		connection.commit();
	}

}
