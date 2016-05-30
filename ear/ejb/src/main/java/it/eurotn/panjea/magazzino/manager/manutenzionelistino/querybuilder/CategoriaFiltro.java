package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

/**
 * 
 * Testo sql per il filtro delle categorie.
 * 
 * @author giangi
 * @version 1.0, 11/nov/2010
 * 
 */
public class CategoriaFiltro extends AbstractFiltroSorgente {

	@Override
	public String getJoin() {
		return "";
	}

	@Override
	public String getWhere() {
		return " articoli.categoria_id in (:categorieSql) ";
	}

}
