/**
 * 
 */
package it.eurotn.panjea.rich;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.beans.factory.InitializingBean;

/**
 * Implementa un pannello dove inserire dei comandi.
 * 
 * @author giangi
 * @version 1.0, 20/ott/06
 * 
 */
public class MenuPanelCommandGroup extends AbstractMenuPanel implements InitializingBean {

    /**
     * 
     * Costruttore.
     */
    public MenuPanelCommandGroup() {
        super();
        // setto come non singleton perche' ho la necessita' per la sicurezza che
        // i controlli vengano ricreati ogni volta (il tree va ricreato per verificare
        // se il command e' authorized)
        setSingleton(false);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

    @Override
    protected JComponent createControl() {
        // CommandGroup gruppo=
        // Application.instance().getActiveWindow().getCommandManager().getCommandGroup(commandGroupFactoryBeanId);
        // return gruppo.createButtonStack();
        return new JTextField();
    }

    @Override
    public boolean hasElements() {
        return true;
    }

    /**
     * @param commandGroupFactoryBeanId
     *            id del bean per il factory del commandGroup
     */
    public void setCommandGroupFactoryBeanId(String commandGroupFactoryBeanId) {
    }
}
