/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author fattazzo
 *
 */
@Entity
@Table(name = "para_situazione_ritenute_acconto")
public class ParametriSituazioneRitenuteAcconto extends AbstractParametriRicerca implements Serializable {

	private static final long serialVersionUID = 4960707599503389920L;

	private Periodo periodo;

	private FornitoreLite fornitore;

	{
		periodo = new Periodo();
		periodo.setTipoPeriodo(TipoPeriodo.MESE_PRECEDENTE);
		fornitore = null;
	}

	/**
	 * @return the fornitore
	 */
	public FornitoreLite getFornitore() {
		return fornitore;
	}

	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @param fornitore
	 *            the fornitore to set
	 */
	public void setFornitore(FornitoreLite fornitore) {
		this.fornitore = fornitore;
	}

	/**
	 * @param periodo
	 *            the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}
