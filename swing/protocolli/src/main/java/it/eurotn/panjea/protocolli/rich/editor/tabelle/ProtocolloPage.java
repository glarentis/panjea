package it.eurotn.panjea.protocolli.rich.editor.tabelle;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.rich.form.ProtocolloForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * Page di gestione di Protocollo Non esegue il salvataggio della classe Protocollo perche' page utilizzata
 * esclusivamente all'interno della gestione dei protocolli annuali per l'inserimento di un nuovo Protocollo. La
 * persistenza dell'oggetto Protocollo e' garantita dal salvataggio di ProtocolloAnno
 * 
 * @author adriano
 */
public class ProtocolloPage extends FormBackedDialogPageEditor {

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 */
	public ProtocolloPage(final String pageId) {
		super(pageId, new ProtocolloForm(new Protocollo()));
	}

	/**
	 * restituisce lo stesso oggetto recuperato dal FormModel del Form senza effettuare alcun salvataggio.
	 * 
	 * @return Protocollo
	 */
	@Override
	protected Object doSave() {
		Protocollo protocollo = (Protocollo) getForm().getFormObject();
		return protocollo;
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

}
