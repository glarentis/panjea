package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ContabilitaAnagraficaManager {

	/**
	 * Metodo che cancella il {@link RegistroIva}
	 *
	 * @param registroIva
	 *            registro da eliminare
	 */
	public void cancellaRegistroIva(RegistroIva registroIva);

	/**
	 * Carica le note sede e le note entita contabilità.
	 *
	 * @param sedeEntita
	 *            la sede entita di cui caricare le note
	 * @return note caricate
	 */
	NoteAreaContabile caricaNoteSede(SedeEntita sedeEntita);

	/**
	 * Metodo che restituisce i {@link RegistroIva} per l'azienda loggata
	 *
	 * @param valueSearch
	 *            .
	 * @param fieldSearch
	 *            .
	 *
	 * @return List<RegistroIva>
	 * @throws ContabilitaException
	 */
	public List<RegistroIva> caricaRegistriIva(String fieldSearch, String valueSearch) throws ContabilitaException;

	/**
	 * Metodo che restituisce il {@link RegistroIva} per l'id passato per parametro
	 *
	 * @param id
	 *            identificativo per caricare il {@link RegistroIva}
	 * @return RegistroIva
	 * @throws ContabilitaException
	 */
	public RegistroIva caricaRegistroIva(Integer id) throws ContabilitaException;

	/**
	 * Carica il registro iva con numero e tiporegistro scelto
	 *
	 * @param numero
	 *            il numero del registro iva
	 * @param tipoRegistro
	 *            il tipo registro del registro iva
	 * @return RegistroIva
	 * @throws ObjectNotFoundException
	 */
	public RegistroIva caricaRegistroIva(Integer numero, TipoRegistro tipoRegistro) throws ObjectNotFoundException;

	/**
	 * Esegue il salvataggio dell'argomento <code>RegistroIva</code> e lo restituisce aggiornato
	 *
	 * @param registroIva
	 *            il registroIva da salvare
	 * @return RegistroIva aggiornato
	 */
	public RegistroIva salvaRegistroIva(RegistroIva registroIva);

}
