package it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni;

import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;

/**
 * @author fattazzo
 *
 */
public class TabellaCategorieEntita extends TabellaDimensione {

    private static final long serialVersionUID = 1905933988631885782L;

    /**
     * Costruttore.
     */
    public TabellaCategorieEntita() {
        super("CategorieEntita", "vista", null, "categorie Entità");
        setContentTableAlias("entità");
        addColumn("sezione", int.class, "Sezione Categoria");
        addColumn("descrizione", String.class, "Desc. Categoria");
    }

}
