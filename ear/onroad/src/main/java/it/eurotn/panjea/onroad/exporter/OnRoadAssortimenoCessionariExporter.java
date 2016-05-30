package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.onroad.domain.wrapper.AssortimentiCessionariOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.AssortimentoSedeCessionarioOnRoad;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.ejb.EJB;
import javax.persistence.Query;

public abstract class OnRoadAssortimenoCessionariExporter extends AbstractDataExporter {

	@EJB
	private PanjeaDAO panjeaDAO;

	/**
	 * Carica gli articoli/sede in assortimento incrociando gli articoli conto terzi con le sedi rifatturazione.
	 * 
	 * @return List<AssortimentoSedeCessionarioOnRoad>
	 */
	@SuppressWarnings("unchecked")
	public List<AssortimentoSedeCessionarioOnRoad> caricaAssortimentiSedi() {
		StringBuilder query = new StringBuilder("select codArtEnt,se");
		query.append(" from CodiceArticoloEntita codArtEnt,SedeEntita se ");
		query.append(" where codArtEnt.consegnaContoTerzi=true ");
		query.append(" and se.entita.id=codArtEnt.entita.id ");
		query.append(" and se.tipoSede.sedePrincipale=true");

		Query queryFornitori = panjeaDAO.prepareQuery(query.toString());
		List<Object[]> articoliSedeEntitaContoTerzi = queryFornitori.getResultList();

		Query queryRifatt = panjeaDAO
				.prepareQuery("select sm from SedeMagazzino sm where sm.sedePerRifatturazione != null");
		List<SedeMagazzino> sediRifatturazioneAssociate = queryRifatt.getResultList();

		AssortimentiCessionariOnRoad assortimentiCessioniOnRoad = new AssortimentiCessionariOnRoad();
		assortimentiCessioniOnRoad.addArticoliSedeEntitaContoTerzi(articoliSedeEntitaContoTerzi);
		assortimentiCessioniOnRoad.addSediRifatturazione(sediRifatturazioneAssociate);

		List<AssortimentoSedeCessionarioOnRoad> assortimentiSede = assortimentiCessioniOnRoad.getAssortimentiSedi();

		return assortimentiSede;
	}
}
