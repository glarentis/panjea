/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.search;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;
import it.eurotn.panjea.sicurezza.domain.Utente;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.CommandManager;

/**
 * Search result per visualizzare gli utenti disponibili sotto forma di tabella, questo editor viene visualizzato nel
 * SearchResultWorkspace.
 * 
 * @author Leonardo
 * @see it.eurotn.panjea.rich.pages.AbstractTableSearchResult
 * @see it.eurotn.panjea.rich.pages.SearchResultWorkspace
 */
public class SearchResultUtenti extends AbstractTableSearchResult<Utente> {

    private static Logger logger = Logger.getLogger(SearchResultUtenti.class);

    private static final String VIEW_ID = "searchResultUtenti";

    private ISicurezzaBD sicurezzaBD;

    @Override
    public void close() {
    }

    @Override
    protected Utente doDelete(Utente objToRemove) {
        logger.debug("---> Enter doDelete");
        boolean isUtenteCancellato = sicurezzaBD.cancellaUtente(objToRemove);
        Utente deletedObject = null;
        if (isUtenteCancellato) {
            deletedObject = objToRemove;
        }
        return deletedObject;
    }

    @Override
    protected String[] getColumnPropertyNames() {
        return new String[] { "abilitato", "userName", "descrizione", "nome", "cognome", "cellulare" };
    }

    @Override
    protected AbstractCommand[] getCommand() {
        CommandManager commandManager = getActiveWindow().getCommandManager();
        AbstractCommand command = (AbstractCommand) commandManager.getCommand("newUtenteCommand");
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getRefreshCommand(), getDeleteCommand(), command };
        return abstractCommands;
    }

    @Override
    protected Collection<Utente> getData(Map<String, Object> parameters) {
        logger.debug("---> Enter getData");
        Collection<Utente> utenti = sicurezzaBD.caricaUtenti("userName", null);
        logger.debug("---> Exit getData");
        return utenti;
    }

    @Override
    public String getId() {
        return VIEW_ID;
    }

    @Override
    protected Class<Utente> getObjectsClass() {
        return Utente.class;
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
    public Object reloadObject(Utente object) {
        return sicurezzaBD.caricaUtente(object.getId());
    }

    /**
     * @param sicurezzaBD
     *            The sicurezzaBD to set.
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }
}
