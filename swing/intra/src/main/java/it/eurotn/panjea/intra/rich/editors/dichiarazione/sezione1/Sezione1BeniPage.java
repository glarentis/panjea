package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione1;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.form.Form;

public class Sezione1BeniPage extends FormBackedDialogPageEditor {

	private IIntraBD intraBD;
	protected DichiarazioneIntra dichiarazioneIntra;

	/**
	 * Costruttore.
	 */
	public Sezione1BeniPage() {
		this("sezione1BeniPage", new Sezione1BeniForm(new RigaSezione1Intra()));
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param string
	 *            id
	 * @param sezioneBeneForm
	 *            form
	 */
	public Sezione1BeniPage(final String string, final Form sezioneBeneForm) {
		super(string, sezioneBeneForm);
	}

	@Override
	protected Object doDelete() {
		RigaSezioneIntra rigaSezioneIntra = (RigaSezioneIntra) getBackingFormPage().getFormObject();
		intraBD.cancellaRigaSezioneDichiarazione(rigaSezioneIntra);
		return rigaSezioneIntra;
	}

	@Override
	protected Object doSave() {
		RigaSezioneIntra rigaSezioneIntra = (RigaSezioneIntra) getBackingFormPage().getFormObject();
		rigaSezioneIntra = intraBD.salvaRigaSezioneDichiarazione(rigaSezioneIntra);
		return rigaSezioneIntra;
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
	 * @param dichiarazioneIntra
	 *            the dichiarazioneIntra to set
	 */
	public void setDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		this.dichiarazioneIntra = dichiarazioneIntra;
		((Sezione1BeniForm) getBackingFormPage()).setDichiarazioneIntra(dichiarazioneIntra);
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
