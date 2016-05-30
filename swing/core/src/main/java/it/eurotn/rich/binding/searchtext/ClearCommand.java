package it.eurotn.rich.binding.searchtext;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Command per la cancellazione del valueModel della searchtextfield.
 */
public class ClearCommand extends MenuItemCommand {
    private static final String CLEARCOMMAND_ID = "searchCancellaCommand";
    /**
     * @uml.property name="searchPanel"
     * @uml.associationEnd
     */
    private SearchPanel searchPanel;

    /**
     * Costruttore.
     * 
     * @param searchPanel
     *            pannello di ricerca
     */
    public ClearCommand(final SearchPanel searchPanel) {
        super(CLEARCOMMAND_ID);
        this.searchPanel = searchPanel;
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        getSearchTextField().uninstallListeners();
        searchPanel.selectObject(null);
        getSearchTextField().installListeners();
    }
}