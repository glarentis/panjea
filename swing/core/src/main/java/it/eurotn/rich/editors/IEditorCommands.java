package it.eurotn.rich.editors;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Interfaccia per l'accesso ai command standard di un editor Questo accesso viene utilizzato principalmente
 * nell'AbstractEditorDialogPage per registrare i commands della dialog page visualizzata nell'editor corrente; se viene
 * ritornato il command la registrazione viene eseguita, se invece ritorno null, la registrazione del command non viene
 * eseguita.<br>
 * <br>
 *
 * NOTA: ho scelto un nome univoco per i metodi per evitare di avere conflitti tra i metodi che tornano commands da
 * utilizzare negli editor e commands che servono principalmente per registrare i local commands sui global commands;
 * <br>
 * ad es: si vuole avere un local command non accessibile da tastiera quindi l'implementazione di questa interfaccia
 * ritorna null, ma il command nell'editor deve esistere quindi deve tornare un Abstract command valido.
 *
 * @see AbstractEditorDialogPage.registerLocalCommand(PageComponentContext context, IEditorCommands dialogPageEditor)
 * @author Leonardo
 */
public interface IEditorCommands {

    /**
     * @return delete command per eliminare l'elemento, utile nelle tabelle
     */
    public AbstractCommand getEditorDeleteCommand();

    /**
     * @return lock command per modificare l'elemento dell'editor
     */
    public AbstractCommand getEditorLockCommand();

    /**
     * @return nuovo command per creare un nuovo elemento
     */
    public AbstractCommand getEditorNewCommand();

    /**
     * @return salva command per salvare l'elemento dell'editor, deve esserci una modifica in sospeso
     */
    public AbstractCommand getEditorSaveCommand();

    /**
     * @return undo command per rilasciare il lock sull'oggetto e tornare allo stato precedente
     */
    public AbstractCommand getEditorUndoCommand();
}
