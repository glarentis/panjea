/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe Embedded LegaleRappresentate.
 *
 * @author adriano
 * @version 1.0, 13/dic/07
 */
@Embeddable
public class LegaleRappresentante implements Serializable {

	private static final long serialVersionUID = -1402460923748958954L;

	public static final String REF = "LegaleRappresentante";
	public static final String PROP_PARTITA_I_V_A = "partitaIVA";
	public static final String PROP_SESSO = "sesso";
	public static final String PROP_DATA_NASCITA = "dataNascita";
	public static final String PROP_CARICA = "carica";
	public static final String PROP_INDIRIZZO_MAIL = "indirizzoMail";
	public static final String PROP_VIA = "viaResidenza";
	public static final String PROP_NOME = "nome";
	public static final String PROP_DATA_CARICA = "dataCarica";
	public static final String PROP_COGNOME = "cognome";
	public static final String PROP_FAX = "fax";
	public static final String PROP_CODICE_FISCALE = "codiceFiscale";
	public static final String PROP_TELEFONO = "telefono";

	@Column(name = "legaleRappresentante_cognome", length = 40)
	private String cognome;

	@Column(name = "legaleRappresentante_nome", length = 40)
	private String nome;

	@Column(name = "legaleRappresentante_dataNascita")
	@Temporal(TemporalType.DATE)
	private Date dataNascita;

	@Column(name = "legaleRappresentante_sesso")
	private Sesso sesso;

	@Embedded
	@AssociationOverrides({
		@AssociationOverride(name = "nazione", joinColumns = @JoinColumn(name = "nazioneNascita_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo1", joinColumns = @JoinColumn(name = "lvlNascita1_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo2", joinColumns = @JoinColumn(name = "lvlNascita2_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo3", joinColumns = @JoinColumn(name = "lvlNascita3_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo4", joinColumns = @JoinColumn(name = "lvlNascita4_lr_id")),
		@AssociationOverride(name = "localita", joinColumns = @JoinColumn(name = "localitaNascita_lr_id")),
		@AssociationOverride(name = "cap", joinColumns = @JoinColumn(name = "capNascita_lr_id")) })
	private DatiGeografici datiGeograficiNascita;

	@Embedded
	@AssociationOverrides({
		@AssociationOverride(name = "nazione", joinColumns = @JoinColumn(name = "nazioneResidenza_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo1", joinColumns = @JoinColumn(name = "lvlResidenza1_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo2", joinColumns = @JoinColumn(name = "lvlResidenza2_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo3", joinColumns = @JoinColumn(name = "lvlResidenza3_lr_id")),
		@AssociationOverride(name = "livelloAmministrativo4", joinColumns = @JoinColumn(name = "lvlResidenza4_lr_id")),
		@AssociationOverride(name = "localita", joinColumns = @JoinColumn(name = "localitaResidenza_lr_id")),
		@AssociationOverride(name = "cap", joinColumns = @JoinColumn(name = "capResidenza_lr_id")) })
	private DatiGeografici datiGeograficiResidenza;

	@Column(name = "legaleRappresentante_viaResidenza", length = 50)
	private String viaResidenza;

	@Column(name = "legaleRappresentante_partitaIVA", length = 25)
	private String partitaIVA;

	@Column(name = "legaleRappresentante_codiceFiscale", length = 25)
	private String codiceFiscale;

	@Column(name = "legaleRappresentante_telefono", length = 20)
	private String telefono;

	@Column(name = "legaleRappresentante_fax", length = 20)
	private String fax;

	@Column(name = "legaleRappresentante_indirizzoMail", length = 50)
	private String indirizzoMail;

	@Column(name = "legaleRappresentante_dataCarica")
	@Temporal(TemporalType.DATE)
	private Date dataCarica;

	@ManyToOne
	@JoinColumn(name = "legaleRappresentante_carica_id")
	private Carica carica;

	@Column(name = "legaleRappresentante_codiceIdentificativoFiscale", length = 50)
	private String codiceIdentificativoFiscale;

	@Column(name = "legaleRappresentante_denominazione", length = 80)
	private String denominazione;

	@Column(name = "legaleRappresentante_titolo", length = 10)
	private String titolo;

	@Column(name = "legaleRappresentante_codiceEORI", length = 17)
	private String codiceEori;

	@ManyToOne
	@JoinColumn(name = "legaleRappresentante_nazione_id")
	private Nazione nazione;

	/**
	 * Costruttore.
	 */
	public LegaleRappresentante() {
		initialize();
	}

	/**
	 * @return Returns the carica.
	 */
	public Carica getCarica() {
		return carica;
	}

	/**
	 * @return the codiceEori
	 */
	public String getCodiceEori() {
		return codiceEori;
	}

	/**
	 * @return Returns the codiceFiscale.
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
	 * @return Returns the cognome.
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @return Returns the dataCarica.
	 */
	public Date getDataCarica() {
		return dataCarica;
	}

	/**
	 * @return Returns the dataNascita.
	 */
	public Date getDataNascita() {
		return dataNascita;
	}

	/**
	 * @return the datiGeograficiNascita
	 */
	public DatiGeografici getDatiGeograficiNascita() {
		if (datiGeograficiNascita == null) {
			datiGeograficiNascita = new DatiGeografici();
		}
		return datiGeograficiNascita;
	}

	/**
	 * @return the datiGeograficiResidenza
	 */
	public DatiGeografici getDatiGeograficiResidenza() {
		if (datiGeograficiResidenza == null) {
			datiGeograficiResidenza = new DatiGeografici();
		}
		return datiGeograficiResidenza;
	}

	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @return Returns the fax.
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @return Returns the indirizzoMail.
	 */
	public String getIndirizzoMail() {
		return indirizzoMail;
	}

	/**
	 * @return the nazione
	 */
	public Nazione getNazione() {
		return nazione;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the partitaIva.
	 */
	public String getPartitaIVA() {
		return partitaIVA;
	}

	/**
	 * @return Returns the sesso.
	 */
	public Sesso getSesso() {
		return sesso;
	}

	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @return the titolo
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * @return Returns the viaResidenza.
	 */
	public String getViaResidenza() {
		return viaResidenza;
	}

	/**
	 * Init degli embedded datiGeografici.
	 */
	@PostLoad
	public void initialize() {
	}

	/**
	 * @param carica
	 *            The carica to set.
	 */
	public void setCarica(Carica carica) {
		this.carica = carica;
	}

	/**
	 * @param codiceEori the codiceEori to set
	 */
	public void setCodiceEori(String codiceEori) {
		this.codiceEori = codiceEori;
	}

	/**
	 * @param codiceFiscale
	 *            The codiceFiscale to set.
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @param codiceIdentificativoFiscale the codiceIdentificativoFiscale to set
	 */
	public void setCodiceIdentificativoFiscale(String codiceIdentificativoFiscale) {
		this.codiceIdentificativoFiscale = codiceIdentificativoFiscale;
	}

	/**
	 * @param cognome
	 *            The cognome to set.
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @param dataCarica
	 *            The dataCarica to set.
	 */
	public void setDataCarica(Date dataCarica) {
		this.dataCarica = dataCarica;
	}

	/**
	 * @param dataNascita
	 *            The dataNascita to set.
	 */
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	/**
	 * @param datiGeograficiNascita
	 *            the datiGeograficiNascita to set
	 */
	public void setDatiGeograficiNascita(DatiGeografici datiGeograficiNascita) {
		this.datiGeograficiNascita = datiGeograficiNascita;
	}

	/**
	 * @param datiGeograficiResidenza
	 *            the datiGeograficiResidenza to set
	 */
	public void setDatiGeograficiResidenza(DatiGeografici datiGeograficiResidenza) {
		this.datiGeograficiResidenza = datiGeograficiResidenza;
	}

	/**
	 * @param denominazione the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @param fax
	 *            The fax to set.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @param indirizzoMail
	 *            The indirizzoMail to set.
	 */
	public void setIndirizzoMail(String indirizzoMail) {
		this.indirizzoMail = indirizzoMail;
	}

	/**
	 * @param nazione the nazione to set
	 */
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param partitaIVA
	 *            The partitaIva to set.
	 */
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}

	/**
	 * @param sesso
	 *            The sesso to set.
	 */
	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}

	/**
	 * @param telefono
	 *            The telefono to set.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @param titolo the titolo to set
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	/**
	 * @param viaResidenza
	 *            The viaResidenza to set.
	 */
	public void setViaResidenza(String viaResidenza) {
		this.viaResidenza = viaResidenza;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("LegaleRappresentante[");
		buffer.append(super.toString());
		buffer.append(" datiGeograficiNascita = ").append(datiGeograficiNascita);
		buffer.append(" carica = ").append(carica);
		buffer.append(" codiceFiscale = ").append(codiceFiscale);
		buffer.append(" cognome = ").append(cognome);
		buffer.append(" dataCarica = ").append(dataCarica);
		buffer.append(" dataNascita = ").append(dataNascita);
		buffer.append(" fax = ").append(fax);
		buffer.append(" indirizzoMail = ").append(indirizzoMail);
		buffer.append(" nome = ").append(nome);
		buffer.append(" partitaIVA = ").append(partitaIVA);
		buffer.append(" datiGeograficiResidenza = ").append(datiGeograficiResidenza);
		buffer.append(" sesso = ").append(sesso);
		buffer.append(" telefono = ").append(telefono);
		buffer.append(" viaResidenza = ").append(viaResidenza);
		buffer.append("]");
		return buffer.toString();
	}

}
