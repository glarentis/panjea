package it.eurotn.panjea.beniammortizzabili.rich.editors.beni.bene;

import it.eurotn.panjea.anagrafica.rich.table.renderer.NoteCellRenderer;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamento;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class QuoteFiscaliTableModel extends DefaultBeanTableModel<QuotaAmmortamentoFiscale> {

	public static final String MODEL_ID = "quoteFiscaliTableModel";

	private static final long serialVersionUID = -1837270695807825080L;

	private static ConverterContext numberPrezzoContext;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
	}

	/**
	 * Costruttore.
	 *
	 */
	public QuoteFiscaliTableModel() {
		super(MODEL_ID, new String[] { QuotaAmmortamento.PROP_ANNO_SOLARE_AMMORTAMENTO,
				"importoSoggettoAmmortamentoBene", "percQuotaAmmortamentoOrdinarioApplicata",
				QuotaAmmortamento.PROP_IMP_QUOTA_AMMORTAMENTO_ORDINARIO, "percQuotaAmmortamentoAnticipatoApplicata",
				QuotaAmmortamentoFiscale.PROP_IMP_QUOTA_AMMORTAMENTO_ANTICIPATO,
				QuotaAmmortamentoFiscale.PROP_PERC_QUOTA_AMMORTAMENTO_ACCELERATO,
				QuotaAmmortamentoFiscale.PROP_PERC_QUOTA_AMMORTAMENTO_RIDOTTO,
				QuotaAmmortamentoFiscale.PROP_IMP_QUOTA_AMMORTAMENTO_ACCELERATO, QuotaAmmortamento.PROP_ANNOTAZIONI,
		"totaleAnno" }, QuotaAmmortamentoFiscale.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 10:
			return numberPrezzoContext;
		default:
			break;
		}
		return super.getConverterContextAt(i, j);
	}

	@Override
	public EditorContext getEditorContextAt(int i, int j) {

		switch (j) {
		case 2:
			return PercApplicataCellRenderer.PERC_ORDINARIO_APPLICATA_CONTEXT;
		case 4:
			return PercApplicataCellRenderer.PERC_ANTICIPATO_APPLICATA_CONTEXT;
		case 9:
			return NoteCellRenderer.NOTE_CONTEXT;
		default:
			break;
		}
		return super.getEditorContextAt(i, j);
	}

}
