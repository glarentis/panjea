/**
 * 
 */
package it.eurotn.panjea.spedizioni.rich.bd;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;

import java.util.List;

/**
 * Interfaccia del business delegate di {@link AnagraficaService}
 * 
 * @author adriano
 * @version 1.0, 18/dic/07
 * 
 */
public interface ISpedizioniBD {

	static final String BEAN_ID = "spedizioniBD";

	/**
	 * Crea i file per la generazione delle etichette dell'area magazzino indicata in base ai parametri specificati.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @param parametriCreazioneEtichette
	 *            parametri di creazione.
	 * @return file creato restituito come arry di Byte
	 */
	@AsyncMethodInvocation
	byte[] generaEtichette(AreaMagazzino areaMagazzino, ParametriCreazioneEtichette parametriCreazioneEtichette);

	/**
	 * Crea il file per la generazione della rendicontazione al vettore.
	 * 
	 * @param areeMagazzinoRicerca
	 *            aree magazzino da rendicontare
	 * @param vettore
	 *            vettore di riferimento
	 * @return file creato restituito come array di byte
	 */
	@AsyncMethodInvocation
	byte[] generaRendicontazione(List<AreaMagazzinoRicerca> areeMagazzinoRicerca, Vettore vettore);

	/**
	 * Legge il file di etichette generato dal vettore e salva i dati nell'area magazzino selezionata.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @param data
	 *            file generato dal vettore
	 */
	void leggiRisultatiEtichette(AreaMagazzino areaMagazzino, byte[] data);

	/**
	 * Imposta le aree magazzino come rendicontate.
	 * 
	 * @param areeMagazzino
	 *            aree magazzino
	 */
	@AsyncMethodInvocation
	void rendicontaAreeMagazzino(List<AreaMagazzinoRicerca> areeMagazzino);

	/**
	 * Salva una entita.
	 * 
	 * @param entita
	 *            entita da salvare
	 * @return entita salvata
	 * @throws AnagraficheDuplicateException
	 *             anagrafica duplicata
	 */
	Entita salvaEntita(Entita entita) throws AnagraficheDuplicateException;
}
