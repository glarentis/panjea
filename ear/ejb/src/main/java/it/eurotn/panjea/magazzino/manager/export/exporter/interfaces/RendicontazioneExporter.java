package it.eurotn.panjea.magazzino.manager.export.exporter.interfaces;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface RendicontazioneExporter {

	/**
	 * Restituisce tutti i flussi generati per le aree magazzino e il tipo esportazione passati come parametro.
	 * 
	 * @param areeMagazzino
	 *            aree magazzino
	 * @param tipoEsportazione
	 *            tipo esportazione
	 * @param parametri
	 *            parametri per l'esportazione.
	 * @return flussi generati
	 */
	List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri);

}
