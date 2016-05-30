/**
 * 
 */
package it.eurotn.panjea.rich.pages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.PageDescriptor;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.application.support.DefaultViewContext;

import com.jidesoft.docking.DefaultDockableHolder;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.spring.richclient.docking.JideApplicationPage;
import com.jidesoft.spring.richclient.docking.JidePageDescriptor;
import com.jidesoft.spring.richclient.docking.editor.EditorComponentPane;
import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;
import com.jidesoft.spring.richclient.docking.editor.WorkspaceView;

/**
 * Implementazione di DockingApplicationPage, alla quale aggiunge l'accesso ad una docking workspace view dove poter
 * aggiungere nuovi editor AbstractSearchResult.
 * 
 * @author Leonardo
 */
public class PanjeaDockingApplicationPage extends JideApplicationPage {

    private static final String SEARCH_VIEW_ID = "searchView";

    /**
     * 
     * @param window
     *            window applicazione
     * @param pageDescriptor
     *            pageDescriptor
     */
    public PanjeaDockingApplicationPage(final ApplicationWindow window, final PageDescriptor pageDescriptor) {
        super(window, pageDescriptor);
    }

    /**
     * 
     * @param <T>
     *            tipo
     * @param viewKey
     *            chiave della vista da creare
     * @return searchResult con i componenti creati.
     */
    @SuppressWarnings("unchecked")
    private <T> AbstractSearchResult<T> createSearchViewPageComponent(String viewKey) {
        JidePageDescriptor pageDescriptor = (JidePageDescriptor) getPageDescriptor();
        EditorDescriptor editorDescriptor = null;
        editorDescriptor = ((PanjeaDockingPageDescriptor) pageDescriptor).getSearchFactory()
                .getSearchResultDescriptor(viewKey);
        WorkspaceView workSpace = getSearchView();

        AbstractSearchResult<T> pageComponent = null;
        if (editorDescriptor != null) {
            pageComponent = (AbstractSearchResult<T>) editorDescriptor.createPageComponent();
            EditorComponentPane editorPane = new EditorComponentPane(pageComponent);
            pageComponent.setContext(new DefaultViewContext(this, editorPane));
            pageComponent = (AbstractSearchResult<T>) workSpace.addDocumentComponent(pageComponent, null);

            // Fires lifecycle event so listeners can react to editor being
            // opened.
            LifecycleApplicationEvent event = new LifecycleApplicationEvent(LifecycleApplicationEvent.CREATED,
                    pageComponent);
            Application.instance().getApplicationContext().publishEvent(event);

            DockingManager dm = ((DefaultDockableHolder) this.getWindow().getControl()).getDockingManager();
            dm.showFrame(SEARCH_VIEW_ID);
            dm.activateFrame(SEARCH_VIEW_ID);
        }
        return pageComponent;
    }

    /**
     * Ritorna la SearchResultWorkspace cercando il bean nel context restituendo così la singola istanza creata.
     * 
     * @return SearchResultWorkspace
     */
    protected WorkspaceView getSearchView() {
        Object objectView = findPageComponent(SEARCH_VIEW_ID);
        if (objectView == null) {
            throw new IllegalArgumentException(SEARCH_VIEW_ID + " NON PRESENTE");
        }
        return (WorkspaceView) objectView;
    }

    /**
     * Richiede l'apertura di un nuovo searchResultEditor nel SearchResultWorkspace.
     * 
     * @param viewKey
     *            la Stringa che identifica la chiave (solitamente il nome della classe) mappata nel searchResultfactory
     *            nell'xml
     */
    public void openResultView(String viewKey) {
        openResultView(viewKey, new HashMap<String, Object>());
    }

    /**
     * 
     * *
     * 
     * @param <T>
     *            tipo
     * @param viewKey
     *            la Stringa che identifica la chiave (solitamente il nome della classe) mappata nel searchResultfactory
     *            nell'xml
     * @param objectsToView
     *            lista degli oggetti da inserire nella ricerca
     */
    public <T> void openResultView(String viewKey, Collection<T> objectsToView) {
        processSearchResultObject(viewKey, objectsToView);
    }

    /**
     * Richiede l'apertura di un nuovo searchResultEditor nel SearchResultWorkspace.
     * 
     * @param viewKey
     *            la Stringa che identifica la chiave (solitamente il nome della classe) mappata nel searchResultfactory
     *            nell'xml
     * @param parameters
     *            la mappa di parametri da passare al metodo getData della searchResult
     */
    public void openResultView(String viewKey, Map<String, Object> parameters) {
        processSearchResultObject(viewKey, parameters);
    }

    /**
     * @param <T>
     *            tipo
     * @param viewKey
     *            chiave della vista
     * @param objectsToView
     *            lista di oggetti da inserire nella vista di ricerca.
     */
    private <T> void processSearchResultObject(String viewKey, Collection<T> objectsToView) {
        // Prendo la classe del primo elemento
        if (objectsToView == null) {
            return;
        }
        if (objectsToView.size() == 0) {
            return;
        }

        AbstractSearchResult<T> pageComponent = createSearchViewPageComponent(viewKey);
        pageComponent.viewResults(objectsToView);
        ((AbstractSearchResult<?>) pageComponent).getControl().requestFocusInWindow();
    }

    /**
     * Viene aggiunto il searchResultEditor nel SearchResultworkspace.
     * 
     * @param <T>
     *            tipo
     * @param viewKey
     *            la Stringao oggetto che identifica la classe mappata nel searchResultfactory nell'xml<br>
     *            se viene passata una lista,viene aperta la searchView prendendo come riferimento il primo oggetto
     *            della lista e viene presentata poi la lista di risultati senza eseguire alcuna ricerca
     * @param parameters
     *            la mappa di parametri per eseguire la ricerca
     */
    private <T> void processSearchResultObject(String viewKey, Map<String, Object> parameters) {
        AbstractSearchResult<T> pageComponent = createSearchViewPageComponent(viewKey);
        pageComponent.search(parameters);
        ((AbstractSearchResult<?>) pageComponent).getControl().requestFocusInWindow();
    }
}
