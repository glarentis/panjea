/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto.TipoRiga;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.rich.editors.contratto.agenti.RigheContrattoAgenteForm;
import it.eurotn.panjea.magazzino.rich.forms.contratto.RigaContrattoForm;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.ToolbarPageEditor.SaveCommand;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class RigaContrattoPage extends FormsBackedTabbedDialogPageEditor {

	private class NewRigaContrattoArticoloCommand extends ActionCommand {

		private static final String NEW_RIGA_CONTRATTO_ARTICOLO_COMMAND = "newRigaContrattoArticoloCommand";

		/**
		 * Crea una riga con Tipo riga articolo.
		 */
		public NewRigaContrattoArticoloCommand() {
			super(NEW_RIGA_CONTRATTO_ARTICOLO_COMMAND);
			setSecurityControllerId(PAGE_ID + ".controller");
			commandConfigurer.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			((RigaContrattoForm) getForm()).setTipoRiga(TipoRiga.ARTICOLO);
			onNew();
		}
	}

	private class NewRigaContrattoCategoriaCommand extends ActionCommand {

		private static final String NEW_RIGA_CONTRATTO_CATEGORIA_COMMAND = "newRigaContrattoCategoriaCommand";

		/**
		 * Crea una riga con Tipo riga contratto.
		 */
		public NewRigaContrattoCategoriaCommand() {
			super(NEW_RIGA_CONTRATTO_CATEGORIA_COMMAND);
			setSecurityControllerId(PAGE_ID + ".controller");
			commandConfigurer.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			((RigaContrattoForm) getForm()).setTipoRiga(TipoRiga.CATEGORIA);
			onNew();
		}

	}

	private class NewRigaContrattoTutteCategorieCommand extends ActionCommand {

		private static final String NEW_RIGA_CONTRATTO_TUTTE_CATEGORIE_COMMAND = "newRigaContrattoTutteCategorieCommand";

		/**
		 * Crea una riga con Tipo riga articolo.
		 */
		public NewRigaContrattoTutteCategorieCommand() {
			super(NEW_RIGA_CONTRATTO_TUTTE_CATEGORIE_COMMAND);
			setSecurityControllerId(PAGE_ID + ".controller");
			commandConfigurer.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			((RigaContrattoForm) getForm()).setTipoRiga(TipoRiga.TUTTI);
			onNew();
		}
	}

	private final CommandConfigurer commandConfigurer = (CommandConfigurer) ApplicationServicesLocator.services()
			.getService(CommandConfigurer.class);

	private static final String PAGE_ID = "rigaContrattoPage";
	private static final String ARTICOLO_RICHIESTO_MSG = "rigaContrattoPage.articolo.richiesto.message";
	private static final String CATEGORIA_RICHIESTO_MSG = "rigaContrattoPage.categoria.richiesto.message";
	private final IContrattoBD contrattoBD;

	private Contratto contratto;

	private CommandGroup nuovoCommandGroup;

	private NewRigaContrattoArticoloCommand rigaArticolocommand;

	private PluginManager pluginManager;

	/**
	 * Costruttore di default.
	 * 
	 * @param contrattoBD
	 *            anagrafica bd per il magazzino
	 */
	public RigaContrattoPage(final IContrattoBD contrattoBD) {
		super(PAGE_ID, new RigaContrattoForm());
		this.contrattoBD = contrattoBD;
	}

	@Override
	public void addForms() {

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			RigheContrattoAgenteForm righeContrattoAgenteForm = new RigheContrattoAgenteForm(getBackingFormPage()
					.getFormModel());
			addForm(righeContrattoAgenteForm);
		}
	}

	@Override
	protected Object doDelete() {
		Object deleted = null;
		RigaContratto rigaContratto = (RigaContratto) getBackingFormPage().getFormObject();
		if (rigaContratto.getId() != null) {
			contrattoBD.cancellaRigaContratto(rigaContratto);
			deleted = rigaContratto;
		}
		return deleted;
	}

	@Override
	protected Object doSave() {
		RigaContratto rigaContratto = (RigaContratto) getBackingFormPage().getFormObject();
		rigaContratto.setContratto(contratto);
		rigaContratto = contrattoBD.salvaRigaContratto(rigaContratto);
		return rigaContratto;
	}

	@Override
	protected AbstractCommand[] getCommand() {

		SaveCommand saveCommand = toolbarPageEditor.getSaveCommand();
		saveCommand.addCommandInterceptor(new ActionCommandInterceptor() {

			@Override
			public void postExecution(ActionCommand command) {

			}

			@Override
			public boolean preExecution(ActionCommand command) {
				RigaContratto rigaContratto = (RigaContratto) getBackingFormPage().getFormObject();
				TipoRiga tipoRiga = ((RigaContrattoForm) getBackingFormPage()).getTipoRiga();
				if (tipoRiga.equals(TipoRiga.ARTICOLO)) {
					if (rigaContratto.getArticolo() == null) {
						RcpSupport.showWarningDialog(getMessage(ARTICOLO_RICHIESTO_MSG));
						return false;
					}
				} else if (tipoRiga.equals(TipoRiga.CATEGORIA)) {
					if (rigaContratto.getCategoriaCommercialeArticolo() == null) {
						RcpSupport.showWarningDialog(getMessage(CATEGORIA_RICHIESTO_MSG));
						return false;
					}
				}
				return true;
			}
		});
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), saveCommand, toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	/**
	 * @return the contratto
	 */
	public Contratto getContratto() {
		return contratto;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getNewArticoloCommand();
	}

	/**
	 * @return rigaArticolocommand
	 */
	private AbstractCommand getNewArticoloCommand() {
		if (rigaArticolocommand == null) {
			rigaArticolocommand = new NewRigaContrattoArticoloCommand();
		}
		return rigaArticolocommand;
	}

	@Override
	public AbstractCommand getNewCommand() {
		if (nuovoCommandGroup == null) {
			nuovoCommandGroup = new CommandGroup(PAGE_ID + ".NuovoCommandGroupId");
			nuovoCommandGroup.add(getNewArticoloCommand());
			nuovoCommandGroup.add(new NewRigaContrattoCategoriaCommand());
			nuovoCommandGroup.add(new NewRigaContrattoTutteCategorieCommand());
			commandConfigurer.configure(nuovoCommandGroup);
			toolbarPageEditor.setNewCommand(nuovoCommandGroup);
		}
		return toolbarPageEditor.getNewCommand();
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
	 * @param contratto
	 *            the contratto to set
	 */
	public void setContratto(Contratto contratto) {
		this.contratto = contratto;
		// pulisco la riga contratto quando imposto un nuovo contratto,
		// ci penserà la tabella a reimpostare una riga contratto
		((PanjeaAbstractForm) getBackingFormPage()).getNewFormObjectCommand().execute();
	}

	@Override
	public void setFormObject(Object object) {

		RigaContratto rigaContratto = (RigaContratto) object;
		if (rigaContratto != null && rigaContratto.getId() != null) {
			super.setFormObject(contrattoBD.caricaRigaContratto(rigaContratto));
		} else {
			((RigaContrattoForm) getForm()).setTipoRiga(TipoRiga.ARTICOLO);
			super.setFormObject(object);
		}
	}

	/**
	 * @param pluginManager
	 *            the pluginManager to set
	 */
	public void setPluginManager(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}
}
