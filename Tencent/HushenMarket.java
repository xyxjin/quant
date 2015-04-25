package Tencent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class HushenMarket {
	public static final String tableName = "marketStockCode";
	Connection connection;
	
	public HushenMarket(Connection connection) {
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
			stmt = connection.prepareStatement("CREATE TABLE " + tableName + " (SDB_ID VARCHAR(200))");
			System.out.println(stmt.toString());
			stmt.execute();
			connection.commit();
		}else
			deleteAll();
		rs.close();
	}
	
	public void save(Set<String> quotes) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + tableName + " (SDB_ID) VALUES (?)");
		Iterator<String> itr = quotes.iterator();
		while(itr.hasNext()){
			stmt.setString(1, itr.next());
			stmt.execute();
		}
		connection.commit();
	}
	
	public void update(Set<String> quotes) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("REPLACE INTO " + tableName + " (SDB_ID) VALUES (?)");
		Iterator<String> itr = quotes.iterator();
		while(itr.hasNext()){
			String code = itr.next();
			stmt.setString(1, code);
			stmt.execute();
		}
		flush();
	}
	
	public Set<String> findAll() throws SQLException {
		Set<String> quotes = new HashSet<String>();;
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + tableName);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String stockId = rs.getString(1);
			quotes.add(stockId);
		}
		rs.close();
		return quotes;
	}
	
	@SuppressWarnings("resource")
	public Set<String> processOneSheet(String filename) throws Exception {
		Set<String> quotes= new HashSet<String>();
		HSSFWorkbook workbook;
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(filename));
			workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = worksheet.iterator();
			while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                quotes.add(row.getCell(0).toString().substring(1, 7));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}
		return quotes;
	}
	
	public void deleteAll() throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName);
		stmt.execute();
		flush();
	}

	public void flush() throws SQLException {
		connection.commit();
	}
	
	public void importMarket(){
		Set<String> quotes;
		try {
			initDb();
			quotes = processOneSheet("D:\\Personal\\B.xls");
			save(quotes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
