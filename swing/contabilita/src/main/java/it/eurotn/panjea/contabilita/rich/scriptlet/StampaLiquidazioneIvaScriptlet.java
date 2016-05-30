package it.eurotn.panjea.contabilita.rich.scriptlet;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import org.apache.log4j.Logger;

/**
 * Bean che rappresenta il report della stampa registro iva, descrive il comportamento in particolari momenti del
 * report.
 * 
 * @author Leonardo
 */
public class StampaLiquidazioneIvaScriptlet extends JRDefaultScriptlet {

	private static Logger logger = Logger.getLogger(StampaLiquidazioneIvaScriptlet.class);
	private Integer ultimoNumeroPagina = null;

	/**
	 * Costruttore.
	 * 
	 */
	public StampaLiquidazioneIvaScriptlet() {
		super();
		this.ultimoNumeroPagina = 0;
	}

	@Override
	public void afterDetailEval() throws JRScriptletException {
		super.afterDetailEval();
		logger.debug("--> Enter afterDetailEval");
		Integer paginaCorrente = (Integer) getVariableValue("paginaCorrente");
		this.ultimoNumeroPagina = paginaCorrente;
		logger.debug("--> Exit afterDetailEval");
	}

	@Override
	public void beforeReportInit() throws JRScriptletException {
		super.beforeReportInit();
	}

	/**
	 * @return ultimo numero pagina del report
	 */
	public Integer getUltimoNumeroPagina() {
		return ultimoNumeroPagina;
	}

	/**
	 * 
	 * @param ultimoNumeroPagina
	 *            the ultimoNumeroPagina to set
	 */
	public void setUltimoNumeroPagina(Integer ultimoNumeroPagina) {
		this.ultimoNumeroPagina = ultimoNumeroPagina;
	}

}
