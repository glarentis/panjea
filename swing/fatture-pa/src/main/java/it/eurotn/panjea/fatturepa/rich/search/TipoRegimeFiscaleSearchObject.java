package it.eurotn.panjea.fatturepa.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.fatturepa.domain.TipoRegimeFiscale;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

/**
 * SearchObject per il caricamento di {@link TipoRegimeFiscale} da utilizzare nelle ricerche.
 *
 */
public class TipoRegimeFiscaleSearchObject extends AbstractSearchObject {

    private static final Logger LOGGER = Logger.getLogger(TipoRegimeFiscaleSearchObject.class);

    private static final String SEARCH_OBJECT_ID = "tipoRegimeFiscaleSearchObject";

    private IFatturePAAnagraficaBD fatturePAAnagraficaBD;

    /**
     *
     */
    public TipoRegimeFiscaleSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter getData");
        List<TipoRegimeFiscale> tipiRegimeFiscale = fatturePAAnagraficaBD.caricaTipiRegimiFiscali();
        LOGGER.debug("--> Exit getData");
        return tipiRegimeFiscale;
    }

    /**
     * @param fatturePAAnagraficaBD
     *            the fatturePAAnagraficaBD to set
     */
    public void setFatturePAAnagraficaBD(IFatturePAAnagraficaBD fatturePAAnagraficaBD) {
        this.fatturePAAnagraficaBD = fatturePAAnagraficaBD;
    }

}
