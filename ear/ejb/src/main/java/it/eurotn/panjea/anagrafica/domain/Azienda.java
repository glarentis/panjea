/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.conai.domain.ConaiTipoIscrizione;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

/**
 * Classe di dominio di Azienda.
 * 
 * @author adriano
 * @version 1.0, 13/dic/07
 */
@Entity
@Audited
@Table(name = "anag_aziende", uniqueConstraints = { @UniqueConstraint(columnNames = "codice") })
@NamedQueries({
		@NamedQuery(name = "Azienda.caricaByCodice", query = " from Azienda a where a.codice = :paramCodice"),
		@NamedQuery(name = "Azienda.caricaLingua", query = " select o.lingua from AziendaLite o where o.codice=:codiceAzienda", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "azienda") }) })
public class Azienda extends EntityBase {

	private static final long serialVersionUID = 1L;

	public static final String REF = "Azienda";
	public static final String PROP_CODICE_RAPPORTO = "codiceRapporto";
	public static final String PROP_PARTITA_I_V_A = "partitaIVA";
	public static final String PROP_SEDE_AMMINISTRATIVA = "sedeAmministrativa";
	public static final String PROP_STATO_AZIENDA = "statoAzienda";
	public static final String PROP_DENOMINAZIONE = "denominazione";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_NUMERO_RAPPORTO = "numeroRapporto";
	public static final String PROP_ABILITATA = "abilitata";
	public static final String PROP_INCREMENTO = "incremento";
	public static final String PROP_DATA_NASCITA = "dataNascita";
	public static final String PROP_DATA_CARICA = "dataCarica";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_CODICE_FISCALE = "codiceFiscale";
	public static final String PROP_DESCRIZIONE_FORMA_GIURIDICA = "descrizioneFormaGiuridica";
	public static final String PROP_SESSO = "sesso";
	public static final String PROP_TIPO_RAPPORTO = "tipoRapporto";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_VIA_RESIDENZA = "viaResidenza";
	public static final String PROP_NOME = "nome";
	public static final String PROP_COGNOME = "cognome";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_CODICE_FORMA_GIURIDICA = "codiceFormaGiuridica";
	public static final String PROP_SEDE_LEGALE = "sedeLegale";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_CODICE_ATTIVITA_PREVALENTE = "codiceAttivitaPrevalente";
	public static final String PROP_DESCRIZIONE_ATTIVITA_PREVALENTE = "descrizioneAttivitaPrevalente";
	public static final String PROP_NUMERO_ISCRIZIONE_TRIBUNALE = "numeroIscrizioneTribunale";
	public static final String PROP_TRIBUNALE_ISCRIZIONE = "tribunaleIscrizione";
	public static final String PROP_NUMERO_ISCRIZIONE_CCIAA = "numeroIscrizioneCCIAA";
	public static final String PROP_CCIAA_ISCRIZIONE = "iscrizioneCciaa";
	public static final String PROP_DATA_INIZIO_ATTIVITA = "dataInizioAttivita";
	public static final String PROP_DATA_INIZIO_ESERCIZIO = "dataInizioEsercizio";
	public static final String PROP_CODICE_SIA = "codiceSIA";

	@Column(length = 10, nullable = false)
	private String codice;

	@Column
	private Boolean abilitata;

	@Column(length = 60, nullable = false)
	private String denominazione;

	@Column(length = 25, nullable = false)
	private String partitaIVA;

	@Column(length = 25, nullable = false)
	private String codiceFiscale;

	@Column(length = 10)
	private String codiceAttivitaPrevalente;

	@Column(length = 300)
	private String descrizioneAttivitaPrevalente;

	@Column(length = 20)
	private String numeroIscrizioneTribunale;

	@Column(length = 300)
	private String tribunaleIscrizione;

	@Column(length = 20)
	private String numeroIscrizioneCCIAA;

	@Column(length = 5)
	private String codiceSIA;

	@Column(length = 300, name = "CCIAAIscrizione")
	private String iscrizioneCciaa;

	@Column
	@Temporal(TemporalType.DATE)
	private Date dataInizioAttivita;

	@Column
	@Temporal(TemporalType.DATE)
	private Date dataInizioEsercizio;

	@Column(length = 3)
	private String lingua;

	@Column(length = 3)
	private String codiceValuta;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "azienda")
	private Set<SedeAzienda> sedi;

	@ManyToOne
	private FormaGiuridica formaGiuridica;

	@Embedded
	private PersonaFisica personaFisica;

	@Embedded
	private LegaleRappresentante legaleRappresentante;

	@Embedded
	private DatiProtocollo datiProtocollo;

	@Column(length = 100)
	private String pec;

	@Column(length = 20)
	private String codiceSocioConai;

	@Enumerated
	private ConaiTipoIscrizione conaiTipoIscrizione;

	/**
	 * Costruttore.
	 */
	public Azienda() {
		initialize();
	}

	/**
	 * @return Returns the abilitata.
	 */
	public Boolean getAbilitata() {
		return abilitata;
	}

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiceAttivitaPrevalente.
	 */
	public String getCodiceAttivitaPrevalente() {
		return codiceAttivitaPrevalente;
	}

	/**
	 * @return Returns the codiceFiscale.
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * @return Returns the codiceSIA.
	 */
	public String getCodiceSIA() {
		return codiceSIA;
	}

	/**
	 * @return the codiceSocioConai
	 */
	public String getCodiceSocioConai() {
		return codiceSocioConai;
	}

	/**
	 * @return Returns the codiceValuta.
	 */
	public String getCodiceValuta() {
		return codiceValuta;
	}

	/**
	 * @return the conaiTipoIscrizione
	 */
	public ConaiTipoIscrizione getConaiTipoIscrizione() {
		return conaiTipoIscrizione;
	}

	/**
	 * @return Returns the dataInizioAttivita.
	 */
	public Date getDataInizioAttivita() {
		return dataInizioAttivita;
	}

	/**
	 * @return Returns the dataInizioEsercizio.
	 */
	public Date getDataInizioEsercizio() {
		return dataInizioEsercizio;
	}

	/**
	 * @return Returns the datiProtocollo.
	 */
	public DatiProtocollo getDatiProtocollo() {
		if (datiProtocollo == null) {
			datiProtocollo = new DatiProtocollo();
		}
		return datiProtocollo;
	}

	/**
	 * @return Returns the denominazione.
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @return Returns the descrizioneAttivitaPrevalente.
	 */
	public String getDescrizioneAttivitaPrevalente() {
		return descrizioneAttivitaPrevalente;
	}

	/**
	 * @return Returns the formaGiuridica.
	 */
	public FormaGiuridica getFormaGiuridica() {
		return formaGiuridica;
	}

	/**
	 * @return the iscrizioneCciaa
	 */
	public String getIscrizioneCciaa() {
		return iscrizioneCciaa;
	}

	/**
	 * @return Returns the legaleRappresentante.
	 */
	public LegaleRappresentante getLegaleRappresentante() {
		if (legaleRappresentante == null) {
			legaleRappresentante = new LegaleRappresentante();
		}
		return legaleRappresentante;
	}

	/**
	 * @return Returns the lingua.
	 */
	public String getLingua() {
		return lingua;
	}

	/**
	 * @return Returns the numeroIscrizioneCCIAA.
	 */
	public String getNumeroIscrizioneCCIAA() {
		return numeroIscrizioneCCIAA;
	}

	/**
	 * @return Returns the numeroIscrizioneTribunale.
	 */
	public String getNumeroIscrizioneTribunale() {
		return numeroIscrizioneTribunale;
	}

	/**
	 * @return Returns the partitaIva.
	 */
	public String getPartitaIVA() {
		return partitaIVA;
	}

	/**
	 * @return the pec
	 */
	public String getPec() {
		return pec;
	}

	/**
	 * @return Returns the personaFisica.
	 */
	public PersonaFisica getPersonaFisica() {
		if (personaFisica == null) {
			personaFisica = new PersonaFisica();
		}
		return personaFisica;
	}

	/**
	 * @return Returns the sedi.
	 */
	public Set<SedeAzienda> getSedi() {
		return sedi;
	}

	/**
	 * @return Returns the tribunaleIscrizione.
	 */
	public String getTribunaleIscrizione() {
		return tribunaleIscrizione;
	}

	/**
	 * Inizializza gli oggetti collegati a this.
	 */
	@PostLoad
	public void initialize() {
		if (abilitata == null) {
			abilitata = true;
		}
	}

	/**
	 * @param abilitata
	 *            The abilitata to set.
	 */
	public void setAbilitata(Boolean abilitata) {
		this.abilitata = abilitata;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAttivitaPrevalente
	 *            The codiceAttivitaPrevalente to set.
	 */
	public void setCodiceAttivitaPrevalente(String codiceAttivitaPrevalente) {
		this.codiceAttivitaPrevalente = codiceAttivitaPrevalente;
	}

	/**
	 * @param codiceFiscale
	 *            The codiceFiscale to set.
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @param codiceSIA
	 *            The codiceSIA to set.
	 */
	public void setCodiceSIA(String codiceSIA) {
		this.codiceSIA = codiceSIA;
	}

	/**
	 * @param codiceSocioConai
	 *            the codiceSocioConai to set
	 */
	public void setCodiceSocioConai(String codiceSocioConai) {
		this.codiceSocioConai = codiceSocioConai;
	}

	/**
	 * @param codiceValuta
	 *            The codiceValuta to set.
	 */
	public void setCodiceValuta(String codiceValuta) {
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @param conaiTipoIscrizione
	 *            the conaiTipoIscrizione to set
	 */
	public void setConaiTipoIscrizione(ConaiTipoIscrizione conaiTipoIscrizione) {
		this.conaiTipoIscrizione = conaiTipoIscrizione;
	}

	/**
	 * @param dataInizioAttivita
	 *            The dataInizioAttivita to set.
	 */
	public void setDataInizioAttivita(Date dataInizioAttivita) {
		this.dataInizioAttivita = dataInizioAttivita;
	}

	/**
	 * @param dataInizioEsercizio
	 *            The dataInizioEsercizio to set.
	 */
	public void setDataInizioEsercizio(Date dataInizioEsercizio) {
		this.dataInizioEsercizio = dataInizioEsercizio;
	}

	/**
	 * @param datiProtocollo
	 *            The datiProtocollo to set.
	 */
	public void setDatiProtocollo(DatiProtocollo datiProtocollo) {
		this.datiProtocollo = datiProtocollo;
	}

	/**
	 * @param denominazione
	 *            The denominazione to set.
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @param descrizioneAttivataPrevalente
	 *            The descrizioneAttivataPrevalente to set.
	 */
	public void setDescrizioneAttivataPrevalente(String descrizioneAttivataPrevalente) {
		this.descrizioneAttivitaPrevalente = descrizioneAttivataPrevalente;
	}

	/**
	 * @param descrizioneAttivitaPrevalente
	 *            The descrizioneAttivitaPrevalente to set.
	 */
	public void setDescrizioneAttivitaPrevalente(String descrizioneAttivitaPrevalente) {
		this.descrizioneAttivitaPrevalente = descrizioneAttivitaPrevalente;
	}

	/**
	 * @param formaGiuridica
	 *            The formaGiuridica to set.
	 */
	public void setFormaGiuridica(FormaGiuridica formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
	}

	/**
	 * @param iscrizioneCciaa
	 *            the iscrizioneCciaa to set
	 */
	public void setIscrizioneCciaa(String iscrizioneCciaa) {
		this.iscrizioneCciaa = iscrizioneCciaa;
	}

	/**
	 * @param legaleRappresentante
	 *            The legaleRappresentante to set.
	 */
	public void setLegaleRappresentante(LegaleRappresentante legaleRappresentante) {
		this.legaleRappresentante = legaleRappresentante;
	}

	/**
	 * @param lingua
	 *            The lingua to set.
	 */
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	/**
	 * @param numeroIscrizioneCCIAA
	 *            The numeroIscrizioneCCIAA to set.
	 */
	public void setNumeroIscrizioneCCIAA(String numeroIscrizioneCCIAA) {
		this.numeroIscrizioneCCIAA = numeroIscrizioneCCIAA;
	}

	/**
	 * @param numeroIscrizioneTribunale
	 *            The numeroIscrizioneTribunale to set.
	 */
	public void setNumeroIscrizioneTribunale(String numeroIscrizioneTribunale) {
		this.numeroIscrizioneTribunale = numeroIscrizioneTribunale;
	}

	/**
	 * @param partitaIVA
	 *            The partitaIva to set.
	 */
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}

	/**
	 * @param pec
	 *            the pec to set
	 */
	public void setPec(String pec) {
		this.pec = pec;
	}

	/**
	 * @param personaFisica
	 *            The personaFisica to set.
	 */
	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}

	/**
	 * @param sedi
	 *            The sedi to set.
	 */
	public void setSedi(Set<SedeAzienda> sedi) {
		this.sedi = sedi;
	}

	/**
	 * @param tribunaleIscrizione
	 *            The tribunaleIscrizione to set.
	 */
	public void setTribunaleIscrizione(String tribunaleIscrizione) {
		this.tribunaleIscrizione = tribunaleIscrizione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Azienda[");
		buffer.append(super.toString());
		buffer.append(" abilitata = ").append(abilitata);
		buffer.append(" CCIAAIscrizione = ").append(iscrizioneCciaa);
		buffer.append(" codice = ").append(codice);
		buffer.append(" codiceAttivitaPrevalente = ").append(codiceAttivitaPrevalente);
		buffer.append(" codiceFiscale = ").append(codiceFiscale);
		buffer.append(" codiceSIA = ").append(codiceSIA);
		buffer.append(" dataInizioAttivita = ").append(dataInizioAttivita);
		buffer.append(" dataInizioEsercizio = ").append(dataInizioEsercizio);
		buffer.append(" datiProtocollo = ").append(datiProtocollo);
		buffer.append(" denominazione = ").append(denominazione);
		buffer.append(" descrizioneAttivitaPrevalente = ").append(descrizioneAttivitaPrevalente);
		buffer.append(" formaGiuridica = ").append(formaGiuridica != null ? formaGiuridica.getId() : null);
		buffer.append(" legaleRappresentante = ").append(legaleRappresentante);
		buffer.append(" numeroIscrizioneCCIAA = ").append(numeroIscrizioneCCIAA);
		buffer.append(" numeroIscrizioneTribunale = ").append(numeroIscrizioneTribunale);
		buffer.append(" partitaIVA = ").append(partitaIVA);
		buffer.append(" personaFisica = ").append(personaFisica);
		buffer.append(" tribunaleIscrizione = ").append(tribunaleIscrizione);
		buffer.append(" lingua = ").append(lingua);
		buffer.append(" codiceValuta = ").append(codiceValuta);
		buffer.append("]");
		return buffer.toString();
	}

}
