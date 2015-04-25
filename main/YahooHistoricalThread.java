package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import model.BarSize;
import model.DataType;
import model.IContract;
import model.TimePeriod;
import yahoo.YahooFinanceAdapterComponent;
import dao.IOHLCPoint;
import dao.OHLCPointDao;
import dao.StockDatebaseFactory;

public class YahooHistoricalThread implements Runnable{
	private String dbName;
	private String stockId;
	private Connection connection;
	
	public YahooHistoricalThread(String dbName, String stockId) {
		super();
		this.dbName = dbName;
		this.stockId = stockId;
	}
	
	public void run() {
		System.out.println(Thread.currentThread().getName() + " " +stockId + " start!!");
		List<IOHLCPoint> info = null;
		Currency currency = Currency.getInstance(Locale.CHINA);
		Date startDateTime;
		Date endDateTime = new Date();
		long days = endDateTime.getTime()/BarSize.ONE_DAY.getDurationInMs();
		endDateTime.setTime(days * BarSize.ONE_DAY.getDurationInMs() - BarSize.EIGHT_HOURS.getDurationInMs());
		
		IContract contract = new portfolio(stockId, currency);
		YahooFinanceAdapterComponent adapter = new YahooFinanceAdapterComponent();
		
		try{
			connection = StockDatebaseFactory.getInstance(dbName);
			OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
			ohlcPointDao.initDb(stockId);
			startDateTime = ohlcPointDao.lastDate(stockId);
			long startDays = startDateTime.getTime()/BarSize.ONE_DAY.getDurationInMs();
			/*
			System.out.println("do something");
			System.out.println(TimePeriod.getWeekOfDate(startDateTime));
			System.out.println(days);
			System.out.println(startDays);
			System.out.println(startDateTime);
			System.out.println(endDateTime);
			*/
			boolean flagFetch = true;
			if (TimePeriod.getWeekOfDate(startDateTime) == 6 && days-startDays <= 2)
				flagFetch = false;
			if (flagFetch && startDateTime.before(endDateTime)) {
				info = adapter.historicalBars(contract, startDateTime, endDateTime, BarSize.ONE_DAY, DataType.BID_ASK, true, null);
				Iterator<IOHLCPoint> itr = info.iterator();
				while(itr.hasNext()){
					ohlcPointDao.save(stockId, itr.next());
				}
				ohlcPointDao.flush();
				//System.out.println(ohlcPointDao.lastDate(stockId));
			}
			//wait();
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
		System.out.println(Thread.currentThread().getName() + " " +stockId + " end!!"); 
		//return true;
	}
}
