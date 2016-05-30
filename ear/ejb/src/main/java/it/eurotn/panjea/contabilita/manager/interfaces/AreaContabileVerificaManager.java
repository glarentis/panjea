package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.iva.domain.AreaIva;

import javax.ejb.Local;

/**
 * Interfaccia di utilita' per verificare/invalidare parti iva o contabile, giornale o registro iva.
 * 
 * @author Leonardo
 * @version 1.0, 27/mar/08
 */
@Local
public interface AreaContabileVerificaManager {

	/**
	 * Crea l'area iva di una area contabile (se non esiste) e se gia' esistente verifica se e' necessario invalidarla.
	 * 
	 * @param areaContabileOld
	 *            area contabile precedente
	 * @param areaContabileNew
	 *            area contabile da invalidare
	 */
	void checkCreaInvalidaAreaIva(AreaContabile areaContabileOld, AreaContabile areaContabileNew);

	/**
	 * Metodo da utilizzare per invalidare sia giornale che registro iva, cmq ogni metodo chiamato sul manager relativo
	 * esegue l'invalidazione secondo alcune condizioni.
	 * 
	 * @param areaContabileOld
	 *            area contabile precedente, pu� essere null
	 * @param areaContabileNew
	 *            l'area contabile da invalidare
	 * @throws ContabilitaException
	 *             eccezione generica
	 */
	void checkInvalidaDocumentiContabilita(AreaContabile areaContabileOld, AreaContabile areaContabileNew)
			throws ContabilitaException;

	/**
	 * Invalida l'area contabile.
	 * 
	 * @param areaContabile
	 *            l'area da invalidare
	 * @param changeConfermatoToProvvisorio
	 *            cambiare lo stato documento a provvisorio (true o false) return AreaContabile
	 * @return areacontabile invalidata
	 * 
	 */
	AreaContabile invalidaAreaContabile(AreaContabile areaContabile, boolean changeConfermatoToProvvisorio);

	/**
	 * Invalida se c'� l'area iva dell'area contabile associata.
	 * 
	 * @param areaContabile
	 *            l'area contabile di cui cercare l'area iva legata
	 * @return AreaIva o null se non c'e' area iva per l'area contabile passata
	 */
	AreaIva invalidaAreaIva(AreaContabile areaContabile);

	/**
	 * Carica l'areaRate associata al documento dell'areaContabile corrente e se presente la invalida.
	 * 
	 * @param areaContabile
	 *            area contabile legata all'area rate da invalidare
	 */
	void invalidaAreaRate(AreaContabile areaContabile);

	/**
	 * Metodo che verifica le condizioni ed eventualmente si preoccupa di chiamare l'invalidazione del giornale.
	 * 
	 * 
	 * @param areaContabileOld
	 *            prima della modifica
	 * @param areaContabileNew
	 *            dopo la modifica
	 * @throws ContabilitaException
	 *             eccezione generica
	 */
	void invalidaLibroGiornale(AreaContabile areaContabileOld, AreaContabile areaContabileNew)
			throws ContabilitaException;

	/**
	 * Metodo che verifica le condizioni ed eventualmente si preoccupa di chiamare l'invalidazione del registro iva
	 * dell'area contabile.
	 * 
	 * 
	 * @param areaContabileOld
	 *            prima della modifica
	 * @param areaContabileNew
	 *            dopo la modifica
	 */
	void invalidaRegistroIva(AreaContabile areaContabileOld, AreaContabile areaContabileNew);

	/**
	 * Verifica sele data documento sono diverse.
	 * 
	 * @param areaContabileOld
	 *            prima della modifica
	 * @param areaContabileNew
	 *            dopo la modifica
	 * @return true se diverse
	 */
	boolean isDateDocumentoDiverse(AreaContabile areaContabileOld, AreaContabile areaContabileNew);

	/**
	 * Verifica se la parte iva dell'area contabile e' attiva.
	 * 
	 * @param areaContabile
	 *            area con le righe iva
	 * @return true se righe iva abilitate
	 */
	boolean isRigheIvaEnabled(AreaContabile areaContabile);

	/**
	 * Verifica se i totali documento sono diversi.
	 * 
	 * @param areaContabileOld
	 *            prima della modifica
	 * @param areaContabileNew
	 *            dopo la modifica
	 * @return true or false
	 */
	boolean isTotaliDiversi(AreaContabile areaContabileOld, AreaContabile areaContabileNew);
}
