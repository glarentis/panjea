package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Date;

/**
 * @author fattazzo
 */
public class RigaArticoloDTO extends RigaOrdineDTO {

	private static final long serialVersionUID = 7308580919519248590L;

	private String codiceEntita;
	private Importo prezzoUnitario;
	private Double qta;
	private Integer numeroDecimaliPrezzo;
	private Importo prezzoNetto;
	private Sconto variazione;
	private Importo prezzoTotale;
	private Double qtaChiusa;
	private Date dataConsegna;

	private final ArticoloLite articolo;

	private String noteRiga;

	/**
	 * Costruttore.
	 */
	public RigaArticoloDTO() {
		super();
		articolo = new ArticoloLite();
	}

	/**
	 * @return articolo sulla riga
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the codiceEntita
	 */
	public String getCodiceEntita() {
		return codiceEntita;
	}

	/**
	 * @return the dataConsegna
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return the noteRiga
	 */
	public String getNoteRiga() {
		return noteRiga;
	}

	/**
	 * @return the numeroDecimaliPrezzo
	 */
	public Integer getNumeroDecimaliPrezzo() {
		return numeroDecimaliPrezzo;
	}

	/**
	 * @return the prezzoNetto
	 */
	public Importo getPrezzoNetto() {
		return prezzoNetto;
	}

	/**
	 * @return the prezzoTotale
	 */
	public Importo getPrezzoTotale() {
		return prezzoTotale;
	}

	/**
	 * @return the prezzoUnitario
	 */
	public Importo getPrezzoUnitario() {
		return prezzoUnitario;
	}

	/**
	 * @return the qta
	 */
	public Double getQta() {
		return qta;
	}

	/**
	 * @return the qtaChiusa
	 */
	public Double getQtaChiusa() {
		return qtaChiusa;
	}

	@Override
	public RigaOrdine getRigaOrdine() {
		RigaOrdine riga = new RigaArticolo();
		PanjeaEJBUtil.copyProperties(riga, this);
		return riga;
	}

	/**
	 * @return the variazione
	 */
	public Sconto getVariazione() {
		return variazione;
	}

	/**
	 * @return the isEvasa
	 */
	public boolean isEvasa() {
		boolean evasa = false;

		if (isEvasioneForzata()) {
			evasa = true;
		} else {
			if (qta != null && qtaChiusa != null) {
				evasa = qta > 0.0 && (qta - qtaChiusa <= 0.0);
			}
		}
		return evasa;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.articolo.setCodice(codice);
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(String codiceEntita) {
		this.codiceEntita = codiceEntita;
	}

	/**
	 * @param dataConsegna
	 *            the dataConsegna to set
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param descrizioneArticolo
	 *            descrizione dell'articolo
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.articolo.setDescrizione(descrizioneArticolo);
	}

	/**
	 * @param idArticolo
	 *            the idArticolo to set
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.articolo.setId(idArticolo);
	}

	/**
	 * @param noteRiga
	 *            the noteRiga to set
	 */
	public void setNoteRiga(String noteRiga) {
		this.noteRiga = noteRiga;
	}

	/**
	 * @param numeroDecimaliPrezzo
	 *            the numeroDecimaliPrezzo to set
	 */
	public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
		this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
	}

	/**
	 * @param prezzoNetto
	 *            the prezzoNetto to set
	 */
	public void setPrezzoNetto(Importo prezzoNetto) {
		this.prezzoNetto = prezzoNetto;
	}

	/**
	 * @param prezzoTotale
	 *            the prezzoTotale to set
	 */
	public void setPrezzoTotale(Importo prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}

	/**
	 * @param prezzoUnitario
	 *            the prezzoUnitario to set
	 */
	public void setPrezzoUnitario(Importo prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}

	/**
	 * @param qta
	 *            the qta to set
	 */
	public void setQta(Double qta) {
		this.qta = qta;
	}

	/**
	 * @param qtaChiusa
	 *            the qtaChiusa to set
	 */
	public void setQtaChiusa(Double qtaChiusa) {
		if (qtaChiusa == null) {
			qtaChiusa = new Double(0.0);
		}
		this.qtaChiusa = qtaChiusa;
	}

	/**
	 * @param sconto
	 *            the variazione to set
	 */
	public void setSconto(Sconto sconto) {
		this.variazione = sconto;
	}

}
