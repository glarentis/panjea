/**
 *
 */
package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ENoteGiornale;
import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.contabilita.domain.NotaGiornale;
import it.eurotn.panjea.contabilita.manager.interfaces.LibroGiornaleManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Manager che gestisce le operazioni del libro giornale roles: visualizzaLibriGiornale, amministraGiornale.
 *
 * @author fattazzo
 * @version 1.0, 25/set/07
 */
@Stateless(name = "Panjea.LibroGiornaleManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LibroGiornaleManager")
public class LibroGiornaleManagerBean implements LibroGiornaleManager {

	private static final int NUMERO_MESI = 12;
	private static Logger logger = Logger.getLogger(LibroGiornaleManagerBean.class);
	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AnagraficaService anagraficaService;

	@EJB
	private PanjeaMessage panjeaMessage;

	/**
	 * Aggiunge alla lista dei giornali già esistenti, tutti i nuovi giornali per l'anno di competenza.
	 *
	 * @param listGiornaliPresenti
	 *            lista dei giornali già creati.
	 * @param annoCompetenza
	 *            anno di competenza dei giornali
	 * @param aziendaLite
	 *            aziendaLite
	 * @return lista dei giornali mancanti. I giornali sono in stato invalidato
	 */
	private List<Giornale> aggiungiGiornaliMancanti(List<Giornale> listGiornaliPresenti, int annoCompetenza,
			AziendaLite aziendaLite) {
		logger.debug("--> Enter caricaGiornaliMancanti");

		Integer annoGiornale = annoCompetenza;

		Integer meseGiornale;
		if (listGiornaliPresenti.size() > 0) {
			meseGiornale = listGiornaliPresenti.get(listGiornaliPresenti.size() - 1).getMese();
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(aziendaLite.getDataInizioEsercizio());
			meseGiornale = calendar.get(Calendar.MONTH);
		}

		if (meseGiornale == NUMERO_MESI) {
			meseGiornale = 1;
		} else {
			meseGiornale++;
		}
		List<Giornale> listNuoviGiornali = new ArrayList<Giornale>();
		for (int i = 1; i <= NUMERO_MESI - listGiornaliPresenti.size(); i++) {

			Giornale nuovoGiornale = new Giornale();
			nuovoGiornale.setAnno(annoGiornale);
			nuovoGiornale.setAnnoCompetenza(annoCompetenza);
			nuovoGiornale.setCodiceAzienda(getAzienda());
			nuovoGiornale.setMese(meseGiornale);
			nuovoGiornale.setSaldoAvere(null);
			nuovoGiornale.setSaldoDare(null);
			nuovoGiornale.setValido(false);
			nuovoGiornale.setStampabile(false);

			listNuoviGiornali.add(nuovoGiornale);

			if (meseGiornale.intValue() == NUMERO_MESI) {
				meseGiornale = new Integer(0);
				annoGiornale++;
			}

			meseGiornale++;
		}

		listGiornaliPresenti.addAll(listNuoviGiornali);
		logger.debug("--> Exit caricaGiornaliMancanti");
		return listGiornaliPresenti;
	}

	@Override
	public void cancellaNoteGiornale(Integer idGiornale) {
		logger.debug("--> Enter cancellaNoteGiornale");

		Query query = panjeaDAO.prepareNamedQuery("NotaGiornale.cancellaNoteByGiornale");
		query.setParameter("paramIdGiornale", idGiornale);

		query.executeUpdate();

		logger.debug("--> Exit cancellaNoteGiornale");
	}

	@Override
	public Giornale caricaGiornale(Integer anno, Integer mese) throws ContabilitaException {
		logger.debug("--> Enter caricaGiornale");

		Query query = panjeaDAO.prepareNamedQuery("Giornale.caricaGiornaleByMeseEAnno");
		query.setParameter("paramAnno", anno);
		query.setParameter("paramMese", mese);
		query.setParameter("paramCodiceAzienda", getAzienda());

		Giornale giornale = null;
		try {
			giornale = (Giornale) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> Nessun giornale trovato con anno = " + anno + " e mese = " + mese);
			return null;
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del giornale.", e);
			throw new ContabilitaException("Errore durante il caricamento del giornale.", e);
		}

		logger.debug("--> Exit caricaGiornale");

		return giornale;
	}

	@Override
	public Giornale caricaGiornalePrecedente(Giornale giornaleAttuale) {
		logger.debug("--> Enter caricaGiornalePrecedente");

		Integer anno = giornaleAttuale.getAnnoCompetenza();
		Integer mese = giornaleAttuale.getMese();
		AziendaLite aziendaLite;
		try {
			aziendaLite = anagraficaService.caricaAzienda();
		} catch (AnagraficaServiceException e1) {
			logger.error("--> errore nel caricare l'aziendaLite", e1);
			throw new RuntimeException(e1);
		}

		// Controllo se il mese e' quello di inizio esercizio
		// Mese inizio esercizio parte da 0, mentre il giornale parte da 1
		if (mese == aziendaLite.getMeseInizioEsercizio() - 1) {
			return null;
		}
		// se sono in un mese diverso da gennaio posso prendere il
		// mese precedente altrimenti torno null
		if (mese > 1) {
			mese--;
		} else {
			anno--;
			mese = 12;
		}

		Query query = panjeaDAO.prepareNamedQuery("Giornale.caricaGiornaleByMeseEAnnoCompetenza");
		query.setParameter("paramAnnoCompetenza", anno);
		query.setParameter("paramCodiceAzienda", giornaleAttuale.getCodiceAzienda());
		query.setParameter("paramMese", mese);

		Giornale giornalePrecedente = null;
		try {
			giornalePrecedente = (Giornale) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.warn("--> Non ho trovato il giornale precedente per l'anno " + anno + " e mese " + mese);
		}

		logger.debug("--> Exit caricaGiornalePrecedente");
		return giornalePrecedente;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Giornale> caricaGiornali(int annoCompetenza) throws ContabilitaException {
		logger.debug("--> Enter caricaGiornali");

		AziendaLite aziendaLite = null;
		try {
			aziendaLite = anagraficaService.caricaAzienda();
		} catch (AnagraficaServiceException e) {
			logger.error("--> Errore durante il caricamento dell'azienda corrente.", e);
			throw new ContabilitaException("Errore durante il caricamento dell'azienda corrente.", e);
		}

		Query query = panjeaDAO.prepareNamedQuery("Giornale.caricaGiornali");
		query.setParameter("paramAnnoCompetenza", annoCompetenza);
		query.setParameter("paramCodiceAzienda", aziendaLite.getCodice());

		List<Giornale> listGiornali = new ArrayList<Giornale>();

		try {
			listGiornali = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei libri giornale.", e);
			throw new ContabilitaException("Errore durante il caricamento dei libri giornale.", e);
		}
		listGiornali = aggiungiGiornaliMancanti(listGiornali, annoCompetenza, aziendaLite);
		listGiornali = impostaGiornaliStampabili(listGiornali, aziendaLite.getDataInizioAttivita(),
				aziendaLite.getDataInizioEsercizio());
		Collections.sort(listGiornali, new Comparator<Giornale>() {

			@Override
			public int compare(Giornale o1, Giornale o2) {
				return o1.getMese().compareTo(o2.getMese());
			}
		});
		logger.debug("--> Exit caricaGiornali");
		return listGiornali;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Giornale> caricaGiornaliSuccessivi(Giornale giornaleAttuale) throws ContabilitaException {
		logger.debug("--> Enter caricaGiornaliSuccessivi");

		Query query = panjeaDAO.prepareNamedQuery("Giornale.caricaGiornaliSuccessivi");
		query.setParameter("paramAnno", giornaleAttuale.getAnno());
		query.setParameter("paramMese", giornaleAttuale.getMese());
		query.setParameter("paramCodiceAzienda", getAzienda());

		List<Giornale> listGiornali = null;
		try {
			listGiornali = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei giornali successivi.", e);
			throw new ContabilitaException("Errore durante il caricamento dei giornali successivi.", e);
		}

		logger.debug("--> Exit caricaGiornaliSuccessivi");
		return listGiornali;
	}

	/**
	 *
	 * @return codice dell'azienda per l'utente loggato
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	/**
	 * Cambia, se ci sono le condizioni, lo stato stampabile dei giornali.<br/>
	 * Le condizioni per essere stampabile sono:<br/>
	 * <ul>
	 * <li>giornale valido</li>
	 * <li>giornale precedente valido</li>
	 * <li>nessun giornale e mese/anno uguale a mese/anno di dataInizioAttivita dell'azienda</li>
	 * <li>il primo tra i giornali non validi</li>
	 * </ul>
	 *
	 * @param giornali
	 *            i giornali a cui cambiare lo stato stampabile
	 * @param dataInizioAttivita
	 *            la data inizio attivita' dell'azienda
	 * @param dataInizioEsercizio
	 *            la data di inizio esercizio dell'azienda
	 * @return List<Giornale>
	 */
	private List<Giornale> impostaGiornaliStampabili(List<Giornale> giornali, Date dataInizioAttivita,
			Date dataInizioEsercizio) {

		Giornale giornalePrec = null;
		for (Giornale giornale : giornali) {

			boolean stampabile = false;

			// se il giornale è del mese della data inizio esercizio o nel mese e anno della data inizio attività è
			// sempre stampabile
			Calendar inizioEsercizioCal = Calendar.getInstance();
			inizioEsercizioCal.setTime(dataInizioEsercizio);
			stampabile = giornale.getMese().equals(inizioEsercizioCal.get(Calendar.MONTH) + 1);

			Calendar inizioAttivitaCal = Calendar.getInstance();
			inizioAttivitaCal.setTime(dataInizioAttivita);
			stampabile = stampabile
					|| (giornale.getMese().equals(inizioAttivitaCal.get(Calendar.MONTH) + 1) && giornale.getAnno()
							.equals(inizioAttivitaCal.get(Calendar.YEAR)));

			// non risulta stampabile in base alle date inizio attività ed esercizio controllo in base alla validità del
			// giornale.
			if (!stampabile) {
				// se è valido il giornale può essere stampabile
				if (giornale.getValido().booleanValue()) {
					stampabile = true;
				} else {
					// se non è valido controllo lo stato del giornale precedente
					if (giornalePrec == null) {
						giornalePrec = caricaGiornalePrecedente(giornale);
					}
					stampabile = giornalePrec != null && giornalePrec.getValido() && giornalePrec.isStampabile();
				}
			}
			giornalePrec = giornale;

			giornale.setStampabile(stampabile);
		}

		return giornali;
	}

	@Override
	public void invalidaLibroGiornale(AreaContabile areaContabile) throws ContabilitaException {
		invalidaLibroGiornale(areaContabile, null);
	}

	@Override
	public void invalidaLibroGiornale(AreaContabile areaContabile, Date dataRegistrazionePrecedente)
			throws ContabilitaException {
		logger.debug("--> Enter invalidaLibroGiornale");

		Calendar calendar = Calendar.getInstance();
		// NPE mail data registrazione null
		if (areaContabile.getDataRegistrazione() == null) {
			// dallo stack trace questa nullpointer viene sollevata quando sto cancellando una area contabile
			// se non ho una data non invalido nulla
			return;
		}
		calendar.setTime(areaContabile.getDataRegistrazione());

		// aggiungo 1 al mese perche' calendar.get(Calendar.MONTH) mi restituisce il numero del mese -1
		Giornale giornale = caricaGiornale(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		Giornale nuovoGiornale = null;

		logger.debug("--> Caricato il giornale");

		// se non trovo nessun giornale non faccio niente
		if (giornale == null) {
			return;
		}

		boolean valido = giornale.getValido();
		boolean invalidaSuccessivi = true;
		List<NotaGiornale> note = giornale.getNoteGiornale();

		// se il giornale e' valido lo posso invalidare altrimenti devo solo aggiungere la nota del documento
		if (valido) {
			giornale.setValido(false);
			salvaGiornale(giornale);

			// invalido righe e area contabile
			invalidaRigheContabiliGiornale(giornale);

			// lancio il messaggio di invalidazione del giornale
			panjeaMessage.send(giornale, "libroGiornaleInvalidato");
		}

		// chiamato in caso di data registrazione precedente null quindi quando non ho modificato la testata
		// del documento ma ho solo un cambio di stato, in cui l'unica data da verificare e' quella del documento
		// stesso
		if (dataRegistrazionePrecedente == null) {
			// in caso di cambio stato aggiungo le note di tipo ENoteGiornale.CAMBIO_STATO_DOCUMENTO
			NotaGiornale notaGiornale = new NotaGiornale();
			notaGiornale.setGiornale(giornale);
			notaGiornale.setTipoNotaGiornale(ENoteGiornale.CAMBIO_STATO_DOCUMENTO);
			Format format = new SimpleDateFormat("dd/MM/yyyy");
			notaGiornale.setNoteInterne(areaContabile.getDocumento().getCodice().getCodice() + "|"
					+ format.format(areaContabile.getDataRegistrazione()));
			if (!isNotaPresente(notaGiornale, note)) {
				salvaNotaGiornale(notaGiornale);
			}
		} else {
			// controllo date
			Calendar calendarDataPrecedente = Calendar.getInstance();
			calendarDataPrecedente.setTime(dataRegistrazionePrecedente);

			if ((calendar.get(Calendar.YEAR) == calendarDataPrecedente.get(Calendar.YEAR))
					&& (calendar.get(Calendar.MONTH) == calendarDataPrecedente.get(Calendar.MONTH))) {

				NotaGiornale notaGiornale = new NotaGiornale();
				notaGiornale.setGiornale(giornale);
				Format format = new SimpleDateFormat("dd/MM/yyyy");
				notaGiornale.setNoteInterne(areaContabile.getDocumento().getCodice() + "|"
						+ format.format(areaContabile.getDataRegistrazione()));
				// se � nello stesso mese controllo se il movimento � prima o dopo dell'ultimo
				// movimento stampato nel giornale per determinare la nota
				if (areaContabile.getDataRegistrazione().before(giornale.getDataUltimoDocumento())) {
					notaGiornale.setTipoNotaGiornale(ENoteGiornale.INSERITO_MOVIMENTO_NEL_GIORNALE);
				} else {
					notaGiornale.setTipoNotaGiornale(ENoteGiornale.INSERITO_MOVIMENTO_DOPO_GIORNALE);
				}
				if (!isNotaPresente(notaGiornale, note)) {
					salvaNotaGiornale(notaGiornale);
				}

			} else {
				// se la nuova data � dopo il mese successivo a quella precedente scrivo la nota di inserimento
				// ma non aggiorno tutti i giornali successivi
				if (areaContabile.getDataRegistrazione().after(dataRegistrazionePrecedente)) {
					invalidaSuccessivi = false;
				}

				nuovoGiornale = caricaGiornale(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

				if (nuovoGiornale != null) {
					NotaGiornale notaGiornale = new NotaGiornale();
					notaGiornale.setGiornale(nuovoGiornale);
					notaGiornale.setTipoNotaGiornale(ENoteGiornale.INSERITO_NUOVO_MOVIMENTO);
					Format format = new SimpleDateFormat("dd/MM/yyyy");
					notaGiornale.setNoteInterne(areaContabile.getDocumento().getCodice() + "|"
							+ format.format(areaContabile.getDataRegistrazione()));

					if (!isNotaPresente(notaGiornale, note)) {
						salvaNotaGiornale(notaGiornale);
					}
				}
			}
		}

		if (invalidaSuccessivi) {

			List<Giornale> listGiornaliSuccessivi;
			if (nuovoGiornale != null) {
				listGiornaliSuccessivi = caricaGiornaliSuccessivi(nuovoGiornale);
			} else {
				listGiornaliSuccessivi = caricaGiornaliSuccessivi(giornale);
			}

			for (Giornale giornalePrecedente : listGiornaliSuccessivi) {
				NotaGiornale notaGiornale = new NotaGiornale();
				notaGiornale.setGiornale(giornalePrecedente);
				notaGiornale.setTipoNotaGiornale(ENoteGiornale.GIORNALE_PRECEDENTE_INVALIDATO);

				note = giornalePrecedente.getNoteGiornale();

				if (!isNotaPresente(notaGiornale, note)) {
					salvaNotaGiornale(notaGiornale);
				}

				if (giornalePrecedente.getValido()) {
					giornalePrecedente.setValido(false);
					salvaGiornale(giornalePrecedente);

					// invalido righe e area contabile
					invalidaRigheContabiliGiornale(giornalePrecedente);
				}
			}
		}

		logger.debug("--> Exit invalidaLibroGiornale");
	}

	/**
	 * Invalida tutte le righe contabili e la relativa area contabile settando per riga contabile le propriet�
	 * paginaGiornale e numeroRigaGiornale a NULL, mentre per quanto riguarda l'area contabile riporta il valore di
	 * numeroPaginaGiornale a ZERO e lo stato a confermato.
	 *
	 * @param giornale
	 *            il giornale da cui recuperare i parametri da settare per il filtro ricerca righe contabili.
	 */
	@SuppressWarnings("unchecked")
	private void invalidaRigheContabiliGiornale(Giornale giornale) {
		logger.debug("--> Enter invalidaRigheContabiliGiornale");
		// Carica gli id di tutte le aree contabili che rientrano nel libro giornale
		Query query = panjeaDAO.prepareNamedQuery("AreaContabile.caricaIdAreeContabilePerGiornale");

		query.setParameter("paramCodiceAzienda", getAzienda());

		Calendar calendar = new GregorianCalendar(giornale.getAnno(), giornale.getMese() - 1, 1);
		query.setParameter("paramDaDataRegistrazione", PanjeaEJBUtil.getDateTimeToZero(calendar.getTime()));

		calendar.set(giornale.getAnno(), giornale.getMese() - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		query.setParameter("paramADataRegistrazione", PanjeaEJBUtil.getDateTimeToZero(calendar.getTime()));
		query.setParameter("paramAnnoMovimento", giornale.getAnnoCompetenza());

		// list di id AreaContabile
		List<Integer> idAreeContabili = query.getResultList();

		for (Integer id : idAreeContabili) {

			Query invalidaRigheCont = panjeaDAO.prepareNamedQuery("RigaContabile.invalidaLibroGiornale");
			invalidaRigheCont.setParameter("paramIdAreaContabile", id);

			try {
				int updatedRighe = invalidaRigheCont.executeUpdate();
				logger.debug("--> Aggiornate " + updatedRighe + " righe contabili");
			} catch (Exception e) {
				logger.error("--> Errore nell'invalidazione delle aree e righe contabili", e);
				throw new RuntimeException("--> Errore nell'invalidazione delle aree e righe contabili", e);
			}
		}
	}

	/**
	 * Verifica se nella lista di note del giornale e' presente una la nota che si vuole salvare.
	 *
	 * @param notaGiornale
	 *            la {@link NotaGiornale} che si vuole salvare
	 * @param note
	 *            lista di {@link NotaGiornale}
	 * @return true o false
	 */
	private boolean isNotaPresente(NotaGiornale notaGiornale, List<NotaGiornale> note) {
		boolean presente = false;
		for (NotaGiornale nota : note) {
			if (nota.getNoteInterne() != null && !nota.getNoteInterne().equals("")
					&& nota.getNoteInterne().equals(notaGiornale.getNoteInterne())) {
				presente = true;
			}
			if ((notaGiornale.getNoteInterne() == null || (notaGiornale.getNoteInterne() != null && notaGiornale
					.getNoteInterne().equals("")))
					&& nota.getTipoNotaGiornale().equals(notaGiornale.getTipoNotaGiornale())) {
				presente = true;
			}
		}
		return presente;
	}

	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	@Override
	public Giornale salvaGiornale(Giornale giornale) {
		logger.debug("--> Enter salvaGiornale");

		Giornale giornale2 = null;
		try {
			giornale2 = panjeaDAO.save(giornale);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del giornale", e);
			throw new RuntimeException("Errore durante il salvataggio del giornale", e);
		}

		// se il giornale � valido cancello le eventuali note generate
		// da invalidazioni precedenti
		if (giornale.getValido()) {
			cancellaNoteGiornale(giornale.getId());
		}

		logger.debug("--> Exit salvaGiornale");
		return giornale2;
	}

	/**
	 * Salva una nota del giornale.<br/>
	 * Viene usata all'interno della classe per inserire una nota associata al giornale. <br/>
	 * Le note vengono create dagli eventi sul giornale e sono di sola lettura.
	 *
	 * @param notaGiornale
	 *            nota da associare al giornale
	 * @throws ContabilitaException
	 *             rilanciato se ho un' errore generico
	 */
	private void salvaNotaGiornale(NotaGiornale notaGiornale) throws ContabilitaException {
		logger.debug("--> Enter salvaNotaGiornale");

		try {
			panjeaDAO.save(notaGiornale);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della nota del giornale.", e);
			throw new ContabilitaException("Errore durante il salvataggio della nota del giornale.", e);
		}

		logger.debug("--> Exit salvaNotaGiornale");
	}

	@Override
	public void verificaStatoGiornali(Integer annoEsercizio) throws GiornaliNonValidiException {
		List<Giornale> giornali = null;
		try {
			giornali = caricaGiornali(annoEsercizio);
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i libri giornali ", e);
			throw new RuntimeException(e);
		}
		for (Giornale giornale : giornali) {
			if (!giornale.getValido()) {
				throw new GiornaliNonValidiException();
			}
		}
	}
}
