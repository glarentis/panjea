/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import org.springframework.binding.value.ValueModel;

/**
 * Property change definito per le forms dei parametri ricerca da aggiungere alla proprieta' anno di competenza che data
 * una lista di ValueModel contenenti un tipo Date esegue le operazioni necessarie per allineare con il nuovo anno di
 * competenza i valori anno dei campi data specificati.
 * 
 * @author Leonardo
 */
public class PeriodoParametriChangeListener implements PropertyChangeListener {

	private ValueModel periodoValueModel = null;

	/**
	 * Costruttore.
	 * 
	 */
	public PeriodoParametriChangeListener() {
		super();
	}

	/**
	 * Metodo che intercetta il cambio del valore della proprieta' annoCompetenza e di conseguenza scorre la lista di
	 * valueModel aggiornando l'anno di ogni Date con il newValue ricevuto.
	 * 
	 * @param evt
	 *            evento
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// se in qlc caso l'evento e' null esco
		if (evt == null) {
			return;
		}
		// se non ho inizializzato la lista aggiungendo un value model di cui modificare
		// il valore lancio una eccezione
		if (periodoValueModel == null) {
			throw new IllegalStateException(
					"Non e' stato aggiunto nessun value model di tipo data per questo propertyChangeListener");
		}
		// l'anno di competenza e' di norma un Integer ma nel caso del controllo documenti e' una String
		Integer annoCompetenza = null;
		Object newValue = evt.getNewValue();

		// caso di newValue Integer
		if (newValue instanceof Integer) {
			annoCompetenza = (Integer) newValue;
		} else if (newValue instanceof String && newValue != null && !((String) newValue).equals("")) {
			// caso di newValue String
			annoCompetenza = new Integer((String) newValue);
		}

		// controlli sul valore integer di annoCompetenza, non considero valori null,o minori di 999
		if (annoCompetenza == null || (annoCompetenza != null && annoCompetenza.intValue() <= 999)) {
			return;
		}
		// cambio l'anno prendendo come valore l'anno di competenza
		if (!(periodoValueModel.getValue() instanceof Periodo)) {
			throw new IllegalStateException("Il valueModel non racchiude un valore di tipo periodo");
		}

		Periodo periodo = (Periodo) PanjeaSwingUtil.cloneObject(periodoValueModel.getValue());
		if (periodo.getTipoPeriodo() == TipoPeriodo.DATE) {
			if (periodo.getDataIniziale() != null) {
				Date date = periodo.getDataIniziale();
				if (date != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.set(Calendar.YEAR, annoCompetenza.intValue());
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					periodo.setDataIniziale(cal.getTime());
				}
			}
			if (periodo.getDataFinale() != null) {
				Date date = periodo.getDataFinale();
				if (date != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.set(Calendar.YEAR, annoCompetenza.intValue());
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					periodo.setDataFinale(cal.getTime());
				}
			}
			periodoValueModel.setValue(periodo);
		}
	}

	/**
	 * @param periodoValueModel
	 *            The periodoValueModel to set.
	 */
	public void setPeriodoValueModel(ValueModel periodoValueModel) {
		this.periodoValueModel = periodoValueModel;
	}

}
