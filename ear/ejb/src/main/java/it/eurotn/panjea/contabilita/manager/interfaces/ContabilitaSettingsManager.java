package it.eurotn.panjea.contabilita.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;

@Local
public interface ContabilitaSettingsManager {

    /**
     * Cancella un codice iva corrispettivo.
     *
     * @param codiceIvaCorrispettivo
     *            {@link CodiceIvaCorrispettivo} da cancellare
     */
    public void cancellaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo);

    /**
     * Carica tutti i codici iva corrispettivo per il tipo documento selezionato.
     *
     * @param tipoDocumento
     *            tipo documento
     * @return codici caricati
     */
    public List<CodiceIvaCorrispettivo> caricaCodiciIvaCorrispettivo(TipoDocumento tipoDocumento);

    /**
     * Carica il settings della contabilità.<br/>
     * Se non esiste ne crea uno, lo salva e lo restituisce
     *
     * @return <code>ContabilitaSettings</code> caricato
     */
    public ContabilitaSettings caricaContabilitaSettings();

    /**
     * Salva un codice iva corrispettivo.
     *
     * @param codiceIvaCorrispettivo
     *            codice da salvare
     * @return codice salvato
     */
    public CodiceIvaCorrispettivo salvaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo);

    /**
     * Salva un {@link ContabilitaSettings}.
     *
     * @param contabilitaSettings
     *            contabilita settings
     * @return <code>ContabilitaSettings</code> salvato
     */
    public ContabilitaSettings salvaContabilitaSettings(ContabilitaSettings contabilitaSettings);

}
