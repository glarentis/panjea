package it.eurotn.panjea.vega.rich.bd.importazione;

import java.util.Date;

import org.apache.log4j.Logger;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class VegaImportaFattureBD extends AbstractVegaImportaBD {

    private static final Logger LOGGER = Logger.getLogger(VegaImportaFattureBD.class);

    @Override
    public String importa(Date dataInizio, Date dataFine, String tipiDocumenti) {
        LOGGER.debug("--> Enter importaFatture");
        String result = "";
        start("importaFatture");
        try {
            result = getRestClient().execute("/importa/fatture");
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importaFatture");
        }
        LOGGER.debug("--> Exit importaFatture ");
        return result;
    }

}
