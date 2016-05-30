package it.eurotn.panjea.magazzino.rich.editors.aspettoesteriore;

import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.aspettoesteriore.AspettoEsterioreForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author angelo
 * 
 */
public class AspettoEsteriorePage extends FormBackedDialogPageEditor {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public AspettoEsteriorePage(final String pageID) {
		super(pageID, new AspettoEsterioreForm(new AspettoEsteriore()));
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaAspettoEsteriore((AspettoEsteriore) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		AspettoEsteriore aspettoEsteriore = (AspettoEsteriore) this.getForm().getFormObject();
		return magazzinoAnagraficaBD.salvaAspettoEsteriore(aspettoEsteriore);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
