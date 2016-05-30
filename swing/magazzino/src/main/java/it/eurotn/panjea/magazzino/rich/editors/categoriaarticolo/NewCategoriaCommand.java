package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

/**
 * Command per la creazione di una {@link Categoria} padre.
 * 
 * @author adriano
 * @version 1.0, 07/mag/08
 * 
 */
class NewCategoriaCommand extends ApplicationWindowAwareCommand {

	protected final CategorieTreeTablePage categorieTreeTablePage;
	private static final String NEW_COMMAND = "newCommand";

	/**
	 * Costruttore.
	 * 
	 * @param categorieTreeTablePage
	 *            pagina delle categorie
	 */
	public NewCategoriaCommand(final CategorieTreeTablePage categorieTreeTablePage) {
		super(NEW_COMMAND);
		this.categorieTreeTablePage = categorieTreeTablePage;
		setSecurityControllerId(CategorieTreeTablePage.PAGE_ID + ".controller");
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		if (this.categorieTreeTablePage.getTreeTable().getTreeSelectionModel().getSelectionPath() != null) {
			TreePath selPath = this.categorieTreeTablePage.getTreeTable().getTreeSelectionModel().getSelectionPath();
			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();
			CategoriaLite categoriaPadre = (CategoriaLite) node.getUserObject();

			Integer idCategoria = categoriaPadre.getId();
			if (idCategoria == -1) {
				idCategoria = null;
			}

			Categoria categoria = this.categorieTreeTablePage.getMagazzinoAnagraficaBD().creaCategoria(idCategoria);
			this.categorieTreeTablePage.openCategoriaPage(categoria);

		}
	}
}