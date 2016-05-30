package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.renderer.TipoAreaMagazzinoListCellRenderer;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.list.ComboBoxListModel;
import org.springframework.richclient.util.RcpSupport;

public class EntitaSenzaTipoDocumentoEvasionePage extends AbstractDialogPage {

	private class AssegnaTipoAreaEvasioneCommand extends ActionCommand {

		public static final String COMMAND_ID = "assegnaTipoAreaEvasioneCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public AssegnaTipoAreaEvasioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			if (tableWidget.getTable().getCellEditor() != null) {
				tableWidget.getTable().getCellEditor().stopCellEditing();
			}

			TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) tipiAreaComboBox.getSelectedItem();

			for (EntitaEvasione entitaEvasione : tableWidget.getRows()) {
				if (entitaEvasione.getSelezionata()) {
					entitaEvasione.setTipoAreaEvasione(tipoAreaMagazzino);
					entitaEvasione.setSelezionata(Boolean.FALSE);
				}
			}
			tableWidget.getTable().repaint();
		}

	}

	public static final String PAGE_ID = "entitaSenzaTipoDocumentoEvasionePage";
	public static final String TABLE_TITLE_LABEL = PAGE_ID + ".table.title";

	public static final String LABEL_SELEZIONE_TIPO_AREA = PAGE_ID + ".label.selezione.tipoArea";

	private JideTableWidget<EntitaEvasione> tableWidget;

	private JComboBox<TipoAreaMagazzino> tipiAreaComboBox;

	private EntitaSenzaTipoDocumentoEvasioneException exception;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private AssegnaTipoAreaEvasioneCommand assegnaTipoAreaEvasioneCommand;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccezione
	 * 
	 */
	public EntitaSenzaTipoDocumentoEvasionePage(final EntitaSenzaTipoDocumentoEvasioneException exception) {
		super(PAGE_ID);
		this.exception = exception;
		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
	}

	@Override
	protected JComponent createControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		rootPanel.add(createTable(), BorderLayout.CENTER);
		rootPanel.add(createFooterComponent(), BorderLayout.SOUTH);

		return rootPanel;
	}

	/**
	 * Crea i controlli che verranno posizionati sotto la tabella.
	 * 
	 * @return controlli creati
	 */
	@SuppressWarnings("unchecked")
	private JComponent createFooterComponent() {

		JPanel rootPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		rootPanel.setPreferredSize(new Dimension(200, 50));
		rootPanel.setBorder(BorderFactory.createTitledBorder(RcpSupport.getMessage(LABEL_SELEZIONE_TIPO_AREA)));

		tipiAreaComboBox = getComponentFactory().createComboBox();
		tipiAreaComboBox.setModel(new ComboBoxListModel(magazzinoDocumentoBD.caricaTipiAreaMagazzino(
				"tipoDocumento.codice", null, false)));
		tipiAreaComboBox.setRenderer(new TipoAreaMagazzinoListCellRenderer());

		rootPanel.add(tipiAreaComboBox);
		rootPanel.add(getAssegnaTipoAreaEvasioneCommand().createButton());

		return rootPanel;
	}

	/**
	 * @return tabella
	 */
	private JComponent createTable() {

		EntitaSenzaTipoDocumentoTableModel tableModel = new EntitaSenzaTipoDocumentoTableModel(PAGE_ID + ".tableModel");

		tableWidget = new JideTableWidget<EntitaEvasione>(PAGE_ID + ".table", tableModel);

		List<EntitaEvasione> entitaEvasione = new ArrayList<EntitaEvasione>();
		for (EntitaLite entita : exception.getEntita()) {
			entitaEvasione.add(new EntitaEvasione(entita));
		}
		tableWidget.setRows(entitaEvasione);

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(tableWidget.getComponent(), BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createTitledBorder(RcpSupport.getMessage(TABLE_TITLE_LABEL)));
		return panel;
	}

	/**
	 * @return the assegnaTipoAreaEvasioneCommand
	 */
	public AssegnaTipoAreaEvasioneCommand getAssegnaTipoAreaEvasioneCommand() {
		if (assegnaTipoAreaEvasioneCommand == null) {
			assegnaTipoAreaEvasioneCommand = new AssegnaTipoAreaEvasioneCommand();
		}

		return assegnaTipoAreaEvasioneCommand;
	}

	/**
	 * @return entità evasione
	 */
	public Collection<EntitaEvasione> getEntitaEvasione() {
		return tableWidget.getRows();
	}
}
