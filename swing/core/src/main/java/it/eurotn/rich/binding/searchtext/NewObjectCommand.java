/**
 *
 */
package it.eurotn.rich.binding.searchtext;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author Leonardo
 */
public class NewObjectCommand extends ActionCommand {

    private static final String NEWOBJECTCOMMAND_ID = "searchNewObjectCommand";

    private SearchPanel searchPanel = null;

    /**
     * @param searchPanel
     *            search panel
     */
    public NewObjectCommand(final SearchPanel searchPanel) {
        super(NEWOBJECTCOMMAND_ID);
        RcpSupport.configure(this);
        this.searchPanel = searchPanel;
    }

    @Override
    protected void doExecuteCommand() {
        if (searchPanel.getSearchObject().isOpenNewObjectManaged()) {
            Object obj = searchPanel.getSearchObject().createNewInstance();
            searchPanel.getSearchObject().openDialogPage(obj);
            searchPanel.getSearchObject().openEditor(obj);
        }
    }

}
