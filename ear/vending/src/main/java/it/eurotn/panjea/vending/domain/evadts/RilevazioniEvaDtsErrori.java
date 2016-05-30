package it.eurotn.panjea.vending.domain.evadts;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_rilevazioni_eva_dts_errori")
public class RilevazioniEvaDtsErrori extends EntityBase {
    @ManyToOne(fetch = FetchType.LAZY)
    private RilevazioneEvaDts rilevazioneEvaDts;
    protected int progressivo;
    protected String tipo;
    protected String codice;
    protected String elemento;
    protected int occorrenze;

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the elemento.
     */
    public String getElemento() {
        return elemento;
    }

    /**
     * @return Returns the occorrenze.
     */
    public int getOccorrenze() {
        return occorrenze;
    }

    /**
     * @return Returns the progressivo.
     */
    public int getProgressivo() {
        return progressivo;
    }

    /**
     * @return Returns the rilevazioneEvaDts.
     */
    public RilevazioneEvaDts getRilevazioneEvaDts() {
        return rilevazioneEvaDts;
    }

    /**
     * @return Returns the tipo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param codice
     *            The codice to set.
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param elemento
     *            The elemento to set.
     */
    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    /**
     * @param occorrenze
     *            The occorrenze to set.
     */
    public void setOccorrenze(int occorrenze) {
        this.occorrenze = occorrenze;
    }

    /**
     * @param progressivo
     *            The progressivo to set.
     */
    public void setProgressivo(int progressivo) {
        this.progressivo = progressivo;
    }

    /**
     * @param rilevazioneEvaDts
     *            The rilevazioneEvaDts to set.
     */
    public void setRilevazioneEvaDts(RilevazioneEvaDts rilevazioneEvaDts) {
        this.rilevazioneEvaDts = rilevazioneEvaDts;
    }

    /**
     * @param tipo
     *            The tipo to set.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
