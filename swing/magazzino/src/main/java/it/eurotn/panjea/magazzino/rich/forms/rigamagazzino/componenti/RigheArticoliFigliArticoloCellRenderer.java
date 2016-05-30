package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.componenti;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.plugin.PluginManager;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;

public class RigheArticoliFigliArticoloCellRenderer extends ContextSensitiveCellRenderer {

	private static class LottoLabel extends JLabel {

		private static final long serialVersionUID = -4722475076650126779L;

		/**
		 * Costruttore.
		 */
		public LottoLabel() {
			super("");
			setIcon(LOTTO_ICON);
			// setOpaque(false);
		}
	}

	public static final EditorContext RIGHE_ARTICOLI_FIGLI_ARTICOLO_CONTEXT = new EditorContext(
			"RIGHE_ARTICOLI_FIGLI_ARTICOLO_CONTEXT");

	private static final long serialVersionUID = -2318431791366819568L;

	private static final Icon ARTICOLO_ICON;
	private static final Icon LOTTO_ICON;

	private static final boolean PLUGIN_LOTTI_PRESENTE;

	private static LottoLabel lottoLabel;

	static {
		ARTICOLO_ICON = RcpSupport.getIcon(Articolo.class.getName());
		LOTTO_ICON = RcpSupport.getIcon(Lotto.class.getName());

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		PLUGIN_LOTTI_PRESENTE = pluginManager.isPresente(PluginManager.PLUGIN_LOTTI);

		lottoLabel = new LottoLabel();
	}

	private JPanel rootPanel = new JPanel(new BorderLayout());

	/**
	 * Costruttore.
	 */
	public RigheArticoliFigliArticoloCellRenderer() {
		super();

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		label.setText(label.getText());
		label.setIcon(ARTICOLO_ICON);

		// rootPanel.removeAll();
		rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(label, BorderLayout.CENTER);
		rootPanel.setOpaque(false);

		if (PLUGIN_LOTTI_PRESENTE && ((ArticoloLite) value) != null
				&& ((ArticoloLite) value).getTipoLotto() != TipoLotto.NESSUNO) {
			rootPanel.add(lottoLabel, BorderLayout.EAST);
		}

		return rootPanel;
	}
}
