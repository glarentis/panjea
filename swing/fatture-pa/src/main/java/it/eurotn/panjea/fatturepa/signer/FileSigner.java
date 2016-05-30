package it.eurotn.panjea.fatturepa.signer;

import it.eurotn.panjea.fatturepa.domain.XMLFatturaPA;

public interface FileSigner {

    /**
     * @param path
     *            path del file eseguibile di firma
     */
    void setSoftwarePath(String path);

    /**
     * Segna il file con il pin specificato.
     *
     * @param fileXML
     *            file da firmare
     * @param pin
     *            pin
     * @return risultati del processo di firma del file
     */
    SignResult signFile(XMLFatturaPA fileXML, String pin);

}
