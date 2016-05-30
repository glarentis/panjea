package it.eurotn.panjea.anagrafica.rich.editors.daticommercialideposito;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.forms.anagraficadaticommercialideposito.AnagraficaDatiCommercialiDepositoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.AbstractCommand;

public class DatiCommercialiDepositoPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(DatiCommercialiDepositoPage.class);

	public static final String PAGE_ID = "datiCommercialiDeposito";

	private List<FormBackedDialogPageEditor> pagine = new ArrayList<FormBackedDialogPageEditor>();

	/**
 * 
 */
	public DatiCommercialiDepositoPage() {
		super(PAGE_ID, new AnagraficaDatiCommercialiDepositoForm());
	}

	@Override
	public JComponent createControl() {
		// recupero i controlli creati per la pagina corrente e quelli delle
		// pagine iniettate.
		JPanel pannello = getComponentFactory().createPanel(new VerticalLayout(0));
		pannello.add(super.createControl());
		for (FormBackedDialogPageEditor pagina : pagine) {
			pannello.add(pagina.getForm().getControl());
			this.getForm().getFormModel().addChild(pagina.getForm().getFormModel());
		}
		return pannello;
	}

	@Override
	protected Object doSave() {
		for (FormBackedDialogPageEditor pagina : pagine) {
			pagina.onSave();
		}
		// salvo i dati della sede
		return getForm().getFormObject();
	}

	@Override
	protected AbstractCommand[] getCommand() {
		logger.debug("--> Enter getCommand");
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
		logger.debug("--> Exit getCommand");
		return commands;
	}

	/**
	 * 
	 * @return List<FormbackedDialogPageEditor>
	 */
	public List<FormBackedDialogPageEditor> getPagine() {
		return pagine;
	}

	@Override
	public void loadData() {
		// i dati della sede magazzino sono gia settati
		for (IPageLifecycleAdvisor pagina : pagine) {
			pagina.loadData();
		}
	}

	@Override
	public ILock onLock() {
		ILock lock = super.onLock();
		// HACK devo ciclare le page perche' all'interno perche' come child
		// sembra che non ricevano la variazione del
		// read only
		// da verificare
		for (FormBackedDialogPageEditor page : pagine) {
			page.getForm().getFormModel().setReadOnly(false);
		}
		return lock;
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

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
		for (IPageLifecycleAdvisor pagina : pagine) {
			pagina.setFormObject(object);
		}
	}

	/**
	 * 
	 * @param pagine
	 *            .
	 */
	public void setPagine(List<FormBackedDialogPageEditor> pagine) {
		this.pagine = pagine;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		for (FormBackedDialogPageEditor page : pagine) {
			page.getForm().getFormModel().setReadOnly(readOnly);
		}
	}
}
