package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface RigheCollegateManager {

	/**
	 * Carica tutte le righe magazzino collegate alle riga passata come parametro.
	 * 
	 * @param rigaMagazzino
	 *            riga magazzino di riferimento
	 * @return righe magazzino caricate
	 */
	List<RigaDestinazione> caricaRigheMagazzinoCollegate(RigaMagazzino rigaMagazzino);

	/**
	 * Collega le righe magazzino all'area magazzino specificata.
	 * 
	 * @param righeDaCollegare
	 *            righe da collegare
	 * @param areaMagazzino
	 *            area magazzino di destinazione
	 * @param calcolaOrdinamento
	 *            indica se calcolare l'ordinamento delle righe nell'area
	 */
	void collegaRigaMagazzino(List<RigaMagazzino> righeDaCollegare, AreaMagazzino areaMagazzino,
			boolean calcolaOrdinamento);
}
