package it.eurotn.panjea.stampe.util;

import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;

/**
 * @author leonardo
 */
public final class LayoutStampaFactory {

	/**
	 * Crea una istanza di LayoutStampa o LayoutStampaDocumento.
	 *
	 * @param tipoAreaDocumento
	 *            tipoAreaDocumento
	 * @param reportName
	 *            reportName
	 * @param entita
	 *            entita
	 * @param sedeEntita
	 *            sedeEntita
	 * @return LayoutStampa
	 */
	public static LayoutStampa create(ITipoAreaDocumento tipoAreaDocumento, String reportName, EntitaLite entita,
			SedeEntita sedeEntita) {
		LayoutStampa layoutStampa = new LayoutStampa();
		if (tipoAreaDocumento != null) {
			layoutStampa = new LayoutStampaDocumento(tipoAreaDocumento, entita, sedeEntita);

			// nel caso di un tipo area magazzino setto il numero copie in base al numero copie del documento + numero copie del vettore
			if(tipoAreaDocumento instanceof TipoAreaMagazzino) {
				String formulaNumeroCopie = "$Copie tipo documento$ + $Copie vettore$";
				layoutStampa.setFormulaNumeroCopie(formulaNumeroCopie);
			}
		}
		layoutStampa.setReportName(reportName);
		return layoutStampa;
	}

	/**
	 * Costruttore.
	 */
	private LayoutStampaFactory() {
		super();
	}

}
