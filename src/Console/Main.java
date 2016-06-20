/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Console;

import YahooFinance.*;
import java.util.ArrayList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 *
 * @author Jean Pierre Patzlaff
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            
            StockQuoter sq = new StockQuoter();
            //List<string> lista = sq.GetStockQuoteList().ToList();

            Bolsa bovespa = new Bolsa();
            bovespa.setNome("Bovespa");
            try
            {
                bovespa.setLista((ArrayList<String>)sq.GetStockQuoteListBovespa());
            }
            catch (YahooExceptionNoComponentsDataAvailable ex)
            {   
               System.out.println(ex.toString() + " " + bovespa.getNome());
               System.out.println();
            }

            Bolsa nasdaq = new Bolsa();
            nasdaq.setNome("Nasdaq");
            try
            {
                nasdaq.setLista(sq.GetStockQuoteListNasdaq());
            }
            catch (YahooExceptionNoComponentsDataAvailable ex)
            {
                System.out.println(ex.toString() + " " + nasdaq.getNome());
                System.out.println();
            }

            Bolsa downJones = new Bolsa();
            downJones.setNome("DownJones");
            try
            {
                downJones.setLista(sq.GetStockQuoteListDownJones());
            }
            catch (YahooExceptionNoComponentsDataAvailable ex)
            {
                System.out.println(ex.toString() + " " + downJones.getNome());
                System.out.println();
            }

            String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22||TICKER||%22)%0A%09%09&format=json&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env";
            System.out.println(new Date().toString());
            System.out.println("Podem dobrar:");
            System.out.println();
            ImprimeDadosBolsa(sq, bovespa, url);
            ImprimeDadosBolsa(sq, nasdaq, url);
            ImprimeDadosBolsa(sq, downJones, url);
            System.out.println("pressione qualquer tecla para finalizar");
            try {
                System.in.read();
            } catch (IOException ex) {
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("exceção ao tentar ler.");
            }
        }

    private static void ImprimeDadosBolsa(StockQuoter sq, Bolsa bolsa, String url)
    {
        System.out.println("Bolsa: " + bolsa.getNome());
        if (bolsa.getLista() == null)
            return;

        for (String acao : bolsa.getLista())
        {
            try
            {
                yQuote stock = sq.StockQuote(acao, url);
                if (stock.YearHigh > 0 && 
                    stock.Ask > 0 && 
                    stock.Ask * 2 < stock.YearHigh)
                {
                    if (!Configuracao.getListPennyStocks() && 
                        stock.Ask < 1.00) {
                        continue;
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("Ação: " + stock.Symbol + "; ");
                    sb.append("Atual: " + stock.Ask + "; ");
                    sb.append("Variação ano: " + stock.YearLow + "-" + stock.YearHigh + "; ");
                    //TODO
                    //sb.Append("Vezes em que dobrou: " + ObterVezesEmQueDobrou(stock.Symbol, bolsa.Nome) + "; ");

                    //http://www.bmfbovespa.com.br/indices/ResumoCarteiraTeorica.aspx?Indice=IDIV&idioma=pt-br
                    //TODO:
                    sb.append("IDIV: ????; "); //Destaque do IDIV
                    //http://finance.yahoo.com/news/investing-education--p-e-ratio-191844134.html
                    //Perto de 1 = cálcular para dobrar... P/E (ttm) e EPS (ttm):
                    /*
                    sb.append("PEG: ");
                    if (stock.PEGRatio > 0)
                        sb.append(stock.PEGRatio);
                    else
                        sb.append("???");
                    */
                    sb.append("; ");
                    System.out.println(sb.toString());
                }
            }
            catch (Exception ex)
            {
                System.out.println("erro ao converter :" + acao + ";"); 
            }
        }
        System.out.println();
    }

        //TODO
        //private static int ObterVezesEmQueDobrou(string simboloAcao, string nomeBolsa)
        //{
        //    // Load stock from the MSSQL repository.
        //    StockBusiness stockBusiness = new StockBusiness(new MSSQLStockRepository());

        //    Acao acao = stockBusiness.GetStockBySymbol(simboloAcao);
        //    if (acao == null)
        //    {
        //        acao.Bolsa = new Bolsa() { Nome = nomeBolsa };
        //        acao.Simbolo = simboloAcao;
        //        //TODO: não esquecer de cadastrar nova bolsa se ela não existir
        //        stockBusiness.InsertStock(acao);
        //        return 0;
        //    }

        //    //Nova ação cadastrada                     

        //    //TODO: não esquecer de chamar somente uma vez esse método
        //    List<Acao> stockList = stockBusiness.GetStocks();


        //}

}
