/**
 *
 */
package it.eurotn.panjea.ordini.rich.commands.navigationloaders;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.commands.navigationloaders.ArticoloLoaderObjectVisitor;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

import org.springframework.core.ReflectiveVisitorHelper;

/**
 * @author fattazzo
 *
 */
public class ArticoloAnalisiLoaderCommand extends OpenEditorLoaderActionCommand<ParametriRicercaMovimentazione> {

	private ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();
	private ArticoloLoaderObjectVisitor articoloVisitor = new ArticoloLoaderObjectVisitor();

	/**
	 * Costruttore.
	 *
	 * @param commandId
	 */
	public ArticoloAnalisiLoaderCommand() {
		super("ordiniArticoloAnalisiLoaderCommand");
	}

	/**
	 * Costruttore.
	 *
	 * @param commandId
	 *            id
	 */
	public ArticoloAnalisiLoaderCommand(final String commandId) {
		super(commandId);
	}

	@Override
	protected ParametriRicercaMovimentazione getObjectForOpenEditor(Object loaderObject) {
		// siccome il command è mappato sia su articolo sia su articolo lite perchè deve fare la stessa cosa,
		// controllo cosa mi arriva dai parametri
		Articolo articolo = (Articolo) visitorHelper.invokeVisit(articoloVisitor, loaderObject);

		ParametriRicercaMovimentazione parametri = new ParametriRicercaMovimentazione();
		parametri.setArticoloLite(articolo.getArticoloLite());
		Periodo periodo = new Periodo();
		periodo.setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
		parametri.setDataRegistrazione(periodo);
		parametri.setEffettuaRicerca(true);
		return parametri;
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Articolo.class, ArticoloLite.class };
	}

}
