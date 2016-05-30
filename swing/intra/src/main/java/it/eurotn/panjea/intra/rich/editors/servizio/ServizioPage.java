package it.eurotn.panjea.intra.rich.editors.servizio;

import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class ServizioPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "servizioPage";

	private IIntraBD intraBD = null;

	/**
	 * Costruttore.
	 */
	public ServizioPage() {
		super(PAGE_ID, new ServizioForm(new Servizio()));
	}

	@Override
	protected Object doDelete() {
		Servizio servizio = (Servizio) getForm().getFormObject();
		intraBD.cancellaServizio(servizio);
		return servizio;
	}

	@Override
	protected Object doSave() {
		Servizio servizio = (Servizio) getForm().getFormObject();
		servizio = intraBD.salvaServizio(servizio);
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
