package it.eurotn.panjea.ordini.rich.editors.righeinserimento.parametri;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;

public abstract class AbstractInserimentoComponent extends JPanel {

	private static final long serialVersionUID = -6257931418944517390L;

	public static final String PARAMETRI_CREATED_PROPERTY = "parametriCreatedProperty";

	private List<PropertyChangeListener> parametriCreatedListeners;

	{
		parametriCreatedListeners = new ArrayList<PropertyChangeListener>();
	}

	/**
	 * Costruttore.
	 */
	public AbstractInserimentoComponent() {
		super();
		setBorder(BorderFactory.createTitledBorder(getTitle()));
	}

	/**
	 * @param listener
	 *            listener to add
	 */
	public void addParametriCreatedListener(PropertyChangeListener listener) {
		parametriCreatedListeners.add(listener);
	}

	protected void fireParametriCreated(ParametriRigheOrdineInserimento parametri) {
		for (PropertyChangeListener propertyChangeListener : parametriCreatedListeners) {
			PropertyChangeEvent event = new PropertyChangeEvent(this, PARAMETRI_CREATED_PROPERTY, null, parametri);
			propertyChangeListener.propertyChange(event);
		}
	}

	/**
	 * @return titolo
	 */
	public abstract String getTitle();

	/**
	 * Aggiorna i controlli del componente.
	 *
	 * @param areaOrdine
	 *            area ordine di riferimento
	 */
	public abstract void updateControl(AreaOrdine areaOrdine);

}
