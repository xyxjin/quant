package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import ta.AverageCrossingTradingAgent;
import model.BarSize;
import model.BareDate;
import model.DataType;
import model.IContract;
import model.ISeries;
import model.ISeriesAugmentable;
import model.ISeriesPoint;
import model.LinkedListSeries;
import model.TimePeriod;
import dao.IOHLCPoint;
import dao.IOHLCTimeSeries;
import dao.IStockDatabase;
import dao.OHLCVirtualTimeSeries;
import dao.StockDatabase;
import dao.OHLCPointDao;
import dao.StockDatebaseFactory;

import algo_service.LinearCommissionCalculator;
import algo_service.SimulatedExecutionService;
import calendar.FlatCalendar;
import calendar.ITradingDay;
import calendar.PeriodicTradingCalendar;

public class algoExecutor {

	public algoExecutor() {
		// TODO Auto-generated constructor stub
	}
	
	private Map<String, IOHLCTimeSeries> createInputSeriesFromStockDatabases(Map<String, IStockDatabase> inputStockDatabase) {
		Map<String, IOHLCTimeSeries> inputSeries = new HashMap<String, IOHLCTimeSeries>();
		for (Map.Entry<String, IStockDatabase> entry : inputStockDatabase.entrySet()) {
			inputSeries.put(entry.getKey(), entry.getValue().getVirtualTimeSeries());
		}
		return inputSeries;
	}
	
	public void executor(){
		Connection conn=null;
		Currency currency = Currency.getInstance(Locale.CHINA);
		TimeZone timezone = TimeZone.getDefault();
		Set<BareDate> bankHolidays = null;
		Map<Integer, ITradingDay> weekTradingDays = null;
		Map<String, ITradingDay> specialTradingDays = null;
		Date startDate = TimePeriod.formatStringToDate("2014-1-1");
		Date endDate = TimePeriod.formatStringToDate("2014-12-1");
		String[] exchange = {"CBOT"};
		PeriodicTradingCalendar calendar = new PeriodicTradingCalendar("xjin", "first calendar example", TimeZone.getDefault(), exchange, true, bankHolidays, weekTradingDays, specialTradingDays, startDate, endDate);
		System.out.println(calendar.tradingDay(endDate));
		
		FlatCalendar calendar1 = new FlatCalendar();
		AverageCrossingTradingAgent acta = new AverageCrossingTradingAgent(calendar1, 30, 18, 200, true);
		
		IContract contract = new portfolio("600030.SS", currency);
		StockDatabase db = new StockDatabase(contract, DataType.ASK, BarSize.ONE_DAY, true, timezone);
		Map<String, IStockDatabase> inputStockDatabase = new HashMap<String, IStockDatabase>();
		inputStockDatabase.put("600030.SS", db);
				
		OHLCVirtualTimeSeries virt = (OHLCVirtualTimeSeries)db.getVirtualTimeSeries();
		try{
			conn = StockDatebaseFactory.getInstance("test");
			OHLCPointDao ohlcPointDao = new OHLCPointDao(conn);
			List<IOHLCPoint> info = ohlcPointDao.findTicker("600030.SS", TimePeriod.formatStringToDate("2014-12-1"), TimePeriod.formatStringToDate("2014-1-1"));
			Map<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> inputSeries = createInputSeriesFromStockDatabases(inputStockDatabase);
			
			LinearCommissionCalculator calc = new LinearCommissionCalculator(0,0,1);
			SimulatedExecutionService orderReceiver = new SimulatedExecutionService(calc);
			Collection<ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> input = new HashSet<ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>>();
			System.out.println(inputSeries.get("600030.SS"));
			input.add(inputSeries.get("600030.SS"));
			orderReceiver.setInputSeries(input);
			acta.setOrderReceiver(orderReceiver);
			
			String processorOutputSeriesID = "123456";
			ISeriesAugmentable<Date, Double, ISeriesPoint<Date, Double>> outputSeries = new LinkedListSeries<Date, Double, ISeriesPoint<Date, Double>>(processorOutputSeriesID, false);
			//ISeriesAugmentable<Date, Double, ISeriesPoint<Date, Double>> outputSeries = null;
			Iterator<IOHLCPoint> ohlciter = info.iterator();
			virt.addOrUpdateBar(ohlciter.next());
			acta.wire(inputSeries, outputSeries);
			
			Thread first = new Thread(acta);
			first.start();
			int count =0;
			while(ohlciter.hasNext()){
				IOHLCPoint point = ohlciter.next();
				virt.addOrUpdateBar(point);
				acta.onItemAdded(point);
				//if(count>200)
				//	break;
				//count++;
				//System.out.println(db.getVirtualTimeSeries().size());
			}
			acta.kill();
			//System.out.println(outputSeries.size());
			//System.out.println(orderReceiver.getTrades().toString());
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
            e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("done!!!");
		//System.out.println(outputSeries);
	}
	//info = ohlcPointDao.findTicker(quotes[i], TimePeriod.formatStringToDate("2014-12-1"), TimePeriod.formatStringToDate("2014-1-1"));
	//System.out.println(info.get(0).getClose());
	//System.out.println(info);

}
