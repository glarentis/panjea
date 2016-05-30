package it.eurotn.panjea.anagrafica.rich.editors.azienda.dao;

import javax.swing.ImageIcon;

public interface ILogoAziendaDAO {

	/**
	 * Carica il logo aziendale.
	 *
	 * @return immagine del logo presente
	 */
	ImageIcon caricaLogo();

	/**
	 * Salva l'immagine come logo aziendale.
	 *
	 * @param imageFilePath
	 *            percorso del file dell'immagine
	 */
	void salvaLogo(String imageFilePath);

}
