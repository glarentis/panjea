package it.eurotn.panjea.anagrafica.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * {@link FormBackedDialogPageEditor} di {@link ContattoSedeEntita}.
 * 
 * @author adriano
 * @version 1.0, 18/dic/07
 * 
 */
public class ContattoSedeEntitaPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(ContattoSedeEntitaPage.class);
	private static final String PAGE_ID = "contattoSedeEntitaPage";
	private final IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public ContattoSedeEntitaPage(final IAnagraficaBD anagraficaBD) {
		super(PAGE_ID, new ContattoSedeEntitaForm(new ContattoSedeEntita()));
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public Object doDelete() {
		anagraficaBD.cancellaContattoSedeEntita((ContattoSedeEntita) getBackingFormPage().getFormObject());
		PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.DELETED,
				getBackingFormPage().getFormObject());
		Application.instance().getApplicationContext().publishEvent(event);
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ContattoSedeEntita contattoSedeEntita = (ContattoSedeEntita) getBackingFormPage().getFormObject();
		ContattoSedeEntita contattoSedeEntitaSalvato = anagraficaBD.salvaContattoSedeEntita(contattoSedeEntita);
		return contattoSedeEntitaSalvato;
	}

	@Override
	public AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
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
	public void preSetFormObject(Object object) {
		super.preSetFormObject(object);
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(Entita entita) {
		((ContattoSedeEntitaForm) this.getForm()).setEntita(entita);
	}
}
