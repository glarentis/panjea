/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;

/**
 * {@link Constraint} per la validazione del numero attributi configurati per ogni riga. Il numero di attributi massimi
 * per ogni riga è 3.
 * 
 * @author gfattarsi
 * 
 */
public class NumeroAttributiRigaConstraint extends TypeResolvableConstraint {

	/**
	 * Costruttore.
	 * 
	 */
	public NumeroAttributiRigaConstraint() {
		super("numeroAttributiRigaConstraint");
	}

	@Override
	public boolean test(Object object) {
		if (object instanceof Collection) {

			if (((Collection<?>) object).size() == 0) {
				return true;
			} else {
				boolean isValid = Boolean.TRUE;

				Map<Integer, Integer> rowsMap = new HashMap<Integer, Integer>();

				@SuppressWarnings("unchecked")
				Collection<AttributoRigaArticolo> attributi = (Collection<AttributoRigaArticolo>) object;

				// costruisco la mappa che conterrà come chiave il numero della riga e come valore il numero di
				// attributi configurati per la riga.
				for (AttributoRigaArticolo attributoRiga : attributi) {

					Integer nrAttributi = rowsMap.get(attributoRiga.getRiga());

					if (nrAttributi == null) {
						nrAttributi = 1;
					} else {
						nrAttributi++;
					}

					rowsMap.put(attributoRiga.getRiga(), nrAttributi);
				}

				// verifico che nessuna riga abbia più di 3 attributi
				for (Entry<Integer, Integer> entry : rowsMap.entrySet()) {

					if (entry.getValue().intValue() > 3) {
						isValid = false;
						break;
					}
				}

				return isValid;
			}
		}

		return false;
	}

}
