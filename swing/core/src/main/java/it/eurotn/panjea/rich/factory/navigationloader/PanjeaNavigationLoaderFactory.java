/**
 * 
 */
package it.eurotn.panjea.rich.factory.navigationloader;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author fattazzo
 * 
 */
public class PanjeaNavigationLoaderFactory implements InitializingBean {

    private List<AbstractLoaderActionCommand> loaderCommands;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (AbstractLoaderActionCommand command : loaderCommands) {
            for (Class<?> clazz : command.getTypes()) {
                CellNavigationLoadersManager.registerNavigationLoader(clazz, command);
            }
        }
    }

    /**
     * @return the loaderCommands
     */
    public List<AbstractLoaderActionCommand> getLoaderCommands() {
        return loaderCommands;
    }

    /**
     * @param loaderCommands
     *            the loaderCommands to set
     */
    public void setLoaderCommands(List<AbstractLoaderActionCommand> loaderCommands) {
        this.loaderCommands = loaderCommands;
    }
}
