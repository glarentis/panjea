/**
 *
 */
package it.eurotn.panjea.rich.factory.navigationloader;

import java.util.List;

import org.springframework.richclient.command.CommandGroup;

import it.eurotn.rich.command.JECCommandGroup;

/**
 * @author fattazzo
 *
 */
public final class CellNavigationLoadersManager {

    private static NavigationLoaderMap loaderMap = new NavigationLoaderMap();

    /**
     * Costruttore.
     */
    private CellNavigationLoadersManager() {
        super();
    }

    /**
     * Restituisce il command group contenente i loader command configurati per la classe e i context richiesti. Vengono
     * considerati anche loaders per il context di default.
     *
     * @param clazz
     *            classe
     * @param contexts
     *            contexts
     * @return {@link CommandGroup}
     */
    public static JECCommandGroup getLoadersCommandGroup(Class<?> clazz, NavigationLoaderContext[] contexts) {
        List<AbstractLoaderActionCommand> commands = loaderMap.getLoaderActionCommands(clazz, contexts);

        JECCommandGroup commandGroup = new JECCommandGroup();
        for (AbstractLoaderActionCommand loaderActionCommand : commands) {
            commandGroup.add(loaderActionCommand);
        }

        return commandGroup;
    }

    /**
     * Indica se sono configurati dei loaders per la classe e i context richiesti. Vengono considerati anche loaders per
     * il context di default.
     *
     * @param clazz
     *            classe
     * @param contexts
     *            contexts
     * @return <code>true</code> se esistono loaders configurati
     */
    public static boolean isLoadersPresent(Class<?> clazz, NavigationLoaderContext[] contexts) {
        List<AbstractLoaderActionCommand> commands = loaderMap.getLoaderActionCommands(clazz, contexts);
        boolean loaderPresent = !commands.isEmpty() || (contexts != null && contexts.length > 0);
        return loaderPresent;
    }

    /**
     * Registra un {@link LoaderActionCommand} alla classe indicata.
     *
     * @param clazz
     *            classe
     * @param command
     *            command
     */
    public static void registerNavigationLoader(Class<?> clazz, AbstractLoaderActionCommand command) {
        loaderMap.register(clazz, command);
    }

}
