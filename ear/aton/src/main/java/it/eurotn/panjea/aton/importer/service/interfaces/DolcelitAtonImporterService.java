package it.eurotn.panjea.aton.importer.service.interfaces;

import it.eurotn.panjea.ordini.domain.OrdineImportato;

import javax.ejb.Local;

@Local
public interface DolcelitAtonImporterService {
	/**
	 * 
	 * @param ordineImportato
	 *            ordine da "customizzare" per dolcelit
	 */
	void importa(OrdineImportato ordineImportato);
}
