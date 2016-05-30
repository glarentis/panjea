/**
 *
 */
package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.interfaces.IAreaDocumentiService;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.FatturatoDTO;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * @author Leonardo
 * 
 */
@Local
public interface AreaContabileManager extends IAreaDocumentiService {

	/**
	 * Aggiorna tutte le aree contabili contenute nel giornale. Porta a verificato lo stato di tutte le aree contabili
	 * che non hanno parte iva oppure che hanno parte iva attiva e risultano stampate a registro (il registro iva può
	 * essere cmq non valido)
	 * 
	 * @param mapAreeContabili
	 *            mappe conteneti le aree contabili da aggiorna
	 * @throws ContabilitaException
	 *             rilancia un errore generico
	 */
	void aggiornaAreeContabiliPerGiornale(Map<AreaContabileDTO, Integer> mapAreeContabili) throws ContabilitaException;

	/**
	 * Aggiorna le aree contabili settandole stampate su registro iva e impostando l'anno con l'anno del registro iva
	 * (giornaleIva).
	 * 
	 * @param giornaleIva
	 *            registro iva in cui sono stampati i documenti
	 * @param mapAreeContabili
	 *            documenti presenti nel registro iva scelto
	 */
	void aggiornaAreeContabiliPerRegistroIva(GiornaleIva giornaleIva, Map<AreaContabileDTO, Integer> mapAreeContabili);

	/**
	 * Aggiorna tutte le righe contabili contenute nel giornale. Attenzione: il salvataggio di ogni riga contabile non
	 * esegue il flush sulla sessione
	 * 
	 * @param mapRigheContabili
	 *            mappa contenente le righe contabili da aggiornare
	 * @throws ContabilitaException
	 *             rilancia un errore generico
	 */
	void aggiornaRigheContabiliPerGiornale(Map<RigaContabileDTO, List<Integer>> mapRigheContabili)
			throws ContabilitaException;

	/**
	 * metodo che restituisce {@link AreaContabile}.
	 * 
	 * @param id
	 *            id dell'area contabile
	 * @return area contabile da caricare
	 */
	AreaContabile caricaAreaContabile(Integer id);

	/**
	 * Esegue la ricerca di {@link AreaContabile} attraverso l'attributo chiaveImportazione.
	 * 
	 * @param chiaveImportazione
	 *            chiave esterna di importazione (es. codice Europa)
	 * @return area contabile caricata
	 * @throws ContabilitaException
	 *             rilancia un errore generico
	 */
	AreaContabile caricaAreaContabileByChiaveImportazione(String chiaveImportazione) throws ContabilitaException;

	/**
	 * Carica {@link AreaContabile} attraverso il legame con il {@link Documento} identificato dall'argomento
	 * idDocumento.
	 * 
	 * @param idDocumento
	 *            id documento dell'area contabile
	 * @return area contabile legata al documento
	 */
	AreaContabile caricaAreaContabileByDocumento(Integer idDocumento);

	/**
	 * Restituisce un {@link AreaContabileLite}.
	 * 
	 * @param documento
	 *            per cui caricare il {@link AreaContabileLite}
	 * @return {@link AreaContabileLite}
	 */
	AreaContabileLite caricaAreaContabileLiteByDocumento(Documento documento);

	/**
	 * metodo di ricerca di {@link AreaContabile}.
	 * 
	 * @return lista {@link AreaContabile}
	 * @throws ContabilitaException
	 *             standard exception
	 */
	List<AreaContabile> caricaDocumentiContabili() throws ContabilitaException;

	/**
	 * Carica il fatturato in base ai parametri specificati.
	 * 
	 * @param annoCompetenza
	 *            anno di competenza
	 * @param dataRegistrazioneIniziale
	 *            data di registrazione iniziale
	 * @param dataRegistrazioneFinale
	 *            data di registrazione finale
	 * @param tipoEntita
	 *            tipo di entita ( CLIENTE o FORNITORE )
	 * @return fatturato
	 */
	List<FatturatoDTO> caricaFatturato(Integer annoCompetenza, Date dataRegistrazioneIniziale,
			Date dataRegistrazioneFinale, TipoEntita tipoEntita);

	/**
	 * Carica il fatturato diviso per sede in base ai parametri specificati.
	 * 
	 * @param annoCompetenza
	 *            anno di competenza
	 * @param dataRegistrazioneIniziale
	 *            data di registrazione iniziale
	 * @param dataRegistrazioneFinale
	 *            data di registrazione finale
	 * @param tipoEntita
	 *            tipo di entita ( CLIENTE o FORNITORE )
	 * @return fatturato
	 */
	List<FatturatoDTO> caricaFatturatoPerSede(Integer annoCompetenza, Date dataRegistrazioneIniziale,
			Date dataRegistrazioneFinale, TipoEntita tipoEntita);

	/**
	 * metodo che carica {@link RigaContabile}.
	 * 
	 * @param id
	 *            della {@link RigaContabile}
	 * @return {@link RigaContabile}
	 * @throws ContabilitaException
	 *             Standard Exception
	 */
	RigaContabile caricaRigaContabile(Integer id) throws ContabilitaException;

	/**
	 * 
	 * @param id
	 *            id riga da caricare
	 * @return rigaContabile con le variabile lazy non inizializzate
	 */
	RigaContabile caricaRigaContabileLazy(Integer id);

	/**
	 * metodo che esegue il caricamento delle righe contabili dato l'identificativo dell' {@link AreaContabile}.
	 * 
	 * @param idAreaContabile
	 *            della {@link AreaContabile}.
	 * @return lista {@link RigaContabile}
	 */
	List<RigaContabile> caricaRigheContabili(Integer idAreaContabile);

	/**
	 * metodo che esegue il caricamento delle righe contabili dato l'identificativo di {@link AreaContabile} e
	 * {@link SottoConto}.
	 * 
	 * @param idAreaContabile
	 *            della {@link AreaContabile}
	 * @param sottoConto
	 *            del {@link SottoConto}
	 * @return lista {@link RigaContabile}
	 */
	List<RigaContabile> caricaRigheContabiliPerSottoConto(Integer idAreaContabile, SottoConto sottoConto);

	/**
	 * Caica tutti i tipo documento in base al tipo registro.
	 * 
	 * @param tipoRegistro
	 *            tipo registro a caricare.
	 * @return lista {@link TipoDocumento}
	 */
	List<TipoDocumento> caricaTipiDocumentoByTipoRegistro(TipoRegistro tipoRegistro);

	/**
	 * Verifica e invalida se necessario le aree presenti e i documenti contabili collegati.
	 * 
	 * @param areaContabile
	 *            area contabile da invalidare
	 * @return areaContabile la nuova area contabile aggiornata
	 * @throws ContabilitaException
	 *             standard exception
	 */
	AreaContabile checkInvalidaAreeCollegate(AreaContabile areaContabile) throws ContabilitaException;

	/**
	 * Invalida un'area contabile portando allo stato di non validato le righe contabili. Lo stato viene portato da
	 * confermato in provvisorio.
	 * 
	 * @param areaContabile
	 *            da inavlidare.
	 * @return {@link AreaContabile} invalidata.
	 */
	AreaContabile invalidaAreaContabile(AreaContabile areaContabile);

	/**
	 * Invalida un'area contabile portando allo stato di non validato le righe contabili. Lo stato viene portato da
	 * confermato in provvisorio se eseguo.
	 * 
	 * @param areaContabile
	 *            {@link AreaContabile}
	 * @param changeConfermatoToProvvisorio
	 *            nel caso seva cambiar i documenti confermati in provisori.
	 * @return {@link AreaContabile} inavlidata
	 */
	AreaContabile invalidaAreaContabile(AreaContabile areaContabile, boolean changeConfermatoToProvvisorio);

	/**
	 * Verifica se esistono righe contabili per l'area.
	 * 
	 * @param areaContabile
	 *            in cui cercare se ci sono righe contabili.
	 * @return sucesso nella coso ci siano {@link RigaContabile}
	 * @throws ContabilitaException
	 *             standard Exception
	 */
	boolean isRigheContabiliPresenti(AreaContabile areaContabile) throws ContabilitaException;

	/**
	 * Restituisce tutti i documenti nell'anno legati alle liquidazioni.
	 * 
	 * @param anno
	 *            l'anno interessato
	 * @return lista contenente le areeContabili utilizzate per le liquidazioni
	 * @throws TipoDocumentoBaseException
	 *             TipiDocumentoBaseException
	 */
	List<AreaContabileDTO> ricercaAreaContabilePerLiquidazione(Integer anno) throws TipoDocumentoBaseException;

	/**
	 * metodo di ricerca per il controllo delle aree contabili.
	 * 
	 * @param parametriRicerca
	 *            {@link ParametriRicercaMovimentiContabili}
	 * @return lista {@link RigaContabileDTO}
	 */
	List<RigaContabileDTO> ricercaControlloAreeContabili(ParametriRicercaMovimentiContabili parametriRicerca);

	/**
	 * Salva una {@link AreaContabile} e la restituisce aggiornata, viene eseguita per prima cosa la verifica univocita'
	 * dell'area secondo i parametri data documento, numero documento, codice entita'(o banca), tipo entita'.
	 * 
	 * @param areaContabile
	 *            area contabile da salvare
	 * @param eseguiRollBack
	 *            esegue il rollback della transazione
	 * @return area contabile salvata
	 * @throws AreaContabileDuplicateException
	 *             AreaContabileDuplicateException
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	AreaContabile salvaAreaContabile(AreaContabile areaContabile, boolean eseguiRollBack) throws ContabilitaException,
			AreaContabileDuplicateException, DocumentoDuplicateException;

	/**
	 * Salva una {@link AreaContabile} e la restituisce aggiornata, non vengono eseguiti controlli di univocita' o di
	 * altra sorta per rendere il piu' agevole possibile il salvataggio; deve essere chiamato questo metodo solo nel
	 * caso in cui sono sicuro di poter saltare i controlli e dove non ho bisogno di area iva o altre aree associate.<br>
	 * Es.: creazione del documento di apertura,chiusura conti dove viene gia' eseguito il controllo sull'esistenza di
	 * quel tipo documento e dove per il salvataggio non deve essere invalidato il libro giornale.
	 * 
	 * @param areaContabile
	 *            {@link AreaContabile}
	 * @return {@link AreaContabile}
	 * @throws ContabilitaException
	 *             standard exception
	 * @throws AreaContabileDuplicateException
	 *             eccezione per il areacontabile duplicata
	 * @throws DocumentoDuplicateException
	 *             eccezione per documeto duplicato
	 */
	AreaContabile salvaAreaContabileNoCheck(AreaContabile areaContabile) throws ContabilitaException,
			AreaContabileDuplicateException, DocumentoDuplicateException;

	/**
	 * metodo che salva {@link RigaContabile} ed esegue i controlli per l'invalidazione delle aree collegate e i
	 * documenti contabili legati (libro giornale).
	 * 
	 * @param rigaContabile
	 *            {@link RigaContabile}
	 * @return {@link RigaContabile}
	 */
	RigaContabile salvaRigaContabile(RigaContabile rigaContabile);

	/**
	 * Metodo che salva {@link RigaContabile} senza eseguire alcun controllo per l'invalidazione aree e documenti
	 * contabili collegati.
	 * 
	 * @param rigaContabile
	 *            {@link RigaContabile}
	 * @return {@link RigaContabile} salvata
	 */
	RigaContabile salvaRigaContabileNoCheck(RigaContabile rigaContabile);

	/**
	 * Esegue la validazione delle righe contabili controllando la quadratura del documento e assegnando lo stato di
	 * confermato all'area contabile del documento.
	 * 
	 * @param areaContabile
	 *            a cui validare le righe
	 * @return {@link AreaContabile} validata
	 * @throws RigheContabiliNonValidiException
	 *             eccezione per la validita delle righe contabili
	 * @throws ContabilitaException
	 *             standard exception
	 */
	AreaContabile validaRigheContabili(AreaContabile areaContabile) throws ContabilitaException,
			RigheContabiliNonValidiException;

	/**
	 * Esegue la validazione delle righe contabili controllando la quadratura del documento e assegnando lo stato di
	 * confermato all'area contabile del documento.
	 * 
	 * @param areaContabile
	 *            a cui validare le righe
	 * @param controllaRigheValide
	 *            controlla se le righe sono valide
	 * @return {@link AreaContabile} validata
	 * @throws RigheContabiliNonValidiException
	 *             eccezione per la validita delle righe contabili
	 * @throws ContabilitaException
	 *             standard exception
	 */
	AreaContabile validaRigheContabili(AreaContabile areaContabile, boolean controllaRigheValide)
			throws ContabilitaException, RigheContabiliNonValidiException;
}
