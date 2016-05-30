package it.eurotn.panjea.partite.util;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.rate.domain.Rata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 
 * Classe instanziata da FactoryStrategiaDataScadenza che calcola le scadenze a partire da una data prendendo
 * l'intervallo su {@link RigaStrutturaPartite} . E' la classe che genera fisicamente le {@link Rata} Gestisce altresi'
 * i giorni di postscadenza, provenienti dall'entita' o dalla struttura partita
 * 
 * @author vittorio
 * @version 1.0, 01/lug/08
 * 
 */
public class StrategiaDataGGSolari extends StrategiaDataScadenza {
	private static Logger logger = Logger.getLogger(StrategiaDataGGSolari.class.getName());

	@Override
	public ArrayList<Rata> getScadenze(Date data, StrutturaPartita strutturaPartita, TipologiaPartita tipo) {
		logger.debug("--> Enter getScadenze");
		if (strutturaPartita.getRigheStrutturaPartita() == null) {
			return null;
		}
		ArrayList<Rata> scadenze = new ArrayList<Rata>();
		Calendar calendarInit = Calendar.getInstance();
		calendarInit.setTime(data);
		for (RigaStrutturaPartite rigaStrutturaPartite : strutturaPartita.getRigheStrutturaPartita()) {
			Calendar calendarTemp = (Calendar) calendarInit.clone();
			// gestione normale
			// npe mail
			if (rigaStrutturaPartite.getIntervallo() != null) {
				calendarTemp.add(Calendar.DAY_OF_MONTH, rigaStrutturaPartite.getIntervallo());
			}
			// giorno fisso

			if (strutturaPartita.getGiornoFisso() != null) {
				calendarTemp.set(Calendar.DAY_OF_MONTH, strutturaPartita.getGiornoFisso());
			}
			// gestione post scadenza
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
		logger.debug("--> Exit getScadenze");
		return scadenze;
	}

}
