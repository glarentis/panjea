package it.eurotn.panjea.aton.rich.exporter;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;

public class TabelleExporter extends AtonExporter {

	/**
	 *
	 * @param atonBD
	 *            bd
	 * @param exportTabelleLabel
	 *            dell'esportazione tabelle
	 * @param exportUMLabel
	 *            dell'esportazione unità di misura
	 * @param exportUtentiLabel
	 *            dell'esportazione utenti
	 * @param exportCodiciIvaLabel
	 *            dell'esportazione codici iva
	 */
	public TabelleExporter(final IAtonBD atonBD, final ExportLabel exportTabelleLabel,final ExportLabel exportUMLabel,final ExportLabel exportUtentiLabel,final ExportLabel exportCodiciIvaLabel) {
		super(atonBD, exportTabelleLabel,exportUMLabel,exportUtentiLabel,exportCodiciIvaLabel);
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (exportLabels[0].isChecked()) {
			atonBD.esportaTabelle();
		}

		if (exportLabels[1].isChecked()) {
			atonBD.esportaUm();
		}

		if (exportLabels[2].isChecked()) {
			atonBD.esportaUtenti();
		}

		if (exportLabels[3].isChecked()) {
			atonBD.esportaCodiciIva();
		}
		return null;
	}

}
