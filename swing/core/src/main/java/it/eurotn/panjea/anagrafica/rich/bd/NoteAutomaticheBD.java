/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.service.interfaces.NoteAutomaticheService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author fattazzo
 * 
 */
public class NoteAutomaticheBD extends AbstractBaseBD implements INoteAutomaticheBD {

	public static final String BEAN_ID = "noteAutomaticheBD";

	private static Logger logger = Logger.getLogger(NoteAutomaticheBD.class);

	private NoteAutomaticheService noteAutomaticheService;

	@Override
	public void cancellaNotaAutomatica(NotaAutomatica notaAutomatica) {
		logger.debug("--> Enter cancellaNotaAutomatica");
		start("cancellaNotaAutomatica");
		try {
			noteAutomaticheService.cancellaNotaAutomatica(notaAutomatica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaNotaAutomatica");
		}
		logger.debug("--> Exit cancellaNotaAutomatica ");
	}

	@Override
	public NotaAutomatica caricaNotaAutomatica(Integer idNotaAutomatica) {
		logger.debug("--> Enter caricaNotaAutomatica");
		start("caricaNotaAutomatica");

		NotaAutomatica notaAutomaticaCaricata = null;
		try {
			notaAutomaticaCaricata = noteAutomaticheService.caricaNotaAutomatica(idNotaAutomatica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaNotaAutomatica");
		}
		logger.debug("--> Exit caricaNotaAutomatica ");
		return notaAutomaticaCaricata;
	}

	@Override
	public List<NotaAutomatica> caricaNoteAutomatiche(Date data, Documento documento) {
		logger.debug("--> Enter caricaNoteAutomatiche");
		start("caricaNoteAutomatiche");
		List<NotaAutomatica> results = new ArrayList<NotaAutomatica>();
		try {
			results = noteAutomaticheService.caricaNoteAutomatiche(data, documento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaNoteAutomatiche");
		}
		logger.debug("--> Exit caricaNoteAutomatiche ");
		return results;
	}

	@Override
	public List<NotaAutomatica> caricaNoteAutomatiche(Integer idEntita, Integer idSedeEntita) {
		logger.debug("--> Enter caricaNoteAutomatiche");
		start("caricaNoteAutomatiche");
		List<NotaAutomatica> results = new ArrayList<NotaAutomatica>();
		try {
			results = noteAutomaticheService.caricaNoteAutomatiche(idEntita, idSedeEntita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaNoteAutomatiche");
		}
		logger.debug("--> Exit caricaNoteAutomatiche ");
		return results;
	}

	@Override
	public NotaAutomatica salvaNotaAutomatica(NotaAutomatica notaAutomatica) {
		logger.debug("--> Enter salvaNotaAutomatica");
		start("salvaNotaAutomatica");

		NotaAutomatica notaAutomaticaSalvata = null;
		try {
			notaAutomaticaSalvata = noteAutomaticheService.salvaNotaAutomatica(notaAutomatica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaNotaAutomatica");
		}
		logger.debug("--> Exit salvaNotaAutomatica ");
		return notaAutomaticaSalvata;
	}

	/**
	 * @param noteAutomaticheService
	 *            the noteAutomaticheService to set
	 */
	public void setNoteAutomaticheService(NoteAutomaticheService noteAutomaticheService) {
		this.noteAutomaticheService = noteAutomaticheService;
	}

}
