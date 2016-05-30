package it.eurotn.panjea.corrispettivi.service.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.TotaliCodiceIvaDTO;

@Remote
public interface CorrispettiviService {

    /**
     * Cancella un {@link Corrispettivo}.
     *
     * @param id
     *            id Corrispettivo da cancellare
     */
    void cancellaCorrispettivo(Integer id);

    /**
     * Cancella un {@link CorrispettivoLinkTipoDocumento}.
     *
     * @param id
     *            id CorrispettivoLinkTipoDocumento da cancellare
     */
    void cancellaCorrispettivoLinkTipoDocumento(Integer id);

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
     * Carica tutti i {@link Corrispettivo} presenti.
     *
     * @return {@link Corrispettivo} caricati
     */
    List<Corrispettivo> caricaCorrispettivi();

    /**
     * Carica tutti i {@link CorrispettivoLinkTipoDocumento} presenti.
     *
     * @return {@link CorrispettivoLinkTipoDocumento} caricati
     */
    List<CorrispettivoLinkTipoDocumento> caricaCorrispettiviLinkTipoDocumento();

    /**
     * Carica un {@link Corrispettivo} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Corrispettivo} caricato
     */
    Corrispettivo caricaCorrispettivoById(Integer id);

    /**
     * Carica un {@link CorrispettivoLinkTipoDocumento} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link CorrispettivoLinkTipoDocumento} caricato
     */
    CorrispettivoLinkTipoDocumento caricaCorrispettivoLinkTipoDocumentoById(Integer id);

    /**
     * Carica tutti i tipo documento gestiti dai corrispettivi.
     *
     * @return tipi documento
     */
    List<TipoDocumento> caricaTipiDocumentoCorrispettivi();

    /**
     * Carica i totali raggruppati per codice iva in base al giornale corrispettivi.
     *
     * @param calendarioCorrispettivo
     *            calendario di riferimento
     * @return totali
     */
    List<TotaliCodiceIvaDTO> caricaTotaliCalendarioCorrispettivi(CalendarioCorrispettivo calendarioCorrispettivo);

    /**
     * Crea tutti i documenti in base ai corrispettivi contenuti nel calendario.
     *
     * @param calendarioCorrispettivo
     *            calendarioCorrispettivo
     * @return {@link CalendarioCorrispettivo} aggiornato
     */
    CalendarioCorrispettivo creaDocumenti(CalendarioCorrispettivo calendarioCorrispettivo);

    /**
     * Importa i corrispettivi per la data indicata.
     *
     * @param data
     *            data di importazione
     */
    void importa(Date data);

    /**
     * Salva un {@link Corrispettivo}.
     *
     * @param corrispettivo
     *            {@link Corrispettivo} da salvare
     * @return {@link Corrispettivo} salvato
     */
    Corrispettivo salvaCorrispettivo(Corrispettivo corrispettivo);

    /**
     * Salva un {@link CorrispettivoLinkTipoDocumento}.
     *
     * @param corrispettivoLinkTipoDocumento
     *            {@link CorrispettivoLinkTipoDocumento} da salvare
     * @return {@link CorrispettivoLinkTipoDocumento} salvato
     */
    CorrispettivoLinkTipoDocumento salvaCorrispettivoLinkTipoDocumento(
            CorrispettivoLinkTipoDocumento corrispettivoLinkTipoDocumento);

}
