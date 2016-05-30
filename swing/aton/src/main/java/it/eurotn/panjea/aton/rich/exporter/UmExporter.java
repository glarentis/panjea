package it.eurotn.panjea.aton.rich.exporter;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;

public class UmExporter extends AtonExporter {

	/**
	 * 
	 * @param atonBD
	 *            bd
	 * @param exportLabel
	 *            dell'esportazione
	 */
	public UmExporter(final IAtonBD atonBD, final ExportLabel exportLabel) {
		super(atonBD, exportLabel);
	}

	@Override
	protected Void doInBackground() throws Exception {
		// spostato in TabelleExporter
		return null;
	}
}