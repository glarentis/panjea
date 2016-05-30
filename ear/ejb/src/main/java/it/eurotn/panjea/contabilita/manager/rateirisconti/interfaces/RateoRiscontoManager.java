package it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaContabileRateiRisconti;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.util.RigaElencoRiscontiDTO;

@Local
public interface RateoRiscontoManager {

    /**
     *
     * @param rigaContabile
     *            rigaContabile
     * @return rigacontabile senza riferimenti alla riga rateo.
     */
    RigaContabile cancellaRiferimentiRateoRisconto(RigaContabile rigaContabile);

    /**
     *
     *
     * @param rigaContabile
     *            rigacontabile
     * @return la lista di documenti che ha generato la riga contabile risconto
     */
    List<Documento> caricaDocumentiCollegatiRisconto(RigaContabileRateiRisconti rigaContabile);

    /**
     * Carica l'elenco dei ratei/risconti per l'anno indicato.
     *
     * @param anno
     *            anno
     * @param clazz
     *            classe da caricare
     * @return risconti
     */
    List<RigaElencoRiscontiDTO> caricaElencoRisconti(int anno, Class<? extends RigaRiscontoAnno> clazz);

    /**
     * Carica una {@link RigaRateoRisconto}.
     *
     * @param idRigaRateoRisconto
     *            id riga
     * @return riga caricata
     */
    RigaRateoRisconto caricaRigaRateoRisconto(Integer idRigaRateoRisconto);

    /**
     *
     * @param rigaContabile
     *            rigaContabile da modificare con ratei. La riga non viene salvata
     * @return riga modificata. crea/aggiorna la riga di rateo collegata nello stesso documento
     * @throws ContiBaseException
     *             lanciata se non trovo i conti base utilizzati
     * @throws ContabilitaException
     *             eccezione generica
     * @throws TipoDocumentoBaseException
     *             .
     */
    RigaContabile salvaRigaContabileRateoRisconto(RigaContabile rigaContabile)
            throws ContabilitaException, ContiBaseException, TipoDocumentoBaseException;
}
