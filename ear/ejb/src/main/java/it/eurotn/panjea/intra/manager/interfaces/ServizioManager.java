package it.eurotn.panjea.intra.manager.interfaces;

import it.eurotn.panjea.intra.domain.Servizio;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ServizioManager {

	/**
	 * associa la nomenclaura all'articolo. il file deve essere csv con codice articolo,codice
	 * nomenclatura,peso,nazione,ModalitaErogazione
	 * 
	 * @param file
	 *            .
	 * @return log
	 */
	String associaNomenclatura(byte[] file);

	/**
	 * Cancella il servizio.
	 * 
	 * @param servizio
	 *            servizio da cancellare
	 */
	void cancellaServizio(Servizio servizio);

	/**
	 * Carica i servizi/nomenclature richiesti.
	 * 
	 * @param classServizio
	 *            classe richiesta
	 * @param fieldSearch
	 *            campo filtro
	 * @param filtro
	 *            filtro
	 * 
	 * @return le nomenclature richieste o tutte se class null
	 */
	List<?> caricaServizi(Class<?> classServizio, String fieldSearch, String filtro);

	/**
	 * Carica un servizio ricercandolo per ID.
	 * 
	 * @param servizio
	 *            servizio da caricare.
	 * @return servizio ricercato
	 */
	Servizio caricaServizio(Servizio servizio);

	/**
	 * Importa il file di nomenclatura esportato con l'intraWeb.
	 * 
	 * @param file
	 *            file esportato da intraweb
	 */
	void importaNomenclatura(byte[] file);

	/**
	 * Importa il file di servizi esportato con l'intraWeb.
	 * 
	 * @param file
	 *            file esportato da intraweb
	 */
	void importaServizi(byte[] file);

	/**
	 * Salva il servizio.
	 * 
	 * @param servizio
	 *            servizio da salvare
	 * @return servizio salvato
	 */
	Servizio salvaServizio(Servizio servizio);

}
