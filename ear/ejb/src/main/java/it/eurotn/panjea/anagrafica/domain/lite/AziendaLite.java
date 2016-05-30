/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain.lite;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.conai.domain.ConaiTipoIscrizione;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * @author Leonardo Recupera i dati dalla stessa tabella dell'Azienda ancora in ejb2
 */
@Entity
@Table(name = "anag_aziende")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@NamedQueries({
		@NamedQuery(name = "AziendaLite.caricaAll", query = " from AziendaLite a "),
		@NamedQuery(name = "AziendaLite.caricaByCodice", query = " from AziendaLite a where a.codice = :paramCodiceAzienda ", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "azienda") }) })
public class AziendaLite extends EntityBase {

	private static final long serialVersionUID = 3394864741582839043L;

	@Column(length = 10, nullable = false)
	private String codice;

	@Column(length = 60, nullable = false)
	private String denominazione;

	@Temporal(TemporalType.DATE)
	private Date dataInizioEsercizio;

	@Temporal(TemporalType.DATE)
	private Date dataInizioAttivita;

	@Column(length = 25, nullable = false)
	private String partitaIVA;

	@Column(length = 25, nullable = false)
	private String codiceFiscale;

	@Transient
	private String indirizzo;

	@Transient
	private Nazione nazione;

	@Transient
	private Cap cap;

	@Transient
	private Localita localita;

	@Transient
	private LivelloAmministrativo1 livelloAmministrativo1;
	@Transient
	private LivelloAmministrativo2 livelloAmministrativo2;
	@Transient
	private LivelloAmministrativo3 livelloAmministrativo3;
	@Transient
	private LivelloAmministrativo4 livelloAmministrativo4;

	@Column(length = 5)
	private String codiceSIA;

	@Column(length = 3)
	private String lingua;

	@Column(length = 3)
	private String codiceValuta;

	@Column(length = 100)
	private String pec;

	@Column(length = 20)
	private String codiceSocioConai;

	@Enumerated
	private ConaiTipoIscrizione conaiTipoIscrizione;

	/**
	 * @return the cap
	 */
	public Cap getCap() {
		return cap;
	}

	/**
	 * @return the codice
	 */
	public java.lang.String getCodice() {
		return codice;
	}

	/**
	 * @return codiceFiscale
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * @return codiceSIA
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
	 * Restuisce la data di fine esercizio per l'anno desiderato basandosi sul mese e giorno di fine esercizio impostati
	 * nell'azienda.
	 * 
	 * @param annoEsercizio
	 *            anno di esercizio da utilizzare nella data
	 * @return data di fine esercizio dell'azienda per l'anno richiesto
	 */
	public Date getDataFineEsercizio(Integer annoEsercizio) {
		Calendar calendarDataFineEsercizioAzienda = Calendar.getInstance();
		calendarDataFineEsercizioAzienda.setTime(getDataInizioEsercizio(annoEsercizio));
		// la data di fine è +1 anno -1 giorno
		calendarDataFineEsercizioAzienda.add(Calendar.YEAR, 1);
		calendarDataFineEsercizioAzienda.add(Calendar.DAY_OF_MONTH, -1);
		return calendarDataFineEsercizioAzienda.getTime();
	}

	/**
	 * @return Returns the dataInizioAttivita.
	 */
	public Date getDataInizioAttivita() {
		return dataInizioAttivita;
	}

	/**
	 * @return the dataInizioEsercizio
	 */
	public Date getDataInizioEsercizio() {
		return dataInizioEsercizio;
	}

	/**
	 * Restuisce la data di inizio esercizio per l'anno desiderato basandosi sul mese e giorno di inizio esercizio
	 * impostati nell'azienda.
	 * 
	 * @param annoEsercizio
	 *            anno di esercizio da utilizzare nella data
	 * @return data di inizio esercizio dell'azienda per l'anno richiesto
	 */
	public Date getDataInizioEsercizio(Integer annoEsercizio) {
		Calendar calendarDataInizioEsercizioAzienda = Calendar.getInstance();
		calendarDataInizioEsercizioAzienda.set(Calendar.DAY_OF_MONTH, getGiornoInizioEsercizio());
		calendarDataInizioEsercizioAzienda.set(Calendar.MONTH, getMeseInizioEsercizio());
		calendarDataInizioEsercizioAzienda.set(Calendar.YEAR, annoEsercizio);
		calendarDataInizioEsercizioAzienda.set(Calendar.HOUR_OF_DAY, 0);
		calendarDataInizioEsercizioAzienda.set(Calendar.MINUTE, 0);
		calendarDataInizioEsercizioAzienda.set(Calendar.SECOND, 0);
		calendarDataInizioEsercizioAzienda.set(Calendar.MILLISECOND, 0);
		return calendarDataInizioEsercizioAzienda.getTime();
	}

	/**
	 * @return the denominazione
	 */
	public java.lang.String getDenominazione() {
		return denominazione;
	}

	/**
	 * @return giorno inizio esercizio
	 */
	public int getGiornoInizioEsercizio() {
		Calendar calendar = Calendar.getInstance();
		if (dataInizioEsercizio != null) {
			calendar.setTime(dataInizioEsercizio);
		}
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @return indirizzo
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * @return lingua
	 */
	public String getLingua() {
		return lingua;
	}

	/**
	 * @return the livelloAmministrativo1
	 */
	public LivelloAmministrativo1 getLivelloAmministrativo1() {
		return livelloAmministrativo1;
	}

	/**
	 * @return the livelloAmministrativo2
	 */
	public LivelloAmministrativo2 getLivelloAmministrativo2() {
		return livelloAmministrativo2;
	}

	/**
	 * @return the livelloAmministrativo3
	 */
	public LivelloAmministrativo3 getLivelloAmministrativo3() {
		return livelloAmministrativo3;
	}

	/**
	 * @return the livelloAmministrativo4
	 */
	public LivelloAmministrativo4 getLivelloAmministrativo4() {
		return livelloAmministrativo4;
	}

	/**
	 * @return the localita
	 */
	public Localita getLocalita() {
		return localita;
	}

	/**
	 * @return mese inizio esercizio
	 */
	public int getMeseInizioEsercizio() {
		Calendar calendar = Calendar.getInstance();
		if (dataInizioEsercizio != null) {
			calendar.setTime(dataInizioEsercizio);
		}
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * @return the nazione
	 */
	public Nazione getNazione() {
		return nazione;
	}

	/**
	 * @return partitaIVA
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
	 * @param cap
	 *            the cap to set
	 */
	public void setCap(Cap cap) {
		this.cap = cap;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(java.lang.String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceFiscale
	 *            the codiceFiscale to set
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @param codiceSIA
	 *            the codiceSIA to set
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
	 *            the dataInizioEsercizio to set
	 */
	public void setDataInizioEsercizio(Date dataInizioEsercizio) {
		this.dataInizioEsercizio = dataInizioEsercizio;
	}

	/**
	 * @param denominazione
	 *            the denominazione to set
	 */
	public void setDenominazione(java.lang.String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @param indirizzo
	 *            the indirizzo to set
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * @param lingua
	 *            the lingua to set
	 */
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	/**
	 * @param livelloAmministrativo1
	 *            the livelloAmministrativo1 to set
	 */
	public void setLivelloAmministrativo1(LivelloAmministrativo1 livelloAmministrativo1) {
		this.livelloAmministrativo1 = livelloAmministrativo1;
	}

	/**
	 * @param livelloAmministrativo2
	 *            the livelloAmministrativo2 to set
	 */
	public void setLivelloAmministrativo2(LivelloAmministrativo2 livelloAmministrativo2) {
		this.livelloAmministrativo2 = livelloAmministrativo2;
	}

	/**
	 * @param livelloAmministrativo3
	 *            the livelloAmministrativo3 to set
	 */
	public void setLivelloAmministrativo3(LivelloAmministrativo3 livelloAmministrativo3) {
		this.livelloAmministrativo3 = livelloAmministrativo3;
	}

	/**
	 * @param livelloAmministrativo4
	 *            the livelloAmministrativo4 to set
	 */
	public void setLivelloAmministrativo4(LivelloAmministrativo4 livelloAmministrativo4) {
		this.livelloAmministrativo4 = livelloAmministrativo4;
	}

	/**
	 * @param localita
	 *            the localita to set
	 */
	public void setLocalita(Localita localita) {
		this.localita = localita;
	}

	/**
	 * @param nazione
	 *            the nazione to set
	 */
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}

	/**
	 * @param partitaIVA
	 *            the partitaIVA to set
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

	@Override
	public String toString() {
		return "AziendaLite [" + (cap != null ? "cap=" + cap.getId() + ", " : "")
				+ (codice != null ? "codice=" + codice + ", " : "")
				+ (codiceFiscale != null ? "codiceFiscale=" + codiceFiscale + ", " : "")
				+ (codiceSIA != null ? "codiceSIA=" + codiceSIA + ", " : "")
				+ (codiceValuta != null ? "codiceValuta=" + codiceValuta + ", " : "")
				+ (dataInizioEsercizio != null ? "dataInizioEsercizio=" + dataInizioEsercizio + ", " : "")
				+ (denominazione != null ? "denominazione=" + denominazione + ", " : "")
				+ (indirizzo != null ? "indirizzo=" + indirizzo + ", " : "")
				+ (lingua != null ? "lingua=" + lingua + ", " : "")
				+ (partitaIVA != null ? "partitaIVA=" + partitaIVA + ", " : "");
	}

}
