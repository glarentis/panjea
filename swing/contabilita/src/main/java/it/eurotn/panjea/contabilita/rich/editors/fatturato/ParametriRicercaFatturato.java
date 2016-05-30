package it.eurotn.panjea.contabilita.rich.editors.fatturato;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Calendar;

import org.apache.log4j.Logger;

public class ParametriRicercaFatturato {

	public enum TipoEntitaFatturazione {
		CLIENTE, FORNITORE
	}

	private static Logger logger = Logger.getLogger(ParametriRicercaFatturato.class);

	private Integer annoCompetenza;

	private Periodo periodo;

	private TipoEntitaFatturazione tipoEntitaFatturazione;

	private boolean fatturatoPerSedi;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaFatturato() {
		super();
		this.annoCompetenza = Calendar.getInstance().get(Calendar.YEAR);
		this.tipoEntitaFatturazione = TipoEntitaFatturazione.CLIENTE;
		this.periodo = new Periodo();
		this.periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
		this.fatturatoPerSedi = false;
	}

	/**
	 * @return Returns the annoCompetenza.
	 */
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @return Returns the periodo.
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @return Returns the tipoEntitaFatturazione.
	 */
	public TipoEntitaFatturazione getTipoEntitaFatturazione() {
		return tipoEntitaFatturazione;
	}

	/**
	 * @return Returns the tipoEntitaFatturazione.
	 */
	public String getTipoEntitaFatturazioneString() {
		switch (tipoEntitaFatturazione) {
		case CLIENTE:
			return "C";
		case FORNITORE:
			return "F";
		default:
			logger.error("--> errore, tipo entità non prevista");
			return null;
		}
	}

	/**
	 * @return the fatturatoPerSedi
	 */
	public boolean isFatturatoPerSedi() {
		return fatturatoPerSedi;
	}

	/**
	 * @param annoCompetenza
	 *            The annoCompetenza to set.
	 */
	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	/**
	 * @param fatturatoPerSedi
	 *            the fatturatoPerSedi to set
	 */
	public void setFatturatoPerSedi(boolean fatturatoPerSedi) {
		this.fatturatoPerSedi = fatturatoPerSedi;
	}

	/**
	 * @param periodo
	 *            The periodo to set.
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/**
	 * @param tipoEntitaFatturazione
	 *            The tipoEntitaFatturazione to set.
	 */
	public void setTipoEntitaFatturazione(TipoEntitaFatturazione tipoEntitaFatturazione) {
		this.tipoEntitaFatturazione = tipoEntitaFatturazione;
	}

}
