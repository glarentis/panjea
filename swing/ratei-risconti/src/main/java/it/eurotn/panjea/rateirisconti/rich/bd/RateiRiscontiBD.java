package it.eurotn.panjea.rateirisconti.rich.bd;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.contabilita.service.interfaces.RateiRiscontiService;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaElencoRiscontiDTO;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class RateiRiscontiBD extends AbstractBaseBD implements IRateiRiscontiBD {

    private static final Logger LOGGER = Logger.getLogger(RateiRiscontiBD.class);

    public static final String BEAN_ID = "rateiRiscontiBD";

    private RateiRiscontiService rateiRiscontiService;

    @Override
    public List<RigaElencoRiscontiDTO> caricaElencoRisconti(int anno, Class<? extends RigaRiscontoAnno> clazz) {
        LOGGER.debug("--> Enter caricaElencoRisconti");
        start("caricaElencoRisconti");

        List<RigaElencoRiscontiDTO> elenco = null;
        try {
            elenco = rateiRiscontiService.caricaElencoRisconti(anno, clazz);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaElencoRisconti");
        }
        LOGGER.debug("--> Exit caricaElencoRisconti ");
        return elenco;
    }

    @Override
    public RigaRateoRisconto caricaRigaRateoRisconto(Integer idRigaRateoRisconto) {
        LOGGER.debug("--> Enter caricaRigaRateoRisconto");
        start("caricaRigaRateoRisconto");

        RigaRateoRisconto rigaRateoRisconto = null;
        try {
            rigaRateoRisconto = rateiRiscontiService.caricaRigaRateoRisconto(idRigaRateoRisconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaRateoRisconto");
        }
        LOGGER.debug("--> Exit caricaRigaRateoRisconto ");
        return rigaRateoRisconto;
    }

    @Override
    public ParametriRicercaMovimentiContabili creaMovimentiChiusureRisconti(int anno, Date dataMovimenti) {
        LOGGER.debug("--> Enter creaMovimentiChiusureRisconti");
        start("creaMovimentiChiusureRisconti");

        ParametriRicercaMovimentiContabili parametri = null;
        try {
            parametri = rateiRiscontiService.creaMovimentiChiusureRisconti(anno, dataMovimenti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaMovimentiChiusureRisconti");
        }
        LOGGER.debug("--> Exit creaMovimentiChiusureRisconti ");
        return parametri;
    }

    /**
     * @param rateiRiscontiService
     *            the rateiRiscontiService to set
     */
    public void setRateiRiscontiService(RateiRiscontiService rateiRiscontiService) {
        this.rateiRiscontiService = rateiRiscontiService;
    }

}
