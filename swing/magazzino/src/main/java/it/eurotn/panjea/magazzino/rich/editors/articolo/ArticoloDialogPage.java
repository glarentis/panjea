/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author adriano
 * @version 1.0, 15/mag/08
 * 
 */
public class ArticoloDialogPage extends ButtonCompositeDialogPage implements IPageLifecycleAdvisor, IEditorCommands {

	private static Logger logger = Logger.getLogger(ArticoloDialogPage.class);

	public static final String PAGE_ID = "articoloDialogPage";

	private ArticoloPage articoloPage;

	/**
	 * Costruttore.
	 * 
	 */
	public ArticoloDialogPage() {
		super(PAGE_ID);
	}

	/**
	 * Costruttore.
	 * 
	 * @param pages
	 *            lista delle pagine da gestire
	 */
	public ArticoloDialogPage(final String[] pages) {
		super(PAGE_ID);
		logger.debug("--> Enter ArticoloDialogPage , costruttore");
		setPages(Arrays.asList(pages));
		logger.debug("--> Exit ArticoloDialogPage");
	}

	/**
	 * @return Returns the articoloPage.
	 */
	public ArticoloPage getArticoloPage() {
		if (articoloPage == null) {
			articoloPage = (ArticoloPage) getPage(ArticoloPage.PAGE_ID);
		}
		return articoloPage;
	}

	@Override
	public AbstractCommand getEditorDeleteCommand() {
		return getArticoloPage().getEditorDeleteCommand();
	}

	@Override
	public AbstractCommand getEditorLockCommand() {
		return getArticoloPage().getEditorLockCommand();
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getArticoloPage().getEditorNewCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getArticoloPage().getEditorSaveCommand();
	}

	@Override
	public AbstractCommand getEditorUndoCommand() {
		return getArticoloPage().getEditorUndoCommand();
	}

	@Override
	public void loadData() {
		getArticoloPage().loadData();

	}

	@Override
	public void onPostPageOpen() {
		getArticoloPage().onPostPageOpen();

	}

	@Override
	public boolean onPrePageOpen() {
		return getArticoloPage().onPrePageOpen();
	}

	@Override
	public void postSetFormObject(Object object) {
		getArticoloPage().postSetFormObject(object);
	}

	@Override
	public void preSetFormObject(Object object) {
		getArticoloPage().preSetFormObject(object);
	}

	@Override
	public void refreshData() {
		getArticoloPage().refreshData();
	}

	@Override
	public void setCurrentObject(Object currentObject) {
		super.setCurrentObject(currentObject);
	}

	@Override
	public void setFormObject(Object object) {
		getArticoloPage().setFormObject(object);
	}

	/**
	 * @param idPages
	 *            lista di pagine da gestire
	 */
	public void setPages(List<String> idPages) {
		for (String idPage : idPages) {
			addPage(idPage);
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		getArticoloPage().setReadOnly(readOnly);
	}

}
