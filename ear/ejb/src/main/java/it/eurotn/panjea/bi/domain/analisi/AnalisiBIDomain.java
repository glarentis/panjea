package it.eurotn.panjea.bi.domain.analisi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Tabella;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaFatti;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaAgenti;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaArticoli;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaCategorieCommerciale;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaCategorieEntita;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaClienti;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaData;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaDepositi;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaDistributori;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaDocumenti;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaFornitori;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaOperatori;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaPagamenti;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaSediEntita;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaTipiDocumento;
import it.eurotn.panjea.bi.domain.analisi.tabelle.dimensioni.TabellaVettore;
import it.eurotn.panjea.bi.domain.analisi.tabelle.fatti.TabellaDocumentiMagazzino;
import it.eurotn.panjea.bi.domain.analisi.tabelle.fatti.TabellaDocumentiOrdini;
import it.eurotn.panjea.bi.domain.analisi.tabelle.fatti.TabellaMovimenti;
import it.eurotn.panjea.bi.domain.analisi.tabelle.fatti.TabellaOrdini;

/**
 *
 * Dominio per le analisi BI.
 *
 * @author giangi
 * @version 1.0, 21/mag/2014
 *
 */
public final class AnalisiBIDomain {

    private static List<Tabella> tabelle;

    private static Map<String, Colonna> colonne;

    private static List<Colonna> colonneByIndex;

    public static final String[] FUNZIONI_AGGREGAZIONE = new String[] { "sum", "max", "min", "avg", "variance",
            "stddev_pop", "count" };

    public static final String[] FUNZIONI_AGGREGAZIONE_CAPTION = new String[] { "somma", "max", "min", "media", "var",
            "stdev", "conta" };

    static {

        tabelle = new ArrayList<Tabella>();
        colonne = new HashMap<String, Colonna>();

        // Costruisco il modello per la generazione della query

        // Tabelle dei fatti

        // movimenti magazzino
        TabellaFatti movimenti = new TabellaMovimenti();
        tabelle.add(movimenti);
        colonne.putAll(movimenti.getColonne());

        // Ordini
        TabellaFatti ordini = new TabellaOrdini();
        tabelle.add(ordini);
        colonne.putAll(ordini.getColonne());

        // documenti magazzino
        TabellaFatti docMagazzino = new TabellaDocumentiMagazzino();
        tabelle.add(docMagazzino);
        colonne.putAll(docMagazzino.getColonne());

        // documenti ordine
        TabellaFatti docOrdine = new TabellaDocumentiOrdini();
        tabelle.add(docOrdine);
        colonne.putAll(docOrdine.getColonne());

        // Dimensioni
        TabellaDimensione vettori = new TabellaVettore();
        vettori.addLinkToFactTable(movimenti.getNome(), "sedeVettore_id", "sede_entita_id");
        vettori.addLinkToFactTable(docMagazzino.getNome(), "sedeVettore_id", "sede_entita_id");
        vettori.addLinkToFactTable(ordini.getNome(), "sedeVettore_id", "sede_entita_id");
        vettori.addLinkToFactTable(docOrdine.getNome(), "sedeVettore_id", "sede_entita_id");
        tabelle.add(vettori);
        colonne.putAll(vettori.getColonne());

        TabellaDimensione distributore = new TabellaDistributori();
        distributore.addLinkToFactTable(movimenti.getNome(), "arif.distributore_id", "id");
        tabelle.add(distributore);
        colonne.putAll(distributore.getColonne());

        TabellaDimensione operatori = new TabellaOperatori();
        operatori.addLinkToFactTable(movimenti.getNome(), "arif.operatore_id", "id");
        // operatori.addLinkToFactTable(docMagazzino.getNome(), "sedeVettore_id", "sede_entita_id");
        // operatori.addLinkToFactTable(ordini.getNome(), "sedeVettore_id", "sede_entita_id");
        // operatori.addLinkToFactTable(docOrdine.getNome(), "sedeVettore_id", "sede_entita_id");
        tabelle.add(operatori);
        colonne.putAll(operatori.getColonne());

        TabellaDimensione tipiDocumento = new TabellaTipiDocumento();
        tipiDocumento.addLinkToFactTable(movimenti.getNome(), "tipodocumentoid", "id");
        tipiDocumento.addLinkToFactTable(docMagazzino.getNome(), "tipo_documento_id", "id");
        tipiDocumento.addLinkToFactTable(ordini.getNome(), "tipo_documento_id", "id");
        tipiDocumento.addLinkToFactTable(docOrdine.getNome(), "tipo_documento_id", "id");
        tabelle.add(tipiDocumento);
        colonne.putAll(tipiDocumento.getColonne());

        TabellaDimensione articoli = new TabellaArticoli();
        articoli.addLinkToFactTable(movimenti.getNome(), "articolo_id", "id");
        articoli.addLinkToFactTable(ordini.getNome(), "articolo_id", "id");
        tabelle.add(articoli);
        colonne.putAll(articoli.getColonne());

        TabellaDimensione depositi = new TabellaDepositi();
        depositi.addLinkToFactTable(movimenti.getNome(), "deposito_id", "id");
        depositi.addLinkToFactTable(docMagazzino.getNome(), "depositoOrigine_id", "id");
        depositi.addLinkToFactTable(ordini.getNome(), "depositoOrigine_id", "id");
        depositi.addLinkToFactTable(docOrdine.getNome(), "depositoOrigine_id", "id");
        tabelle.add(depositi);
        colonne.putAll(depositi.getColonne());

        TabellaDimensione data = new TabellaData();
        data.addLinkToFactTable(movimenti.getNome(), "dataRegistrazione ", "DATA_ID");
        data.addLinkToFactTable(docMagazzino.getNome(), "dataRegistrazione", "DATA_ID");
        data.addLinkToFactTable(ordini.getNome(), "dataRegistrazione ", "DATA_ID");
        data.addLinkToFactTable(docOrdine.getNome(), "dataRegistrazione", "DATA_ID");
        tabelle.add(data);
        colonne.putAll(data.getColonne());

        TabellaDimensione sediEntita = new TabellaSediEntita();
        sediEntita.addLinkToFactTable(movimenti.getNome(), "sedeEntita_id", "sede_entita_id");
        sediEntita.addLinkToFactTable(docMagazzino.getNome(), "sedeEntita_id", "sede_entita_id");
        sediEntita.addLinkToFactTable(ordini.getNome(), "sedeEntita_id", "sede_entita_id");
        sediEntita.addLinkToFactTable(docOrdine.getNome(), "sedeEntita_id", "sede_entita_id");
        tabelle.add(sediEntita);
        colonne.putAll(sediEntita.getColonne());

        TabellaDimensione clienti = new TabellaClienti();
        clienti.addLinkToFactTable(movimenti.getNome(), "sedeEntita_id", "sede_entita_id");
        clienti.addLinkToFactTable(docMagazzino.getNome(), "sedeEntita_id", "sede_entita_id");
        clienti.addLinkToFactTable(ordini.getNome(), "sedeEntita_id", "sede_entita_id");
        clienti.addLinkToFactTable(docOrdine.getNome(), "sedeEntita_id", "sede_entita_id");
        tabelle.add(clienti);
        colonne.putAll(clienti.getColonne());

        TabellaPagamenti pagamenti = new TabellaPagamenti();
        pagamenti.addLinkToFactTable(movimenti.getNome(), "ap.codicePagamento_id", "id");
        pagamenti.addLinkToFactTable(docMagazzino.getNome(), "codicePagamento_id", "id");
        pagamenti.addLinkToFactTable(docOrdine.getNome(), "codicePagamento_id", "id");
        pagamenti.addLinkToFactTable(ordini.getNome(), "codicePagamento_id", "id");
        tabelle.add(pagamenti);
        colonne.putAll(pagamenti.getColonne());

        TabellaDimensione agenti = new TabellaAgenti();
        agenti.addLinkToFactTable(movimenti.getNome(), "agente_Id", "entita_id");
        agenti.addLinkToFactTable(ordini.getNome(), "agente_Id", "sede_entita_id");
        tabelle.add(agenti);
        colonne.putAll(agenti.getColonne());

        TabellaDimensione fornitori = new TabellaFornitori();
        fornitori.addLinkToFactTable(movimenti.getNome(), "sedeEntita_id", "sede_entita_id");
        fornitori.addLinkToFactTable(docMagazzino.getNome(), "sedeEntita_id", "sede_entita_id");
        fornitori.addLinkToFactTable(ordini.getNome(), "sedeEntita_id", "sede_entita_id");
        fornitori.addLinkToFactTable(docOrdine.getNome(), "sedeEntita_id", "sede_entita_id");
        tabelle.add(fornitori);
        colonne.putAll(fornitori.getColonne());

        TabellaDimensione documenti = new TabellaDocumenti();
        documenti.addLinkToFactTable(movimenti.getNome(), "am.documento_id", "id");
        documenti.addLinkToFactTable(docMagazzino.getNome(), "documentoMagazzinoId", "id");
        documenti.addLinkToFactTable(ordini.getNome(), "documentoOrdineId", "id");
        documenti.addLinkToFactTable(docOrdine.getNome(), "documentoOrdineId", "id");
        tabelle.add(documenti);
        colonne.putAll(documenti.getColonne());

        TabellaDimensione categorieEntita = new TabellaCategorieEntita();
        categorieEntita.addLinkToFactTable(movimenti.getNome(), "sedeEntita_id", "sede_entita_id");
        categorieEntita.addLinkToFactTable(docMagazzino.getNome(), "sedeEntita_id", "sede_entita_id");
        categorieEntita.addLinkToFactTable(ordini.getNome(), "sedeEntita_id", "sede_entita_id");
        categorieEntita.addLinkToFactTable(docOrdine.getNome(), "sedeEntita_id", "sede_entita_id");
        tabelle.add(categorieEntita);
        colonne.putAll(categorieEntita.getColonne());

        TabellaDimensione categorieCommerciale = new TabellaCategorieCommerciale();
        categorieCommerciale.addLinkToFactTable(movimenti.getNome(), "articolo_id", "art_id");
        categorieCommerciale.addLinkToFactTable(ordini.getNome(), "articolo_id", "art_id");
        tabelle.add(categorieCommerciale);
        colonne.putAll(categorieCommerciale.getColonne());

        // Aggiorno gli indici
        colonneByIndex = new ArrayList<Colonna>(colonne.values().size());
        // Devo ordinare la mappa altrimenti gli indici sballano perchè caricati lato client e
        // server
        List<Colonna> colonneToOrder = new ArrayList<Colonna>(colonne.values());
        Collections.sort(colonneToOrder, new Comparator<Colonna>() {

            @Override
            public int compare(Colonna o1, Colonna o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for (Colonna colonna : colonneToOrder) {
            colonneByIndex.add(colonna);
            colonna.setModelIndex(colonneByIndex.size() - 1);
        }
    }

    /**
     * Costruttore privato.
     */
    private AnalisiBIDomain() {
    }

    /**
     *
     * @param index
     *            indice colonna. l'indice è il numero di inserimento
     * @return colonna
     */
    public static Colonna getColonna(int index) {
        return colonneByIndex.get(index);
    }

    /**
     *
     * @param keyColonna
     *            chiave della colonna
     * @return column by key
     */
    public static Colonna getColonna(String keyColonna) {
        return colonne.get(keyColonna);
    }

    /**
     * @return Returns the colonne.
     */
    public static Map<String, Colonna> getColonne() {
        return colonne;
    }

    /**
     * @return Returns the tabelle.
     */
    public static List<Tabella> getTabelle() {
        return tabelle;
    }
}
