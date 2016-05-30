package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.math.BigDecimal;

import javax.ejb.Local;

@Local
public interface AreaAnticipoFattureContabilitaManager {

	/**
	 * Calcola l'importo da anticipare prendendo il totale imponibile e iva sul documento di origine della rata.<br>
	 * 
	 * importoAnticipato = percentualeAnticipoFatture di tot.imponibile + percentualeIvaAnticipoFatture di tot imposta
	 * 
	 * @param pagamento
	 *            pagamento
	 * @param percentualeAnticipoFatture
	 *            percentualeAnticipoFatture
	 * @param percentualeIvaAnticipoFatture
	 *            percentualeIvaAnticipoFatture
	 * @return importoAnticipato
	 */
	BigDecimal calcolaImportoAnticipato(Pagamento pagamento, BigDecimal percentualeAnticipoFatture,
			BigDecimal percentualeIvaAnticipoFatture);

	/**
	 * Carica il conto anticipi fatture cercando prima il conto anticipi impostato sul rapporto bancario azienda; nel
	 * caso in cui non fosse impostato, carica il conto anticipi aasociato al conto base anticipi.
	 * 
	 * @param rapportoBancarioAzienda
	 *            rapportoBancarioAzienda
	 * @return SottoConto
	 */
	SottoConto caricaSottoContoAnticipiFatture(RapportoBancarioAzienda rapportoBancarioAzienda);

	/**
	 * Creazione dell'area contabile per l'anticipo fatture.<br>
	 * L'importo della riga contabile deve essere la percentuale impostata sul rapporto bancario.
	 * 
	 * @param areaPagamenti
	 *            area pagamenti
	 * @param parametriCreazioneAreaChiusure
	 *            .
	 * @return area pagamenti
	 * @throws PagamentiException
	 *             eccezione
	 */
	AreaPagamenti creaAreaContabileAnticipoFatture(AreaPagamenti areaPagamenti,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws PagamentiException;

}
