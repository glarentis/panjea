package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import javax.swing.JComponent;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideSplitPane;

public class StrutturaPartitaTablePage extends AbstractTablePageEditor<StrutturaPartitaLite> {

	private class DeleteStrutturaPartitaActionCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand command) {

		}

		@Override
		public boolean preExecution(ActionCommand command) {
			StrutturaPartitaLite strutturaPartitaLite = getTable().getSelectedObject();
			if (strutturaPartitaLite != null) {
				command.addParameter(PARAM_STRUTTURA, strutturaPartitaLite);
				return true;
			} else {
				return false;
			}
		}

	}

	private class DeleteStrutturaPartitaCommand extends AbstractDeleteCommand {

		/**
		 * Costruttore.
		 */
		public DeleteStrutturaPartitaCommand() {
			super(DELETE_STRUTTURA_PARTITA_COMMAND);
			RcpSupport.configure(this);
		}

		@Override
		public Object onDelete() {
			StrutturaPartitaLite strutturaPartitaLite = (StrutturaPartitaLite) getParameter(PARAM_STRUTTURA);
			if (strutturaPartitaLite != null) {
				partiteBD.cancellaStrutturaPartita(strutturaPartitaLite.getId());

				PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
						LifecycleApplicationEvent.DELETED, strutturaPartitaLite);
				Application.instance().getApplicationContext().publishEvent(event);
				return strutturaPartitaLite;
			}
			return null;
		}

	}

	private class NewStrutturaPartitaCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public NewStrutturaPartitaCommand() {
			super(NEW_STRUTTURA_PARTITA_COMMAND);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			StrutturaPartita strutturaPartita = new StrutturaPartita();
			strutturaPartitaCompositePage.update(null, strutturaPartita);
		}

	}

	private static final String NEW_STRUTTURA_PARTITA_COMMAND = "newCommand";
	private static final String DELETE_STRUTTURA_PARTITA_COMMAND = "deleteCommand";

	private static final String PARAM_STRUTTURA = "paramStrutturaPartita";

	private static final String PAGE_ID = "strutturaPartitaTablePage";
	private IPartiteBD partiteBD = null;
	private StrutturaPartitaCompositePage strutturaPartitaCompositePage = null;
	private NewStrutturaPartitaCommand newStrutturaPartitaCommand = null;
	private DeleteStrutturaPartitaCommand deleteStrutturaPartitaCommand = null;
	private DeleteStrutturaPartitaActionCommandInterceptor deleteStrutturaPartitaActionCommandInterceptor = null;

	/**
	 * Costruttore.
	 */
	protected StrutturaPartitaTablePage() {
		super(PAGE_ID, new String[] { "descrizione", "categoriaRata.descrizione", "tipoPagamento",
				"tipoStrategiaDataScadenza" }, StrutturaPartitaLite.class);
	}

	@Override
	protected JComponent createControl() {
		JideSplitPane panel = new JideSplitPane();
		panel.setOrientation(JideSplitPane.VERTICAL_SPLIT);
		panel.add(super.createControl());
		getTable().addSelectionObserver(strutturaPartitaCompositePage);

		panel.add(getComponentFactory().createScrollPane(strutturaPartitaCompositePage.getControl()));
		return panel;
	}

	@Override
	protected EditFrame<StrutturaPartitaLite> createEditFrame() {
		return new StrutturaPartitaEditFrame(EEditPageMode.DETAIL, this, EditFrame.QUICK_ACTION_DEFAULT);
	}

	@Override
	public AbstractCommand[] getCommands() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getEditorNewCommand(), getEditorDeleteCommand(),
				getRefreshCommand() };
		return abstractCommands;
	}

	/**
	 * @return the deleteStrutturaPartitaActionCommandInterceptor to get
	 */
	public DeleteStrutturaPartitaActionCommandInterceptor getDeleteStrutturaPartitaActionCommandInterceptor() {
		if (deleteStrutturaPartitaActionCommandInterceptor == null) {
			deleteStrutturaPartitaActionCommandInterceptor = new DeleteStrutturaPartitaActionCommandInterceptor();
		}
		return deleteStrutturaPartitaActionCommandInterceptor;
	}

	/**
	 * @return the deleteStrutturaPartitaCommand to get
	 */
	public ActionCommand getDeleteStrutturaPartitaCommand() {
		if (deleteStrutturaPartitaCommand == null) {
			deleteStrutturaPartitaCommand = new DeleteStrutturaPartitaCommand();
			deleteStrutturaPartitaCommand.addCommandInterceptor(getDeleteStrutturaPartitaActionCommandInterceptor());
		}
		return deleteStrutturaPartitaCommand;
	}

	@Override
	public AbstractCommand getEditorDeleteCommand() {
		return getDeleteStrutturaPartitaCommand();
	}

	@Override
	public AbstractCommand getEditorLockCommand() {
		if (strutturaPartitaCompositePage.getActivePage() == null) {
			return null;
		}
		return ((IEditorCommands) strutturaPartitaCompositePage.getActivePage()).getEditorLockCommand();
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getNewStrutturaPartitaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		if (strutturaPartitaCompositePage.getActivePage() == null) {
			return null;
		}
		return ((IEditorCommands) strutturaPartitaCompositePage.getActivePage()).getEditorSaveCommand();
	}

	@Override
	public AbstractCommand getEditorUndoCommand() {
		if (strutturaPartitaCompositePage.getActivePage() == null) {
			return null;
		}
		return ((IEditorCommands) strutturaPartitaCompositePage.getActivePage()).getEditorUndoCommand();
	}

	/**
	 * @return the newStrutturaPartitaCommand to get
	 */
	public NewStrutturaPartitaCommand getNewStrutturaPartitaCommand() {
		if (newStrutturaPartitaCommand == null) {
			newStrutturaPartitaCommand = new NewStrutturaPartitaCommand();
		}
		return newStrutturaPartitaCommand;
	}

	/**
	 * @return the strutturaPartitaCompositePage
	 */
	public StrutturaPartitaCompositePage getStrutturaPartitaCompositePage() {
		return strutturaPartitaCompositePage;
	}

	@Override
	public Collection<StrutturaPartitaLite> loadTableData() {
		return partiteBD.caricaStrutturePartita();
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {
		super.onEditorEvent(event);

		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
		if (panjeaEvent.getSource() instanceof StrutturaPartita
				&& (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.CREATED) || panjeaEvent.getEventType()
						.equals(LifecycleApplicationEvent.MODIFIED))) {

			StrutturaPartita strutturaPartita = (StrutturaPartita) panjeaEvent.getSource();
			StrutturaPartitaLite strutturaPartitaLite = strutturaPartita.getStrutturaPartitaLite();

			getTable().replaceOrAddRowObject(strutturaPartitaLite, strutturaPartitaLite, strutturaPartitaCompositePage);
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if (JecCompositeDialogPage.PAGE_ACTIVE_PROPERTY.equals(event.getPropertyName())) {
			firePropertyChange(JecCompositeDialogPage.GLOBAL_COMMAND_PROPERTY, null, this);
		}
	}

	@Override
	public Collection<StrutturaPartitaLite> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param partiteBD
	 *            partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

	/**
	 * @param strutturaPartitaCompositePage
	 *            the strutturaPartitaCompositePage to set
	 */
	public void setStrutturaPartitaCompositePage(StrutturaPartitaCompositePage strutturaPartitaCompositePage) {
		this.strutturaPartitaCompositePage = strutturaPartitaCompositePage;
		// Quando cambia la activePage rilancio la mia activePage per
		// riassociare gli shortcut
		this.strutturaPartitaCompositePage.addPropertyChangeListener(this);
	}

}
