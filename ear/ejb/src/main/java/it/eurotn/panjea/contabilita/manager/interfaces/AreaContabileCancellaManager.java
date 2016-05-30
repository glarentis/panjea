/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;

import java.util.List;

import javax.ejb.Local;

/**
 * @author Leonardo
 * 
 */
@Local
public interface AreaContabileCancellaManager {

	/**
	 * metodo che cancella {@link AreaContabile} e invalida le aree collegate e i documenti contabili se ci sono le
	 * condizioni
	 * 
	 * @param areaContabile
	 * @throws DocumentiCollegatiPresentiException
	 * @throws AreeCollegatePresentiException
	 */
	public void cancellaAreaContabile(AreaContabile areaContabile) throws DocumentiCollegatiPresentiException,
			AreeCollegatePresentiException;

	/**
	 * 
	 * @param areaContabile
	 * @param deleteAreeCollegate
	 * @throws DocumentiCollegatiPresentiException
	 * @throws AreeCollegatePresentiException
	 */
	public void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate)
			throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException;

	/**
	 * 
	 * @param areaContabile
	 * @param deleteAreeCollegate
	 * @param forceDeleteAreeCollegate
	 * @throws DocumentiCollegatiPresentiException
	 * @throws AreeCollegatePresentiException
	 */
	public void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate,
			boolean forceDeleteAreeCollegate) throws DocumentiCollegatiPresentiException,
			AreeCollegatePresentiException;

	/**
	 * metodo che cancella {@link AreaContabile} dal suo id invalidando areeCollegate e documenti contabili se ci sono
	 * le condizioni
	 * 
	 * @param documento
	 * @throws DocumentiCollegatiPresentiException
	 * @throws AreeCollegatePresentiException
	 */
	public void cancellaAreaContabile(Documento documento) throws DocumentiCollegatiPresentiException,
			AreeCollegatePresentiException;

	/**
	 * 
	 * @param documento
	 * @param forceDeleteAreeCollegate
	 * @throws DocumentiCollegatiPresentiException
	 * @throws AreeCollegatePresentiException
	 */
	public void cancellaAreaContabile(Documento documento, boolean forceDeleteAreeCollegate)
			throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException;

	/**
	 * metodo che cancella {@link AreaContabile} senza eseguire alcun controllo eo invalidazione sulle aree e documenti
	 * contabili collegati
	 * 
	 * @param areaContabile
	 */
	public void cancellaAreaContabileNoCheck(AreaContabile areaContabile);

	/**
	 * metodo che cancella {@link RigaContabile}
	 * 
	 * @param rigaContabile
	 * @param area
	 *            contabile
	 */
	public AreaContabile cancellaRigaContabile(RigaContabile rigaContabile);

	/**
	 * Cancella le righe contabili.
	 * 
	 * @param areaContabile
	 *            area di riferimento
	 * @return area contabile
	 */
	AreaContabile cancellaRigheContabili(AreaContabile areaContabile);

	/**
	 * Cancella le righe contabili.
	 * 
	 * @param righeContabili
	 *            righe da cancellare
	 * @return area contabile
	 */
	AreaContabile cancellaRigheContabili(List<RigaContabile> righeContabili);

}
