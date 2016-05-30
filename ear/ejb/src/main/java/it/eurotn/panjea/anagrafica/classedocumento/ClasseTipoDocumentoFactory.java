package it.eurotn.panjea.anagrafica.classedocumento;

import org.apache.log4j.Logger;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public final class ClasseTipoDocumentoFactory {

    private static final Logger LOGGER = Logger.getLogger(ClasseTipoDocumentoFactory.class);

    /**
     * 
     * Costruttore.
     */
    private ClasseTipoDocumentoFactory() {
    }

    /**
     * Restituisce la classe tipo documento in base al nome della classe passato come parametro.
     * 
     * @param className
     *            Nome della classe da caricare
     * @return Istanza di <code>ClasseTipoDocumento</code> caricata
     */
    public static IClasseTipoDocumento getClasseTipoDocumento(String className) {
        LOGGER.debug("--> Enter getClasseTipoDocumento");
        try {
            IClasseTipoDocumento classeTipoDocumento = (IClasseTipoDocumento) Class.forName(className).newInstance();
            LOGGER.debug("--> Classe istanziata: " + className);
            return classeTipoDocumento;
        } catch (Exception ex) {
            LOGGER.error("--> Errore nell'istanziare la classe: " + className, ex);
            throw new RuntimeException("Errore nell'istanziare la classe: " + className, ex);
        }
    }
}
