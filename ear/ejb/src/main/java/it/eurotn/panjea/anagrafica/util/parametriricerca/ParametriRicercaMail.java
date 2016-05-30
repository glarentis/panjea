package it.eurotn.panjea.anagrafica.util.parametriricerca;

import javax.persistence.Entity;
import javax.persistence.Table;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

/**
 * @author leonardo
 */
@Entity
@Table(name = "para_ricerca_mail")
public class ParametriRicercaMail extends AbstractParametriRicerca {

    private static final long serialVersionUID = 8231314750068734728L;

    private Periodo periodo;

    private EntitaLite entita;

    private String testo;

    /**
     * Costruttore.
     */
    public ParametriRicercaMail() {
        super();
        periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
        testo = "";
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the periodo
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @return the testo
     */
    public String getTesto() {
        return testo;
    }

    /**
     * @return true se l'entita è impostata, false altrimenti
     */
    public boolean hasEntita() {
        return this.entita != null && this.entita.getId() != null;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param periodo
     *            the periodo to set
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @param testo
     *            the testo to set
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

}
