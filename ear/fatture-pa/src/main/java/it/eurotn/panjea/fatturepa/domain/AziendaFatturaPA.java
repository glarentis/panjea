
package it.eurotn.panjea.fatturepa.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;

/**
 * @author fattazzo
 *
 */
@Entity
@Audited
@Table(name = "ftpa_dati_azienda")
public class AziendaFatturaPA extends EntityBase {

    private static final long serialVersionUID = 4884893536577713005L;

    @ManyToOne
    private Azienda azienda;

    @Column(length = 50)
    private String codiceIdentificativoFiscale;

    @Column(length = 17)
    private String descrizioneCodiceEori;

    @Column(length = 10)
    private String descrizioneTitolare;

    @Column(length = 60)
    private String descrizioneAlbo;
    @Column(length = 2)
    private String provinciaAlbo;
    @Column(length = 60)
    private String descrizioneNumeroIscrizioneAlbo;
    @Temporal(TemporalType.DATE)
    private Date dataIscrizioneAlbo;

    @ManyToOne
    @NotAudited
    private TipoRegimeFiscale regimeFiscale;

    @Embedded
    private StabileOrganizzazione stabileOrganizzazione;

    @Embedded
    private Contatto contatto;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "enable", column = @Column(name = "enableRea") ) })
    private DatiIscrizioneRea datiIscrizioneRea;

    @Embedded
    private RappresentanteFiscaleCedente rappresentanteFiscale;

    @Transient
    private SedeAzienda sedePrincipale;

    /**
     * @return the azienda
     */
    public Azienda getAzienda() {
        return azienda;
    }

    /**
     * @return codice fiscale dell'azienda.
     */
    public String getCodiceFiscale() {
        return getAzienda().getCodiceFiscale();
    }

    /**
     * @return the codiceIdentificativoFiscale
     */
    public String getCodiceIdentificativoFiscale() {
        return codiceIdentificativoFiscale;
    }

    /**
     * @return the contatto
     */
    public Contatto getContatto() {
        if (contatto == null) {
            contatto = new Contatto();
        }

        return contatto;
    }

    /**
     * @return the dataIscrizioneAlbo
     */
    public Date getDataIscrizioneAlbo() {
        return dataIscrizioneAlbo;
    }

    /**
     * @return the datiIscrizioneRea
     */
    public DatiIscrizioneRea getDatiIscrizioneRea() {
        if (datiIscrizioneRea == null) {
            datiIscrizioneRea = new DatiIscrizioneRea();
        }

        return datiIscrizioneRea;
    }

    /**
     * @return denominazione dell'azienda.
     */
    public String getDenominazione() {
        return getAzienda().getDenominazione();
    }

    /**
     * @return descrizione
     */
    @Transient
    public String getDescrizione() {
        return (StringUtils.defaultString(azienda.getDenominazione(),
                azienda.getLegaleRappresentante().getNome() + " " + azienda.getLegaleRappresentante().getCognome()))
                + " " + (codiceIdentificativoFiscale != null ? "(" + codiceIdentificativoFiscale + ")"
                        : "(" + azienda.getCodiceFiscale() + ")");
    }

    /**
     * @return the descrizioneAlbo
     */
    public String getDescrizioneAlbo() {
        return descrizioneAlbo;
    }

    /**
     * @return the descrizioneCodiceEori
     */
    public String getDescrizioneCodiceEori() {
        return descrizioneCodiceEori;
    }

    /**
     * @return the descrizioneNumeroIscrizioneAlbo
     */
    public String getDescrizioneNumeroIscrizioneAlbo() {
        return descrizioneNumeroIscrizioneAlbo;
    }

    /**
     * @return the descrizioneTitolare
     */
    public String getDescrizioneTitolare() {
        return descrizioneTitolare;
    }

    /**
     * @return the email
     */
    public String getEMail() {
        return getSedePrincipale().getSede().getIndirizzoMail();
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return getSedePrincipale().getSede().getFax();
    }

    /**
     * @return codice eori del legale rappresentante
     */
    public String getLegaleRappresentanteCodiceEori() {
        return StringUtils.isBlank(getAzienda().getLegaleRappresentante().getCodiceEori()) ? null
                : getAzienda().getLegaleRappresentante().getCodiceEori();
    }

    /**
     * @return codice fiscale del legale rappresentante
     */
    public String getLegaleRappresentanteCodiceFiscale() {
        return getAzienda().getLegaleRappresentante().getCodiceFiscale();
    }

    /**
     * @return codice nazione del legale rappresentante
     */
    public String getLegaleRappresentanteCodiceNazione() {
        return getAzienda().getLegaleRappresentante().getNazione() != null
                ? getAzienda().getLegaleRappresentante().getNazione().getCodice() : null;
    }

    /**
     * @return cognome del legale rappresentante
     */
    public String getLegaleRappresentanteCognome() {
        return getAzienda().getLegaleRappresentante().getCognome();
    }

    /**
     * @return denominazione del legale rappresentante
     */
    public String getLegaleRappresentanteDenominazione() {
        return getAzienda().getLegaleRappresentante().getDenominazione();
    }

    /**
     * @return identificativo fiscale del legale rappresentante
     */
    public String getLegaleRappresentanteIdentificativoFiscale() {
        return getAzienda().getLegaleRappresentante().getCodiceIdentificativoFiscale();
    }

    /**
     * @return nome del legale rappresentante
     */
    public String getLegaleRappresentanteNome() {
        return getAzienda().getLegaleRappresentante().getNome();
    }

    /**
     * @return titolo del legale rappresentante
     */
    public String getLegaleRappresentanteTitolo() {
        return getAzienda().getLegaleRappresentante().getTitolo();
    }

    /**
     * @return the provinciaAlbo
     */
    public String getProvinciaAlbo() {
        return provinciaAlbo;
    }

    /**
     * @return the rappresentanteFiscale
     */
    public RappresentanteFiscaleCedente getRappresentanteFiscale() {
        if (rappresentanteFiscale == null) {
            rappresentanteFiscale = new RappresentanteFiscaleCedente();
        }
        return rappresentanteFiscale;
    }

    /**
     * @return the regimeFiscale
     */
    public TipoRegimeFiscale getRegimeFiscale() {
        return regimeFiscale;
    }

    /**
     * @return cap della sede
     */
    public String getSedeCAP() {
        return getSedePrincipale().getSede().getDatiGeografici().getCap().getDescrizione();
    }

    /**
     * @return comune della sede
     */
    public String getSedeComune() {
        Localita localita = getSedePrincipale().getSede().getDatiGeografici().getLocalita();
        return localita != null && !localita.isNew() ? localita.getDescrizione() : null;
    }

    /**
     * @return indirizzo della sede
     */
    public String getSedeIndirizzo() {
        return getSedePrincipale().getSede().getIndirizzo();
    }

    /**
     * @return nazione della sede
     */
    public String getSedeNazione() {
        return getSedePrincipale().getSede().getDatiGeografici().getNazione().getCodice();
    }

    /**
     * @return numero civico della sede
     */
    public String getSedeNumeroCivico() {
        return getSedePrincipale().getSede().getNumeroCivico();
    }

    private SedeAzienda getSedePrincipale() {
        if (sedePrincipale == null) {
            for (SedeAzienda sede : getAzienda().getSedi()) {
                if (sede.getTipoSede().isSedePrincipale()) {
                    sedePrincipale = sede;
                    break;
                }
            }
        }

        return sedePrincipale;
    }

    /**
     * @return provincia della sede
     */
    public String getSedeProvincia() {
        return getSedePrincipale().getSede().getDatiGeografici().hasLivelloAmministrativo2()
                ? getSedePrincipale().getSede().getDatiGeografici().getLivelloAmministrativo2().getSigla() : null;
    }

    /**
     * @return the stabileOrganizzazione
     */
    public StabileOrganizzazione getStabileOrganizzazione() {
        if (stabileOrganizzazione == null) {
            stabileOrganizzazione = new StabileOrganizzazione();
        }

        return stabileOrganizzazione;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return getSedePrincipale().getSede().getTelefono();
    }

    /**
     * @param azienda
     *            the azienda to set
     */
    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    /**
     * @param codiceIdentificativoFiscale
     *            the codiceIdentificativoFiscale to set
     */
    public void setCodiceIdentificativoFiscale(String codiceIdentificativoFiscale) {
        this.codiceIdentificativoFiscale = codiceIdentificativoFiscale;
    }

    /**
     * @param contatto
     *            the contatto to set
     */
    public void setContatto(Contatto contatto) {
        this.contatto = contatto;
    }

    /**
     * @param dataIscrizioneAlbo
     *            the dataIscrizioneAlbo to set
     */
    public void setDataIscrizioneAlbo(Date dataIscrizioneAlbo) {
        this.dataIscrizioneAlbo = dataIscrizioneAlbo;
    }

    /**
     * @param datiIscrizioneRea
     *            the datiIscrizioneRea to set
     */
    public void setDatiIscrizioneRea(DatiIscrizioneRea datiIscrizioneRea) {
        this.datiIscrizioneRea = datiIscrizioneRea;
    }

    /**
     * @param descrizioneAlbo
     *            the descrizioneAlbo to set
     */
    public void setDescrizioneAlbo(String descrizioneAlbo) {
        this.descrizioneAlbo = descrizioneAlbo;
    }

    /**
     * @param descrizioneCodiceEori
     *            the descrizioneCodiceEori to set
     */
    public void setDescrizioneCodiceEori(String descrizioneCodiceEori) {
        this.descrizioneCodiceEori = descrizioneCodiceEori;
    }

    /**
     * @param descrizioneNumeroIscrizioneAlbo
     *            the descrizioneNumeroIscrizioneAlbo to set
     */
    public void setDescrizioneNumeroIscrizioneAlbo(String descrizioneNumeroIscrizioneAlbo) {
        this.descrizioneNumeroIscrizioneAlbo = descrizioneNumeroIscrizioneAlbo;
    }

    /**
     * @param descrizioneTitolare
     *            the descrizioneTitolare to set
     */
    public void setDescrizioneTitolare(String descrizioneTitolare) {
        this.descrizioneTitolare = descrizioneTitolare;
    }

    /**
     * @param provinciaAlbo
     *            the provinciaAlbo to set
     */
    public void setProvinciaAlbo(String provinciaAlbo) {
        this.provinciaAlbo = provinciaAlbo;
    }

    /**
     * @param rappresentanteFiscale
     *            the rappresentanteFiscale to set
     */
    public void setRappresentanteFiscale(RappresentanteFiscaleCedente rappresentanteFiscale) {
        this.rappresentanteFiscale = rappresentanteFiscale;
    }

    /**
     * @param regimeFiscale
     *            the regimeFiscale to set
     */
    public void setRegimeFiscale(TipoRegimeFiscale regimeFiscale) {
        this.regimeFiscale = regimeFiscale;
    }

    /**
     * @param stabileOrganizzazione
     *            the stabileOrganizzazione to set
     */
    public void setStabileOrganizzazione(StabileOrganizzazione stabileOrganizzazione) {
        this.stabileOrganizzazione = stabileOrganizzazione;
    }
}
