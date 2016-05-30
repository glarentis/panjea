package it.eurotn.panjea.vending.rest.service.interfaces;

import java.io.OutputStream;

import javax.ejb.Remote;

import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;

@Remote
public interface VendingPalmariRestService {

    /**
     *
     * @param codiceOperatore
     *            codiceOperatore
     * @param output
     *            strea per scrivere le query sql per sincronizzare l'intero database del palmare
     */
    public void esporta(OutputStream output, String codiceOperatore);

    /**
     *
     * @param codiceOperatore
     *            codice operatore
     * @param contenutoFile
     *            file xml
     * @return ??
     * @throws ImportazioneException
     *             errore durante l'improatzione
     */
    public String importa(String codiceOperatore, byte[] contenutoFile) throws ImportazioneException;
}
