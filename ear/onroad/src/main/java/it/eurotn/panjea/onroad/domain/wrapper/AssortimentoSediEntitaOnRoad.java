package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.List;

public class AssortimentoSediEntitaOnRoad {

	private CodiceArticoloEntita codiceArticoloEntita;
	private List<SedeEntita> sedi;

	public AssortimentoSediEntitaOnRoad(CodiceArticoloEntita codiceArticoloEntita, SedeEntita sedeEntita) {
		super();
		this.codiceArticoloEntita = codiceArticoloEntita;
		addSede(sedeEntita);
	}

	public void addSede(SedeEntita sedeEntita) {
		if (sedi == null) {
			sedi = new ArrayList<SedeEntita>();
		}
		sedi.add(sedeEntita);
	}

	/**
	 * @return the articolo
	 */
	public Articolo getArticolo() {
		return codiceArticoloEntita.getArticolo();
	}

	public List<AssortimentoSedeCessionarioOnRoad> getAssortimentiSedi() {
		List<AssortimentoSedeCessionarioOnRoad> assorimentiSedi = new ArrayList<AssortimentoSedeCessionarioOnRoad>();
		for (SedeEntita sede : sedi) {
			AssortimentoSedeCessionarioOnRoad assortimentoSedeCessionarioOnRoad = new AssortimentoSedeCessionarioOnRoad(
					codiceArticoloEntita, sede);
			assorimentiSedi.add(assortimentoSedeCessionarioOnRoad);
		}

		List<AssortimentoSedeCessionarioOnRoad> assortimentiSediAggunte = new ArrayList<AssortimentoSedeCessionarioOnRoad>();
		for (SedeEntita sede : sedi) {
			for (AssortimentoSedeCessionarioOnRoad assortimentoSedeCessionarioOnRoad : assorimentiSedi) {
				AssortimentoSedeCessionarioOnRoad newAssortimento = new AssortimentoSedeCessionarioOnRoad();
				PanjeaEJBUtil.copyProperties(newAssortimento, assortimentoSedeCessionarioOnRoad);
				newAssortimento.setSedeEntita(sede);
				assortimentiSediAggunte.add(newAssortimento);
			}
		}
		assorimentiSedi.addAll(assortimentiSediAggunte);
		List<AssortimentoSedeCessionarioOnRoad> assorimentiFinali = new ArrayList<AssortimentoSedeCessionarioOnRoad>();
		for (AssortimentoSedeCessionarioOnRoad assortSede : assorimentiSedi) {
			if (assortSede.getSedeCliente() != null && assortSede.getSedeFornitore() != null) {
				assorimentiFinali.add(assortSede);
			}
		}

		return assorimentiFinali;
	}

}
