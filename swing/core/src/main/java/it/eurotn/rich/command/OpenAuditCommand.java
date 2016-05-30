package it.eurotn.rich.command;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.utils.AuditObject;

public class OpenAuditCommand extends ApplicationWindowAwareCommand {

    public static final String AUDIT_OBJECT = "auditObject";

    /**
     * Costruttore.
     */
    public OpenAuditCommand() {
        setId("auditCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        Object object = getParameter(AUDIT_OBJECT);
        LifecycleApplicationEvent event = new OpenEditorEvent(new AuditObject(object));
        Application.instance().getApplicationContext().publishEvent(event);
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
        button.setFocusable(false);
        super.onButtonAttached(button);
    }
}
