/**
 *
 */
package it.eurotn.rich.services;

import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.rich.bd.ILayoutBD;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

/**
 * @author fattazzo
 * 
 */
public class TableLayoutCache {

    private ILayoutBD layoutBD;

    private Map<String, List<TableLayout>> layoutsCache;

    {
        layoutsCache = new HashMap<String, List<TableLayout>>();
    }

    /**
     * Cancella il layout e lo rimuove dalla cache.
     * 
     * @param layout
     *            layout da cancellare
     */
    public void cancella(TableLayout layout) {

        layoutBD.cancella(layout);

        // se il layout è in cache lo rimuovo
        if (getLayoutFromCache(layout) != null) {
            // Siccome la equals si basa su chiave e utente, invece di ciclare per trovare il layout ( la remove della
            // list potrebbe togliere quello non corretto ), faccio prima a ricaricare i layout
            layoutsCache.remove(layout.getChiave());
            caricaTableLayout(layout.getChiave());
        }
    }

    /**
     * Restituisce tutti i layout per la chiave specificata.
     * 
     * @param key
     *            chiave
     * @return layout caricati
     */
    public List<TableLayout> caricaTableLayout(String key) {

        List<TableLayout> listLayouts = layoutsCache.get(key);

        // se nella cache non ho già i layout per la chiave specificata vado a caricarli
        if (listLayouts == null) {
            listLayouts = layoutBD.caricaTableLayout(key);
            layoutsCache.put(key, listLayouts);
        }

        // clono i layout che restituisco altrimenti se vengono cambiate da fuori le loro proprietà per riferimento
        // cambiano anche quelle della cache
        List<TableLayout> results = new ArrayList<>();
        for (TableLayout tableLayout : listLayouts) {
            results.add((TableLayout) SerializationUtils.clone(tableLayout));
        }

        return Collections.unmodifiableList(results);
    }

    /**
     * Svuota la cache dei layout delle tabelle.
     */
    public void clearCache() {
        layoutsCache.clear();
    }

    /**
     * Restituisce il layout contenuto nella cache se esiste. <code>null</code> altrimenti.
     * 
     * @param layout
     *            layout da cercare
     * @return layout trovato
     */
    private TableLayout getLayoutFromCache(TableLayout layout) {

        TableLayout layoutCache = null;

        List<TableLayout> layoutsKey = layoutsCache.get(layout.getChiave());
        if (layoutsKey != null) {
            for (TableLayout tableLayout : layoutsKey) {
                if (tableLayout.getId().equals(layout.getId())) {
                    layoutCache = tableLayout;
                    break;
                }
            }
        }

        return layoutCache;
    }

    /**
     * Indica se i 2 layout sono diversi, verificando il cambiamento delle proprietà:<br>
     * - data<br>
     * - estendiColonne<br>
     * - global<br>
     * - name<br>
     * - visualizzaNumeriRiga.<br>
     * 
     * @param layout
     *            layout 1
     * @param layoutCache
     *            layout 2
     * @return <code>true</code> se una della proprietà è cambiata
     */
    private boolean isLayoutChanged(TableLayout layout, TableLayout layoutCache) {
        Boolean estendiColonne = layout.getEstendiColonne();
        Boolean estendiColonneCache = layoutCache.getEstendiColonne();

        Boolean visualizzaNumeriRiga = layout.getVisualizzaNumeriRiga();
        Boolean visualizzaNumeriRigaCache = layoutCache.getVisualizzaNumeriRiga();

        boolean global = layout.isGlobal();
        boolean globalCache = layoutCache.isGlobal();

        String name = layout.getName();
        String nameCache = layoutCache.getName();

        String data = layout.getData();
        String dataCache = layoutCache.getData();

        return (estendiColonne != null && estendiColonneCache != null && !estendiColonne.equals(estendiColonneCache))
                || (visualizzaNumeriRiga != null && visualizzaNumeriRigaCache != null
                        && !visualizzaNumeriRiga.equals(visualizzaNumeriRigaCache))
                || global != globalCache || (name != null && nameCache != null && !name.equals(nameCache))
                || (data != null && dataCache != null && !data.equals(dataCache));
    }

    /**
     * Salva il layout della tabella se non è in cache o se è cambiato.
     * 
     * @param layout
     *            layout da salvare
     * @return layout salvato
     */
    public TableLayout salva(TableLayout layout) {

        // ottengo il layout in cache
        TableLayout layoutCache = getLayoutFromCache(layout);

        // se non ho il layout in cache o se quello nuovo è cambiato lo salvo altrimenti restituisco quello in cache
        if (layoutCache == null || isLayoutChanged(layout, layoutCache)) {
            layout = (TableLayout) layoutBD.salva(layout);
            updateLayoutCache(layout);
        }

        return (TableLayout) PanjeaEJBUtil.cloneObject(layout);
    }

    /**
     * @param layoutBD
     *            the layoutBD to set
     */
    public void setLayoutBD(ILayoutBD layoutBD) {
        this.layoutBD = layoutBD;
    }

    /**
     * Aggiorna il layout in cache o lo aggiunge se non esiste.
     * 
     * @param layout
     *            layout
     */
    private void updateLayoutCache(TableLayout layout) {

        List<TableLayout> layouts = layoutsCache.get(layout.getChiave());

        if (layouts == null || layouts.isEmpty()) {
            layouts = new ArrayList<TableLayout>();
            layouts.add(layout);
        } else {
            // cerco il layout in cache, se non c'è lo aggiungo altrimenti lo cerco per id visto che non posso tramite
            // equals
            if (getLayoutFromCache(layout) == null) {
                layouts.add(layout);
            } else {
                int idx = -1;
                for (int i = 0; i < layouts.size(); i++) {
                    idx = layouts.get(i).getId().equals(layout.getId()) ? i : idx;
                }
                layouts.set(idx, layout);
            }
        }

        layoutsCache.put(layout.getChiave(), layouts);
    }
}
