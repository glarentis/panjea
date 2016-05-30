/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.swing.TitledSeparator;

/**
 * Pannello dei totali che visualizza il valore di totale dare,avere e sbilancio per la tabella di righe contabili;
 * 
 * @author Leonardo
 */
public class TotaleRigheContabiliPanel extends JPanel implements TableModelListener {

	private static final long serialVersionUID = 8888540752855010185L;
	private JLabel labelTotaleDare = null;
	private JLabel labelTotaleAvere = null;
	private JLabel labelSbilancio = null;
	private JLabel labelSquadratura = null;
	private MessageSource messageSource = null;

	private RigheContabiliTableModel righeContabiliTableModel;

	/**
	 * Costruttore per il pannello totali righe contabili.
	 */
	public TotaleRigheContabiliPanel() {
		super();
		initialize();
	}

	/**
	 * Prepara i componenti del pannello aggiungendo: un labeledSeparator totali, a sx un messaggio se lo sbilancio
	 * risulta diverso da 0 a dx i totali dare, avere e sbilancio.
	 */
	private void initialize() {
		ComponentFactory componentFactory = (ComponentFactory) Application.services()
				.getService(ComponentFactory.class);
		messageSource = (MessageSource) Application.services().getService(MessageSource.class);

		labelTotaleDare = componentFactory.createLabel("");
		labelTotaleAvere = componentFactory.createLabel("");
		labelSbilancio = componentFactory.createLabel("");
		labelSquadratura = componentFactory.createLabel("");
		labelSquadratura.setName("TotaleRigheContabiliPanel.squadraturaLabel");

		JPanel panelTotali = componentFactory.createPanel();
		panelTotali.setLayout(new FlowLayout(FlowLayout.RIGHT));

		panelTotali.add(componentFactory.createLabel("<HTML><B>"
				+ messageSource.getMessage("totaleDare", null, Locale.getDefault()) + "</B></HTML>"));
		panelTotali.add(labelTotaleDare);
		panelTotali.add(Box.createRigidArea(new Dimension(20, 10)));
		panelTotali.add(componentFactory.createLabel("<HTML><B>"
				+ messageSource.getMessage("totaleAvere", null, Locale.getDefault()) + "</B></HTML>"));
		panelTotali.add(labelTotaleAvere);
		panelTotali.add(Box.createRigidArea(new Dimension(20, 10)));
		panelTotali.add(componentFactory.createLabel("<HTML><B>"
				+ messageSource.getMessage("sbilancio", null, Locale.getDefault()) + "</B></HTML>"));
		panelTotali.add(labelSbilancio);

		setLayout(new BorderLayout());
		TitledSeparator titledSeparator = new TitledSeparator(messageSource.getMessage("totaliSeparator", null,
				Locale.getDefault()), TitledSeparator.TYPE_PARTIAL_LINE, SwingConstants.LEFT);
		add(titledSeparator, BorderLayout.PAGE_START);

		JPanel panel2 = componentFactory.createPanel(new BorderLayout());
		panel2.add(labelSquadratura, BorderLayout.LINE_START);

		panel2.add(panelTotali, BorderLayout.CENTER);

		add(panel2, BorderLayout.PAGE_END);
	}

	/**
	 * 
	 * @param righeContabiliTableModel
	 *            table model delle righe contabili
	 */
	public void setRigheContabiliTableModel(RigheContabiliTableModel righeContabiliTableModel) {
		this.righeContabiliTableModel = righeContabiliTableModel;
		righeContabiliTableModel.addTableModelListener(this);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		update();
	}

	/**
	 * Aggiorna i valori delle label dei totali del conto avere e del conto dare. Aggiorna anche la differenza
	 * colcolandola e mettendo il testo della label in rosso se non e' uguale a 0 Se poi le righe contabili non
	 * risultano quadrate viene notificato con una label nella sezione totali
	 */
	public void update() {
		BigDecimal totaleDare = righeContabiliTableModel.getTotaleDare();
		BigDecimal totaleAvere = righeContabiliTableModel.getTotaleAvere();
		BigDecimal sbilancio = righeContabiliTableModel.getSbilancio();

		Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
		labelTotaleAvere.setText(format.format(totaleAvere));
		labelTotaleDare.setText(format.format(totaleDare));

		labelSbilancio.setText(format.format(sbilancio));
		if (sbilancio.compareTo(BigDecimal.ZERO) == 0) {
			labelSbilancio.setForeground(Color.BLACK);
		} else {
			labelSbilancio.setForeground(Color.RED);
		}

		labelSquadratura.setText("");
		labelSquadratura.setIcon(null);
		if (!righeContabiliTableModel.isRigheQuadrate()) {
			String textMessage = messageSource.getMessage("righeContabiliTablePage.nonQuadrate", null,
					Locale.getDefault());
			IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
			Icon icon = iconSource.getIcon("severity.warning");
			labelSquadratura.setText(textMessage);
			labelSquadratura.setIcon(icon);
		}
	}

}
