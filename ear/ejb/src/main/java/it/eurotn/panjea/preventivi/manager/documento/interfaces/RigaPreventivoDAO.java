package it.eurotn.panjea.preventivi.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;

import java.util.List;

import javax.ejb.Local;

@Local
public interface RigaPreventivoDAO {

	/**
	 * Cancella la {@link RigaPreventivo}.
	 * 
	 * @param rigaPreventivo
	 *            riga da cancellare
	 * @return area Magazzino con lo stato aggiornato
	 */
	AreaPreventivo cancellaRigaPreventivo(RigaPreventivo rigaPreventivo);

	/**
	 * Carica una riga ordine.
	 * 
	 * @param rigaPreventivo
	 *            Riga magazzino da caricare
	 * @return Riga magazzino caricata
	 */
	RigaPreventivo caricaRigaPreventivo(RigaPreventivo rigaPreventivo);

	/**
	 * Carica le righe di una area ordine scelta.
	 * 
	 * @param areaPreventivo
	 *            l'area ordine di cui caricare le righe.
	 * @return List<RigaPreventivo>
	 */
	List<RigaPreventivo> caricaRighePreventivo(AreaPreventivo areaPreventivo);

	/**
	 * Carica le righe DTO di un'areaPreventivo scelta.
	 * 
	 * @param areaPreventivo
	 *            l'area ordine di cui caricare le righe
	 * @return List<RigaPreventivoDTO>
	 */
	List<RigaPreventivoDTO> caricaRighePreventivoDTO(AreaPreventivo areaPreventivo);

	/**
	 * Carica le righe di una area ordine scelta per la stampa.
	 * 
	 * @param areaPreventivo
	 *            l'area ordine di cui caricare le righe.
	 * @return List<RigaPreventivo>
	 */
	List<RigaPreventivo> caricaRighePreventivoStampa(AreaPreventivo areaPreventivo);

	/**
	 * Crea una riga articolo.
	 * 
	 * @param parametriCreazioneRigaArticolo
	 *            parametri con i dati utili alla creazione della riga articolo *
	 * @return rigaArticolo con i parametri settatti
	 */
	IRigaArticoloDocumento creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

	/**
	 * 
	 * @param rigaPreventivo
	 *            RigaPreventivo da salvare
	 * @return riga magazzino salvata
	 */
	RigaPreventivo salvaRigaPreventivo(RigaPreventivo rigaPreventivo);

	/**
	 * 
	 * @param rigaPreventivo
	 *            riga magazzino da salvare
	 * @return RigaPreventivo salvata
	 */
	RigaPreventivo salvaRigaPreventivoNoCheck(RigaPreventivo rigaPreventivo);
}
