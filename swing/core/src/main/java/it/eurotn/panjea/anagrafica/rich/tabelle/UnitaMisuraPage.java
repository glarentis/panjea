/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 * 
 */
public class UnitaMisuraPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "unitaMisuraPage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	public UnitaMisuraPage() {
		super(PAGE_ID, new UnitaMisuraForm(new UnitaMisura()));
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaUnitaMisura((UnitaMisura) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		UnitaMisura unitaMisura = (UnitaMisura) getBackingFormPage().getFormObject();
		return anagraficaTabelleBD.salvaUnitaMisura(unitaMisura);
	}

	@Override
	public AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#loadData()
	 */
	@Override
	public void loadData() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#onPostPageOpen()
	 */
	@Override
	public void onPostPageOpen() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#onPrePageOpen()
	 */
	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void postSetFormObject(Object object) {
		super.postSetFormObject(object);
		UnitaMisura um = (UnitaMisura) object;
		setReadOnly(um.getIntra());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#refreshData()
	 */
	@Override
	public void refreshData() {
	}

	/**
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}
}
