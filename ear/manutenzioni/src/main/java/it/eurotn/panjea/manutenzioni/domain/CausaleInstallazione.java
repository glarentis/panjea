package it.eurotn.panjea.manutenzioni.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "manu_causali_installazioni")
public class CausaleInstallazione extends EntityBase {
    public enum TipoInstallazione {
        PRIMA_INSTALLAZIONE(true), INSTALLAZIONE(true), RITIRO(false), RITIRO_DEFINITIVO(false);

        private boolean installazione;

        /**
         *
         * @param paramInstallazione
         *            indica se la tipologia è un'installazione (altrimenti ritiro)
         */
        private TipoInstallazione(final boolean paramInstallazione) {
            installazione = paramInstallazione;
        }

        /**
         * @return true se è una installazione
         */
        public boolean isInstallazione() {
            return installazione;
        }

        /**
         *
         * @return true se è un ritiro
         */
        public boolean isRitiro() {
            return !installazione;
        }
    }

    @Column(length = 15)
    private String codice;

    private Integer ordinamento;

    @Column(length = 40)
    private String descrizione;

    private TipoInstallazione tipoInstallazione;

    /**
     * Costruttore.
     */
    public CausaleInstallazione() {
        ordinamento = 10;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the ordinamento.
     */
    public Integer getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return Returns the tipoInstallazione.
     */
    public TipoInstallazione getTipoInstallazione() {
        return tipoInstallazione;
    }

    /**
     * @param codice
     *            The codice to set.
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param ordinamento
     *            The ordinamento to set.
     */
    public void setOrdinamento(Integer ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param tipoInstallazione
     *            The tipoInstallazione to set.
     */
    public void setTipoInstallazione(TipoInstallazione tipoInstallazione) {
        this.tipoInstallazione = tipoInstallazione;
    }

}