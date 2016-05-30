/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;
import com.jidesoft.spring.richclient.docking.editor.EditorRegistry;

/**
 * @author Leonardo
 * 
 */
public class PanjeaEditorRegistry implements EditorRegistry {

    private Map<Object, String> editorMap;

    /**
     * @param editorObject
     *            object da aprire nell'editor.
     * @return an EditorDescriptor keyed by the class of the injected object. If non exists throw NPE.
     */
    @Override
    public EditorDescriptor getEditorDescriptor(Object editorObject) {
        EditorDescriptor descriptor;
        String editorKey = "";
        if (editorObject instanceof String) {
            // Se ho la pagina da aprire isolo solamente l'editor
            editorKey = editorMap.get(((String) editorObject).split("#")[0]);
        } else {
            Class<? extends Object> editorClass = editorObject.getClass();
            editorKey = editorMap.get(editorClass);

            if (editorKey == null) {
                // se non trovo nessun editor provo a cercare un editor che è mappato con una super classe dell'oggetto
                // che mi arriva
                for (Entry<Object, String> entry : editorMap.entrySet()) {
                    if (!(entry.getKey() instanceof String)) {
                        Class<?> mapClass = (Class<?>) entry.getKey();
                        if (mapClass.isAssignableFrom(editorClass)) {
                            editorKey = entry.getValue();
                            break;
                        }
                    }
                }
            }
        }
        descriptor = RcpSupport.getBean(editorKey);
        if (descriptor == null) {
            throw new NullPointerException("EdirtorDescrizionfsdjk non trovato.Key dell'editor " + editorObject);
        }
        return descriptor;
    }

    /**
     * @return Returns the editorMap.
     */
    public Map<Object, String> getEditorMap() {
        return editorMap;
    }

    /**
     * setter for editorMap.
     * 
     * @param editorMap
     *            mappa con gli editor e gli oggetti associati
     */
    public void setEditorMap(Map<Object, String> editorMap) {
        this.editorMap = editorMap;
    }

}
