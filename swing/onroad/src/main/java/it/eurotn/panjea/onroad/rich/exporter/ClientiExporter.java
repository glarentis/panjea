package it.eurotn.panjea.onroad.rich.exporter;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;

public class ClientiExporter extends AtonExporter {

	/**
	 *
	 * @param onRoadBD
	 *            bd
	 * @param exportLabelClienti
	 *            label dell'esportazione clienti
	 * @param exportLabelClientiCessionari
	 *            label dell'esportazione clienti cessionari
	 */
	public ClientiExporter(final IOnRoadBD onRoadBD, final ExportLabel exportLabelClienti,
			final ExportLabel exportLabelClientiCessionari) {
		super(onRoadBD, exportLabelClienti, exportLabelClientiCessionari);
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (exportLabels[0].isChecked()) {
			onRoadBD.esportaClienti();
		}

		if (exportLabels[1].isChecked()) {
			onRoadBD.esportaClientiCessionari();
		}
		return null;
	}

}
