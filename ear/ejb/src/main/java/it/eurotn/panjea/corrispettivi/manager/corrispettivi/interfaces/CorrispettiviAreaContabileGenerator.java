package it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;

@Local
public interface CorrispettiviAreaContabileGenerator {

    /**
     * Crea tutti i documenti in base ai corrispettivi contenuti nel calendario.
     *
     * @param calendarioCorrispettivo
     *            calendarioCorrispettivo
     */
    void creaDocumenti(CalendarioCorrispettivo calendarioCorrispettivo);
}
