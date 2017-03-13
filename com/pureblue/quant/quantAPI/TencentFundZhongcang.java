package com.pureblue.quant.quantAPI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.pureblue.quant.TencentAPI.IFund;
import com.pureblue.quant.TencentAPI.TencentFundAdaptor;
import com.pureblue.quant.TencentAPI.TencentFundAdaptorDao;
import com.pureblue.quant.dao.StockDatebaseFactory;

public class TencentFundZhongcang implements Runnable {
	private String dbName;
	private Connection connection;
	private Logger logger;
	
	public TencentFundZhongcang(String dbName) {
		super();
		this.dbName = dbName;
		this.logger = Logger.getLogger(getClass());
	}
	
	@Override
	public void run() {
	    logger.debug(Thread.currentThread().getName() + " entry.");
		List<IFund> points;
		TencentFundAdaptor adapter = new TencentFundAdaptor();
		try{
			connection = StockDatebaseFactory.getInstance(dbName);
			TencentFundAdaptorDao dao = new TencentFundAdaptorDao(connection);
			dao.initDb();
			points = adapter.fetchAllFunds();
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
		logger.debug(Thread.currentThread().getName() + " end!!"); 
	}

}
