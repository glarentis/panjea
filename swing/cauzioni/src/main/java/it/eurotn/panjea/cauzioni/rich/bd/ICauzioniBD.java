package it.eurotn.panjea.cauzioni.rich.bd;

import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniEntitaDTO;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;
import java.util.Set;

public interface ICauzioniBD {

	/**
	 * Carica la movimentazione delle cauzioni.
	 * 
	 * @param idEntita
	 *            entita di riferimento
	 * @param idSedeEntita
	 *            sede entita di riferimento
	 * @param idArticolo
	 *            articolo di riferimento
	 * @return movimentazione caricata
	 */
	@AsyncMethodInvocation
	List<MovimentazioneCauzioneDTO> caricaMovimentazioneCauzioniArticolo(Set<Integer> idEntita,
			Set<Integer> idSedeEntita, Set<Integer> idArticolo);

	/**
	 * Carica la situazione cauzioni in base ai parametri specificati.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return stuazione cauzioni caricata
	 */
	@AsyncMethodInvocation
	List<SituazioneCauzioniDTO> caricaSituazioneCauzioni(ParametriRicercaSituazioneCauzioni parametri);

	/**
	 * Carica la situazione cauzioni dell'entità specificata.
	 * 
	 * @param idEntita
	 *            id dell'entita
	 * @param raggruppamentoSedi
	 *            indica e usare il raggruppamento per sede-articolo o solo per articolo
	 * @return stuazione cauzioni caricata
	 */
	@AsyncMethodInvocation
	List<SituazioneCauzioniEntitaDTO> caricaSituazioneCauzioniEntita(Integer idEntita, boolean raggruppamentoSedi);
}
