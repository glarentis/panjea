/**
 * 
 */
package it.eurotn.panjea.spedizioni.rich.bd;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.spedizioni.service.interfaces.SpedizioniService;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Implementazione del Business Delegate di {@link AnagraficaService}.
 * 
 * @author adriano
 * @version 1.0, 18/dic/07
 * 
 */
public class SpedizioniBD extends AbstractBaseBD implements ISpedizioniBD {

	private static Logger logger = Logger.getLogger(SpedizioniBD.class);

	public static final String BEAN_ID = "spedizioniBD";

	/**
	 * 
	 * @return {@link SpedizioniBD}
	 */
	public static String getBeanId() {
		return ISpedizioniBD.BEAN_ID;
	}

	private AnagraficaService anagraficaService;
	private SpedizioniService spedizioniService;

	/**
	 * Costruttore.
	 */

	public SpedizioniBD() {
		super();
	}

	@Override
	public byte[] generaEtichette(AreaMagazzino areaMagazzino, ParametriCreazioneEtichette parametriCreazioneEtichette) {
		logger.debug("--> Enter generaEtichette");
		start("generaEtichette");

		byte[] fileEtichetta = null;
		try {
			fileEtichetta = spedizioniService.generaEtichette(areaMagazzino, parametriCreazioneEtichette);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaEtichette");
		}
		logger.debug("--> Exit generaEtichette ");
		return fileEtichetta;
	}

	@Override
	public byte[] generaRendicontazione(List<AreaMagazzinoRicerca> areeMagazzinoRicerca, Vettore vettore) {
		logger.debug("--> Enter generaRendicontazione");
		start("generaRendicontazione");
		byte[] fileRendicontazione = null;
		try {
			fileRendicontazione = spedizioniService.generaRendicontazione(areeMagazzinoRicerca, vettore);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaRendicontazione");
		}
		logger.debug("--> Exit generaRendicontazione ");
		return fileRendicontazione;
	}

	@Override
	public void leggiRisultatiEtichette(AreaMagazzino areaMagazzino, byte[] data) {
		logger.debug("--> Enter leggiRisultatiEtichette");
		start("leggiRisultatiEtichette");
		try {
			spedizioniService.leggiRisultatiEtichette(areaMagazzino, data);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("leggiRisultatiEtichette");
		}
		logger.debug("--> Exit leggiRisultatiEtichette ");
	}

	@Override
	public void rendicontaAreeMagazzino(List<AreaMagazzinoRicerca> areeMagazzino) {
		logger.debug("--> Enter rendicontaAreeMagazzino");
		start("rendicontaAreeMagazzino");
		try {
			spedizioniService.rendicontaAreeMagazzino(areeMagazzino);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("rendicontaAreeMagazzino");
		}
		logger.debug("--> Exit rendicontaAreeMagazzino ");
	}

	@Override
	public Entita salvaEntita(Entita entita) throws AnagraficheDuplicateException {
		logger.debug("--> Enter salvaEntita");

		start("salvaEntita");
		Entita entitaSave = null;
		try {
			entitaSave = anagraficaService.salvaEntita(entita);
		} catch (AnagraficheDuplicateException e) {
			throw e;

		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaEntita");
		}
		logger.debug("--> Exit salvaEntita ");
		return entitaSave;
	}

	/**
	 * @param anagraficaService
	 *            The anagraficaService to set.
	 */
	public void setAnagraficaService(AnagraficaService anagraficaService) {
		this.anagraficaService = anagraficaService;
	}

	/**
	 * @param spedizioniService
	 *            the spedizioniService to set
	 */
	public void setSpedizioniService(SpedizioniService spedizioniService) {
		this.spedizioniService = spedizioniService;
	}
}
