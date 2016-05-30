package it.eurotn.panjea.onroad.domain;

import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.panjea.onroad.domain.wrapper.CodiciEsportazioneContratti;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class DatiEsportazioneContratti {

	private static Logger logger = Logger.getLogger(DatiEsportazioneContratti.class);
	public static final String TIPORECORD_LISTINO = "0013";
	private static List<DatiEsportazioneContratti> contrattiBuilder;

	static {
		// vengono ripetuti dei livelli per coprire i livelli categoria articolo 1 e categoria articolo 2 nelle sue
		// varianti per i livelli entità
		contrattiBuilder = new ArrayList<DatiEsportazioneContratti>();

		// tutti-nessuno peso 0
		contrattiBuilder.add(new DatiEsportazioneContratti1());

		// tutti-tutti peso 1
		contrattiBuilder.add(new DatiEsportazioneContratti1());

		// tutti-categoria cliente peso 2
		contrattiBuilder.add(new DatiEsportazioneContratti2());

		// tutti-cliente peso 3
		contrattiBuilder.add(new DatiEsportazioneContratti3());

		// tutti-sede peso 4
		contrattiBuilder.add(new DatiEsportazioneContratti4());

		// cat art 1
		// cat art-nessuno peso 5
		contrattiBuilder.add(new DatiEsportazioneContratti6());

		// categoria articolo-tutti peso 6
		contrattiBuilder.add(new DatiEsportazioneContratti6());

		// categoria articolo-categoria cliente peso 7
		contrattiBuilder.add(new DatiEsportazioneContratti7());

		// categoria articolo-cliente peso 8
		contrattiBuilder.add(new DatiEsportazioneContratti8());

		// categoria articolo-sede peso 9
		contrattiBuilder.add(new DatiEsportazioneContratti9());

		// ripeto per cat art 2
		// categoria articolo 2-nessuno peso 10
		contrattiBuilder.add(new DatiEsportazioneContratti6());

		// categoria articolo 2-tutti peso 11
		contrattiBuilder.add(new DatiEsportazioneContratti6());

		// categoria articolo 2-categoria cliente peso 12
		contrattiBuilder.add(new DatiEsportazioneContratti7());

		// categoria articolo 2-cliente peso 13
		contrattiBuilder.add(new DatiEsportazioneContratti8());

		// categoria articolo 2-sede peso 14
		contrattiBuilder.add(new DatiEsportazioneContratti9());

		// continuo con le condizioni
		// articolo-nessuna peso 15
		contrattiBuilder.add(new DatiEsportazioneContratti11());

		// art-tutti peso 16
		contrattiBuilder.add(new DatiEsportazioneContratti11());

		// art-categoria sede peso 17
		contrattiBuilder.add(new DatiEsportazioneContratti12());

		// art-entita peso 18
		contrattiBuilder.add(new DatiEsportazioneContratti13());

		// articolo-sede peso 19
		contrattiBuilder.add(new DatiEsportazioneContratti14());
	}

	/**
	 * @param rigaContratto
	 *            riga del contratto da esportare
	 * @param codiciEsportazioneContratti
	 *            dati per recuperare il codice esportazione contratto
	 * @return builder per l'esportazione della riga in Aton dipendente dal peso (TipoRecord di aton)
	 */
	public static DatiEsportazioneContratti getBuilder(RigaContrattoCalcolo rigaContratto,
			CodiciEsportazioneContratti codiciEsportazioneContratti) {
		if (logger.isDebugEnabled()) {
			logger.debug("--> Recupero il contrattoBuilder con peso " + rigaContratto.getPeso());
		}
		DatiEsportazioneContratti builder = getContrattiBuilder().get(rigaContratto.getPeso()).createNewInstance();
		builder.setCodiceArticolo(codiciEsportazioneContratti.getCodiciArticolo().get(rigaContratto.getIdArticolo()));
		builder.setCodiceCategoriaArticolo(codiciEsportazioneContratti.getCategorieCommercialiArticolo().get(
				rigaContratto.getIdCategoriaCommercialeArticolo()));
		builder.setCodiceCategoriaCliente(codiciEsportazioneContratti.getCategorieSedeMagazzino().get(
				rigaContratto.getIdCategoriaSede()));

		if (rigaContratto.getIdEntita() == null && rigaContratto.getIdSedeMagazzino() != null) {
			builder.setCodiceCliente(codiciEsportazioneContratti.getCodiciEntitaSedeMagazzino().get(
					rigaContratto.getIdSedeMagazzino()));
		} else {
			builder.setCodiceCliente(codiciEsportazioneContratti.getClienti().get(rigaContratto.getIdEntita()));
		}

		builder.setCodiceSede(codiciEsportazioneContratti.getCodiciSedeEntitaSedeMagazzino().get(
				rigaContratto.getIdSedeMagazzino()));
		return builder;
	}

	/**
	 * @return List<ChiaveCondizRiga>
	 */
	public static List<ChiaveCondizRiga> getChiavi() {
		List<ChiaveCondizRiga> chiavi = new ArrayList<ChiaveCondizRiga>();
		for (DatiEsportazioneContratti contrattoBuilder : getContrattiBuilder()) {
			chiavi.add(contrattoBuilder.getChiaveCondiz());
		}
		return chiavi;
	}

	/**
	 * I livelli 6,7,8,9 sono aggiunti una seconda volta per coprire i pesi della categoria commerciale articolo 2 che
	 * presenta un peso diverso dalle righe contratto con categoria commerciale 1, ma in termini pratici per aton
	 * risulta essere identica.
	 * 
	 * @return lista dei builder per l'esportazione
	 */
	private static List<DatiEsportazioneContratti> getContrattiBuilder() {
		return contrattiBuilder;
	}

	private String codiceArticolo;
	private String codiceCategoriaArticolo;
	private String codiceCategoriaCliente;
	private Integer codiceCliente;
	private String codiceSede;

	/**
	 * Costruttore protetto. Costruita solamente tramite il metodo factory getBuilder.
	 */
	protected DatiEsportazioneContratti() {
	}

	/**
	 * @return una nuova istanza della corretta DatiEsportazioneContratti
	 */
	protected abstract DatiEsportazioneContratti createNewInstance();

	/**
	 * Restituisce la chiave associata al condiz; visto che ogni combinazione di codiz dai contratti si compone da
	 * diverse combinazioni dei parametri articolo, categoria articolo, sede, entità, categoria entità, ogni chiave è
	 * strettamente associata alla rigaCondiz.
	 * 
	 * @return chiave condiz
	 */
	protected abstract ChiaveCondizRiga getChiaveCondiz();

	/**
	 * @return codice dell'articolo
	 */
	protected String getCodiceArticolo() {
		// Query query = panjeaDAO.prepareQuery("select a.codice from Articolo a where a.id=" + riga.getIdArticolo());
		// String codice = "";
		// try {
		// codice = (String) panjeaDAO.getSingleResult(query);
		// } catch (DAOException e) {
		// logger.error("-->errore nel caricare il codice articolo", e);
		// throw new RuntimeException(e);
		// }
		String codice = "";
		if (codiceArticolo != null) {
			codice = codiceArticolo;
		}
		String fill = codice + "                      ";
		return fill.substring(0, 20);
	}

	/**
	 * @return codice categoria articolo.
	 */
	protected String getCodiceCategoriaArticolo() {
		// String codice = "";
		// Query query = panjeaDAO.prepareQuery("select c.codice from CategoriaCommercialeArticolo c where c.id="
		// + riga.getIdCategoriaCommercialeArticolo());
		// try {
		// codice = (String) panjeaDAO.getSingleResult(query);
		// } catch (DAOException e) {
		// logger.error("-->errore nel caricare il codice categoria articolo", e);
		// throw new RuntimeException(e);
		// }
		String codice = "";
		if (codiceCategoriaArticolo != null) {
			codice = codiceCategoriaArticolo;
		}
		String fill = codice + "              ";
		return fill.substring(0, 15);
	}

	/**
	 * @return codice categoria cliente.
	 */
	protected String getCodiceCategoriaCliente() {
		// String codice = "";
		// Query query = panjeaDAO.prepareQuery("select c.descrizione from CategoriaSedeMagazzino c where c.id="
		// + riga.getIdCategoriaSede());
		//
		// try {
		// @SuppressWarnings("unchecked")
		// List<String> result = panjeaDAO.getResultList(query);
		// if (result.size() == 0) {
		// codice = "categoriafornitore?";
		// } else {
		// codice = result.get(0);
		// }
		// } catch (DAOException e) {
		// logger.error("-->errore nel caricare il codice categoria cliente", e);
		// throw new RuntimeException(e);
		// }

		String codice = "";
		if (codiceCategoriaCliente == null) {
			codice = "categoriafornitore";
		} else {
			codice = codiceCategoriaCliente;
		}
		String fill = codice + "              ";
		return fill.substring(0, 4);
	}

	/**
	 * @return codiceCliente
	 */
	protected String getCodiceCliente() {
		// String queryString = new String("select c.codice from Cliente c where c.id=" + riga.getIdEntita());
		// // se ho la sede magazzino, ma non la sede entità, mi serve cmq il codice entità e quindi cambio la query in
		// // modo da trovare il codice entità dalla sede
		// if (riga.getIdEntita() == null && riga.getIdSedeMagazzino() != null) {
		// queryString = new String(
		// "select ent.codice from SedeMagazzino sm join sm.sedeEntita se join se.entita ent where sm.id="
		// + riga.getIdSedeMagazzino());
		// }
		// Query query = panjeaDAO.prepareQuery(queryString);
		// String codice = "";
		// try {
		// @SuppressWarnings("unchecked")
		// List<Integer> result = panjeaDAO.getResultList(query);
		// if (result.size() == 0) {
		// codice = "fornitore";
		// } else {
		// codice = result.get(0).toString();
		// }
		// } catch (DAOException e) {
		// logger.error("-->errore nel caricare il codice cliente", e);
		// throw new RuntimeException(e);
		// }

		String codice = "";
		if (codiceCliente == null) {
			codice = "fornitore";
		} else {
			codice = codiceCliente + "";
		}
		String fill = codice + "              ";
		return fill.substring(0, 12);
	}

	/**
	 * @return stringa rappresentate il codice per l'esportazione verso aton
	 */
	public abstract String getCodiceEsportazione();

	/**
	 * @return codice sede (codice entita se sede principale, codice sede entita se secondaria)
	 */
	protected String getCodiceSede() {
		// Query query = panjeaDAO
		// .prepareQuery("select case ts.sedePrincipale when true then cast(ent.codice as string) else se.codice end from SedeMagazzino sm join sm.sedeEntita se join se.tipoSede ts join se.entita ent where sm.id="
		// + riga.getIdSedeMagazzino());
		// String codice = "";
		// if (riga.getIdSedeMagazzino() != null) {
		// try {
		// @SuppressWarnings("unchecked")
		// List<String> result = panjeaDAO.getResultList(query);
		// if (result.size() == 0) {
		// codice = "fornitore";
		// } else {
		// codice = result.get(0).toString();
		// }
		// } catch (DAOException e) {
		// logger.error("-->errore nel caricare il codice cliente", e);
		// throw new RuntimeException(e);
		// }
		// }

		String codice = "";
		if (codiceSede == null) {
			codice = "fornitore";
		} else {
			codice = codiceSede;
		}
		String fill = codice + "              ";
		return fill.substring(0, 12);
	}

	/**
	 * @return tipo record per l'esportazione verso aton; il tipo record va in ordine inverso rispetto al peso, quindi
	 *         il peso 0 sarà l'ultimo, mentre il peso 14 sarà il primo.
	 */
	public abstract String getTipoRecord();

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	private void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	/**
	 * @param codiceCategoriaArticolo
	 *            the codiceCategoriaArticolo to set
	 */
	private void setCodiceCategoriaArticolo(String codiceCategoriaArticolo) {
		this.codiceCategoriaArticolo = codiceCategoriaArticolo;
	}

	/**
	 * @param codiceCategoriaCliente
	 *            the codiceCategoriaCliente to set
	 */
	private void setCodiceCategoriaCliente(String codiceCategoriaCliente) {
		this.codiceCategoriaCliente = codiceCategoriaCliente;
	}

	/**
	 * @param codiceCliente
	 *            the codiceCliente to set
	 */
	private void setCodiceCliente(Integer codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	/**
	 * @param codiceSede
	 *            the codiceSede to set
	 */
	private void setCodiceSede(String codiceSede) {
		this.codiceSede = codiceSede;
	}
}
