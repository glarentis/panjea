package it.eurotn.panjea.contabilita.manager.ivasospesa.interfaces;

import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface IvaSospesaManager {

	/**
	 * Carica i totali codice iva sospesa per il periodo. I valori presentati sono la somma di imponibile e imposta
	 * pagati effettivamente nel periodo.<br>
	 * Rif. <code>USE CASE 1050,1763,1764,1765</code>
	 * 
	 * @param liquidazioneIvaDTO
	 *            liquidazioneIvaDTO
	 * @param dataInizioPeriodo
	 *            dataInizioPeriodo
	 * @param dataFinePeriodo
	 *            dataFinePeriodo
	 * @return {@link LiquidazioneIvaDTO}, i risultati sono integrati nella liquidazione iva, divido quindi la lista di
	 *         righe iva divise per registro iva
	 */
	LiquidazioneIvaDTO aggiungiTotaliCodiceIvaSospesa(LiquidazioneIvaDTO liquidazioneIvaDTO, Date dataInizioPeriodo,
			Date dataFinePeriodo);

}
