package it.eurotn.panjea.pagamenti.service.interfaces;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoEsistenteException;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;

import java.util.List;

import javax.ejb.Remote;

/**
 * Interfaccia remota del Service del modulo pagamenti.
 * 
 * @author adriano
 * @version 1.0, 18/lug/08
 */
@Remote
public interface AnagraficaPagamentiService {

	/**
	 * Esegue la cancellazione.
	 * 
	 * @param codicePagamento
	 *            codicePagamento da cancellare
	 */
	void cancellaCodicePagamento(CodicePagamento codicePagamento);

	/**
	 * Cancella una sede pagamento.
	 * 
	 * @param sedePagamentoId
	 *            id della sede da cancellare
	 */
	void cancellaSedePagamento(Integer sedePagamentoId);

	/**
	 * Metodo per caricare tutto il codice di pagamento completo di strutture di
	 * calcolo con ID.
	 * 
	 * @param id
	 *            id del codice da caricare
	 * @return istanza di <code>CodicePagamento</code> identificata da id
	 */
	CodicePagamento caricaCodicePagamento(Integer id);

	/**
	 * Metodo per caricare tutto il codice di pagamento completo di strutture di
	 * calcolo con CODICE.
	 * 
	 * @param codice
	 *            codice da caricare
	 * @return istanza di <code>CodicePagamento</code> identificata da id
	 */
	CodicePagamento caricaCodicePagamento(String codice);

	/**
	 * Restituisce la lista di <code>CodicePagamento</code> eseguendo il filtro
	 * per il parametro <code>filterCodice</code> e filtrando ulteriormente per
	 * il codice dell'azienda corrente.
	 * 
	 * @param filtro
	 *            valore del filtro della ricerca
	 * @param tipoRicerca
	 *            tipo di ricerca
	 * @param includiDisabilitati
	 *            include nei risultati anche i codici pagamento disabilitati
	 * @return codici pagamento caricati
	 */
	List<CodicePagamento> caricaCodiciPagamento(String filtro, TipoRicercaCodicePagamento tipoRicerca,
			boolean includiDisabilitati);

	/**
	 * Carica una sede pagamento.
	 * 
	 * @param sedePagamentoId
	 *            id della sede da caricare
	 * @return sede caricata
	 * @throws PagamentiException
	 *             PagamentiException
	 */
	SedePagamento caricaSedePagamento(Integer sedePagamentoId) throws PagamentiException;

	/**
	 * Carica la sede di pagamento in base alla sede entità.
	 * 
	 * @param sedeEntitaId
	 *            id della sede entità
	 * @return sede pagamento caricata
	 */
	SedePagamento caricaSedePagamentoBySedeEntita(Integer sedeEntitaId);

	/**
	 * Carica la sede pagamento principale dell'entità.
	 * 
	 * @param entitaId
	 *            id dell'entità
	 * @return sede pagamento
	 */
	SedePagamento caricaSedePagamentoPrincipaleEntita(Integer entitaId);

	/**
	 * Metodo per il salvataggio di <code>CodicePagamento</code> Test OK.
	 * 
	 * @param codicePagamento
	 *            codice da salvare
	 * @return istanza di <code>CodicePagamento</code> salvata
	 * @throws CodicePagamentoEsistenteException
	 *             CodicePagamentoEsistenteException
	 */
	CodicePagamento salvaCodicePagamento(CodicePagamento codicePagamento) throws CodicePagamentoEsistenteException;

	/**
	 * Salva una sede pagamento.
	 * 
	 * @param sedePagamento
	 *            sede da salvare
	 * @return sede salvata
	 */
	SedePagamento salvaSedePagamento(SedePagamento sedePagamento);

}
