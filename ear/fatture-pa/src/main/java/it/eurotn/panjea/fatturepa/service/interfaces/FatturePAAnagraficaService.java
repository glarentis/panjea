package it.eurotn.panjea.fatturepa.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.TipoRegimeFiscale;

/**
 * @author fattazzo
 *
 */
@Remote
public interface FatturePAAnagraficaService {

    /**
     * Carica la {@link AziendaFatturaPA} relativa all'azienda loggata.
     *
     * @return {@link AziendaFatturaPA} caricata
     */
    AziendaFatturaPA caricaAziendaFatturaPA();

    /**
     * Carica i settings della fattura PA. Se non esistono ne viene creato uno, salvato e lo restituito.
     *
     * @return settings
     */
    FatturaPASettings caricaFatturaPASettings();

    /**
     * Carica tutti i tipi di regimi fiscali.
     *
     * @return regimi caricati
     */
    List<TipoRegimeFiscale> caricaTipiRegimiFiscali();

    /**
     * Metodo utilizzato solamente per eseguire un test sul client per l'azienda loggata.
     *
     * @return log delle operazioni
     */
    String checkMailForTest();

    /**
     * Salva una {@link AziendaFatturaPA}.
     *
     * @param aziendaFatturaPA
     *            azienda da salvare
     * @return azienda salvata
     */
    AziendaFatturaPA salvaAziendaFatturaPA(AziendaFatturaPA aziendaFatturaPA);

    /**
     * Salva un {@link FatturaPASettings}.
     *
     * @param fatturaPaSettings
     *            settings da salvare
     * @return settings salvate
     */
    FatturaPASettings salvaFatturaPASettings(FatturaPASettings fatturaPaSettings);

}
