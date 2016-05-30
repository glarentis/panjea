package it.eurotn.panjea.bi.rich.editors.analisi.commands;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;

public class VisualizzaFieldChooserCommand extends ApplicationWindowAwareCommand {
    public static final String COMMAND_ID = "DWVisualizzaFieldChooserCommand";

    /**
     * Istanza del controller che contiene il comando.
     */
    private AnalisiBiEditorController analisiBiEditorController;

    /**
     * Costruttore.
     * 
     * @param analisiBiEditorController
     *            Istanza della controller dell'editor.
     */
    public VisualizzaFieldChooserCommand(final AnalisiBiEditorController analisiBiEditorController) {
        super(COMMAND_ID);
        CommandConfigurer command = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        this.setSecurityControllerId(COMMAND_ID);
        command.configure(this);
        this.analisiBiEditorController = analisiBiEditorController;
    }

    @Override
    protected void doExecuteCommand() {
        analisiBiEditorController.changeFieldChooserVisibility();
    }
}