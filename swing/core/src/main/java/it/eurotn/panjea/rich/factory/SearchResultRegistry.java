/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;

/**
 * Intefaccia per accedere all' EditorDescriptor a seconda dell'oggetto passato che per questo caso risulta essere una
 * String che identifica il nome esteso della classe
 * 
 * @author Leonardo
 */
public interface SearchResultRegistry {

    public EditorDescriptor getSearchResultDescriptor(Object searchResultObject);

}
