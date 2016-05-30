package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public final class CombinationEffettiSum {

	private static void buildResult(Effetto[] num, int start, BigDecimal target, ArrayList<Effetto> current,
			ArrayList<ArrayList<Effetto>> result) {
		if (target.compareTo(BigDecimal.ZERO) == 0) {
			ArrayList<Effetto> temp = new ArrayList<Effetto>(current);
			result.add(temp);
			return;
		}

		for (int i = start; i < num.length; i++) {
			if (num[i].getImporto().getImportoInValutaAzienda().compareTo(target) > 0) {
				continue;
			}
			current.add(num[i]);
			buildResult(num, i + 1, target.subtract(num[i].getImporto().getImportoInValutaAzienda()), current, result);
			current.remove(current.size() - 1);
			while (i + 1 < num.length && num[i] == num[i + 1]) {
				i++;
			}
		}
	}

	/**
	 *
	 * @param effetti
	 *            array di numeri dove cercare
	 * @param valoreSomma
	 *            valore della somma da trovare
	 * @return risultato.
	 */
	public static ArrayList<ArrayList<Effetto>> cercaCombinazione(final Effetto[] effetti, final BigDecimal valoreSomma) {
		ArrayList<ArrayList<Effetto>> result = new ArrayList<ArrayList<Effetto>>();
		if (effetti == null || effetti.length == 0) {
			return result;
		}
		ArrayList<Effetto> current = new ArrayList<Effetto>();
		int start = 0;
		Arrays.sort(effetti, new Comparator<Effetto>() {

			@Override
			public int compare(Effetto a, Effetto b) {
				return a.getImporto().getImportoInValutaAzienda().compareTo(b.getImporto().getImportoInValutaAzienda());
			}
		});
		buildResult(effetti, start, valoreSomma, current, result);
		return result;
	}

	private CombinationEffettiSum() {

	}
}