package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class StrutturaPartitaPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(StrutturaPartitaPage.class.getName());
	private IPartiteBD partiteBD = null;
	public static final String PAGE_ID = "strutturaPartitaPage";

	/**
	 * Costruttore.
	 */
	public StrutturaPartitaPage() {
		super(PAGE_ID, new StrutturaPartitaForm(new StrutturaPartita()));
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
		logger.debug("--> Aggiungo i commands di default");
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
		if (object instanceof StrutturaPartitaLite) {
			StrutturaPartitaLite strutturaPartitaLite = (StrutturaPartitaLite) object;
			object = partiteBD.caricaStrutturaPartita(strutturaPartitaLite.getId());
		}
		super.setFormObject(object);
	}

	/**
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
