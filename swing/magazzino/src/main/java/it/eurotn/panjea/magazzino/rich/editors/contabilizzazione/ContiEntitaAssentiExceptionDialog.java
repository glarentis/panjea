package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ContiEntitaAssentiExceptionDialog extends ApplicationDialog {

	private class OpenEntitaCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "openEntitaCommand";

		private final IAnagraficaBD anagraficaBD;

		/**
		 * Costruttore.
		 */
		public OpenEntitaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			anagraficaBD = RcpSupport.getBean("anagraficaBD");
		}

		@Override
		protected void doExecuteCommand() {
			if (table.getSelectedObject() != null) {
				LifecycleApplicationEvent event = new OpenEditorEvent(anagraficaBD.caricaEntita(table
						.getSelectedObject().getEntitaLite(), false));
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	public static final String PAGE_ID = "contiEntitaAssentiExceptionDialog";

	private final ContiEntitaAssentiException exception;
	private JPanel rootPanel;

	private JideTableWidget<ContoEntitaAssenteException> table;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccezione da gestire
	 */
	public ContiEntitaAssentiExceptionDialog(final ContiEntitaAssentiException exception) {
		super();
		this.exception = exception;
		this.setPreferredSize(new Dimension(800, 400));
	}

	@Override
	protected JComponent createDialogContentPane() {

		return getRootPanel();
	}

	/**
	 * Crea la tabella che visualizza le combinazioni non trovate.
	 * 
	 * @return componenti creati
	 */
	private JideTableWidget<ContoEntitaAssenteException> createTable() {

		table = new JideTableWidget<ContoEntitaAssenteException>(PAGE_ID, new String[] { "entita" },
				ContoEntitaAssenteException.class);
		table.setPropertyCommandExecutor(new OpenEntitaCommand());

		return table;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return (new AbstractCommand[] { getFinishCommand() });
	}

	/**
	 * @return the rootPanel
	 */
	public JPanel getRootPanel() {
		rootPanel = getComponentFactory().createPanel(new BorderLayout());

		rootPanel.add(new JLabel("I conti per le seguenti entità risultano essere assenti."), BorderLayout.NORTH);

		createTable();
		rootPanel.add(table.getComponent(), BorderLayout.CENTER);

		table.setRows(this.exception.getContiEntitaExceptions());

		return rootPanel;
	}

	@Override
	protected String getTitle() {
		return RcpSupport.getMessage(PAGE_ID + ".title");
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

}
