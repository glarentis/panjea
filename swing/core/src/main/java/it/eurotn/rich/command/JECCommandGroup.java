package it.eurotn.rich.command;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandRegistry;
import org.springframework.richclient.command.config.CommandFaceDescriptor;

import it.eurotn.rich.command.support.JECButtonStackGroupContainerPopulator;

public class JECCommandGroup extends CommandGroup {

    private List<AbstractCommand> commands;

    {
        commands = new ArrayList<AbstractCommand>();
    }

    /**
     *
     * Costruttore.
     */
    public JECCommandGroup() {
        super();
    }

    /**
     *
     * Costruttore.
     *
     * @param groupId
     *            id gruppo
     */
    public JECCommandGroup(final String groupId) {
        super(groupId);
    }

    /**
     *
     * Costruttore.
     *
     * @param groupId
     *            id gruppo
     * @param face
     *            descriptor
     */
    public JECCommandGroup(final String groupId, final CommandFaceDescriptor face) {
        super(groupId, face);
    }

    /**
     *
     * Costruttore.
     *
     * @param groupId
     *            id gruppo
     * @param commandRegistry
     *            registry
     */
    public JECCommandGroup(final String groupId, final CommandRegistry commandRegistry) {
        super(groupId, commandRegistry);
    }

    /**
     *
     * Costruttore.
     *
     * @param id
     *            id
     * @param encodedLabel
     *            label
     */
    public JECCommandGroup(final String id, final String encodedLabel) {
        super(id, encodedLabel);
    }

    /**
     *
     * Costruttore.
     *
     * @param id
     *            id
     * @param encodedLabel
     *            label
     * @param icon
     *            icona
     * @param caption
     *            caption
     */
    public JECCommandGroup(final String id, final String encodedLabel, final Icon icon, final String caption) {
        super(id, encodedLabel, icon, caption);
    }

    @Override
    public void add(AbstractCommand command) {
        super.add(command);

        commands.add(command);
    }

    @Override
    public void addGlue(boolean rebuild) {
        getMemberList().append(new PanjeaGlueGroupMember());
        if (rebuild) {
            rebuildAllControls();
            fireMembersChanged();
        }
    }

    @Override
    public JComponent createButtonStack() {
        JECButtonStackGroupContainerPopulator container = new JECButtonStackGroupContainerPopulator();
        addCommandsToGroupContainer(container);
        return container.getButtonStack();
    }

    /**
     * @return the commands
     */
    public List<AbstractCommand> getCommands() {
        return commands;
    }
}
