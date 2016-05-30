/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.SottoContoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * 
 * @author fattazzo
 * @version 1.0, 16/apr/07
 * 
 */
public class SottoContoPage extends FormBackedDialogPageEditor {

	static Logger logger = Logger.getLogger(MastroPage.class);

	private static final String PAGE_ID = "sottoContoPage";

	private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	public SottoContoPage(SottoConto sottoConto, IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		super(PAGE_ID, new SottoContoForm(sottoConto));
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	@Override
	protected Object doSave() {
		SottoConto sottoConto = (SottoConto) getBackingFormPage().getFormObject();
		logger.debug("--> Salvo l'oggetto: " + sottoConto);
		SottoConto sottoContoSalvato = contabilitaAnagraficaBD.salvaSottoConto(sottoConto);
		setFormObject(sottoContoSalvato);
		return sottoContoSalvato;
	}

	public AbstractCommand getLockCommand() {
		return toolbarPageEditor.getLockCommand();
	}

	public AbstractCommand getUndoCommand() {
		return toolbarPageEditor.getUndoCommand();
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
