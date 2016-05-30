package it.eurotn.panjea.magazzino.rich.forms.listino;

import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.rich.control.table.JideEmptyOverlayTableScrollPane;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;

public class ApriStoriaPrezzoListinoCommand extends ApplicationWindowAwareCommand {

	private class StoriaPrezzoDialog extends MessageDialog {

		private ScaglioneListino scaglioneListino;

		private JideTableWidget<ScaglioneListinoStorico> tableWidget;

		private JCheckBox versioneCheckBox;

		/**
		 * Costruttore.
		 *
		 * @param scaglioneListino
		 *            scaglione listino
		 */
		public StoriaPrezzoDialog(final ScaglioneListino scaglioneListino) {
			super("Variazione del prezzo della riga del listino", "message");
			this.scaglioneListino = scaglioneListino;
		}

		private void caricaStorico() {

			Integer numeroVersione = null;
			if (versioneCheckBox.isSelected()) {
				numeroVersione = scaglioneListino.getRigaListino().getVersioneListino().getCodice();
			}

			tableWidget.setOverlayTable(new JideEmptyOverlayTableScrollPane(false));
			tableWidget.setRows(magazzinoAnagraficaBD.caricaStoricoScaglione(scaglioneListino, numeroVersione));
			((JideTable) tableWidget.getTable()).setRowAutoResizes(true);
			((JideTable) tableWidget.getTable()).setRowResizable(true);

			SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
					tableWidget.getTable().getModel(), SortableTableModel.class);
			sortableTableModel.sortColumn(0, true, false);
		}

		@Override
		protected JComponent createDialogContentPane() {
			JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

			tableWidget = new JideTableWidget<ScaglioneListinoStorico>("", new StoriaPrezzoTableModel(scaglioneListino
					.getRigaListino().getNumeroDecimaliPrezzo()));

			versioneCheckBox = new JCheckBox("Visualizza solo lo storico della versione corrente");
			versioneCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					caricaStorico();
				}
			});
			rootPanel.add(versioneCheckBox, BorderLayout.NORTH);
			rootPanel.add(tableWidget.getComponent(), BorderLayout.CENTER);
			rootPanel.setPreferredSize(new Dimension(700, 300));

			return rootPanel;
		}

		@Override
		protected void onAboutToShow() {
			super.onAboutToShow();
			caricaStorico();
			TableUtils.autoResizeAllColumns((tableWidget.getTable()), null, true, true);
		}
	}

	public static final String COMMAND_ID = "apriStoriaPrezzoListinoCommand";

	public static final String PARAM_RIGA_LISTINO = "paramRigaListino";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public ApriStoriaPrezzoListinoCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		RigaListino rigaListino = (RigaListino) getParameter(PARAM_RIGA_LISTINO, null);
		StoriaPrezzoDialog dialog = new StoriaPrezzoDialog(rigaListino.getScaglioni().iterator().next());
		dialog.showDialog();
	}

}
