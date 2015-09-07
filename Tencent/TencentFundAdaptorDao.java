package Tencent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class TencentFundAdaptorDao {
	public static final String tableName = "fundZhongCang";
	private final Connection connection;
	private Logger logger;
	
	public TencentFundAdaptorDao(Connection connection) {
		this.connection = connection;
		
	}
	
	public void initDb() throws SQLException {
		PreparedStatement stmt;
		int numberOfTables = 0;
		stmt = connection.prepareStatement("SHOW TABLES");
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			if (tableName.equalsIgnoreCase(rs.getString(1))){
//				System.out.println(rs.getString(1));
				numberOfTables++;
			}
		}
		if (numberOfTables == 0) {
			stmt = connection.prepareStatement("CREATE TABLE " + tableName + 
												" (SYMBOL VARCHAR(200), " + 
												"DATE_TIME VARCHAR(20), " + 
												"CODE VARCHAR(6), " +
												"COUNT INT, " +
												"ASSETS DECIMAL(30,2), " +
												"RATIO DECIMAL(30,2)" + ")"
												);
//			System.out.println(stmt.toString());
			stmt.execute();
			flush();
		}
		rs.close();
	}
	
	public void save(List<IFund> stocks) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("REPLACE INTO " + tableName + " (SYMBOL, DATE_TIME, CODE, COUNT, ASSETS, RATIO) VALUES (?, ?, ?, ?, ?, ?)");
		Iterator<IFund> itr = stocks.iterator();
//		System.out.println(stocks.toString());
		IFund point;
		while(itr.hasNext()){
			point = itr.next();
//			System.out.println(point.toString());
			stmt.setString(1, point.getSymbol());
			stmt.setString(2, point.getDate());
			stmt.setString(3, point.getCode());
			stmt.setInt(4, point.getCount());
			stmt.setDouble(5, point.getAssets());
			stmt.setDouble(6, point.getRatio());
//			System.out.println(stmt.toString());
			stmt.execute();
			flush();
		}	
	}
	
	public ArrayList<String> findSymbols() throws SQLException{
		ArrayList<String> symbols = new ArrayList<String>();
		PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT SYMBOL FROM " + tableName);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			symbols.add(rs.getString(1));
		}
		rs.close();
		return symbols;
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
