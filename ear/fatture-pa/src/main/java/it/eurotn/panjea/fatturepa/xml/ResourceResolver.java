package it.eurotn.panjea.fatturepa.xml;

import java.io.InputStream;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ResourceResolver implements LSResourceResolver {

	/**
	 * @param type
	 *            type
	 * @param namespaceURI
	 *            namespaceURI
	 * @param publicId
	 *            publicId
	 * @param systemId
	 *            systemId
	 * @param baseURI
	 *            baseURI
	 * @return resource
	 */
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {

		InputStream resourceAsStream = this.getClass().getClassLoader()
				.getResourceAsStream("it/gov/fatturapa/sdi/fatturapa/v1_1/xsd/fatturapa_v1.1.xsd");
		return new InputResolver(publicId, systemId, resourceAsStream);
	}

}
