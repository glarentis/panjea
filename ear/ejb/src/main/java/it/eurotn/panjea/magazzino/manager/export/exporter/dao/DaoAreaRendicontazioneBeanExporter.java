package it.eurotn.panjea.magazzino.manager.export.exporter.dao;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.export.exporter.sait.SaitAreaRendicontazioneBeanExporter;

public class DaoAreaRendicontazioneBeanExporter extends SaitAreaRendicontazioneBeanExporter {

	/**
	 * Costruttore.
	 * 
	 * @param codiceCliente
	 *            codice cliente
	 * @param areaMagazzino
	 *            area magazzino
	 * @param areaCollegata
	 *            area collegata
	 * @param progressivo
	 *            progressivo
	 */
	public DaoAreaRendicontazioneBeanExporter(final String codiceCliente, final AreaMagazzino areaMagazzino,
			final IAreaDocumento areaCollegata, final int progressivo) {
		super(codiceCliente, areaMagazzino, areaCollegata, progressivo);
	}

	@Override
	public int getTipoRecord() {
		return 10;
	}
}
