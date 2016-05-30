package it.eurotn.panjea.ordini.rich.editors.produzione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoProduzione;
import it.eurotn.panjea.ordini.rich.editors.evasione.StatoRigaDistintaCaricoCellRenderer;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class ProduzioneTableModel extends DefaultBeanTableModel<RigaDistintaCaricoProduzione> {

	private static final long serialVersionUID = -127103737438238995L;

	private static final ConverterContext NUMBERQTACONVERSIONCONTEXT = new NumberWithDecimalConverterContext();
	private static final EditorContext NUMBERQTAEDITORCONTEXT = new EditorContext("numberQtaEditorContext");

	{
		NUMBERQTACONVERSIONCONTEXT.setUserObject(6);
		NUMBERQTAEDITORCONTEXT.setUserObject(6);
	}

	/**
	 * Costruttore.
	 */
	public ProduzioneTableModel() {
		super(RisultatiRicercaProduzioneTablePage.PAGE_ID, new String[] { "rigaArticolo.areaOrdine",
				"rigaArticolo.areaOrdine.documento.tipoDocumento", "dataProduzione", "dataConsegna", "articolo",
				"qtaOrdinata", "qtaEvasa", "qtaDaEvadere", "qtaResidua", "forzata", "rigaOrdineCollegata.areaOrdine",
				"rigaOrdineCollegata.areaOrdine.documento.tipoDocumento",
				"rigaOrdineCollegata.areaOrdine.documento.entita", "rigaOrdineCollegata.articolo" },
				RigaDistintaCaricoProduzione.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 5:
		case 6:
		case 7:
		case 8:
			return NUMBERQTACONVERSIONCONTEXT;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		if (row == -1) {
			return StatoRigaDistintaCaricoCellRenderer.STATO_RIGA_DISTINTA_CARICO_CONTEXT;
		}
		switch (column) {
		case 5:
		case 6:
		case 7:
		case 8:
			return NUMBERQTAEDITORCONTEXT;
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 7:
		case 9:
			return true;
		default:
			return false;
		}
	}
}
