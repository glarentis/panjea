package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.model;

import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.util.Comparator;

@Deprecated
public class DataValutaEffettoComparator implements Comparator<Effetto> {

	@Override
	public int compare(Effetto effetto1, Effetto effetto2) {
		return effetto1.getDataValuta().compareTo(effetto2.getDataValuta());
	}

}
