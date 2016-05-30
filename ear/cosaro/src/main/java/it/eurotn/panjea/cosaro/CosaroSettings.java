package it.eurotn.panjea.cosaro;

import java.io.File;

import javax.ejb.Local;

import org.beanio.StreamFactory;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

@Local
public interface CosaroSettings {
    /**
     *
     * @return codice del cliente coop
     */
    String caricaCodiceCoop();

    /**
     *
     * @return codice td coop
     */
    String caricaCodiceTipoDocumentoCoop();

    /**
     *
     * @return tipoDocumentoUnicomm
     */
    String caricaCodiceTipoDocumentoUnicomm();

    /**
     *
     * @return codice del cliente unicomm
     */
    String caricaCodiceUnicomm();

    /**
     *
     * @return percorso dove esportare il file per le bilance
     */
    String caricaFilePathBilanceExport();

    /**
     *
     * @return tipoAreaMagazzino settata per il carico produzione
     */
    TipoAreaMagazzino caricaTamProduzione();

    /**
     *
     * @param key
     *            key preference
     * @return File della cartelal se trovata, null se non trovata
     */
    File getPreferenceDir(String key);

    /**
     *
     * @param templateFileName
     *            nome del template
     * @return Stream aperto del template
     */
    StreamFactory getStreamTemplate(String templateFileName);

    /**
     *
     * @return true se gammameat è abilitato
     */
    boolean isGammaMeatEnable();

}
