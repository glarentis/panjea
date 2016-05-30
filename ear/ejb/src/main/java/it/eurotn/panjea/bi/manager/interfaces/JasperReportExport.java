package it.eurotn.panjea.bi.manager.interfaces;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;

public interface JasperReportExport {

	/**
	 *
	 * @param analisiBi
	 *            analisiDa esportare
	 * @param template
	 *            template da esportare
	 * @return jrxml del report
	 */
	String creaJrxml(AnalisiBi analisiBi, String template);

	/**
	 *
	 * @param dashBoard
	 *            dashBoard esportare
	 * @param template
	 *            template da esportare
	 * @return jrxml del report
	 */
	String creaJrxml(DashBoard dashBoard, String template);

}