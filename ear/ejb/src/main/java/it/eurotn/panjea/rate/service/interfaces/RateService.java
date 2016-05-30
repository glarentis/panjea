package it.eurotn.panjea.rate.service.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.util.RataRV;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

/**
 *
 * Interfaccia remota del Service Rate.
 *
 * @author vittorio
 * @version 1.0, 25/nov/2009
 *
 */
@Remote
public interface RateService {

    /**
     * Associa il rapporto bancario dell'entita' definito sul documento alla rata.
     *
     * @param rata
     *            Rata
     * @param areaDocumento
     *            IAreaDocumento
     * @param tipoPagamento
     *            TipoPagamento
     * @param salvaRata
     *            se si deve fare il salvataggio della rata
     * @return Rata
     */

    Rata associaRapportoBancario(Rata rata, IAreaDocumento areaDocumento,
            TipoPagamento tipoPagamento, boolean salvaRata);

    /**
     * Cancella l'area rate.
     *
     * @param documento
     *            che ha l'area rate
     */
    void cancellaAreaRate(Documento documento);

    /**
     * Cancella la {@link Rata} solo se lo stato della rata e' aperto.
     *
     * @param rata
     *            la rata da eliminare
     * @throws DocumentiCollegatiPresentiException
     *             rilanciata quando ci sono delle rate gia' pagate
     * @throws PagamentiException
     *             eccezione generica
     */
    void cancellaRata(Rata rata) throws DocumentiCollegatiPresentiException, PagamentiException;

    /**
     * Cancella la {@link Rata} senza controlli.
     *
     * @param rata
     *            la rata da eliminare
     */

    void cancellaRataNoCheck(Rata rata);

    /**
     *
     * @param idAreaContabile
     *            id dell'area magazzino
     * @return AreaContabileFullDTO
     */
    AreaContabileFullDTO caricaAreaContabileFullDTO(Integer idAreaContabile);

    /**
     *
     * @param idAreaMagazzino
     *            id dell'area magazzino
     * @return AreaMagazzinoFullDTO
     */
    AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(Integer idAreaMagazzino);

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
     * Carica tutte le rate collegate a quella di riferimento.
     *
     * @param rata
     *            rata di riferimento
     * @return rate collegate
     */
    List<Rata> caricaRateCollegate(Rata rata);

    /**
     * Ricerca tutte le rate che devono essere stampate per la richiesta di versamento a mezzo
     * bonifico elettronico in base ai parametri.
     *
     * @param parametri
     *            parametri
     * @return lista di rate caricate
     */
    List<RataRV> caricaRatePerRichiestaVersamento(Map<Object, Object> parametri);

    /**
     * Ricerca tutte le rate che devono essere stampate per la richiesta di versamento a mezzo
     * bonifico elettronico in base ai parametri e le raggruppa per cliente.
     *
     * @param parametri
     *            parametri
     * @return lista di rate caricate
     */
    List<RataRV> caricaRateRaggruppatePerRichiestaVersamento(Map<Object, Object> parametri);

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
    List<Rata> generaRate(CodicePagamento codicePagamento, Date dataDocumento,
            BigDecimal imponibile, BigDecimal iva, CalendarioRate calendarioRate);

    /**
     * Metodo che genera un'arae rate le rate delle partite secondo diverse strategie.<br>
     * la scelta della STRATEGIA e' data dalla strutturaPartita.getTipoStrategiaDataScadenza()<br>
     *
     * @param areaDocumento
     *            IAreaDocumento
     * @return AreaRate
     */
    public AreaRate generaRate(IAreaDocumento areaDocumento);

    /**
     * Metodo che genera un'arae rate le rate delle partite secondo diverse strategie.<br>
     * la scelta della STRATEGIA e' data dalla strutturaPartita.getTipoStrategiaDataScadenza()<br>
     *
     * @param areaDocumento
     *            IAreaDocumento
     * @param areaRate
     *            AreaRate
     * @return AreaRate
     */
    AreaRate generaRate(IAreaDocumento areaDocumento, AreaRate areaRate);

    /**
     * Restituisce la lista di {@link Rata} secondo i parametri {@link ParametriRicercaRate}.
     *
     * @param parametriRicercaRate
     *            i {@link ParametriRicercaRate} per eseguire la ricerca
     * @return la lista di {@link SituazioneRata}
     */
    List<SituazioneRata> ricercaRate(ParametriRicercaRate parametriRicercaRate);

    /**
     * Dalla rata da riemettere genera le nuove rate ad essa collegata. Solo se la rata è insoluta è
     * possibile riemetterla.
     *
     * @param rataRiemessa
     *            contiene la rata da riemettere e i valori delle rate da generare. Le rate da
     *            generare solo contenute delle proprietà <code>rateDaCreare</code> di cui verranno
     *            usati i valori dell'importo e della data di scadenza per la generazione
     */
    void riemettiRate(RataRiemessa rataRiemessa);

    /**
     * Metodo per salvare l'area rate.
     *
     * @param areaRate
     *            da salvare
     * @return areaRate salvata
     */
    AreaRate salvaAreaRate(AreaRate areaRate);

    /**
     * Metodo che salva le modifiche alle rate (Individualmente), si preoccupa di invalidare l'area
     * collegata se valida.
     *
     * @param rata
     *            Rata
     * @return Rata
     */
    Rata salvaRata(Rata rata);

    /**
     * Metodo che salva la rata senza verifiche e invalidazione aree.
     *
     * @param rata
     *            Rata
     * @return Rata
     */
    Rata salvaRataNoCheck(Rata rata);

    /**
     * Metodo per validare l'area rate nel magazzino o nella contabilità.
     *
     * @param areaRate
     *            da validare
     * @param areaDocumento
     *            area magazzino o contabile del documento
     * @return areaRate validata
     */

    AreaRate validaAreaRate(AreaRate areaRate, IAreaDocumento areaDocumento);

}
