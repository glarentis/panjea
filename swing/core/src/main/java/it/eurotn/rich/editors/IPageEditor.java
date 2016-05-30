package it.eurotn.rich.editors;

import org.springframework.binding.value.PropertyChangePublisher;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.Messagable;

import it.eurotn.locking.ILock;

/**
 * Interfaccia da implementare per accedere ai metodi legati alla toolbar di default per un editor.
 *
 * @author Leonardo
 */
public interface IPageEditor
        extends PropertyChangePublisher, Messagable, IPageLifecycleAdvisor, IEditorCommands, Focussable {

    /**
     * @return pageEditorId l'id dell' editor
     */
    String getPageEditorId();

    /*
     * @return oggetto gestito dalla pagina
     */
    Object getPageObject();

    /**
     * @return page security id
     */
    String getPageSecurityEditorId();

    /**
     * @return true se la pagina è committabile
     */
    boolean isCommittable();

    /**
     * Utilizzato per sapere se l'oggetto modificato nell'editor è dirty. Di default deve mappare la
     * proprietà isDirty della pageDialog.
     *
     * @return stato dirty della pagina
     */
    boolean isDirty();

    /**
     * Utilizzato per sapere se l'oggetto modificato nell'editor è bloccato.
     *
     * @return stato locked della pagina
     */
    boolean isLocked();

    /**
     * Metodo da implementare chiamato alla execute del comando delete della toolbar.
     *
     * @return l'oggetto cancellato
     */
    Object onDelete();

    /**
     * Metodo da implementare chiamato alla execute del comando lock della toolbar.
     *
     * @return Lock
     */
    ILock onLock();

    /**
     * Metodo da implementare chiamato alla execute del comando new della toolbar.
     */
    void onNew();

    /**
     * Metodo da implementare chiamato alla execute del comando save della toolbar.
     *
     * @return true o false a seconda della riuscita del salvataggio
     */
    boolean onSave();

    /**
     * Metodo da implementare chiamato alla execute del comando undo della toolbar.
     *
     * @return true o false a seconda della riuscita dell'operazione di annullamento
     */
    boolean onUndo();

    /**
     * Metodo da implementare al rilascio del lock.
     */
    void unLock();

}
