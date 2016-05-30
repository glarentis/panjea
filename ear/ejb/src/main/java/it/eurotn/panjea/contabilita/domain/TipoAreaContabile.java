package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "cont_tipi_area_contabile")
@org.hibernate.annotations.Table(appliesTo = "cont_tipi_area_contabile", indexes = { @Index(name = "IdxTipoDoc", columnNames = { "tipoDocumento_id" }) })
@NamedQueries({
		@NamedQuery(name = "TipoAreaContabile.caricaByTipoDocumento", query = "select ac from TipoAreaContabile ac inner join ac.tipoDocumento td where td.id = :paramId ", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "tipoAreaContabile") }),
		@NamedQuery(name = "TipoAreaContabile.caricaTipiDocumentiContabili", query = " select ta.tipoDocumento from TipoAreaContabile ta where ta.tipoDocumento.codiceAzienda = :paramCodiceAzienda ", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "tipoAreaContabile") }),
		@NamedQuery(name = "TipoAreaContabile.caricaTipoDocumentoByTipoRegistro", query = "select distinct tac.tipoDocumento from TipoAreaContabile tac where tac.registroIva.tipoRegistro = :paramTipoRegistro and tac.tipoDocumento.codiceAzienda = :paramCodiceAzienda and tac.tipoDocumento.abilitato=true") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "tipoAreaContabile")
public class TipoAreaContabile extends EntityBase implements java.io.Serializable, Cloneable, ITipoAreaDocumento {

	/**
	 * enum per definire come viene eseguita la gestione iva per il tipo area contabile Normale INTRA (Acquisto intra
	 * CEE) ART17 (sub appalto).
	 *
	 * @author Leonardo
	 */
	public enum GestioneIva {
		NORMALE, INTRA, ART17
	}

	/**
	 * @author adriano
	 * @version 1.0, 21/mag/07
	 */
	public enum TipoCollegamento {
		NIENTE
	}

	/**
	 * Definisce la tipologia corrispettivo da usare solo nel caso in cui il registro iva � un
	 * TipoRegistro.CORRISPETTIVO.
	 */
	public enum TipologiaCorrispettivo {
		DA_VENTILARE, ALIQUOTA_NOTA, RICEVUTA_FISCALE, DA_FATTURA
	}

	public enum TipoRitenutaAcconto {
		ENASARCO(ETipoContoBase.QUOTA_ENASARCO), INPS(ETipoContoBase.QUOTA_INPS);

		private ETipoContoBase contoBasePrevidenziale;

		/**
		 * Costruttore.
		 *
		 * @param contoBasePrevidenziale
		 *            conto base
		 */
		private TipoRitenutaAcconto(final ETipoContoBase contoBasePrevidenziale) {
			this.contoBasePrevidenziale = contoBasePrevidenziale;
		}

		/**
		 * @return the contoBasePrevidenziale
		 */
		public ETipoContoBase getContoBasePrevidenziale() {
			return contoBasePrevidenziale;
		}
	}

	public enum TipoTotalizzazione {
		NONTRATTATO, SOMMA, SOTTRAI
	}

	private static final long serialVersionUID = 8851738048044850315L;
	// Determina se l'area iva per questo tipo area contabile e' prevista
	private boolean areaScadenzaPresente = false;
	// DATI AREA CONTABILE

	@OneToOne
	@Fetch(FetchMode.SELECT)
	private TipoDocumento tipoDocumento;

	private boolean dataDocLikeDataReg = false;

	private boolean protocolloLikeNumDoc = false;

	private boolean stampaGiornale = false;

	private boolean simulazione = false;

	private boolean centroDiCostoRichiesto = false;

	@ManyToOne
	private TipoAreaContabile tipoDocumentoCollegato;

	@Enumerated
	private TipoCollegamento tipoCollegamento;

	// DATI AREA IVA
	@Enumerated
	private GestioneIva gestioneIva;

	@ManyToOne
	@JoinColumn(name = "registro_iva_id")
	@Fetch(FetchMode.SELECT)
	private RegistroIva registroIva;

	@ManyToOne
	@JoinColumn(name = "registro_iva_collegato_id")
	private RegistroIva registroIvaCollegato;

	@Column(length = 5)
	private String registroProtocollo;

	@Column(length = 50)
	private String patternNumeroProtocollo;

	@Column(length = 5)
	private String registroProtocolloCollegato;

	private boolean stampaRegistroIva = false;

	@Enumerated
	private TipoTotalizzazione tipoTotalizzazione;

	@Enumerated
	private TipologiaCorrispettivo tipologiaCorrispettivo;

	private boolean validazioneAreaIvaAutomatica;

	/**
	 * lo stato iniziale dell'area contabile che dovrà assumere per i documenti generati da altre aree.
	 */
	@Column
	private StatoAreaContabile statoAreaContabileGenerata;

	/**
	 * definisce il conto cassa utilizzato per generazione dell'area contabile da area partita.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private SottoConto contoCassa;

	/**
	 * indicatore per il raggruppamento delle voci di conto cassa in un'unica scrittura.
	 */
	@Column
	private boolean raggruppamentoContoCassa;

	/**
	 * Indicatore per la gestione della ritenuta d'acconto.
	 */
	private boolean ritenutaAcconto;

	private TipoRitenutaAcconto tipoRitenutaAcconto;

	@ManyToOne
	private EntitaLite entitaPredefinita;

	/**
	 * Inizializza i valori di default.
	 */
	{
		this.gestioneIva = GestioneIva.NORMALE;
		this.tipoDocumento = new TipoDocumento();
		this.ritenutaAcconto = Boolean.FALSE;
		this.tipoRitenutaAcconto = null;
	}

	@Override
	public Object clone() {
		TipoAreaContabile tipoAreaContabile = PanjeaEJBUtil.cloneObject(this);
		tipoAreaContabile.setId(null);

		return tipoAreaContabile;
	}

	/**
	 * Azzera le impostazione per la parte iva.
	 */
	public void disabilitaAreaContabileIva() {
		setRegistroProtocollo(null);
		setStampaRegistroIva(false);
		setTipoTotalizzazione(null);
	}

	/**
	 * @return Returns the contoCassa.
	 * @uml.property name="contoCassa"
	 */
	public SottoConto getContoCassa() {
		return contoCassa;
	}

	/**
	 * HACK la descrizione del tipo area contabile punta alla descrizione del tipo documento,rendo questo campo solo in
	 * lettura in modo da mantenerlo accessibile a reports e/o qlq altra entita' che lo utilizza.
	 *
	 * @deprecated
	 * @return descrizione
	 */
	@Deprecated
	public String getDescrizioneAreaContabile() {
		return tipoDocumento != null ? tipoDocumento.getDescrizione() : "";
	}

	@Override
	public String getDescrizionePerStampa() {
		return "";
	}

	/**
	 *
	 * @return entita predefinita
	 */
	public EntitaLite getEntitaPredefinita() {
		return entitaPredefinita;
	}

	@Override
	public String getFormulaStandardNumeroCopie() {
		return "1";
	}

	/**
	 * @return gestioneIva
	 */
	public GestioneIva getGestioneIva() {
		return gestioneIva;
	}

	/**
	 * Recupera l'intero associato all'enum gestione iva, usato per passare al client via propertypath il valore della
	 * gestione iva dove nell'anagrafica non e' visibile la contabilita'.
	 *
	 * @return intero equivalente all'ordine nell'enum gestioneIva o se gestione iva e' null viene ritornato -1
	 */
	public int getGestioneIvaOrdinal() {
		return this.getGestioneIva() != null ? this.getGestioneIva().ordinal() : -1;
	}

	/**
	 * @return the patternNumeroProtocollo
	 */
	public String getPatternNumeroProtocollo() {
		return patternNumeroProtocollo;
	}

	/**
	 * @return registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @return registroIvaCollegato
	 */
	public RegistroIva getRegistroIvaCollegato() {
		return registroIvaCollegato;
	}

	/**
	 * @return registroProtocollo
	 */
	public String getRegistroProtocollo() {
		return registroProtocollo;
	}

	/**
	 * @return registroProtocolloCollegato
	 */
	public String getRegistroProtocolloCollegato() {
		return registroProtocolloCollegato;
	}

	/**
	 * Restituisce il codice del registro protocollo utilizzato. Se protocolloLikeNumDoc = true è il registro protocollo
	 * del tipo documento altrimenti quelli del tipo area contabile.
	 *
	 * @return registro protocollo
	 */
	public String getRegistroProtocolloUtilizzato() {

		if (protocolloLikeNumDoc) {
			return tipoDocumento.getRegistroProtocollo();
		} else {
			return registroProtocollo;
		}
	}

	@Override
	public String getReportPath() {
		return "";
	}

	/**
	 * @return stampaRegistroIva
	 */
	public boolean getStampaRegistroIva() {
		return stampaRegistroIva;
	}

	/**
	 * @return statoAreaContabileGenerata
	 */
	public StatoAreaContabile getStatoAreaContabileGenerata() {
		return statoAreaContabileGenerata;
	}

	/**
	 * @return tipoCollegamento
	 */
	public TipoCollegamento getTipoCollegamento() {
		return tipoCollegamento;
	}

	/**
	 * @return tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return tipoDocumentoCollegato
	 */
	public TipoAreaContabile getTipoDocumentoCollegato() {
		return tipoDocumentoCollegato;
	}

	/**
	 * @return tipologiaCorrispettivo
	 */
	public TipologiaCorrispettivo getTipologiaCorrispettivo() {
		return tipologiaCorrispettivo;
	}

	/**
	 * @return the tipoRitenutaAcconto
	 */
	public TipoRitenutaAcconto getTipoRitenutaAcconto() {
		return tipoRitenutaAcconto;
	}

	/**
	 * @return tipoTotalizzazione
	 */
	public TipoTotalizzazione getTipoTotalizzazione() {
		return tipoTotalizzazione;
	}

	/**
	 * @return areaScadenzaPresente
	 */
	public boolean isAreaScadenzaPresente() {
		return areaScadenzaPresente;
	}

	/**
	 * @return centroDiCostoRichiesto
	 */
	public boolean isCentroDiCostoRichiesto() {
		return centroDiCostoRichiesto;
	}

	/**
	 * @return dataDocLikeDataReg
	 */
	public boolean isDataDocLikeDataReg() {
		return dataDocLikeDataReg;
	}

	/**
	 * @return the protocolloLikeNumDoc
	 */
	public boolean isProtocolloLikeNumDoc() {
		return protocolloLikeNumDoc;
	}

	/**
	 * @return Returns the raggruppamentoContoCassa.
	 */
	public boolean isRaggruppamentoContoCassa() {
		return raggruppamentoContoCassa;
	}

	/**
	 * @return the ritenutaAcconto
	 */
	public boolean isRitenutaAcconto() {
		return ritenutaAcconto;
	}

	/**
	 * @return simulazione
	 */
	public boolean isSimulazione() {
		return simulazione;
	}

	/**
	 * @return stampaGiornale
	 */
	public boolean isStampaGiornale() {
		return stampaGiornale;
	}

	/**
	 * @return validazioneAreaIvaAutomatica
	 */
	public boolean isValidazioneAreaIvaAutomatica() {
		return validazioneAreaIvaAutomatica;
	}

	/**
	 * @param areaScadenzaPresente
	 *            the areaScadenzaPresente to set
	 */
	public void setAreaScadenzaPresente(boolean areaScadenzaPresente) {
		this.areaScadenzaPresente = areaScadenzaPresente;
	}

	/**
	 * @param centroDiCostoRichiesto
	 *            the centroDiCostoRichiesto to set
	 */
	public void setCentroDiCostoRichiesto(boolean centroDiCostoRichiesto) {
		this.centroDiCostoRichiesto = centroDiCostoRichiesto;
	}

	/**
	 * @param contoCassa
	 *            the contoCassa to set
	 */
	public void setContoCassa(SottoConto contoCassa) {
		this.contoCassa = contoCassa;
	}

	/**
	 * @param dataDocLikeDataReg
	 *            the dataDocLikeDataReg to set
	 */
	public void setDataDocLikeDataReg(boolean dataDocLikeDataReg) {
		this.dataDocLikeDataReg = dataDocLikeDataReg;
	}

	/**
	 *
	 * @param entitaPredefinita
	 *            entita predefinita
	 */
	public void setEntitaPredefinita(EntitaLite entitaPredefinita) {
		this.entitaPredefinita = entitaPredefinita;
	}

	/**
	 * @param gestioneIva
	 *            the gestioneIva to set
	 */
	public void setGestioneIva(GestioneIva gestioneIva) {
		this.gestioneIva = gestioneIva;
	}

	/**
	 * @param patternNumeroProtocollo
	 *            the patternNumeroProtocollo to set
	 */
	public void setPatternNumeroProtocollo(String patternNumeroProtocollo) {
		this.patternNumeroProtocollo = patternNumeroProtocollo;
	}

	/**
	 * @param protocolloLikeNumDoc
	 *            the protocolloLikeNumDoc to set
	 */
	public void setProtocolloLikeNumDoc(boolean protocolloLikeNumDoc) {
		this.protocolloLikeNumDoc = protocolloLikeNumDoc;
	}

	/**
	 * @param raggruppamentoContoCassa
	 *            the raggruppamentoContoCassa to set
	 */
	public void setRaggruppamentoContoCassa(boolean raggruppamentoContoCassa) {
		this.raggruppamentoContoCassa = raggruppamentoContoCassa;
	}

	/**
	 * @param registroIva
	 *            the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @param registroIvaCollegato
	 *            the registroIvaCollegato to set
	 */
	public void setRegistroIvaCollegato(RegistroIva registroIvaCollegato) {
		this.registroIvaCollegato = registroIvaCollegato;
	}

	/**
	 * @param registroProtocollo
	 *            the registroProtocollo to set
	 */
	public void setRegistroProtocollo(String registroProtocollo) {
		this.registroProtocollo = registroProtocollo;
	}

	/**
	 * @param registroProtocolloCollegato
	 *            the registroProtocolloCollegato to set
	 */
	public void setRegistroProtocolloCollegato(String registroProtocolloCollegato) {
		this.registroProtocolloCollegato = registroProtocolloCollegato;
	}

	/**
	 * @param ritenutaAcconto
	 *            the ritenutaAcconto to set
	 */
	public void setRitenutaAcconto(boolean ritenutaAcconto) {
		this.ritenutaAcconto = ritenutaAcconto;
	}

	/**
	 * @param simulazione
	 *            the simulazione to set
	 */
	public void setSimulazione(boolean simulazione) {
		this.simulazione = simulazione;
	}

	/**
	 * @param stampaGiornale
	 *            the stampaGiornale to set
	 */
	public void setStampaGiornale(boolean stampaGiornale) {
		this.stampaGiornale = stampaGiornale;
	}

	/**
	 * @param stampaRegistroIva
	 *            the stampaRegistroIva to set
	 */
	public void setStampaRegistroIva(boolean stampaRegistroIva) {
		this.stampaRegistroIva = stampaRegistroIva;
	}

	/**
	 * @param statoAreaContabileGenerata
	 *            the statoAreaContabileGenerata to set
	 */
	public void setStatoAreaContabileGenerata(StatoAreaContabile statoAreaContabileGenerata) {
		this.statoAreaContabileGenerata = statoAreaContabileGenerata;
	}

	/**
	 * @param tipoCollegamento
	 *            the tipoCollegamento to set
	 */
	public void setTipoCollegamento(TipoCollegamento tipoCollegamento) {
		this.tipoCollegamento = tipoCollegamento;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
		if (!tipoDocumento.isRigheIvaEnable()) {
			disabilitaAreaContabileIva();
		}
	}

	/**
	 * @param tipoDocumentoCollegato
	 *            the tipoDocumentoCollegato to set
	 */
	public void setTipoDocumentoCollegato(TipoAreaContabile tipoDocumentoCollegato) {
		this.tipoDocumentoCollegato = tipoDocumentoCollegato;
	}

	/**
	 * @param tipologiaCorrispettivo
	 *            the tipologiaCorrispettivo to set
	 */
	public void setTipologiaCorrispettivo(TipologiaCorrispettivo tipologiaCorrispettivo) {
		this.tipologiaCorrispettivo = tipologiaCorrispettivo;
	}

	/**
	 * @param tipoRitenutaAcconto
	 *            the tipoRitenutaAcconto to set
	 */
	public void setTipoRitenutaAcconto(TipoRitenutaAcconto tipoRitenutaAcconto) {
		this.tipoRitenutaAcconto = tipoRitenutaAcconto;
	}

	/**
	 * @param tipoTotalizzazione
	 *            the tipoTotalizzazione to set
	 */
	public void setTipoTotalizzazione(TipoTotalizzazione tipoTotalizzazione) {
		this.tipoTotalizzazione = tipoTotalizzazione;
	}

	/**
	 * @param validazioneAreaIvaAutomatica
	 *            the validazioneAreaIvaAutomatica to set
	 */
	public void setValidazioneAreaIvaAutomatica(boolean validazioneAreaIvaAutomatica) {
		this.validazioneAreaIvaAutomatica = validazioneAreaIvaAutomatica;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TipoAreaContabile[");
		buffer.append("areaScadenzaPresente = ").append(areaScadenzaPresente);
		buffer.append(" centroDiCostoRichiesto = ").append(centroDiCostoRichiesto);
		buffer.append(" descrizioneAreaContabile = ").append(getDescrizioneAreaContabile());
		buffer.append(" gestioneIva = ").append(gestioneIva);
		buffer.append(" registroIva = ").append(registroIva);
		buffer.append(" registroProtocollo = ").append(registroProtocollo);
		buffer.append(" simulazione = ").append(simulazione);
		buffer.append(" stampaGiornale = ").append(stampaGiornale);
		buffer.append(" stampaRegistroIva = ").append(stampaRegistroIva);
		buffer.append(" tipoCollegamento = ").append(tipoCollegamento);
		buffer.append(" tipoDocumento = ").append(tipoDocumento);
		buffer.append(" tipoDocumentoCollegato = ").append(tipoDocumentoCollegato);
		buffer.append(" tipoTotalizzazione = ").append(tipoTotalizzazione);
		buffer.append("]");
		return buffer.toString();
	}
}
