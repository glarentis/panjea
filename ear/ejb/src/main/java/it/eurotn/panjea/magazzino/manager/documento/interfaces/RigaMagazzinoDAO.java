package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.conai.domain.RigaConaiComponente;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.RigheMagazzinoDTOResult;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface RigaMagazzinoDAO {

	/**
	 * Cancella la {@link RigaMagazzino}.
	 * 
	 * @param rigaMagazzino
	 *            riga da cancellare
	 * @return area Magazzino con lo stato aggiornato
	 */
	AreaMagazzino cancellaRigaMagazzino(RigaMagazzino rigaMagazzino);

	/**
	 * Cancella tutte le righe automatiche dell'area magazzino.
	 * 
	 * @param areaMagazzino
	 *            area magazzino
	 */
	void cancellaRigheAutomatiche(AreaMagazzino areaMagazzino);

	/**
	 * Carica la qta di attrezzaggio secondo i parametri ricevuti. La qta di attrezzaggio è composta dalla qta
	 * attrezzaggio del componente + la qta attrezzaggio delle fasi direttamente collegate. non si sommano le qta
	 * attrezzaggio dei componenti o delle fasi dei componenti.
	 * 
	 * @param rigaArticolo
	 *            rigaArticolo
	 * @param configurazioneDistinta
	 *            configurazioneDistinta
	 * @return la riga con qta di attrezzaggio valorizzata
	 */
	RigaArticolo caricaQtaAttrezzaggio(RigaArticolo rigaArticolo, ConfigurazioneDistinta configurazioneDistinta);

	/**
	 * Carica una riga magazzino.
	 * 
	 * @param rigaMagazzino
	 *            Riga magazzino da caricare
	 * @return Riga magazzino caricata
	 */
	RigaMagazzino caricaRigaMagazzino(RigaMagazzino rigaMagazzino);

	/**
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino di cui caricare le righe articolo (esclude le altre tipologie di righe)
	 * @return righe articolo dell'area magazzino
	 */
	List<RigaArticolo> caricaRigheArticolo(AreaMagazzino areaMagazzino);

	/**
	 * Carica le righe di una area ordine scelta.
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino di cui caricare le righe.
	 * @return List<RigaMagazzino>
	 */
	List<? extends RigaMagazzino> caricaRigheMagazzino(AreaMagazzino areaMagazzino);

	/**
	 * Carica le righe magazzino di una areacontabile filtrando sole le righe con l'articolo scelto.
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino di cui caricare le righe
	 * @param idArticolo
	 *            l'id dell'articolo per filtrare le righe
	 * @return List<RigaMagazzino>
	 */
	List<RigaMagazzino> caricaRigheMagazzino(AreaMagazzino areaMagazzino, Integer idArticolo);

	/**
	 * Carica le righe DTO di un'areaMagazzino scelta.
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino di cui caricare le righe
	 * @return List<RigaMagazzinoDTO>
	 */
	RigheMagazzinoDTOResult caricaRigheMagazzinoDTO(AreaMagazzino areaMagazzino);

	/**
	 * Carica le righe di una area ordine scelta.
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino di cui caricare le righe.
	 * @return List<RigaMagazzino>
	 */
	List<? extends RigaMagazzino> caricaRigheMagazzinoStampa(AreaMagazzino areaMagazzino);

	/**
	 * Crea una riga articolo.
	 * 
	 * @param parametriCreazioneArticolo
	 *            parametri con i dati utili alla creazione della riga articolo *
	 * @return rigaArticolo con i parametri settatti
	 */
	IRigaArticoloDocumento creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneArticolo);

	/**
	 * Se l'articolo ha dei componenti conai e il tipo documento prevede la gestione conai, allora creo le righe conai
	 * componente.
	 * 
	 * @param riga
	 *            la riga a cui associare i componenti conai
	 * @param parametriCreazioneRigaArticolo
	 *            i parametri da cui recuperare i dati ConaiComponente per creare la lista di RigaConaiComponente
	 * @return la riga articolo con associati
	 */
	Set<RigaConaiComponente> creaRigheConaiComponente(RigaArticolo riga,
			ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

	/**
	 * 
	 * @param rigaMagazzino
	 *            rigaMagazzino da salvare
	 * @return riga magazzino salvata
	 * @throws RimanenzaLottiNonValidaException
	 *             rilanciata se non c'è una rimanenza nei lotti valida.
	 * @throws RigheLottiNonValideException
	 *             rilanciata se le righe lotti non sono valide
	 * @throws QtaLottiMaggioreException
	 *             rilanciata se la quantità assegnata ai lotti supera la quantità della riga articolo
	 */
	RigaMagazzino salvaRigaMagazzino(RigaMagazzino rigaMagazzino) throws RimanenzaLottiNonValidaException,
			RigheLottiNonValideException, QtaLottiMaggioreException;

	/**
	 * 
	 * @param rigaMagazzino
	 *            riga magazzino da salvare
	 * @return rigaMagazzino salvata
	 */
	RigaMagazzino salvaRigaMagazzinoNoCheck(RigaMagazzino rigaMagazzino);

}
