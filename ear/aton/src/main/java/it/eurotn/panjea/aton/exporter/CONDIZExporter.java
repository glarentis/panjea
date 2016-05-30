package it.eurotn.panjea.aton.exporter;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.aton.domain.CondizRigaContratto;
import it.eurotn.panjea.aton.domain.CondizRigaListino;
import it.eurotn.panjea.aton.domain.DatiEsportazioneContratti;
import it.eurotn.panjea.aton.domain.wrapper.CodiciEsportazioneContratti;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto.Azione;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.ModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.CONDIZExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CONDIZExporter")
public class CONDIZExporter extends AbstractDataExporter implements DataExporter {

	private class IntegerIntegerKeyValueFromObjectProvider implements KeyValueFromObjectProvider<Integer, Integer> {

		@Override
		public Integer keyFromArray(Object[] elem) {
			return (Integer) elem[0];
		}

		@Override
		public Integer valueFromArray(Object[] elem) {
			return (Integer) elem[1];
		}
	}

	private class IntegerStringKeyValueFromObjectProvider implements KeyValueFromObjectProvider<Integer, String> {

		@Override
		public Integer keyFromArray(Object[] elem) {
			return (Integer) elem[0];
		}

		@Override
		public String valueFromArray(Object[] elem) {
			return (String) elem[1];
		}
	}

	private interface KeyValueFromObjectProvider<T, S> {

		/**
		 * @param elem
		 *            array dal quale recuperare la chiave
		 * @return chiave dell'oggetto
		 */
		T keyFromArray(Object[] elem);

		/**
		 * @param elem
		 *            elem
		 * @return valore dell'oggetto
		 */
		S valueFromArray(Object[] elem);
	}

	public static final String BEAN_NAME = "Panjea.CONDIZExporter";

	/**
	 * @param coll
	 *            coll
	 * @param provider
	 *            provider
	 * @param <T>
	 *            Key class
	 * @param <S>
	 *            Value class
	 * @return Map<T, S>
	 */
	public static <T, S> Map<T, S> collectionToMap(Collection<Object[]> coll, KeyValueFromObjectProvider<T, S> provider) {
		Map<T, S> retval = new HashMap<T, S>();
		for (Object[] elem : coll) {
			retval.put(provider.keyFromArray(elem), provider.valueFromArray(elem));
		}
		return retval;
	}

	@EJB
	private PanjeaDAO panjeaDAO;
	// @EJB
	// private PrezzoArticoloCalculator prezzoArticoloCalculator;
	// @EJB
	// private ContrattiManager contrattiManagerBean;

	@Resource
	private SessionContext sessionContext;

	@EJB
	private ListinoManager listinoManager;

	/**
	 * @return Map<Integer, String>
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, String> caricaCategorieCommercialiArticolo() {
		StringBuilder sb = new StringBuilder();
		sb.append("select c.id,c.codice from CategoriaCommercialeArticolo c");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<Object[]> categorieCommercialiArticolo = new ArrayList<Object[]>();
		try {
			categorieCommercialiArticolo = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		Map<Integer, String> collectionToMap = collectionToMap(categorieCommercialiArticolo,
				new IntegerStringKeyValueFromObjectProvider());

		return collectionToMap;
	}

	/**
	 * @return Map<Integer, String>
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, String> caricaCategorieSedeMagazzino() {
		StringBuilder sb = new StringBuilder();
		sb.append("select c.id,c.descrizione from CategoriaSedeMagazzino c");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<Object[]> categorieSediMagazzino = new ArrayList<Object[]>();
		try {
			categorieSediMagazzino = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		Map<Integer, String> collectionToMap = collectionToMap(categorieSediMagazzino,
				new IntegerStringKeyValueFromObjectProvider());
		return collectionToMap;
	}

	/**
	 * @return Map<Integer, Integer>
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> caricaClienti() {
		StringBuilder sb = new StringBuilder();
		sb.append("select c.id,c.codice from Cliente c");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<Object[]> clienti = new ArrayList<Object[]>();
		try {
			clienti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		Map<Integer, Integer> collectionToMap = collectionToMap(clienti, new IntegerIntegerKeyValueFromObjectProvider());
		return collectionToMap;
	}

	/**
	 * @return Map<Integer, String>
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, String> caricaCodiciArticolo() {
		StringBuilder sb = new StringBuilder();
		sb.append("select a.id,a.codice from Articolo a");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<Object[]> articoli = new ArrayList<Object[]>();
		try {
			articoli = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}

		Map<Integer, String> collectionToMap = collectionToMap(articoli, new IntegerStringKeyValueFromObjectProvider());
		return collectionToMap;
	}

	/**
	 * @return Map<Integer, Integer>
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> caricaCodiciEntitaSedeMagazzino() {
		StringBuilder sb = new StringBuilder();
		sb.append("select sm.id,ent.codice from SedeMagazzino sm join sm.sedeEntita se join se.entita ent");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<Object[]> entitaSediMagazzino = new ArrayList<Object[]>();
		try {
			entitaSediMagazzino = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		Map<Integer, Integer> collectionToMap = collectionToMap(entitaSediMagazzino,
				new IntegerIntegerKeyValueFromObjectProvider());
		return collectionToMap;
	}

	/**
	 * @return Map<Integer, String>
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, String> caricaCodiciSedeEntitaSedeMagazzino() {
		StringBuilder sb = new StringBuilder();
		sb.append("select sm.id,");
		sb.append("case ts.sedePrincipale when true then cast(ent.codice as string) else se.codice end ");
		sb.append("from SedeMagazzino sm ");
		sb.append("join sm.sedeEntita se ");
		sb.append("join se.tipoSede ts ");
		sb.append("join se.entita ent ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<Object[]> righeContratto = new ArrayList<Object[]>();
		try {
			righeContratto = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		Map<Integer, String> collectionToMap = collectionToMap(righeContratto,
				new IntegerStringKeyValueFromObjectProvider());
		return collectionToMap;
	}

	/**
	 * @return List<RigaContratto>
	 */
	@SuppressWarnings("unchecked")
	private List<RigaContratto> caricaRigheContratto() {
		StringBuilder sb = new StringBuilder();
		sb.append("select r from RigaContratto r");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		List<RigaContratto> righeContratto = new ArrayList<RigaContratto>();
		try {
			righeContratto = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		return righeContratto;
	}

	/**
	 * Crea le CondizRigaContratto partendo dalle righe contratto e i parametri prezzo calcolati in base ai relativi
	 * parametri.
	 * 
	 * @param rigaContratto
	 *            rigaContratto
	 * @param parametriCalcoloPrezzo
	 *            parametriCalcoloPrezzo
	 * @param idSedeMagazzino
	 *            idSedeMagazzino
	 * @param codiciEsportazioneContratti
	 *            codiciEsportazioneContratti
	 * @return CondizRigaContratto
	 */
	private CondizRigaContratto createCondizRigaContratto(RigaContratto rigaContratto,
			ParametriCalcoloPrezzi parametriCalcoloPrezzo, Integer idSedeMagazzino,
			CodiciEsportazioneContratti codiciEsportazioneContratti) {

		if (rigaContratto.isStrategiaPrezzoAbilitata()
				&& rigaContratto.getStrategiaPrezzo().getAzionePrezzo().compareTo(Azione.VARIAZIONE) == 0) {
			return null;
		}

		RigaContrattoCalcolo rigaContrattoCalcolo = new RigaContrattoCalcolo();
		rigaContrattoCalcolo.setId(rigaContratto.getId());
		rigaContrattoCalcolo.setIdContratto(rigaContratto.getContratto().getId());

		rigaContrattoCalcolo.setIdArticolo(parametriCalcoloPrezzo.getIdArticolo());
		rigaContrattoCalcolo.setIdSedeMagazzino(idSedeMagazzino);

		// dato che il peso per le righe calcolo, va da tutti-tutti ad articolo-sede, mentre il peso per aton va da
		// articolo-sede a tutti-tutti
		rigaContrattoCalcolo.setIdEntita(parametriCalcoloPrezzo.getIdEntita());
		rigaContrattoCalcolo.setIdCategoriaSede(parametriCalcoloPrezzo.getIdCategoriaSedeMagazzino());
		rigaContrattoCalcolo.setIdCategoriaCommercialeArticolo(parametriCalcoloPrezzo
				.getIdCategoriaCommercialeArticolo());

		rigaContrattoCalcolo.setTutteLeCategorieArticolo(rigaContratto.getTutteLeCategorie());
		rigaContrattoCalcolo.setTutteLeCategorieSediMagazzino(rigaContratto.getContratto()
				.isTutteCategorieSedeMagazzino());
		DatiEsportazioneContratti datiEsportazioneContratti = DatiEsportazioneContratti.getBuilder(
				rigaContrattoCalcolo, codiciEsportazioneContratti);

		// la condiz riga contratto la creo con le informazioni di riga contratto, parametricalcolo prezzo dove ho i
		// prezzi calcolati già sulla base di sconti e variazioni/sostituzioni e datiEsportazioneContratti che mi serve
		// per il peso della riga contratto e per la chiave
		// (codiceArticolo,codiceCatArt,codiceEntita,codiceSede,codiceCatSede)
		CondizRigaContratto condizRigaContratto = new CondizRigaContratto(rigaContratto, parametriCalcoloPrezzo,
				datiEsportazioneContratti);

		return condizRigaContratto;
	}

	@Override
	public void esporta() throws FileCreationException {
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("condiz", getFileForExport());

		esportaListini(out);

		esportaContratti(out);

		out.flush();
		out.close();
	}

	/**
	 * Esporta la parte dei contratti per il condiz.
	 * 
	 * @param out
	 *            il beanWriter su cui scrivere i contratti
	 */
	private void esportaContratti(BeanWriter out) {
		ModuloPrezzoCalculator calculator = (ModuloPrezzoCalculator) sessionContext
				.lookup("Panjea.ContrattoModuloPrezzoCalculator");

		List<RigaContratto> righeContratto = caricaRigheContratto();

		CodiciEsportazioneContratti codiciEsportazioneContratti = new CodiciEsportazioneContratti();
		codiciEsportazioneContratti.setCategorieCommercialiArticolo(caricaCategorieCommercialiArticolo());
		codiciEsportazioneContratti.setCategorieSedeMagazzino(caricaCategorieSedeMagazzino());
		codiciEsportazioneContratti.setClienti(caricaClienti());
		codiciEsportazioneContratti.setCodiciArticolo(caricaCodiciArticolo());
		codiciEsportazioneContratti.setCodiciEntitaSedeMagazzino(caricaCodiciEntitaSedeMagazzino());
		codiciEsportazioneContratti.setCodiciSedeEntitaSedeMagazzino(caricaCodiciSedeEntitaSedeMagazzino());

		// ciclo tutte le righe contratto esistenti
		for (RigaContratto rigaContratto : righeContratto) {

			// se sono associate delle sedi magazzino cilo su queste e a sua volta ciclo sulle categorie sedi, se
			// esistenti
			if (!rigaContratto.getContratto().getSediMagazzino().isEmpty()) {
				for (SedeMagazzinoLite sedeMaga : rigaContratto.getContratto().getSediMagazzino()) {
					List<CondizRigaContratto> list = inspectCategorieSediMagazzino(rigaContratto, null, sedeMaga,
							calculator, codiciEsportazioneContratti);
					writeRigheContratto(out, list);
				}
			}

			// se sono associate delle entita faccio la stessa cosa che per le sedi
			if (!rigaContratto.getContratto().getEntita().isEmpty()) {
				for (EntitaLite entLite : rigaContratto.getContratto().getEntita()) {
					List<CondizRigaContratto> list = inspectCategorieSediMagazzino(rigaContratto, entLite, null,
							calculator, codiciEsportazioneContratti);
					writeRigheContratto(out, list);
				}
			}

			// se non ho sedi, ne entità collegate al contratto allora creo semplicemente la rigaCondiz
			if (rigaContratto.getContratto().getSediMagazzino().isEmpty()
					&& rigaContratto.getContratto().getEntita().isEmpty()) {
				ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(rigaContratto);
				CondizRigaContratto riga = createCondizRigaContratto(rigaContratto,
						calculator.calcola(parametriCalcoloPrezzi), null, codiciEsportazioneContratti);
				if (riga != null) {
					out.write(riga);
				}
			}
		}
	}

	/**
	 * @param out
	 *            writer su cui scrivere i risultati
	 */
	private void esportaListini(BeanWriter out) {
		List<String> codiciListinoDaEsportare = getCodiciListinoDaEsportare();

		List<Listino> listini = listinoManager.caricaListini();
		for (Listino listino : listini) {
			if (codiciListinoDaEsportare.isEmpty() || codiciListinoDaEsportare.indexOf(listino.getCodice()) != -1) {
				VersioneListino versione = listinoManager.caricaVersioneListinoByData(listino, Calendar.getInstance()
						.getTime());
				if (versione == null) {
					continue;
				}
				versione.getRigheListino().size();
				for (RigaListino rigaListino : versione.getRigheListino()) {
					out.write(new CondizRigaListino(rigaListino));
				}
			}
		}
	}

	/**
	 * @return carica la lista di codici listino da esportare dalla chiave delle preference onRoadExportListiniList
	 */
	private List<String> getCodiciListinoDaEsportare() {
		List<String> codiciListinoDaEsportare = new ArrayList<String>();
		Preference preference = null;
		String listiniDaEsportare = "";
		try {
			Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
			query.setParameter("paramChiave", "atonExportListiniList");
			preference = (Preference) panjeaDAO.getSingleResult(query);
			listiniDaEsportare = preference.getValore();
		} catch (Exception e) {
		}
		String[] split = listiniDaEsportare.split("#");
		for (String string : split) {
			if (string != null && !string.trim().isEmpty()) {
				codiciListinoDaEsportare.add(string);
			}
		}
		return codiciListinoDaEsportare;
	}

	/**
	 * 
	 * @param rigaContratto
	 *            rigaContratto
	 * @param entitaLite
	 *            entitaLite
	 * @param sedeMagazzino
	 *            sedeMagazzino
	 * @param calculator
	 *            calculator
	 * @param codiciEsportazioneContratti
	 *            codiciEsportazioneContratti
	 * @return List<CondizRigaContratto>
	 */
	private List<CondizRigaContratto> inspectCategorieSediMagazzino(RigaContratto rigaContratto, EntitaLite entitaLite,
			SedeMagazzinoLite sedeMagazzino, ModuloPrezzoCalculator calculator,
			CodiciEsportazioneContratti codiciEsportazioneContratti) {
		List<CondizRigaContratto> risultati = new ArrayList<CondizRigaContratto>();
		Integer idEnt = entitaLite != null ? entitaLite.getId() : null;
		Integer idSedeMaga = sedeMagazzino != null ? sedeMagazzino.getId() : null;
		Integer idSedeEnt = sedeMagazzino != null ? sedeMagazzino.getSedeEntita().getId() : null;

		if (rigaContratto.getContratto().getCategorieSediMagazzino() != null
				&& rigaContratto.getContratto().getCategorieSediMagazzino().size() > 0) {

			for (CategoriaSedeMagazzino catSede : rigaContratto.getContratto().getCategorieSediMagazzino()) {
				Integer idCatSede = catSede.getId();

				ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(rigaContratto);
				parametriCalcoloPrezzi.setIdEntita(idEnt);
				parametriCalcoloPrezzi.setIdSedeEntita(idSedeEnt);
				parametriCalcoloPrezzi.setIdCategoriaSedeMagazzino(idCatSede);
				CondizRigaContratto riga = createCondizRigaContratto(rigaContratto,
						calculator.calcola(parametriCalcoloPrezzi), idSedeMaga, codiciEsportazioneContratti);
				if (riga != null) {
					risultati.add(riga);
				}
			}
		} else {
			ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(rigaContratto);
			parametriCalcoloPrezzi.setIdEntita(idEnt);
			parametriCalcoloPrezzi.setIdSedeEntita(idSedeEnt);
			CondizRigaContratto riga = createCondizRigaContratto(rigaContratto,
					calculator.calcola(parametriCalcoloPrezzi), idSedeMaga, codiciEsportazioneContratti);
			if (riga != null) {
				risultati.add(riga);
			}
		}
		return risultati;
	}

	/**
	 * @param out
	 *            writer su cui scrivere la lista
	 * @param righeContratto
	 *            righeContratto da scrivere
	 */
	private void writeRigheContratto(BeanWriter out, List<CondizRigaContratto> righeContratto) {
		for (CondizRigaContratto rigaContratto : righeContratto) {
			out.write(rigaContratto);
		}
	}

}
