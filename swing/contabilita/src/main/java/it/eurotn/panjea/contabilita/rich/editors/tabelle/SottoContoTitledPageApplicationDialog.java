/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;

/**
 * 
 * 
 * @author fattazzo
 * @version 1.0, 16/apr/07
 * 
 */
public class SottoContoTitledPageApplicationDialog extends PanjeaTitledPageApplicationDialog {

	private SottoConto sottoContoSalvato;

	public SottoContoTitledPageApplicationDialog(SottoConto sottoConto, IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		super(new SottoContoPage(sottoConto, contabilitaAnagraficaBD));
		sottoContoSalvato = (SottoConto) ((FormBackedDialogPageEditor) this.getDialogPage()).getForm().getFormObject();
		setPreferredSize(new Dimension(600, 350));
	}

	public SottoConto getSottoContoSalvato() {
		return sottoContoSalvato;
	}

	@Override
	protected String getTitle() {
		return getMessage("sottoContoTitledPageApplicationDialog.title");
	}

	@Override
	protected void onAboutToShow() {
		SottoContoPage sottoContoPage = (SottoContoPage) this.getDialogPage();
		sottoContoPage.getLockCommand().execute();
	}

	@Override
	protected void onCancel() {
		SottoContoPage sottoContoPage = (SottoContoPage) this.getDialogPage();
		sottoContoPage.getUndoCommand().execute();
		sottoContoSalvato = (SottoConto) sottoContoPage.getForm().getFormObject();
		super.onCancel();
	}

	@Override
	protected boolean onFinish() {
		SottoContoPage sottoContoPage = (SottoContoPage) this.getDialogPage();
		if (sottoContoPage.getForm().isDirty()) {
			sottoContoPage.onSave();
		}
		sottoContoSalvato = (SottoConto) sottoContoPage.getForm().getFormObject();
		return true;
	}

}
