/**
 *
 */
package it.eurotn.panjea.magazzino.rich.commands.navigationloaders;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

/**
 * @author fattazzo
 *
 */
public class ContrattoLoaderCommand extends OpenEditorLoaderActionCommand<Contratto> {

	private IContrattoBD contrattoBD;

	/**
	 * Costruttore.
	 */
	public ContrattoLoaderCommand() {
		super("contrattoLoaderCommand");
	}

	@Override
	protected Contratto getObjectForOpenEditor(Object loaderObject) {

		Contratto contratto = contrattoBD.caricaContratto((Contratto) loaderObject, true);

		return contratto;
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Contratto.class };
	}

	/**
	 * @param contrattoBD
	 *            the contrattoBD to set
	 */
	public void setContrattoBD(IContrattoBD contrattoBD) {
		this.contrattoBD = contrattoBD;
	}

}
