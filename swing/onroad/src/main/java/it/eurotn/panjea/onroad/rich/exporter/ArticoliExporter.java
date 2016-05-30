package it.eurotn.panjea.onroad.rich.exporter;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;

public class ArticoliExporter extends AtonExporter {

	/**
	 *
	 * @param onRoadBD
	 *            bd
	 * @param exportLabelArticoli
	 *            label dell'esportazione articoli
	 * @param exportLabelAttributi
	 *            label dell'esportazione attributi
	 */
	public ArticoliExporter(final IOnRoadBD onRoadBD, final ExportLabel exportLabelArticoli,
			final ExportLabel exportLabelAttributi) {
		super(onRoadBD, exportLabelArticoli, exportLabelAttributi);
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (exportLabels[0].isChecked()) {
			onRoadBD.esportaArticoli();
		}

		if (exportLabels[1].isChecked()) {
			onRoadBD.esportaAttributiArticoli();
		}
		return null;
	}

}
