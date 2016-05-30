package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.rich.bd.IValutaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class ValutaAziendaSearchObject extends AbstractSearchObject {

	private IValutaBD valutaBD = null;

	/**
	 * Costruttore di default.
	 */
	public ValutaAziendaSearchObject() {
		super("valutaAziendaSearchObject");
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<ValutaAzienda> valute = valutaBD.caricaValuteAzienda();

		List<ValutaAzienda> valuteFiltered = new ArrayList<ValutaAzienda>();
		for (ValutaAzienda valutaAzienda : valute) {
			if (valueSearch == null
					|| valutaAzienda.getCodiceValuta().toLowerCase()
							.startsWith(valueSearch.toLowerCase().replaceAll("%", ""))) {
				valuteFiltered.add(valutaAzienda);
			}
		}
		return valuteFiltered;
	}

	/**
	 * @param valutaBD
	 *            the valutaBD to set
	 */
	public void setValutaBD(IValutaBD valutaBD) {
		this.valutaBD = valutaBD;
	}

}
