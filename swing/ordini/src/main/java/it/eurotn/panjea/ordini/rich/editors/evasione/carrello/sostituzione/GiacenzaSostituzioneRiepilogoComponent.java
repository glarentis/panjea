package it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.springframework.richclient.util.RcpSupport;

public class GiacenzaSostituzioneRiepilogoComponent extends JPanel {

	private static final long serialVersionUID = -3576535830248659964L;

	private DecimalFormat format = new DecimalFormat("#,##0.000000");

	private JLabel lblQtaGiacenza = new JLabel();
	private JLabel lblQtaGiacenzaAttuale = new JLabel();

	/**
	 * Costruttore.
	 */
	public GiacenzaSostituzioneRiepilogoComponent() {
		super();
		setLayout(new GridLayout(1, 2, 5, 2));
		setBorder(new BottomBorder(Color.BLACK));
		initialize();
	}

	/**
	 * Crea il componente per visualizzare la proprietà specificata.
	 * 
	 * @param propertyName
	 *            nome della proprietà
	 * @param propertyComponent
	 *            componente per visualizzare il valore della proprietà
	 * @return componente creato
	 */
	private JComponent createRiepilogoComponent(String propertyName, JLabel propertyComponent) {
		JPanel propertyPanel = new JPanel(new BorderLayout());
		JLabel propertyLabel = new JLabel(RcpSupport.getMessage(propertyName) + ":");
		propertyLabel.setFont(propertyLabel.getFont().deriveFont(Font.BOLD));
		propertyPanel.add(propertyLabel, BorderLayout.WEST);
		propertyPanel.add(propertyComponent, BorderLayout.EAST);

		return propertyPanel;
	}

	/**
	 * Inizializza tutti i componenti.
	 */
	private void initialize() {

		add(createRiepilogoComponent("qtaGiacenza", lblQtaGiacenza));
		add(createRiepilogoComponent("qtaGiacenzaAttuale", lblQtaGiacenzaAttuale));
	}

	/**
	 * Aggiorna i valori visualizzati.
	 * 
	 * @param rigaSostituzione
	 *            riga sostituzione
	 */
	public void update(RigaDistintaCarico rigaSostituzione) {

		if (rigaSostituzione == null) {
			lblQtaGiacenza.setText(format.format(0.0));
			lblQtaGiacenzaAttuale.setText(format.format(0.0));
		} else {

			Double qtaGiacenzaAttuale = rigaSostituzione.getQtaGiacenzaAttuale() == null ? 0 : rigaSostituzione
					.getQtaGiacenzaAttuale();
			lblQtaGiacenza.setText(format.format(rigaSostituzione.getQtaGiacenza()));
			lblQtaGiacenzaAttuale.setText(format.format(qtaGiacenzaAttuale));

			Font font = new Font(lblQtaGiacenza.getFont().getName(), Font.PLAIN, lblQtaGiacenza.getFont().getSize());
			Color textColor = UIManager.getColor("Label.foreground");

			if (qtaGiacenzaAttuale.compareTo(new Double(0.0)) < 0) {
				font = new Font(lblQtaGiacenza.getFont().getName(), Font.BOLD, lblQtaGiacenza.getFont().getSize());
				textColor = Color.RED;
			}

			lblQtaGiacenzaAttuale.setFont(font);
			lblQtaGiacenzaAttuale.setForeground(textColor);
		}
	}
}
