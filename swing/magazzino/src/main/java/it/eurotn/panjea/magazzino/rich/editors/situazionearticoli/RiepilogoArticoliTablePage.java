package it.eurotn.panjea.magazzino.rich.editors.situazionearticoli;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoloCategoriaDTO;
import it.eurotn.panjea.riepilogo.util.RiepilogoArticoloDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.util.Collection;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class RiepilogoArticoliTablePage extends AbstractTablePageEditor<RiepilogoArticoloDTO> {

	private class OpenArticoloCommand implements ActionCommandExecutor {

		@Override
		public void execute() {
			RiepilogoArticoloDTO riepilogoArticoloDTO = getTable().getSelectedObject();

			if (riepilogoArticoloDTO != null) {
				Articolo articolo = magazzinoAnagraficaBD.caricaArticolo(riepilogoArticoloDTO.getArticolo()
						.creaProxyArticolo(), true);
				ArticoloCategoriaDTO articoloCategoriaDTO = new ArticoloCategoriaDTO(articolo, articolo.getCategoria());
				LifecycleApplicationEvent event = new OpenEditorEvent(articoloCategoriaDTO);
				// TIPS:Alla prima apertura non si capisce perchè ma l'editor degli
				// articoli pagina ArticoliTablePage)
				// non seleziona l'articolo me visualizza il primo della categoria.
				// Per ovviare lancio due volte l'evento, il primo apre l'editor ed
				// il secondo seleziona correttamente
				// l'articolo.
				// Controllare perchè succede ed in case togliere il secondo evento.
				Application.instance().getApplicationContext().publishEvent(event);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}

	}

	/**
	 * Comando per la sostituzione del codice iva degli articoli. Il comando qui è provvisorio, per questo non uso
	 * properties per il testo del command e form.
	 *
	 * @author fattazzo
	 *
	 */
	private class SostituzioneCodiceIvaCommand extends ApplicationWindowAwareCommand {

		@Override
		protected void doExecuteCommand() {
			PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
					new SostituzioneCodiceIVAForm(), null) {

				@Override
				protected String getTitle() {
					return "Sostituzione codice iva";
				}

				@Override
				protected boolean isMessagePaneVisible() {
					return false;
				}

				@Override
				protected boolean onFinish() {
					SostituzioneCodiceIvaPM sostituzioneCodiceIvaPM = (SostituzioneCodiceIvaPM) ((FormBackedDialogPage) getDialogPage())
							.getBackingFormPage().getFormObject();

					if (sostituzioneCodiceIvaPM.isValid()) {
						getCancelCommand().setEnabled(false);
						magazzinoAnagraficaBD.cambiaCodiceIVA(sostituzioneCodiceIvaPM.getCodiceIvaDaSostituire(),
								sostituzioneCodiceIvaPM.getNuovoCodiceIva());
						RiepilogoArticoliTablePage.this.refreshData();
					} else {
						new MessageDialog("ATTENZIONE",
								"Dati non corretti. I codici iva devono essere presenti e non uguali").showDialog();
					}
					return sostituzioneCodiceIvaPM.isValid();
				}
			};
			dialog.showDialog();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setText("Sostituzione codice iva");
			button.setIcon(RcpSupport.getIcon(CodiceIva.class.getName()));
			button.setName("SostituzioneCodiceIvaButton");
		}

	}

	public static final String PAGE_ID = "riepilogoArticoliTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private SostituzioneCodiceIvaCommand sostituzioneCodiceIvaCommand;

	/**
	 *
	 * Costruttore.
	 */
	protected RiepilogoArticoliTablePage() {
		super(PAGE_ID, new String[] { "categoria", "articolo", "barCode", "tipoArticolo", "articoloLibero",
				"codiceIva", "ivaAlternativa", "categoriaContabileArticolo", "unitaMisura", "unitaMisuraQtaMagazzino",
				"formulaTrasformazioneQta", "formulaTrasformazioneQtaMagazzino", "numeroDecimaliQta",
				"numeroDecimaliPrezzo", "provenienzaPrezzoArticolo", "categoriaCommercialeArticolo",
				"categoriaCommercialeArticolo2", "codiceInterno" }, RiepilogoArticoloDTO.class);
		getTable().setPropertyCommandExecutor(new OpenArticoloCommand());
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getSostituzioneCodiceIvaCommand() };
	}

	/**
	 * @return the sostituzioneCodiceIvaCommand
	 */
	public SostituzioneCodiceIvaCommand getSostituzioneCodiceIvaCommand() {
		if (sostituzioneCodiceIvaCommand == null) {
			sostituzioneCodiceIvaCommand = new SostituzioneCodiceIvaCommand();
		}

		return sostituzioneCodiceIvaCommand;
	}

	@Override
	public Collection<RiepilogoArticoloDTO> loadTableData() {
		return magazzinoAnagraficaBD.caricaRiepilogoArticoli();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RiepilogoArticoloDTO> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
