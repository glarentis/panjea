package it.eurotn.panjea.stampe.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

public final class FormulaNumeroCopieCalculator {

	/**
	 * Calcola il numero delle copie in base alla forluma e ai valori di riferimento.
	 *
	 * @param formula
	 *            formula da calcolare
	 * @param variabili
	 *            variabili di calcolo
	 * @return numero copie
	 */
	public static int calcolaNumeroCopie(String formula, Map<String, Object> variabili) {
		BigDecimal result = null;

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");

		Bindings variabiliFormula;
		variabiliFormula = engine.createBindings();
		variabiliFormula.put("result", result);

		if (StringUtils.isBlank(formula)) {
			formula = "1";
		}

		// controllo se la formula contiene la variabile "result"
		if (!formula.contains("result")) {
			formula = "result = " + formula;
		}

		// tolgo tutti gli spazi dalle varibili altrimenti engine.eval solleva una eccezione. Gli spazi sono stati
		// utilizzati solo per avere dei nomi variabili più chiari per l'utente
		Map<String, Object> variabiliParse = new HashMap<String, Object>();
		for (Entry<String, Object> entry : variabili.entrySet()) {
			variabiliParse.put(entry.getKey().replaceAll(" ", ""), entry.getValue());
		}
		formula = formula.replaceAll(" ", "");

		variabiliFormula.putAll(variabiliParse);
		try {
			engine.eval(formula, variabiliFormula);
			Object resultObject = variabiliFormula.get("result");
			if (resultObject instanceof BigDecimal) {
				result = ((BigDecimal) resultObject).setScale(0, RoundingMode.HALF_UP);
			} else {
				if (resultObject instanceof Double) {
					result = BigDecimal.valueOf((Double) resultObject).setScale(0, RoundingMode.HALF_UP);
				} else {
					if (resultObject instanceof Integer) {
						result = new BigDecimal((Integer) resultObject).setScale(0, RoundingMode.HALF_UP);
					}
				}
			}

		} catch (ScriptException se) {
			throw new RuntimeException(se.getMessage());
		}

		// se non avessi trovato un numero copie o se questo fosse < 1 imposto il numero copie a 1
		if (result == null || result.compareTo(BigDecimal.ZERO) <= 0) {
			result = BigDecimal.ONE;
		}

		return result.intValue();
	}

	/**
	 * Costruttore.
	 */
	private FormulaNumeroCopieCalculator() {
		super();
	}
}
