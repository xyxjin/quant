package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import util.LoggerUtils;
import Tencent.IFund;
import Tencent.TencentFundAdaptor;
import Tencent.TencentFundAdaptorDao;
import dao.StockDatebaseFactory;

public class TencentFundZhongcang implements Runnable {
	private String dbName;
	private Connection connection;
	private Logger logger;
	
	public TencentFundZhongcang(String dbName) {
		super();
		this.dbName = dbName;
		this.logger = LoggerUtils.getLogger(LoggerUtils.path);
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " start!!");
		List<IFund> points;
		TencentFundAdaptor adapter = new TencentFundAdaptor();
		try{
			connection = StockDatebaseFactory.getInstance(dbName);
			TencentFundAdaptorDao dao = new TencentFundAdaptorDao(connection);
			dao.initDb();
			points = adapter.fetchAllFunds();
//			System.out.println(points.toString());
			dao.save(points);
			dao.flush();
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
            e.printStackTrace();
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName() + " end!!"); 
	}

}
