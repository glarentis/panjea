package it.eurotn.panjea.ordini.manager.documento.evasione.interfaces;

import it.eurotn.panjea.ordini.manager.documento.evasione.DatiDistintaCaricoEvasione;

import javax.ejb.Local;

@Local
public interface DatiDistintaCaricoEvasioneManager {
	/**
	 * 
	 * @return dati per poter fare un'evasione di righe contenute in distinta
	 */
	DatiDistintaCaricoEvasione getDatiDistintaCaricoEvasione();
}
