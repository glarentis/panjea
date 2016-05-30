package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.service.exception.DocumentiExporterException;
import it.eurotn.panjea.magazzino.service.interfaces.DocumentiExporterService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DocumentiExporterBD extends AbstractBaseBD implements IDocumentiExporterBD {

	private static Logger logger = Logger.getLogger(DocumentiExporterBD.class);

	public static final String BEAN_ID = "documentiExporterBD";

	private DocumentiExporterService documentiExporterService;

	@Override
	public List<byte[]> esportaDocumenti(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) throws DocumentiExporterException {
		logger.debug("--> Enter esportaDocumenti");
		start("esportaDocumenti");
		List<byte[]> flusso = null;
		try {
			flusso = documentiExporterService.esportaDocumenti(areeMagazzino, tipoEsportazione, parametri);
		} catch (DocumentiExporterException e) {
			throw e;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaDocumenti");
		}
		logger.debug("--> Exit esportaDocumenti ");
		return flusso;
	}

	/**
	 * @param documentiExporterService
	 *            the documentiExporterService to set
	 */
	public void setDocumentiExporterService(DocumentiExporterService documentiExporterService) {
		this.documentiExporterService = documentiExporterService;
	}

}
