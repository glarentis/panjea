package it.eurotn.panjea.ordini.manager.documento.evasione.interfaces;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

import java.util.Set;

import javax.ejb.Local;

@Local
public interface DocumentoEvasioneManager {

	/**
	 * 
	 * @param righeOrdineDaforzare
	 *            righe che sono state forzate per l'evasione identificata da uuid
	 * @param uuid
	 *            uuid che identifica i documenti evasi
	 */
	void aggiornaDistinteCaricoEvase(Set<Integer> righeOrdineDaforzare, String uuid);

	/**
	 * Salva il documento di evasione..
	 * 
	 * @param areaMagazzinoFullDTO
	 *            area da salvare
	 * @return id area magazzino creata
	 */
	AreaMagazzino salvaDocumentoEvasione(AreaMagazzinoFullDTO areaMagazzinoFullDTO);

}
