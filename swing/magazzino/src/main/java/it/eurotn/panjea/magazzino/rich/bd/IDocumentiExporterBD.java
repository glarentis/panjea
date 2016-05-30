package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.service.exception.DocumentiExporterException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;
import java.util.Map;

public interface IDocumentiExporterBD {

	/**
	 * Esporta i documenti generando i flussi di rendicontazione per il tipo di esportazione richiesta.
	 * 
	 * @param areeMagazzino
	 *            aree magazzino
	 * @param tipoEsportazione
	 *            tipo esportazione richiesta
	 * @param parametri
	 *            parametri per la generazione
	 * @return flussi generati
	 * @throws DocumentiExporterException
	 *             rilanciata se non esiste un codice per l'exporter o il codice sulle sedi
	 */
	@AsyncMethodInvocation
	List<byte[]> esportaDocumenti(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) throws DocumentiExporterException;
}
