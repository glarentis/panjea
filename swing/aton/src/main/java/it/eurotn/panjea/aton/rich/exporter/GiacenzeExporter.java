package it.eurotn.panjea.aton.rich.exporter;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;

public class GiacenzeExporter extends AtonExporter {

	/**
	 *
	 * @param atonBD
	 *            bd
	 * @param exportLabel
	 *            dell'esportazione
	 */
	public GiacenzeExporter(final IAtonBD atonBD, final ExportLabel exportLabel) {
		super(atonBD, exportLabel);
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (exportLabels[0].isChecked()) {
			atonBD.esportaGiacenze();
		}
		return null;
	}

}