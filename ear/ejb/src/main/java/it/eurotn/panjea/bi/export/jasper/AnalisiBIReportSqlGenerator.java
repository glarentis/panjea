package it.eurotn.panjea.bi.export.jasper;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;
import it.eurotn.panjea.bi.domain.analisi.sql.AnalisiBISqlGenerator;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColonnaFunzione;

import org.apache.log4j.Logger;

/**
 * Classe incaricata di recuperare i dati dal datawarehouse e riempire il {@link DataWarehouseModel} .
 *
 * @author giangi
 *
 */
public class AnalisiBIReportSqlGenerator extends AnalisiBISqlGenerator {

	private AnalisiBi analisiBi;

	private static Logger logger = Logger.getLogger(AnalisiBIReportSqlGenerator.class);

	/**
	 *
	 * @param analisiBi
	 *            analisi
	 */
	public AnalisiBIReportSqlGenerator(final AnalisiBi analisiBi) {
		super(analisiBi);
		this.analisiBi = analisiBi;
	}

	@Override
	protected StringBuilder createSqlWhere(Colonna colonna, String aliasTabellaFatti) {
		logger.debug("--> Enter createSqlWhere");
		StringBuilder sbWhere = new StringBuilder(500);
		String nomeInputControl = analisiBi.getInputControl(colonna.getNome());
		AnalisiValueSelected filtro = analisiBi.getFiltri().get(colonna.getKey());
		if (!nomeInputControl.isEmpty()) {
			// Devo aggiungere il filtro come parametro nella query
			sbWhere.append(" AND $X{IN,");
			if (!(colonna instanceof ColonnaFunzione)) {
				sbWhere.append(colonna.getTabella().getAlias());
				sbWhere.append(".");
			}
			sbWhere.append(colonna.getNome());
			sbWhere.append(",");
			sbWhere.append(nomeInputControl);
			sbWhere.append("}");
		} else if (filtro != null) {
			sbWhere = new StringBuilder(500);
			Object[] fitriColonna = filtro.getParameter();
			if (fitriColonna != null && fitriColonna.length > 0) {
				sbWhere = super.createSqlWhere(colonna, aliasTabellaFatti);
			}
		}
		logger.debug("--> Exit createSqlWhere");
		return sbWhere;
	}
}
