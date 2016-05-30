package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.manager.interfaces.CategorieManager;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

/**
 * Classe che si preoccupa di avvalorare le proprietà di ParametriRicercaArticolo in modo da impostare correttamente i
 * filtri per la ricerca articoli.
 * 
 * @author leonardo
 */
public final class ParametriRicercaArticoloPopulator {

	/**
	 * Popola le proprietà di ParametriRicercaArticolo prendendo informazioni dall'esterno (ad esempio il codice azienda
	 * o le categorie figlie) e trattando proprietà già impostate dei parametri (ad esempio la proprietà filtro in cui
	 * ci possono essere informazioni riguardanti attributi, barcode, ecc).
	 * 
	 * @param parametriRicercaArticolo
	 *            i parametri di cui trattare i dati
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param principal
	 *            principal
	 * @param categorieManager
	 *            categorieManager
	 * @return i parametri con tutte le proprietà utili alla ricerca valorizzate
	 */
	public static ParametriRicercaArticolo populate(ParametriRicercaArticolo parametriRicercaArticolo,
			PanjeaDAO panjeaDAO, JecPrincipal principal, CategorieManager categorieManager) {

		Query queryLigua = panjeaDAO.prepareNamedQuery("Azienda.caricaLingua");
		queryLigua.setParameter("codiceAzienda", principal.getCodiceAzienda());

		String linguaAzienda = (String) queryLigua.getSingleResult();
		// controllo se la lingua dell'utente corrente è quella aziendale
		boolean isLinguaAzienda = (principal.getLingua() != null) && (principal.getLingua().equals(linguaAzienda));

		parametriRicercaArticolo.setLinguaAzienda(isLinguaAzienda);
		parametriRicercaArticolo.setLingua(linguaAzienda);
		parametriRicercaArticolo.setCodiceAzienda(principal.getCodiceAzienda());

		// valorizzo codici (codice,codiceEntita,barcode,descrizione) con le informazioni contenute nei parametri
		parametriRicercaArticolo = ParametriRicercaArticoloCodicePopulator.populate(parametriRicercaArticolo);

		// valorizzo gli attributi con le informazioni contenute nei parametri
		parametriRicercaArticolo = ParametriRicercaArticoloAttributiPopulator.populate(parametriRicercaArticolo,
				panjeaDAO, principal);

		// se la categoria è impostata e ho il flag carica figli, li carico e li aggiungo alla lista di categorie nei
		// parametri
		if (parametriRicercaArticolo.getIdCategoria() != null) {
			List<Integer> idCategorie = new ArrayList<Integer>();
			if (parametriRicercaArticolo.isIncludiArticoliCategorieFiglie()) {
				List<CategoriaLite> categorieFiglie = categorieManager.caricaCategorieFiglie(parametriRicercaArticolo
						.getIdCategoria());
				for (CategoriaLite categoriaLite : categorieFiglie) {
					idCategorie.add(categoriaLite.getId());
				}
			}
			idCategorie.add(parametriRicercaArticolo.getIdCategoria());
			parametriRicercaArticolo.setIdCategorie(idCategorie);
		}

		return parametriRicercaArticolo;
	}

	/**
	 * Costruttore.
	 */
	private ParametriRicercaArticoloPopulator() {
		super();
	}

}
