package it.eurotn.panjea.fatturepa.xml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.w3c.dom.ls.LSInput;

public class InputResolver implements LSInput {

	public static transient Logger logger;

	static {
		logger = Logger.getLogger(InputResolver.class);
	}

	private String publicId;

	private String systemId;

	private BufferedInputStream inputStream;

	public InputResolver(String publicId, String sysId, InputStream input) {
		this.publicId = publicId;
		this.systemId = sysId;
		this.inputStream = new BufferedInputStream(input);
	}

	public String getBaseURI() {
		return null;
	}

	public InputStream getByteStream() {
		return null;
	}

	public boolean getCertifiedText() {
		return false;
	}

	public Reader getCharacterStream() {
		return null;
	}

	public String getEncoding() {
		return null;
	}

	public BufferedInputStream getInputStream() {
		return inputStream;
	}

	public String getPublicId() {
		return publicId;
	}

	public String getStringData() {
		synchronized (inputStream) {
			try {
				byte[] input = new byte[inputStream.available()];
				inputStream.read(input);
				String contents = new String(input);
				return contents;
			} catch (IOException e) {
				logger.error("Exception caught while retrieving data.", e);
				return null;
			}
		}
	}

	public String getSystemId() {
		return systemId;
	}

	public void setBaseURI(String baseURI) {
	}

	public void setByteStream(InputStream byteStream) {
	}

	public void setCertifiedText(boolean certifiedText) {
	}

	public void setCharacterStream(Reader characterStream) {
	}

	public void setEncoding(String encoding) {
	}

	public void setInputStream(BufferedInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public void setStringData(String stringData) {
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
