package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;

/**
 * @author fattazzo
 *
 */
@Entity
@Table(name = "para_ricerca_stampa_contratti")
public class ParametriRicercaStampaContratti extends AbstractParametriRicerca implements Serializable {

    private static final long serialVersionUID = -6005913285868028619L;

    @ManyToOne(optional = true)
    private EntitaLite entita;

    @ManyToOne(optional = true)
    private ArticoloLite articolo;

    private boolean escludiContrattiNonAttivi;

    {
        escludiContrattiNonAttivi = false;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the escludiContrattiNonAttivi
     */
    public boolean isEscludiContrattiNonAttivi() {
        return escludiContrattiNonAttivi;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param escludiContrattiNonAttivi
     *            the escludiContrattiNonAttivi to set
     */
    public void setEscludiContrattiNonAttivi(boolean escludiContrattiNonAttivi) {
        this.escludiContrattiNonAttivi = escludiContrattiNonAttivi;
    }

}
