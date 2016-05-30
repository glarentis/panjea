package it.eurotn.panjea.partite.rich.search;

import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class CategoriaRateSearchObject extends AbstractSearchObject {
	private IPartiteBD partiteBD;

	/**
	 * Costruttore.
	 */
	public CategoriaRateSearchObject() {
		super("categoriaRateSearchObject");
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {

		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return partiteBD.caricaCategorieRata(fieldSearch, valueSearch);
	}

	/**
	 * @param partiteBD
	 *            The partiteBD to set.
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
