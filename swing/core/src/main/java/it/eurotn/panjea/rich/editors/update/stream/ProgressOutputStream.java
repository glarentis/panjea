package it.eurotn.panjea.rich.editors.update.stream;

import it.eurotn.panjea.rich.editors.update.PanjeaServer;
import it.eurotn.panjea.rich.editors.update.step.StepUpdate;

import java.beans.PropertyChangeSupport;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.output.CountingOutputStream;

public class ProgressOutputStream extends CountingOutputStream {

	private PropertyChangeSupport propertyChangeSupport;

	/**
	 *
	 * @param stream
	 *            stram
	 * @param propertyChangeSupport
	 *            property per spedire i byte trasmessi
	 */
	public ProgressOutputStream(final FileOutputStream stream, final PropertyChangeSupport propertyChangeSupport) {
		super(stream);
		this.propertyChangeSupport = propertyChangeSupport;
	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_STEP, StepUpdate.CHECKSUM_DOWNLOAD,
				StepUpdate.UPDATE_DOWNLOAD);
		propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_BYTE_TRASMETTI, 0, getCount());
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
		propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_STEP, StepUpdate.CHECKSUM_DOWNLOAD,
				StepUpdate.UPDATE_DOWNLOAD);
		propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_BYTE_TRASMETTI, 0, getCount());
	}

	@Override
	public void write(int b) throws IOException {
		super.write(b);
		propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_STEP, StepUpdate.CHECKSUM_DOWNLOAD,
				StepUpdate.UPDATE_DOWNLOAD);
		propertyChangeSupport.firePropertyChange(PanjeaServer.PROPERTY_BYTE_TRASMETTI, 0, getCount());
	}

}
