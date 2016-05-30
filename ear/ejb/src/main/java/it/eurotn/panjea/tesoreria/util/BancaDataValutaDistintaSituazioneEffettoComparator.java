package it.eurotn.panjea.tesoreria.util;

import java.util.Comparator;

public final class BancaDataValutaDistintaSituazioneEffettoComparator implements Comparator<SituazioneEffetto> {

	@Override
	public int compare(SituazioneEffetto o1, SituazioneEffetto o2) {

		if (o1.getRapportoBancario().compareTo(o2.getRapportoBancario()) != 0) {
			return o1.getRapportoBancario().compareTo(o2.getRapportoBancario());
		} else {
			if (o1.getDataValutaEffetto().compareTo(o2.getDataValutaEffetto()) != 0) {
				return o1.getDataValutaEffetto().compareTo(o2.getDataValutaEffetto());
			} else {
				return o1.getAreaEffetto().compareTo(o2.getAreaEffetto());
			}
		}
	}
}