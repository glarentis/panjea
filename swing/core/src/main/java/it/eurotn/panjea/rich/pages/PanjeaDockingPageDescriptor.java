/**
 * 
 */
package it.eurotn.panjea.rich.pages;

import it.eurotn.panjea.rich.factory.*;

import org.apache.log4j.Logger;

import com.jidesoft.spring.richclient.docking.JidePageDescriptor;

/**
 * Estensione allo scopo di avere accesso al SearchResultRegistry, il factory per aggiungere searchResultEditor nella
 * SearchResultWorkspace
 * 
 * @author Leonardo
 */
public class PanjeaDockingPageDescriptor extends JidePageDescriptor {

    static Logger logger = Logger.getLogger(PanjeaDockingPageDescriptor.class);

    private SearchResultRegistry searchFactory = new DefaultSearchResultRegistry();

    public SearchResultRegistry getSearchFactory() {
        return searchFactory;
    }

    public void setSearchFactory(SearchResultRegistry searchFactory) {
        this.searchFactory = searchFactory;
    }

}
