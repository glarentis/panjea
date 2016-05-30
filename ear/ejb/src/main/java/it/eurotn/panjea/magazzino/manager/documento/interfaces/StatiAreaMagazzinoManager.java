package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;

import javax.ejb.Local;

/**
 * 
 * Interfaccia local del {@link StatiAreaMagazzinoManager} incaricato di variare lo stato di {@link AreaMagazzino}.
 * 
 * @author adriano
 * @version 1.0, 10/ott/2008
 * 
 */
@Local
public interface StatiAreaMagazzinoManager {

	/**
	 * modifica lo stato di {@link AreaMagazzino} da {@link StatoAreaMagazzino}.CONFERMATO a {@link StatoAreaMagazzino}
	 * .PROVVISORIO.
	 * 
	 * @param areaMagazzino
	 *            area Magazzino interessata al cambio stato
	 * @return areaMagazzino con lo stato cambiato
	 */
	AreaMagazzino cambiaStatoDaConfermatoInProvvisorio(AreaMagazzino areaMagazzino);

	/**
	 * modifica lo stato di {@link AreaMagazzino} da {@link StatoAreaMagazzino}.PROVVISORIO a {@link StatoAreaMagazzino}
	 * .CONFERMATO.
	 * 
	 * @param areaMagazzino
	 *            area Magazzino interessata al cambio stato
	 * @return areaMagazzino con lo stato cambiato
	 */
	AreaMagazzino cambiaStatoDaProvvisorioInConfermato(AreaMagazzino areaMagazzino);

	/**
	 * modifica lo stato di {@link AreaMagazzino} da {@link StatoAreaMagazzino}.PROVVISORIO a {@link StatoAreaMagazzino}
	 * .FORZATO.
	 * 
	 * @param areaMagazzino
	 *            area Magazzino interessata al cambio stato
	 * @return areaMagazzino con lo stato cambiato
	 */
	AreaMagazzino cambiaStatoDaProvvisorioInForzato(AreaMagazzino areaMagazzino);

}
