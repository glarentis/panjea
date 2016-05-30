package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;

/**
 * 
 * 
 * 
 * @author fattazzo
 * @version 1.0, 16/apr/07
 * 
 */
public class MastroTitledPageApplicationDialog extends PanjeaTitledPageApplicationDialog {

	private Mastro mastroSalvato;

	public MastroTitledPageApplicationDialog(Mastro mastro, IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		super(new MastroPage(mastro, contabilitaAnagraficaBD));
		mastroSalvato = (Mastro) ((FormBackedDialogPageEditor) this.getDialogPage()).getForm().getFormObject();
		setPreferredSize(new Dimension(600, 250));
	}

	public Mastro getMastroSalvato() {
		return mastroSalvato;
	}

	@Override
	protected String getTitle() {
		return getMessage("mastroTitledPageApplicationDialog.title");
	}

	@Override
	protected void onAboutToShow() {
		MastroPage mastroPage = (MastroPage) this.getDialogPage();
		mastroPage.getLockCommand().execute();
	}

	@Override
	protected void onCancel() {
		MastroPage mastroPage = (MastroPage) this.getDialogPage();
		mastroPage.getUndoCommand().execute();
		mastroSalvato = (Mastro) mastroPage.getForm().getFormObject();
		super.onCancel();
	}

	@Override
	protected boolean onFinish() {
		MastroPage mastroPage = (MastroPage) this.getDialogPage();
		if (mastroPage.getForm().isDirty()) {
			mastroPage.onSave();
		}
		mastroSalvato = (Mastro) mastroPage.getForm().getFormObject();
		return true;
	}

}
