package com.pureblue.quant.TencentAPI;

import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.pureblue.quant.dao.CapitalPoint;
import com.pureblue.quant.dao.IMarketDataProvider;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.model.BarSize;
import com.pureblue.quant.model.DataType;
import com.pureblue.quant.model.IContract;
import com.pureblue.quant.model.ITaskMonitor;
import com.pureblue.quant.model.RequestFailedException;
import com.pureblue.quant.util.HttpUtil;

public class TencentRealTimeAdapterComponent implements IMarketDataProvider {
    private static final String TENCENT_STOCK_DATE_FORMAT = "yyyyMMdd";
    private static final String TENCENT_STOCK_PRICES_QUERY_URL = "http://qt.gtimg.cn/q=%s";
    private static final String TENCENT_STOCK_CAPITAL_QUERY_URL = "http://qt.gtimg.cn/q=ff_%s";
    private Logger logger;
    private String quotedSymbol;

    public TencentRealTimeAdapterComponent(String quotedSymbol) {
        this.logger = Logger.getLogger(TencentRealTimeAdapterComponent.class);
        this.quotedSymbol = quotedSymbol;
        logger.info("TencentRealTimeAdapterComponent::constructor: Tencent Realtime " + quotedSymbol +" adaptor created.");
    }

    @Override
    public DataType[] availableDataTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BarSize[] availableBarSizes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IContract> searchContracts(IContract criteria, ITaskMonitor taskMonitor)
            throws ConnectException, RequestFailedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IOHLCPoint> historicalBars(IContract contract, Date startDateTime,
            Date endDateTime, BarSize barSize, DataType dataType, boolean includeAfterHours,
            ITaskMonitor taskMonitor) throws ConnectException, RequestFailedException {
        // TODO Auto-generated method stub
        return null;
    }

    private CapitalPoint parseCapitalLine(String capitalInfo, String basicInfo,
            DateFormat dateFormat) {
        logger.info("TencentRealTimeAdapterComponent::parseCapitalLine: Tencent Realtime " + quotedSymbol + " parse Capital Line entry.");
        logger.info("TencentRealTimeAdapterComponent::parseCapitalLine: Tencent Realtime " + quotedSymbol + " capitalinfo: " + capitalInfo);
        logger.info("TencentRealTimeAdapterComponent::parseCapitalLine: Tencent Realtime " + quotedSymbol + " basicInfo: " + basicInfo);
        Date date;
        CapitalPoint point = null;
        String[] tokens = capitalInfo.split("~");
        if (tokens.length < 13) {
            logger.error("TencentRealTimeAdapterComponent::parseCapitalLine: Tencent Realtime " + quotedSymbol + " Parse captialinfo to "
                    + capitalInfo);
            return point;
        }
        try {
            date = dateFormat.parse(tokens[13]);
            Double mainInflow = Double.parseDouble(tokens[1]);
            Double mainOutflow = Double.parseDouble(tokens[2]);
            Double mainNetflow = Double.parseDouble(tokens[3]);
            Double mainRate = Double.parseDouble(tokens[4]);
            Double retailInflow = Double.parseDouble(tokens[5]);
            Double retailOutflow = Double.parseDouble(tokens[6]);
            Double retailNetflow = Double.parseDouble(tokens[7]);
            Double retailRate = Double.parseDouble(tokens[8]);
            Double volumn = Double.parseDouble(tokens[9]);
            tokens = basicInfo.split("~");
            Double price = Double.parseDouble(tokens[3]);
            Double turnoverRate = Double.parseDouble(tokens[38]);
            Double PER = Double.parseDouble(tokens[39]);
            Double marketValue = Double.parseDouble(tokens[44]);
            Double totalValue = Double.parseDouble(tokens[45]);
            Double PBR = Double.parseDouble(tokens[46]);
            point = new CapitalPoint(date, mainInflow, mainOutflow, mainNetflow, mainRate,
                    retailInflow, retailOutflow, retailNetflow, retailRate, volumn, price,
                    turnoverRate, PER, marketValue, totalValue, PBR);
        } catch (ParseException e) {
            logger.warn("TencentRealTimeAdapterComponent::parseCapitalLine: Tencent Realtime " + quotedSymbol + " ParseException to "
                    + basicInfo + " " + e.toString());
        } catch (NumberFormatException e) {
            logger.warn("TencentRealTimeAdapterComponent::parseCapitalLine: Tencent Realtime " + quotedSymbol + " Thread exception with "
                    + e.toString());
        }
        logger.info("TencentRealTimeAdapterComponent::parseCapitalLine: Tencent Realtime " + quotedSymbol + " parse Capital Line exit.");
        return point;
    }

    public CapitalPoint latestQuotation() {
        logger.info("TencentRealTimeAdapterComponent::latestQuotation: Tencent Realtime " + quotedSymbol + " adaptor fetch the last update time entry.");
        String queryCapitalUrl = String.format(TENCENT_STOCK_CAPITAL_QUERY_URL, quotedSymbol);
        String queryPriceUrl = String.format(TENCENT_STOCK_PRICES_QUERY_URL, quotedSymbol);
        DateFormat dateFormat = new SimpleDateFormat(TENCENT_STOCK_DATE_FORMAT);
        String rspCapitalString = HttpUtil.httpQuery(queryCapitalUrl);
        String rspPriceString = HttpUtil.httpQuery(queryPriceUrl);
        if(rspCapitalString == null || rspPriceString == null)
            return null;
        return parseCapitalLine(rspCapitalString, rspPriceString, dateFormat);
    }
}
