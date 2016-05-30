package it.eurotn.panjea.magazzino.manager.export.interfaces;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.service.exception.DocumentiExporterException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface DocumentiExporterManager {

	/**
	 * Esporta i documenti generando i flussi di rendicontazione per il tipo di esportazione richiesta.
	 * 
	 * @param areeMagazzino
	 *            aree magazzino
	 * @param tipoEsportazione
	 *            tipo esportazione richiesta
	 * @param parametri
	 *            parametri per la rendicontazione
	 * @return flussi generati
	 * @throws DocumentiExporterException
	 *             rilanciata se non esiste un codice per l'exporter o il codice sulle sedi
	 */
	List<byte[]> esportaDocumenti(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) throws DocumentiExporterException;

}
