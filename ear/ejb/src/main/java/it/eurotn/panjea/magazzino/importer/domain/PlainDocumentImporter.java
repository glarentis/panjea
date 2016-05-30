/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.domain;

import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.importer.util.PlainDocumentImport;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.beanio.BeanReader;
import org.beanio.BeanReaderIOException;
import org.beanio.StreamFactory;

/**
 * @author fattazzo
 * 
 */
@Entity
@DiscriminatorValue("FLAT")
public class PlainDocumentImporter extends AbstractBeanIOImporter {

	private static final long serialVersionUID = 8729550406768422672L;

	@Override
	public Collection<DocumentoImport> caricaDocumenti() throws ImportException {
		Collection<PlainDocumentImport> plainDocsCaricati = new ArrayList<PlainDocumentImport>();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getByteArray());
		InputStreamReader reader = new InputStreamReader(byteArrayInputStream);
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getXmlTemplatePath());

			BeanReader in = factory.createReader(STREAM_NAME, reader);
			// in.setErrorHandler(new FileReaderErrorHandler());

			PlainDocumentImport documento;
			while ((documento = (PlainDocumentImport) in.read()) != null) {
				plainDocsCaricati.add(documento);
			}
			in.close();
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, getCodice());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		Collection<DocumentoImport> documentiImport = new ArrayList<DocumentoImport>();
		for (PlainDocumentImport plainDocument : plainDocsCaricati) {
			documentiImport.add(plainDocument.getDocumentoImport());
		}

		return documentiImport;
	}
}
