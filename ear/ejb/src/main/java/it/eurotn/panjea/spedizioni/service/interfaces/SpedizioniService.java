package it.eurotn.panjea.spedizioni.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.spedizioni.exception.SpedizioniVettoreException;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SpedizioniService {

	/**
	 * Crea i file per la generazione delle etichette dell'area magazzino indicata in base ai parametri specificati.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @param parametriCreazioneEtichette
	 *            parametri di creazione.
	 * @return file creato restituito come arry di Byte
	 * @throws SpedizioniVettoreException
	 *             eccezione generica
	 * @throws FileCreationException
	 *             sollevata se viene generato un errore durante la lettura del file di template
	 */
	byte[] generaEtichette(AreaMagazzino areaMagazzino, ParametriCreazioneEtichette parametriCreazioneEtichette)
			throws FileCreationException, SpedizioniVettoreException;

	/**
	 * Crea il file per la generazione della rendicontazione al vettore.
	 * 
	 * @param areeMagazzinoRicerca
	 *            aree magazzino da rendicontare
	 * @param vettore
	 *            vettore di riferimento
	 * @return file creato restituito come array di byte
	 * @throws SpedizioniVettoreException
	 *             eccezione generica
	 * @throws FileCreationException
	 *             sollevata se viene generato un errore durante la lettura del file di template
	 */
	byte[] generaRendicontazione(List<AreaMagazzinoRicerca> areeMagazzinoRicerca, Vettore vettore)
			throws SpedizioniVettoreException, FileCreationException;

	/**
	 * Legge il file di etichette generato dal vettore e salva i dati nell'area magazzino selezionata.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @param data
	 *            file generato dal vettore
	 * @throws SpedizioniVettoreException
	 *             eccezione generica
	 * @throws FileCreationException
	 *             sollevata se viene generato un errore durante la lettura del file di template
	 */
	void leggiRisultatiEtichette(AreaMagazzino areaMagazzino, byte[] data) throws SpedizioniVettoreException,
			FileCreationException;

	/**
	 * Imposta le aree magazzino come rendicontate.
	 * 
	 * @param areeMagazzino
	 *            aree magazzino
	 * @throws SpedizioniVettoreException
	 *             eccezione generica
	 */
	void rendicontaAreeMagazzino(List<AreaMagazzinoRicerca> areeMagazzino) throws SpedizioniVettoreException;
}
