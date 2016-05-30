package it.eurotn.panjea.magazzino.manager.export.exporter.dao;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.manager.export.exporter.sait.SaitRigaRendicontazioneBeanExporter;

public class DaoRigaRendicontazioneBeanExporter extends SaitRigaRendicontazioneBeanExporter {

	/**
	 * Costruttore.
	 * 
	 * @param rigaArticolo
	 *            riga articolo
	 * @param progressivo
	 *            progressivo
	 * @param codiceArticoloEntita
	 *            codice articolo dell'entita
	 */
	public DaoRigaRendicontazioneBeanExporter(final RigaArticolo rigaArticolo, final int progressivo,
			final String codiceArticoloEntita) {
		super(rigaArticolo, progressivo, codiceArticoloEntita);
	}

	/**
	 * @return Returns the tipoMovimento
	 */
	public int getTipoMovimento() {

		int tipoMov = 1;

		if (getTipoDocumento().isNotaCreditoEnable()) {
			tipoMov = 2;
		}

		return tipoMov;
	}

	@Override
	public int getTipoRecord() {
		return 20;
	}

	/**
	 * @param tipoMovimento
	 *            The tipoMovimento to set.
	 */
	public void setTipoMovimento(int tipoMovimento) {
	}
}
