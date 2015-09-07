package main;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.LoggerUtils;
import dao.CapitalPointDao;
import dao.ICapitalPoint;
import dao.StockDatebaseFactory;
import Tencent.TencentRealTimeAdapterComponent;


public class TencentRealTimeThread implements Runnable{
	private String dbName;
	private String stockId;
	private Connection connection;
	private Logger logger;
	
	public TencentRealTimeThread(String dbName, String stockId) {
		super();
		this.dbName = dbName;
		this.stockId = stockId;
		this.logger = LoggerUtils.getLogger(LoggerUtils.path);
	}

	@Override
	public void run() {
		logger.info("Tencent Realtime Thread for " + stockId + " starting!!!");
		ICapitalPoint point;
		TencentRealTimeAdapterComponent adapter = new TencentRealTimeAdapterComponent();
		try{
			connection = StockDatebaseFactory.getInstance(dbName);
			CapitalPointDao dao = new CapitalPointDao(connection, stockId);
			dao.initDb();
			point = adapter.latestQuotation(stockId);
			dao.save(stockId, point);
			dao.flush();
		}catch (SQLException e) {
			//e.printStackTrace();
		}catch (Exception e) {
            //e.printStackTrace();
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.info("Tencent Realtime Thread for " + stockId + " end!!!"); 
	}

}
