package it.eurotn.panjea.rich.bd;

import java.util.Map;

import org.apache.log4j.Logger;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author fattazzo
 *
 */
public class CodiceBD extends AbstractBaseBD implements ICodiceBD {

    public static final String BEAN_ID = "codiceBD";

    private static final Logger LOGGER = Logger.getLogger(CodiceBD.class);

    private AnagraficaService anagraficaService;

    @Override
    public Map<String, String> creaVariabiliCodice(EntityBase entity) {
        LOGGER.debug("--> Enter creaVariabiliCodice");
        start("creaVariabiliCodice");
        Map<String, String> map = null;
        try {
            map = anagraficaService.creaVariabiliCodice(entity);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaVariabiliCodice");
        }
        LOGGER.debug("--> Exit creaVariabiliCodice ");
        return map;
    }

    @Override
    public String generaCodice(String pattern, Map<String, String> mapVariabili) {
        LOGGER.debug("--> Enter genera");
        start("genera");
        String codice = "";
        try {
            codice = anagraficaService.generaCodice(pattern, mapVariabili);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("genera");
        }
        LOGGER.debug("--> Exit genera ");
        return codice;
    }

    /**
     * @param anagraficaService
     *            the anagraficaService to set
     */
    public void setAnagraficaService(AnagraficaService anagraficaService) {
        this.anagraficaService = anagraficaService;
    }

}
