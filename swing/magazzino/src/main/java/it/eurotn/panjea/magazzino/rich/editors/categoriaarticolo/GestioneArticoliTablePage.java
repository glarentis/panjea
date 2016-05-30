package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.utils.SwingWorker;

public class GestioneArticoliTablePage extends ArticoliTablePage implements Observer {

	private class CambiaCategoriaCommandInteceptor extends ActionCommandInterceptorAdapter {

		@Override
		public void postExecution(ActionCommand actioncommand) {
			loadData();
			super.postExecution(actioncommand);
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			command.addParameter(CambiaCategoriaArticoliCommand.PARAM_ARTICOLI, getTable().getSelectedObjects());
			return super.preExecution(command);
		}
	}

	private class CancellaArticoliSelezionatiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "cancellaArticoliSelezionatiCommand";

		/**
		 * Costruttore.
		 */
		public CancellaArticoliSelezionatiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<ArticoloRicerca> articoliSelezionati = getTable().getSelectedObjects();

			if (articoliSelezionati != null && !articoliSelezionati.isEmpty()) {
				getMagazzinoAnagraficaBD().cancellaArticoli(articoliSelezionati);
				setCategoriaChanged(true);
				loadData();

				if (getTable().isEmpty()) {
					// Aggiorna l'editor dell'articolo con un articolo vuoto visto che non ne esistono più per la
					// categoria.
					GestioneArticoliTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
							new ArticoloCategoriaDTO(null, getCategoriaCorrente()));
				}
			}

		}

	}

	private class StampaEtichetteCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {
			command.addParameter(StampaEtichetteArticoliCommand.PARAM_ARTICOLI, getTable().getVisibleObjects());
			return super.preExecution(command);
		}
	}

	private IEditorCommands articoloPage = null;

	private StampaEtichetteArticoliCommand stampaEtichetteArticoliCommand = null;
	private CambiaCategoriaArticoliCommand cambiaCategoriaCommand = null;
	private CancellaArticoliSelezionatiCommand cancellaSelezionatiCommand = null;

	private CambiaCategoriaCommandInteceptor cambiaCategoriaCommandInteceptor = null;
	private StampaEtichetteCommandInterceptor stampaEtichetteCommandInterceptor = null;

	private ArticoloRicerca articoloRicercaSelezionato = null;

	/**
	 * Costruttore.
	 */
	public GestioneArticoliTablePage() {
		super();
	}

	@Override
	public void dispose() {
		getCambiaCategoriaArticoliCommand().removeCommandInterceptor(getCambiaCategoriaCommandInteceptor());
		getStampaEtichetteArticoliCommand().removeCommandInterceptor(getStampaEtichetteCommandInterceptor());

		super.dispose();
	}

	/**
	 * @return command per cambiare la categoria agli articoli selezionati
	 */
	protected CambiaCategoriaArticoliCommand getCambiaCategoriaArticoliCommand() {
		if (cambiaCategoriaCommand == null) {
			cambiaCategoriaCommand = new CambiaCategoriaArticoliCommand(getMagazzinoAnagraficaBD());
			cambiaCategoriaCommand.addCommandInterceptor(getCambiaCategoriaCommandInteceptor());
		}
		return cambiaCategoriaCommand;
	}

	/**
	 * @return cambiaCategoriaCommandInteceptor
	 */
	private CambiaCategoriaCommandInteceptor getCambiaCategoriaCommandInteceptor() {
		if (cambiaCategoriaCommandInteceptor == null) {
			cambiaCategoriaCommandInteceptor = new CambiaCategoriaCommandInteceptor();
		}
		return cambiaCategoriaCommandInteceptor;
	}

	/**
	 * @return the cancellaSelezionatiCommand
	 */
	public CancellaArticoliSelezionatiCommand getCancellaSelezionatiCommand() {
		if (cancellaSelezionatiCommand == null) {
			cancellaSelezionatiCommand = new CancellaArticoliSelezionatiCommand();
		}
		return cancellaSelezionatiCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getCambiaCategoriaArticoliCommand(), getCancellaSelezionatiCommand(),
				getStampaEtichetteArticoliCommand(), getRefreshCommand() };
	}

	@Override
	public AbstractCommand getEditorDeleteCommand() {
		return articoloPage.getEditorDeleteCommand();
	}

	@Override
	public AbstractCommand getEditorLockCommand() {
		return articoloPage.getEditorLockCommand();
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return articoloPage.getEditorNewCommand();
	}

	/**
	 * @return the stampaEtichetteArticoliCommand
	 */
	protected StampaEtichetteArticoliCommand getStampaEtichetteArticoliCommand() {
		if (stampaEtichetteArticoliCommand == null) {
			stampaEtichetteArticoliCommand = new StampaEtichetteArticoliCommand();
			stampaEtichetteArticoliCommand.addCommandInterceptor(getStampaEtichetteCommandInterceptor());
		}

		return stampaEtichetteArticoliCommand;
	}

	/**
	 * @return StampaEtichetteCommandInterceptor
	 */
	private StampaEtichetteCommandInterceptor getStampaEtichetteCommandInterceptor() {
		if (stampaEtichetteCommandInterceptor == null) {
			stampaEtichetteCommandInterceptor = new StampaEtichetteCommandInterceptor();
		}
		return stampaEtichetteCommandInterceptor;
	}

	/**
	 * @param articoloPage
	 *            The articoloPage to set.
	 */
	public void setArticoloPage(IEditorCommands articoloPage) {
		this.articoloPage = articoloPage;
		getTable().setPropertyCommandExecutor(articoloPage.getEditorLockCommand());
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		// Rilancio l'articolo selezionato
		if (!getTable().isAdjustingMode() && getTable().getSelectedObject() != null
				&& !getTable().getSelectedObject().equals(articoloRicercaSelezionato)) {

			articoloRicercaSelezionato = getTable().getSelectedObject();
			if (articoloRicercaSelezionato != null) {

				new SwingWorker<Articolo, Void>() {

					@Override
					protected Articolo doInBackground() throws Exception {
						return getMagazzinoAnagraficaBD().caricaArticolo(
								articoloRicercaSelezionato.createProxyArticolo(), true);
					}

					@Override
					protected void done() {
						try {
							GestioneArticoliTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
									null, new ArticoloCategoriaDTO(get(), null));
						} catch (InterruptedException | ExecutionException e) {
							logger.error("-->errore nel lanciare l'object changed", e);
						}
					};
				}.execute();
			}
		}
	}
}
