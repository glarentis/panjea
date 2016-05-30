package it.eurotn.panjea.partite.util;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.rate.domain.Rata;

import java.util.ArrayList;
import java.util.Date;

public class StrategiaDataDaTabella extends StrategiaDataScadenza {

	@Override
	public ArrayList<Rata> getScadenze(Date data, StrutturaPartita strutturaPartita, TipologiaPartita tipo) {
		return null;
	}

}
