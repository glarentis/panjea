
package com.jidesoft.spring.richclient.docking.editor;

import org.springframework.richclient.application.event.LifecycleApplicationEvent;

public class OpenEditorEvent extends LifecycleApplicationEvent {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4370859684282898113L;

    public static final String EVENT_TYPE = "lifecycleEvent.openEditor";

    /**
     *
     * @param editorEvent
     *            classe o stringa che deve essere mappata nel EditorFactoy
     */
    public OpenEditorEvent(final Object editorEvent) {
        super(EVENT_TYPE, editorEvent);
    }
}
