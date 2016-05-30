package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class CategoriaContabileArticoloSearchObject extends AbstractSearchObject {
	public static final String PAGE_ID = "categoriaContabileArticoloSearchObject";
	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 */
	public CategoriaContabileArticoloSearchObject() {
		super(PAGE_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoContabilizzazioneBD.caricaCategorieContabileArticolo(fieldSearch, valueSearch);
	}

	/**
	 * @param magazzinoContabilizzazioneBD
	 *            The magazzinoContabilizzazioneBD to set.
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

}
