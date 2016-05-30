package it.eurotn.panjea.onroad.rich.exporter;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;

public class CondizExporter extends AtonExporter {

	/**
	 *
	 * @param onRoadBD
	 *            bd
	 * @param exportLabel
	 *            dell'esportazione
	 */
	public CondizExporter(final IOnRoadBD onRoadBD, final ExportLabel exportLabel) {
		super(onRoadBD, exportLabel);
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (exportLabels[0].isChecked()) {
			onRoadBD.esportaCondiz();
		}
		return null;
	}
}