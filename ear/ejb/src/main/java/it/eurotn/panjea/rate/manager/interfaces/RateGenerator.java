package it.eurotn.panjea.rate.manager.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;

/**
 *
 * Classe per diminuire il fan di Rate manager. Serve a generare e calcolare le rate
 *
 * @author vittorio
 * @version 1.0, 25/nov/2009
 *
 */

@Local
public interface RateGenerator {
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
     *            Calendario rate
     * @return lista di rate generate
     */
    List<Rata> generaRate(CodicePagamento codicePagamento, Date dataDocumento,
            BigDecimal imponibile, BigDecimal iva, CalendarioRate calendarioRate);

    /**
     * Metodo che genera un'arae rate le rate delle partite secondo diverse strategie.<br>
     * la scelta della STRATEGIA e' data dalla strutturaPartita.getTipoStrategiaDataScadenza()<br>
     * Se non ho un area rate viene generato un errore.
     * 
     * @param areaDocumento
     *            IAreaDocumento documento con un area rate da poter caricare.
     * @return AreaRate
     */
    AreaRate generaRate(IAreaDocumento areaDocumento);

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

}
