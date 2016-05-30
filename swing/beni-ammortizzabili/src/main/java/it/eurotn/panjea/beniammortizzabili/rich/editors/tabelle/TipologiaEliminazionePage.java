/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelle;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Aracno
 * @version 1.0, 16/ott/06
 * 
 */
public class TipologiaEliminazionePage extends FormBackedDialogPageEditor {

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public TipologiaEliminazionePage(final String pageId, final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(pageId, new TipologiaEliminazioneForm(new TipologiaEliminazione()));
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	protected Object doDelete() {
		beniAmmortizzabiliBD
				.cancellaTipologieEliminazione((TipologiaEliminazione) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipologiaEliminazione tipologiaEliminazione = (TipologiaEliminazione) getBackingFormPage().getFormObject();
		TipologiaEliminazione tipologiaEliminazioneSalvata;
		tipologiaEliminazioneSalvata = beniAmmortizzabiliBD.salvaTipologiaEliminazione(tipologiaEliminazione);
		return tipologiaEliminazioneSalvata;
	}

	@Override
	public AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {
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
