package Tencent;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import util.HttpUtil;

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
		} catch (HttpException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fundInfo;
	}

}
