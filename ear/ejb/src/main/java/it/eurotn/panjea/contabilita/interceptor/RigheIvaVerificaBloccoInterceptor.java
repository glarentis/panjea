/**
 *
 */
package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fattazzo
 */
public class RigheIvaVerificaBloccoInterceptor extends DocumentoContabileVerificaBloccoInterceptor {

	@Override
	protected Date getDataBlocco() {
		ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();
		return contabilitaSettings.getDataBloccoIva();
	}

	@Override
	protected List<Date> getDateRegistrazioniAreeContabili(Object arg) {
		List<Date> areeContabili = new ArrayList<Date>();

		if (arg instanceof RigaIva) {
			AreaIva areaIva = ((RigaIva) arg).getAreaIva();
			Date dataRegAreaCont = caricaDataRegistrazioneByDocumento(areaIva.getDocumento().getId());
			areeContabili.add(dataRegAreaCont);
		} else {
			List<Date> dateRegistrazioniDocumenti = super.getDateRegistrazioniAreeContabili(arg);
			areeContabili.addAll(dateRegistrazioniDocumenti);
		}

		return areeContabili;
	}
}
