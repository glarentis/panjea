package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;

public class AreeMagazzinoTipoDocumentoGroup extends ArrayList<List<AreaMagazzinoFatturazione>> {

	private static final long serialVersionUID = -4239325139200879116L;

	/**
	 * Raggruppa la lista di areeMagazzino per Entità e li aggiunge a se stessa.
	 * 
	 * @param areeMagazzino
	 *            lista di aree da raggruppare
	 */
	public AreeMagazzinoTipoDocumentoGroup(final Collection<AreaMagazzinoFatturazione> areeMagazzino) {

		EventList<AreaMagazzinoFatturazione> eventList = new BasicEventList<AreaMagazzinoFatturazione>();
		eventList.addAll(areeMagazzino);

		Comparator<AreaMagazzinoFatturazione> comparator = new Comparator<AreaMagazzinoFatturazione>() {

			@Override
			public int compare(AreaMagazzinoFatturazione o1, AreaMagazzinoFatturazione o2) {
				return o1.getTipoAreaMagazzinoPerFatturazione().getTipoDocumento()
						.compareTo(o2.getTipoAreaMagazzinoPerFatturazione().getTipoDocumento());
			}
		};
		GroupingList<AreaMagazzinoFatturazione> areeGroup = new GroupingList<AreaMagazzinoFatturazione>(eventList,
				comparator);
		this.addAll(areeGroup);
	}
}
