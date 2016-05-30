/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.search;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author adriano
 * @version 1.0, 20/giu/07
 */
public class UtenteSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "utenteSearchObject";

    private ISicurezzaBD sicurezzaBD;

    /**
     * 
     */
    public UtenteSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return null;
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        List<Utente> list = sicurezzaBD.caricaUtenti(fieldSearch, valueSearch);
        return list;
    }

    /**
     * @return Returns the sicurezzaBD.
     */
    public ISicurezzaBD getSicurezzaBD() {
        return sicurezzaBD;
    }

    /**
     * @param sicurezzaBD
     *            The sicurezzaBD to set.
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }

}
