package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class RigaStrutturaPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "rigaStrutturaPage";
	private IPartiteBD partiteBD = null;
	private StrutturaPartita strutturaPartita = null;

	/**
	 * Costruttore.
	 */
	public RigaStrutturaPage() {
		super(PAGE_ID, new RigaStrutturaForm(new RigaStrutturaPartite()));
	}

	@Override
	protected Object doDelete() {
		RigaStrutturaPartite rigaStrutturaPartite = (RigaStrutturaPartite) getForm().getFormObject();
		partiteBD.cancellaRigaStrutturaPartite(rigaStrutturaPartite);
		strutturaPartita.getRigheStrutturaPartita().remove(rigaStrutturaPartite);
		firePropertyChange(OBJECT_CHANGED, null, strutturaPartita);
		return rigaStrutturaPartite;
	}

	@Override
	protected Object doSave() {
		RigaStrutturaPartite rigaStrutturaPartite = (RigaStrutturaPartite) getForm().getFormObject();
		rigaStrutturaPartite.setStrutturaPartita(strutturaPartita);
		RigaStrutturaPartite rigaSalvata = partiteBD.salvaRigaStrutturaPartite(rigaStrutturaPartite);
		strutturaPartita.getRigheStrutturaPartita().add(rigaStrutturaPartite);
		firePropertyChange(OBJECT_CHANGED, null, strutturaPartita);
		return rigaSalvata;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	/**
	 * @return the partiteBD
	 */
	public IPartiteBD getPartiteBD() {
		return partiteBD;
	}

	/**
	 * @return the strutturaPartita
	 */
	public StrutturaPartita getStrutturaPartita() {
		return strutturaPartita;
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
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

	/**
	 * @param strutturaPartita
	 *            the strutturaPartita to set
	 */
	public void setStrutturaPartita(StrutturaPartita strutturaPartita) {
		this.strutturaPartita = strutturaPartita;
	}

}
