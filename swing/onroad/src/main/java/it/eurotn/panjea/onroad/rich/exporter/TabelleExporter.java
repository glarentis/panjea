package it.eurotn.panjea.onroad.rich.exporter;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;

public class TabelleExporter extends AtonExporter {

	/**
	 *
	 * @param onRoadBD
	 *            bd
	 * @param exportLabelTabelle
	 *            label dell'esportazione tabelle
	 * @param exportLabelIva
	 *            label dell'esportazione iva
	 * @param exportLabelPagamenti
	 *            label dell'esportazione pagamenti
	 */
	public TabelleExporter(final IOnRoadBD onRoadBD, final ExportLabel exportLabelTabelle,
			final ExportLabel exportLabelIva, final ExportLabel exportLabelPagamenti) {
		super(onRoadBD, exportLabelTabelle, exportLabelIva, exportLabelPagamenti);
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (exportLabels[0].isChecked()) {
			onRoadBD.esportaTabelle();
		}

		// non esporto, non c'e'
		// onRoadBD.esportaUm();
		// onRoadBD.esportaUtenti();

		if (exportLabels[1].isChecked()) {
			onRoadBD.esportaCodiciIva();
		}

		if (exportLabels[2].isChecked()) {
			onRoadBD.esportaCodiciPagamento();
		}
		return null;
	}

}
