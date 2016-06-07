/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package YahooFinance;

/**
 *
 * @author Jean Pierre Patzlaff
 */
public class YahooExceptionNoComponentsDataAvailable extends Exception {
    
    public YahooExceptionNoComponentsDataAvailable() {
        super("Não há dados disponíveis no momento para os componentes.");
    }
    
}
