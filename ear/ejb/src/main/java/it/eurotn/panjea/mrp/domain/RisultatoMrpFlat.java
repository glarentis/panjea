package it.eurotn.panjea.mrp.domain;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RisultatoMrpFlat implements IDefProperty, Serializable {

	private static final long serialVersionUID = -1377343920484610670L;

	private ArticoloLite distinta;
	private ArticoloLite articolo;
	private ArticoloLite articoloOrdine;
	private DepositoLite deposito;
	private FornitoreLite fornitore;
	// private TipoDocumento tipoDocumentoDaGenerare; // Tipo documento
	// dell'ordine da generare
	private TipoAreaOrdine tipoAreaOrdineDaGenerare; // Tipo documento
	// dell'ordine da
	// generare
	private TipoDocumento tipoDocumentoOrdine;// Tipo documento dell'ordine di
	// origine
	private AreaOrdine areaOrdine;
	private ConfigurazioneDistinta configurazioneDistinta;

	private Date dataConsegna;
	private Date dataDocumento;

	private List<ConflittoMrp> conflitti;

	private Double giacenza;

	private Double disponibilita;

	private Double disponibilitaDopoUtilizzo;
	private Double qtaR;
	private Double qtaInArrivo;

	private Double qtaS;

	private Double qtaCalcolata;
	private Double scorta;

	private Double lottoRiordino;

	private int ordinamento;
	private Double minOrdinabile;

	private Integer leadTime;

	private Integer idAreaOrdine;
	private Integer idRigaOrdine;
	private Integer numRiga;
	private Integer id;
	{
		this.disponibilita = 0.0;
		this.disponibilitaDopoUtilizzo = 0.0;
		this.giacenza = 0.0;
		this.deposito = new DepositoLite();
		this.fornitore = new FornitoreLite();
		this.articolo = new ArticoloLite();
		this.articoloOrdine = new ArticoloLite();
		this.distinta = new ArticoloLite();
		this.tipoAreaOrdineDaGenerare = new TipoAreaOrdine();
		this.tipoAreaOrdineDaGenerare.setTipoDocumento(new TipoDocumento());
		this.areaOrdine = new AreaOrdine();
		this.areaOrdine.setDocumento(new Documento());
		this.tipoDocumentoOrdine = new TipoDocumento();
		this.areaOrdine.setTipoAreaOrdine(new TipoAreaOrdine());
		this.areaOrdine.getTipoAreaOrdine().setTipoDocumento(tipoDocumentoOrdine);
		this.configurazioneDistinta = new ConfigurazioneDistinta();
	}

	/**
	 * @param conflitto
	 *            The conflitti to set.
	 */
	public void addConflitto(ConflittoMrp conflitto) {
		if (conflitti == null) {
			conflitti = new ArrayList<>();
		}
		conflitti.add(conflitto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RisultatoMrpFlat other = (RisultatoMrpFlat) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the articoloCodice.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return articolo riga ordine
	 */
	public ArticoloLite getArticoloOrdine() {
		return articoloOrdine;
	}

	/**
	 * @return the configurazioneDistinta
	 */
	public ConfigurazioneDistinta getConfigurazioneDistinta() {
		return configurazioneDistinta;
	}

	/**
	 * @return Returns the conflitti.
	 */
	public List<ConflittoMrp> getConflitti() {
		return conflitti;
	}

	/**
	 * @return Returns the dataConsegna.
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return Returns the dataDocumento.
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return Returns the depositoCodice.
	 */
	public DepositoLite getDeposito() {
		return deposito;
	}

	/**
	 * @return Returns the disponibilita.
	 */
	public Double getDisponibilita() {
		return disponibilita;
	}

	/**
	 * @return Returns the disponibilitaDopoUtilizzo.
	 */
	public Double getDisponibilitaDopoUtilizzo() {
		return disponibilitaDopoUtilizzo;
	}

	/**
	 * @return articoloLite.
	 */
	public ArticoloLite getDistinta() {
		return distinta;
	}

	@Override
	public String getDomainClassName() {
		return RisultatoMrpFlat.class.getName();
	}

	/**
	 * @return Returns the fornitore.
	 */
	public FornitoreLite getFornitore() {
		if (fornitore != null && fornitore.isNew()) {
			return null;
		}
		return fornitore;
	}

	/**
	 * @return Returns the giacenza.
	 */
	public Double getGiacenza() {
		return giacenza;
	}

	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @return Returns the idAreaOrdine.
	 */
	public Integer getIdAreaOrdine() {
		return idAreaOrdine;
	}

	/**
	 * @return Returns the idRigaOrdine.
	 */
	public Integer getIdRigaOrdine() {
		return idRigaOrdine;
	}

	/**
	 * @return Returns the leadTime.
	 */
	public Integer getLeadTime() {
		return leadTime;
	}

	/**
	 * @return Returns the lottoRiordino.
	 */
	public Double getLottoRiordino() {
		return lottoRiordino;
	}

	/**
	 * @return Returns the minOrdinabile.
	 */
	public Double getMinOrdinabile() {
		return minOrdinabile;
	}

	/**
	 * @return Returns the numRiga.
	 */
	public Integer getNumRiga() {
		return numRiga;
	}

	public int getOrdinamento() {
		return ordinamento;
	}

	/**
	 * @return Returns the ordine.
	 */
	public AreaOrdine getOrdine() {
		return areaOrdine;
	}

	/**
	 * @return Returns the qtaCalcolata.
	 */
	public Double getQtaCalcolata() {
		return qtaCalcolata;
	}

	/**
	 * @return the qtaInArrivo
	 */
	public Double getQtaInArrivo() {
		return qtaInArrivo;
	}

	/**
	 * @return Returns the qtaR.
	 */
	public Double getQtaR() {
		return qtaR;
	}

	/**
	 * @return Returns the qtaS.
	 */
	public Double getQtaS() {
		return qtaS;
	}

	/**
	 * @return the scorta
	 */
	public Double getScorta() {
		return scorta;
	}

	/**
	 * @return Returns the tipoAreaOrdineDaGenerare.
	 */
	public TipoAreaOrdine getTipoAreaOrdineDaGenerare() {
		return tipoAreaOrdineDaGenerare;
	}

	/**
	 * @return Returns the tipoDocumentoOrdine.
	 */
	public TipoDocumento getTipoDocumentoOrdine() {
		return tipoDocumentoOrdine;
	}

	@Override
	public Integer getVersion() {
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	/**
	 * @param articoloCodice
	 *            The articoloCodice to set.
	 */
	public void setArticoloCodice(String articoloCodice) {
		this.articolo.setCodice(articoloCodice);
	}

	/**
	 * @param articoloDescrizione
	 *            The articoloDescrizione to set.
	 */
	public void setArticoloDescrizione(String articoloDescrizione) {
		this.articolo.setDescrizione(articoloDescrizione);
	}

	/**
	 * @param articoloDistinta
	 *            The articoloDistinta to set.
	 */
	public void setArticoloDistinta(boolean articoloDistinta) {
		this.articolo.setDistinta(articoloDistinta);
	}

	/**
	 * @param articoloId
	 *            The articoloId to set.
	 */
	public void setArticoloId(Integer articoloId) {
		this.articolo.setId(articoloId);
	}

	/**
	 *
	 * @param numDecimali
	 *            num decimali articolo
	 */
	public void setArticoloNumeroDecimaliQta(Integer numDecimali) {
		this.articolo.setNumeroDecimaliQta(numDecimali);
	}

	/**
	 * @param articoloOrigCodice
	 *            the articoloOrigCodice to set
	 */
	public void setArticoloOrigCodice(String articoloOrigCodice) {
		this.articoloOrdine.setCodice(articoloOrigCodice);
	}

	/**
	 * @param articoloOrigDescrizione
	 *            the articoloOrigDescrizione to set
	 */
	public void setArticoloOrigDescrizione(String articoloOrigDescrizione) {
		this.articoloOrdine.setDescrizione(articoloOrigDescrizione);
	}

	/**
	 * @param articoloOrigDistinta
	 *            the articoloOrigDistinta to set
	 */
	public void setArticoloOrigDistinta(boolean articoloOrigDistinta) {
		this.articoloOrdine.setDistinta(articoloOrigDistinta);
	}

	/**
	 * @param articoloOrigId
	 *            the articoloOrigId to set
	 */
	public void setArticoloOrigId(Integer articoloOrigId) {
		this.articoloOrdine.setId(articoloOrigId);
	}

	/**
	 * @param articoloOrigNumeroDecimaliQta
	 *            the articoloOrigNumeroDecimaliQta to set
	 */
	public void setArticoloOrigNumeroDecimaliQta(Integer articoloOrigNumeroDecimaliQta) {
		this.articoloOrdine.setNumeroDecimaliQta(articoloOrigNumeroDecimaliQta);
	}

	/**
	 * @param configurazioneDistintaId
	 *            the configurazioneDistintaId to set
	 */
	public void setConfigurazioneDistintaId(Integer configurazioneDistintaId) {
		this.configurazioneDistinta.setId(configurazioneDistintaId);
	}

	/**
	 * @param configurazioneDistintaNome
	 *            the configurazioneDistintaNome to set
	 */
	public void setConfigurazioneDistintaNome(String configurazioneDistintaNome) {
		this.configurazioneDistinta.setNome(configurazioneDistintaNome);
	}

	/**
	 * @param dataConsegna
	 *            The dataConsegna to set.
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataDocumento
	 *            The dataDocumento to set.
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param depositoCodice
	 *            The depositoCodice to set.
	 */
	public void setDepositoCodice(String depositoCodice) {
		this.deposito.setCodice(depositoCodice);
	}

	/**
	 * @param depositoDescrizione
	 *            The depositoCodice to set.
	 */
	public void setDepositoDescrizione(String depositoDescrizione) {
		this.deposito.setCodice(depositoDescrizione);
	}

	/**
	 * @param depositoId
	 *            The depositoId to set.
	 */
	public void setDepositoId(Integer depositoId) {
		this.deposito.setId(depositoId);
	}

	/**
	 * @param disponibilita
	 *            The disponibilita to set.
	 */
	public void setDisponibilita(Double disponibilita) {
		this.disponibilita = disponibilita;
	}

	/**
	 * @param disponibilitaDopoUtilizzo
	 *            The disponibilitaDopoUtilizzo to set.
	 */
	public void setDisponibilitaDopoUtilizzo(Double disponibilitaDopoUtilizzo) {
		this.disponibilitaDopoUtilizzo = disponibilitaDopoUtilizzo;
	}

	/**
	 * @param distinta
	 *            The distinta to set.
	 */
	public void setDistinta(ArticoloLite distinta) {
		this.distinta = distinta;
	}

	/**
	 * @param distintaCodice
	 *            the distintaCodice to set
	 */
	public void setDistintaCodice(String distintaCodice) {
		this.distinta.setCodice(distintaCodice);
	}

	/**
	 * @param distintaDescrizione
	 *            the distintaDescrizione to set
	 */
	public void setDistintaDescrizione(String distintaDescrizione) {
		this.distinta.setDescrizione(distintaDescrizione);
	}

	/**
	 * @param distintaId
	 *            the distintaId to set
	 */
	public void setDistintaId(Integer distintaId) {
		this.distinta.setId(distintaId);
	}

	/**
	 * @param fornitore
	 *            The fornitore to set.
	 */
	public void setFornitore(FornitoreLite fornitore) {
		this.fornitore = fornitore;
	}

	/**
	 * @param fornitoreCodice
	 *            The fornitoreCodice to set.
	 */
	public void setFornitoreCodice(Integer fornitoreCodice) {
		this.fornitore.setCodice(fornitoreCodice);
	}

	/**
	 * @param fornitoreDenominazione
	 *            The fornitoreDenominazione to set.
	 */
	public void setFornitoreDenominazione(String fornitoreDenominazione) {
		this.fornitore.getAnagrafica().setDenominazione(fornitoreDenominazione);
	}

	/**
	 * @param fornitoreId
	 *            The fornitoreId to set.
	 */
	public void setFornitoreId(Integer fornitoreId) {
		this.fornitore.setId(fornitoreId);
	}

	/**
	 * @param giacenza
	 *            The giacenza to set.
	 */
	public void setGiacenza(Double giacenza) {
		this.giacenza = giacenza;
	}

	/**
	 * @param idRisultato
	 *            the idRisultato to set
	 */
	public void setId(Integer idRisultato) {
		this.id = idRisultato;
	}

	/**
	 * @param idAreaOrdine
	 *            the idAreaOrdine to set
	 */
	public void setIdAreaOrdine(Integer idAreaOrdine) {
		this.idAreaOrdine = idAreaOrdine;
		areaOrdine.setId(idAreaOrdine);
	}

	/**
	 * @param idRigaOrdine
	 *            The idRigaOrdine to set.
	 */
	public void setIdRigaOrdine(Integer idRigaOrdine) {
		this.idRigaOrdine = idRigaOrdine;
	}

	/**
	 * @param leadTime
	 *            The leadTime to set.
	 */
	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}

	/**
	 * @param lottoRiordino
	 *            The lottoRiordino to set.
	 */
	public void setLottoRiordino(Double lottoRiordino) {
		this.lottoRiordino = lottoRiordino;
	}

	/**
	 * @param minOrdinabile
	 *            The minOrdinabile to set.
	 */
	public void setMinOrdinabile(Double minOrdinabile) {
		this.minOrdinabile = minOrdinabile;
	}

	/**
	 * @param numRiga
	 *            The numRiga to set.
	 */
	public void setNumRiga(Integer numRiga) {
		this.numRiga = numRiga;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setOrdineCodice(String codice) {
		// caricare dalla query il codice di tipo stringa e togliere il toString
		areaOrdine.getDocumento().getCodice().setCodice(codice);
	}

	/**
	 * @param dataOrdine
	 *            the dataOrdine to set
	 */
	public void setOrdineData(Date dataOrdine) {
		areaOrdine.getDocumento().setDataDocumento(dataOrdine);
	}

	/**
	 * @param idParam
	 *            the idParam to set
	 */
	public void setOrdineId(Integer idParam) {
		areaOrdine.getDocumento().setId(idParam);
	}

	/**
	 * @param ordineProduzione
	 *            the ordineProduzione to set
	 */
	public void setOrdineProduzione(Boolean ordineProduzione) {
		if (ordineProduzione == null) {
			ordineProduzione = Boolean.FALSE;
		}
		this.areaOrdine.getTipoAreaOrdine().setOrdineProduzione(ordineProduzione);
	}

	/**
	 * @param qtaCalcolata
	 *            The qtaCalcolata to set.
	 */
	public void setQtaCalcolata(Double qtaCalcolata) {
		this.qtaCalcolata = qtaCalcolata;
	}

	/**
	 * @param qtaInArrivo
	 *            the qtaInArrivo to set
	 */
	public void setQtaInArrivo(Double qtaInArrivo) {
		this.qtaInArrivo = qtaInArrivo;
	}

	/**
	 * @param qtaR
	 *            The qtaR to set.
	 */
	public void setQtaR(Double qtaR) {
		this.qtaR = qtaR;
	}

	/**
	 * @param qtaS
	 *            The qtaS to set.
	 */
	public void setQtaS(Double qtaS) {
		this.qtaS = qtaS;
	}

	/**
	 * @param scorta
	 *            the scorta to set
	 */
	public void setScorta(Double scorta) {
		this.scorta = scorta;
	}

	/**
	 * @param tipoAreaOrdineDaCreareId
	 *            value to set
	 */
	public void setTipoAreaOrdineDaCreareId(Integer tipoAreaOrdineDaCreareId) {
		this.tipoAreaOrdineDaGenerare.setId(tipoAreaOrdineDaCreareId);
	}

	/**
	 * @param tipoAreaOrdineDaGenerare
	 *            The tipoAreaOrdineDaGenerare to set.
	 */
	public void setTipoAreaOrdineDaGenerare(TipoAreaOrdine tipoAreaOrdineDaGenerare) {
		this.tipoAreaOrdineDaGenerare = tipoAreaOrdineDaGenerare;
	}

	/**
	 * @param tipoDocumento
	 *            The tipoDocumento to set.
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoAreaOrdineDaGenerare.setTipoDocumento(tipoDocumento);
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setTipoDocumentoCodice(String codiceTipoDocumento) {
		this.tipoAreaOrdineDaGenerare.getTipoDocumento().setCodice(codiceTipoDocumento);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setTipoDocumentoDescrizione(String descrizioneTipoDocumento) {
		this.tipoAreaOrdineDaGenerare.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idTipoDocumento
	 *            id tipo documento
	 */
	public void setTipoDocumentoId(Integer idTipoDocumento) {
		this.tipoAreaOrdineDaGenerare.getTipoDocumento().setId(idTipoDocumento);
	}

	/**
	 * @param codice
	 *            the tipoDocumentoOrdineCodice to set
	 */
	public void setTipoDocumentoOrdineCodice(String codice) {
		tipoDocumentoOrdine.setCodice(codice);
		areaOrdine.getDocumento().getTipoDocumento().setCodice(codice);
	}

	/**
	 * @param descrizione
	 *            the tipoDocumentoOrdineDescrizione to set
	 */
	public void setTipoDocumentoOrdineDescrizione(String descrizione) {
		tipoDocumentoOrdine.setDescrizione(descrizione);
		areaOrdine.getDocumento().getTipoDocumento().setDescrizione(descrizione);
	}

	/**
	 * @param idParam
	 *            the tipoDocumentoOrdineId to set
	 */
	public void setTipoDocumentoOrdineId(Integer idParam) {
		tipoDocumentoOrdine.setId(idParam);
	}

	@Override
	public String toString() {
		return "RisultatoMrpFlat [dataConsegna=" + dataConsegna + ", dataDocumento=" + dataDocumento
				+ ", idRigaOrdine=" + idRigaOrdine + ", id=" + id + "]";
	}

}
