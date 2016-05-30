package it.eurotn.panjea.aton.rich.exporter;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;

import javax.swing.SwingWorker;

public abstract class AtonExporter extends SwingWorker<Void, Void> {

	protected IAtonBD atonBD;
	protected String idExporter;
	protected ExportLabel[] exportLabels;

	/**
	 * @param atonBD
	 *            bd
	 * @param exportLabels
	 *            la label dell'esportazione con icona e label
	 */
	public AtonExporter(final IAtonBD atonBD, final ExportLabel... exportLabels) {
		super();
		this.atonBD = atonBD;
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
