/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package YahooFinance;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.json.JSONObject;

/**
 *
 * @author Jean Pierre Patzlaff
 */
public class StockQuoter {
    /// <summary>
    /// This original method grabs the YQL url from the config file, but it was causing problems int testing
    /// so i created the second method where the url is manually passed in.
    /// </summary>
    /// <param name="ticker"></param>
    /// <returns></returns>
    public yQuote StockQuote(String ticker)
    {
        String URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22||TICKER||%22)%0A%09%09&format=json&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env";
                
        return StockQuote(ticker, URL);
    }

    
    public yQuote StockQuote(String ticker, String url)
    {
        String requestUrl = url;
        requestUrl = requestUrl.replace("||TICKER||", ticker);
        
        // MAKE WEB REQUEST TO THE URL
        String content = MakeWebRequestToTheURL(requestUrl, true);
                
        // DESERIALIZE THE JSON RESPONSE TO AN OBJECT
        /*
        JObject yahooresult = JObject.Parse(content);
        JToken result = yahooresult["query"]["results"]["quote"];
        qte = JsonConvert.DeserializeObject<yQuote>(result.ToString());
        */
        JSONObject jsonObject = new JSONObject(content);
        JSONObject query = jsonObject.getJSONObject("query");
        JSONObject results = query.getJSONObject("results");
        JSONObject quote = results.getJSONObject("quote");
        
        return ConvertJSONToYQuote(quote);
    }

    public List<String> GetStockQuoteList()
    {
        
        //TODO: html

        //select * from html where url='http://finance.yahoo.com/q/cp?s=%5EBVSP+Components'

        List<String> list = new ArrayList<String>();
        /* aparentemente não utilizado
        //yQuote qte = new yQuote();

        //select * from yahoo.finance.industry where id in (select industry.id from yahoo.finance.sectors)
        String requesturl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.industry%20where%20id%20in%20(select%20industry.id%20from%20yahoo.finance.sectors)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

        // MAKE WEB REQUEST TO THE URL
        String content = MakeWebRequestToTheURL(requesturl);

        // DESERIALIZE THE JSON RESPONSE TO AN OBJECT
        JObject yahooresult = JObject.Parse(content);
        JArray industryArray = yahooresult["query"]["results"]["industry"] as JArray;
        foreach (var industry in industryArray)
        {
            JArray companies = industry["company"] as JArray;
            if (companies != null)
            {
                foreach (var company in companies)
                {
                    JValue symbol = (company["symbol"] as JValue);
                    list.Add(symbol.ToString());
                }
            }
        }

        //qte = JsonConvert.DeserializeObject<yQuote>(result.ToString());
        */
        return list;        
    }

    private static String MakeWebRequestToTheURL(String requestUrl, Boolean isResponseJSON)
    {
        Document doc = null;
        String json = "";
        try {
            if(isResponseJSON)
                json = Jsoup.connect(requestUrl).ignoreContentType(true).execute().body();
            else
                doc = Jsoup.connect(requestUrl).get();
            
        } catch (IOException ex) {
            System.out.println("erro ao realizar request para a URL"+ex.getMessage());
        }
        /* 
            // MAKE WEB REQUEST TO THE URL
            WebRequest request = WebRequest.Create(requesturl);
            WebResponse response = request.GetResponse();

            Stream receive = response.GetResponseStream();
            Encoding encoding = Encoding.GetEncoding("utf-8");
            StreamReader read = new StreamReader(receive, encoding);

            string content = read.ReadToEnd();
            return content;
        */
        if(isResponseJSON)
            return json;
        else
            return doc.outerHtml();            
    }

    public List<String> GetStockQuoteListBovespa() 
            throws YahooExceptionNoComponentsDataAvailable
    {
        List<String> URIList = new ArrayList<String>();
        for (int i = 0; i <= 1; i++)
        {
            URIList.add("http://finance.yahoo.com/q/cp?s=^BVSP&c=" + i);
        };

        List<String> list = null;
        try
        {
            list = GetStockQuoteListYahooWebSite(URIList);
        }
        catch(YahooExceptionNoComponentsDataAvailable ex)
        {
            throw ex;
        }
        return list;
    }

    public List<String> GetStockQuoteListNasdaq() 
            throws YahooExceptionNoComponentsDataAvailable
    {
        List<String> URIList = new ArrayList<String>();
        for (int i = 0; i <= 51; i++)
        {
            URIList.add("http://finance.yahoo.com/q/cp?s=^IXIC&c=" + i);
        };

        List<String> list = null;
        try
        {
            list = GetStockQuoteListYahooWebSite(URIList);
        }
        catch(YahooExceptionNoComponentsDataAvailable ex)
        {
            throw ex;
        }
        return list;
    }

    public List<String> GetStockQuoteListDownJones() 
            throws YahooExceptionNoComponentsDataAvailable
    {
            List<String> URIList = new ArrayList<String>();
            for (int i = 0; i <= 1; i++)
            {
                URIList.add("http://finance.yahoo.com/q/cp?s=^DJA&c=" + i);
            };

            List<String> list = null;
            try
            {
                list = GetStockQuoteListYahooWebSite(URIList);
            }
            catch(YahooExceptionNoComponentsDataAvailable ex)
            {
                throw ex;
            }
            return list;
    }
    
    private static List<String> GetStockQuoteListYahooWebSite(List<String> URIList) 
            throws YahooExceptionNoComponentsDataAvailable
    {
        String requestUrl = URIList.get(0);

        // MAKE WEB REQUEST TO THE URL
        String content = MakeWebRequestToTheURL(requestUrl, false);

        ArrayList<String> list = new ArrayList<String>();
            
            
            //            HtmlAgilityPack.HtmlDocument htmlDoc = new HtmlAgilityPack.HtmlDocument();
            //            //html
            Document doc = Jsoup.parse(content);
            //            htmlDoc.LoadHtml(content);
            //            var html = htmlDoc.DocumentNode.ChildNodes[2];
            //body
            //var body = html.ChildNodes[1];
            Element body = doc.body();
            
            //4º div - id="screen"
            //var divScreen = body.ChildNodes[3];
            Node divScreen = null;
            for (Node div : body.childNodes()) {
                if(div.attr("id").equals("screen"))
                        divScreen = div;
            }
                      
            
            //4º div - id="rightcol"
            //var divRightCol = divScreen.ChildNodes[5];
            Node divRightCol = null;
            for (Node div : divScreen.childNodes()) {
                if(div.attr("id").equals("rightcol"))
                        divRightCol = div;
            }
            
            
            //2º table - id="yfncsumtab"
            //var tableYfncSumTab = divRightCol.ChildNodes[3];
            Node tableYfncSumTab = null;
            for (Node table : divRightCol.childNodes()) {
                if(table.attr("id").equals("yfncsumtab"))
                        tableYfncSumTab = table;
            }
            
 
            if (tableYfncSumTab.outerHtml().contains("There is no Components data available for"))
                throw new YahooExceptionNoComponentsDataAvailable();

            
            //2º tr 
            //var tr = tableYfncSumTab.ChildNodes[1];
            Node tbody = tableYfncSumTab.childNode(1);
            Node segundoTR = tbody.childNode(3);
                        
            //1º td
            //var td = tr.ChildNodes[0];
            Node primeiroTD = segundoTR.childNode(1);
                        
            //2º table - class="yfnc_tableout1"
            //var tableYfncTableOut1 = td.ChildNodes[3];
            Node segundaTableYfncTableOut1 = primeiroTD.childNode(7);
                        
            //tr
            //var trTableAcoes = tableYfncTableOut1.ChildNodes[0];
            Node tBody = segundaTableYfncTableOut1.childNode(1);
            Node trTableAcoes = tBody.childNode(1);            
                        
            //td
            //var tdTableAcoes = trTableAcoes.ChildNodes[0];
            Node tdTableAcoes = trTableAcoes.childNode(1);
                        
            //Table
            //var tableAcoes = tdTableAcoes.ChildNodes[0];
            Node tableAcoes = tdTableAcoes.childNode(1);
            Node tBodyTableAcoes = tableAcoes.childNode(1);
                 
            //iterar somente pelos nodes do tipo Element
            int ignorar = 1;
            for(Node filho : tBodyTableAcoes.childNodes())
            {                
                if(!org.jsoup.nodes.Element.class.isInstance(filho))
                    continue;
                                
                //primeira Element é header, por isso ignora
                if (ignorar == 1)
                {
                    ignorar--;
                    continue;
                }
                
                Node tdYfncTableData1 = filho.childNode(1);
                Node b = tdYfncTableData1.childNode(0);
                Node a = b.childNode(0);
                //Exceção ocorrendo
                Node text = null;
                try
                {
                     text = a.childNode(0);
                }
                catch(Exception ex) {
                    //System.out.println(ex.toString());
                    continue;
                }
                    
                System.out.println(list.size());
                
                list.add(text.outerHtml());
            }
            /* falta converter 
            //linhas
            int ignorar = 1;
            foreach (var linhaAcoes in tableAcoes.ChildNodes)
            {
                //primeira é header, por isso ignora
                if (ignorar == 1)
                {
                    ignorar--;
                    continue;
                }
                //resto é ação
                //td
                var conlunaAcao = linhaAcoes.ChildNodes[0];
                //b
                var b = conlunaAcao.ChildNodes[0];
                //a
                var a = b.ChildNodes[0];

                list.Add(a.InnerText);
            }
            */
        return list;
    }    

    private yQuote ConvertJSONToYQuote(JSONObject quote) {
        yQuote cotacao = new yQuote();
        
    /*
        ,,,,,"BidRealtime":null,"BookValue":"16.87","Change_PercentChange":"+0.26 - +1.02%","Change":"+0.26","Commission":null,"Currency":"BRL","ChangeRealtime":null,"AfterHoursChangeRealtime":null,"DividendShare":null,"LastTradeDate":"6/17/2016","TradeDate":null,"EarningsShare":"3.33","ErrorIndicationreturnedforsymbolchangedinvalid":null,"EPSEstimateCurrentYear":null,"EPSEstimateNextYear":null,"EPSEstimateNextQuarter":"0.00","DaysLow":"25.63","DaysHigh":"26.05","YearLow":"16.27","YearHigh":"30.15","HoldingsGainPercent":null,"AnnualizedGain":null,"HoldingsGain":null,"HoldingsGainPercentRealtime":null,"HoldingsGainRealtime":null,"MoreInfo":null,"OrderBookRealtime":null,"MarketCapitalization":"142.17B","MarketCapRealtime":null,"EBITDA":"0.00","ChangeFromYearLow":"9.43","PercentChangeFromYearLow":"+57.93%","LastTradeRealtimeWithTime":null,"ChangePercentRealtime":null,"ChangeFromYearHigh":"-4.45","PercebtChangeFromYearHigh":"-14.75%","LastTradeWithTime":"12:22pm - <b>25.70</b>","LastTradePriceOnly":"25.70","HighLimit":null,"LowLimit":null,"DaysRange":"25.63 - 26.05","DaysRangeRealtime":null,"FiftydayMovingAverage":"26.51","TwoHundreddayMovingAverage":"23.33","ChangeFromTwoHundreddayMovingAverage":"2.37","PercentChangeFromTwoHundreddayMovingAverage":"+10.14%","ChangeFromFiftydayMovingAverage":"-0.81","PercentChangeFromFiftydayMovingAverage":"-3.06%","Name":"BRADESCO    ON  EJ  N1","Notes":null,"Open":"25.77","PreviousClose":"25.44","PricePaid":null,"ChangeinPercent":"+1.02%","PriceSales":"2.28","PriceBook":"1.51","ExDividendDate":"6/2/2016","PERatio":"7.72","DividendPayDate":null,"PERatioRealtime":null,"PEGRatio":"0.00","PriceEPSEstimateCurrentYear":null,"PriceEPSEstimateNextYear":null,"Symbol":"BBDC3.SA","SharesOwned":null,"ShortRatio":"0.00","LastTradeTime":"12:22pm","TickerTrend":null,"OneyrTargetPrice":null,"Volume":"218200","HoldingsValue":null,"HoldingsValueRealtime":null,"YearRange":"16.27 - 30.15","DaysValueChange":null,"DaysValueChangeRealtime":null,"StockExchange":"SAO","DividendYield":null,"PercentChange":"+1.02%"}}}}
        */    
    //String 
    cotacao.symbol = quote.getString("symbol"); //"symbol":"BBDC3.SA"
    //double 
    cotacao.Ask = quote.getDouble("Ask"); //"Ask":"25.72"
    //int 
    cotacao.AverageDailyVolume = quote.getInt("AverageDailyVolume"); //"AverageDailyVolume":"1532470"
    //float 
    cotacao.Bid; //"Bid":"25.70"
    //float 
    cotacao.AskRealtime; //"AskRealtime":null
    //float 
    cotacao.BidRealtime;
    //float 
    cotacao.BookValue;
    //String 
    cotacao.Change_PercentChange;
    //float 
    cotacao.Change;
    //String 
    cotacao.Commission;
    //float 
    cotacao.ChangeRealtime;
    //String 
    cotacao.AfterHoursChangeRealtime;
    //String 
    cotacao.DividendShare;
    //Date 
    cotacao.LastTradeDate;
    //Date 
    cotacao.TradeDate;
    //float 
    cotacao.EarningsShare;
    //String 
    cotacao.ErrorIndicationreturnedforsymbolchangedinvalid;
    //float 
    cotacao.EPSEstimateCurrentYear;
    //float 
    cotacao.EPSEstimateNextYear;
    //float 
    cotacao.EPSEstimateNextQuarter;
    //float 
    cotacao.DaysLow;
    //float 
    cotacao.DaysHigh;
    //float 
    cotacao.YearLow;
    //float 
    cotacao.YearHigh;
    //String 
    cotacao.HoldingsGainPercent;
    //float 
    cotacao.AnnualizedGain;
    //float 
    cotacao.HoldingsGain;
    //String 
    cotacao.HoldingsGainPercentRealtime;
    //float 
    cotacao.HoldingsGainRealtime;
    //String 
    cotacao.MoreInfo;
    //String 
    cotacao.OrderBookRealtime;
    //String 
    cotacao.MarketCapitalization;
    //String 
    cotacao.MarketCapRealtime;
    //String 
    cotacao.EBITDA;
    //float 
    cotacao.ChangeFromYearLow;
    //String 
    cotacao.PercentChangeFromYearLow;
    //String 
    cotacao.LastTradeRealtimeWithTime;
    //String 
    cotacao.ChangePercentRealtime;
    //float 
    cotacao.ChangeFromYearHigh;
    //String 
    cotacao.PercebtChangeFromYearHigh;
    //String 
    cotacao.LastTradeWithTime;
    //float 
    cotacao.LastTradePriceOnly;
    //String 
    cotacao.HighLimit;
    //String 
    cotacao.LowLimit;
    //String 
    cotacao.DaysRange;
    //String 
    cotacao.DaysRangeRealtime;
    //float 
    cotacao.FiftydayMovingAverage;
    //float 
    cotacao.TwoHundreddayMovingAverage;
    //float 
    cotacao.ChangeFromTwoHundreddayMovingAverage;
    //String 
    cotacao.PercentChangeFromTwoHundreddayMovingAverage;
    //float 
    cotacao.ChangeFromFiftydayMovingAverage;
    //String 
    cotacao.PercentChangeFromFiftydayMovingAverage;
    //String 
    cotacao.Name;
    //String 
    cotacao.Notes;
    //float 
    cotacao.Open;
    //float 
    cotacao.PreviousClose;
    //float 
    cotacao.PricePaid;
    //String 
    cotacao.ChangeinPercent;
    //float 
    cotacao.PriceSales;
    //float 
    cotacao.PriceBook;
    //Date 
    cotacao.ExDividendDate;
    //float 
    cotacao.PERatio;
    //Date 
    cotacao.DividendPayDate;
    //float 
    cotacao.PERatioRealtime;
    //float 
    cotacao.PEGRatio;
    //float 
    cotacao.PriceEPSEstimateCurrentYear;
    //float 
    cotacao.PriceEPSEstimateNextYear;
    //String 
    cotacao.Symbol;
    //String 
    cotacao.SharesOwned;
    //float 
    cotacao.ShortRatio;
    //Date 
    cotacao.LastTradeTime;
    //String 
    cotacao.TickerTrend;
    //float 
    cotacao.OneyrTargetPrice;
    //int 
    cotacao.Volume;
    //float 
    cotacao.HoldingsValue;
    //float 
    cotacao.HoldingsValueRealtime;
    //String 
    cotacao.YearRange;
    //String 
    cotacao.DaysValueChange;
    //String 
    cotacao.DaysValueChangeRealtime;
    //String 
    cotacao.StockExchange;
    //float 
    cotacao.DividendYield;
    //String 
    cotacao.PercentChange;
       
    return cotacao;
    }
}
