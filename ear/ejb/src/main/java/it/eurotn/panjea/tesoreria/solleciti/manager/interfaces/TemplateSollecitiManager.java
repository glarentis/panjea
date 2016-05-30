package it.eurotn.panjea.tesoreria.solleciti.manager.interfaces;

import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ejb.Local;

@Local
public interface TemplateSollecitiManager {
	/**
	 * esegue la cancellazione di {@link TemplateSolleciti}.
	 * 
	 * @param templateSolleciti
	 *            .
	 */
	void cancellaTemplateSolleciti(TemplateSolleciti templateSolleciti);

	/**
	 * carica la lista di {@link TemplateSolleciti}.
	 * 
	 * @return .
	 * @throws PagamentiException .
	 */
	List<TemplateSolleciti> caricaTemplateSolleciti() throws PagamentiException;

	/**
	 * carica {@link TemplateSolleciti} identificato da idTemplateSollecito.
	 * 
	 * @param idTemplateSollecito
	 *            .
	 * @return .
	 */
	TemplateSolleciti caricaTemplateSollecito(int idTemplateSollecito);

	/**
	 * Ritorna il testo per il report del sollecito.
	 * 
	 * @param testo
	 *            testo contenente le variabili da avvalorare
	 * @param sollecito
	 *            sollecito di cui generare il testo dal template
	 * @return ritorna il testo per il sollecito
	 * @throws IllegalAccessException
	 *             IllegalAccessException
	 * @throws InvocationTargetException
	 *             InvocationTargetException
	 * @throws NoSuchMethodException
	 *             NoSuchMethodException
	 */
	String creaTesto(String testo, Sollecito sollecito) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException;

	/**
	 * esegue il salvataggio di {@link TemplateSolleciti}.
	 * 
	 * @param templateSolleciti
	 *            .
	 * @return .
	 */
	TemplateSolleciti salvaTemplateSolleciti(TemplateSolleciti templateSolleciti);
}
