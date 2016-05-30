/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.editor.tabelle;

import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.protocolli.rich.bd.IProtocolliBD;
import it.eurotn.panjea.protocolli.rich.form.ProtocolloForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author adriano
 * @version 1.0, 01/giu/07
 */
public class ProtocolloValorePage extends FormBackedDialogPageEditor {

	private IProtocolliBD protocolliBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 */
	public ProtocolloValorePage(final String pageId) {
		super(pageId, new ProtocolloForm(new ProtocolloValore()));
	}

	@Override
	protected Object doDelete() {
		protocolliBD.cancellaProtocollo((ProtocolloValore) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ProtocolloValore protocolloValore = (ProtocolloValore) getBackingFormPage().getFormObject();
		protocolloValore = protocolliBD.salvaProtocollo(protocolloValore);
		return protocolloValore;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
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
