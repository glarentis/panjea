package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.MastroForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class MastroPage extends FormBackedDialogPageEditor {

	static Logger logger = Logger.getLogger(MastroPage.class);

	private static final String PAGE_ID = "mastroPage";

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	public MastroPage(Mastro mastro, IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		super(PAGE_ID, new MastroForm(mastro));
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	@Override
	protected Object doSave() {
		Mastro mastro = (Mastro) getBackingFormPage().getFormObject();
		logger.debug("--> Salvo l'oggetto: " + mastro);
		Mastro mastroSalvato = contabilitaAnagraficaBD.salvaMastro(mastro);
		setFormObject(mastroSalvato);
		return mastroSalvato;
	}

	public AbstractCommand getUndoCommand() {
		return toolbarPageEditor.getUndoCommand();
	}

	public AbstractCommand getLockCommand() {
		return toolbarPageEditor.getLockCommand();
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
