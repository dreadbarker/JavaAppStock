/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Console;

import java.util.Date;

/**
 *
 * @author Jean Pierre Patzlaff
 */
public class Cotacao {
    public int cotacaoID;
    public Acao acao;
    public Bolsa bolsa;
    public float preco;
    public Date data;

    public Cotacao(int cotacaoID, int acaoID, int bolsaID, float preco, Date data)
    {
        this.cotacaoID = cotacaoID;
        this.acao = new Acao(); 
        this.acao.setAcaoID(acaoID);
        this.bolsa = new Bolsa(); 
        this.bolsa.setBolsaID(bolsaID);
        this.preco = preco;
        this.data = data;
    }    
}
