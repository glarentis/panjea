package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.clone.CloneArticoloPage;
import it.eurotn.panjea.magazzino.rich.editors.articolo.clone.CloneArticoloParameter;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class CopiaArticoloCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "copiaArticoloCommand";
	public static final String PARAM_ARTICOLO = "paramArticolo";

	private Articolo cloneArticolo;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 *
	 * @param magazzinoAnagraficaBD
	 *            bd anagrafica magazzino
	 */
	public CopiaArticoloCommand(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super(COMMAND_ID);
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		final Articolo articolo = (Articolo) getParameter(PARAM_ARTICOLO, null);

		if (articolo != null && !articolo.isNew()) {
			CloneArticoloParameter parameter = new CloneArticoloParameter(articolo);
			final CloneArticoloPage page = new CloneArticoloPage(parameter);
			DefaultTitledPageApplicationDialog dialog = new DefaultTitledPageApplicationDialog(parameter, null, page) {
				@Override
				protected boolean onFinish() {
					CloneArticoloParameter parametriCopiaArticolo = (CloneArticoloParameter) page.getForm()
							.getFormObject();
					if (parametriCopiaArticolo.getNuovoCodice().isEmpty()) {
						Message message = new DefaultMessage("Codice articolo non può essere vuoto", Severity.ERROR);
						MessageDialog alert = new MessageDialog("Dati non correttti", message);
						alert.showDialog();
						return false;
					}
					if (parametriCopiaArticolo.getNuovaDescrizione().isEmpty()) {
						Message message = new DefaultMessage("Descrizione articolo non può essere vuoto",
								Severity.ERROR);
						MessageDialog alert = new MessageDialog("Dati non correttti", message);
						alert.showDialog();
						return false;
					}
					cloneArticolo = magazzinoAnagraficaBD.cloneArticolo(articolo.getId(),
							parametriCopiaArticolo.getNuovoCodice(), parametriCopiaArticolo.getNuovaDescrizione(),
							parametriCopiaArticolo.isCopyDistinta(), parametriCopiaArticolo.getAttributiArticolo(),
							parametriCopiaArticolo.isCopyListino(), parametriCopiaArticolo.isAzzeraPrezziListino());
					return true;
				}
			};
			dialog.showDialog();
		}
	}

	/**
	 * @return Returns the cloneArticolo.
	 */
	public Articolo getArticoloCopiato() {
		return cloneArticolo;
	}

}
