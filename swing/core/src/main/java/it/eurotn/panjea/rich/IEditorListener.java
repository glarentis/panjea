package it.eurotn.panjea.rich;

import org.springframework.context.ApplicationEvent;

/**
 * Implementato dagli editor per ricevere gil eventi dell'applicazione. Il bean WorkSpaceListener riceve gli eventi
 * dall'applicazione poi rigira gli eventi interessati agli editor <br/>
 * a tutti gli editor aperti<br/>
 * 
 * @author giangi
 * 
 */
public interface IEditorListener {

    /**
     * @param event
     *            evento
     */
    void onEditorEvent(ApplicationEvent event);
}
