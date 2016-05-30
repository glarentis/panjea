/**t\
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.contabilita.domain.SottoConto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;

/**
 * @author Leonardo
 *
 */
@Entity
@Table(name = "bamm_bene_ammortizzabile")
@NamedQueries({ @NamedQuery(name = "BeneAmmortizzabileLite.caricaAll", query = " from BeneAmmortizzabileLite b where b.codiceAzienda = :paramCodiceAzienda ") })
public class BeneAmmortizzabileLite extends EntityBase {

	private static final long serialVersionUID = -5322677252640881058L;

	public static final String PROP_CODICE = "codice";
	public static final String PROP_CODICE_AZIENDA = "codiceAzienda";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_ANNO_ACQUISTO = "annoAcquisto";
	public static final String PROP_NUMERO_REGISTRO = "numeroRegistro";
	public static final String PROP_NUMERO_PROTOCOLLO_ACQUISTO = "numeroProtocolloAcquisto";
	public static final String PROP_VALORE_ACQUISTO = "valoreAcquisto";
	public static final String PROP_IMPORTO_FATTURA_ACQUISTO = "importoFatturaAcquisto";
	public static final String PROP_DATA_INIZIO_AMMORTAMENTO = "dataInizioAmmortamento";
	public static final String PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO = "importoSoggettoAdAmmortamento";
	public static final String PROP_ACQUISTATO_USATO = "acquistatoUsato";
	public static final String PROP_BENE_MATERIALE = "beneMateriale";
	public static final String PROP_FABBRICATO = "fabbricato";
	public static final String PROP_MANUTENZIONE = "manutenzione";
	public static final String PROP_STAMPA_SU_REGISTRI_BENI = "stampaSuRegistriBeni";
	public static final String PROP_STAMPA_SU_REGISTRI_INVENTARI = "stampaSuRegistriInventari";
	public static final String PROP_SOGGETTO_A_CONTRIBUTO = "soggettoAContributo";
	public static final String PROP_BENE_DI_PROPRIETA = "beneDiProprieta";
	public static final String PROP_BENE_IN_LEASING = "beneInLeasing";
	public static final String PROP_PERCENTUALE_USO_PROMISCUO = "percentualeUsoPromiscuo";
	public static final String PROP_PERCENTUALE_CONTRIBUTO = "percentualeContributo";
	public static final String PROP_IND_AMMORTAMENTO = "indAmmortamento";
	public static final String PROP_IND_TESTO_UNICO = "indTestoUnico";
	public static final String PROP_CODICE_BENE_ESTERNO = "codiceBeneEsterno";
	public static final String PROP_MATRICOLA_AZIENDALE = "matricolaAziendale";
	public static final String PROP_MATRICOLA_FORNITORE = "matricolaFornitore";
	public static final String PROP_FORNITORE = "fornitore";

	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda = null;
	private Integer codice;
	private String descrizione = null;
	@Column(length = 10)
	private String codiceBeneEsterno;

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

	@Column(length = 10)
	private String numeroProtocolloAcquisto;

	private BigDecimal valoreAcquisto;
	private BigDecimal importoFatturaAcquisto;
	@Formula("( select SUM(ba.importoSoggettoAdAmmortamentoSingolo) from bamm_bene_ammortizzabile ba where ba.id = id or ba.benePadre_id = id )")
	private BigDecimal importoSoggettoAdAmmortamento;
	private BigDecimal importoSoggettoAdAmmortamentoSingolo;
	private BigDecimal percentualeUsoPromiscuo;
	private BigDecimal percentualeContributo;

	@Temporal(TemporalType.DATE)
	private Date dataInizioAmmortamento;

	@Embedded
	private DatiCivilistici datiCivilistici;
	@Embedded
	private DatiFiscali datiFiscali;

	@ManyToOne(fetch = FetchType.LAZY)
	private SottoSpecie sottoSpecie;
	@ManyToOne(fetch = FetchType.LAZY)
	private Ubicazione ubicazione;

	@Column(length = 15)
	private String matricolaAziendale;

	@Column(length = 15)
	private String matricolaFornitore;

	@ManyToOne
	private FornitoreLite fornitore;

	private boolean eliminato;

	@Transient
	private BigDecimal variazioniBene = BigDecimal.ZERO;
	@Transient
	private BigDecimal variazioniFondo = BigDecimal.ZERO;

	@Transient
	private BigDecimal venditeBene = BigDecimal.ZERO;
	@Transient
	private BigDecimal venditeFondo = BigDecimal.ZERO;

	@Embedded
	private SottocontiBeni sottocontiBeni;

	/**
	 * Costruttore di default.
	 */
	public BeneAmmortizzabileLite() {
		initialize();
	}

	/**
	 *
	 * @param acquistatoUsato
	 *            valore di acquistatoUsato
	 * @param annoAcquisto
	 *            valore di annoAcquisto
	 * @param beneDiProprieta
	 *            valore di beneDiProprieta
	 * @param beneInLeasing
	 *            valore di beneInLeasing
	 * @param beneMateriale
	 *            valore di beneMateriale
	 * @param codice
	 *            valore di codice
	 * @param codiceAzienda
	 *            valore di codiceAzienda
	 * @param codiceBeneEsterno
	 *            valore di codiceBeneEsterno
	 * @param dataInizioAmmortamento
	 *            valore di dataInizioAmmortamento
	 * @param datiCivilistici
	 *            valore di datiCivilistici
	 * @param datiFiscali
	 *            valore di datiFiscali
	 * @param descrizione
	 *            valore di descrizione
	 * @param fabbricato
	 *            valore di fabbricato
	 * @param id
	 *            valore di id
	 * @param importoFatturaAcquisto
	 *            valore di importoFatturaAcquisto
	 * @param importoSoggettoAdAmmortamento
	 *            valore di importoSoggettoAdAmmortamento
	 * @param indAmmortamento
	 *            valore di indAmmortamento
	 * @param indTestoUnico
	 *            valore di indTestoUnico
	 * @param manutenzione
	 *            valore di manutenzione
	 * @param numeroProtocolloAcquisto
	 *            valore di numeroProtocolloAcquisto
	 * @param numeroRegistro
	 *            valore di numeroRegistro
	 * @param percentualeContributo
	 *            valore di percentualeContributo
	 * @param percentualeUsoPromiscuo
	 *            valore di percentualeUsoPromiscuo
	 * @param soggettoAContributo
	 *            valore di soggettoAContributo
	 * @param sottoSpecie
	 *            valore di sottoSpecie
	 * @param stampaSuRegistriBeni
	 *            valore di stampaSuRegistriBeni
	 * @param stampaSuRegistriInventari
	 *            valore di stampaSuRegistriInventari
	 * @param stato
	 *            valore di stato
	 * @param ubicazione
	 *            valore di ubicazione
	 * @param valoreAcquisto
	 *            valore di valoreAcquisto
	 * @param version
	 *            valore di version
	 * @param importoVariazioniBene
	 *            valore di importoVariazioniBene
	 * @param importoVariazioniFondo
	 *            valore di importoVariazioniFondo
	 * @param importoVenditeBene
	 *            valore di importoVenditeBene
	 * @param importoVenditeFondo
	 *            valore di importoVenditeFondo
	 * @param importoSoggettoAdAmmortamentoSingolo
	 *            valore di importoSoggettoAdAmmortamentoSingolo
	 */
	public BeneAmmortizzabileLite(final BigDecimal importoSoggettoAdAmmortamento,
			final BigDecimal importoVariazioniBene, final BigDecimal importoVariazioniFondo,
			final BigDecimal importoVenditeBene, final BigDecimal importoVenditeFondo, final boolean acquistatoUsato,
			final Integer annoAcquisto, final boolean beneDiProprieta, final boolean beneInLeasing,
			final boolean beneMateriale, final Integer codice, final String codiceAzienda,
			final String codiceBeneEsterno, final Date dataInizioAmmortamento, final DatiCivilistici datiCivilistici,
			final DatiFiscali datiFiscali, final String descrizione, final boolean fabbricato, final Integer id,
			final BigDecimal importoFatturaAcquisto, final BigDecimal importoSoggettoAdAmmortamentoSingolo,
			final boolean indAmmortamento, final boolean indTestoUnico, final boolean manutenzione,
			final String numeroProtocolloAcquisto, final Integer numeroRegistro,
			final BigDecimal percentualeContributo, final BigDecimal percentualeUsoPromiscuo,
			final boolean soggettoAContributo, final SottoSpecie sottoSpecie, final boolean stampaSuRegistriBeni,
			final boolean stampaSuRegistriInventari, final boolean stato, final Ubicazione ubicazione,
			final BigDecimal valoreAcquisto, final Integer version) {
		this.importoSoggettoAdAmmortamento = importoSoggettoAdAmmortamento;
		// proprieta calcolata e solo in lettura
		this.variazioniBene = importoVariazioniBene;
		this.variazioniFondo = importoVariazioniFondo;
		this.venditeBene = importoVenditeBene;
		this.venditeFondo = importoVenditeFondo;
		setAcquistatoUsato(acquistatoUsato);
		setAnnoAcquisto(annoAcquisto);
		setBeneDiProprieta(beneDiProprieta);
		setBeneInLeasing(beneInLeasing);
		setBeneMateriale(beneMateriale);
		setCodice(codice);
		setCodiceAzienda(codiceAzienda);
		setCodiceBeneEsterno(codiceBeneEsterno);
		setDataInizioAmmortamento(dataInizioAmmortamento);
		setDatiCivilistici(datiCivilistici);
		setDatiFiscali(datiFiscali);
		setDescrizione(descrizione);
		setFabbricato(fabbricato);
		setId(id);
		setImportoFatturaAcquisto(importoFatturaAcquisto);
		setImportoSoggettoAdAmmortamentoSingolo(importoSoggettoAdAmmortamentoSingolo);
		setIndAmmortamento(indAmmortamento);
		setIndTestoUnico(indTestoUnico);
		setManutenzione(manutenzione);
		setNumeroProtocolloAcquisto(numeroProtocolloAcquisto);
		setNumeroRegistro(numeroRegistro);
		setPercentualeContributo(percentualeContributo);
		setPercentualeUsoPromiscuo(percentualeUsoPromiscuo);
		setSoggettoAContributo(soggettoAContributo);
		setSottoSpecie(sottoSpecie);
		setStampaSuRegistriBeni(stampaSuRegistriBeni);
		setStampaSuRegistriInventari(stampaSuRegistriInventari);
		setUbicazione(ubicazione);
		setValoreAcquisto(valoreAcquisto);
		setVersion(version);
		setEliminato(stato);
	}

	/**
	 * @return the annoAcquisto
	 */
	public Integer getAnnoAcquisto() {
		return annoAcquisto;
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
	 *
	 * @return fornitore
	 */
	public FornitoreLite getFornitore() {
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
	 *
	 * @return matricolaAziendale
	 */
	public String getMatricolaAziendale() {
		return matricolaAziendale;
	}

	/**
	 *
	 * @return matricolaFornitore
	 */
	public String getMatricolaFornitore() {
		return matricolaFornitore;
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
	 * @return the sottoContoAmmortamento
	 */
	public SottoConto getSottoContoAmmortamento() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoAmmortamento();
	}

	/**
	 * @return the sottoContoAmmortamentoAnticipato
	 */
	public SottoConto getSottoContoAmmortamentoAnticipato() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoAmmortamentoAnticipato();
	}

	/**
	 * @return the sottoContoFondoAmmortamento
	 */
	public SottoConto getSottoContoFondoAmmortamento() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoFondoAmmortamento();
	}

	/**
	 * @return the sottoContoFondoAmmortamentoAnticipato
	 */
	public SottoConto getSottoContoFondoAmmortamentoAnticipato() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoFondoAmmortamentoAnticipato();
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
	 * @return the variazioniBene
	 */
	public BigDecimal getVariazioniBene() {
		return variazioniBene;
	}

	/**
	 * @return the variazioniFondo
	 */
	public BigDecimal getVariazioniFondo() {
		return variazioniFondo;
	}

	/**
	 * @return the venditeBene
	 */
	public BigDecimal getVenditeBene() {
		return venditeBene;
	}

	/**
	 * @return the venditeFondo
	 */
	public BigDecimal getVenditeFondo() {
		return venditeFondo;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		valoreAcquisto = BigDecimal.ZERO;
		importoFatturaAcquisto = BigDecimal.ZERO;
		importoSoggettoAdAmmortamento = BigDecimal.ZERO;
		percentualeUsoPromiscuo = BigDecimal.ZERO;
		percentualeContributo = BigDecimal.ZERO;
		datiCivilistici = new DatiCivilistici();
		datiFiscali = new DatiFiscali();
		sottoSpecie = new SottoSpecie();
		ubicazione = new Ubicazione();
		sottocontiBeni = new SottocontiBeni();
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
	 *
	 * @param Fornitore
	 *            the codiceFornitore to set
	 */
	public void setCodiceFornitore(Integer codiceFornitore) {
		this.fornitore.setCodice(codiceFornitore);
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
	 *
	 * @param descrizioneFornitore
	 *            the descrizioneFornitore to set
	 */
	public void setDescrizioneFornitore(String descrizioneFornitore) {
		this.fornitore.getAnagrafica().setDenominazione(descrizioneFornitore);
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
	 *
	 * @param fornitore
	 *            the fornitore to set
	 */
	public void setFornitore(FornitoreLite fornitore) {
		this.fornitore = fornitore;
	}

	/**
	 *
	 * @param idFornitore
	 *            the idFornitore to set
	 */
	public void setIdFornitore(Integer idFornitore) {
		this.fornitore = new FornitoreLite();
		this.fornitore.setId(idFornitore);
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
	 *
	 * @param matricolaAziendale
	 *            the matricolaAziendale to set
	 */
	public void setMatricolaAziendale(String matricolaAziendale) {
		this.matricolaAziendale = matricolaAziendale;
	}

	/**
	 *
	 * @param matricolaFornitore
	 *            the matricolaFornitore to set
	 */
	public void setMatricolaFornitore(String matricolaFornitore) {
		this.matricolaFornitore = matricolaFornitore;
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
		buffer.append("BeneAmmortizzabileLite[");
		buffer.append("acquistatoUsato = ").append(acquistatoUsato);
		buffer.append(" annoAcquisto = ").append(annoAcquisto);
		buffer.append(" beneDiProprieta = ").append(beneDiProprieta);
		buffer.append(" beneInLeasing = ").append(beneInLeasing);
		buffer.append(" beneMateriale = ").append(beneMateriale);
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" codiceBeneEsterno = ").append(codiceBeneEsterno);
		buffer.append(" dataInizioAmmortamento = ").append(dataInizioAmmortamento);
		buffer.append(" datiCivilistici = ").append(datiCivilistici);
		buffer.append(" datiFiscali = ").append(datiFiscali);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" fabbricato = ").append(fabbricato);
		buffer.append(" importoFatturaAcquisto = ").append(importoFatturaAcquisto);
		buffer.append(" importoSoggettoAdAmmortamento = ").append(importoSoggettoAdAmmortamento);
		buffer.append(" indAmmortamento = ").append(indAmmortamento);
		buffer.append(" indTestoUnico = ").append(indTestoUnico);
		buffer.append(" manutenzione = ").append(manutenzione);
		buffer.append(" numeroProtocolloAcquisto = ").append(numeroProtocolloAcquisto);
		buffer.append(" numeroRegistro = ").append(numeroRegistro);
		buffer.append(" percentualeContributo = ").append(percentualeContributo);
		buffer.append(" percentualeUsoPromiscuo = ").append(percentualeUsoPromiscuo);
		buffer.append(" soggettoAContributo = ").append(soggettoAContributo);
		// buffer.append(" sottoSpecie = ").append(sottoSpecie!=null ?
		// sottoSpecie.getId() : null);
		buffer.append(" stampaSuRegistriBeni = ").append(stampaSuRegistriBeni);
		buffer.append(" stampaSuRegistriInventari = ").append(stampaSuRegistriInventari);
		// buffer.append(" ubicazione = ").append(ubicazione!=null ?
		// ubicazione.getId() : null);
		buffer.append(" valoreAcquisto = ").append(valoreAcquisto);
		buffer.append("]");
		return buffer.toString();
	}

}
