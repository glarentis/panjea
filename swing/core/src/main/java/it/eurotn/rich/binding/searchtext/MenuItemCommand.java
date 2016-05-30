package it.eurotn.rich.binding.searchtext;

import org.springframework.richclient.command.ActionCommand;

/**
 * @author giangi
 * @version 1.0, 10/gen/2011
 */
public abstract class MenuItemCommand extends ActionCommand {

    /**
     * @uml.property name="searchTextField"
     * @uml.associationEnd
     */
    protected SearchTextField searchTextField;

    /**
     * Costruttore.
     * 
     * @param commandId
     *            id del comando
     */
    public MenuItemCommand(final String commandId) {
        super(commandId);
    }

    /**
     * @return the searchTextField
     * @uml.property name="searchTextField"
     */
    public SearchTextField getSearchTextField() {
        return searchTextField;
    }

    /**
     * @param searchTextField
     *            the searchTextField to set
     * @uml.property name="searchTextField"
     */
    public void setSearchTextField(SearchTextField searchTextField) {
        this.searchTextField = searchTextField;
    }

}