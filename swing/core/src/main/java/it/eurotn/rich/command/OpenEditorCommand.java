package it.eurotn.rich.command;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.Assert;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 *
 */
public class OpenEditorCommand extends ApplicationWindowAwareCommand implements InitializingBean {

    private String idCommand;

    private String openString;
    private String openClassName;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(idCommand, "L'id del command è nullo.");
        this.setId(idCommand);
    }

    @Override
    protected void doExecuteCommand() {
        Assert.isTrue(openClassName != null || openString != null,
                "String o className per l'apertura dell'editor non avvalorati. Command: "
                        + idCommand);

        LifecycleApplicationEvent event = null;
        if (openString != null) {
            event = new OpenEditorEvent(openString);
        } else {
            try {
                Class<?> clazz = Class.forName(openClassName);
                Object clazzObj = clazz.newInstance();
                event = new OpenEditorEvent(clazzObj);
            } catch (Exception e) {
                logger.error("--> errore nell'instanziare la classe " + openClassName, e);
            }
        }
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * @param idCommand
     *            the idCommand to set
     */
    public void setIdCommand(String idCommand) {
        this.idCommand = idCommand;
    }

    /**
     * @param openClassName
     *            the openClassName to set
     */
    public void setOpenClassName(String openClassName) {
        this.openClassName = openClassName;
    }

    /**
     * @param openString
     *            the openString to set
     */
    public void setOpenString(String openString) {
        this.openString = openString;
    }

}
