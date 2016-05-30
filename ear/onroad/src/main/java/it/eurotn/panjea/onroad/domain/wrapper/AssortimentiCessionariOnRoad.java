package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssortimentiCessionariOnRoad {

	private Map<String, AssortimentoSedeCessionarioOnRoad> assortimentiSediCessionario = null;
	private List<AssortimentoSedeCessionarioOnRoad> assortimentiSedi = null;

	/**
	 * Costruttore.
	 */
	public AssortimentiCessionariOnRoad() {
		super();
		assortimentiSedi = new ArrayList<AssortimentoSedeCessionarioOnRoad>();
	}

	/**
	 * 
	 * @param codiciArticoloSedeEntita
	 *            .
	 */
	public void addArticoliSedeEntitaContoTerzi(List<Object[]> codiciArticoloSedeEntita) {
		Map<Integer, AssortimentoSediEntitaOnRoad> assortimentiCessioni = new HashMap<Integer, AssortimentoSediEntitaOnRoad>();

		// raggruppo per articolo e mi trovo quindi articolo - lista di sedi
		for (Object[] codiceArticoloSedeEntita : codiciArticoloSedeEntita) {
			CodiceArticoloEntita codiceArticoloEntita = (CodiceArticoloEntita) codiceArticoloSedeEntita[0];
			SedeEntita sedeEntita = (SedeEntita) codiceArticoloSedeEntita[1];

			AssortimentoSediEntitaOnRoad assortimentoClienteCessionarioOnRoad = new AssortimentoSediEntitaOnRoad(
					codiceArticoloEntita, sedeEntita);
			Integer key = assortimentoClienteCessionarioOnRoad.getArticolo().getId();

			if (!assortimentiCessioni.containsKey(key)) {
				assortimentiCessioni.put(key, assortimentoClienteCessionarioOnRoad);
			} else {
				AssortimentoSediEntitaOnRoad assortimentoClienteCessionarioCorrente = assortimentiCessioni.get(key);
				assortimentoClienteCessionarioCorrente.addSede(sedeEntita);
			}
		}

		List<AssortimentoSedeCessionarioOnRoad> assortimentiSediTmp = new ArrayList<AssortimentoSedeCessionarioOnRoad>();
		for (AssortimentoSediEntitaOnRoad assortimentoSedi : assortimentiCessioni.values()) {
			assortimentiSediTmp.addAll(assortimentoSedi.getAssortimentiSedi());
		}
		assortimentiSediCessionario = new HashMap<String, AssortimentoSedeCessionarioOnRoad>();
		for (AssortimentoSedeCessionarioOnRoad assortimento : assortimentiSediTmp) {
			assortimentiSediCessionario.put(assortimento.getMapKeyAssort(), assortimento);
		}
	}

	/**
	 * 
	 * @param sediRifatturazioneAssociate
	 *            sedi Rifatturazione associate
	 */
	public void addSediRifatturazione(List<SedeMagazzino> sediRifatturazioneAssociate) {
		Map<String, List<SedeEntita>> sediPerSedeRifatturazione = new HashMap<String, List<SedeEntita>>();
		for (SedeMagazzino sm : sediRifatturazioneAssociate) {
			String codiceEntita = sm.getSedePerRifatturazione().getSedeEntita().getEntita().getCodice() + "";
			String codiceDestinazione = ((!sm.getSedePerRifatturazione().getSedeEntita().getTipoSede()
					.isSedePrincipale()) ? sm.getSedePerRifatturazione().getSedeEntita().getCodice() : (sm
					.getSedePerRifatturazione().getSedeEntita().getEntita().getCodice() + ""));

			String key = codiceEntita + "#" + codiceDestinazione;

			if (!sediPerSedeRifatturazione.containsKey(key)) {
				List<SedeEntita> sedi = new ArrayList<SedeEntita>();
				sedi.add(sm.getSedeEntita());
				sediPerSedeRifatturazione.put(key, sedi);
			} else {
				List<SedeEntita> list = sediPerSedeRifatturazione.get(key);
				list.add(sm.getSedeEntita());
				sediPerSedeRifatturazione.put(key, list);
			}
		}

		List<AssortimentoSedeCessionarioOnRoad> assortimentiToRemove = new ArrayList<AssortimentoSedeCessionarioOnRoad>();
		List<AssortimentoSedeCessionarioOnRoad> assortimentiDaRifatturazioneDaAggiungere = new ArrayList<AssortimentoSedeCessionarioOnRoad>();
		for (AssortimentoSedeCessionarioOnRoad assortSede : assortimentiSediCessionario.values()) {
			String codiceEntita = assortSede.getSedeCliente().getEntita().getCodice() + "";
			String codiceDest = assortSede.getCodiceDestinatario();
			String key = codiceEntita + "#" + codiceDest;

			if (sediPerSedeRifatturazione.containsKey(key)) {
				assortimentiToRemove.add(assortSede);
				List<SedeEntita> list = sediPerSedeRifatturazione.get(key);
				for (SedeEntita sedeEntita : list) {
					AssortimentoSedeCessionarioOnRoad newAssortimento = new AssortimentoSedeCessionarioOnRoad();
					PanjeaEJBUtil.copyProperties(newAssortimento, assortSede);
					newAssortimento.setSedeEntita(sedeEntita);

					assortimentiDaRifatturazioneDaAggiungere.add(newAssortimento);
				}
			}
		}

		for (AssortimentoSedeCessionarioOnRoad assortToRemove : assortimentiToRemove) {
			assortimentiSediCessionario.remove(assortToRemove);
		}
		assortimentiSedi.addAll(assortimentiSediCessionario.values());
		assortimentiSedi.addAll(assortimentiDaRifatturazioneDaAggiungere);
	}

	/**
	 * @return List<AssortimentoSedeCessionarioOnRoad>
	 */
	public List<AssortimentoSedeCessionarioOnRoad> getAssortimentiSedi() {
		return assortimentiSedi;
	}
}
