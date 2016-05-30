package it.eurotn.panjea.magazzino.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.entita.ArticoloEntitaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class ArticoloEntitaPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "articoloEntitaPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private Entita entita;

	/**
	 * Costruttore.
	 * 
	 */
	public ArticoloEntitaPage() {
		super(PAGE_ID, new ArticoloEntitaForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaCodiceArticoloEntita((CodiceArticoloEntita) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CodiceArticoloEntita codiceArticoloEntita = (CodiceArticoloEntita) this.getForm().getFormObject();
		codiceArticoloEntita.setEntita(entita.getEntitaLite());
		return magazzinoAnagraficaBD.salvaCodiceArticoloEntita(codiceArticoloEntita);
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
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(Entita entita) {
		this.entita = entita;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
