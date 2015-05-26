package Tencent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;

public class TencentFundAdaptorDao {
	public static final String tableName = "fundZhongCang";
	private final Connection connection;
	
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
				System.out.println(rs.getString(1));
				numberOfTables++;
			}
		}
		if (numberOfTables == 0) {
			stmt = connection.prepareStatement("CREATE TABLE " + tableName + 
												" (SYMBOL VARCHAR(200)," + 
												"DATE_TIME TIMESTAMP" + 
												"CODE VARCHAR(6)" +
												"COUNT INT" +
												"ASSETS DECIMAL(30,10)" +
												"RATIO DECIMAL(30,10)" + ")"
												);
			System.out.println(stmt.toString());
			stmt.execute();
			flush();
		}
		rs.close();
	}
	
	public void save(LinkedList<FundStocks> stocks) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + tableName + " (SYMBOL, DATE_TIME, CODE, COUNT, ASSETS, RATIO) VALUES (?, ?, ?, ?, ?, ?)");
		Iterator<FundStocks> itr = stocks.iterator();
		while(itr.hasNext()){
			stmt.setString(1, itr.next().getSymbol());
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			ts = Timestamp.valueOf(itr.next().getDate());
			stmt.setTimestamp(2, ts);
			stmt.setString(3, itr.next().getCode());
			stmt.setInt(4, itr.next().getCount());
			stmt.setDouble(5, itr.next().getAssets());
			stmt.setDouble(6, itr.next().getRatio());
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
