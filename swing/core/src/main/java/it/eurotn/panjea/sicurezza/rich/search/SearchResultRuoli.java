/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.search;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;
import it.eurotn.panjea.sicurezza.domain.Ruolo;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Search result per visualizzare i ruoli disponibili sotto forma di tabella, questo editor viene visualizzato nel
 * SearchResultWorkspace.
 * 
 * @author Leonardo
 * @see it.eurotn.panjea.rich.pages.AbstractTableSearchResult
 * @see it.eurotn.panjea.rich.pages.SearchResultWorkspace
 */
public class SearchResultRuoli extends AbstractTableSearchResult<Ruolo> {

    private static Logger logger = Logger.getLogger(SearchResultRuoli.class);

    private static final String VIEW_ID = "searchResultRuoli";

    private ISicurezzaBD sicurezzaBD;

    @Override
    protected Ruolo doDelete(Ruolo objToRemove) {
        logger.debug("---> Enter doDelete");
        boolean isRuoloDeleted = sicurezzaBD.cancellaRuolo(objToRemove);
        Ruolo deletedObject = null;
        if (isRuoloDeleted) {
            deletedObject = objToRemove;
        }
        return deletedObject;
    }

    @Override
    protected String[] getColumnPropertyNames() {
        return new String[] { "descrizione", "codice" };
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getRefreshCommand(), getDeleteCommand(),
                (AbstractCommand) getActiveWindow().getCommandManager().getCommand("newRuoloCommand") };
        return abstractCommands;
    }

    @Override
    protected Collection<Ruolo> getData(Map<String, Object> parameters) {
        logger.debug("---> Enter getData");
        Collection<Ruolo> ruoli = sicurezzaBD.caricaRuoliAziendaCorrente();
        logger.debug("---> Exit getData");
        return ruoli;
    }

    @Override
    public String getId() {
        return VIEW_ID;
    }

    @Override
    protected Class<Ruolo> getObjectsClass() {
        return Ruolo.class;
    }

    @Override
    protected Map<String, Object> getParameters() {
        return null;
    }

    /**
     * @return Returns the sicurezzaBD.
     */
    public ISicurezzaBD getSicurezzaBD() {
        return sicurezzaBD;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public Object reloadObject(Ruolo object) {
        return sicurezzaBD.caricaRuolo(object.getId());
    }

    /**
     * @param sicurezzaBD
     *            The sicurezzaBD to set.
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }
}
