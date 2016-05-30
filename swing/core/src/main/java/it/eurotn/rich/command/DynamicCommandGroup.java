package it.eurotn.rich.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.support.ApplicationServicesAccessor;
import org.springframework.richclient.command.ActionCommand;

/**
 *
 *
 * @author Aracno
 * @version 1.0, 15/dic/06
 *
 */
public class DynamicCommandGroup extends ApplicationServicesAccessor implements InitializingBean {

    private Map<String, List<ActionCommand>> mapMenu = null;

    /**
     *
     * Costruttore.
     */
    public DynamicCommandGroup() {
        super();
    }

    /**
     * @param groupName
     *            group
     * @param actionCommand
     *            command
     */
    public void addMenuItems(String groupName, ActionCommand actionCommand) {
        if (this.mapMenu == null) {
            this.mapMenu = new HashMap<String, List<ActionCommand>>();
        }

        List<ActionCommand> list = new ArrayList<ActionCommand>();
        if (mapMenu.containsKey(groupName)) {
            list = mapMenu.get(groupName);
        }

        list.add(actionCommand);
        mapMenu.put(groupName, list);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    /**
     * @return map item
     */
    public Map<String, List<ActionCommand>> getMapItem() {
        return this.mapMenu;
    }
}
