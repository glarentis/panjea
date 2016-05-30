package it.eurotn.panjea.ordini.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;

import java.util.List;

import javax.ejb.Local;

@Local
public interface RigaOrdineDAO {

	/**
	 * Associa la configurazione alla riga ordine.
	 * 
	 * @param rigaArticolo
	 *            rigaArticolo
	 * @param configurazioneDistintaDaAssociare
	 *            configurazioneDistintaDaAssociare
	 * @return la RigaArticolo salvata
	 */
	RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
			ConfigurazioneDistinta configurazioneDistintaDaAssociare);

	/**
	 * Cancella la {@link RigaOrdine}.
	 * 
	 * @param rigaOrdine
	 *            riga da cancellare
	 * @return area Magazzino con lo stato aggiornato
	 */
	AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine);

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
	 * Carica una riga ordine.
	 * 
	 * @param rigaOrdine
	 *            Riga magazzino da caricare
	 * @return Riga magazzino caricata
	 */
	RigaOrdine caricaRigaOrdine(RigaOrdine rigaOrdine);

	/**
	 * Carica le righe di una area ordine scelta.
	 * 
	 * @param areaOrdine
	 *            l'area ordine di cui caricare le righe.
	 * @return List<RigaOrdine>
	 */
	List<RigaOrdine> caricaRigheOrdine(AreaOrdine areaOrdine);

	/**
	 * Carica le righe DTO di un'areaOrdine scelta.
	 * 
	 * @param areaOrdine
	 *            l'area ordine di cui caricare le righe
	 * @return List<RigaOrdineDTO>
	 */
	List<RigaOrdineDTO> caricaRigheOrdineDTO(AreaOrdine areaOrdine);

	/**
	 * Carica le righe di una area ordine scelta per la stampa.
	 * 
	 * @param areaOrdine
	 *            l'area ordine di cui caricare le righe.
	 * @return List<RigaOrdine>
	 */
	List<RigaOrdine> caricaRigheOrdineStampa(AreaOrdine areaOrdine);

	/**
	 * Crea una riga articolo.
	 * 
	 * @param parametriCreazioneArticolo
	 *            parametri con i dati utili alla creazione della riga articolo *
	 * @return rigaArticolo con i parametri settatti
	 */
	IRigaArticoloDocumento creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneArticolo);

	/**
	 * 
	 * @param rigaOrdine
	 *            RigaOrdine da salvare
	 * @return riga magazzino salvata
	 */
	RigaOrdine salvaRigaOrdine(RigaOrdine rigaOrdine);

	/**
	 * 
	 * @param rigaOrdine
	 *            riga magazzino da salvare
	 * @return RigaOrdine salvata
	 */
	RigaOrdine salvaRigaOrdineNoCheck(RigaOrdine rigaOrdine);

}
