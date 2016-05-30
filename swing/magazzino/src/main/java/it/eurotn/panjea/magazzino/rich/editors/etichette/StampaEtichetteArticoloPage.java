package it.eurotn.panjea.magazzino.rich.editors.etichette;

import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.etichette.StampaEtichetteArticoloForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.AbstractCommand;

public class StampaEtichetteArticoloPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "stampaEtichetteArticoloPage";

	/**
	 * Costruttore.
	 */
	public StampaEtichetteArticoloPage() {
		super(PAGE_ID, new StampaEtichetteArticoloForm());
	}

	@Override
	protected Object doDelete() {
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		return getBackingFormPage().getFormObject();
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
	 * @param formModelParametriStampa
	 *            the formModelParametriStampa to set
	 */
	public void setFormModelParametriStampa(FormModel formModelParametriStampa) {
		((StampaEtichetteArticoloForm) getForm()).setFormModelParametriStampa(formModelParametriStampa);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		((StampaEtichetteArticoloForm) getForm()).setMagazzinoAnagraficaBD(magazzinoAnagraficaBD);
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		((StampaEtichetteArticoloForm) getForm()).setMagazzinoDocumentoBD(magazzinoDocumentoBD);
	}

	/**
	 * @param parametriStampaEtichetteArticolo
	 *            The parametriStampaEtichetteArticolo to set.
	 */
	public void setParametriStampaEtichetteArticolo(ParametriStampaEtichetteArticolo parametriStampaEtichetteArticolo) {
		((StampaEtichetteArticoloForm) getForm()).setParametriStampaEtichetteArticolo(parametriStampaEtichetteArticolo);
	}

}
