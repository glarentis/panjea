package it.eurotn.panjea.pagamenti.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;

import javax.ejb.Local;

@Local
public interface SediPagamentoManager {

	/**
	 * Cancella la sedePagamento di una sedeEntita.
	 * 
	 * @param sedeEntita
	 *            sedeEntità interessata
	 */
	void cancellaSedePagamento(SedeEntita sedeEntita);

	/**
	 * Esegue la cancellazione di {@link SedePagamento}.
	 * 
	 * @param sedePagamento
	 *            sedePagamento da cancellare
	 */
	void cancellaSedePagamento(SedePagamento sedePagamento);

	/**
	 * Carica {@link SedePagamento}.
	 * 
	 * @param sedePagamento
	 *            sedePagamento con campo id valorizzato
	 * @return {@link SedePagamento} sedePagamento caricata
	 * @throws PagamentiException
	 *             errore generico
	 */
	SedePagamento caricaSedePagamento(SedePagamento sedePagamento) throws PagamentiException;

	/**
	 * Carica {@link SedePagamento} collegata a {@link SedeEntita}.
	 * 
	 * @param sedeEntita
	 *            sedeEntita di riferimento
	 * @param ignoraEreditaDatiCommerciali
	 *            se true forza il caricamento della sede pagamento ignorando
	 *            {@link SedeEntita#isEreditaDatiCommerciali()}
	 * @return {@link SedePagamento} di sedeEntita
	 */
	SedePagamento caricaSedePagamentoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali);

	/**
	 * Carica {@link SedePagamento} collegata alla sede entita principale di {@link EntitaLite}.
	 * 
	 * @param entitaLite
	 *            entitaLite interessata con id valorizzatao.
	 * @return {@link SedePagamento} di entita
	 */
	SedePagamento caricaSedePagamentoPrincipaleEntita(EntitaLite entitaLite);

	/**
	 * Rende persistente {@link SedePagamento} e la restituisce.
	 * 
	 * @param sedePagamento
	 *            sedePagamento da salvare
	 * @return {@link SedePagamento} salvata
	 */
	SedePagamento salvaSedePagamento(SedePagamento sedePagamento);

}
