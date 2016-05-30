package it.eurotn.rich.command;

import javax.swing.AbstractButton;
import javax.swing.Icon;

import org.springframework.richclient.command.ToggleCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.factory.ButtonFactory;

import com.jidesoft.swing.JideToggleButton;

/**
 * Estende ToggleCommand sovrascrive la create button per restituire un JideToggleButton dato che il ButtonFactory non
 * puo' restituirlo a causa del fatto che il JideToggleButton non estende ToggleButton. I metodi da implementare del
 * toggle command sono onSelection() e onDeselection().
 *
 * @author Leonardo
 */
public class JideToggleCommand extends ToggleCommand {

    /**
     * Default constructor.
     */
    public JideToggleCommand() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param commandId
     *            id command
     */
    public JideToggleCommand(final String commandId) {
        super(commandId);
    }

    /**
     * Constructor.
     * 
     * @param id
     *            id
     * @param face
     *            descriptor
     */
    public JideToggleCommand(final String id, final CommandFaceDescriptor face) {
        super(id, face);
    }

    /**
     * Constructor.
     * 
     * @param id
     *            id
     * @param encodedLabel
     *            label
     */
    public JideToggleCommand(final String id, final String encodedLabel) {
        super(id, encodedLabel);
    }

    /**
     * Constructor.
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
    public JideToggleCommand(final String id, final String encodedLabel, final Icon icon, final String caption) {
        super(id, encodedLabel, icon, caption);
    }

    @Override
    public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
            CommandButtonConfigurer configurer) {
        AbstractButton button = new JideToggleButton();
        attach(button, faceDescriptorId, configurer);
        return button;
    }

}
