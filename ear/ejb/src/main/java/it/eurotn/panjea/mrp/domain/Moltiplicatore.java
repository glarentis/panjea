package it.eurotn.panjea.mrp.domain;

import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.exception.FormulaException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

public class Moltiplicatore {

	private static Logger logger = Logger.getLogger(Moltiplicatore.class);

	private ScriptEngine engine = null;
	private ScriptEngineManager scriptManager = null;
	private MathContext mathContext = null;

	/**
	 * 
	 * @param formula
	 *            formula da calcolare
	 * @param qta
	 *            valore per la variabile qta
	 * @param numDecimali
	 *            decimali del risultato
	 * @param variabili
	 *            le variabili per l'evaluation della formula
	 * @return valore del moltiplicatore (con la qta già moltiplicata)
	 */
	public BigDecimal calcola(String formula, BigDecimal qta, int numDecimali, Map<String, Object> variabili) {
		BigDecimal result = BigDecimal.ZERO;
		if (mathContext == null || mathContext.getPrecision() != numDecimali) {
			mathContext = new MathContext(numDecimali);
		}

		if (formula == null) {
			throw new FormulaException("", "Formula mancante");
		}
		String formulaTmp = formula.replace("*", "");
		formulaTmp = formulaTmp.replace("result", "");
		formulaTmp = formulaTmp.replace("=", "");
		formulaTmp = formulaTmp.replace(",", ".");
		formulaTmp = formulaTmp.trim();

		Number qtaRiga = (Number) variabili.get(TipoAttributo.ATTRIBUTO_QTA_CODICE_FORMULA);
		if (qtaRiga != null && qtaRiga instanceof Double) {
			qtaRiga = BigDecimal.valueOf((Double) qtaRiga);
		}

		if (formulaTmp.equals(TipoAttributo.ATTRIBUTO_QTA_CODICE_FORMULA)) {
			// Se la formula contiene solo la variabile $qta$ torno la qta
			result = (BigDecimal) qtaRiga;
		} else {
			formulaTmp = formulaTmp.replace(TipoAttributo.ATTRIBUTO_QTA_CODICE_FORMULA, "");
		}

		if (formulaTmp.isEmpty()) {
			result = BigDecimal.ZERO;
		} else if (NumberUtils.isNumber(formulaTmp)) {
			result = new BigDecimal(formulaTmp);
			// Se è un numero ma inizia con = allora non moltiplico per la qta
			if (!formula.startsWith("=")) {
				result = result.multiply((BigDecimal) qtaRiga);
			}
		} else {
			// Se la formula contiene la qta e il valore del padre è zero..il risultato è zero
			// Prevengo le somme...se qta*5 può andare bene la somma qta+5 deve dare zero se la qta è zero.
			if (formula.contains(TipoAttributo.ATTRIBUTO_QTA_CODICE_FORMULA) && qta != null
					&& qta.compareTo(BigDecimal.ZERO) == 0) {
				return BigDecimal.ZERO;
			}

			result = calcolaMoltiplicatore(formula, variabili);
		}
		// imposto la precisione solo alla fine altrimenti
		result = result.setScale(numDecimali, RoundingMode.HALF_UP);
		return result;
	}

	/**
	 * Calcola la variabile come moltiplicatore o come formula.
	 * 
	 * @param formula
	 *            formula/moltiplicatore
	 * @param qta
	 *            qta
	 * @param numDecimali
	 *            numDecimali
	 * @param codiciAttributi
	 *            codiciAttributi
	 * @param valoriAttributi
	 *            valoriAttributi
	 * @return BigDecimal
	 */
	public BigDecimal calcola(String formula, BigDecimal qta, int numDecimali, String codiciAttributi,
			String valoriAttributi) {
		Map<String, Object> variabili = new HashMap<String, Object>();
		if (codiciAttributi != null) {
			String[] codiciAttributiSplit = codiciAttributi.split(",");
			String[] valoriAttributiSplit = ObjectUtils.defaultIfNull(valoriAttributi, "").split(",");
			for (int i = 0; i < codiciAttributiSplit.length; i++) {
				if (NumberUtils.isNumber(valoriAttributiSplit[i])) {
					variabili.put(TipoAttributo.SEPARATORE_CODICE_FORMULA + codiciAttributiSplit[i]
							+ TipoAttributo.SEPARATORE_CODICE_FORMULA, new BigDecimal(valoriAttributiSplit[i]));
				}
			}
		}
		variabili.put(TipoAttributo.ATTRIBUTO_QTA_CODICE_FORMULA, qta);
		logger.debug("formula " + formula + ", qta " + qta);
		return calcola(formula, qta, numDecimali, variabili);
	}

	/**
	 * Calcola la variabile come moltiplicatore o come formula.
	 * 
	 * @param formula
	 *            formula/moltiplicatore
	 * @param variabili
	 *            le variabili per l'evaluation della formula
	 * @return BigDecimal
	 */
	private BigDecimal calcolaMoltiplicatore(String formula, Map<String, Object> variabili) {
		// se la formula non contiene la variabile "result" la aggiungo
		if (!formula.contains("result")) {
			formula = "result = " + formula;
		}

		if (engine == null) {
			scriptManager = new ScriptEngineManager();
			engine = scriptManager.getEngineByName("js");
		}

		Bindings variabiliFormula = engine.createBindings();

		BigDecimal result = BigDecimal.ZERO;
		variabiliFormula.put("result", result);
		variabiliFormula.putAll(variabili);

		try {
			engine.eval(formula, variabiliFormula);
			Object resultObject = variabiliFormula.get("result");
			logger.debug("--> Exit calcola con risultato " + resultObject);

			if (((Number) resultObject).toString().equals("Infinity")) {
				throw new FormulaException(formula, "Divisione by zero");
			}
			if (resultObject instanceof BigDecimal) {
				result = ((BigDecimal) resultObject);
			} else if (resultObject instanceof Double) {
				result = BigDecimal.valueOf((Double) resultObject);
			} else if (resultObject instanceof Integer) {
				result = new BigDecimal((Integer) resultObject);
			} else if (resultObject instanceof Long) {
				result = BigDecimal.valueOf((Long) resultObject);
			}
		} catch (ScriptException se) {
			throw new FormulaException(formula, se.getMessage());
		} catch (Exception e) {
			logger.error("-->errore nel calcolare la formula ", e);
			throw new RuntimeException("errore nel calcolare la formula", e);
		}
		return result;
	}
}
