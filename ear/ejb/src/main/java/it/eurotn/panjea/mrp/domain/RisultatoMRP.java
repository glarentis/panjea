package it.eurotn.panjea.mrp.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mrp_risultati")
public class RisultatoMRP extends EntityBase {

	private static final long serialVersionUID = -5460517892538952235L;

	private String codiciAttributo;
	private String valoriAttributo;
	private String formula;
	private int ordinamento;

	private int idRigaOrdine;

	private int idArticolo;

	private int idDeposito;
	private Integer idFornitore;
	private Integer idTipoDocumento;
	private Integer idConfigurazioneDistinta;
	private Integer idComponente;
	private Integer leadTime;
	private Date dataDocumento;
	private Date dataConsegna;

	private Double scorta;
	private Double minOrdinabile;

	private Double lottoRiordino;
	private double qtaR;
	private double qtaInArrivo;
	private double qtaPor;
	private double giacenza;
	private double disponibilita;
	private double disponibilitaDopoUtilizzo;
	@ManyToOne(fetch = FetchType.LAZY)
	private RisultatoMRP distinta;
	private Integer idDistintaRisultato;// aggiornato sulla salva in batch e
	// utilizzato per caricare i risultati e

	private String ordiniDaCollegare;

	/**
	 * Costruttore.
	 */
	public RisultatoMRP() {
	}

	/**
	 * Costruisco l'enntity dal risultatoDTO.
	 *
	 * @param risultatoDTO
	 *            dto.
	 */
	public RisultatoMRP(final RisultatoMRPArticoloBucket risultatoDTO) {
		PanjeaEJBUtil.copyProperties(this, risultatoDTO);
	}

	// linkarli correttamente. Viene usato solamente per le query

	/**
	 * @return Returns the codiciAttributo.
	 */
	public String getCodiciAttributo() {
		return codiciAttributo;
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
	 * @return Returns the disponibilita.
	 */
	public double getDisponibilita() {
		return disponibilita;
	}

	/**
	 * @return Returns the disponibilitaDopoUtilizzo.
	 */
	public double getDisponibilitaDopoUtilizzo() {
		return disponibilitaDopoUtilizzo;
	}

	/**
	 * @return Returns the distinta.
	 */
	public RisultatoMRP getDistinta() {
		return distinta;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @return Returns the giacenza.
	 */
	public double getGiacenza() {
		return giacenza;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public int getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return the idComponente
	 */
	public Integer getIdComponente() {
		return idComponente;
	}

	/**
	 * @return the idConfigurazioneDistinta
	 */
	public Integer getIdConfigurazioneDistinta() {
		return idConfigurazioneDistinta;
	}

	/**
	 * @return Returns the idDeposito.
	 */
	public int getIdDeposito() {
		return idDeposito;
	}

	/**
	 * @return Returns the idDistintaRisultato.
	 */
	public Integer getIdDistintaRisultato() {
		return idDistintaRisultato;
	}

	/**
	 * @return Returns the idFornitore.
	 */
	public Integer getIdFornitore() {
		return idFornitore;
	}

	/**
	 * @return Returns the idRigaOrdine.
	 */
	public int getIdRigaOrdine() {
		return idRigaOrdine;
	}

	/**
	 * @return Returns the idTipoDocumento.
	 */
	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	/**
	 * @return Returns the leadTime.
	 */
	public Integer getLeadTime() {
		return leadTime;
	}

	/**
	 * @return the lottoRiordino
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

	public int getOrdinamento() {
		return ordinamento;
	}

	public String getOrdiniDaCollegare() {
		return ordiniDaCollegare;
	}

	/**
	 * @return the qtaInArrivo
	 */
	public double getQtaInArrivo() {
		return qtaInArrivo;
	}

	/**
	 * @return Returns the qtaPor.
	 */
	public double getQtaPor() {
		return qtaPor;
	}

	/**
	 * @return Returns the qtaR.
	 */
	public double getQtaR() {
		return qtaR;
	}

	/**
	 * @return Returns the scorta.
	 */
	public Double getScorta() {
		return scorta;
	}

	/**
	 * @return Returns the valoriAttributo.
	 */
	public String getValoriAttributo() {
		return valoriAttributo;
	}

	/**
	 * Copia grli attributi di articolo anagrafica nelal riga del risultato.
	 *
	 * @param articoloAnagrafica
	 *            oggetto con gli attributi da copiare.
	 */
	public void setArticoloAnagrafica(ArticoloAnagrafica articoloAnagrafica) {
		PanjeaEJBUtil.copyProperties(this, articoloAnagrafica);
	}

	/**
	 * @param codiciAttributo
	 *            The codiciAttributo to set.
	 */
	public void setCodiciAttributo(String codiciAttributo) {
		this.codiciAttributo = codiciAttributo;
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
	 * @param disponibilita
	 *            The disponibilita to set.
	 */
	public void setDisponibilita(double disponibilita) {
		this.disponibilita = disponibilita;
	}

	/**
	 * @param disponibilitaDopoUtilizzo
	 *            The disponibilitaDopoUtilizzo to set.
	 */
	public void setDisponibilitaDopoUtilizzo(double disponibilitaDopoUtilizzo) {
		this.disponibilitaDopoUtilizzo = disponibilitaDopoUtilizzo;
	}

	/**
	 * @param distinta
	 *            The distinta to set.
	 */
	public void setDistinta(RisultatoMRP distinta) {
		this.distinta = distinta;
	}

	/**
	 * @param formula
	 *            the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @param giacenza
	 *            The giacenza to set.
	 */
	public void setGiacenza(double giacenza) {
		this.giacenza = giacenza;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(int idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param idComponente
	 *            the idComponente to set
	 */
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

	/**
	 * @param idConfigurazioneDistinta
	 *            the idConfigurazioneDistinta to set
	 */
	public void setIdConfigurazioneDistinta(Integer idConfigurazioneDistinta) {
		this.idConfigurazioneDistinta = idConfigurazioneDistinta;
	}

	/**
	 * @param idDeposito
	 *            The idDeposito to set.
	 */
	public void setIdDeposito(int idDeposito) {
		this.idDeposito = idDeposito;
	}

	/**
	 * @param idDistintaRisultato
	 *            The idDistintaRisultato to set.
	 */
	public void setIdDistintaRisultato(Integer idDistintaRisultato) {
		this.idDistintaRisultato = idDistintaRisultato;
	}

	/**
	 * @param idFornitore
	 *            The idFornitore to set.
	 */
	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}

	/**
	 * @param idRigaOrdine
	 *            The idRigaOrdine to set.
	 */
	public void setIdRigaOrdine(int idRigaOrdine) {
		this.idRigaOrdine = idRigaOrdine;
	}

	/**
	 * @param idTipoDocumento
	 *            The idTipoDocumento to set.
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
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
	 *            the lottoRiordino to set
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

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	public void setOrdiniDaCollegare(String ordiniDaCollegare) {
		this.ordiniDaCollegare = ordiniDaCollegare;
	}

	/**
	 * @param qtaInArrivo
	 *            the qtaInArrivo to set
	 */
	public void setQtaInArrivo(double qtaInArrivo) {
		this.qtaInArrivo = qtaInArrivo;
	}

	/**
	 * @param qtaPor
	 *            The qtaPor to set.
	 */
	public void setQtaPor(double qtaPor) {
		this.qtaPor = qtaPor;
	}

	/**
	 * @param qtaR
	 *            The qtaR to set.
	 */
	public void setQtaR(double qtaR) {
		this.qtaR = qtaR;
	}

	/**
	 * @param scorta
	 *            The scorta to set.
	 */
	public void setScorta(Double scorta) {
		this.scorta = scorta;
	}

	/**
	 * @param valoriAttributo
	 *            The valoriAttributo to set.
	 */
	public void setValoriAttributo(String valoriAttributo) {
		this.valoriAttributo = valoriAttributo;
	}
}
