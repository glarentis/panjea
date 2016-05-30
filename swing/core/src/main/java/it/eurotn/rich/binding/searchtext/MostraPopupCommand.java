package it.eurotn.rich.binding.searchtext;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Command per lo show del PopupMenu.
 */
public class MostraPopupCommand extends MenuItemCommand {
    private static final String MOSTRAPOPUPCOMMAND_ID = "searchMostraPopupCommand";

    /**
     * Costruttore.
     */
    public MostraPopupCommand() {
        super(MOSTRAPOPUPCOMMAND_ID);
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        searchTextField.showMenu();
    }
}