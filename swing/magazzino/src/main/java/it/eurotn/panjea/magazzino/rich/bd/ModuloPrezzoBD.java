package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.service.interfaces.ModuloPrezzoService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;

public class ModuloPrezzoBD extends AbstractBaseBD implements IModuloPrezzoBD {

	private static Logger logger = Logger.getLogger(ModuloPrezzoBD.class);

	private ModuloPrezzoService moduloPrezzoService;

	/**
	 * Costruttore.
	 * 
	 */
	public ModuloPrezzoBD() {
		super();
	}

	@Override
	public PoliticaPrezzo calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
		logger.debug("--> Enter calcola");
		start("calcola");
		PoliticaPrezzo politicaPrezzo = null;
		try {
			politicaPrezzo = moduloPrezzoService.calcola(parametriCalcoloPrezzi);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("calcola");
		}
		logger.debug("--> Exit calcola ");
		return politicaPrezzo;
	}

	/**
	 * @param moduloPrezzoService
	 *            the moduloPrezzoService to set
	 */
	public void setModuloPrezzoService(ModuloPrezzoService moduloPrezzoService) {
		this.moduloPrezzoService = moduloPrezzoService;
	}

}
