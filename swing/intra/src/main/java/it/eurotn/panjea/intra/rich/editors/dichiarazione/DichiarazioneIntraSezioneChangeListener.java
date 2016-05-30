package it.eurotn.panjea.intra.rich.editors.dichiarazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntraAcquisti;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JComponent;

public class DichiarazioneIntraSezioneChangeListener implements PropertyChangeListener {

	private List<JComponent> components = null;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		DichiarazioneIntra intra = (DichiarazioneIntra) evt.getNewValue();
		boolean isAcquisti = false;
		if (intra != null) {
			isAcquisti = intra.getClass().getName().equals(DichiarazioneIntraAcquisti.class.getName());
		}
		updateComponents(isAcquisti);
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents(List<JComponent> components) {
		this.components = components;
	}

	/**
	 * Visuzlizza/nasconde i controlli legati agli acquisti/vendite.
	 * 
	 * @param isAcquisti
	 *            il descriminante per visualizzare o nascondere i controlli
	 */
	private void updateComponents(boolean isAcquisti) {
		if (components != null) {
			for (JComponent comp : components) {
				comp.setVisible(isAcquisti);
			}
		}
	}

}
