package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.domain.RigaInventarioArticolo;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class ModificaQuantitaDialog extends MessageDialog {

	private class EliminaSelezionatiCommand extends ActionCommand {

		public static final String COMMAND_ID = "eliminaSelezionatiCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public EliminaSelezionatiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			for (RigaInventarioArticolo riga : righeTableWidget.getSelectedObjects()) {
				inventarioArticolo.getRigheInventarioArticolo().remove(riga);
			}

			righeTableWidget.setRows(inventarioArticolo.getRigheInventarioArticolo());
		}

	}

	private InventarioArticolo inventarioArticolo;

	private EliminaSelezionatiCommand eliminaSelezionatiCommand;

	private JideTableWidget<RigaInventarioArticolo> righeTableWidget;

	/**
	 * Costruttore.
	 * 
	 * @param inventarioArticolo
	 *            inventario articolo da gestire
	 * 
	 */
	public ModificaQuantitaDialog(final InventarioArticolo inventarioArticolo) {
		super("Dettaglio della giacenza reale.", "Modifica");
		this.inventarioArticolo = inventarioArticolo;
		setCloseAction(CloseAction.HIDE);
		setPreferredSize(new Dimension(500, 250));
	}

	@Override
	protected JComponent createDialogContentPane() {

		righeTableWidget = new JideTableWidget<RigaInventarioArticolo>("righaInventarioArticoloTableWidget",
				new ModificaQuantitaTableModel());
		righeTableWidget.setRows(inventarioArticolo.getRigheInventarioArticolo());

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(righeTableWidget.getComponent(), BorderLayout.CENTER);

		return rootPanel;
	}

	@Override
	protected String getCancelCommandId() {
		return "cancelCommand";
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new Object[] { getEliminaSelezionatiCommand(), getFinishCommand(), getCancelCommand() };
	}

	/**
	 * @return the eliminaSelezionatiCommand
	 */
	public EliminaSelezionatiCommand getEliminaSelezionatiCommand() {
		if (eliminaSelezionatiCommand == null) {
			eliminaSelezionatiCommand = new EliminaSelezionatiCommand();
		}

		return eliminaSelezionatiCommand;
	}

	/**
	 * @return the inventarioArticolo
	 */
	public InventarioArticolo getInventarioArticolo() {
		return inventarioArticolo;
	}

	@Override
	protected void onCancel() {
		this.inventarioArticolo = null;

		super.onCancel();
	}
}
