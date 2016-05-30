package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.model;

import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.util.Comparator;

@Deprecated
public class DataScadenzaEffettoComparator implements Comparator<Effetto> {

	@Override
	public int compare(Effetto effetto1, Effetto effetto2) {
		return effetto1.getPagamenti().iterator().next().getRata().getDataScadenza()
				.compareTo(effetto2.getPagamenti().iterator().next().getRata().getDataScadenza());
	}

}
