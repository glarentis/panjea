/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public class RigaNessunaVariazioneProvvigioneStrategy
        implements RigaDocumentoVariazioneProvvigioneStrategy, Serializable {

    private static final long serialVersionUID = -8295753093829932249L;

    @Override
    public IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione,
            TipoVariazioneProvvigioneStrategy provvigioneStrategy) {

        return rigaArticolo;
    }

}
