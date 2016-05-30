package it.eurotn.panjea.intra.rich.pages;

import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.binding.PeriodoBinder;
import it.eurotn.rich.binding.PeriodoBinding;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

public class RisultatiRicercaAreeIntraTablePage extends AbstractTablePageEditor<AreaContabile> {

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
			String message = "Ricreare le aree intra per i documenti selezionati?<br/>NB:Verrano ricreate le aree solamente per i documenti di magazzino";
			ConfirmationDialog confirmationDialog = new ConfirmationDialog(title, message) {

				@Override
				protected void onConfirm() {
					List<AreaContabile> righeSelezionate = getTable().getSelectedObjects();
					if (righeSelezionate.isEmpty()) {
						return;
					}
					List<Integer> idDocumenti = new ArrayList<Integer>();
					for (AreaContabile areaContabile : righeSelezionate) {
						idDocumenti.add(areaContabile.getDocumento().getId());
					}
					intraBD.generaAreeIntra(idDocumenti);
					getSearchCommand().execute();
					AreaContabile selectObject = getTable().getSelectedObject();
					if (selectObject != null) {
						update(null, selectObject);
					}
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
			List<AreaContabile> aree = intraBD.ricercaAreeContabiliConIntra(parametri);
			setRows(aree);
		}
	}

	private IIntraBD intraBD;
	private ParametriRicercaAreaIntra parametri;
	private ActionCommand searchCommand;

	/**
	 * Costruttore.
	 */
	protected RisultatiRicercaAreeIntraTablePage() {
		super("risultatiRicercaAreeIntraTablePage", new DefaultBeanTableModel<AreaContabile>(
				"risultatiRicercaAreeIntraTableModel", new String[] { "documento.entita", "documento.codice",
						"documento.tipoDocumento", "dataRegistrazione" }, AreaContabile.class));
		getTable().addSelectionObserver(this);
		parametri = new ParametriRicercaAreaIntra();
		getTable().setPropertyCommandExecutor(getOpenAreeDocumentoCommand());
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
				AreaContabile selectedObject = getTable().getSelectedObject();
				if (selectedObject != null) {
					command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, selectedObject.getDocumento()
							.getId());
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
	public boolean isDirty() {
		return false;
	}

	@Override
	public Collection<AreaContabile> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public Collection<AreaContabile> refreshTableData() {
		return null;
	}

	@Override
	public void saveState(Settings settings) {
		super.saveState(settings);
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

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);
		if (obj != null) {
			AreaContabile ac = (AreaContabile) obj;
			ParametriRicercaAreaIntra nuoviParametri = new ParametriRicercaAreaIntra();
			nuoviParametri.setDocumentoCorrente(ac.getDocumento());
			firePropertyChange(OBJECT_CHANGED, null, nuoviParametri);
		}
	}

}
