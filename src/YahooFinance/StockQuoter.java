/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package YahooFinance;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

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
        yQuote qte = new yQuote();
        String URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22||TICKER||%22)%0A%09%09&format=json&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env";
        String requestUrl = URL;
        requestUrl = requestUrl.replaceAll("||TICKER||", ticker);

        // MAKE WEB REQUEST TO THE URL
        String content = MakeWebRequestToTheURL(requestUrl);
        
        /* TODO: converter pra JAVA        

        // DESERIALIZE THE JSON RESPONSE TO AN OBJECT
        //utilizar o jsoup para substituir o htmlagilitypack
        JObject yahooresult = JObject.Parse(content);
        JToken result = yahooresult["query"]["results"]["quote"];
        qte = JsonConvert.DeserializeObject<yQuote>(result.ToString());
        */
                
        return qte;
    }

    public yQuote StockQuote(String ticker, String url)
    {
        yQuote qte = new yQuote();
        String requestUrl = url;
        requestUrl = requestUrl.replaceAll("||TICKER||", ticker);

        /* TODO: converter pra JAVA
        // MAKE WEB REQUEST TO THE URL
        String content = MakeWebRequestToTheURL(requestUrl);

        // DESERIALIZE THE JSON RESPONSE TO AN OBJECT
        JObject yahooresult = JObject.Parse(content);
        JToken result = yahooresult["query"]["results"]["quote"];
        qte = JsonConvert.DeserializeObject<yQuote>(result.ToString());
        */
        
        return qte;
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

    private static String MakeWebRequestToTheURL(String requestUrl)
    {
        Document doc = null;
        try {
            doc = Jsoup.connect(requestUrl).get();
        } catch (IOException ex) {
            System.out.println("erro ao realizar request para a URL");
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
        for (int i = 0; i <= 52; i++)
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
        String content = MakeWebRequestToTheURL(requestUrl);

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

            /* falta converter 
            //2º tr 
            var tr = tableYfncSumTab.ChildNodes[1];
            //1º td
            var td = tr.ChildNodes[0];
            //2º table - class="yfnc_tableout1"
            var tableYfncTableOut1 = td.ChildNodes[3];
            //tr
            var trTableAcoes = tableYfncTableOut1.ChildNodes[0];
            //td
            var tdTableAcoes = trTableAcoes.ChildNodes[0];
            //Table
            var tableAcoes = tdTableAcoes.ChildNodes[0];
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
}
