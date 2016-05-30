package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartita;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.factory.ControlFactory;

/**
 * Visualizza i controlli appropriati per la strategiaCalcoloPartitaComponent.
 * 
 * @author giangi
 */
public class StrategiaCalcoloPartitaComponent extends AbstractControlFactory implements PropertyChangeListener {
	private final CardLayout cardLayout = new CardLayout();

	/*
	 * mantiene l'istanza delle strategie da poter assegnare
	 */
	private final ValueHolder strategie;

	/*
	 * con il valueModel posso registrarmi sia alla proprietà stategia1 sia alla strategia2 indiffentemente
	 */
	private final ValueModel valueModel;

	private JPanel mainPanel;

	private HierarchicalFormModel parentModel;

	/**
	 * Costruttore.
	 * 
	 * @param strategieCalcoloPartite
	 *            strategieCalcoloPartite
	 * @param valueModel
	 *            valueModel
	 * @param parentFormModel
	 *            formModel legato a questi componenti
	 * 
	 */
	public StrategiaCalcoloPartitaComponent(final ValueHolder strategieCalcoloPartite, final ValueModel valueModel,
			final HierarchicalFormModel parentFormModel) {
		super();
		this.strategie = strategieCalcoloPartite;
		this.valueModel = valueModel;
		this.valueModel.addValueChangeListener(this);
		this.parentModel = parentFormModel;
	}

	@Override
	protected JComponent createControl() {
		mainPanel = new JPanel(cardLayout);
		// creo i pannelli per le varie strategie
		StrategiaCalcoloPartita[] strategieArray = (StrategiaCalcoloPartita[]) strategie.getValue();
		for (StrategiaCalcoloPartita strategiaCalcoloPartita : strategieArray) {
			mainPanel.add(getControlForStrategia(strategiaCalcoloPartita).getControl(),
					strategiaCalcoloPartita.getCodiceStrategia());
		}
		return mainPanel;
	}

	/**
	 * metodo Factory per restituire la classe che crea i controlli appropriati per la strategia.
	 * 
	 * @param strategiaCalcoloPartita
	 *            strategiaCalcoloPartita
	 * @return ControlFactory
	 */
	private ControlFactory getControlForStrategia(StrategiaCalcoloPartita strategiaCalcoloPartita) {
		ControlFactory abstractControlFactory = null;

		if (StrategiaCalcoloPartita.FORMULA.equals(strategiaCalcoloPartita.getCodiceStrategia())) {
			// Recupero la strategia interessata
			abstractControlFactory = new StrategiaCalcoloPartitaFormulaComponent(strategie, parentModel);
		}
		if (StrategiaCalcoloPartita.CONTI.equals(strategiaCalcoloPartita.getCodiceStrategia())) {
			abstractControlFactory = new StrategiaCalcoloPartitaContiComponent(strategie, parentModel);
		}

		if (abstractControlFactory != null) {
			return abstractControlFactory;
		}
		throw new RuntimeException("Non ho nessun controllo per la stategia di tipo "
				+ strategiaCalcoloPartita.getCodiceStrategia());
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (isControlCreated() && event.getNewValue() != null) {
			// nel nuovo valore ho l'oggetto strategia selezionato
			StrategiaCalcoloPartita strategiaCalcoloPartita = (StrategiaCalcoloPartita) event.getNewValue();
			cardLayout.show(mainPanel, strategiaCalcoloPartita.getCodiceStrategia());
		}
	}

}
