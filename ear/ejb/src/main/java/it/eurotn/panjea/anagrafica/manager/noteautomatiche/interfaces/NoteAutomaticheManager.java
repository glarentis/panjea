/**
 *
 */
package it.eurotn.panjea.anagrafica.manager.noteautomatiche.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface NoteAutomaticheManager {

	/**
	 * Cancella una nota automatica.
	 * 
	 * @param notaAutomatica
	 *            nota da cancellare
	 */
	void cancellaNotaAutomatica(NotaAutomatica notaAutomatica);

	/**
	 * Carica la nota automatica.
	 * 
	 * @param idNotaAutomatica
	 *            idNotaAutomatica da caricare
	 * @return NotaAutomatica caricata
	 */
	NotaAutomatica caricaNotaAutomatica(Integer idNotaAutomatica);

	/**
	 * Carica tutte le note automatiche del documento per la data indicata.
	 * 
	 * @param data
	 *            data di riferimento
	 * @param documento
	 *            documento
	 * @return note caricate
	 */
	List<NotaAutomatica> caricaNoteAutomatiche(Date data, Documento documento);

	/**
	 * Carica tutte le note automatiche presenti.
	 * 
	 * @param idEntita
	 *            id entita
	 * @param idSedeEntita
	 *            id sede entita
	 * @return note caricate
	 */
	List<NotaAutomatica> caricaNoteAutomatiche(Integer idEntita, Integer idSedeEntita);

	/**
	 * Salva una nota automatica.
	 * 
	 * @param notaAutomatica
	 *            not da salvare
	 * @return nota salvata
	 */
	NotaAutomatica salvaNotaAutomatica(NotaAutomatica notaAutomatica);
}
