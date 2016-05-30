package it.eurotn.panjea.onroad.rich.exporter;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;

import javax.swing.SwingWorker;

public abstract class AtonExporter extends SwingWorker<Void, Void> {

	protected IOnRoadBD onRoadBD;
	protected String idExporter;
	protected ExportLabel[] exportLabels;

	/**
	 * @param onRoadBD
	 *            bd
	 * @param exportLabels
	 *            le label dell'esportazione con icona e label
	 */
	public AtonExporter(final IOnRoadBD onRoadBD, final ExportLabel... exportLabels) {
		super();
		this.onRoadBD = onRoadBD;
		this.exportLabels = exportLabels;
	}

	@Override
	protected void done() {
		super.done();
		for (ExportLabel exportLabel : exportLabels) {
			if (exportLabel.isChecked()) {
				exportLabel.changeToImportato();
			}
		}
	}

	/**
	 * Lancia l'importazione.
	 */
	public void start() {
		for (ExportLabel exportLabel : exportLabels) {
			if (exportLabel.isChecked()) {
				exportLabel.changeToInCorso();
			}
		}
		execute();
	}

}
