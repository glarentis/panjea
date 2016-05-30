package it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi;

import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;

public class ArticoliAlternativiCompositePage extends DockingCompositeDialogPage implements IPageLifecycleAdvisor {

	public static final String BEAN_ID = "articoliAlternativiPage";
	private Object currentObject;

	/**
	 * Costruttore.
	 */
	public ArticoliAlternativiCompositePage() {
		super(BEAN_ID);
	}

	@Override
	public void addPage(DialogPage page) {
		super.addPage(page);
	}

	@Override
	protected JComponent createPageControl() {
		JComponent control = super.createPageControl();
		return control;
	}

	@Override
	public void loadData() {
		setCurrentObject(currentObject);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return false;
	}

	@Override
	public void postSetFormObject(Object object) {
	}

	@Override
	public void preSetFormObject(Object object) {
	}

	@Override
	public void refreshData() {
		setCurrentObject(currentObject);
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);
	}

	@Override
	public void setFormObject(Object object) {
		currentObject = object;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	}

	/**
	 * @param selectCommandExecutor
	 *            The selectCommandExecutor to set.
	 */
	public void setSelectCommandExecutor(ActionCommandExecutor selectCommandExecutor) {
		List<?> pagine = getPages();
		for (Object dialogPage : pagine) {
			AbstractTablePageEditor<?> tablePage = (AbstractTablePageEditor<?>) dialogPage;
			tablePage.getTable().setPropertyCommandExecutor(selectCommandExecutor);
		}
	}

}
