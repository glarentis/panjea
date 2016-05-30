package it.eurotn.panjea.rate.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IRateBD {
	/**
	 * cancella una rata.
	 *
	 * @param rata
	 *            rata da cancellare
	 */
	void cancellaRata(Rata rata);

	/**
	 *
	 * @param idAreaContabile
	 *            id dell'area contabile
	 * @return AreaContabileFullDTO
	 */
	@AsyncMethodInvocation
	AreaContabileFullDTO caricaAreaContabileFullDTO(Integer idAreaContabile);

	/**
	 *
	 * @param idAreaMagazzino
	 *            id dell'area magazzino
	 * @return AreaMagazzinoFullDTO
	 */
	@AsyncMethodInvocation
	AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(Integer idAreaMagazzino);

	/**
	 *
	 * @param idDocumento
	 *            id del documento
	 * @return areaRate per il documento
	 */
	AreaRate caricaAreaRateByDocumento(Integer idDocumento);

	/**
	 * Carica tutte le rate collegate a quella di riferimento.
	 *
	 * @param rata
	 *            rata di riferimento
	 * @return rate collegate
	 */
	List<Rata> caricaRateCollegate(Rata rata);

	/**
	 * Metodo che genera le rate delle partite secondo diverse strategie.<br>
	 * la scelta della STRATEGIA e' data dalla strutturaPartita.getTipoStrategiaDataScadenza()<br>
	 *
	 * @param codicePagamento
	 *            codicePagamento
	 * @param dataDocumento
	 *            data di inizio per il calcolo delle rate
	 * @param imponibile
	 *            imponibile per calcolare le rate
	 * @param iva
	 *            iva per calcolare le rate
	 * @param calendarioRate
	 *            calendario rate
	 * @return lista di rate generate
	 */
	@AsyncMethodInvocation
	List<Rata> generaRate(CodicePagamento codicePagamento, Date dataDocumento, BigDecimal imponibile, BigDecimal iva,
			CalendarioRate calendarioRate);

	/**
	 * Dalla rata da riemettere genera le nuove rate ad essa collegata. Solo se la rata è insoluta è possibile
	 * riemetterla.
	 *
	 * @param rataRiemessa
	 *            contiene la rata da riemettere e i valori delle rate da generare. Le rate da generare solo contenute
	 *            delle proprietà <code>rateDaCreare</code> di cui verranno usati i valori dell'importo e della data di
	 *            scadenza per la generazione
	 */
	void riemettiRate(RataRiemessa rataRiemessa);

	/**
	 * Salva una rata.
	 *
	 * @param rata
	 *            rata da salvare
	 * @return rata salvata
	 */
	Rata salvaRata(Rata rata);

	/**
	 * Salva una rata senza invalidare un documento.
	 *
	 * @param rata
	 *            rata da salvare
	 * @return rata salvata
	 */
	Rata salvaRataNoCheck(Rata rata);

	/**
	 *
	 * @param areaRate
	 *            area da validare
	 * @param areaDocumento
	 *            area documento di partenza, può essere {@link AreaContabile} o {@link AreaMagazzino}
	 * @return areaPartite validata.
	 */
	@AsyncMethodInvocation
	AreaPartite validaRateAreaPartita(AreaRate areaRate, IAreaDocumento areaDocumento);
}
