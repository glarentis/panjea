package it.eurotn.panjea.rate.util;

import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.rate.domain.CalendarioRate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RiepilogoCalendariRateEntita implements Serializable {

	private static final long serialVersionUID = 679893719275259163L;

	private Map<CategoriaEntita, CalendarioRate> calendariiCategorieEntita;

	private CalendarioRate calendarioEntita;

	private Map<SedeEntita, CalendarioRate> calendariSediEntita;

	/**
	 * Costruttore.
	 * 
	 */
	public RiepilogoCalendariRateEntita() {
		super();
		this.calendariiCategorieEntita = new HashMap<CategoriaEntita, CalendarioRate>();
		this.calendarioEntita = null;
		this.calendariSediEntita = new HashMap<SedeEntita, CalendarioRate>();
	}

	/**
	 * @return the calendariiCategorieEntita
	 */
	public Map<CategoriaEntita, CalendarioRate> getCalendariiCategorieEntita() {
		return calendariiCategorieEntita;
	}

	/**
	 * @return the calendarioEntita
	 */
	public CalendarioRate getCalendarioEntita() {
		return calendarioEntita;
	}

	/**
	 * @return the calendariSediEntita
	 */
	public Map<SedeEntita, CalendarioRate> getCalendariSediEntita() {
		return calendariSediEntita;
	}

	/**
	 * @param calendarioEntita
	 *            the calendarioEntita to set
	 */
	public void setCalendarioEntita(CalendarioRate calendarioEntita) {
		this.calendarioEntita = calendarioEntita;
	}
}
