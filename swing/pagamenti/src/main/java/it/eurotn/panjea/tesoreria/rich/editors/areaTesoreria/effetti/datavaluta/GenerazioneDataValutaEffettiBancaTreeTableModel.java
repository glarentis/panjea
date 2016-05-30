package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti.RaggruppamentoEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ca.odell.glazedlists.GroupingList;

import com.jidesoft.grid.ExpandableRow;

public class GenerazioneDataValutaEffettiBancaTreeTableModel extends GenerazioneDataValutaEffettiTreeTableModel {

	/**
	 *
	 * @param areaEffetti
	 *            areaEffetti contenente gli effetti per assegnare la data valuta
	 */
	public GenerazioneDataValutaEffettiBancaTreeTableModel(final AreaEffetti areaEffetti) {
		super(areaEffetti);
	}

	@Override
	public void createRow(RaggruppamentoEffetti nuovoRaggruppamento) {
		if (this.raggruppamento == nuovoRaggruppamento && !dirty) {
			return;
		}

		((ExpandableRow) getRoot()).removeAllChildren();

		this.raggruppamento = nuovoRaggruppamento;
		dirty = false;

		GroupingList<Effetto> effettiRaggruppati = areaEffetti.getEffettiRaggrupati(nuovoRaggruppamento);

		Map<Date, List<Effetto>> effettiPerDataValutaBancaAzienda = new TreeMap<Date, List<Effetto>>();
		Map<Date, List<Effetto>> effettiPerDataValutaBancaAltra = new TreeMap<Date, List<Effetto>>();

		// creo la mappa
		Banca bancaAzienda = areaEffetti.getDocumento().getRapportoBancarioAzienda().getBanca();

		for (List<Effetto> effettoData : effettiRaggruppati) {
			Date key = effettoData.get(0).getDataValuta();
			Banca bancaEntita;
			for (Effetto effetto : effettoData) {
				bancaEntita = null;
				if (effetto.getRata().getRapportoBancarioEntita() != null) {
					bancaEntita = effetto.getRata().getRapportoBancarioEntita().getBanca();
				}
				if (bancaAzienda.equals(bancaEntita)) {
					List<Effetto> listaEffetti = effettiPerDataValutaBancaAzienda.get(key);
					if (listaEffetti == null) {
						listaEffetti = new ArrayList<Effetto>();
						effettiPerDataValutaBancaAzienda.put(key, listaEffetti);
					}
					listaEffetti.add(effetto);
					effettiPerDataValutaBancaAzienda.put(key, listaEffetti);
				} else {
					List<Effetto> listaEffetti = effettiPerDataValutaBancaAltra.get(key);
					if (listaEffetti == null) {
						listaEffetti = new ArrayList<Effetto>();
						effettiPerDataValutaBancaAltra.put(key, listaEffetti);
					}
					listaEffetti.add(effetto);
					effettiPerDataValutaBancaAltra.put(key, listaEffetti);
				}
			}
		}

		BancaRow bancaAziendaRow = new BancaRow(" "
				+ areaEffetti.getDocumento().getRapportoBancarioAzienda().getDescrizione());
		BancaRow bancaAltreRow = new BancaRow("ALTRA BANCA");

		// crea il tree node dalla mappa ordinata per data valuta.Form
		for (Entry<Date, List<Effetto>> dataValuta : effettiPerDataValutaBancaAzienda.entrySet()) {
			DataValutaRow dataValutaRow = new DataValutaRow(dataValuta.getValue());
			bancaAziendaRow.addChild(dataValutaRow);
		}
		for (Entry<Date, List<Effetto>> dataValuta : effettiPerDataValutaBancaAltra.entrySet()) {
			DataValutaRow dataValutaRow = new DataValutaRow(dataValuta.getValue());
			bancaAltreRow.addChild(dataValutaRow);
		}
		addRow(bancaAziendaRow);
		addRow(bancaAltreRow);
	}
}
