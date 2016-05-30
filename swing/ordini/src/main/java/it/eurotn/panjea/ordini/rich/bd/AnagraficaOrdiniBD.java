package it.eurotn.panjea.ordini.rich.bd;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.ordini.service.interfaces.AnagraficaOrdiniService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class AnagraficaOrdiniBD extends AbstractBaseBD implements IAnagraficaOrdiniBD {

	public static final String BEAN_ID = "anagraficaOrdiniBD";

	private static Logger logger = Logger.getLogger(AnagraficaOrdiniBD.class);

	private AnagraficaOrdiniService anagraficaOrdiniService;

	@Override
	public void associaTipoAreaEvasione(Map<TipoAreaMagazzino, Set<EntitaLite>> map) {
		logger.debug("--> Enter associaTipoAreaEvasione");
		start("associaTipoAreaEvasione");
		try {
			anagraficaOrdiniService.associaTipoAreaEvasione(map);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("associaTipoAreaEvasione");
		}
		logger.debug("--> Exit associaTipoAreaEvasione ");
	}

	@Override
	public OrdiniSettings caricaOrdiniSettings() {
		logger.debug("--> Enter caricaOrdiniSettings");
		start("caricaOrdiniSettings");

		OrdiniSettings ordiniSettings = null;
		try {
			ordiniSettings = anagraficaOrdiniService.caricaOrdiniSettings();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaOrdiniSettings");
		}
		logger.debug("--> Exit caricaOrdiniSettings ");
		return ordiniSettings;
	}

	@Override
	public SedeOrdine caricaSedeOrdineBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali) {
		logger.debug("--> Enter caricaSedeOrdineBySedeEntita");
		start("caricaSedeOrdineBySedeEntita");
		SedeOrdine sedeOrdine = null;
		try {
			sedeOrdine = anagraficaOrdiniService.caricaSedeOrdineBySedeEntita(sedeEntita, ignoraEreditaDatiCommerciali);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSedeOrdineBySedeEntita");
		}
		logger.debug("--> Exit caricaSedeOrdineBySedeEntita ");
		return sedeOrdine;
	}

	@Override
	public OrdiniSettings salvaOrdiniSettings(OrdiniSettings ordiniSettings) {
		logger.debug("--> Enter salvaOrdiniSettings");
		start("salvaOrdiniSettings");

		OrdiniSettings ordiniSettingsSave = null;
		try {
			ordiniSettingsSave = anagraficaOrdiniService.salvaOrdiniSettings(ordiniSettings);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaOrdiniSettings");
		}
		logger.debug("--> Exit salvaOrdiniSettings ");
		return ordiniSettingsSave;
	}

	@Override
	public SedeOrdine salvaSedeOrdine(SedeOrdine sedeOrdine) {
		logger.debug("--> Enter salvaSedeOrdine");
		start("salvaSedeOrdine");
		SedeOrdine sedeOrdineSave = null;
		try {
			sedeOrdineSave = anagraficaOrdiniService.salvaSedeOrdine(sedeOrdine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaSedeOrdine");
		}
		logger.debug("--> Exit salvaSedeOrdine ");
		return sedeOrdineSave;
	}

	/**
	 * @param anagraficaOrdiniService
	 *            the anagraficaOrdiniService to set
	 */
	public void setAnagraficaOrdiniService(AnagraficaOrdiniService anagraficaOrdiniService) {
		this.anagraficaOrdiniService = anagraficaOrdiniService;
	}

}
