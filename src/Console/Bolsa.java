/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Console;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jean Pierre Patzlaff
 */
public class Bolsa {
    public Bolsa() { }
    
    private int bolsaID;
    private String nome;
    private List<String> lista;

    public Bolsa(int bolsaID, String nome)
    {
        this.bolsaID = bolsaID;
        this.nome = nome;
    }
    
    public int getBolsaID() { 
        return this.bolsaID;
    }
    
    public void setBolsaID(int bolsaID) {
        this.bolsaID = bolsaID;
    }
    
    public String getNome() { 
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<String> getLista() {
        return this.lista;
    }    
    
    public void setLista(List<String> lista) {
        this.lista = lista;
    }
}
