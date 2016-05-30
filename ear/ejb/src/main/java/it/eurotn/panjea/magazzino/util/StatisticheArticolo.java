package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Contiene le statistiche articolo per i vari depositi.
 *
 * @author giangi
 * @version 1.0, 14/set/2010
 */
public class StatisticheArticolo implements Serializable {
    private static final long serialVersionUID = 589287217145255349L;

    private Map<String, List<StatisticaArticolo>> mapStatistiche;

    /**
     * @uml.property name="idArticolo"
     */
    private int idArticolo;

    /**
     * Costruttore.
     *
     * @param idArticolo
     *            utilizzato per indicare per quale articolo sono le statistiche
     * @param statistiche
     *            lista di statistiche articolo.
     * @param disponibilita
     *            disp. dell'articolo considerando i fabbisogni e i carichi
     * @param indiciRotazione
     *            indici rotazione per i vari articoli
     */
    public StatisticheArticolo(final int idArticolo, final List<StatisticaArticolo> statistiche,
            final Map<String, Map<Integer, List<DisponibilitaArticolo>>> disponibilita,
            final Map<Integer, IndiceGiacenzaArticolo> indiciRotazione) {
        this.idArticolo = idArticolo;
        mapStatistiche = new TreeMap<String, List<StatisticaArticolo>>();
        StatisticaArticolo statisticaTotale = creaStatisticaTotale("");
        for (StatisticaArticolo statisticaArticolo : statistiche) {
            if (disponibilita.containsKey(statisticaArticolo.getDepositoLite().getCodice())) {
                statisticaArticolo.setDisponibilita(
                        disponibilita.get(statisticaArticolo.getDepositoLite().getCodice()).get(idArticolo));
            }
            if (indiciRotazione.containsKey(statisticaArticolo.getDepositoLite().getId())) {
                statisticaArticolo
                        .setIndiceRotazione(indiciRotazione.get(statisticaArticolo.getDepositoLite().getId()));
            }
            String codiceTipoDeposito = statisticaArticolo.getDepositoLite().getTipoDeposito().getCodice();
            List<StatisticaArticolo> statisticheTipoDeposito = null;
            StatisticaArticolo statisticaTotaleTipoDeposito = null;
            if (!mapStatistiche.containsKey(codiceTipoDeposito)) {
                statisticheTipoDeposito = new ArrayList<StatisticaArticolo>();
                // Aggiungo la statistica per il deposito TOTALE
                statisticaTotaleTipoDeposito = creaStatisticaTotale(codiceTipoDeposito);
                statisticheTipoDeposito.add(statisticaTotaleTipoDeposito);
            } else {
                statisticheTipoDeposito = mapStatistiche.get(codiceTipoDeposito);
                statisticaTotaleTipoDeposito = statisticheTipoDeposito.get(0);
            }
            statisticaTotaleTipoDeposito.add(statisticaArticolo);
            statisticheTipoDeposito.add(statisticaArticolo);
            mapStatistiche.put(codiceTipoDeposito, statisticheTipoDeposito);
            statisticaTotale.add(statisticaArticolo);
        }
        List<StatisticaArticolo> statisticheTotale = new ArrayList<StatisticaArticolo>();
        statisticheTotale.add(statisticaTotale);
        mapStatistiche.put(StatisticaArticolo.TUTTI_KEY, statisticheTotale);
    }

    /**
     *
     * @param codiceTipoDeposito
     *            codice della tipologia del deposito
     * @return statistica articolo per il totale dei depositi
     */
    private StatisticaArticolo creaStatisticaTotale(String codiceTipoDeposito) {
        StatisticaArticolo statisticaArticolo = new StatisticaArticolo();
        statisticaArticolo.setCodiceDeposito(StatisticaArticolo.TUTTI_KEY);
        statisticaArticolo.setDescrizioneDeposito(
                new StringBuilder("Totale per tutti i depositi ").append(codiceTipoDeposito.toLowerCase()).toString());
        return statisticaArticolo;
    }

    /**
     * @return Returns the idArticolo.
     * @uml.property name="idArticolo"
     */
    public int getIdArticolo() {
        return idArticolo;
    }

    /**
     *
     * @return mappa in sola lettura contenente le statistiche raggruppate per tipologia deposito.
     */
    public Map<String, List<StatisticaArticolo>> getStatistichePerTipologiaDeposito() {
        return Collections.unmodifiableMap(mapStatistiche);
    }
}
