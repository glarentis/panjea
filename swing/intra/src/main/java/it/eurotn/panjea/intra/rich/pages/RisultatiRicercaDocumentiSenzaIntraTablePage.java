package it.eurotn.panjea.intra.rich.pages;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.binding.PeriodoBinder;
import it.eurotn.rich.binding.PeriodoBinding;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

public class RisultatiRicercaDocumentiSenzaIntraTablePage extends AbstractTablePageEditor<Documento> {

	private class RicreaAreaIntraCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public RicreaAreaIntraCommand() {
			super("ricreaAreaIntraCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			String title = "Conferma";
			String message = "Crea le aree intra per i documenti selezionati?<br/>NB:Verrano create le aree solamente per i documenti di magazzino";
			ConfirmationDialog confirmationDialog = new ConfirmationDialog(title, message) {

				@Override
				protected void onConfirm() {
					List<Documento> righeSelezionate = getTable().getSelectedObjects();
					if (righeSelezionate.isEmpty()) {
						return;
					}
					List<Integer> idDocumenti = new ArrayList<Integer>();
					for (Documento documento : righeSelezionate) {
						idDocumenti.add(documento.getId());
					}
					intraBD.generaAreeIntra(idDocumenti);
					RisultatiRicercaDocumentiSenzaIntraTablePage.this.loadData();
				}
			};
			confirmationDialog.setPreferredSize(new Dimension(250, 150));
			confirmationDialog.setCloseAction(CloseAction.HIDE);
			confirmationDialog.setResizable(false);
			confirmationDialog.showDialog();
		}

	}

	private class SearchCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public SearchCommand() {
			super("searchCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<Documento> documenti = intraBD.caricaDocumentiSenzaIntra(parametri);
			setRows(documenti);
		}

	}

	private IIntraBD intraBD;
	private ParametriRicercaAreaIntra parametri;
	private ActionCommand searchCommand;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaDocumentiSenzaIntraTablePage() {
		super("risultatiRicercaDocumentiSenzaIntraTablePage", new String[] { "entita", "codice", "tipoDocumento",
				"dataDocumento" }, Documento.class);
		getTable().setPropertyCommandExecutor(getOpenAreeDocumentoCommand());
		parametri = new ParametriRicercaAreaIntra();
		AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
		parametri.setCodiceNazioneAzienda(aziendaCorrente.getNazione().getCodice());
	}

	@Override
	public AbstractCommand[] getCommands() {
		return null;
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new HorizontalLayout());
		PeriodoBinder periodoBinder = new PeriodoBinder();
		FormModel formModel = PanjeaFormModelHelper.createFormModel(parametri, false,
				"parametriRicercaAreeIntraPageModel");
		PeriodoBinding periodoBinding = (PeriodoBinding) periodoBinder.bind(formModel, "periodoRegistrazione",
				new HashMap<Object, Object>());
		headerPanel.add(periodoBinding.getControl(), BorderLayout.CENTER);
		buttonPanel.add(getSearchCommand().createButton());
		buttonPanel.add(new RicreaAreaIntraCommand().createButton());
		headerPanel.add(buttonPanel, BorderLayout.EAST);
		return headerPanel;
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	/**
	 * @return command per aprire le aree documento
	 */
	private ActionCommand getOpenAreeDocumentoCommand() {
		ActionCommand command = new OpenAreeDocumentoCommand();
		command.addCommandInterceptor(new ActionCommandInterceptor() {

			@Override
			public void postExecution(ActionCommand arg0) {
			}

			@Override
			public boolean preExecution(ActionCommand command) {
				Documento selectedObject = getTable().getSelectedObject();
				if (selectedObject != null) {
					command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, selectedObject.getId());
				}
				return selectedObject != null;
			}
		});
		return command;
	}

	/**
	 * @return command per la ricerca
	 */
	private ActionCommand getSearchCommand() {
		if (searchCommand == null) {
			searchCommand = new SearchCommand();
		}
		return searchCommand;
	}

	@Override
	public Collection<Documento> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public Collection<Documento> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}
}
