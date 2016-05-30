package it.eurotn.panjea.contabilita.service.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaElencoRiscontiDTO;

@Remote
public interface RateiRiscontiService {

    /**
     * Carica l'elenco dei risconti per l'anno indicato.
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
     * Crea i movimenti di chiusura dei risconti per l'anno indicato.
     *
     * @param anno
     *            anno di riferimento
     * @param dataMovimenti
     *            data dei movimenti
     * @throws ContiBaseException
     *             .
     * @throws TipoDocumentoBaseException
     *             .
     * @return ParametriRicercaMovimentiContabili per effettuare la ricerca dei documenti creati
     */
    ParametriRicercaMovimentiContabili creaMovimentiChiusureRisconti(int anno, Date dataMovimenti)
            throws ContiBaseException, TipoDocumentoBaseException;
}
