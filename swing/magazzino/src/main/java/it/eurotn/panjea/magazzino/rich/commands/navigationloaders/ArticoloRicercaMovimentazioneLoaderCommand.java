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
public class ArticoloRicercaMovimentazioneLoaderCommand extends ArticoloMovimentazioneLoaderCommand {

	/**
	 * Costruttore.
	 *
	 * @param commandId
	 */
	public ArticoloRicercaMovimentazioneLoaderCommand() {
		super("articoloMovimentazioneLoaderCommand");
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Articolo.class, ArticoloLite.class, String.class };
	}

}
