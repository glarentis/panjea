package it.eurotn.panjea.contabilita.manager.liquidazione.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.TipoDocumentoNonConformeException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings.ETipoPeriodicita;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.util.Date;

import javax.ejb.Local;

@Local
public interface LiquidazionePagamentiManager {

	/**
	 * Cancella il pagamento se e solo se l'area tesoreria collegata è una AreaAcconto.
	 * 
	 * @param pagamento
	 *            il pagamento da cancellare
	 */
	void cancellaPagamentoAccontoLiquidazione(Pagamento pagamento);

	/**
	 * Cancella, se esiste, il pagamento sul documento di liquidazione del mese precedente a quello ricevuto come
	 * parametro.<br>
	 * Se il tipo documento base di liquidazione non e' impostato non viene rilanciata nessuna eccezione.
	 * 
	 * @param documento
	 *            il documento da cui ricavare il pagame
	 */
	void cancellaPagamentoLiquidazionePrecedente(Documento documento);

	/**
	 * Carica la rata della liquidazione precedente.
	 * 
	 * @param mese
	 *            mese
	 * @param anno
	 *            anno
	 * @return Rata
	 * @throws TipoDocumentoBaseException
	 *             se manca la configurazione dei tipidocumento base per la contabilita'
	 */
	Rata caricaRataLiquidazionePrecedente(int mese, int anno) throws TipoDocumentoBaseException;

	/**
	 * Restituisce il valore totale dell'acconto iva se il periodo e' nel mese di dicembre con periodicita' mensile o
	 * nel quarto trimenstre se trimestrale.<br>
	 * Altrimenti restituisce null.
	 * 
	 * @param periodicitaLiquidazione
	 *            tipo periodicita' per la liquidazione
	 * @param dataInizioPeriodo
	 *            data inizio periodo
	 * @param dataFinePeriodo
	 *            data fine periodo
	 * @return il totale calcolato per l'acconto iva di dicembre
	 */
	BigDecimal caricaTotaleAccontoIvaDicembre(ETipoPeriodicita periodicitaLiquidazione, Date dataInizioPeriodo,
			Date dataFinePeriodo);

	/**
	 * Crea l'area rate per il documento di liquidazione.
	 * 
	 * @param areaContabile
	 *            areaContabile
	 * @param codicePagamento
	 *            codicePagamento
	 * @return AreaRate
	 * @throws CodicePagamentoNonTrovatoException
	 *             codice pagamento non trovato
	 * @throws TipoDocumentoNonConformeException
	 *             problema nella configurazione del tipo documento
	 */
	AreaRate creaAreaRatePerDocumentoLiquidazione(AreaContabile areaContabile, CodicePagamento codicePagamento)
			throws CodicePagamentoNonTrovatoException, TipoDocumentoNonConformeException;

}
