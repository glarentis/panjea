/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoSchedeArticoloService;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author fattazzo
 * 
 */
public class MagazzinoSchedeArticoloBD extends AbstractBaseBD implements IMagazzinoSchedeArticoloBD {

	public static final String BEAN_ID = "magazzinoSchedeArticoloBD";

	private static Logger logger = Logger.getLogger(MagazzinoSchedeArticoloBD.class);

	private MagazzinoSchedeArticoloService magazzinoSchedeArticoloService;

	@Override
	public List<ArticoloRicerca> caricaArticoliNonValidi(Integer anno, Integer mese) {
		logger.debug("--> Enter caricaArticoliNonValidi");
		start("caricaArticoliNonValidi");
		List<ArticoloRicerca> articoli = new ArrayList<ArticoloRicerca>();
		try {
			articoli = magazzinoSchedeArticoloService.caricaArticoliNonValidi(anno, mese);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaArticoliNonValidi");
		}
		logger.debug("--> Exit caricaArticoliNonValidi ");
		return articoli;
	}

	@Override
	public List<ArticoloRicerca> caricaArticoliRimanenti(Integer anno, Integer mese) {
		logger.debug("--> Enter caricaArticoliRimanenti");
		start("caricaArticoliRimanenti");
		List<ArticoloRicerca> articoli = new ArrayList<ArticoloRicerca>();
		try {
			articoli = magazzinoSchedeArticoloService.caricaArticoliRimanenti(anno, mese);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaArticoliRimanenti");
		}
		logger.debug("--> Exit caricaArticoliRimanenti ");
		return articoli;
	}

	@Override
	public List<ArticoloRicerca> caricaArticoliStampati(Integer anno, Integer mese) {
		logger.debug("--> Enter caricaArticoliStampati");
		start("caricaArticoliStampati");
		List<ArticoloRicerca> articoli = new ArrayList<ArticoloRicerca>();
		try {
			articoli = magazzinoSchedeArticoloService.caricaArticoliStampati(anno, mese);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaArticoliStampati");
		}
		logger.debug("--> Exit caricaArticoliStampati ");
		return articoli;
	}

	@Override
	public List<ElaborazioneSchedaArticoloDTO> caricaElaborazioniSchedeArticolo(ParametriRicercaElaborazioni parametri) {
		logger.debug("--> Enter caricaElaborazioniSchedeArticolo");
		start("caricaElaborazioniSchedeArticolo");
		List<ElaborazioneSchedaArticoloDTO> elaborazioni = new ArrayList<ElaborazioneSchedaArticoloDTO>();
		try {
			elaborazioni = magazzinoSchedeArticoloService.caricaElaborazioniSchedeArticolo(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaElaborazioniSchedeArticolo");
		}
		logger.debug("--> Exit caricaElaborazioniSchedeArticolo ");
		return elaborazioni;
	}

	@Override
	public byte[] caricaFileSchedaArticolo(Integer anno, Integer mese, Integer idArticolo) throws FileNotFoundException {
		logger.debug("--> Enter caricaFileSchedaArticolo");
		byte[] result = null;
		start("caricaFileSchedaArticolo");
		try {
			result = magazzinoSchedeArticoloService.caricaFileSchedaArticolo(anno, mese, idArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaFileSchedaArticolo");
		}
		logger.debug("--> Exit caricaFileSchedaArticolo ");
		return result;
	}

	@Override
	public int caricaNumeroSchedeArticoloInCodaDiElaborazione() {
		logger.debug("--> Enter caricaNumeroSchedeArticoloInCodaDiElaborazione");
		start("caricaNumeroSchedeArticoloInCodaDiElaborazione");
		int numeroSchede = 0;
		try {
			numeroSchede = magazzinoSchedeArticoloService.caricaNumeroSchedeArticoloInCodaDiElaborazione();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaNumeroSchedeArticoloInCodaDiElaborazione");
		}
		logger.debug("--> Exit caricaNumeroSchedeArticoloInCodaDiElaborazione ");
		return numeroSchede;
	}

	@Override
	public List<SituazioneSchedaArticoloDTO> caricaSituazioneSchedeArticolo(Integer anno) {
		logger.debug("--> Enter caricaSituazioneSchedeArticolo");
		start("caricaSituazioneSchedeArticolo");

		List<SituazioneSchedaArticoloDTO> situazione = null;
		try {
			situazione = magazzinoSchedeArticoloService.caricaSituazioneSchedeArticolo(anno);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSituazioneSchedeArticolo");
		}
		logger.debug("--> Exit caricaSituazioneSchedeArticolo ");
		return situazione;
	}

	@Override
	public void creaSchedeArticolo(ParametriCreazioneSchedeArticoli parametriCreazioneSchedeArticoli) {
		logger.debug("--> Enter creaSchedeArticolo");
		start("creaSchedeArticolo");
		try {
			magazzinoSchedeArticoloService.creaSchedeArticolo(parametriCreazioneSchedeArticoli);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaSchedeArticolo");
		}
		logger.debug("--> Exit creaSchedeArticolo ");
	}

	@Override
	public void modificaDescrizioneElaborazione(String descrizioneOld, String descrizioneNew) {
		logger.debug("--> Enter modificaDescrizioneElaborazione");
		start("modificaDescrizioneElaborazione");
		try {
			magazzinoSchedeArticoloService.modificaDescrizioneElaborazione(descrizioneOld, descrizioneNew);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("modificaDescrizioneElaborazione");
		}
		logger.debug("--> Exit modificaDescrizioneElaborazione ");
	}

	/**
	 * @param magazzinoSchedeArticoloService
	 *            the magazzinoSchedeArticoloService to set
	 */
	public void setMagazzinoSchedeArticoloService(MagazzinoSchedeArticoloService magazzinoSchedeArticoloService) {
		this.magazzinoSchedeArticoloService = magazzinoSchedeArticoloService;
	}

}
