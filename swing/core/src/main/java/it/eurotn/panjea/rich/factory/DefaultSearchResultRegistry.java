/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import java.util.Map;

import org.apache.log4j.Logger;

import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;

/**
 * Factory che restituisce l'editor descriptor della classe editor associata che verra' aperta nel
 * SearchResultWorkSpace, contiene la mappa di riferimento definita nel pages-context.xml che associa la Stringa che
 * identifica la classe con l'editor associato per quella stringa.
 * 
 * @author Leonardo
 */
public class DefaultSearchResultRegistry implements SearchResultRegistry {

    private static Logger logger = Logger.getLogger(DefaultSearchResultRegistry.class);

    // mappa che contiene come chiave una String che identifica il nome esteso
    // della classe
    // e come valore l'EditorDescriptor da cui creo l'Editor da aggiungere al
    // SearchResultWorkSpace
    private Map<String, EditorDescriptor> searchResultMap;

    /**
     * @param searchResultObject
     *            l'oggetto che in questo caso e' una Stringa mappata nell'xml e passata chiamando il metodo
     *            openResult("", Map paramenters) della classe PanjeaDockingApplicationPage
     * @return Restituisce l'editorDescriptor per creare l'editor a seconda della mappa definita di origine
     */
    @Override
    public EditorDescriptor getSearchResultDescriptor(Object searchResultObject) {
        logger.debug("---> Enter getSearchResultDescriptor " + searchResultObject);
        String editorClass = (String) searchResultObject;
        EditorDescriptor descriptor = searchResultMap.get(editorClass);
        logger.debug("---> Exit getSearchResultDescriptor descriptor " + descriptor);
        return descriptor;
    }

    /**
     * @return Returns the searchResultMap.
     */
    public Map<String, EditorDescriptor> getSearchResultMap() {
        return searchResultMap;
    }

    /**
     * @param searchResultMap
     *            setter of searchResultMap
     */
    public void setSearchResultMap(Map<String, EditorDescriptor> searchResultMap) {
        this.searchResultMap = searchResultMap;
    }

}
