package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;

/**
 * Contiene tutti i dati riguardanti la movimentazione di un articolo.<br/>
 *
 * @author fattazzo
 */
public class MovimentazioneArticolo implements Serializable {

    private static final long serialVersionUID = 4351552678055753538L;

    private Double giacenzaPrecedente = new Double(-1);

    private Double giacenzaFinale = new Double(-1);

    private Double giacenzaAttuale = new Double(-1);

    private List<RigaMovimentazione> righeMovimentazione = new ArrayList<RigaMovimentazione>();

    private int maxNumeroDecimaliPrezzo;
    private int maxNumeroDecimaliQta;

    private DepositoLite deposito;

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     * @return the giacenzaAttuale
     */
    public Double getGiacenzaAttuale() {
        return giacenzaAttuale;
    }

    /**
     * @return the giacenzaFinale
     */
    public Double getGiacenzaFinale() {
        return giacenzaFinale;
    }

    /**
     * @return the giacenzaPrecedente
     */
    public Double getGiacenzaPrecedente() {
        return giacenzaPrecedente;
    }

    /**
     * @return the maxNumeroDecimaliPrezzo
     */
    public int getMaxNumeroDecimaliPrezzo() {
        return maxNumeroDecimaliPrezzo;
    }

    /**
     * @return the maxNumeroDecimaliQta
     */
    public int getMaxNumeroDecimaliQta() {
        return maxNumeroDecimaliQta;
    }

    /**
     * @return righeMovimentazione
     */
    public List<RigaMovimentazione> getRigheMovimentazione() {
        return righeMovimentazione;
    }

    /**
     * @param deposito
     *            the deposito to set
     */
    public void setDeposito(DepositoLite deposito) {
        this.deposito = deposito;
    }

    /**
     * @param giacenzaAttuale
     *            the giacenzaAttuale to set
     */
    public void setGiacenzaAttuale(Double giacenzaAttuale) {
        this.giacenzaAttuale = giacenzaAttuale;
    }

    /**
     * @param giacenzaFinale
     *            the giacenzaFinale to set
     */
    public void setGiacenzaFinale(Double giacenzaFinale) {
        this.giacenzaFinale = giacenzaFinale;
    }

    /**
     * @param giacenzaPrecedente
     *            the giacenzaPrecedente to set
     */
    public void setGiacenzaPrecedente(Double giacenzaPrecedente) {
        this.giacenzaPrecedente = giacenzaPrecedente;
    }

    /**
     * @param maxNumeroDecimaliPrezzo
     *            the maxNumeroDecimaliPrezzo to set
     */
    public void setMaxNumeroDecimaliPrezzo(int maxNumeroDecimaliPrezzo) {
        this.maxNumeroDecimaliPrezzo = maxNumeroDecimaliPrezzo;
    }

    /**
     * @param maxNumeroDecimaliQta
     *            the maxNumeroDecimaliQta to set
     */
    public void setMaxNumeroDecimaliQta(int maxNumeroDecimaliQta) {
        this.maxNumeroDecimaliQta = maxNumeroDecimaliQta;
    }

    /**
     * @param righeMovimentazione
     *            the righeMovimentazione to set
     */
    public void setRigheMovimentazione(List<RigaMovimentazione> righeMovimentazione) {
        this.righeMovimentazione = righeMovimentazione;
    }

}
