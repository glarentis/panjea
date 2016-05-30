/**
 * 
 */
package it.eurotn.panjea.rich.factory.navigationloader;

/**
 * @author fattazzo
 * 
 */
public interface ILoaderActionCommand {

    /**
     * @return the navigationLoaderContext
     */
    NavigationLoaderContext getNavigationLoaderContext();

    /**
     * @return tipi di oggetti gestiti dal command
     */
    Class<?>[] getTypes();
}
