/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 */
public class ConversioneUnitaMisuraPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "conversioneUnitaMisuraPage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	public ConversioneUnitaMisuraPage() {
		super(PAGE_ID, new ConversioneUnitaMisuraForm(new ConversioneUnitaMisura()));
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaConversioneUnitaMisura((ConversioneUnitaMisura) getBackingFormPage()
				.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ConversioneUnitaMisura conversioneUnitaMisura = (ConversioneUnitaMisura) getBackingFormPage().getFormObject();
		return anagraficaTabelleBD.salvaConversioneUnitaMisura(conversioneUnitaMisura);
	}

	@Override
	public AbstractCommand[] getCommand() {
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
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}
}
