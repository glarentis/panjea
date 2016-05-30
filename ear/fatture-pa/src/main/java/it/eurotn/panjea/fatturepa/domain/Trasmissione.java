package it.eurotn.panjea.fatturepa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;

@Entity
@Audited
@Table(name = "ftpa_trasmissione")
public class Trasmissione extends EntityBase {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Nazione nazione;

    @Column(nullable = false)
    private String codiceIdentificativoFiscale;

    @Column(nullable = false)
    private String progressivoInvio;

    @Column(nullable = false)
    private FormatoTrasmissioneType formatoTrasmissione;

    @Column(nullable = false)
    private String codiceDestinatario;

    private String telefono;

    private String email;

    /**
     * @return the codiceDestinatario
     */
    public String getCodiceDestinatario() {
        return codiceDestinatario;
    }

    /**
     * @return the codiceIdentificativoFiscale
     */
    public String getCodiceIdentificativoFiscale() {
        return codiceIdentificativoFiscale;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the formatoTrasmissione
     */
    public FormatoTrasmissioneType getFormatoTrasmissione() {
        return formatoTrasmissione;
    }

    /**
     * @return the nazione
     */
    public Nazione getNazione() {
        return nazione;
    }

    /**
     * @return the progressivoInvio
     */
    public String getProgressivoInvio() {
        return progressivoInvio;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param codiceDestinatario
     *            the codiceDestinatario to set
     */
    public void setCodiceDestinatario(String codiceDestinatario) {
        this.codiceDestinatario = codiceDestinatario;
    }

    /**
     * @param codiceFiscale
     *            the codiceFiscale to set
     */
    public void setCodiceIdentificativoFiscale(String codiceFiscale) {
        this.codiceIdentificativoFiscale = codiceFiscale;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param formatoTrasmissione
     *            the formatoTrasmissione to set
     */
    public void setFormatoTrasmissione(FormatoTrasmissioneType formatoTrasmissione) {
        this.formatoTrasmissione = formatoTrasmissione;
    }

    /**
     * @param nazione
     *            the nazione to set
     */
    public void setNazione(Nazione nazione) {
        this.nazione = nazione;
    }

    /**
     * @param progressivoInvio
     *            the progressivoInvio to set
     */
    public void setProgressivoInvio(String progressivoInvio) {
        this.progressivoInvio = progressivoInvio;
    }

    /**
     * @param telefono
     *            the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
