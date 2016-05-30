package it.eurotn.panjea.rich.editors.update.stream;

import it.eurotn.panjea.rich.editors.update.PanjeaServer;
import it.eurotn.panjea.rich.editors.update.step.StepUpdate;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.input.CountingInputStream;

public class ProgressInputStream extends CountingInputStream {

	private PropertyChangeSupport propertyChangeSupport;
	private StepUpdate stepDownload;
	private boolean changeState;

	/**
	 *
	 * @param url
	 *            url del file da scaricare
	 * @param propertyChangeSupport
	 *            property per spedire i byte trasmessi
	 * @param stepDownload
	 *            step da rilanciare quando comincio a scaricare
	 * @throws IOException
	 *             ecc. generica.
	 */
	public ProgressInputStream(final URL url, final PropertyChangeSupport propertyChangeSupport,
			final StepUpdate stepDownload) throws IOException {
		super(url.openStream());
		this.propertyChangeSupport = propertyChangeSupport;
		this.stepDownload = stepDownload;
		this.changeState = true;
	}

	@Override
	protected synchronized void afterRead(int n) {
		super.afterRead(n);
		if (changeState) {
			propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_STEP, stepDownload, stepDownload);
		}
		propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_BYTE_TRASMETTI, 0, getCount());
	}

}
