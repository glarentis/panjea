package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GroupingList;
import ca.odell.glazedlists.matchers.Matcher;

public final class GiacenzaAttualeCalculator {

	/**
	 * Calcola la giacenza progressiva delle righe evasione.
	 * 
	 * @param righe
	 *            righe evasione
	 * @param articolo
	 *            articolo
	 * @param deposito
	 *            deposito
	 * @param mapGiacenze
	 *            mappa delle giacenze
	 */
	public static void calcola(List<RigaDistintaCarico> righe, final ArticoloLite articolo,
			final DepositoLite deposito, Map<DepositoLite, Map<ArticoloLite, Double>> mapGiacenze) {
		EventList<RigaDistintaCarico> eventList = new BasicEventList<RigaDistintaCarico>();
		eventList.addAll(righe);
		for (RigaDistintaCarico rigaEvasione : righe) {
			eventList.addAll(rigaEvasione.getRigheSostituzione());
		}

		// filtro le righe che contengono solo l'articolo e il deposito che mi
		// interessa
		FilterList<RigaDistintaCarico> filterList = new FilterList<RigaDistintaCarico>(eventList,
				new Matcher<RigaDistintaCarico>() {

					@Override
					public boolean matches(RigaDistintaCarico riga) {
						return (riga.getArticolo().equals(articolo) && riga.getDeposito().equals(deposito) ? true
								: false);
					}
				});

		// calcolo la quantità da evadere settata.
		Double qtaDaEvadereTotale = 0.0;
		for (RigaDistintaCarico rigaEvasione : filterList) {
			if (rigaEvasione.getQtaDaEvadere() != null) {
				qtaDaEvadereTotale = qtaDaEvadereTotale + rigaEvasione.getQtaDaEvadere();
			}
		}

		// giacenza per articolo e deposito
		Double qtaGiacenza = mapGiacenze.get(deposito).get(articolo);
		qtaGiacenza = (qtaGiacenza != null) ? qtaGiacenza : 0.0;

		Double qtaGiacenzaAttuale = qtaGiacenza - qtaDaEvadereTotale;

		// aggiorno le righe con i valori attuali
		for (RigaDistintaCarico rigaEvasione : filterList) {
			rigaEvasione.setQtaGiacenzaAttuale(qtaGiacenzaAttuale);
		}
	}

	/**
	 * Calcola la giacenza progressiva delle righe evasione.
	 * 
	 * @param righe
	 *            righe evasione
	 * @param mapGiacenze
	 *            mappa delle giacenze
	 */
	public static void calcola(List<RigaDistintaCarico> righe, Map<DepositoLite, Map<ArticoloLite, Double>> mapGiacenze) {

		EventList<RigaDistintaCarico> eventList = new BasicEventList<RigaDistintaCarico>();
		eventList.addAll(righe);

		for (RigaDistintaCarico rigaEvasione : righe) {
			eventList.addAll(rigaEvasione.getRigheSostituzione());
		}

		// raggruppo le righe per articolo e deposito
		GroupingList<RigaDistintaCarico> groupingList = new GroupingList<RigaDistintaCarico>(eventList,
				new Comparator<RigaDistintaCarico>() {

					@Override
					public int compare(RigaDistintaCarico riga0, RigaDistintaCarico riga1) {
						String chiave0 = riga0.getArticolo().getId() + "-" + riga0.getDeposito().getId();
						String chiave1 = riga1.getArticolo().getId() + "-" + riga1.getDeposito().getId();
						return chiave0.compareTo(chiave1);
					}
				});

		// ricalcolo la giacenza attuale per ogni gruppo
		for (List<RigaDistintaCarico> righeGruppo : groupingList) {
			if (!righeGruppo.isEmpty()) {
				ArticoloLite articoloLite = righeGruppo.get(0).getArticolo();
				DepositoLite depositoLite = righeGruppo.get(0).getDeposito();

				calcola(righeGruppo, articoloLite, depositoLite, mapGiacenze);
			}
		}
	}

	/**
	 * Costruttore.
	 */
	private GiacenzaAttualeCalculator() {
		super();
	}
}
