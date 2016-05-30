package it.eurotn.panjea.onroad.rich.bd;

import it.eurotn.panjea.onroad.domain.wrapper.ClientiOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.DocumentiOnRoad;
import it.eurotn.panjea.onroad.exporter.service.interfaces.OnRoadExporterService;
import it.eurotn.panjea.onroad.importer.service.interfaces.OnRoadImporterService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;

public class OnRoadBD extends AbstractBaseBD implements IOnRoadBD {

	private OnRoadExporterService exporterService;
	private OnRoadImporterService importerService;

	private static Logger logger = Logger.getLogger(OnRoadBD.class);

	@Override
	public void esporta() {
		logger.debug("--> Enter esporta");
		start("esporta");
		try {
			exporterService.esporta();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esporta");
		}
		logger.debug("--> Exit esporta ");
	}

	@Override
	public void esportaArticoli() {
		logger.debug("--> Enter esportaArticoli");
		start("esportaArticoli");
		try {
			exporterService.esportaArticoli();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaArticoli");
		}
		logger.debug("--> Exit esportaArticoli ");
	}

	@Override
	public void esportaAssortimentoArticoli() {
		logger.debug("--> Enter esportaAssortimentoArticoli");
		start("esportaAssortimentoArticoli");
		try {
			exporterService.esportaAssortimentoArticoli();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaAssortimentoArticoli");
		}
		logger.debug("--> Exit esportaAssortimentoArticoli");
	}

	@Override
	public void esportaAttributiArticoli() {
		logger.debug("--> Enter esportaAttributiArticoli");
		start("esportaAttributiArticoli");
		try {
			exporterService.esportaAttributiArticoli();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaAttributiArticoli");
		}
		logger.debug("--> Exit esportaAttributiArticoli ");
	}

	@Override
	public void esportaClienti() {
		logger.debug("--> Enter esportaClienti");
		start("esportaClienti");
		try {
			exporterService.esportaClienti();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaClienti");
		}
		logger.debug("--> Exit esportaClienti ");
	}

	@Override
	public void esportaClientiCessionari() {
		logger.debug("--> Enter esportaClientiCessionari");
		start("esportaClientiCessionari");
		try {
			exporterService.esportaClientiCessionari();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaClientiCessionari");
		}
		logger.debug("--> Exit esportaClientiCessionari ");
	}

	@Override
	public void esportaCodiciIva() {
		logger.debug("--> Enter esportaCodiciIva");
		start("esportaCodiciIva");
		try {
			exporterService.esportaCodiciIva();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaCodiciIva");
		}
		logger.debug("--> Exit esportaCodiciIva ");
	}

	@Override
	public void esportaCodiciPagamento() {
		logger.debug("--> Enter esportaCodiciPagamento");
		start("esportaCodiciPagamento");
		try {
			exporterService.esportaCodiciPagamento();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaCodiciPagamento");
		}
		logger.debug("--> Exit esportaCodiciPagamento");
	}

	@Override
	public void esportaCondiz() {
		logger.debug("--> Enter esportaCondiz");
		start("esporta");
		try {
			exporterService.esportaCondiz();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esporta");
		}
		logger.debug("--> Exit esportaCondiz");
	}

	@Override
	public void esportaGiacenze() {
		logger.debug("--> Enter esportaGiacenze");
		start("esportaGiacenze");
		try {
			exporterService.esportaGiacenze();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaGiacenze");
		}
		logger.debug("--> Exit esportaGiacenze ");
	}

	@Override
	public void esportaRate() {
		logger.debug("--> Enter esportaRate");
		start("esportaRate");
		try {
			exporterService.esportaRate();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaRate");
		}
		logger.debug("--> Exit esportaRate ");
	}

	@Override
	public void esportaTabelle() {
		logger.debug("--> Enter esportaTabelle");
		start("esportaTabelle");
		try {
			exporterService.esportaTabelle();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esportaTabelle");
		}
		logger.debug("--> Exit esportaTabelle ");
	}

	@Override
	public void esportaUm() {
		logger.debug("--> Enter esportaUm");
		start("esporta");
		try {
			exporterService.esportaUm();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esporta");
		}
		logger.debug("--> Exit esportaUm");
	}

	@Override
	public void esportaUtenti() {
		logger.debug("--> Enter esportaUtenti");
		start("esporta");
		try {
			exporterService.esportaUtenti();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("esporta");
		}
		logger.debug("--> Exit esportaUtenti");
	}

	@Override
	public ClientiOnRoad importaClienti() {
		logger.debug("--> Enter importaClienti");
		start("importaClienti");
		ClientiOnRoad clientiOnRoad = null;
		try {
			clientiOnRoad = importerService.importaClienti();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("importaClienti");
		}
		logger.debug("--> Exit importaClienti");
		return clientiOnRoad;
	}

	@Override
	public DocumentiOnRoad importaDocumenti() {
		logger.debug("--> Enter importaDocumenti");
		start("importaDocumenti");
		DocumentiOnRoad documentiOnRoad = null;
		try {
			documentiOnRoad = importerService.importaDocumenti();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("importaDocumentiDocumenti");
		}
		logger.debug("--> Exit importaDocumenti");
		return documentiOnRoad;
	}

	/**
	 * @param exporterService
	 *            The exporterService to set.
	 */
	public void setExporterService(OnRoadExporterService exporterService) {
		this.exporterService = exporterService;
	}

	/**
	 * @param importerService
	 *            The atonImporterService to set.
	 */
	public void setImporterService(OnRoadImporterService importerService) {
		this.importerService = importerService;
	}

}
