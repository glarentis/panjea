package it.eurotn.panjea.aton.rich.bd;

import it.eurotn.panjea.aton.exporter.service.interfaces.AtonExporterService;
import it.eurotn.panjea.aton.importer.service.interfaces.AtonImporterService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;

public class AtonBD extends AbstractBaseBD implements IAtonBD {
	private AtonExporterService exporterService;
	private AtonImporterService importerService;

	private static Logger logger = Logger.getLogger(AtonBD.class);

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
	public void importa() {
		logger.debug("--> Enter importa");
		start("importa");
		try {
			importerService.importa();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("importa");
		}
		logger.debug("--> Exit importa ");
	}

	/**
	 * @param exporterService
	 *            The exporterService to set.
	 */
	public void setExporterService(AtonExporterService exporterService) {
		this.exporterService = exporterService;
	}

	/**
	 * @param importerService
	 *            The atonImporterService to set.
	 */
	public void setImporterService(AtonImporterService importerService) {
		this.importerService = importerService;
	}

	@Override
	public int[] verificaOrdiniDaImportare() {
		logger.debug("--> Enter verificaOrdiniDaImportare");
		int[] numOrdini = new int[] { 0, 0 };
		try {
			numOrdini = importerService.verifica();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit verificaOrdiniDaImportare ");
		return numOrdini;
	}

}
