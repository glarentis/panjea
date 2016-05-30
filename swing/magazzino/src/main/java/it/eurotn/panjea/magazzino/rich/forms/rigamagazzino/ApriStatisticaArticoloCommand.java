package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.commands.navigationloaders.ArticoloLoaderObjectVisitor;
import it.eurotn.panjea.magazzino.rich.editors.articolo.StatisticaDialog;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.rich.factory.navigationloader.AbstractLoaderActionCommand;

import javax.swing.AbstractButton;

import org.springframework.core.ReflectiveVisitorHelper;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

public class ApriStatisticaArticoloCommand extends AbstractLoaderActionCommand {

	public static final String COMMAND_ID = "apriStatisticaArticoloCommand";
	public static final String PARAMETER_DEPOSITO = "parameter_deposito";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();
	private ArticoloLoaderObjectVisitor articoloVisitor = new ArticoloLoaderObjectVisitor();

	/**
	 * Costruttore.
	 */
	public ApriStatisticaArticoloCommand() {
		this(COMMAND_ID);

	}

	/**
	 * Costruttore.
	 *
	 * @param commandId
	 *            id
	 */
	public ApriStatisticaArticoloCommand(final String commandId) {
		super(commandId);
		this.setSecurityControllerId(commandId);
		RcpSupport.configure(this);
		magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {
		AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
		button.setFocusable(false);
		return button;
	}

	@Override
	protected void doExecuteCommand() {
		Object loaderObject = getParameter(PARAM_LOADER_OBJECT, null);
		if (loaderObject != null) {
			// nelle tabelle i dati che non ci sono dovrebbero essere nulli ma specialmente per le prime create ci sono
			// ancora tantissime colonne che contengono nuove istanze anzichè valori nulli quindi metto questo controllo
			// perchè controllarle tutte sarebbe impossibile.
			if (loaderObject instanceof IDefProperty && ((IDefProperty) loaderObject).isNew()) {
				return;
			}
			Articolo articolo = (Articolo) visitorHelper.invokeVisit(articoloVisitor, loaderObject);
			DepositoLite deposito = (DepositoLite) getParameter(PARAMETER_DEPOSITO);
			if (articolo != null) {
				StatisticheArticolo statisticheArticolo = magazzinoDocumentoBD.caricaStatisticheArticolo(articolo);
				StatisticaDialog dialog = new StatisticaDialog(statisticheArticolo, deposito,
						articolo.getArticoloLite(), magazzinoDocumentoBD);
				dialog.showDialog();
			}
		}
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { ArticoloLite.class, Articolo.class };
	}
}
