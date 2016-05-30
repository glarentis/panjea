/**
 * 
 */
package it.eurotn.panjea.rich.file;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoSpedizione;

import org.springframework.richclient.util.Assert;

/**
 * @author fattazzo
 * 
 */
public final class FileTransportFactory {

    private static FileTransportFactory instance = null;

    /**
     * @return {@link FileTransportFactory} instance
     */
    public static FileTransportFactory getInstance() {
        if (instance == null) {
            instance = new FileTransportFactory();
        }
        return instance;
    }

    /**
     * Costruttore.
     */
    private FileTransportFactory() {

    }

    /**
     * Restituisce il {@link FileTransport} corretto per il tipo di spedizione.
     * 
     * @param tipoSpedizione
     *            tipo spedizione
     * @return {@link FileTransport}
     */
    public FileTransport getTransport(TipoSpedizione tipoSpedizione) {

        Assert.notNull(tipoSpedizione, "Tipo spedizione non può essere nullo!");

        FileTransport fileTransport = null;

        switch (tipoSpedizione) {
        case FTP:
            fileTransport = new FTPFileTransport();
            break;
        case EMAIL:
            fileTransport = new EmailFileTransport();
            break;
        default:
            fileTransport = new LocalFileTransport();
            break;
        }

        return fileTransport;
    }
}
