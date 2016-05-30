/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.domain;

import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.importer.util.RigaImport;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.BeanReaderIOException;
import org.beanio.StreamFactory;
import org.beanio.UnidentifiedRecordException;

/**
 * @author fattazzo
 * 
 */
@Entity
@DiscriminatorValue("HIER")
public class HierarchicalDocumentImporter extends AbstractBeanIOImporter {

	private static Logger logger = Logger.getLogger(HierarchicalDocumentImporter.class);

	private static final long serialVersionUID = 6829461727655261172L;

	@Override
	public Collection<DocumentoImport> caricaDocumenti() throws ImportException {
		Collection<DocumentoImport> documentiImport = new ArrayList<DocumentoImport>();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getByteArray());
		InputStreamReader reader = new InputStreamReader(byteArrayInputStream);
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getXmlTemplatePath());

			BeanReader in = factory.createReader(STREAM_NAME, reader);
			// in.setErrorHandler(new FileReaderErrorHandler());

			Object objectImport;
			DocumentoImport documento = null;
			while ((objectImport = in.read()) != null) {
				if (objectImport instanceof DocumentoImport) {
					documento = (DocumentoImport) objectImport;
					documento.setTipoAreaMagazzino(getTipoAreaMagazzino());
					documentiImport.add(documento);
				} else if (objectImport instanceof RigaImport) {
					documento.getRighe().add((RigaImport) objectImport);
				}
			}
			in.close();
		} catch (BeanReaderIOException e) {
			throw new ImportException(e, getCodice());
		} catch (UnidentifiedRecordException e2) {
			// non faccio niente. Eccezione che potrei eliminare con la versione 2.0.4 in poi con il tag
			// ignoreUnidentifiedRecords="true" sullo stream ma non posso aggiornare beanio per le altre
			// importazioni/esportazioni esistenti
			if (logger.isDebugEnabled()) {
				logger.debug("--> Carattere di inizio record non gestito nel file");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return documentiImport;
	}

}
