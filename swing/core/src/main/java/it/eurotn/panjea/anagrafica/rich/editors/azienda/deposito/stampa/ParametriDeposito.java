package it.eurotn.panjea.anagrafica.rich.editors.azienda.deposito.stampa;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

public class ParametriDeposito {

	private String report;
	private Periodo periodo;

	{
		periodo = new Periodo();
		periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
	}

	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @return the report
	 */
	public String getReport() {
		return report;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(String report) {
		this.report = report;
	}

}
