package it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.GiacenzaCalculator;
import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.LegendaToggleCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.springframework.richclient.settings.Settings;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableModelWrapperUtils;

public class RigaSostituzioneTablePage extends AbstractTablePageEditor<RigaDistintaCarico> {

	public enum PageMode {
		SOSTITUZIONE, LEGENDA
	}

	public static final String PAGE_ID = "rigaSostituzioneTablePage";
	private static final String VIEW_PAGE_SETTING = PAGE_ID + ".visualizzaSempre";

	public static final String RIGHE_SOSTITUZIONE_CHANGE = "righeSostituzioneChange";

	private boolean visualizzaSempre = false;

	private RigaDistintaCarico rigaEvasione = null;
	private GiacenzaCalculator giacenzaCalculator;

	private JCheckBox visualizzaSempreCheckBox;
	private RigaEvasioneRiepilogoComponent rigaEvasioneRiepilogoComponent;
	private GiacenzaSostituzioneRiepilogoComponent giacenzaSostituzioneRiepilogoComponent;

	private JPanel rootPanel;

	private LegendaToggleCommand legendaToggleCommand;

	/**
	 * Costruttore.
	 * 
	 * @param giacenzaCalculator
	 *            {@link GiacenzaCalculator}
	 * @param legendaToggleCommand
	 *            {@link LegendaToggleCommand}
	 */
	public RigaSostituzioneTablePage(final GiacenzaCalculator giacenzaCalculator,
			final LegendaToggleCommand legendaToggleCommand) {
		super(PAGE_ID, new RigaSostituzioneTableModel());
		setShowTitlePane(false);
		this.giacenzaCalculator = giacenzaCalculator;
		this.legendaToggleCommand = legendaToggleCommand;
		((JideTable) getTable().getTable()).setRowAutoResizes(true);
		getTableModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				if (!getTable().isAdjustingMode()) {
					rigaEvasione.clearRigheSostituzione();
					for (RigaDistintaCarico rigaDistintaCarico : getTableModel().getObjects()) {
						rigaEvasione.aggiungiRigaSostituzione(rigaDistintaCarico);
					}
					RigaSostituzioneTablePage.this.giacenzaCalculator.calculate(rigaEvasione.getRigheSostituzione());
					RigaSostituzioneTablePage.this.firePropertyChange(RIGHE_SOSTITUZIONE_CHANGE, false, true);
					getRigaEvasioneRiepilogoComponent().update(rigaEvasione);
					if (!rigaEvasione.isSostituita()) {
						getGiacenzaSostituzioneRiepilogoComponent().update((RigaDistintaCarico) null);
					} else {
						getGiacenzaSostituzioneRiepilogoComponent().update(getTable().getSelectedObject());
					}
				}
			}
		});
	}

	@Override
	protected JComponent createControl() {
		rootPanel = new JPanel(new CardLayout());
		rootPanel.add(PageMode.SOSTITUZIONE.name(), super.createControl());
		rootPanel.add(PageMode.LEGENDA.name(), createLegendPanel());
		return rootPanel;
	}

	/**
	 * @return componente delle lagenda.
	 */
	private JComponent createLegendPanel() {

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());

		JPanel buttonPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 1, 4));
		buttonPanel.add(legendaToggleCommand.createButton());

		panel.add(new LegendaEvasionePanel(), BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		panel.setBorder(BorderFactory.createEmptyBorder());

		return panel;
	}

	@Override
	public JComponent createToolbar() {
		JPanel rooPanel = getComponentFactory().createPanel(new BorderLayout());

		JPanel leftPanel = getComponentFactory().createPanel(new BorderLayout(5, 0));
		leftPanel.add(legendaToggleCommand.createButton(), BorderLayout.WEST);

		visualizzaSempreCheckBox = getComponentFactory().createCheckBox("Visualizza sempre");
		visualizzaSempreCheckBox.setSelected(visualizzaSempre);
		visualizzaSempreCheckBox.setAction(new AbstractAction() {
			private static final long serialVersionUID = 8382881979577920279L;

			@Override
			public void actionPerformed(ActionEvent actionevent) {
				visualizzaSempre = ((JCheckBox) actionevent.getSource()).isSelected();
			}
		});
		visualizzaSempreCheckBox.setText("Visualizza sempre");
		visualizzaSempreCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
		leftPanel.add(visualizzaSempreCheckBox, BorderLayout.EAST);

		rooPanel.add(leftPanel, BorderLayout.WEST);
		rooPanel.add(super.createToolbar(), BorderLayout.CENTER);
		return rooPanel;
	}

	@Override
	public JComponent getFooterControl() {
		JPanel footerPanel = getComponentFactory().createPanel(new BorderLayout());

		footerPanel.add(getGiacenzaSostituzioneRiepilogoComponent(), BorderLayout.NORTH);

		footerPanel.add(getRigaEvasioneRiepilogoComponent(), BorderLayout.CENTER);

		return footerPanel;
	}

	/**
	 * @return the giacenzaSostituzioneRiepilogoComponent
	 */
	public GiacenzaSostituzioneRiepilogoComponent getGiacenzaSostituzioneRiepilogoComponent() {
		if (giacenzaSostituzioneRiepilogoComponent == null) {
			giacenzaSostituzioneRiepilogoComponent = new GiacenzaSostituzioneRiepilogoComponent();
		}

		return giacenzaSostituzioneRiepilogoComponent;
	}

	/**
	 * @return the rigaEvasioneRiepilogoComponent
	 */
	public RigaEvasioneRiepilogoComponent getRigaEvasioneRiepilogoComponent() {
		if (rigaEvasioneRiepilogoComponent == null) {
			rigaEvasioneRiepilogoComponent = new RigaEvasioneRiepilogoComponent();
		}

		return rigaEvasioneRiepilogoComponent;
	}

	/**
	 * 
	 * @return tableModel della pagina
	 */
	RigaSostituzioneTableModel getTableModel() {
		RigaSostituzioneTableModel tableModel = (RigaSostituzioneTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		return tableModel;
	}

	/**
	 * @return the visualizzaSempre
	 */
	public boolean isVisualizzaSempre() {
		return visualizzaSempre;
	}

	@Override
	public Collection<RigaDistintaCarico> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(Collection<RigaDistintaCarico> results) {
		if (rigaEvasione != null) {
			getRigaEvasioneRiepilogoComponent().update(rigaEvasione);
		}

		super.processTableData(results);

		getTableModel().setRigaEvasione(rigaEvasione);
	}

	@Override
	public Collection<RigaDistintaCarico> refreshTableData() {
		Collection<RigaDistintaCarico> righe = Collections.emptyList();

		if (rigaEvasione != null) {
			righe = rigaEvasione.getRigheSostituzione();
		}

		return righe;
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);

		this.visualizzaSempre = true;
		if (settings.contains(VIEW_PAGE_SETTING)) {
			this.visualizzaSempre = settings.getBoolean(VIEW_PAGE_SETTING);
		}
		visualizzaSempreCheckBox.setSelected(this.visualizzaSempre);
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);

		settings.setBoolean(VIEW_PAGE_SETTING, this.visualizzaSempre);
	}

	@Override
	public void setFormObject(Object object) {
		rigaEvasione = (RigaDistintaCarico) object;
		getTableModel().setRigaEvasione(rigaEvasione);
		legendaToggleCommand.setSelected(false);
	}

	/**
	 * Configura la modalità di visualizzaizone della pagina.
	 * 
	 * @param pageMode
	 *            {@link PageMode}
	 */
	public void setPageMode(PageMode pageMode) {
		CardLayout cl = (CardLayout) (rootPanel.getLayout());
		cl.show(rootPanel, pageMode.name());
	}

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);
		getGiacenzaSostituzioneRiepilogoComponent().update((RigaDistintaCarico) obj);
	}
}
