/**
 *
 */
package it.eurotn.rich.binding.searchtext;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author Leonardo
 */
public class EditObjectCommand extends ActionCommand {

    private static final String EDITOBJECTCOMMAND_ID = "searchEditObjectCommand";
    /**
     * @uml.property name="searchPanel"
     * @uml.associationEnd
     */
    private SearchPanel searchPanel = null;

    /**
     * Costruttore.
     *
     * @param searchPanel
     *            search panel
     */
    public EditObjectCommand(final SearchPanel searchPanel) {
        super(EDITOBJECTCOMMAND_ID);
        RcpSupport.configure(this);
        this.searchPanel = searchPanel;
    }

    @Override
    protected void doExecuteCommand() {
        // nel caso la search non abbia un oggetto selezionato non apro la
        // modifica
        if (searchPanel.getValueModel().getValue() != null) {
            searchPanel.getSearchObject().openDialogPage(searchPanel.getValueModel().getValue());
            searchPanel.getSearchObject().openEditor(searchPanel.getValueModel().getValue());
        }
    }

}
