package it.eurotn.panjea.partite.util;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.rate.domain.Rata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * Classe instanziata da FactoryStrategiaDataScadenza che calcola le scadenze a partire da una data prendendo
 * l'intervallo su {@link RigaStrutturaPartite} e portando i giorni al giorno fisso in RigaPartita. Questo giorno fisso
 * puo' provenire dall'entita di riferimento del documento E' la classe che genera fisicamente le {@link Rata} Gestisce
 * altresi' i giorni di postscadenza, provenienti dall'entita' o dalla struttura partita
 * 
 * @author vittorio
 * @version 1.0, 01/lug/08
 * 
 */
public class StrategiaDataGGCommFineMese extends StrategiaDataScadenza {

	@Override
	public ArrayList<Rata> getScadenze(Date data, StrutturaPartita strutturaPartita, TipologiaPartita tipo) {
		if (strutturaPartita.getRigheStrutturaPartita() == null) {
			return null;
		}
		ArrayList<Rata> scadenze = new ArrayList<Rata>();
		Calendar calendarInit = Calendar.getInstance();
		calendarInit.setTime(data);
		for (RigaStrutturaPartite rigaStrutturaPartite : strutturaPartita.getRigheStrutturaPartita()) {
			Calendar calendarTemp = (Calendar) calendarInit.clone();
			// gestione normale
			int mesi = rigaStrutturaPartite.getIntervallo() / 30;
			calendarTemp.add(Calendar.MONTH, mesi);
			int fineMese = calendarTemp.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendarTemp.set(Calendar.DAY_OF_MONTH, fineMese);
			// giorno fisso
			if (strutturaPartita.getGiornoFisso() != null) {
				calendarTemp.set(Calendar.DAY_OF_MONTH, strutturaPartita.getGiornoFisso());
			}
			if (strutturaPartita.getGgPostScadenza() != null) {
				calendarTemp.add(Calendar.DAY_OF_MONTH, strutturaPartita.getGgPostScadenza());
			}
			if (calendarTemp.getTime().before(data)) {
				// se e' precedente alla data del doc la scadenza e' la data doc
				calendarTemp.setTime(data);
			}
			Rata rata = new Rata();
			// set a null la proprietà rapportoBancarioAzienda perchè istanziata
			// sul costruttore.
			rata.setDataScadenza(calendarTemp.getTime());
			rata.setTipologiaPartita(tipo);
			rata.setTipoPagamento(strutturaPartita.getTipoPagamento());
			rata.setCategoriaRata(strutturaPartita.getCategoriaRata());
			scadenze.add(rata);
		}
		return scadenze;
	}

}
