package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

public final class ParametriRicercaArticoloAttributiPopulator {

	/**
	 * Recupera la posizione di inizio e di fine attributi nella stringa passata.
	 * 
	 * @param filterStringToParse
	 *            la string in cui cercare l'indice di inizio e fine per gli attributi
	 * @return un array con due elementi, beginIndex e endIndex
	 */
	public static int[] getBeginEndAttributiIndexes(String filterStringToParse) {
		// trova l'inizio e la fine della substring che rappresenta gli attributi
		int sxInizioAttributi = filterStringToParse.indexOf(ParametriRicercaArticolo.DELIMITER_ATTRIBUTO);
		int dxLastDollar = filterStringToParse.lastIndexOf(ParametriRicercaArticolo.DELIMITER_ATTRIBUTO);
		int dxFineAttr = filterStringToParse.length();
		if (sxInizioAttributi != dxLastDollar) {
			int dxLastSpace = filterStringToParse.lastIndexOf(" ");
			dxFineAttr = dxLastSpace != -1 && dxLastSpace > dxLastDollar ? dxLastSpace : dxFineAttr;
		}
		return new int[] { sxInizioAttributi, dxFineAttr };
	}

	/**
	 * Restituisce il tipo di dato del tipo attributo associato al codice scelto.
	 * 
	 * @param codiceAttributo
	 *            il codice del tipo attributo di cui si vuole conoscere il tipo di dato
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param principal
	 *            principal
	 * @return ETipoDatoTipoAttributo
	 */
	private static ETipoDatoTipoAttributo getTipoDatoAttributo(String codiceAttributo, PanjeaDAO panjeaDAO,
			JecPrincipal principal) {
		ETipoDatoTipoAttributo tipoAttributo = null;
		Query queryTipoAttributo = panjeaDAO
				.prepareQuery("select ta.tipoDato from TipoAttributo ta where ta.codiceAzienda = :paramCodiceAzienda and ta.codice=:paramCodice ");
		queryTipoAttributo.setParameter("paramCodiceAzienda", principal.getCodiceAzienda());
		queryTipoAttributo.setParameter("paramCodice", codiceAttributo);
		try {
			tipoAttributo = (ETipoDatoTipoAttributo) panjeaDAO.getSingleResult(queryTipoAttributo);
		} catch (DAOException e) {
			// non faccio nulla, potrei scrivere il codice errato
		}
		return tipoAttributo;
	}

	/**
	 * Esegue il parse della stringa ricevuta alla ricerca di attributi.
	 * 
	 * @param filterStringToParse
	 *            filtro di cui fare il parse per gli attributi
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param principal
	 *            principal
	 * @param attributi2
	 * @return la stringa per eseguire la ricerca senza la parte relativa agli attributi
	 */
	private static List<ParametroRicercaAttributo> parseFilterForAttributi(String filterStringToParse,
			PanjeaDAO panjeaDAO, JecPrincipal principal, Map<String, AttributoArticolo> attributiArticolo) {
		List<ParametroRicercaAttributo> ricercaAttributi = null;

		if (filterStringToParse != null
				&& filterStringToParse.indexOf(ParametriRicercaArticolo.DELIMITER_ATTRIBUTO) != -1) {

			String attributi = null;
			String[] attrs = null;

			ricercaAttributi = new ArrayList<ParametroRicercaAttributo>();
			Pattern pattern = Pattern.compile(ParametriRicercaArticolo.PATTERN_OPERATORE);

			int[] indexBeginEnd = getBeginEndAttributiIndexes(filterStringToParse);

			// prendo solo gli attributi, escludo quindi l'eventuale descrizione
			attributi = filterStringToParse.substring(indexBeginEnd[0], indexBeginEnd[1]);
			attrs = attributi.split(" ");

			for (String attr : attrs) {
				String operatore = null;
				String codiceAttributo = StringUtils.substringBetween(attr,
						ParametriRicercaArticolo.DELIMITER_ATTRIBUTO);
				// mi può arrivare {$attributo$}= non avrò quindi nessun valore per il value dell'attributo
				String value = attr.split(ParametriRicercaArticolo.PATTERN_OPERATORE).length == 2 ? attr
						.split(ParametriRicercaArticolo.PATTERN_OPERATORE)[1] : "";
				if (value.isEmpty() && !attributiArticolo.isEmpty()) {
					AttributoArticolo att = attributiArticolo.get(codiceAttributo);
					if (att != null) {
						value = att.getValore();
					}
				}
				ETipoDatoTipoAttributo tipoDato = getTipoDatoAttributo(codiceAttributo, panjeaDAO, principal);

				// se trovo un operatore impostato dall'utente lo utilizzo come operatore di default; spetta comunque
				// alle implementazioni di ParametroRicercaAttributo permettere o meno l'utilizzo di operatori
				// particolari
				Matcher matcher = pattern.matcher(attr);
				if (matcher.find()) {
					operatore = matcher.group(1);
				}

				if (codiceAttributo != null && !codiceAttributo.isEmpty()) {
					ParametroRicercaAttributo ricercaAttributo = ParametroRicercaAttributoFactory.create(
							codiceAttributo, operatore, value, tipoDato);
					if (ricercaAttributo != null) {
						ricercaAttributi.add(ricercaAttributo);
					}
				}
			}

		}
		return ricercaAttributi;
	}

	/**
	 * Avvalora gli attributi di ParametriRicercaArticolo con le informazioni trovate nella stessa
	 * ParametriRicercaArticolo.
	 * 
	 * @param parametriRicercaArticolo
	 *            i parametri da cui ricavare le informazioni per avvalorare gli attributi
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param principal
	 *            principal
	 * @return ParametriRicercaArticolo avvalorati con le informazioni riguardo gli attributi
	 */
	public static ParametriRicercaArticolo populate(ParametriRicercaArticolo parametriRicercaArticolo,
			PanjeaDAO panjeaDAO, JecPrincipal principal) {
		// parse degli attributi in una lista di parametroRicercaAttributo, vedere le implementazioni per i tipi
		// disponibili
		Map<String, AttributoArticolo> attributi = new HashMap<String, AttributoArticolo>();
		if (parametriRicercaArticolo.getIdArticolo() != null) {
			// carico gli attributi dell'articolo per mettere il valore se non l'ho fissato in maschera
			Query query = panjeaDAO.prepareNamedQuery("AttributoArticolo.caricaByArticolo");
			query.setParameter("paramIdArticolo", parametriRicercaArticolo.getIdArticolo());

			try {
				List<AttributoArticolo> attributiList = new ArrayList<AttributoArticolo>();
				attributiList = panjeaDAO.getResultList(query);
				for (AttributoArticolo attributoArticolo : attributiList) {
					attributi.put(attributoArticolo.getTipoAttributo().getCodice(), attributoArticolo);
				}
			} catch (Exception e) {
				throw new RuntimeException("errore durante il caricamento degli attributi articolo.", e);
			}
		}
		List<ParametroRicercaAttributo> ricercaAttributi = parseFilterForAttributi(
				parametriRicercaArticolo.getFiltro(), panjeaDAO, principal, attributi);

		parametriRicercaArticolo.setRicercaAttributi(ricercaAttributi);
		return parametriRicercaArticolo;

	}

	/**
	 * Costruttore.
	 */
	private ParametriRicercaArticoloAttributiPopulator() {
		super();
	}

}
