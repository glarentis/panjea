package it.eurotn.panjea.fatturepa.signer;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings.SoftwareFirmaElettronica;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.signer.firmacerta.FirmaCertaFileSigner;

public final class FileSignerFactory {

    /**
     * Costruttore.
     */
    private FileSignerFactory() {
    }

    /**
     * @return signer da utilizzare per firmare i file.
     */
    public static FileSigner getFileSigner() {

        IFatturePAAnagraficaBD fattureAnagraficaBD = RcpSupport.getBean(FatturePAAnagraficaBD.BEAN_ID);
        FatturaPASettings settings = fattureAnagraficaBD.caricaFatturaPASettings();

        if (!settings.isValid()) {
            return null;
        }

        FileSigner fileSigner = null;
        if (settings.getSoftwareFirmaElettronica() == SoftwareFirmaElettronica.FIRMA_CERTA) {
            fileSigner = new FirmaCertaFileSigner();
            fileSigner.setSoftwarePath(settings.getSoftwareFirmaPath());
        }

        return fileSigner;
    }
}
