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
                    
                //System.out.println(list.size());
                
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
    //String 
    cotacao.symbol = quote.getString("symbol"); //"symbol":"BBDC3.SA"
    */
    //double 
    cotacao.Ask = quote.getDouble("Ask"); //"Ask":"25.72"
    /*
    //int 
    cotacao.AverageDailyVolume = quote.getInt("AverageDailyVolume"); //"AverageDailyVolume":"1532470"
    //float 
    cotacao.Bid; //"Bid":"25.70"
    //float 
    cotacao.AskRealtime; //"AskRealtime":null
    //float 
    cotacao.BidRealtime; //"BidRealtime":null
    //float 
    cotacao.BookValue; //"BookValue":"16.87"
    //String 
    cotacao.Change_PercentChange; //"Change_PercentChange":"+0.26 - +1.02%"
    //float 
    cotacao.Change; //"Change":"+0.26"
    //String 
    cotacao.Commission; //"Commission":null
    
    //cotacao.Currency; //"Currency":"BRL"

    //float 
    cotacao.ChangeRealtime; //"ChangeRealtime":null
    //String 
    cotacao.AfterHoursChangeRealtime; //"AfterHoursChangeRealtime":null
    //String 
    cotacao.DividendShare; //"DividendShare":null
    //Date 
    cotacao.LastTradeDate; //"LastTradeDate":"6/17/2016"
    //Date 
    cotacao.TradeDate; //"TradeDate":null
    //float 
    cotacao.EarningsShare; //"EarningsShare":"3.33"
    //String 
    cotacao.ErrorIndicationreturnedforsymbolchangedinvalid; //"ErrorIndicationreturnedforsymbolchangedinvalid":null
    //float 
    cotacao.EPSEstimateCurrentYear; //"EPSEstimateCurrentYear":null
    //float 
    cotacao.EPSEstimateNextYear; //"EPSEstimateNextYear":null
    //float 
    cotacao.EPSEstimateNextQuarter; //"EPSEstimateNextQuarter":"0.00"
    //float 
    cotacao.DaysLow; //"DaysLow":"25.63"
    //float 
    cotacao.DaysHigh; //"DaysHigh":"26.05"   
    */
    //double 
    cotacao.YearLow = quote.getDouble("YearLow"); //"YearLow":"16.27"  
    //double 
    cotacao.YearHigh = quote.getDouble("YearHigh"); //"YearHigh":"30.15"        
    /*
    //String 
    cotacao.HoldingsGainPercent; //"HoldingsGainPercent":null        
    //float 
    cotacao.AnnualizedGain; //"AnnualizedGain":null        
    //float 
    cotacao.HoldingsGain; //"HoldingsGain":null 
    //String 
    cotacao.HoldingsGainPercentRealtime; //"HoldingsGainPercentRealtime":null 
    //float 
    cotacao.HoldingsGainRealtime; //"HoldingsGainRealtime":null        
    //String 
    cotacao.MoreInfo; //"MoreInfo":null              
    //String 
    cotacao.OrderBookRealtime; //"OrderBookRealtime":null            
    //String 
    cotacao.MarketCapitalization; //"MarketCapitalization":"142.17B"
    //String 
    cotacao.MarketCapRealtime; //"MarketCapRealtime":null         
    //String 
    cotacao.EBITDA; //"EBITDA":"0.00"               
    //float 
    cotacao.ChangeFromYearLow; //"ChangeFromYearLow":"9.43"   
    //String 
    cotacao.PercentChangeFromYearLow; //"PercentChangeFromYearLow":"+57.93%"        
    //String 
    cotacao.LastTradeRealtimeWithTime; //"LastTradeRealtimeWithTime":null        
    //String 
    cotacao.ChangePercentRealtime; //    "ChangePercentRealtime":null        
    //float 
    cotacao.ChangeFromYearHigh; //    "ChangeFromYearHigh":"-4.45"        
    //String 
    cotacao.PercebtChangeFromYearHigh; //    "PercebtChangeFromYearHigh":"-14.75%"        
    //String 
    cotacao.LastTradeWithTime; //    "LastTradeWithTime":"12:22pm - <b>25.70</b>"        
    //float 
    cotacao.LastTradePriceOnly; //    "LastTradePriceOnly":"25.70"        
    //String 
    cotacao.HighLimit; //    "HighLimit":null,"LowLimit":ull
    //String 
    cotacao.LowLimit; //    
    //String 
    cotacao.DaysRange; //"DaysRange":"25.63 - 26.05"
    //String 
    cotacao.DaysRangeRealtime; //    "DaysRangeRealtime":null
    //float 
    cotacao.FiftydayMovingAverage; //    "FiftydayMovingAverage":"26.51"        
    //float 
    cotacao.TwoHundreddayMovingAverage; //    "TwoHundreddayMovingAverage":"23.33"        
    //float 
    cotacao.ChangeFromTwoHundreddayMovingAverage; //    "ChangeFromTwoHundreddayMovingAverage":"2.37"        
    //String 
    cotacao.PercentChangeFromTwoHundreddayMovingAverage; //    "PercentChangeFromTwoHundreddayMovingAverage":"+10.14%"        
    //float 
    cotacao.ChangeFromFiftydayMovingAverage; //    "ChangeFromFiftydayMovingAverage":"-0.81"        
    //String 
    cotacao.PercentChangeFromFiftydayMovingAverage; //    "PercentChangeFromFiftydayMovingAverage":"-3.06%"        
    //String 
    cotacao.Name; //    "Name":"BRADESCO    ON  EJ  N1"        
    //String 
    cotacao.Notes;  //    "Notes":null        
    //float 
    cotacao.Open; //   "Open":"25.77"        
    //float 
    cotacao.PreviousClose; //    "PreviousClose":"25.44"        
    //float 
    cotacao.PricePaid; //    "PricePaid":null        
    //String 
    cotacao.ChangeinPercent; //    "ChangeinPercent":"+1.02%"        
    //float 
    cotacao.PriceSales; //    "PriceSales":"2.28"        
    //float 
    cotacao.PriceBook; //    "PriceBook":"1.51"        
    //Date 
    cotacao.ExDividendDate; //    "ExDividendDate":"6/2/2016"        
    //float 
    cotacao.PERatio; //    "PERatio":"7.72"        
    //Date 
    cotacao.DividendPayDate; //    "DividendPayDate":null        
    //float 
    cotacao.PERatioRealtime; //    "PERatioRealtime":null        
    //float 
    cotacao.PEGRatio; //    "PEGRatio":"0.00"
    //float 
    cotacao.PriceEPSEstimateCurrentYear; //    "PriceEPSEstimateCurrentYear":null
    //float 
    cotacao.PriceEPSEstimateNextYear; //    "PriceEPSEstimateNextYear":null
    */
    //String 
    cotacao.Symbol = quote.getString("Symbol"); //    "Symbol":"BBDC3.SA"
    /*
    //String 
    cotacao.SharesOwned; //    "SharesOwned":null
    //float 
    cotacao.ShortRatio; //    "ShortRatio":"0.00"
    //Date 
    cotacao.LastTradeTime; //    "LastTradeTime":"12:22pm"
    //String 
    cotacao.TickerTrend;  //   "TickerTrend":null
    //float 
    cotacao.OneyrTargetPrice; //    "OneyrTargetPrice":null
    //int 
    cotacao.Volume; //    "Volume":"218200"
    //float 
    cotacao.HoldingsValue; //   "HoldingsValue":null
    //float 
    cotacao.HoldingsValueRealtime; //    "HoldingsValueRealtime":null
    //String 
    cotacao.YearRange; //    "YearRange":"16.27 - 30.15"
    //String 
    cotacao.DaysValueChange; //    "DaysValueChange":null
    //String 
    cotacao.DaysValueChangeRealtime; //    "DaysValueChangeRealtime":null
    //String 
    cotacao.StockExchange; //    "StockExchange":"SAO"
    //float 
    cotacao.DividendYield; //    "DividendYield":null       
    //String 
    cotacao.PercentChange; //        "PercentChange":"+1.02%"
    */        
        
    
        
    return cotacao;
    }
}
