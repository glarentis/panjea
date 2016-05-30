package it.eurotn.panjea.magazzino.rich.editors.raggruppamentoArticoli;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class RaggruppamentoArticoliPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "raggruppamentoArticoliPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public RaggruppamentoArticoliPage() {
		super(PAGE_ID, new RaggruppamentoArticoliForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaRaggruppamento((RaggruppamentoArticoli) getForm().getFormObject());
		// Ritorno null per evitare di chiudere l'editor dato che l'oggetto RaggruppamentoArticoli è quello gestito
		// dall'editor.
		// Se viene tornato l'oggetto la on delete lancia un evento con LifecycleApplicationEvent.DELETED e quindi il
		// workspace chiude l'editor.
		// Ritornando null l'evento di cancellazione non viene lanciato.
		return null;
	}

	@Override
	protected Object doSave() {
		return magazzinoAnagraficaBD.salvaRaggruppamento((RaggruppamentoArticoli) getForm().getFormObject());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public void loadData() {
	}

	@Override
	public ILock onLock() {

		RaggruppamentoArticoli raggruppamentoArticoli = magazzinoAnagraficaBD
				.caricaRaggruppamentoArticoli((RaggruppamentoArticoli) getBackingFormPage().getFormObject());
		setFormObject(raggruppamentoArticoli);

		return super.onLock();
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
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
