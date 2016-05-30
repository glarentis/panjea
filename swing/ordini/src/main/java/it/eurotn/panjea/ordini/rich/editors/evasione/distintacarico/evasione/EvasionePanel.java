package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.evasione;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;
import it.eurotn.panjea.contabilita.util.ContabilizzazioneStateDescriptor;
import it.eurotn.panjea.magazzino.util.FatturazioneStateDescriptor;
import it.eurotn.panjea.ordini.util.EvasioneStateDescriptor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.core.TitleConfigurable;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

public class EvasionePanel extends JPanel implements TitleConfigurable, PropertyChangeListener {

	private static final long serialVersionUID = 7719786795542137280L;

	private static final String PANEL_ID = "evasionePanel";

	private JLabel labelTitle;

	private Map<Integer, EvasioneStepPanel> mapStepPanel = new HashMap<Integer, EvasioneStepPanel>();

	private EvasioneOrdiniMessageDelegate messageDelegate = RcpSupport
			.getBean(EvasioneOrdiniMessageDelegate.DELEGATE_ID);

	/**
	 * Costruttore.
	 */
	public EvasionePanel() {
		super(new VerticalLayout(5));
		GuiStandardUtils.attachBorder(this);

		initControl();
		RcpSupport.configure(this, PANEL_ID);
	}

	/**
	 * Inizializza i controlli.
	 */
	private void initControl() {

		labelTitle = new JLabel();
		add(labelTitle);
		add(new JSeparator());

		EvasioneStepPanel insRigheEvasioneStepPanel = new EvasioneStepPanel("inserimentoRigheEvasioneStepPanel", true);
		mapStepPanel.put(EvasioneStateDescriptor.STATE_INSERIMENTO_RIGHE_EVASIONE, insRigheEvasioneStepPanel);
		add(insRigheEvasioneStepPanel);

		EvasioneStepPanel verificaCarrelloStepPanel = new EvasioneStepPanel("verificaCarrelloStepPanel", false);
		mapStepPanel.put(EvasioneStateDescriptor.STATE_VERIFICA_CARRELLO, verificaCarrelloStepPanel);
		add(verificaCarrelloStepPanel);

		EvasioneStepPanel creazioneMovimentiStepPanel = new EvasioneStepPanel("creazioneMovimentiStepPanel", true);
		mapStepPanel.put(FatturazioneStateDescriptor.STATE_CREAZIONE_MOVIMENTI, creazioneMovimentiStepPanel);
		add(creazioneMovimentiStepPanel);

		EvasioneStepPanel contabilizzazioneStepPanel = new EvasioneStepPanel("contabilizzazioneStepPanel", true);
		mapStepPanel.put(ContabilizzazioneStateDescriptor.STATE_CONTABILIZZAZIONE, contabilizzazioneStepPanel);
		add(contabilizzazioneStepPanel);

		messageDelegate.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(EvasioneOrdiniMessageDelegate.OBJECT_CHANGE)) {

			AbstractStateDescriptor stateDescriptor = (AbstractStateDescriptor) evt.getNewValue();

			mapStepPanel.get(stateDescriptor.getState()).progress(stateDescriptor);
		}
	}

	/**
	 * Resetta tutti i valori del pannello di evasione.
	 */
	public void reset() {
		for (EvasioneStepPanel evasioneStepPanel : mapStepPanel.values()) {
			evasioneStepPanel.reset();
		}
	}

	@Override
	public void setTitle(String text) {
		labelTitle.setText(text);
	}
}
