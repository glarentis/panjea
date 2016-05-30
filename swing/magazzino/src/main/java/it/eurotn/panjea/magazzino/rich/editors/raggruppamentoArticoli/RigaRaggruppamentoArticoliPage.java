package it.eurotn.panjea.magazzino.rich.editors.raggruppamentoArticoli;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class RigaRaggruppamentoArticoliPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "rigaRaggruppamentoArticoli";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private RaggruppamentoArticoli raggruppamentoCorrente;

	/**
	 * Costruttore.
	 */
	public RigaRaggruppamentoArticoliPage() {
		super(PAGE_ID, new RigaRaggruppamentoArticoliForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD
				.cancellaRigaRaggruppamentoArticoli((RigaRaggruppamentoArticoli) getForm().getFormObject());
		return getForm().getFormObject();
	}

	@Override
	protected Object doSave() {
		RigaRaggruppamentoArticoli formObject = (RigaRaggruppamentoArticoli) getForm().getFormObject();
		formObject.setRaggruppamento(raggruppamentoCorrente);
		RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli = magazzinoAnagraficaBD
				.salvaRigaRaggruppamentoArticoli(formObject);
		return rigaRaggruppamentoArticoli;
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

	/**
	 * @param raggruppamentoCorrente
	 *            The raggruppamentoCorrente to set.
	 */
	public void setRaggruppamentoCorrente(RaggruppamentoArticoli raggruppamentoCorrente) {
		this.raggruppamentoCorrente = raggruppamentoCorrente;
	}
}
