/**
 *
 */
package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.contabilita.manager.interfaces.FormuleManager;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Manager per la gestione delle formule roles: accessoUtente.
 *
 * @author fattazzo
 * @version 1.0, 07/set/07
 */
@Stateless(name = "Panjea.FormuleManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FormuleManager")
public class FormuleManagerBean implements FormuleManager {

	/**
	 *
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum TipoVariabiliFormula {
		STRUTTURA_CONTABILE, CONTRO_PARTITA
	};

	@Override
	public BigDecimal calcola(String formula, Map<String, BigDecimal> map, int nDecimali) throws FormulaException {

		BigDecimal result = BigDecimal.ZERO;

		if (formula != null && formula.trim().length() > 0) {
			// controllo se la formula contiene la variabile "result"
			if (!formula.contains("result")) {
				formula = "result = " + formula;
			}

			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");

			engine.put("result", BigDecimal.ZERO);

			Set<String> keyMap = map.keySet();

			for (String key : keyMap) {
				engine.put(key, map.get(key));
			}

			try {
				engine.eval(formula);
				Object tmp = engine.get("result");
				if (tmp instanceof BigDecimal) {
					result = (BigDecimal) tmp;
				} else {
					if (tmp instanceof Double) {
						result = BigDecimal.valueOf((Double) tmp);
					} else {
						if (tmp instanceof Integer) {
							result = new BigDecimal((Integer) tmp);
						}
					}
				}
				result = result.setScale(nDecimali, RoundingMode.HALF_UP);
			} catch (ScriptException se) {
				throw new FormulaException(se.getMessage());
			}
		}
		return result;
	}
}
