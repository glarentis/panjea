package it.eurotn.panjea.magazzino.rich.editors.omaggio;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class OmaggioPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "omaggioPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public OmaggioPage() {
		super(PAGE_ID, new OmaggioForm());
	}

	@Override
	protected Object doSave() {
		Omaggio omaggio = (Omaggio) getForm().getFormObject();
		Omaggio omaggioSalvato = magazzinoAnagraficaBD.salvaOmaggio(omaggio);
		return omaggioSalvato;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand() };
	}

	/**
	 * @return the magazzinoAnagraficaBD
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public void loadData() {

	}

	@Override
	public ILock onLock() {
		ILock lock = super.onLock();
		((OmaggioForm) getBackingFormPage()).requestFocusOnCodiceIva();
		return lock;

	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
