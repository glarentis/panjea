package it.eurotn.panjea.onroad.rich.exporter;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;

public class AttributiExporter extends AtonExporter {

	/**
	 * 
	 * @param onRoadBD
	 *            bd
	 * @param exportLabel
	 *            dell'esportazione
	 */
	public AttributiExporter(final IOnRoadBD onRoadBD, final ExportLabel exportLabel) {
		super(onRoadBD, exportLabel);
	}

	@Override
	protected Void doInBackground() throws Exception {
		// spostato in articoli
		return null;
	}

}
