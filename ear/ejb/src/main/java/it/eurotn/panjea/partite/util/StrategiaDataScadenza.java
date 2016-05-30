package it.eurotn.panjea.partite.util;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.rate.domain.Rata;

import java.util.ArrayList;
import java.util.Date;

public abstract class StrategiaDataScadenza {
	/**
	 * Restituisce la Lista delle Rate generate appropriatamente secondo le varie Strategie
	 * 
	 * @param data
	 * @param strutturaPartita
	 * @param tipo
	 * @return lista delle rate generate
	 */
	public abstract ArrayList<Rata> getScadenze(Date data, StrutturaPartita strutturaPartita, TipologiaPartita tipo);

}
