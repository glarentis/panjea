/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.AggiungiProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.RigaNessunaVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.RigaNonPresenteInRigaVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.RigaPresenteInRigaVariazioneProvivgioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.RigaTutteVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.SostituisciProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.AggiungiScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.RigaNessunaVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.RigaNonPresenteDaContrattoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.RigaNonPresentiInRigaVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.RigaPresenteDaContrattoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.RigaPresentiInRigaVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.RigaTutteVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.SostituisciScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.plugin.PluginManager;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.springframework.richclient.components.BigDecimalTextField;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 */
public class AggiungiVariazioneDialog extends ConfirmationDialog {

	private class StrategyRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 2592949983230648812L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel valueLabel = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			valueLabel.setText(getMessage(value.getClass().getName()));

			return valueLabel;
		}
	}

	private BigDecimalTextField variazioneTextField;
	private JComboBox<RigaDocumentoVariazioneScontoStrategy> scontoStrategyComboBox;
	private JComboBox<TipoVariazioneScontoStrategy> tipoVariazioneScontoComboBox;
	private RigaDocumentoVariazioneScontoStrategy[] scontoStrategyArray = new RigaDocumentoVariazioneScontoStrategy[] {
			new RigaNessunaVariazioneScontoStrategy(), new RigaTutteVariazioneScontoStrategy(),
			new RigaPresenteDaContrattoVariazioneScontoStrategy(),
			new RigaNonPresenteDaContrattoVariazioneScontoStrategy(), new RigaPresentiInRigaVariazioneScontoStrategy(),
			new RigaNonPresentiInRigaVariazioneScontoStrategy() };
	private TipoVariazioneScontoStrategy[] tipoScontoArray = new TipoVariazioneScontoStrategy[] {
			new AggiungiScontoStrategy(), new SostituisciScontoStrategy() };

	private BigDecimalTextField percProvvigioneTextField;
	private JComboBox<RigaDocumentoVariazioneProvvigioneStrategy> provvigioneStrategyComboBox;
	private JComboBox<TipoVariazioneProvvigioneStrategy> tipoVariazioneProvvigioneComboBox;
	private RigaDocumentoVariazioneProvvigioneStrategy[] provvigioneStrategyArray = new RigaDocumentoVariazioneProvvigioneStrategy[] {
			new RigaNessunaVariazioneProvvigioneStrategy(), new RigaTutteVariazioneProvvigioneStrategy(),
			new RigaPresenteInRigaVariazioneProvivgioneStrategy(),
			new RigaNonPresenteInRigaVariazioneProvvigioneStrategy() };
	private TipoVariazioneProvvigioneStrategy[] tipoProvvigioneArray = new TipoVariazioneProvvigioneStrategy[] {
			new AggiungiProvvigioneStrategy(), new SostituisciProvvigioneStrategy() };

	private StrategyRenderer strategyRenderer = new StrategyRenderer();

	private boolean gestioneProvvigioniAgente = true;

	/**
	 * Costruttore.
	 */
	public AggiungiVariazioneDialog() {
		this(true);
	}

	/**
	 * 
	 * @param gestisciProvvigioniAgente
	 *            indica se le provvigioni agente devono essere gestite
	 */
	public AggiungiVariazioneDialog(final boolean gestisciProvvigioniAgente) {
		super("Inserimeno variazioni", "Inserimeno variazioni");

		this.gestioneProvvigioniAgente = gestisciProvvigioniAgente;

		// se non ho il plugin degli agenti non posso comunque gestirle
		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		this.gestioneProvvigioniAgente = this.gestioneProvvigioniAgente
				&& pluginManager.isPresente(PluginManager.PLUGIN_AGENTI);
	}

	@Override
	protected JComponent createDialogContentPane() {

		FormLayout layout = new FormLayout("right:pref,4dlu,50dlu,10dlu,100dlu",
				"2dlu,pref,2dlu,pref,2dlu,10dlu,2dlu,pref,2dlu,pref");
		JPanel rootPanel = getComponentFactory().createPanel(layout);

		CellConstraints cc = new CellConstraints();

		rootPanel.add(new JLabel("Inserire la variazione (negativa)"), cc.xy(1, 2));
		variazioneTextField = new BigDecimalTextField(3, 2, true);
		variazioneTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		rootPanel.add(variazioneTextField, cc.xy(3, 2));
		rootPanel.add(new JLabel("%"), cc.xy(4, 2));

		tipoVariazioneScontoComboBox = new JComboBox<TipoVariazioneScontoStrategy>(tipoScontoArray);
		tipoVariazioneScontoComboBox.setSelectedIndex(0);
		tipoVariazioneScontoComboBox.setRenderer(strategyRenderer);
		rootPanel.add(tipoVariazioneScontoComboBox, cc.xy(5, 2));

		rootPanel.add(new JLabel("Righe da modificare"), cc.xy(1, 4));
		scontoStrategyComboBox = new JComboBox<RigaDocumentoVariazioneScontoStrategy>(scontoStrategyArray);
		scontoStrategyComboBox.setSelectedIndex(0);
		scontoStrategyComboBox.setRenderer(strategyRenderer);
		rootPanel.add(scontoStrategyComboBox, cc.xyw(3, 4, 3));

		if (gestioneProvvigioniAgente) {

			rootPanel.add(new JSeparator(), cc.xyw(1, 6, 5));

			rootPanel.add(new JLabel("Inserire la provvigione"), cc.xy(1, 8));
			percProvvigioneTextField = new BigDecimalTextField(3, 2, true);
			percProvvigioneTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			rootPanel.add(percProvvigioneTextField, cc.xy(3, 8));
			rootPanel.add(new JLabel("%"), cc.xy(4, 8));

			tipoVariazioneProvvigioneComboBox = new JComboBox<TipoVariazioneProvvigioneStrategy>(tipoProvvigioneArray);
			tipoVariazioneProvvigioneComboBox.setSelectedIndex(0);
			tipoVariazioneProvvigioneComboBox.setRenderer(strategyRenderer);
			rootPanel.add(tipoVariazioneProvvigioneComboBox, cc.xy(5, 8));

			rootPanel.add(new JLabel("Righe da modificare"), cc.xy(1, 10));
			provvigioneStrategyComboBox = new JComboBox<RigaDocumentoVariazioneProvvigioneStrategy>(
					provvigioneStrategyArray);
			provvigioneStrategyComboBox.setSelectedIndex(0);
			provvigioneStrategyComboBox.setRenderer(strategyRenderer);
			rootPanel.add(provvigioneStrategyComboBox, cc.xyw(3, 10, 3));
		}

		return rootPanel;
	}

	@Override
	protected String getCancelCommandId() {
		return "cancelCommand";
	}

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	}

	/**
	 * @return the provvigione to get
	 */
	public BigDecimal getProvvigione() {
		return percProvvigioneTextField == null ? null : (BigDecimal) percProvvigioneTextField.getValue();
	}

	/**
	 * @return tipo variazione provvigione selezionata.
	 */
	public TipoVariazioneProvvigioneStrategy getTipoVariazioneProvvigioneStrategy() {
		if (gestioneProvvigioniAgente) {
			return (TipoVariazioneProvvigioneStrategy) tipoVariazioneProvvigioneComboBox.getSelectedItem();
		} else {
			return null;
		}
	}

	/**
	 * @return tipo variazione sconto selezionata.
	 */
	public TipoVariazioneScontoStrategy getTipoVariazioneScontoStrategy() {
		return (TipoVariazioneScontoStrategy) tipoVariazioneScontoComboBox.getSelectedItem();
	}

	/**
	 * @return the variazione to get
	 */
	public BigDecimal getVariazione() {
		return (BigDecimal) variazioneTextField.getValue();
	}

	/**
	 * @return variazione provvigione selezionata.
	 */
	public RigaDocumentoVariazioneProvvigioneStrategy getVariazioneProvvigioneStrategy() {
		if (gestioneProvvigioniAgente) {
			return (RigaDocumentoVariazioneProvvigioneStrategy) provvigioneStrategyComboBox.getSelectedItem();
		} else {
			return new RigaNessunaVariazioneProvvigioneStrategy();
		}
	}

	/**
	 * @return variazione sconto selezionata.
	 */
	public RigaDocumentoVariazioneScontoStrategy getVariazioneScontoStrategy() {
		return (RigaDocumentoVariazioneScontoStrategy) scontoStrategyComboBox.getSelectedItem();
	}

	@Override
	protected void onCancel() {
		variazioneTextField.setValue(null);
		scontoStrategyComboBox.setSelectedItem(0);
		tipoVariazioneScontoComboBox.setSelectedIndex(0);
		if (gestioneProvvigioniAgente) {
			percProvvigioneTextField.setValue(null);
			provvigioneStrategyComboBox.setSelectedIndex(0);
			tipoVariazioneProvvigioneComboBox.setSelectedIndex(0);
		}
		super.onCancel();
	}

	@Override
	protected void onConfirm() {

	}
}
