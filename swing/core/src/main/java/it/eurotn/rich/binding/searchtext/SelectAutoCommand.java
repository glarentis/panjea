package it.eurotn.rich.binding.searchtext;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.factory.MenuFactory;

/**
 * Command per la cancellazione del valueModel della searchtextfield.
 */
public class SelectAutoCommand extends MenuItemCommand {
    private static final String CLEARCOMMAND_ID = "SelectAutoCommandCommand";
    /**
     * @uml.property name="searchPanel"
     * @uml.associationEnd
     */
    private JCheckBoxMenuItem menuItem;

    /**
     * Costruttore.
     *
     * @param searchPanel
     *            pannello di ricerca
     */
    public SelectAutoCommand(final SearchPanel searchPanel) {
        super(CLEARCOMMAND_ID);
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
    }

    @Override
    public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
            org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
        menuItem = new JCheckBoxMenuItem();
        attach(menuItem, faceDescriptorId, buttonConfigurer);
        return menuItem;
    }

    @Override
    protected void doExecuteCommand() {
        searchTextField.toggleSelectAuto();
    }

    @Override
    public void setSearchTextField(SearchTextField searchTextField) {
        super.setSearchTextField(searchTextField);
        if (menuItem != null) {
            menuItem.setSelected(searchTextField.isSelectAuto());
        }
    }
}