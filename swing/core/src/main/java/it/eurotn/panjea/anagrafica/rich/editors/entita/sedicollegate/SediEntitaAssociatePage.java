package it.eurotn.panjea.anagrafica.rich.editors.entita.sedicollegate;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.JComponent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public abstract class SediEntitaAssociatePage extends AbstractTablePageEditor<RiepilogoSedeEntitaDTO> implements
		InitializingBean {

	private class DeleteCommand extends ActionCommand {

		private static final String DELETE_COMMAND_ID = "deleteCommand";

		/**
		 * Costruttore di default.
		 */
		public DeleteCommand() {
			super(DELETE_COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			for (RiepilogoSedeEntitaDTO sedeEntita : getTable().getSelectedObjects()) {
				doDelete(sedeEntita);
			}
			loadData();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + "." + DELETE_COMMAND_ID);
		}
	}

	private class OpenEntitaCommand extends ActionCommand {

		@Override
		protected void doExecuteCommand() {

			RiepilogoSedeEntitaDTO sedeEntita = getTable().getSelectedObject();

			if (sedeEntita != null) {

				Entita entitaLocal = new Cliente();
				entitaLocal.setId(sedeEntita.getEntita().getId());
				entitaLocal = anagraficaBD.caricaEntita(entitaLocal);

				LifecycleApplicationEvent event = new OpenEditorEvent(entitaLocal);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	private Entita entita;

	private IAnagraficaBD anagraficaBD;

	private static final String PAGE_ID = "sediEntitaAssociatePage";
	private SediEntitaAssociateHeaderComponent sediEntitaAssociateHeaderComponent;

	private DeleteCommand deleteCommand;

	/**
	 * Costruttore di default.
	 */
	public SediEntitaAssociatePage() {
		super(PAGE_ID, new String[] { "entita", "sedePrincipale", "sedeEntita.codice", "sedeEntita.sede.descrizione",
				"sedeEntita.sede.indirizzo", "lvl2", "localita", "indirizzoMail", "indirizzoPEC", "telefono", "fax" },
				RiepilogoSedeEntitaDTO.class);
		getTable().setPropertyCommandExecutor(new OpenEntitaCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		sediEntitaAssociateHeaderComponent = new SediEntitaAssociateHeaderComponent(anagraficaBD, new Closure() {

			@Override
			public Object call(Object arg0) {
				doSave((SedeEntita) arg0);
				return null;
			}
		});
		sediEntitaAssociateHeaderComponent.getAddSedeCommand().addCommandInterceptor(new ActionCommandInterceptor() {

			@Override
			public void postExecution(ActionCommand arg0) {
				loadData();
			}

			@Override
			public boolean preExecution(ActionCommand arg0) {
				return true;
			}
		});
		sediEntitaAssociateHeaderComponent.getAddAllSediEntitaCommand().addCommandInterceptor(
				new ActionCommandInterceptor() {

					@Override
					public void postExecution(ActionCommand arg0) {
						loadData();
					}

					@Override
					public boolean preExecution(ActionCommand arg0) {
						return true;
					}
				});
	}

	/**
	 * Esegue la cancellazione del collegamento delle sede.
	 *
	 * @param sedeEntita
	 *            sede di riferimento
	 */
	protected abstract void doDelete(RiepilogoSedeEntitaDTO sedeEntita);

	/**
	 * Esegue il salvataggio dell'associazione del vettore con l'entita.
	 *
	 * @param sedeEntita
	 *            sede di riferimento
	 */
	protected abstract void doSave(SedeEntita sedeEntita);

	/**
	 * @return the anagraficaBD
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getDeleteCommand(), getRefreshCommand() };
	}

	/**
	 * @return DeleteCommand
	 */
	private DeleteCommand getDeleteCommand() {
		if (deleteCommand == null) {
			deleteCommand = new DeleteCommand();
		}
		return deleteCommand;
	}

	/**
	 * @return the entita
	 */
	public Entita getEntita() {
		return entita;
	}

	@Override
	public JComponent getHeaderControl() {
		return sediEntitaAssociateHeaderComponent.getControl();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		if (entita.isNew()) {
			initializePage = false;
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = RcpSupport.getMessage("entita.null.messageDialog.title");
			String messaggio = messageSourceAccessor.getMessage("entita.null.messageDialog.message",
					new Object[] { messageSourceAccessor.getMessage(entita.getDomainClassName(), new Object[] {},
							Locale.getDefault()) }, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	protected void onRefresh() {
		loadData();
	}

	@Override
	public void preSetFormObject(Object object) {
		entita = (Entita) object;
		super.preSetFormObject(object);
	}

	@Override
	public Collection<RiepilogoSedeEntitaDTO> refreshTableData() {
		return null;
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
	}
}