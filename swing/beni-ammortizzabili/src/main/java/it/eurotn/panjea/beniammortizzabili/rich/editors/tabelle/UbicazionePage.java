/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelle;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Aracno
 * @version 1.0, 16/ott/06
 * 
 */
public class UbicazionePage extends FormBackedDialogPageEditor {

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public UbicazionePage(final String pageId, final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(pageId, new UbicazioneForm(new Ubicazione()));
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	protected Object doSave() {
		Ubicazione ubicazione = (Ubicazione) getBackingFormPage().getFormObject();
		Ubicazione ubicazioneSalvata;
		ubicazioneSalvata = beniAmmortizzabiliBD.salvaUbicazione(ubicazione);
		return ubicazioneSalvata;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {
	}

	@Override
	public Object onDelete() {
		beniAmmortizzabiliBD.cancellaUbicazione((Ubicazione) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return false;
	}

	@Override
	public void refreshData() {

	}

}
