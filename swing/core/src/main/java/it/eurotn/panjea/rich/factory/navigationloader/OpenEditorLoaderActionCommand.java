package it.eurotn.panjea.rich.factory.navigationloader;

import it.eurotn.locking.IDefProperty;

import org.springframework.richclient.application.Application;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public abstract class OpenEditorLoaderActionCommand<T> extends AbstractLoaderActionCommand {

    /**
     *
     * @param commandId
     *            id del commmand
     */
    public OpenEditorLoaderActionCommand(final String commandId) {
        super(commandId);
    }

    @Override
    protected void doExecuteCommand() {
        Object loaderObject = getParameter(PARAM_LOADER_OBJECT, null);
        if (loaderObject != null) {
            // nelle tabelle i dati che non ci sono dovrebbero essere nulli ma specialmente per le prime create ci sono
            // ancora tantissime colonne che contengono nuove istanze anzichè valori nulli quindi metto questo controllo
            // perchè controllarle tutte sarebbe impossibile.
            if (loaderObject instanceof IDefProperty && ((IDefProperty) loaderObject).isNew()) {
                return;
            }
            T openEditorObject = getObjectForOpenEditor(loaderObject);
            OpenEditorEvent event = new OpenEditorEvent(openEditorObject);
            Application.instance().getApplicationContext().publishEvent(event);
        }
    }

    /**
     * Restituisce l'oggetto utilizzato per aprire l'editor.
     *
     * @param loaderObject
     *            l'oggetto su cui viene attivato il command.
     * @return L'oggetto utilizzato per aprire l'editor.
     */
    protected abstract T getObjectForOpenEditor(Object loaderObject);

}
