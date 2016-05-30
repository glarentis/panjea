/**
 *
 */
package it.eurotn.panjea.magazzino.rich.commands.navigationloaders;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

/**
 * @author fattazzo
 *
 */
public class ArticoloRicercaAnalisiLoaderCommand extends ArticoloAnalisiLoaderCommand {

	/**
	 * Costruttore.
	 *
	 * @param commandId
	 */
	public ArticoloRicercaAnalisiLoaderCommand() {
		super("articoloAnalisiLoaderCommand");
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Articolo.class, ArticoloLite.class, String.class };
	}

}
