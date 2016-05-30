/**
 *
 */
package it.eurotn.panjea.rich.focuspolicy;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Focus traversal policy per navigare attraverso i componenti contenuti nella lista di components dove l'ordine della
 * lista rispecchia l'ordine di navigabilita' tra essi. Se il componente successivo o precedente risulta non essere
 * accessibile si passa ricorsivamente al successivo o precedente finche' non trovo il componente disponibile ad
 * accettare il focus; l'elemento successivo all'ultimo sara' il primo elemento, mentre il precedente al primo sara'
 * l'ultimo.
 * 
 * @author Leonardo
 */
public class PanjeaFocusTraversalPolicy extends FocusTraversalPolicy {

    private static Logger logger = Logger.getLogger(PanjeaFocusTraversalPolicy.class);

    private FocusTraversalPolicy focusTraversalPolicy = null;
    private List<Component> componentsToSkip = new ArrayList<Component>();

    /**
     * @param focusTraversalPolicy
     *            policy di base
     * @param componentsToSkip
     *            componenti da saltare nel normale ciclo di focus
     */
    public PanjeaFocusTraversalPolicy(final FocusTraversalPolicy focusTraversalPolicy,
            final List<Component> componentsToSkip) {
        super();
        this.focusTraversalPolicy = focusTraversalPolicy;
        this.componentsToSkip = componentsToSkip;
    }

    @Override
    public Component getComponentAfter(Container container, Component component) {
        logger.debug("--> getComponentAfter al component " + component);
        Component componentAfter = focusTraversalPolicy.getComponentAfter(container, component);
        if (componentsToSkip.indexOf(componentAfter) != -1) {
            componentAfter = getComponentAfter(container, componentAfter);
        }
        return componentAfter;
    }

    @Override
    public Component getComponentBefore(Container container, Component component) {
        logger.debug("--> getComponentAfter al component " + component);
        Component componentBefore = focusTraversalPolicy.getComponentBefore(container, component);
        if (componentsToSkip.indexOf(componentBefore) != -1) {
            componentBefore = getComponentBefore(container, componentBefore);
        }
        return componentBefore;
    }

    @Override
    public Component getDefaultComponent(Container container) {
        return focusTraversalPolicy.getDefaultComponent(container);
    }

    @Override
    public Component getFirstComponent(Container container) {
        return focusTraversalPolicy.getFirstComponent(container);
    }

    @Override
    public Component getLastComponent(Container container) {
        return focusTraversalPolicy.getLastComponent(container);
    }

}
