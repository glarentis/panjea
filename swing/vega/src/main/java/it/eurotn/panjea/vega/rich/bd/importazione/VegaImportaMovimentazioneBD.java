package it.eurotn.panjea.vega.rich.bd.importazione;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class VegaImportaMovimentazioneBD extends AbstractVegaImportaBD {

    private static final Logger LOGGER = Logger.getLogger(VegaImportaMovimentazioneBD.class);

    @Override
    public String importa(Date dataInizio, Date dataFine, String tipiDocumenti) {
        LOGGER.debug("--> Enter importaFatture");
        String result = "";
        start("importaFatture");
        try {
            String restPath = "/importa/movimentazione?dataInizio=" + DateFormatUtils.format(dataInizio, "ddMMyyyy")
                    + "&dataFine=" + DateFormatUtils.format(dataFine, "ddMMyyyy") + "&tipiDocumento=" + tipiDocumenti;
            result = getRestClient().execute(restPath);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importaFatture");
        }
        LOGGER.debug("--> Exit importaFatture ");
        return result;
    }

}
