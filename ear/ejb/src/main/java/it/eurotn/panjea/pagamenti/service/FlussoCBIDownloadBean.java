package it.eurotn.panjea.pagamenti.service;

import it.eurotn.panjea.pagamenti.service.interfaces.FlussoCBIDownload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.FlussoCBIDownload")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.FlussoCBIDownload")
public class FlussoCBIDownloadBean implements FlussoCBIDownload {

	private static Logger logger = Logger.getLogger(FlussoCBIDownloadBean.class);

	@Override
	public byte[] getData(String filePath) {

		InputStream is = null;
		ByteArrayOutputStream bos = null;
		byte[] result = null;
		try {
			// apro il file
			logger.debug("--> pathFileRiba" + filePath);
			File fileTmp = new File(filePath);
			URL url = fileTmp.toURI().toURL();
			is = url.openStream();
			logger.info("Open remote stream: successful.");
			// leggo il file

			bos = new ByteArrayOutputStream();
			int r;
			while ((r = is.read()) != -1) {
				bos.write(r);
			}
			result = bos.toByteArray();

			if (bos != null) {
				bos.close();
			}
			if (is != null) {
				is.close();
			}
			fileTmp.delete();
			logger.info("Closing remote stream: successful");
		} catch (Exception ioe) {
			logger.info("Failure during close()");
			throw new RuntimeException("Errore durante la chiusura del file.");
		}
		return result;
	}
}
