package it.eurotn.panjea.spedizioni.util;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.spedizioni.domain.DatiSpedizioniDocumento.Consegne;
import it.eurotn.panjea.spedizioni.domain.DatiSpedizioniDocumento.ModalitaIncasso;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ParametriCreazioneEtichette implements Serializable {

	private static final long serialVersionUID = -7820898235171074101L;

	private Date dataConsegna;

	private Date dataSpedizione;

	private Consegne consegna;

	private Integer numeroColli;

	private BigDecimal peso;

	private BigDecimal volume;

	private String note;

	private BigDecimal importoContrassegno;

	private ModalitaIncasso modalitaIncasso;

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriCreazioneEtichette() {
		super();
		consegna = Consegne.NESSUNA;
		modalitaIncasso = ModalitaIncasso.NESSUNA;
	}

	/**
	 * Crea i parametri ricerca in base all'ara magazzino specificata.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @param codicePagamento
	 *            codice di pagamento
	 * @param dataInizioTrasporto
	 *            data inizio trasporto
	 * 
	 */
	public ParametriCreazioneEtichette(final AreaMagazzino areaMagazzino, final CodicePagamento codicePagamento,
			final Date dataInizioTrasporto) {
		super();
		this.dataConsegna = areaMagazzino.getDatiSpedizioniDocumento().getDataConsegna();
		this.dataSpedizione = dataInizioTrasporto;
		this.numeroColli = areaMagazzino.getNumeroColli();
		this.peso = areaMagazzino.getPesoLordo();
		this.volume = areaMagazzino.getVolume();
		this.note = areaMagazzino.getDatiSpedizioniDocumento().getNoteSpedizione();

		if (areaMagazzino.getDatiSpedizioniDocumento().getImportoContrassegnoSpedizione() != null) {
			this.importoContrassegno = areaMagazzino.getDatiSpedizioniDocumento().getImportoContrassegnoSpedizione();
			this.consegna = areaMagazzino.getDatiSpedizioniDocumento().getConsegna();
		} else {
			if (codicePagamento != null && codicePagamento.isContrassegno()) {
				this.importoContrassegno = areaMagazzino.getDocumento().getTotale().getImportoInValutaAzienda();
				this.consegna = Consegne.PER_APPUNTAMENTO;
			}

		}
		this.modalitaIncasso = areaMagazzino.getDatiSpedizioniDocumento().getModalitaIncasso();
	}

	/**
	 * @return the consegna
	 */
	public Consegne getConsegna() {
		return consegna;
	}

	/**
	 * @return the dataConsegna
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return the dataSpedizione
	 */
	public Date getDataSpedizione() {
		return dataSpedizione;
	}

	/**
	 * @return the importoContrassegno
	 */
	public BigDecimal getImportoContrassegno() {
		return importoContrassegno;
	}

	/**
	 * @return the modalitaIncasso
	 */
	public ModalitaIncasso getModalitaIncasso() {
		return modalitaIncasso;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the numeroColli
	 */
	public Integer getNumeroColli() {
		return numeroColli;
	}

	/**
	 * @return the peso
	 */
	public BigDecimal getPeso() {
		return peso;
	}

	/**
	 * @return the volume
	 */
	public BigDecimal getVolume() {
		return volume;
	}

	/**
	 * @param consegna
	 *            the consegna to set
	 */
	public void setConsegna(Consegne consegna) {
		this.consegna = consegna;
	}

	/**
	 * @param dataConsegna
	 *            the dataConsegna to set
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataSpedizione
	 *            the dataSpedizione to set
	 */
	public void setDataSpedizione(Date dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	/**
	 * @param importoContrassegno
	 *            the importoContrassegno to set
	 */
	public void setImportoContrassegno(BigDecimal importoContrassegno) {
		this.importoContrassegno = importoContrassegno;
	}

	/**
	 * @param modalitaIncasso
	 *            the modalitaIncasso to set
	 */
	public void setModalitaIncasso(ModalitaIncasso modalitaIncasso) {
		this.modalitaIncasso = modalitaIncasso;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numeroColli
	 *            the numeroColli to set
	 */
	public void setNumeroColli(Integer numeroColli) {
		this.numeroColli = numeroColli;
	}

	/**
	 * @param peso
	 *            the peso to set
	 */
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
}
