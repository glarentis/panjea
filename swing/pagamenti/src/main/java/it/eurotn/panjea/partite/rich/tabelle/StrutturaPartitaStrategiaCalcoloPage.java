package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.util.ArrayList;

import org.springframework.richclient.command.AbstractCommand;

public class StrutturaPartitaStrategiaCalcoloPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "strutturaPartitaStrategiaCalcoloPage";
	private IPartiteBD partiteBD = null;

	/**
	 * Costruttore.
	 */
	public StrutturaPartitaStrategiaCalcoloPage() {
		super(PAGE_ID, new StrutturaPartitaStrategieCalcoloForm());
	}

	@Override
	protected Object doSave() {
		StrutturaPartita strutturaPartita = (StrutturaPartita) getBackingFormPage().getFormObject();
		if (strutturaPartita.getRigheStrutturaPartita() == null) {
			strutturaPartita.setRigheStrutturaPartita(new ArrayList<RigaStrutturaPartite>());
		}
		StrutturaPartita strutturaPartitaNew = partiteBD.salveStrutturaPartita(strutturaPartita);
		return strutturaPartitaNew;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
		return commands;
	}

	/**
	 * @return the partiteBD
	 */
	public IPartiteBD getPartiteBD() {
		return partiteBD;
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
		StrutturaPartita strutturaPartita = (StrutturaPartita) object;
		super.setFormObject(strutturaPartita);
	}

	/**
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
