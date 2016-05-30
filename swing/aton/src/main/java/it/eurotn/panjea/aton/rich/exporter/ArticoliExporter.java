package it.eurotn.panjea.aton.rich.exporter;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;

public class ArticoliExporter extends AtonExporter {

	/**
	 *
	 * @param atonBD
	 *            bd
	 * @param exportLabelArticoli
	 *            label dell'esportazione articoli
	 * @param exportLabelAttributi
	 *            label dell'esportazione attributi
	 */
	public ArticoliExporter(final IAtonBD atonBD, final ExportLabel exportLabelArticoli,
			final ExportLabel exportLabelAttributi) {
		super(atonBD, exportLabelArticoli,exportLabelAttributi);
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (exportLabels[0].isChecked()) {
			atonBD.esportaArticoli();
		}

		if (exportLabels[1].isChecked()) {
			atonBD.esportaAttributiArticoli();
		}
		return null;
	}

}
