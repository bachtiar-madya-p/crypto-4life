package id.bmp.miner.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import id.bmp.miner.model.IndodaxCoinCandle;
import id.bmp.miner.model.IndodaxMarket;
import id.bmp.miner.util.http.HTTPClient;
import id.bmp.miner.util.http.model.HTTPContentType;
import id.bmp.miner.util.http.model.HTTPRequest;
import id.bmp.miner.util.http.model.HTTPResponse;
import id.bmp.miner.util.json.JsonUtil;
import id.bmp.miner.util.property.Property;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class IndodaxController extends BaseController{

    public IndodaxController() {
        log =  getLogger(this.getClass());
    }


    public IndodaxMarket getMarketSummary() {
        String methodName = "getMarketSummary";
        start(methodName);
        IndodaxMarket markets = new IndodaxMarket();

        String url = getProperty(Property.IDX_BASE_URL) +  getProperty(Property.IDX_SUMMARY_URL);

        HTTPRequest request = new HTTPRequest.Builder(url).setContentType(HTTPContentType.APPLICATION_JSON).build();

        HTTPResponse httpResponse = HTTPClient.get(request);
        if(httpResponse.getCode() == 200) {
            markets = JsonUtil.fromJson(httpResponse.getBody(), IndodaxMarket.class);
        }
        completed(methodName);
        return markets;
    }

    public List<IndodaxCoinCandle> getCandle(String market, int tfMinutes, long from, long now) {
        String methodName = "getCandle : " + market;
        //start(methodName);
        List<IndodaxCoinCandle> candles = new ArrayList<>();

        String symbol = market.toLowerCase(Locale.ROOT).replace("_", "");

        String url = String.format(getProperty(Property.IDX_CANDLE_URL),symbol, tfMinutes, from, now);;

        HTTPRequest request = new HTTPRequest.Builder(url).setContentType(HTTPContentType.APPLICATION_JSON).build();
        HTTPResponse httpResponse = HTTPClient.get(request);

        if(httpResponse.getCode() == 200){
            candles = JsonUtil.fromJson(httpResponse.getBody(), new TypeReference<List<IndodaxCoinCandle>>() {});
        }
        //completed(methodName);
        return candles;
    }
}
