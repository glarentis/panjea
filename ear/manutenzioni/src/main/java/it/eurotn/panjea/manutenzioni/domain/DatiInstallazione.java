package it.eurotn.panjea.manutenzioni.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;

@Embeddable
public class DatiInstallazione implements Serializable {
    private static final long serialVersionUID = 800330047659261861L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operatore tecnico;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operatore caricatore;

    @ManyToOne(fetch = FetchType.LAZY)
    private CodiceIva ivaSomministrazione;

    @Enumerated
    private TipoContrattoInstallazione tipoContrattoInstallazione;

    private boolean pubblico;

    private boolean autoAlimentato;

    public DatiInstallazione() {
        tipoContrattoInstallazione = TipoContrattoInstallazione.COMODATO;
    }

    /**
     * @return the caricatore
     */
    public final Operatore getCaricatore() {
        return caricatore;
    }

    /**
     * @return Returns the ivaSomministrazione.
     */
    public CodiceIva getIvaSomministrazione() {
        return ivaSomministrazione;
    }

    /**
     * @return the tecnico
     */
    public final Operatore getTecnico() {
        return tecnico;
    }

    /**
     * @return Returns the tipoContrattoInstallazione.
     */
    public TipoContrattoInstallazione getTipoContrattoInstallazione() {
        return tipoContrattoInstallazione;
    }

    /**
     * @return Returns the autoAlimentato.
     */
    public boolean isAutoAlimentato() {
        return autoAlimentato;
    }

    /**
     * @return Returns the pubblico.
     */
    public boolean isPubblico() {
        return pubblico;
    }

    /**
     * @param autoAlimentato
     *            The autoAlimentato to set.
     */
    public void setAutoAlimentato(boolean autoAlimentato) {
        this.autoAlimentato = autoAlimentato;
    }

    /**
     * @param caricatore
     *            the caricatore to set
     */
    public final void setCaricatore(Operatore caricatore) {
        this.caricatore = caricatore;
    }

    /**
     * @param ivaSomministrazione
     *            The ivaSomministrazione to set.
     */
    public void setIvaSomministrazione(CodiceIva ivaSomministrazione) {
        this.ivaSomministrazione = ivaSomministrazione;
    }

    /**
     * @param pubblico
     *            The pubblico to set.
     */
    public void setPubblico(boolean pubblico) {
        this.pubblico = pubblico;
    }

    /**
     * @param tecnico
     *            the tecnico to set
     */
    public final void setTecnico(Operatore tecnico) {
        this.tecnico = tecnico;
    }

    /**
     * @param tipoContrattoInstallazione
     *            The tipoContrattoInstallazione to set.
     */
    public void setTipoContrattoInstallazione(TipoContrattoInstallazione tipoContrattoInstallazione) {
        this.tipoContrattoInstallazione = tipoContrattoInstallazione;
    }

}
