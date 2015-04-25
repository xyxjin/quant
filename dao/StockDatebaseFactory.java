package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StockDatebaseFactory {
	public StockDatebaseFactory() {
		// TODO Auto-generated constructor stub
	}
	public static Connection getInstance(String dataBase) throws SQLException {
		Connection connection = null;
		String url = "jdbc:mysql://127.0.0.1:3306/" + dataBase;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("成功加载MySQL驱动程序");
			connection = DriverManager.getConnection(url,"root",null);
			connection.setAutoCommit(false);
		} catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
		}catch (Exception e) {
            e.printStackTrace();
        } 
		return connection;
	}
}
