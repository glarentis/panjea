package it.eurotn.panjea.partite.util;

public class FactoryStrategiaDataScadenza {
	/**
	 * 0 - DATA_DOC_GG_SOLARI = dalla data del doc si aggiungono i gg di intervallo esistenti sulla riga struttura
	 * (Eventualmente mod da gg fisso)<br>
	 * 1 - DATA_DOC_GG_COMM = dalla data documento si aggiugono i gg di intervallo commerciali(30)<br>
	 * 2 - DATA_DOC_GG_COMM_FINE_MESE = dalla data documento si aggiugono i gg di intervallo commerciali(30) con data
	 * iniziali a fine mese<br>
	 * 3 - DATA_DOC_TABELLA date scadenza generate da una tabella aggiuntiva sulla struttura partita <br>
	 * 4 - SENZA_DATA genera rate senza data scadenza (es.versamento da ritenuta d'acconto)
	 */
	public static StrategiaDataScadenza getStrategia(Integer tipo) {
		switch (tipo) {
		case 0:
			return new StrategiaDataGGSolari();
		case 1:
			return new StrategiaDataGGCommerciali();
		case 2:
			return new StrategiaDataGGCommFineMese();
		default:
			throw new UnsupportedOperationException("Strategia data scadenza inesistente");
		}

	}
}
