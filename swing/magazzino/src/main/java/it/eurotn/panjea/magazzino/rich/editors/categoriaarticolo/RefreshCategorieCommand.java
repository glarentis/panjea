/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

/**
 * Ricerica tutte le categorie articolo.
 * 
 * @author fattazzo
 * 
 */
public class RefreshCategorieCommand extends ApplicationWindowAwareCommand {

	private final CategorieTreeTablePage categorieTreeTablePage;

	private static final String COMMAND_ID = "refreshCommand";

	/**
	 * Costruttore.
	 * 
	 * @param categorieTreeTablePage
	 *            tree table delle categorie
	 */
	public RefreshCategorieCommand(final CategorieTreeTablePage categorieTreeTablePage) {
		super(COMMAND_ID);
		this.categorieTreeTablePage = categorieTreeTablePage;
		setSecurityControllerId(CategorieTreeTablePage.PAGE_ID + "." + COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);

	}

	@Override
	protected void doExecuteCommand() {
		DefaultTreeTableModel myModel = (DefaultTreeTableModel) this.categorieTreeTablePage.getTreeTable()
				.getTreeTableModel();

		myModel.setRoot(this.categorieTreeTablePage.createNode());
	}

}
