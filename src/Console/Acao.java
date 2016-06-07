/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Console;

/**
 *
 * @author Jean Pierre Patzlaff
 */
public class Acao {    
        public Acao() { }
        
        private int acaoID;
        private String simbolo;
        private Bolsa bolsa;

        public Acao(int acaoID, String simbolo, int bolsaID) {
            this.acaoID = acaoID;
            this.simbolo = simbolo;
            this.bolsa = new Bolsa();
            this.bolsa.setBolsaID(bolsaID);
        }

        public int getAcaoID() {
            return this.acaoID;
        }       
        public void setAcaoID(int acaoID) {
            this.acaoID = acaoID; 
        }
        
        public String getSimbolo() { 
            return this.simbolo;
        }
        
        public void setSimbolo(String simbolo) {
            this.simbolo = simbolo;
        }
        
        public Bolsa getBolsa() { 
            return this.bolsa;
        }
        
        public void setBolsa(Bolsa bolsa) {
            this.bolsa = bolsa;
        }
}
