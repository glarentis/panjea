package it.eurotn.panjea.vega.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.CodiceEsternoEntitaManager;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class VegaBD extends AbstractBaseBD implements IVegaBD {
    private static Logger logger = Logger.getLogger(VegaBD.class);

    private CodiceEsternoEntitaManager codiceEsternoEntitaService;

    @Override
    public List<EntitaLite> caricaEntitaConCodiceEsternoDaConfermare() {
        logger.debug("--> Enter caricaEntitaConCodiceEsternoDaConfermare");
        start("caricaEntitaConCodiceEsternoDaConfermare");
        List<EntitaLite> result = null;
        try {
            result = codiceEsternoEntitaService.caricaEntitaConCodiceEsternoDaConfermare();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaConCodiceEsternoDaConfermare");
        }
        logger.debug("--> Exit caricaEntitaConCodiceEsternoDaConfermare ");
        return result;
    }

    /**
     * @return Returns the codiceEsternoEntitaService.
     */
    public CodiceEsternoEntitaManager getCodiceEsternoEntitaService() {
        return codiceEsternoEntitaService;
    }

    /**
     * @param codiceEsternoEntitaService
     *            The codiceEsternoEntitaService to set.
     */
    public void setCodiceEsternoEntitaService(CodiceEsternoEntitaManager codiceEsternoEntitaService) {
        this.codiceEsternoEntitaService = codiceEsternoEntitaService;
    }

    @Override
    public long verificaClientiDaImportare() {
        logger.debug("--> Enter verificaClientiDaImportare");
        start("verificaClientiDaImportare");
        long result = -1;
        try {
            result = codiceEsternoEntitaService.caricaNumeroEntitaConCodiceEsternoDaConfermare();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("verificaClientiDaImportare");
        }
        logger.debug("--> Exit verificaClientiDaImportare ");
        return result;
    }

}
