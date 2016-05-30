package it.eurotn.panjea.tesoreria.domain;

import java.util.Comparator;

public class DataScadenzaEffettoComparator implements Comparator<Effetto> {

	@Override
	public int compare(Effetto effetto1, Effetto effetto2) {
		if (effetto1.getDataScadenza() != null && effetto2.getDataScadenza() != null) {
			return effetto1.getDataScadenza().compareTo(effetto2.getDataScadenza());
		}
		return 0;
	}

}
