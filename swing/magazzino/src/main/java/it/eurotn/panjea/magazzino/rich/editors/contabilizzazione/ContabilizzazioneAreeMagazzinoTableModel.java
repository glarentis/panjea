package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.magazzino.rich.editors.areamagazzino.RisultatiRicercaAreaMagazzinoTablePage;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class ContabilizzazioneAreeMagazzinoTableModel extends DefaultBeanTableModel<AreaMagazzinoRicerca> {

	private static final long serialVersionUID = -6971923108008422202L;
	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	{
		TOTALE_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public ContabilizzazioneAreeMagazzinoTableModel() {
		super(RisultatiRicercaAreaMagazzinoTablePage.PAGE_ID, new String[] { "datiGenerazione", "documento.codice",
				"documento.dataDocumento", "documento.tipoDocumento", "documento.totale.importoInValutaAzienda",
				"entitaDocumento", "depositoOrigine", "depositoDestinazione", "dataRegistrazione", "stato", "note" },
				AreaMagazzinoRicerca.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 4:
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}
}