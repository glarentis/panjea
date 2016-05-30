/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.formulatrasformazione;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.formulatrasformazione.FormulaTrasformazioneForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 25/nov/2008
 * 
 */
public class FormulaTrasformazionePage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "formulaTrasformazionePage";

	private static Logger logger = Logger.getLogger(FormulaTrasformazionePage.class);

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public FormulaTrasformazionePage() {
		super(PAGE_ID, new FormulaTrasformazioneForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaFormulaTrasformazione((FormulaTrasformazione) getBackingFormPage()
				.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		FormulaTrasformazione formulaTrasformazione = (FormulaTrasformazione) getForm().getFormObject();
		formulaTrasformazione = magazzinoAnagraficaBD.salvaFormulaTrasformazione(formulaTrasformazione);
		logger.debug("--> Exit doSave");
		return formulaTrasformazione;
	}

	@Override
	protected AbstractCommand[] getCommand() {
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
		return true;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
