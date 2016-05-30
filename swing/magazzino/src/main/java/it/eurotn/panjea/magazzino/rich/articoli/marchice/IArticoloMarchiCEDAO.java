package it.eurotn.panjea.magazzino.rich.articoli.marchice;

import java.util.Date;
import java.util.Set;

import javax.swing.ImageIcon;

public interface IArticoloMarchiCEDAO {

	/**
	 * Cancella il marchio già pubblicato.
	 * 
	 * @param codiceArticolo
	 *            codice articolo
	 * @param imageName
	 *            nome dell'immagine
	 */
	void cancellaMarchioCE(String codiceArticolo, String imageName);

	/**
	 * Carica tutte le immagini dei marchi CE pubblicate per l'articolo.
	 * 
	 * @param codiceArticolo
	 *            codice articolo
	 * @return immagini caricate
	 */
	Set<ImageIcon> caricaMarchiCE(String codiceArticolo);

	/**
	 * Carica il path del marchio CE valido per la data passata come parametro.
	 * 
	 * @param codiceArticolo
	 *            codice articolo
	 * @param data
	 *            data di riferimento
	 * @return path del marchio CE, <code>null</code> se non esiste
	 */
	String caricaPathMarchioCECorrente(String codiceArticolo, Date data);

	/**
	 * Controlla se per l'articolo e per la data di decorrenza richiesta esiste già un marchio CE pubblicato.
	 * 
	 * @param codiceArticolo
	 *            codice articolo
	 * @param dataDecorrenza
	 *            data di decorrenza
	 * @return <code>true</code> se esiste l'immagine, <code>false</code> altrimenti
	 */
	boolean checkMarchioCE(String codiceArticolo, Date dataDecorrenza);

	/**
	 * Salva l'immagine del marchio CE relativa all'articolo e alla data di decorrenza.
	 * 
	 * @param codiceArticolo
	 *            codice articolo
	 * @param dataDecorrenza
	 *            data di decorrenza dell'immagine
	 * @param imageFilePath
	 *            percorso del file dell'immagine
	 */
	void salvaMarchioCE(String codiceArticolo, Date dataDecorrenza, String imageFilePath);
}