package it.eurotn.panjea.magazzino.rich.editors.etichette;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

/**
 * Prepara la query sql per il caricamento dei dati articolo per l'etichetta.
 */
public final class ArticoloEtichettaQueryBuilder {

	/**
	 * Prepara la query sql per il caricamento dei dati dell'articolo.
	 *
	 * @param idArticolo
	 *            l'articolo di cui caricare i dati
	 * @param entita
	 *            l'entità di cui caricare i dati articolo associati o null; nel caso in cui sia avvalorata deve avere
	 *            assortimentoArticoli a true
	 * @return sql per caricare i dati dell'articolo o i dati articolo relativi all'entità
	 */
	public static String getSql(Integer idArticolo, EntitaLite entita) {
		StringBuilder sb = new StringBuilder();
		if (entita != null) {
			sb.append("SELECT ");
			sb.append("codArtEnt.codice AS maga_articoli_codice, ");
			sb.append("codArtEnt.barCode AS maga_articoli_barCode, ");
			sb.append("codArtEnt.descrizione AS maga_articoli_descrizioneLinguaAziendale, ");
			sb.append("codArtEnt.barCode2 AS maga_articoli_barCode2, ");
			sb.append("art.note AS note ");
			sb.append("FROM maga_articoli art ");
			sb.append("left join maga_codici_articolo_entita codArtEnt on art.id=codArtEnt.articolo_id ");
			sb.append("where art.id=");
			sb.append(idArticolo);
			sb.append(" and codArtEnt.entita_id=");
			sb.append(entita.getId());
		} else {
			sb.append("SELECT ");
			sb.append("art.codice AS maga_articoli_codice, ");
			sb.append("art.barCode AS maga_articoli_barCode, ");
			sb.append("art.descrizioneLinguaAziendale AS maga_articoli_descrizioneLinguaAziendale, ");
			sb.append("null AS maga_articoli_barCode2, ");
			sb.append("art.note AS note ");
			sb.append("FROM maga_articoli art ");
			sb.append("where art.id=");
			sb.append(idArticolo);
		}
		return sb.toString();
	}

	/**
	 * Costruttore privato.
	 */
	private ArticoloEtichettaQueryBuilder() {
		super();
	}

}
