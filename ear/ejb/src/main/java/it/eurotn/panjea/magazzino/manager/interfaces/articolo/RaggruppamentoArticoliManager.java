package it.eurotn.panjea.magazzino.manager.interfaces.articolo;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;

import java.util.List;
import java.util.Set;

/**
 * Gestione del raggruppamento articoli.
 * 
 * @author giangi
 * @version 1.0, 06/set/2011
 * 
 */
public interface RaggruppamentoArticoliManager {
	/**
	 * 
	 * 
	 * @param raggruppamentoArticoli
	 *            da cancellare
	 */
	void cancellaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

	/**
	 * 
	 * @param rigaRaggruppamentoArticoli
	 *            riga da cancellare
	 */
	void cancellaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli);

	/**
	 * 
	 * @return lista di tutti i raggruppamenti presenti. Le righe non vengono caricate
	 */
	List<RaggruppamentoArticoli> caricaRaggruppamenti();

	/**
	 * Carica un {@link RaggruppamentoArticoli}.
	 * 
	 * @param raggruppamentoArticoli
	 *            raggruppamento da salvare
	 * @return raggruppamento salvato
	 */
	RaggruppamentoArticoli caricaRaggruppamentoArticoli(RaggruppamentoArticoli raggruppamentoArticoli);

	/**
	 * 
	 * @param raggruppamentoArticoli
	 *            raggruppamento per il quale caricare le righe
	 * @return righe del raggruppamento
	 */
	Set<RigaRaggruppamentoArticoli> caricaRigheRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

	/**
	 * 
	 * @param idArticolo
	 *            id dell'articolo da caricare
	 * @return righeRaggruppamento legato all'articolo richiesto.
	 */
	List<RigaRaggruppamentoArticoli> caricaRigheRaggruppamentoArticoliByArticolo(int idArticolo);

	/**
	 * 
	 * @param raggruppamentoArticoli
	 *            raggruppamento da salvare
	 * @return raggruppamento salvato
	 */
	RaggruppamentoArticoli salvaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

	/**
	 * 
	 * @param rigaRaggruppamentoArticoli
	 *            riga da salvare
	 * @return riga salvata.
	 */
	RigaRaggruppamentoArticoli salvaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli);
}
