package it.eurotn.panjea.anagrafica.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Manager per classi della valuta.
 */
@Stateless(name = "Panjea.ValutaManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.ValutaManager")
public class ValutaManagerBean implements ValutaManager {

	/**
	 * 
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	private class CustomDefaultHandler extends DefaultHandler {

		/**
		 * @uml.property name="referenceDate"
		 */
		private Date referenceDate;
		private final Map<String, ValutaAzienda> mapValute;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param mapValute
		 *            mappa con le valute con chiave il codice.
		 */
		public CustomDefaultHandler(final Map<String, ValutaAzienda> mapValute) {
			super();
			this.mapValute = mapValute;
		}

		/**
		 * 
		 * @param codiceAzienda
		 *            azienda
		 * @param codiceValuta
		 *            codice della valuta
		 * @param data
		 *            data del cambio
		 * @return cambio valuta
		 */
		private CambioValuta cariCambioValutaByIndex(String codiceAzienda, String codiceValuta, Date data) {
			logger.debug("--> Enter cariCambioValutaByIndex");
			Query query = panjeaDAO.prepareNamedQuery("CambioValuta.caricaByIndex");
			query.setParameter("paramCodiceValuta", codiceValuta);
			query.setParameter("paramData", data);
			query.setParameter("paramCodiceAzienda", codiceAzienda);

			CambioValuta cambioValuta;
			try {
				cambioValuta = (CambioValuta) panjeaDAO.getSingleResult(query);
			} catch (ObjectNotFoundException e) {
				cambioValuta = null;
			} catch (DAOException e) {
				logger.error("--> Errore durante il caricamento del cambio valuta", e);
				throw new RuntimeException("Errore durante il caricamento del cambio valuta", e);
			}

			logger.debug("--> Exit cariCambioValutaByIndex");
			return cambioValuta;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (localName.equals("Cube")) {
				String date = attributes.getValue("time");
				if (date != null) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
					try {
						referenceDate = df.parse(date + " 14:15 CET");
					} catch (ParseException e) {
						if (logger.isDebugEnabled()) {
							logger.debug("--> Errore nel caricare i dati dal file xml");
						}
					}
				}
				String currency = attributes.getValue("currency");
				String rate = attributes.getValue("rate");
				if (currency != null && rate != null && mapValute.containsKey(currency)) {
					try {
						if (cariCambioValutaByIndex(getAzienda(), currency, referenceDate) == null) {
							CambioValuta cambioValuta = new CambioValuta();
							cambioValuta.setCodiceAzienda(getAzienda());
							cambioValuta.setValuta(mapValute.get(currency));
							cambioValuta.setData(referenceDate);
							cambioValuta.setTasso(BigDecimal.valueOf(stringToDouble(rate)));

							panjeaDAO.save(cambioValuta);
						}
					} catch (Exception e) {
						logger.error("-->errore nel fare il parse del file xml", e);
					}
				}
			}
		}

		/**
		 * Converte una stringa in un valore double.
		 * 
		 * @param str
		 *            string da convertire
		 * @return valore convertito
		 */
		private double stringToDouble(String str) {
			String[] spliStr = str.split("\\.");
			double wholePart = Double.parseDouble(spliStr[0]);
			double fractionPart = 0;

			if (spliStr.length == 2 && spliStr[1].length() > 0) {
				fractionPart = Double.parseDouble(spliStr[1]) / (Math.pow(10, spliStr[1].length()));
			}

			return wholePart + fractionPart;
		}

	}

	private static Logger logger = Logger.getLogger(ValutaManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	public void aggiornaTassi(byte[] byteArray) {
		// costruisco la mappa contenente le valute aziendali perchè solo quelle verranno
		// aggiornate con i valori contenuti nel file
		Map<String, ValutaAzienda> mapValute = new HashMap<String, ValutaAzienda>();
		List<ValutaAzienda> list = caricaValuteAzienda();
		for (ValutaAzienda valutaAzienda : list) {
			mapValute.put(valutaAzienda.getCodiceValuta(), valutaAzienda);
		}
		try {

			XMLReader saxReader = XMLReaderFactory.createXMLReader();

			CustomDefaultHandler handler = new CustomDefaultHandler(mapValute);
			saxReader.setContentHandler(handler);
			saxReader.setErrorHandler(handler);
			saxReader.parse(new InputSource(new ByteArrayInputStream(byteArray)));
		} catch (Exception e) {
			logger.error("-->errore nell'eseguire il parser del documento xml", e);
		}
	}

	@Override
	public void cancellaCambioValuta(CambioValuta cambioValuta) {
		logger.debug("--> Enter cancellaCambioValuta");

		try {
			panjeaDAO.delete(cambioValuta);
			Query query = panjeaDAO.prepareQuery("delete from ValutaAzienda v where v.cambi.size=0");
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione del cambio valuta.", e);
			throw new RuntimeException("Errore durante la cancellazione del cambio valuta.", e);
		}

		logger.debug("--> Exit cancellaCambioValuta");
	};

	@Override
	public void cancellaValutaAzienda(ValutaAzienda valutaAzienda) {
		logger.debug("--> Enter cancellaValutaAzienda");
		try {
			panjeaDAO.delete(valutaAzienda);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione della valuta azienda", e);
			throw new RuntimeException("Errore durante la cancellazione della valuta azienda", e);
		}

		logger.debug("--> Exit cancellaValutaAzienda");
	}

	@Override
	public CambioValuta caricaCambioValuta(String codiceValuta, Date date) throws CambioNonPresenteException {
		Query query = panjeaDAO.prepareNamedQuery("CambioValuta.caricaByUltimaData");
		query.setParameter("paramCodiceValuta", codiceValuta);
		query.setParameter("paramData", date);
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setFirstResult(0);
		query.setMaxResults(1);
		CambioValuta result = null;
		try {
			if (codiceValuta.equals(caricaValutaAziendaCorrente().getCodiceValuta())) {
				result = new CambioValuta();
				result.setTasso(BigDecimal.ONE);
				result.setId(-1);
				result.setValuta(caricaValutaAziendaCorrente());
			} else {
				result = (CambioValuta) panjeaDAO.getSingleResult(query);
			}
		} catch (DAOException e) {
			if (e.getCause() instanceof NoResultException) {
				throw new CambioNonPresenteException(date, codiceValuta);
			} else {
				logger.error("--> Errore durante il caricamento del cambio valuta ", e);
				throw new RuntimeException("Errore durante il caricamento del cambio valuta  ", e);
			}
		}
		return result;
	}

	@Override
	public List<CambioValuta> caricaCambiValute(Date date) {
		logger.debug("--> Enter caricaCambiValuteUltimi");

		Query query = panjeaDAO.prepareNamedQuery("CambioValuta.caricaByUltimaData");

		List<CambioValuta> listCambiValuta = new ArrayList<CambioValuta>();

		// carico tutte le valute dell'azienda e le scorro una ad una per caricare il
		// cambio più recente rispetto alla dat richiesta.
		List<ValutaAzienda> list = caricaValuteAzienda();
		String codiceValutaAzienda = caricaValutaAziendaCorrente().getCodiceValuta();
		for (ValutaAzienda valutaAzienda : list) {
			// se la valuta è quella aziendale la rimuovo
			if (codiceValutaAzienda.equals(valutaAzienda.getCodiceValuta())) {
				continue;
			}
			query.setParameter("paramCodiceValuta", valutaAzienda.getCodiceValuta());
			query.setParameter("paramData", date);
			query.setParameter("paramCodiceAzienda", getAzienda());
			query.setFirstResult(0);
			query.setMaxResults(1);
			try {
				CambioValuta result = (CambioValuta) panjeaDAO.getSingleResult(query);
				listCambiValuta.add(result);
			} catch (DAOException e) {
				if (e.getCause() instanceof NoResultException) {
					CambioValuta cambio = new CambioValuta();
					cambio.setCodiceAzienda(valutaAzienda.getCodiceAzienda());
					cambio.setValuta(valutaAzienda);
					listCambiValuta.add(cambio);
				} else {
					logger.error("--> Errore durante il caricamento dei cambi azienda", e);
					throw new RuntimeException("Errore durante il caricamento dei cambi azienda", e);
				}
			}
		}
		logger.debug("--> Exit caricaCambiValuteUltimi");
		return listCambiValuta;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CambioValuta> caricaCambiValute(String codiceValuta, int anno) {

		Calendar calendar = Calendar.getInstance();

		// setto la data inizio anno
		calendar.set(anno, 0, 1);
		Date dataInizio = calendar.getTime();

		// setto la data fine anno
		calendar.set(anno, 11, 31);
		Date dataFine = calendar.getTime();

		Query query = panjeaDAO.prepareNamedQuery("CambioValuta.caricaByDate");
		query.setParameter("paramDataIniziale", dataInizio);
		query.setParameter("paramDataFinale", dataFine);
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramCodiceValuta", codiceValuta);

		List<CambioValuta> list = new ArrayList<CambioValuta>();
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei cambi valuta.", e);
			throw new RuntimeException("Errore durante il caricamento dei cambi valuta.", e);
		}

		return list;
	}

	@Override
	public List<String> caricaCodiciValuteAzienda() {
		List<ValutaAzienda> list = caricaValuteAzienda();
		List<String> listCodici = new ArrayList<String>();

		for (ValutaAzienda valutaAzienda : list) {
			listCodici.add(valutaAzienda.getCodiceValuta());
		}
		return listCodici;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager#caricaValutaAzienda(java.lang.String)
	 */
	@Override
	public ValutaAzienda caricaValutaAzienda(String codiceValuta) {
		logger.debug("--> Enter caricaValutaAzienda");
		Query query = panjeaDAO.prepareNamedQuery("ValutaAzienda.caricaByCodice");
		query.setParameter("paramCodiceValuta", codiceValuta);
		query.setParameter("paramCodiceAzienda", getAzienda());
		ValutaAzienda valutaAzienda;
		try {
			valutaAzienda = (ValutaAzienda) panjeaDAO.getSingleResult(query);
		} catch (DAOException e) {
			logger.error("-->errore nal caricare la valuta azienda", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaValutaAzienda");
		return valutaAzienda;
	}

	@Override
	public ValutaAzienda caricaValutaAziendaCorrente() {
		Query q = panjeaDAO.prepareNamedQuery("ValutaAzienda.caricaValutaAziendaCorrente");
		q.setParameter("codiceAzienda", getAzienda());
		ValutaAzienda valuta = (ValutaAzienda) q.getSingleResult();
		return valuta;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValutaAzienda> caricaValuteAzienda() {
		logger.debug("--> Enter caricaValuteAzienda");

		Query query = panjeaDAO.prepareNamedQuery("ValutaAzienda.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());

		List<ValutaAzienda> list = new ArrayList<ValutaAzienda>();

		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle valute azienda", e);
			throw new RuntimeException("Errore durante il caricamento delle valute azienda", e);
		}

		logger.debug("--> Exit caricaValuteAzienda");
		return list;
	}

	/**
	 * 
	 * @return azienda loggata
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public CambioValuta salvaCambioValuta(CambioValuta cambioValuta) {
		logger.debug("--> Enter salvaCambioValuta");

		CambioValuta cambioValutaSalvato = null;
		cambioValuta.setCodiceAzienda(getAzienda());
		if (cambioValuta.getValuta().getCodiceAzienda() == null) {
			cambioValuta.getValuta().setCodiceAzienda(getAzienda());
		}

		try {
			cambioValutaSalvato = panjeaDAO.save(cambioValuta);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del cambio valuta", e);
			throw new RuntimeException("Errore durante il salvataggio del cambio valuta", e);
		}

		logger.debug("--> Exit salvaCambioValuta");
		return cambioValutaSalvato;
	}

	@Override
	public ValutaAzienda salvaValutaAzienda(ValutaAzienda valutaAzienda) {
		logger.debug("--> Enter salValutaAzienda");

		valutaAzienda.setCodiceAzienda(getAzienda());

		try {
			valutaAzienda = panjeaDAO.save(valutaAzienda);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della valuta", e);
			throw new RuntimeException("Errore durante il salvataggio della valuta", e);
		}

		logger.debug("--> Exit salValutaAzienda");
		return valutaAzienda;
	}

}
