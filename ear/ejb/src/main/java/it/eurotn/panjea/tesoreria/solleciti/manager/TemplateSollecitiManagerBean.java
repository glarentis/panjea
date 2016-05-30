package it.eurotn.panjea.tesoreria.solleciti.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplatePlaceHolder;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.panjea.tesoreria.solleciti.manager.interfaces.TemplateSollecitiManager;
import it.eurotn.security.JecPrincipal;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.TemplateSollecitiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TemplateSollecitiManager")
public class TemplateSollecitiManagerBean implements TemplateSollecitiManager {
	private static Logger logger = Logger.getLogger(TemplateSollecitiManagerBean.class.getName());

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	/**
	 * cancella un template.
	 * 
	 * @param templateSolleciti
	 *            .
	 */
	@Override
	public void cancellaTemplateSolleciti(TemplateSolleciti templateSolleciti) {
		logger.debug("--> Enter cancellaTemplateSollecito");
		try {
			panjeaDAO.delete(templateSolleciti);
		} catch (Exception e) {
			logger.error("--> Errore nel cancellare il Template " + templateSolleciti, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTemplateSollecito");
	}

	/**
	 * carica la lista de template solleciti per la azienda corrente.
	 * 
	 * @return lista de template.
	 * @throws PagamentiException .
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TemplateSolleciti> caricaTemplateSolleciti() throws PagamentiException {
		logger.debug("--> Enter caricaTemplateSolleciti");

		String codiceAzienda = ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
		Query query = panjeaDAO.prepareNamedQuery("TemplateSolleciti.caricaAll");
		query.setParameter("codiceAzienda", codiceAzienda);
		List<TemplateSolleciti> templateSolleciti;
		try {
			templateSolleciti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di TemplateSolleciti ", e);
			throw new PagamentiException(e);
		}
		logger.debug("--> Exit caricaTemplateSolleciti");
		return templateSolleciti;
	}

	@Override
	public TemplateSolleciti caricaTemplateSollecito(int idTemplateSollecito) {
		try {
			return panjeaDAO.load(TemplateSolleciti.class, idTemplateSollecito);
		} catch (ObjectNotFoundException e) {
			logger.error("--> TemplateSollecito non trovato. ID cercato=" + idTemplateSollecito);
			throw new RuntimeException("TemplateSollecito non trovato. ID cercato=" + idTemplateSollecito);
		}
	}

	@Override
	public String creaTesto(String testo, Sollecito sollecito) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Set<TemplatePlaceHolder> placeHolders = TemplateSolleciti.getTemplatePlaceHolders();

		String testoCreato = testo;

		if (testoCreato == null) {
			testoCreato = "";
		}

		Object object = null;
		for (TemplatePlaceHolder templatePlaceHolder : placeHolders) {
			String path = templatePlaceHolder.getPropertyPath();
			path = path.replace("$", "");
			object = replacePlaceHolders(path, sollecito, "");
			while (testoCreato.indexOf(templatePlaceHolder.getCodice()) != -1) {
				testoCreato = testoCreato.replace(templatePlaceHolder.getCodice(), object.toString());
			}
		}

		return testoCreato;
	}

	/**
	 * Formatta in string l'oggetto in base alla sua instanza.
	 * 
	 * @param object
	 *            oggetto
	 * @return string output
	 */
	public String formatText(Object object) {
		String testo = "";

		if (object instanceof Date) {
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			testo = formatDate.format((Date) object);
			testo = formatDate.format(date);
		} else {
			if (object instanceof BigDecimal) {
				DecimalFormat df = new DecimalFormat("#.##");
				testo = df.format(object);
			} else {
				testo = object.toString();
			}
		}
		return testo;
	}

	/**
	 * 
	 * @return utente loggato
	 */
	private JecPrincipal getPrincipal() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal;

	}

	/**
	 * 
	 * @param path
	 *            path.
	 * @param object
	 *            oggetto
	 * @param result
	 *            result
	 * @return testo del report
	 * @throws IllegalAccessException
	 *             IllegalAccessException
	 * @throws InvocationTargetException
	 *             InvocationTargetException
	 * @throws NoSuchMethodException
	 *             NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	private String replacePlaceHolders(String path, Object object, String result) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String property = path;
		String remainPath = "";

		if (path.contains(".")) {
			property = path.substring(0, path.indexOf("."));
			remainPath = path.substring(path.indexOf(".") + 1, path.length());
		}
		// controllo che la propieta essista
		if (PropertyUtils.isReadable(object, property)) {
			object = PropertyUtils.getNestedProperty(object, property);
		}
		// controllo se sono dati per la tabella o no, il marcatore dei dati della tabella è #
		if (object instanceof List<?> && !((List<?>) object).isEmpty()) {
			object = ((List<RigaSollecito>) object).get(0);
		}
		// recupero la entita
		if (object instanceof EntitaLite) {
			Entita entita = ((EntitaLite) object).creaProxyEntita();

			object = entita;
		}
		// controllo il pat rimanente
		if (!remainPath.equals("")) {
			result = replacePlaceHolders(remainPath, object, result);
		} else {
			if (object == null) {
				object = "";
			}
			result = formatText(object);
		}

		return result;
	}

	/**
	 * salva un template solleciti.
	 * 
	 * @param templateSolleciti
	 *            .
	 * @return .
	 */
	@Override
	public TemplateSolleciti salvaTemplateSolleciti(TemplateSolleciti templateSolleciti) {
		logger.debug("--> Enter salvaTemplateSollecito");
		TemplateSolleciti salvaTemplateSolleciti = null;
		if (templateSolleciti.isNew()) {
			templateSolleciti.setCodiceAzienda(getPrincipal().getCodiceAzienda());
		}
		try {
			salvaTemplateSolleciti = panjeaDAO.save(templateSolleciti);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del template de sollecito", e);
			throw new RuntimeException("Errore durante il salvataggio dell Template", e);
		}
		return salvaTemplateSolleciti;
	}

	/**
	 * Per generare i campi della tabella fisso la lunghezza.
	 * 
	 * @param testo
	 *            testo
	 * @return testo formattato
	 */
	public String toFixLeng(String testo) {
		int length = 20;
		String spazi = "                      ";
		if (testo.length() > length) {
			testo = testo.substring(0, length);
		} else {
			testo = testo + spazi;
			testo = testo.substring(0, length);
		}
		return testo;
	}
}
