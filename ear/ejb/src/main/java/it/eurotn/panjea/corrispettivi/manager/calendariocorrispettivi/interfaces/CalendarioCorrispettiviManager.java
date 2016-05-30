package it.eurotn.panjea.corrispettivi.manager.calendariocorrispettivi.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.TotaliCodiceIvaDTO;

@Local
public interface CalendarioCorrispettiviManager {

    /**
     * Aggiorna tutti i giorni del calendario corrispettivi con i codici iva.
     *
     * @param anno
     *            anno di riferimento
     * @param mese
     *            mese di riferimento
     * @param tipoDocumento
     *            tipo documento
     */
    void aggiornaCodiciIvaCalendarioCorrispettivi(int anno, int mese, TipoDocumento tipoDocumento);

    /**
     * Carica calendario corrispettivo.
     *
     * @param anno
     *            anno
     * @param mese
     *            mese
     * @param tipoDocumento
     *            tipoDocumento
     * @return CalendarioCorrispettivo
     */
    CalendarioCorrispettivo caricaCalendarioCorrispettivo(int anno, int mese, TipoDocumento tipoDocumento);

    /**
     * Carica i totali raggruppati per codice iva in base al giornale corrispettivi.
     *
     * @param calendarioCorrispettivo
     *            calendario di riferimento
     * @return totali
     */
    List<TotaliCodiceIvaDTO> caricaTotaliCalendarioCorrispettivi(CalendarioCorrispettivo calendarioCorrispettivo);
}
