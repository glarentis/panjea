package it.eurotn.panjea.rate.rich.forms.calendarirate;

import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.rate.domain.CalendarioRate;

import java.util.List;

public class RepilogoCalendariRateAziendaForm extends RiepilogoCalendariRateForm {

	@Override
	protected List<CalendarioRate> getCalendariRate(Object object) {

		List<CalendarioRate> result = null;

		if (object instanceof AziendaAnagraficaDTO) {

			result = calendariRateBD.caricaCalendariRateAzienda();
		}

		return result;
	}
}
