package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.articolo.CodiceArticoloEntitaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class CodiceArticoloEntitaPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "codiceArticoloEntitaPage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private Articolo articolo;

	/**
	 * Costruttore di default,inizializza un nuovo form.
	 */
	public CodiceArticoloEntitaPage() {
		super(PAGE_ID, new CodiceArticoloEntitaForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD
				.cancellaCodiceArticoloEntita((CodiceArticoloEntita) getBackingFormPage()
						.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CodiceArticoloEntita codiceArticoloEntita = (CodiceArticoloEntita) this
				.getForm().getFormObject();
		codiceArticoloEntita.setArticolo(articolo);
		return magazzinoAnagraficaBD
				.salvaCodiceArticoloEntita(codiceArticoloEntita);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onNew() {
		super.onNew();
		CodiceArticoloEntita codiceArticoloEntita = (CodiceArticoloEntita) this
				.getForm().getFormObject();
		codiceArticoloEntita.setCodice(articolo.getCodice());
		setFormObject(codiceArticoloEntita);
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
	 * Imposta l'articolo corrente per la gestione del codiceArticoloEntita.
	 *
	 * @param paramArticoloPM
	 *            l'articolo necessario per il salvataggio di codiceArticoloEntita
	 */
	public void setArticolo(Articolo paramArticoloPM) {
		articolo = paramArticoloPM;
	}

	/**
	 * Set magazzinoAnagraficaBD.
	 *
	 * @param magazzinoAnagraficaBD
	 *            il bd da utilizzare per accedere alle operazioni sui tipi mezzo trasporto
	 */
	public void setMagazzinoAnagraficaBD(
			IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
