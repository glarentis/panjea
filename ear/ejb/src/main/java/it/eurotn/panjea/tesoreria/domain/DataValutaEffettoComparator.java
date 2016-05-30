package it.eurotn.panjea.tesoreria.domain;

import java.util.Comparator;

public class DataValutaEffettoComparator implements Comparator<Effetto> {

	@Override
	public int compare(Effetto effetto1, Effetto effetto2) {
		return effetto1.getDataValuta().compareTo(effetto2.getDataValuta());
	}

}
