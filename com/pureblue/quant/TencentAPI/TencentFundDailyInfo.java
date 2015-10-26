package com.pureblue.quant.TencentAPI;

import org.json.JSONException;
import org.json.JSONObject;

import com.pureblue.quant.util.HttpUtil;

public class TencentFundDailyInfo implements IFund{
	private static final String fundBaseUrl="http://web.ifzq.gtimg.cn/fund/newfund/fundBase/getPriceZone?symbol=";

	
	public static void main(String[] args) {
		TencentFundDailyInfo info = new TencentFundDailyInfo();
		IFund fundInfo = info.fetchLatestFundValue("jj000001");
		System.out.println(fundInfo.toString());
	}
	
	public IFund fetchLatestFundValue(String symbol){
		IFund fundInfo = null;
		String url = fundBaseUrl + symbol;
		try {
			String body = HttpUtil.httpQuery(url);
			JSONObject jsonObject;
			jsonObject = new JSONObject(body).getJSONObject("data");
			System.out.println(jsonObject.getJSONObject("data"));
			fundInfo = new Fund(jsonObject.getJSONObject("data").getString("gzsj"), jsonObject.getJSONObject("data").getDouble("dwjz"),
					Double.valueOf(jsonObject.getJSONObject("data").getString("zxgz")), Double.valueOf(jsonObject.getJSONObject("info").getString("zxgm")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fundInfo;
	}

    @Override
    public String getSymbol() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getAssets() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getRatio() {
        // TODO Auto-generated method stub
        return 0;
    }

}
