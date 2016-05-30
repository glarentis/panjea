package it.eurotn.panjea.manutenzioni.rich.search;

import java.util.Collection;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;

/**
 * @author fattazzo
 *
 */
public class SearchResultOperatori extends AbstractTableSearchResult<Operatore> {

    private IManutenzioniBD manutenzioniBD;

    private String id;

    @Override
    protected Operatore doDelete(Operatore objectToDelete) {
        return null;
    }

    @Override
    protected String[] getColumnPropertyNames() {

        return new String[] { "codice", "nome", "cognome", "codiceFiscale", "cellulare", "tecnico", "caricatore" };
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getRefreshCommand() };
    }

    @Override
    protected Collection<Operatore> getData(Map<String, Object> parameters) {
        return manutenzioniBD.caricaOperatori();
    }

    @Override
    public String getId() {
        return id;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected Class getObjectsClass() {
        return Operatore.class;
    }

    @Override
    protected Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public Object reloadObject(Operatore object) {
        return object;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }
}
