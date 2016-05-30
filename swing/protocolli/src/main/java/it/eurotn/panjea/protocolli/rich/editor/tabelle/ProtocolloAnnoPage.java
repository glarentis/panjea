/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.editor.tabelle;

import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.protocolli.rich.bd.IProtocolliBD;
import it.eurotn.panjea.protocolli.rich.form.ProtocolloAnnoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 24/mag/07
 * 
 */
public class ProtocolloAnnoPage extends FormBackedDialogPageEditor {

	private IProtocolliBD protocolliBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina.
	 * 
	 */
	public ProtocolloAnnoPage(final String pageId) {
		super(pageId, new ProtocolloAnnoForm(new ProtocolloAnno()));
	}

	@Override
	protected Object doDelete() {
		protocolliBD.cancellaProtocolloAnno((ProtocolloAnno) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ProtocolloAnno protocolloAnno = (ProtocolloAnno) getBackingFormPage().getFormObject();
		protocolloAnno = this.protocolliBD.salvaProtocolloAnno(protocolloAnno);
		return protocolloAnno;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getNewCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand() };
	}

	/**
	 * @return Returns the protocolliBD.
	 */
	public IProtocolliBD getProtocolliBD() {
		return protocolliBD;
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
	 * @param protocolliBD
	 *            The protocolliBD to set.
	 */
	public void setProtocolliBD(IProtocolliBD protocolliBD) {
		this.protocolliBD = protocolliBD;
	}

}
