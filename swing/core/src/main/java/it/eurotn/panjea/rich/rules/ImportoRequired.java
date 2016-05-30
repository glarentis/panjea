/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import it.eurotn.panjea.anagrafica.domain.Importo;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;

/**
 * Implementazione di Constraint per effettuare la validazione "required" di un oggetto importo
 * 
 * Per l'oggetto importo si intende "required" quando questo non è a null e nessuno dei suoi attributi è vuoto
 * 
 * @author adriano
 * @version 1.0, 12/giu/08
 * 
 */
public class ImportoRequired extends TypeResolvableConstraint implements Constraint {

    private static Logger logger = Logger.getLogger(ImportoRequired.class);

    /**
     * 
     */
    public ImportoRequired() {
        super();

    }

    /**
     * @param type
     *            type
     */
    public ImportoRequired(final String type) {
        super(type);
    }

    @Override
    public boolean test(Object argument) {
        if (argument instanceof Importo) {
            Importo importo = (Importo) argument;
            return validaImporto(importo);
        }
        return false;
    }

    @Override
    public String toString() {
        return RcpSupport.getMessage("required");
    }

    /**
     * Esegue la validazione di {@link Importo} verificando che tutti i suoi attributi siano valorizzati
     * 
     * @param importo
     * @return
     */
    private boolean validaImporto(Importo importo) {
        logger.debug("--> Exit validaImporto");
        if (importo == null) {
            logger.debug("--> Exit validaImporto fallita");
            return false;
        }
        if ((importo.getImportoInValuta() == null) || (BigDecimal.ZERO.compareTo(importo.getImportoInValuta()) == 0)) {
            logger.debug("--> Exit validaImporto fallita");
            return false;
        }
        if ((importo.getImportoInValutaAzienda() == null)
                || (BigDecimal.ZERO.compareTo(importo.getImportoInValutaAzienda()) == 0)) {
            logger.debug("--> Exit validaImporto fallita");
            return false;
        }
        if ((importo.getTassoDiCambio() == null) || (BigDecimal.ZERO.compareTo(importo.getTassoDiCambio()) == 0)) {
            logger.debug("--> Exit validaImporto fallita");
            return false;
        }
        if ((importo.getCodiceValuta() == null) || ("".equals(importo.getCodiceValuta()))) {
            logger.debug("--> Exit validaImporto fallita");
            return false;
        }
        logger.debug("--> Exit validaImporto riuscita");
        return true;
    }

}
