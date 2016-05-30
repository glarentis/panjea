package it.eurotn.panjea.intra.rich.editors.servizio;

import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class NomenclaturaPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "nomenclaturaPage";

	private IIntraBD intraBD = null;

	/**
	 * Costruttore.
	 */
	public NomenclaturaPage() {
		super(PAGE_ID, new NomenclaturaForm(new Nomenclatura()));
	}

	@Override
	protected Object doDelete() {
		Nomenclatura servizio = (Nomenclatura) getForm().getFormObject();
		intraBD.cancellaServizio(servizio);
		return servizio;
	}

	@Override
	protected Object doSave() {
		Nomenclatura servizio = (Nomenclatura) getForm().getFormObject();
		servizio = (Nomenclatura) intraBD.salvaServizio(servizio);
		return servizio;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}
}
