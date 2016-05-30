/**
 * 
 */
package it.eurotn.panjea.pagamenti.rich.search;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.rich.bd.IAnagraficaPagamentiBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Specializzazione di {@link AbstractSearchObject} per la classe di dominio
 * {@link CodicePagamento}.
 * 
 * @author adriano
 * @version 1.0, 15/lug/08
 */
public class CodicePagamentoSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "codicePagamentoSearchObject";

	private IAnagraficaPagamentiBD anagraficaPagamentiBD;

	/**
	 * Costruttore.
	 * 
	 */
	public CodicePagamentoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		// nothing to do
		return null;
	}

	@Override
	public List<CodicePagamento> getData(String fieldSearch, String valueSearch) {
		TipoRicercaCodicePagamento tipoRicerca = TipoRicercaCodicePagamento.TUTTO;
		if ("descrizione".equals(fieldSearch)) {
			tipoRicerca = TipoRicercaCodicePagamento.DESCRIZIONE;
		} else if ("codicePagamento".equals(fieldSearch)) {
			tipoRicerca = TipoRicercaCodicePagamento.CODICE;
		}
		List<CodicePagamento> codiciPagamento = anagraficaPagamentiBD.caricaCodiciPagamento(valueSearch, tipoRicerca,
				false);
		return codiciPagamento;
	}

	/**
	 * @param anagraficaPagamentiBD
	 *            The anagraficaPagamentiBD to set.
	 */
	public void setAnagraficaPagamentiBD(IAnagraficaPagamentiBD anagraficaPagamentiBD) {
		this.anagraficaPagamentiBD = anagraficaPagamentiBD;
	}

}
