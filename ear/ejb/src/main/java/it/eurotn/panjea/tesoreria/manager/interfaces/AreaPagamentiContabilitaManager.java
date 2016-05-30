package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import javax.ejb.Local;

@Local
public interface AreaPagamentiContabilitaManager {

	/**
	 * creazione dell'area contabile per incasso pagamenti.<br>
	 * il metodo crea l'area contabile e le sue righe e gestisce la chiusura forzata delle rate eseguendo la scrittura
	 * contabile <br>
	 * degli abbuoni attivi o passivi in questo modo: <br>
	 * Pagamenti(tipo partita passiva ) rediduo rata > 0 ? abbuono attivo in avere : abbuono passivo in dare Incassi (
	 * tipo partita avere ) residuo rata > 0 ? abbuono passivo in dare : abbuono passivo in avere
	 * 
	 * @param areaPagamenti
	 *            area pagamenti
	 * @param parametriCreazioneAreaChiusure
	 *            .
	 * @return area pagamenti
	 * @throws PagamentiException
	 *             eccezione
	 */
	AreaPagamenti creaAreaContabilePagamentoDiretto(AreaPagamenti areaPagamenti,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws PagamentiException;

	/**
	 * Crea un area contabile per distinta.
	 * 
	 * @param areaPagamenti
	 *            area pagamenti
	 * @param parametriCreazioneAreaChiusure
	 *            .
	 * @return area pagamenti
	 * @throws PagamentiException
	 *             eccezione
	 */
	AreaPagamenti creaAreaContabilePagamentoDistinta(AreaPagamenti areaPagamenti,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws PagamentiException;

}
