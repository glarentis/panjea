package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class CategoriaContabileDepositoSearchObject extends AbstractSearchObject {

	public static final String PAGE_ID = "categoriaContabileDepositoSearchObject";

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 */
	public CategoriaContabileDepositoSearchObject() {
		super(PAGE_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoContabilizzazioneBD.caricaCategorieContabileDeposito(fieldSearch, valueSearch);
	}

	/**
	 * @param magazzinoContabilizzazioneBD
	 *            The magazzinoContabilizzazioneBD to set.
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

}
