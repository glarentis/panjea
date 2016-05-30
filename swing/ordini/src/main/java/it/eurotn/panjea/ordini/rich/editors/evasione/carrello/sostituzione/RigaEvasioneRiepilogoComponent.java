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

import org.springframework.richclient.util.RcpSupport;

public class RigaEvasioneRiepilogoComponent extends JPanel {

	private static final long serialVersionUID = 3783109166897419730L;

	private DecimalFormat format = new DecimalFormat("#,##0.000000");

	private JLabel lblQtaOrdinata = new JLabel();
	private JLabel lblQtaResidua = new JLabel();
	private JLabel lblQtaDaEvadere = new JLabel();
	private JLabel lblQtaDaEvadereSostituzione = new JLabel();

	/**
	 * Costruttore.
	 */
	public RigaEvasioneRiepilogoComponent() {
		super();
		setLayout(new GridLayout(2, 2, 5, 2));
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

		// schema: qta ordinata qta residua
		// qta da evadere qta da evadere x sost
		add(createRiepilogoComponent("qtaDaEvadere", lblQtaDaEvadere));
		add(createRiepilogoComponent("qtaDaEvadereSostituzione", lblQtaDaEvadereSostituzione));
		add(createRiepilogoComponent("qtaOrdinata", lblQtaOrdinata));
		add(createRiepilogoComponent("qtaResidua", lblQtaResidua));
	}

	/**
	 * Aggiorna i valori visualizzati.
	 * 
	 * @param rigaEvasione
	 *            riga evasione originale
	 */
	public void update(RigaDistintaCarico rigaEvasione) {
		// update valori
		lblQtaOrdinata.setText(format.format(rigaEvasione.getQtaOrdinata()));
		lblQtaResidua.setText(format.format(rigaEvasione.getQtaResidua()));
		lblQtaDaEvadere.setText(format.format(rigaEvasione.getQtaDaEvadere()));
		lblQtaDaEvadereSostituzione.setText(format.format(rigaEvasione.getQtaDaEvadereSostituzione()));

		Font redFont = lblQtaOrdinata.getFont();
		redFont = new Font(redFont.getName(), Font.BOLD, redFont.getSize());
		Color redTextColor = Color.RED;

		if (rigaEvasione.getQtaResidua().compareTo(new Double(0.0)) < 0) {
			lblQtaResidua.setFont(redFont);
			lblQtaResidua.setForeground(redTextColor);
		} else {
			lblQtaResidua.setFont(lblQtaOrdinata.getFont());
			lblQtaResidua.setForeground(lblQtaOrdinata.getForeground());
		}
	}
}
