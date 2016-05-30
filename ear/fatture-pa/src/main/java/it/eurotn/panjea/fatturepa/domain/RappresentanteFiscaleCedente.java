package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class RappresentanteFiscaleCedente implements Serializable {

    private static final long serialVersionUID = -8447165538475466918L;

    private boolean enable;

    @ManyToOne
    @JoinColumn(name = "rappFiscNazione")
    private Nazione nazione;

    @Column(length = 50, name = "rappFiscCodiceIdentificativoFiscale")
    private String codiceIdentificativoFiscale;

    @Column(length = 50, name = "rappFiscCodiceFiscale")
    private String codiceFiscale;

    @Column(length = 80, name = "rappFiscDenominazione")
    private String denominazione;

    @Column(length = 60, name = "rappFiscNome")
    private String nome;

    @Column(length = 60, name = "rappFiscCognome")
    private String cognome;

    @Column(length = 10, name = "rappFiscDescrizioneTitolare")
    private String descrizioneTitolare;

    @Column(length = 17, name = "rappFiscDescrizioneCodiceEori")
    private String descrizioneCodiceEori;

    {
        enable = false;
    }

    /**
     * @return the codiceFiscale
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * @return the codiceIdentificativoFiscale
     */
    public String getCodiceIdentificativoFiscale() {
        return codiceIdentificativoFiscale;
    }

    /**
     * @return the cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * @return the denominazione
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * @return the descrizioneCodiceEori
     */
    public String getDescrizioneCodiceEori() {
        return descrizioneCodiceEori;
    }

    /**
     * @return the descrizioneTitolare
     */
    public String getDescrizioneTitolare() {
        return descrizioneTitolare;
    }

    /**
     * @return the nazione
     */
    public Nazione getNazione() {
        return nazione;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param codiceFiscale
     *            the codiceFiscale to set
     */
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    /**
     * @param codiceIdentificativoFiscale
     *            the codiceIdentificativoFiscale to set
     */
    public void setCodiceIdentificativoFiscale(String codiceIdentificativoFiscale) {
        this.codiceIdentificativoFiscale = codiceIdentificativoFiscale;
    }

    /**
     * @param cognome
     *            the cognome to set
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @param denominazione
     *            the denominazione to set
     */
    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    /**
     * @param descrizioneCodiceEori
     *            the descrizioneCodiceEori to set
     */
    public void setDescrizioneCodiceEori(String descrizioneCodiceEori) {
        this.descrizioneCodiceEori = descrizioneCodiceEori;
    }

    /**
     * @param descrizioneTitolare
     *            the descrizioneTitolare to set
     */
    public void setDescrizioneTitolare(String descrizioneTitolare) {
        this.descrizioneTitolare = descrizioneTitolare;
    }

    /**
     * @param enable
     *            the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @param nazione
     *            the nazione to set
     */
    public void setNazione(Nazione nazione) {
        this.nazione = nazione;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
