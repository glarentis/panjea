package it.eurotn.panjea.rate.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.rate.domain.AreaRate;

import javax.ejb.Local;

@Local
public interface AreaRateManager {
	/**
	 * Cancella l'area rate.
	 * 
	 * @param documento
	 *            che ha l'area rate
	 */
	void cancellaAreaRate(Documento documento);

	/**
	 * Carica {@link AreaRate} con il documento a cui appartiene.
	 * 
	 * @param documento
	 *            {@link Documento}
	 * @return areaRate {@link AreaRate}
	 */
	AreaRate caricaAreaRate(Documento documento);

	/**
	 * Carica {@link AreaRate} solo con il proprio ID.
	 * 
	 * @param idAreaRate
	 *            l'id dell' {@link AreaRate} da caricare
	 * @return areaRate {@link AreaRate}
	 */
	AreaRate caricaAreaRate(Integer idAreaRate);

	/**
	 * Controlla se nell'area rate passata come parametro cambia lo sconto commerciale rispetto a quella esistente.
	 * 
	 * @param areaRate
	 *            area rate
	 * @return <code>true</code> se la percentuale di sconto commerciale è diversa
	 */
	boolean checkCambioScontoCommerciale(AreaRate areaRate);

	/**
	 * Verifica lo stato dell'area Rate, se e' valida la invalida e se richiesto cambia stato al documento.
	 * 
	 * @param areaRate
	 *            l'area da invalidare
	 * @param cambiaStatoDocumento
	 *            se devo cambiare sto al Documento
	 * @return l'area rate invalidata
	 */
	AreaRate checkInvalidaAreaRate(AreaRate areaRate, Boolean cambiaStatoDocumento);

	/**
	 * Metodo per salvare l'area rate.
	 * 
	 * @param areaRate
	 *            da salvare
	 * @return areaRate salvata area NON prevista
	 */
	AreaRate salvaAreaRate(AreaRate areaRate);

	/**
	 * Metodo per validare l'area rate in contabilita'.
	 * 
	 * @param areaRate
	 *            da validare
	 * @param areaContabile
	 *            del documento
	 * @return areaRate validata
	 */
	AreaRate validaAreaRate(AreaRate areaRate, AreaContabile areaContabile);

	/**
	 * Metodo per validare l'area rate nel magazzino'.
	 * 
	 * @param areaRate
	 *            da validare
	 * @param areaMagazzino
	 *            del documento
	 * @return areaRate validata
	 */

	AreaRate validaAreaRate(AreaRate areaRate, AreaMagazzino areaMagazzino);

}
