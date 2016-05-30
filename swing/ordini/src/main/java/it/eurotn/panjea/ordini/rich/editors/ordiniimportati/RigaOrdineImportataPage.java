package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class RigaOrdineImportataPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "rigaOrdineImportataPage";

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	public static final String RIGHE_IMPORTATE_CHANGED = "RIGHE_IMPORTATE_CHANGED";

	private boolean selezionata;

	/**
	 * Costruttore.
	 */
	public RigaOrdineImportataPage() {
		super(PAGE_ID, new RigaOrdineImportataForm());
	}

	@Override
	protected Object doSave() {
		RigaOrdineImportata rigaOrdineImportata = (RigaOrdineImportata) getBackingFormPage().getFormObject();

		List<RigaOrdineImportata> righeSalvate = ordiniDocumentoBD.salvaRigaOrdineImportata(rigaOrdineImportata);

		RigaOrdineImportataPage.this.firePropertyChange(RIGHE_IMPORTATE_CHANGED, null, righeSalvate);

		righeSalvate.get(0).setSelezionata(selezionata);
		return righeSalvate.get(0);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand() };
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

	@Override
	public void setFormObject(Object object) {

		this.selezionata = Boolean.FALSE;

		if (object != null && object instanceof RigaOrdineImportata) {
			this.selezionata = ((RigaOrdineImportata) object).isSelezionata();
		}

		super.setFormObject(object);
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}
}
