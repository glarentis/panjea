/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;

/**
 * @author Leonardo
 *
 */
@Entity
@Table(name = "bamm_bene_ammortizzabile")
@NamedQueries({
	@NamedQuery(name = "BeneAmmortizzabile.caricaAll", query = " from BeneAmmortizzabile b where b.codiceAzienda = :paramCodiceAzienda "),
	@NamedQuery(name = "BeneAmmortizzabile.caricaBeniFigli", query = " from BeneAmmortizzabile b where b.benePadre.id = :paramIdBenePadre and b.codiceAzienda = :paramCodiceAzienda "),
	@NamedQuery(name = "BeneAmmortizzabile.caricaBeniDaAmmortizzare", query = " from BeneAmmortizzabile b where (b.datiCivilistici.ammortamentoInCorso = true or b.datiFiscali.ammortamentoInCorso = true) and b.beneDiProprieta = true and b.indAmmortamento = true and b.benePadre is null and b.codiceAzienda = :paramCodiceAzienda "),
	@NamedQuery(name = "BeneAmmortizzabile.caricaBeniDaAmmortizzareLite", query = " select new BeneAmmortizzabileLite(( select (SUM(ba.importoSoggettoAdAmmortamentoSingolo)) from BeneAmmortizzabile ba where (ba.datiCivilistici.ammortamentoInCorso = true or ba.datiFiscali.ammortamentoInCorso = true) and ba.beneDiProprieta = true and ba.indAmmortamento = true and (ba.id = b.id or ba.benePadre.id = b.id) and ba.dataInizioAmmortamento <=:paramData),	 "
			+ "( select (SUM(va.importoValutazioneBene)) from ValutazioneBene va inner join va.bene ba where (ba.datiCivilistici.ammortamentoInCorso = true or ba.datiFiscali.ammortamentoInCorso = true) and ba.beneDiProprieta = true and ba.indAmmortamento = true and (ba.id = b.id or ba.benePadre.id = b.id) and va.anno <= :paramAnno and ba.dataInizioAmmortamento <=:paramData), "
			+ "( select (SUM(va.importoValutazioneFondo)) from ValutazioneBene va inner join va.bene ba where (ba.datiCivilistici.ammortamentoInCorso = true or ba.datiFiscali.ammortamentoInCorso = true) and ba.beneDiProprieta = true and ba.indAmmortamento = true and (ba.id = b.id or ba.benePadre.id = b.id) and va.anno <= :paramAnno and ba.dataInizioAmmortamento <=:paramData), "
			+ "( select (SUM(vb.importoStornoValoreBene)) from VenditaBene vb inner join vb.bene ba where (ba.datiCivilistici.ammortamentoInCorso = true or ba.datiFiscali.ammortamentoInCorso = true) and ba.beneDiProprieta = true and ba.indAmmortamento = true and (ba.id = b.id or ba.benePadre.id = b.id) and vb.dataVendita <= :paramData and  ba.dataInizioAmmortamento <=:paramData),"
			+ "( select (SUM(vb.importoStornoFondoAmmortamento)) from VenditaBene vb inner join vb.bene ba where (ba.datiCivilistici.ammortamentoInCorso = true or ba.datiFiscali.ammortamentoInCorso = true) and ba.beneDiProprieta = true and ba.indAmmortamento = true and (ba.id = b.id or ba.benePadre.id = b.id) and vb.dataVendita <= :paramData and  ba.dataInizioAmmortamento <=:paramData),"
			+ "b.acquistatoUsato,b.annoAcquisto,b.beneDiProprieta,b.beneInLeasing,b.beneMateriale,b.codice,b.codiceAzienda,b.codiceBeneEsterno,b.dataInizioAmmortamento,b.datiCivilistici,b.datiFiscali,b.descrizione,b.fabbricato,b.id, "
			+ " b.importoFatturaAcquisto,b.importoSoggettoAdAmmortamentoSingolo,b.indAmmortamento,b.indTestoUnico,b.manutenzione,b.numeroProtocolloAcquisto,b.numeroRegistro,b.percentualeContributo,b.percentualeUsoPromiscuo,b.soggettoAContributo,b.sottoSpecie,b.stampaSuRegistriBeni, "
			+ " b.stampaSuRegistriInventari,b.eliminato,b.ubicazione,b.valoreAcquisto,b.version) from BeneAmmortizzabile b left join b.ubicazione "
			+ " where (b.datiCivilistici.ammortamentoInCorso = true or b.datiFiscali.ammortamentoInCorso = true) "
			+ " and b.beneDiProprieta = true and b.indAmmortamento = true and b.benePadre is null and b.codiceAzienda = :paramCodiceAzienda and b.dataInizioAmmortamento <= :paramData order by b.sottoSpecie.specie.id,b.sottoSpecie.id "),
			@NamedQuery(name = "BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamento", query = " select new it.eurotn.panjea.beniammortizzabili.util.BeneImportoSoggettoAdAmmortamento(b.id, b.importoSoggettoAdAmmortamentoSingolo ) from BeneAmmortizzabile b "
					+ " where b.codiceAzienda = :paramCodiceAzienda and b.annoAcquisto <= :paramAnnoAcquisto and (b.datiCivilistici.ammortamentoInCorso = true or b.datiFiscali.ammortamentoInCorso = true) and "
					+ " b.beneDiProprieta = true and b.indAmmortamento = true and b.benePadre is null "),
					@NamedQuery(name = "BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamentoPerBene", query = " select new it.eurotn.panjea.beniammortizzabili.util.BeneImportoSoggettoAdAmmortamento(b.id, b.importoSoggettoAdAmmortamentoSingolo ) from BeneAmmortizzabile b "
							+ " where b.id = :paramIdBeneAmmortizzabile and b.annoAcquisto <= :paramAnnoAcquisto and (b.datiCivilistici.ammortamentoInCorso = true or b.datiFiscali.ammortamentoInCorso = true) and "
							+ " b.beneDiProprieta = true and b.indAmmortamento = true and b.benePadre is null "),
							@NamedQuery(name = "BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamentoFigli", query = " select new it.eurotn.panjea.beniammortizzabili.util.BeneImportoSoggettoAdAmmortamento(bp.id, sum(b.importoSoggettoAdAmmortamentoSingolo) ) from BeneAmmortizzabile b join b.benePadre bp "
									+ " where bp.codiceAzienda = :paramCodiceAzienda and b.annoAcquisto <= :paramAnnoAcquisto and (bp.datiCivilistici.ammortamentoInCorso = true or bp.datiFiscali.ammortamentoInCorso = true) and "
									+ " bp.beneDiProprieta = true and bp.indAmmortamento = true " + " group by bp.id "),
									@NamedQuery(name = "BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamentoFigliPerBene", query = " select new it.eurotn.panjea.beniammortizzabili.util.BeneImportoSoggettoAdAmmortamento(bp.id, sum(b.importoSoggettoAdAmmortamentoSingolo) ) from BeneAmmortizzabile b join b.benePadre bp "
											+ " where bp.id = :paramIdBeneAmmortizzabile and b.annoAcquisto <= :paramAnnoAcquisto and (bp.datiCivilistici.ammortamentoInCorso = true or bp.datiFiscali.ammortamentoInCorso = true) and "
											+ " bp.beneDiProprieta = true and bp.indAmmortamento = true " + " group by bp.id ") })
public class BeneAmmortizzabile extends EntityBase {

	private static final long serialVersionUID = -509830178866560712L;

	public static final String REF = "BeneAmmortizzabile";
	public static final String PROP_FABBRICATO = "fabbricato";
	public static final String PROP_BENE_PADRE = "benePadre";
	public static final String PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO_SINGOLO = "importoSoggettoAdAmmortamentoSingolo";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_BENE_MATERIALE = "beneMateriale";
	public static final String PROP_DATA_INIZIO_AMMORTAMENTO = "dataInizioAmmortamento";
	public static final String PROP_NUMERO_REGISTRO = "numeroRegistro";
	public static final String PROP_SOGGETTO_A_CONTRIBUTO = "soggettoAContributo";
	public static final String PROP_PERCENTUALE_CONTRIBUTO = "percentualeContributo";
	public static final String PROP_STAMPA_SU_REGISTRI_INVENTARI = "stampaSuRegistriInventari";
	public static final String PROP_PERCENTUALE_USO_PROMISCUO = "percentualeUsoPromiscuo";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO_ACCELERATO = "percentualeAmmortamentoAccelerato";
	public static final String PROP_IMPORTO_FATTURA_ACQUISTO = "importoFatturaAcquisto";
	public static final String PROP_ACQUISTATO_USATO = "acquistatoUsato";
	public static final String PROP_UBICAZIONE = "ubicazione";
	public static final String PROP_BENE_IN_LEASING = "beneInLeasing";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_NUMERO_PROTOCOLLO_ACQUISTO = "numeroProtocolloAcquisto";
	public static final String PROP_FORNITORE = "fornitore";
	public static final String PROP_CODICE_AZIENDA = "codiceAzienda";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_BENE_DI_PROPRIETA = "beneDiProprieta";
	public static final String PROP_SOTTO_SPECIE = "sottoSpecie";
	public static final String PROP_VALORE_ACQUISTO = "valoreAcquisto";
	public static final String PROP_MANUTENZIONE = "manutenzione";
	public static final String PROP_STAMPA_SU_REGISTRI_BENI = "stampaSuRegistriBeni";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO_ANTICIPATO = "percentualeAmmortamentoAnticipato";
	public static final String PROP_ANNO_ACQUISTO = "annoAcquisto";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO_ORDINARIO = "percentualeAmmortamentoOrdinario";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO = "importoSoggettoAdAmmortamento";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_NOTE = "note";
	public static final String PROP_NOTE_INTERNE = "noteInterne";
	public static final String PROP_IND_AMMORTAMENTO = "indAmmortamento";
	public static final String PROP_IND_TESTO_UNICO = "indTestoUnico";
	public static final String PROP_CODICE_BENE_ESTERNO = "codiceBeneEsterno";
	public static final String PROP_MATRICOLA_AZIENDALE = "matricolaAziendale";
	public static final String PROP_MATRICOLA_FORNITORE = "matricolaFornitore";
	public static final String PROP_ELIMINATO = "eliminato";

	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda = null;
	private Integer codice;
	private String descrizione = null;
	@Column(length = 10)
	private String numeroProtocolloAcquisto;
	@Column(length = 600)
	private String note;
	@Column(length = 300)
	private String noteInterne;
	@Column(length = 10)
	private String codiceBeneEsterno;

	private boolean eliminato = false;
	private boolean acquistatoUsato = false;

	private boolean beneMateriale = false;

	private boolean fabbricato = false;
	private boolean manutenzione = false;
	private boolean stampaSuRegistriBeni = false;
	private boolean stampaSuRegistriInventari = false;
	private boolean soggettoAContributo = false;
	private boolean beneDiProprieta = false;
	private boolean beneInLeasing = false;
	private boolean indAmmortamento = false;
	private boolean indTestoUnico = false;
	private Integer annoAcquisto;
	private Integer numeroRegistro;

	private BigDecimal valoreAcquisto;
	private BigDecimal importoFatturaAcquisto;

	@Column(precision = 19, scale = 6)
	private BigDecimal importoSoggettoAdAmmortamentoSingolo;
	private BigDecimal percentualeUsoPromiscuo;
	private BigDecimal percentualeContributo;
	@Temporal(TemporalType.DATE)
	private Date dataInizioAmmortamento;
	@Embedded
	private DatiCivilistici datiCivilistici;

	@Embedded
	private DatiFiscali datiFiscali;

	@ManyToOne
	@Index(name = "index_benePadre")
	private BeneAmmortizzabileLite benePadre;
	@ManyToOne
	private SottoSpecie sottoSpecie;

	@ManyToOne
	private Ubicazione ubicazione;
	@ManyToOne
	private EntitaLite fornitore;
	@Formula("( select SUM(ba.importoSoggettoAdAmmortamentoSingolo) from bamm_bene_ammortizzabile ba where ba.id = id or ba.benePadre_id = id )")
	private BigDecimal importoSoggettoAdAmmortamento;

	@Column(length = 15)
	private String matricolaAziendale;

	@Column(length = 15)
	private String matricolaFornitore;

	@Embedded
	private SottocontiBeni sottocontiBeni;

	{
		valoreAcquisto = BigDecimal.ZERO;
		importoFatturaAcquisto = BigDecimal.ZERO;
		importoSoggettoAdAmmortamento = BigDecimal.ZERO;
		percentualeUsoPromiscuo = BigDecimal.ZERO;
		percentualeContributo = BigDecimal.ZERO;
		datiCivilistici = new DatiCivilistici();
		datiFiscali = new DatiFiscali();

		indAmmortamento = true;
		beneMateriale = true;
		stampaSuRegistriBeni = true;
		stampaSuRegistriInventari = true;
		beneDiProprieta = true;
		indTestoUnico = true;

		sottocontiBeni = new SottocontiBeni();
	}

	/**
	 * Costruttore di default.
	 */
	public BeneAmmortizzabile() {
	}

	/**
	 * @return the annoAcquisto
	 */
	public Integer getAnnoAcquisto() {
		return annoAcquisto;
	}

	/**
	 * @return the benePadre
	 */
	public BeneAmmortizzabileLite getBenePadre() {
		return benePadre;
	}

	/**
	 * @return the codice
	 */
	public Integer getCodice() {
		return codice;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the codiceBeneEsterno
	 */
	public String getCodiceBeneEsterno() {
		return codiceBeneEsterno;
	}

	/**
	 * @return the dataInizioAmmortamento
	 */
	public Date getDataInizioAmmortamento() {
		return dataInizioAmmortamento;
	}

	/**
	 * @return the datiCivilistici
	 */
	public DatiCivilistici getDatiCivilistici() {
		return datiCivilistici;
	}

	/**
	 * @return the datiFiscali
	 */
	public DatiFiscali getDatiFiscali() {
		return datiFiscali;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the fornitore
	 */
	public EntitaLite getFornitore() {
		return fornitore;
	}

	/**
	 * @return the importoFatturaAcquisto
	 */
	public BigDecimal getImportoFatturaAcquisto() {
		return importoFatturaAcquisto;
	}

	/**
	 * @return the importoSoggettoAdAmmortamento
	 */
	public BigDecimal getImportoSoggettoAdAmmortamento() {
		return importoSoggettoAdAmmortamento;
	}

	/**
	 * @return the importoSoggettoAdAmmortamentoSingolo
	 */
	public BigDecimal getImportoSoggettoAdAmmortamentoSingolo() {
		return importoSoggettoAdAmmortamentoSingolo;
	}

	/**
	 * @return the matricolaAziendale
	 */
	public String getMatricolaAziendale() {
		return matricolaAziendale;
	}

	/**
	 * @return the matricolaFornitore
	 */
	public String getMatricolaFornitore() {
		return matricolaFornitore;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the noteInterne
	 */
	public String getNoteInterne() {
		return noteInterne;
	}

	/**
	 * @return the numeroProtocolloAcquisto
	 */
	public String getNumeroProtocolloAcquisto() {
		return numeroProtocolloAcquisto;
	}

	/**
	 * @return the numeroRegistro
	 */
	public Integer getNumeroRegistro() {
		return numeroRegistro;
	}

	/**
	 * @return the percentualeContributo
	 */
	public BigDecimal getPercentualeContributo() {
		return percentualeContributo;
	}

	/**
	 * @return the percentualeUsoPromiscuo
	 */
	public BigDecimal getPercentualeUsoPromiscuo() {
		return percentualeUsoPromiscuo;
	}

	/**
	 * @return the sottocontiBeni
	 */
	public SottocontiBeni getSottocontiBeni() {
		if (sottocontiBeni == null) {
			sottocontiBeni = new SottocontiBeni();
		}
		return sottocontiBeni;
	}

	/**
	 * @return the sottoSpecie
	 */
	public SottoSpecie getSottoSpecie() {
		return sottoSpecie;
	}

	/**
	 * @return the ubicazione
	 */
	public Ubicazione getUbicazione() {
		return ubicazione;
	}

	/**
	 * @return the valoreAcquisto
	 */
	public BigDecimal getValoreAcquisto() {
		return valoreAcquisto;
	}

	/**
	 * @return the acquistatoUsato
	 */
	public boolean isAcquistatoUsato() {
		return acquistatoUsato;
	}

	/**
	 * @return the beneDiProprieta
	 */
	public boolean isBeneDiProprieta() {
		return beneDiProprieta;
	}

	/**
	 * @return the beneInLeasing
	 */
	public boolean isBeneInLeasing() {
		return beneInLeasing;
	}

	/**
	 * @return the beneMateriale
	 */
	public boolean isBeneMateriale() {
		return beneMateriale;
	}

	/**
	 * @return the eliminato
	 */
	public boolean isEliminato() {
		return eliminato;
	}

	/**
	 * @return the fabbricato
	 */
	public boolean isFabbricato() {
		return fabbricato;
	}

	/**
	 * @return the indAmmortamento
	 */
	public boolean isIndAmmortamento() {
		return indAmmortamento;
	}

	/**
	 * @return the indTestoUnico
	 */
	public boolean isIndTestoUnico() {
		return indTestoUnico;
	}

	/**
	 * @return the manutenzione
	 */
	public boolean isManutenzione() {
		return manutenzione;
	}

	/**
	 * @return the soggettoAContributo
	 */
	public boolean isSoggettoAContributo() {
		return soggettoAContributo;
	}

	/**
	 * @return the stampaSuRegistriBeni
	 */
	public boolean isStampaSuRegistriBeni() {
		return stampaSuRegistriBeni;
	}

	/**
	 * @return the stampaSuRegistriInventari
	 */
	public boolean isStampaSuRegistriInventari() {
		return stampaSuRegistriInventari;
	}

	/**
	 * @param acquistatoUsato
	 *            the acquistatoUsato to set
	 */
	public void setAcquistatoUsato(boolean acquistatoUsato) {
		this.acquistatoUsato = acquistatoUsato;
	}

	/**
	 * @param annoAcquisto
	 *            the annoAcquisto to set
	 */
	public void setAnnoAcquisto(Integer annoAcquisto) {
		this.annoAcquisto = annoAcquisto;
	}

	/**
	 * @param beneDiProprieta
	 *            the beneDiProprieta to set
	 */
	public void setBeneDiProprieta(boolean beneDiProprieta) {
		this.beneDiProprieta = beneDiProprieta;
	}

	/**
	 * @param beneInLeasing
	 *            the beneInLeasing to set
	 */
	public void setBeneInLeasing(boolean beneInLeasing) {
		this.beneInLeasing = beneInLeasing;
	}

	/**
	 * @param beneMateriale
	 *            the beneMateriale to set
	 */
	public void setBeneMateriale(boolean beneMateriale) {
		this.beneMateriale = beneMateriale;
	}

	/**
	 * @param benePadre
	 *            the benePadre to set
	 */
	public void setBenePadre(BeneAmmortizzabileLite benePadre) {
		this.benePadre = benePadre;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(Integer codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codiceBeneEsterno
	 *            the codiceBeneEsterno to set
	 */
	public void setCodiceBeneEsterno(String codiceBeneEsterno) {
		this.codiceBeneEsterno = codiceBeneEsterno;
	}

	/**
	 * @param dataInizioAmmortamento
	 *            the dataInizioAmmortamento to set
	 */
	public void setDataInizioAmmortamento(Date dataInizioAmmortamento) {
		this.dataInizioAmmortamento = dataInizioAmmortamento;
	}

	/**
	 * @param datiCivilistici
	 *            the datiCivilistici to set
	 */
	public void setDatiCivilistici(DatiCivilistici datiCivilistici) {
		this.datiCivilistici = datiCivilistici;
	}

	/**
	 * @param datiFiscali
	 *            the datiFiscali to set
	 */
	public void setDatiFiscali(DatiFiscali datiFiscali) {
		this.datiFiscali = datiFiscali;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param eliminato
	 *            the eliminato to set
	 */
	public void setEliminato(boolean eliminato) {
		this.eliminato = eliminato;
	}

	/**
	 * @param fabbricato
	 *            the fabbricato to set
	 */
	public void setFabbricato(boolean fabbricato) {
		this.fabbricato = fabbricato;
	}

	/**
	 * @param fornitore
	 *            the fornitore to set
	 */
	public void setFornitore(EntitaLite fornitore) {
		this.fornitore = fornitore;
	}

	/**
	 * @param importoFatturaAcquisto
	 *            the importoFatturaAcquisto to set
	 */
	public void setImportoFatturaAcquisto(BigDecimal importoFatturaAcquisto) {
		this.importoFatturaAcquisto = importoFatturaAcquisto;
	}

	/**
	 * @param importoSoggettoAdAmmortamentoSingolo
	 *            the importoSoggettoAdAmmortamentoSingolo to set
	 */
	public void setImportoSoggettoAdAmmortamentoSingolo(BigDecimal importoSoggettoAdAmmortamentoSingolo) {
		this.importoSoggettoAdAmmortamentoSingolo = importoSoggettoAdAmmortamentoSingolo;
	}

	/**
	 * @param indAmmortamento
	 *            the indAmmortamento to set
	 */
	public void setIndAmmortamento(boolean indAmmortamento) {
		this.indAmmortamento = indAmmortamento;
	}

	/**
	 * @param indTestoUnico
	 *            the indTestoUnico to set
	 */
	public void setIndTestoUnico(boolean indTestoUnico) {
		this.indTestoUnico = indTestoUnico;
	}

	/**
	 * @param manutenzione
	 *            the manutenzione to set
	 */
	public void setManutenzione(boolean manutenzione) {
		this.manutenzione = manutenzione;
	}

	/**
	 * @param matricolaAziendale
	 *            the matricolaAziendale to set
	 */
	public void setMatricolaAziendale(String matricolaAziendale) {
		this.matricolaAziendale = matricolaAziendale;
	}

	/**
	 * @param matricolaFornitore
	 *            the matricolaFornitore to set
	 */
	public void setMatricolaFornitore(String matricolaFornitore) {
		this.matricolaFornitore = matricolaFornitore;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param noteInterne
	 *            the noteInterne to set
	 */
	public void setNoteInterne(String noteInterne) {
		this.noteInterne = noteInterne;
	}

	/**
	 * @param numeroProtocolloAcquisto
	 *            the numeroProtocolloAcquisto to set
	 */
	public void setNumeroProtocolloAcquisto(String numeroProtocolloAcquisto) {
		this.numeroProtocolloAcquisto = numeroProtocolloAcquisto;
	}

	/**
	 * @param numeroRegistro
	 *            the numeroRegistro to set
	 */
	public void setNumeroRegistro(Integer numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	/**
	 * @param percentualeContributo
	 *            the percentualeContributo to set
	 */
	public void setPercentualeContributo(BigDecimal percentualeContributo) {
		this.percentualeContributo = percentualeContributo;
	}

	/**
	 * @param percentualeUsoPromiscuo
	 *            the percentualeUsoPromiscuo to set
	 */
	public void setPercentualeUsoPromiscuo(BigDecimal percentualeUsoPromiscuo) {
		this.percentualeUsoPromiscuo = percentualeUsoPromiscuo;
	}

	/**
	 * @param soggettoAContributo
	 *            the soggettoAContributo to set
	 */
	public void setSoggettoAContributo(boolean soggettoAContributo) {
		this.soggettoAContributo = soggettoAContributo;
	}

	/**
	 * @param sottocontiBeni
	 *            the sottocontiBeni to set
	 */
	public void setSottocontiBeni(SottocontiBeni sottocontiBeni) {
		this.sottocontiBeni = sottocontiBeni;
	}

	/**
	 * @param sottoSpecie
	 *            the sottoSpecie to set
	 */
	public void setSottoSpecie(SottoSpecie sottoSpecie) {
		this.sottoSpecie = sottoSpecie;
	}

	/**
	 * @param stampaSuRegistriBeni
	 *            the stampaSuRegistriBeni to set
	 */
	public void setStampaSuRegistriBeni(boolean stampaSuRegistriBeni) {
		this.stampaSuRegistriBeni = stampaSuRegistriBeni;
	}

	/**
	 * @param stampaSuRegistriInventari
	 *            the stampaSuRegistriInventari to set
	 */
	public void setStampaSuRegistriInventari(boolean stampaSuRegistriInventari) {
		this.stampaSuRegistriInventari = stampaSuRegistriInventari;
	}

	/**
	 * @param ubicazione
	 *            the ubicazione to set
	 */
	public void setUbicazione(Ubicazione ubicazione) {
		this.ubicazione = ubicazione;
	}

	/**
	 * @param valoreAcquisto
	 *            the valoreAcquisto to set
	 */
	public void setValoreAcquisto(BigDecimal valoreAcquisto) {
		this.valoreAcquisto = valoreAcquisto;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("BeneAmmortizzabile[");
		buffer.append("acquistatoUsato = ").append(acquistatoUsato);
		buffer.append(" annoAcquisto = ").append(annoAcquisto);
		buffer.append(" beneDiProprieta = ").append(beneDiProprieta);
		buffer.append(" beneInLeasing = ").append(beneInLeasing);
		buffer.append(" beneMateriale = ").append(beneMateriale);
		buffer.append(" benePadre = ").append(benePadre != null ? benePadre.getId() : null);
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" codiceBeneEsterno = ").append(codiceBeneEsterno);
		buffer.append(" dataInizioAmmortamento = ").append(dataInizioAmmortamento);
		buffer.append(" datiCivilistici = ").append(datiCivilistici);
		buffer.append(" datiFiscali = ").append(datiFiscali);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" fabbricato = ").append(fabbricato);
		buffer.append(" fornitore = ").append(fornitore != null ? fornitore.getId() : null);
		buffer.append(" importoFatturaAcquisto = ").append(importoFatturaAcquisto);
		buffer.append(" importoSoggettoAdAmmortamento = ").append(importoSoggettoAdAmmortamento);
		buffer.append(" indAmmortamento = ").append(indAmmortamento);
		buffer.append(" indTestoUnico = ").append(indTestoUnico);
		buffer.append(" manutenzione = ").append(manutenzione);
		buffer.append(" note = ").append(note);
		buffer.append(" noteInterne = ").append(noteInterne);
		buffer.append(" numeroProtocolloAcquisto = ").append(numeroProtocolloAcquisto);
		buffer.append(" numeroRegistro = ").append(numeroRegistro);
		buffer.append(" percentualeContributo = ").append(percentualeContributo);
		buffer.append(" percentualeUsoPromiscuo = ").append(percentualeUsoPromiscuo);
		buffer.append(" soggettoAContributo = ").append(soggettoAContributo);
		buffer.append(" sottoSpecie = ").append(sottoSpecie != null ? sottoSpecie.getId() : null);
		buffer.append(" stampaSuRegistriBeni = ").append(stampaSuRegistriBeni);
		buffer.append(" stampaSuRegistriInventari = ").append(stampaSuRegistriInventari);
		buffer.append(" eliminato = ").append(eliminato);
		buffer.append(" ubicazione = ").append(ubicazione != null ? ubicazione.getId() : null);
		buffer.append(" valoreAcquisto = ").append(valoreAcquisto);
		buffer.append("]");
		return buffer.toString();
	}

}
