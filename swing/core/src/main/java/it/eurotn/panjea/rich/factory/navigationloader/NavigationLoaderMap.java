/**
 *
 */
package it.eurotn.panjea.rich.factory.navigationloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fattazzo
 *
 */
public class NavigationLoaderMap {

    private Map<Class<?>, Map<NavigationLoaderContext, List<AbstractLoaderActionCommand>>> loaderMap;

    /**
     * Costruttore.
     */
    public NavigationLoaderMap() {
        super();
        this.loaderMap = new HashMap<Class<?>, Map<NavigationLoaderContext, List<AbstractLoaderActionCommand>>>();
    }

    /**
     * Restituisce i loader command configurati per la classe sul context di default.
     *
     * @param clazz
     *            classe
     * @return lista di commands. Se non esistono viene restituita una lista vuota
     */
    private List<AbstractLoaderActionCommand> getLoaderActionCommands(Class<?> clazz) {
        return getLoaderActionCommands(clazz, NavigationLoaderContext.DEFAULT_CONTEXT);
    }

    /**
     * Restituisce i loader command configurati per la classe e il context richiesto.
     *
     * @param clazz
     *            classe
     * @param context
     *            context
     * @return lista di commands. Se non esistono viene restituita una lista vuota
     */
    private List<AbstractLoaderActionCommand> getLoaderActionCommands(Class<?> clazz, NavigationLoaderContext context) {
        List<AbstractLoaderActionCommand> commands = null;
        Map<NavigationLoaderContext, List<AbstractLoaderActionCommand>> navigationForClass = loaderMap.get(clazz);
        if (navigationForClass != null) {
            commands = navigationForClass.get(context);
        }

        // restituisco solo i comandi per i quali l'utente dispone dei permessi
        List<AbstractLoaderActionCommand> commandsAuthorized = new ArrayList<AbstractLoaderActionCommand>();
        if (commands != null) {
            for (AbstractLoaderActionCommand abstractLoaderActionCommand : commands) {
                if (abstractLoaderActionCommand.isAuthorized()) {
                    commandsAuthorized.add(abstractLoaderActionCommand);
                }
            }
        }

        return commandsAuthorized;
    }

    /**
     * Restituisce i loader command configurati per la classe e i context richiesti. Vengono considerati anche loaders
     * per il context di default.
     *
     * @param clazz
     *            classe
     * @param contexts
     *            contexts
     * @return lista di commands. Se non esistono viene restituita una lista vuota
     */
    public List<AbstractLoaderActionCommand> getLoaderActionCommands(Class<?> clazz,
            NavigationLoaderContext[] contexts) {
        List<AbstractLoaderActionCommand> commands = getLoaderActionCommands(clazz);
        if (contexts != null) {
            for (NavigationLoaderContext navigationLoaderContext : contexts) {
                commands.addAll(getLoaderActionCommands(clazz, navigationLoaderContext));
            }
        }

        return commands;
    }

    /**
     * Registra un {@link LoaderActionCommand} alla classe indicata.
     *
     * @param clazz
     *            classe
     * @param command
     *            command
     */
    public void register(Class<?> clazz, AbstractLoaderActionCommand command) {
        if (clazz == null) {
            throw new IllegalArgumentException("Parameter clazz cannot be null");
        }
        if (command.getNavigationLoaderContext() == null) {
            throw new IllegalArgumentException("Command context cannot be null");
        }

        Map<NavigationLoaderContext, List<AbstractLoaderActionCommand>> navigationForClass = loaderMap.get(clazz);
        if (navigationForClass == null) {
            navigationForClass = new HashMap<NavigationLoaderContext, List<AbstractLoaderActionCommand>>();
        }
        List<AbstractLoaderActionCommand> navigationForContext = navigationForClass
                .get(command.getNavigationLoaderContext());
        if (navigationForContext == null) {
            navigationForContext = new ArrayList<AbstractLoaderActionCommand>();
        }
        navigationForContext.add(command);

        navigationForClass.put(command.getNavigationLoaderContext(), navigationForContext);
        loaderMap.put(clazz, navigationForClass);
    }

}
