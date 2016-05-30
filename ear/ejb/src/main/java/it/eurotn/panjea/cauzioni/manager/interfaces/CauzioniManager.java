package it.eurotn.panjea.cauzioni.manager.interfaces;

import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniEntitaDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface CauzioniManager {

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
	List<MovimentazioneCauzioneDTO> caricaMovimentazioneCauzioniArticolo(Set<Integer> idEntita,
			Set<Integer> idSedeEntita, Set<Integer> idArticolo);

	/**
	 * Carica la situazione cauzioni in base ai parametri specificati.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return stuazione cauzioni caricata
	 */
	List<SituazioneCauzioniDTO> caricaSituazioneCauzioni(ParametriRicercaSituazioneCauzioni parametri);

	/**
	 * Carica la situazione cauzioni fino al movimento specificato.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @return situazione cauzioni caricata
	 */
	List<SituazioneCauzioniDTO> caricaSituazioneCauzioniAreaMagazzino(AreaMagazzino areaMagazzino);

	/**
	 * Carica la situazione cauzioni dell'entità specificata.
	 * 
	 * @param idEntita
	 *            id dell'entita
	 * @param raggruppamentoSedi
	 *            indica e usare il raggruppamento per sede-articolo o solo per articolo
	 * @return stuazione cauzioni caricata
	 */
	List<SituazioneCauzioniEntitaDTO> caricaSituazioneCauzioniEntita(Integer idEntita, boolean raggruppamentoSedi);

}
