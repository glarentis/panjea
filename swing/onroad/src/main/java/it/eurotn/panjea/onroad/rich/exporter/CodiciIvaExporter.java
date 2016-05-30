package it.eurotn.panjea.onroad.rich.exporter;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;

public class CodiciIvaExporter extends AtonExporter {

	/**
	 * 
	 * @param onRoadBD
	 *            bd
	 * @param exportLabel
	 *            dell'esportazione
	 */
	public CodiciIvaExporter(final IOnRoadBD onRoadBD, final ExportLabel exportLabel) {
		super(onRoadBD, exportLabel);
	}

	@Override
	protected Void doInBackground() throws Exception {
		// spostato in tabelle
		return null;
	}

}
