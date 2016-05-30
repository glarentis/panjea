package it.eurotn.panjea.magazzino.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.Matcher;
import it.eurotn.panjea.contabilita.domain.SottoConto;

/**
 * Recupera il sotto conto associato alle varie categorie, deposito,sedeentita,articolo. Le categorie hanno la seguente
 * priorità: articolo-deposito-categorie
 *
 * @author giangi
 *
 */
public class SottoContiContabilizzazione {
    private class MatcherDepositoSedeArticolo implements Matcher<SottoContoContabilizzazione> {
        /**
         * @uml.property name="categoriaContabileDeposito"
         * @uml.associationEnd
         */
        private CategoriaContabileDeposito categoriaContabileDeposito;
        /**
         * @uml.property name="categoriaContabileSedeMagazzino"
         * @uml.associationEnd
         */
        private CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino;
        /**
         * @uml.property name="categoriaContabileArticolo"
         * @uml.associationEnd
         */
        private CategoriaContabileArticolo categoriaContabileArticolo;

        /**
         * Costruttore.
         *
         * @param categoriaContabileArticolo
         *            {@link CategoriaContabileArticolo}
         * @param categoriaContabileDeposito
         *            {@link CategoriaContabileDeposito}
         * @param categoriaContabileSedeMagazzino
         *            {@link CategoriaContabileSedeMagazzino}
         */
        public MatcherDepositoSedeArticolo(final CategoriaContabileArticolo categoriaContabileArticolo,
                final CategoriaContabileDeposito categoriaContabileDeposito,
                final CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
            this.categoriaContabileDeposito = categoriaContabileDeposito;
            this.categoriaContabileSedeMagazzino = categoriaContabileSedeMagazzino;
            this.categoriaContabileArticolo = categoriaContabileArticolo;
        }

        @Override
        public boolean matches(SottoContoContabilizzazione sottoContoContabilizzazione) {
            boolean articoloMatch = sottoContoContabilizzazione.getCategoriaContabileArticolo() == null;
            if (!articoloMatch) {
                if (categoriaContabileArticolo != null) {
                    // la categoria articolo non è null (quindi ben definita e
                    // non ha la combinazione
                    // "tutte le categorie articolo")
                    articoloMatch = categoriaContabileArticolo
                            .equals(sottoContoContabilizzazione.getCategoriaContabileArticolo());
                } else {
                    articoloMatch = false;
                }
            }

            boolean depositoMatch = sottoContoContabilizzazione.getCategoriaContabileDeposito() == null;
            if (!depositoMatch) {
                if (categoriaContabileDeposito != null) {
                    depositoMatch = categoriaContabileDeposito
                            .equals(sottoContoContabilizzazione.getCategoriaContabileDeposito());
                } else {
                    depositoMatch = false;
                }
            }

            boolean sedeMatch = sottoContoContabilizzazione.getCategoriaContabileSedeMagazzino() == null;
            if (!sedeMatch) {
                if (categoriaContabileSedeMagazzino != null) {
                    sedeMatch = categoriaContabileSedeMagazzino
                            .equals(sottoContoContabilizzazione.getCategoriaContabileSedeMagazzino());
                } else {
                    sedeMatch = false;
                }
            }
            return articoloMatch && depositoMatch && sedeMatch;
        }
    }

    private class SottoContoContabilizzazioneComparator implements Comparator<SottoContoContabilizzazione> {

        @Override
        public int compare(SottoContoContabilizzazione sc1, SottoContoContabilizzazione sc2) {

            String pesoArticoloSc1 = "1";
            String pesoArticoloSc2 = "1";
            String pesoDepositoSc1 = "1";
            String pesoDepositoSc2 = "1";
            String pesoSedeEntitaSc1 = "1";
            String pesoSedeEntitaSc2 = "1";

            if (sc1.getCategoriaContabileArticolo() == null) {
                pesoArticoloSc1 = "0";
            }
            if (sc2.getCategoriaContabileArticolo() == null) {
                pesoArticoloSc2 = "0";
            }

            if (sc1.getCategoriaContabileDeposito() == null) {
                pesoDepositoSc1 = "0";
            }
            if (sc2.getCategoriaContabileDeposito() == null) {
                pesoDepositoSc2 = "0";
            }

            if (sc1.getCategoriaContabileSedeMagazzino() == null) {
                pesoSedeEntitaSc1 = "0";
            }
            if (sc2.getCategoriaContabileSedeMagazzino() == null) {
                pesoSedeEntitaSc2 = "0";
            }

            String peso1 = pesoSedeEntitaSc1 + pesoDepositoSc1 + pesoArticoloSc1;
            String peso2 = pesoSedeEntitaSc2 + pesoDepositoSc2 + pesoArticoloSc2;
            Integer index1 = pesi.indexOf(peso1);
            Integer index2 = pesi.indexOf(peso2);
            return index1.compareTo(index2);
        }
    }

    private static List<String> pesi = new ArrayList<String>();

    private List<SottoContoContabilizzazione> listSottoConti;

    private Map<String, SottoConto> mapSottoContiCache;

    /**
     * Costruttore.
     *
     * @param listSottoConti
     *            lista dei sottoconti
     */
    public SottoContiContabilizzazione(final List<SottoContoContabilizzazione> listSottoConti) {
        this.listSottoConti = listSottoConti;
        if (pesi.isEmpty()) {
            initPesi();
        }
        this.mapSottoContiCache = new HashMap<String, SottoConto>();
    }

    /**
     * @param categoriaContabileArticolo
     *            {@link CategoriaContabileArticolo}
     * @param categoriaContabileDeposito
     *            {@link CategoriaContabileDeposito}
     * @param categoriaContabileSedeMagazzino
     *            {@link CategoriaContabileSedeMagazzino}
     * @return sottoconto
     */
    public SottoConto getSottoConto(CategoriaContabileArticolo categoriaContabileArticolo,
            CategoriaContabileDeposito categoriaContabileDeposito,
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {

        StringBuilder chiave = new StringBuilder();
        chiave.append(categoriaContabileArticolo == null ? "null" : categoriaContabileArticolo.getId() + "-");
        chiave.append(categoriaContabileDeposito == null ? "null" : categoriaContabileDeposito.getId() + "-");
        chiave.append(categoriaContabileSedeMagazzino == null ? "null" : categoriaContabileSedeMagazzino.getId() + "-");

        if (!mapSottoContiCache.containsKey(chiave.toString())) {

            // Recupero tutte le combinazioni per l'articolo, il quale ha peso
            // maggiore rispetto alle
            // altri variabili
            Matcher<SottoContoContabilizzazione> matcher = new MatcherDepositoSedeArticolo(categoriaContabileArticolo,
                    categoriaContabileDeposito, categoriaContabileSedeMagazzino);
            EventList<SottoContoContabilizzazione> listSottoContiEventList = new BasicEventList<SottoContoContabilizzazione>();
            listSottoContiEventList.addAll(listSottoConti);

            // Filtro solamente le combinazioni che mi servono
            FilterList<SottoContoContabilizzazione> sottoContiFiltrati = new FilterList<SottoContoContabilizzazione>(
                    listSottoContiEventList, matcher);

            SortedList<SottoContoContabilizzazione> sottoContiOrdinati = new SortedList<SottoContoContabilizzazione>(
                    sottoContiFiltrati, new SottoContoContabilizzazioneComparator());

            if (!sottoContiOrdinati.isEmpty()) {
                mapSottoContiCache.put(chiave.toString(), sottoContiOrdinati.get(0).getSottoConto());
            } else {
                mapSottoContiCache.put(chiave.toString(), null);
            }
        }

        return mapSottoContiCache.get(chiave.toString());
    }

    /**
     * Inizializza i pesi utilizzati per il recupero del sottoconto.
     */
    private synchronized void initPesi() {
        pesi.add("111");
        pesi.add("011");
        pesi.add("101");
        pesi.add("001");

        pesi.add("110");
        pesi.add("010");
        pesi.add("100");
        pesi.add("000");
    }
}
