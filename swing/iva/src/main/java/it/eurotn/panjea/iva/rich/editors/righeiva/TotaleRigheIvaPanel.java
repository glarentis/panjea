package it.eurotn.panjea.iva.rich.editors.righeiva;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.converter.ImportoConverter;
import it.eurotn.panjea.iva.rich.forms.righeiva.AbstractAreaIvaModel;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.TitledSeparator;

/**
 * Pannello dei totali che visualizza il valore di totale imposta e totale imponibile per la tabella di righe iva;il
 * totale viene aggiornato automaticamente con l'attivazione di un property change che intercetta le modifiche sulla
 * lista di righe iva.
 *
 * @author Leonardo
 */
public class TotaleRigheIvaPanel extends JPanel {

	/**
	 * Property change per intercettare quando vengono aggiornate le righe iva per l'area iva corrente.
	 *
	 * @author Leonardo
	 */
	private class RigheIvaAggiornateChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt != null) {
				update();
			}
		}
	}

	private ValutaAziendaCache valutaAziendaCache;

	private static final long serialVersionUID = 2186766300804875007L;
	private JLabel labelTotaleImponibile = null;
	private JLabel labelTotaleImposta = null;
	private JLabel labelSquadratura = null;
	private MessageSource messageSource = null;
	private AbstractAreaIvaModel areaIvaModel = null;

	private RigheIvaAggiornateChangeListener righeIvaAggiornateChangeListener = null;

	private DecimalFormat decimalFormat = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);

	private IconSource iconSource;

	/**
	 * Costruttore per il pannello totali righe iva.
	 */
	public TotaleRigheIvaPanel() {
		super();

		this.valutaAziendaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
		this.iconSource = (IconSource) Application.services().getService(IconSource.class);

		initialize();
	}

	/**
	 * @return RigheAggiornateChangeListener
	 */
	private PropertyChangeListener getRigheIvaAggiornateChangeListener() {
		if (righeIvaAggiornateChangeListener == null) {
			righeIvaAggiornateChangeListener = new RigheIvaAggiornateChangeListener();
		}
		return righeIvaAggiornateChangeListener;
	}

	/**
	 * Prepara i componenti del pannello aggiungendo: un labeledSeparator totali, a sx un messaggio se il risultato non
	 * e' quadrato rispetto al tot documento,e a dx i due totali imponibile e imposta.
	 */
	private void initialize() {
		setLayout(new BorderLayout());

		ComponentFactory componentFactory = (ComponentFactory) Application.services()
				.getService(ComponentFactory.class);

		labelTotaleImponibile = componentFactory.createLabel("");
		labelTotaleImposta = componentFactory.createLabel("");
		labelSquadratura = componentFactory.createLabel("");
		labelSquadratura.setName("TotaleRigheIvaPanel.squadraturaLabel");

		JPanel panelTotali = componentFactory.createPanel();
		panelTotali.setLayout(new FlowLayout(FlowLayout.RIGHT));

		messageSource = (MessageSource) Application.services().getService(MessageSource.class);

		panelTotali.add(componentFactory.createLabel("<HTML><B>"
				+ messageSource.getMessage("totaleImponibile", null, Locale.getDefault()) + "</B></HTML>"));
		panelTotali.add(labelTotaleImponibile);
		panelTotali.add(Box.createRigidArea(new Dimension(20, 10)));
		panelTotali.add(componentFactory.createLabel("<HTML><B>"
				+ messageSource.getMessage("totaleImposta", null, Locale.getDefault()) + "</B></HTML>"));
		panelTotali.add(labelTotaleImposta);

		TitledSeparator titledSeparator = new TitledSeparator(messageSource.getMessage("totaliSeparator", null,
				Locale.getDefault()), TitledSeparator.TYPE_PARTIAL_LINE, SwingConstants.LEFT);
		add(titledSeparator, BorderLayout.PAGE_START);

		JPanel panel2 = componentFactory.createPanel(new BorderLayout());
		panel2.add(labelSquadratura, BorderLayout.LINE_START);
		panel2.add(panelTotali, BorderLayout.CENTER);
		add(panel2, BorderLayout.PAGE_END);
	}

	/**
	 * Imposta per questo pannello l'areaIvaPM; <br/>
	 * quando viene aggiornata la lista di righe iva il pannello viene aggiornato.
	 *
	 * @param areaIvaModel
	 *            area iva model
	 */
	public void setAreaIvaModel(AbstractAreaIvaModel areaIvaModel) {
		this.areaIvaModel = areaIvaModel;
		this.areaIvaModel.removePropertyChangeListener(AbstractAreaIvaModel.RIGA_AGGIORNATA,
				getRigheIvaAggiornateChangeListener());
		this.areaIvaModel.addPropertyChangeListener(AbstractAreaIvaModel.RIGA_AGGIORNATA,
				getRigheIvaAggiornateChangeListener());
	}

	/**
	 * Aggiorna i valori delle label dei totali imponibile e imposta; se il totale risulta quadrato non mostra nulla, al
	 * contrario viene presentato un messaggio di WARNING.
	 */
	public void update() {
		if (!areaIvaModel.isAreaIvaPresente()) {
			labelTotaleImponibile.setText("");
			labelTotaleImposta.setText("");
			labelSquadratura.setText("");
			return;
		}

		Importo imponibile = areaIvaModel.getTotaleImponibile();
		String codiceValuta = imponibile.getCodiceValuta();
		String simboloValuta = "";
		if (codiceValuta != null && !codiceValuta.equals("")) {
			simboloValuta = valutaAziendaCache.caricaValutaAzienda(codiceValuta).getSimbolo();
		}
		labelTotaleImponibile.setText(decimalFormat.format(imponibile.getImportoInValuta()) + " " + simboloValuta);
		labelTotaleImponibile.setToolTipText(ObjectConverterManager.toString(imponibile, Importo.class,
				ImportoConverter.HTML_CONVERTER_CONTEXT));

		Importo imposta = areaIvaModel.getTotaleImposta();
		labelTotaleImposta.setText(decimalFormat.format(imposta.getImportoInValuta()) + " " + simboloValuta);
		labelTotaleImposta.setToolTipText(ObjectConverterManager.toString(imposta, Importo.class,
				ImportoConverter.HTML_CONVERTER_CONTEXT));

		labelSquadratura.setText("");
		labelSquadratura.setIcon(null);
		if (!areaIvaModel.isAreaIvaQuadrata()) {

			String textMessage = messageSource.getMessage("righeIvaTablePage.nonQuadrate", null, Locale.getDefault());
			textMessage = textMessage + " " + decimalFormat.format(areaIvaModel.getImportoSquadrato()) + " €";
			Icon icon = iconSource.getIcon("severity.warning");
			labelSquadratura.setText(textMessage);
			labelSquadratura.setIcon(icon);
		}
	}
}
