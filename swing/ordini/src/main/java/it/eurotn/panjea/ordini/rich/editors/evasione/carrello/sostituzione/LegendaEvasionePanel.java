package it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione;

import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.CarrelloEvasioneOrdiniTableModel;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

public class LegendaEvasionePanel extends JPanel {

	private static final long serialVersionUID = -3703724812719641448L;

	public static final String PANEL_ID = "legendaEvasionePanel";
	public static final String LEGENDA_TITLE = PANEL_ID + ".legenda.title";

	public static final String RESIDUO_NEGATIVO_LABEL = PANEL_ID + ".residuoNegativoLabel";
	public static final String EVASO_PARZIALE_LABEL = PANEL_ID + ".evasoParzialeLabel";
	public static final String STANDARD_LABEL = PANEL_ID + ".standardLabel";

	/**
	 * Costruttore.
	 * 
	 */
	public LegendaEvasionePanel() {
		super(new VerticalLayout());

		// titolo
		JLabel titleLabel = new JLabel(RcpSupport.getMessage(LEGENDA_TITLE));
		titleLabel.setIcon(RcpSupport.getIcon("LegendaToggleCommand.icon"));
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		this.add(titleLabel);

		// separatore
		JSeparator jSeparator = new JSeparator(SwingConstants.HORIZONTAL);
		this.add(jSeparator);

		// panel fake solo per staccare il titolo dalla legenda
		JPanel fakePanel = new JPanel();
		fakePanel.setPreferredSize(new Dimension(20, 20));
		this.add(fakePanel);

		// label standard
		JLabel standardLabel = new JLabel(RcpSupport.getMessage(STANDARD_LABEL));
		this.add(standardLabel);

		// label residuo negativo
		JLabel residuoNegativoLabel = new JLabel(RcpSupport.getMessage(RESIDUO_NEGATIVO_LABEL));
		residuoNegativoLabel.setForeground(CarrelloEvasioneOrdiniTableModel.RESIDUO_NEGATIVO_COLOR);
		this.add(residuoNegativoLabel);

		// label evaso parziale
		JLabel evasoParzialeLabel = new JLabel(RcpSupport.getMessage(EVASO_PARZIALE_LABEL));
		evasoParzialeLabel.setForeground(CarrelloEvasioneOrdiniTableModel.EVASO_PARZIALE_COLOR);
		this.add(evasoParzialeLabel);

		GuiStandardUtils.attachBorder(this);
	}

}
