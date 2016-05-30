/**
 * 
 */
package it.eurotn.panjea.anagrafica.contratto.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.util.List;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface ContrattiSpesometroManager {

	/**
	 * Aggiunge il contratto al documento.
	 * 
	 * @param contratto
	 *            il contratto da aggiungere
	 * @param documento
	 *            il documento a cui aggiungere il contratto
	 * @return il documento salvato con associato il contratto
	 */
	Documento aggiungiContrattoADocumento(ContrattoSpesometro contratto, Documento documento);

	/**
	 * Esegue la cancellazione di {@link ContrattoSpesometro}.
	 * 
	 * @param contratto
	 *            contratto da cancellare
	 */
	void cancellaContratto(ContrattoSpesometro contratto);

	/**
	 * Carica tutti i contratti o i contratti dell'entità.
	 * 
	 * @param entita
	 *            l'entità di cui caricare i contratti o null
	 * @return List<ContrattoSpesometro>
	 */
	List<ContrattoSpesometro> caricaContratti(EntitaLite entita);

	/**
	 * Carica il contratto identificato dall'id passato.
	 * 
	 * @param idContratto
	 *            l'id del contratto da caricare
	 * @return ContrattoSpesometro
	 */
	ContrattoSpesometro caricaContratto(Integer idContratto);

	/**
	 * Carica il contratto identificato dall'id passato.
	 * 
	 * @param idContratto
	 *            l'id del contratto da caricare
	 * @param loadCollection
	 *            indica se caricare le collection lazy per il contratto
	 * @return ContrattoSpesometro
	 */
	ContrattoSpesometro caricaContratto(Integer idContratto, boolean loadCollection);

	/**
	 * Carica i documenti collegati al contratto scelto.
	 * 
	 * @param idContratto
	 *            l'id del contratto di cui caricare i documenti
	 * @return la lista di documenti per il contratto scelto
	 */
	List<Documento> caricaDocumentiContratto(Integer idContratto);

	/**
	 * Rimuove il contratto dal documento.
	 * 
	 * @param documento
	 *            il documento dal quale rimuovere il contratto
	 */
	void rimuovContrattoDaDocumento(Documento documento);

	/**
	 * Esegue il salvataggio di {@link ContrattoSpesometro}.
	 * 
	 * @param contratto
	 *            contrattoSpesometro da salvare
	 * @return contrattoSpesometro salvato
	 */
	ContrattoSpesometro salvaContratto(ContrattoSpesometro contratto);

}
