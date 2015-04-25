package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Tencent.TencentHistoryAdaptor;
import model.BarSize;
import model.SymbolFormat;
import model.TimePeriod;
import dao.IOHLCPoint;
import dao.OHLCPointDao;
import dao.StockDatebaseFactory;

public class TencentHistoricalThread implements Runnable{
	private String dbName;
	private String stockId;
	private Connection connection;
	private boolean flagFetchAll = false;
	
	public TencentHistoricalThread(String dbName, String stockId) {
		super();
		this.dbName = dbName;
		this.stockId = stockId;
	}
	
	public void setFetchMode(boolean mode){
		flagFetchAll = mode;
	}
	
	public void fetchAll(){
		System.out.println(Thread.currentThread().getName() + " " +stockId + " start!!");
		List<IOHLCPoint> info = null;
		TencentHistoryAdaptor adapter = new TencentHistoryAdaptor();
		try{
			connection = StockDatebaseFactory.getInstance(dbName);
			OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
			ohlcPointDao.initDb(stockId);
			ohlcPointDao.deleteAll(stockId);
			String symbol = SymbolFormat.tencentSymbolFormat(stockId);
			info = adapter.historicalBars(symbol);
			System.out.println(info.toString());
			Iterator<IOHLCPoint> itr = info.iterator();
			while(itr.hasNext()){
				ohlcPointDao.save(stockId, itr.next());
			}
			ohlcPointDao.flush();
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
	}
	
	public void fetchSlice() {
		System.out.println(Thread.currentThread().getName() + " " +stockId + " start!!");
		List<IOHLCPoint> info = null;
		Date startDateTime;
		Date endDateTime = new Date();
		long days = endDateTime.getTime()/BarSize.ONE_DAY.getDurationInMs();
		endDateTime.setTime(days * BarSize.ONE_DAY.getDurationInMs() - BarSize.EIGHT_HOURS.getDurationInMs());

		TencentHistoryAdaptor adapter = new TencentHistoryAdaptor();
		try{
			connection = StockDatebaseFactory.getInstance(dbName);
			OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
			ohlcPointDao.initDb(stockId);
			startDateTime = ohlcPointDao.lastDate(stockId);
			long startDays = startDateTime.getTime()/BarSize.ONE_DAY.getDurationInMs();
			boolean flagFetch = true;
			System.out.println(startDateTime.toString());
			System.out.println(endDateTime.toString());
			if (TimePeriod.getWeekOfDate(startDateTime) == 6 && days-startDays <= 2)
				flagFetch = false;
			if (flagFetch && startDateTime.compareTo(endDateTime)<=0) {
				String symbol = SymbolFormat.tencentSymbolFormat(stockId);
				info = adapter.updateLatestBar(symbol, startDateTime, endDateTime, BarSize.ONE_DAY);
				//System.out.println(info.toString());
				Iterator<IOHLCPoint> itr = info.iterator();
				while(itr.hasNext()){
					ohlcPointDao.save(stockId, itr.next());
				}
				ohlcPointDao.flush();
			}
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
	}

	@Override
	public void run() {
		if(flagFetchAll)
			fetchAll();
		else
			fetchSlice();		
	}
}
