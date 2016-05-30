/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart;

import it.eurotn.panjea.bi.rich.MappeBiManager;
import it.eurotn.panjea.bi.util.Mappa;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.components.WrapLayout;
import it.eurotn.util.KeyFromValueProvider;
import it.eurotn.util.PanjeaEJBUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.swing.JideTabbedPane;

/**
 * @author fattazzo
 *
 */
public abstract class MapSelectionDialog extends ConfirmationDialog {

	private class MappaCommand extends JideToggleCommand {

		private Mappa mappa;

		/**
		 * Costruttore.
		 *
		 * @param mappa
		 *            mappa
		 */
		public MappaCommand(final Mappa mappa) {
			super();
			this.mappa = mappa;
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setText(mappa.getDescrizione());
			button.setFocusable(false);
			button.setVerticalTextPosition(SwingConstants.BOTTOM);
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.setEnabled(isDataModelValidForMap(mappa));
			button.setIcon(new ImageIcon(mappa.getImage()));

		}

		@Override
		protected void onDeselection() {
			// fileMappeSelezionate.remove(mappa.getFileName());
		}

		@Override
		protected void onSelection() {
			// fileMappeSelezionate.add(mappa.getFileName());
			fileMappeSelezionate.clear();
			fileMappeSelezionate.add(mappa.getFileName());
		}

	}

	private class ReloadMapsCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public ReloadMapsCommand() {
			super("reloadMapsCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			// cancello le mappe già scaricate dall'utente
			mappeBiManager.clearUserMapDir();
			refreshControl();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setFocusable(false);
		}

	}

	// la mappa contiene nella chiave il nome del file e nel valore la sua descrizione
	private List<Mappa> mappePresenti;

	private Set<String> fileMappeSelezionate;

	private JideTabbedPane mapsJideTabbedPane;

	private IPivotDataModel pivotDataModel;

	private MappeBiManager mappeBiManager;

	/**
	 * Costruttore.
	 *
	 * @param paramPivotDataModel
	 *            pive data model
	 */
	public MapSelectionDialog(final IPivotDataModel paramPivotDataModel) {
		super();
		setTitle("Selezionare una mappa");
		mappeBiManager = new MappeBiManager();
		pivotDataModel = paramPivotDataModel;
		setPreferredSize(new Dimension(500, 400));
	}

	@Override
	protected JComponent createDialogContentPane() {
		mapsJideTabbedPane = new JideTabbedPane();
		refreshControl();

		JPanel refreshPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT));
		refreshPanel.add(new ReloadMapsCommand().createButton());

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(refreshPanel, BorderLayout.NORTH);

		rootPanel.add(mapsJideTabbedPane, BorderLayout.CENTER);

		return rootPanel;
	}

	@Override
	protected String getCancelCommandId() {
		return "cancelCommand";
	}

	/**
	 * @return the fileMappeSelezionate
	 */
	public Set<String> getFileMappeSelezionate() {
		return fileMappeSelezionate;
	}

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	};

	/**
	 * Indica se il pivot data model è valido per essere gestito della mappa.
	 *
	 * @param mappa
	 *            mappa di riferimento
	 * @return <code>true</code> se valido
	 */
	private boolean isDataModelValidForMap(Mappa mappa) {
		return pivotDataModel.getColumnFields().length == 0 && pivotDataModel.getRowFields().length == 1
				&& mappa.getManagedFields().contains(pivotDataModel.getRowFields()[0].getName());
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();
		refreshControl();

		fileMappeSelezionate = new TreeSet<String>();
	}

	private void refreshControl() {
		mapsJideTabbedPane.removeAll();

		mappePresenti = mappeBiManager.caricaMappe();

		// raggruppo le mappe per categoria
		Map<String, List<Mappa>> mappeRaggruppate = PanjeaEJBUtil.listToMap(mappePresenti,
				new KeyFromValueProvider<Mappa, String>() {
			@Override
			public String keyFromValue(Mappa mappa) {
				return mappa.getCategoria();
			}
		});

		// Solo una mappa alla volta sarà selezionabile quindi metto tutti i command in un commandgroup
		ButtonGroup buttonGroup = new ButtonGroup();

		// creo un tab per ogni cateogoria
		for (Entry<String, List<Mappa>> entry : mappeRaggruppate.entrySet()) {
			JPanel mapsPanel = getComponentFactory().createPanel(new WrapLayout(FlowLayout.LEFT, 5, 5));

			for (Mappa mappa : entry.getValue()) {
				AbstractButton button = new MappaCommand(mappa).createButton();
				buttonGroup.add(button);
				mapsPanel.add(button);
			}

			JScrollPane scrollPane = new JScrollPane(mapsPanel);
			scrollPane.setBorder(null);
			mapsJideTabbedPane.addTab(entry.getKey(), scrollPane);
		}
	}

}
