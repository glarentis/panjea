/**
 *
 */
package it.eurotn.panjea.contabilita.rich.bd;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.contabilita.util.SituazioneRitenuteAccontoDTO;

import java.util.List;

/**
 * @author fattazzo
 *
 */
public interface IRitenutaAccontoBD {

	/**
	 * Cancella una {@link CausaleRitenutaAcconto}.
	 *
	 * @param causaleRitenutaAcconto
	 *            causale da cancellare
	 */
	void cancellaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto);

	/**
	 * Cancella un {@link ContributoPrevidenziale}.
	 *
	 * @param contributoPrevidenziale
	 *            contributo da cancellare
	 */
	void cancellaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale);

	/**
	 * Cancella una {@link Prestazione}.
	 *
	 * @param prestazione
	 *            prestazione da cancellare
	 */
	void cancellaPrestazione(Prestazione prestazione);

	/**
	 * Carica tutte le causali per le ritenute acconto.
	 *
	 * @param codice
	 *            codice da filtrare
	 * @return causali caricate
	 */
	List<CausaleRitenutaAcconto> caricaCausaliRitenuteAcconto(String codice);

	/**
	 * Carica tutti i contributi previdenziali.
	 *
	 * @return contributi caricati
	 */
	List<ContributoPrevidenziale> caricaContributiPrevidenziali();

	/**
	 * Carica tutte le prestazioni.
	 *
	 * @return prestazioni caricate
	 */
	List<Prestazione> caricaPrestazioni();

	/**
	 * Carica la situazione delle ritenute d'acconto per i parametri richiesti.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return situazione ritenute
	 */
	List<SituazioneRitenuteAccontoDTO> caricaSituazioneRitenuteAccont(ParametriSituazioneRitenuteAcconto parametri);

	/**
	 * Salva una {@link CausaleRitenutaAcconto}.
	 *
	 * @param causaleRitenutaAcconto
	 *            causale da salvare
	 * @return causale salvata
	 */
	CausaleRitenutaAcconto salvaCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto);

	/**
	 * Salva un {@link ContributoPrevidenziale}.
	 *
	 * @param contributoPrevidenziale
	 *            contributo da salvare
	 * @return contributo salvato
	 */
	ContributoPrevidenziale salvaContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale);

	/**
	 * Salva una {@link Prestazione}.
	 *
	 * @param prestazione
	 *            prestazione da salvare
	 * @return prestazione salvata
	 */
	Prestazione salvaPrestazione(Prestazione prestazione);

}
