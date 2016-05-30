package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.forms.acconto.ParametriRicercaAreaAccontoForm;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EditFrame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

public class RisultatiRicercaAreaAccontoTablePage extends AbstractTablePageEditor<AreaAcconto> {

	private class ParametriAccontoChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ParametriRicercaAcconti parametriRicercaAcconti = (ParametriRicercaAcconti) parametriAreaAccontoForm
					.getFormObject();
			parametriRicercaAcconti.setEffettuaRicerca(true);
			RisultatiRicercaAreaAccontoTablePage.this.parametriRicerca = parametriRicercaAcconti;
			RisultatiRicercaAreaAccontoTablePage.this.areaAccontoWrapper = null;
			loadData();
		}
	}

	public static final String PAGE_ID = "risultatiRicercaDocumentoAccontoTablePage";
	private ParametriRicercaAcconti parametriRicerca;

	private AreaAccontoWrapper areaAccontoWrapper;
	private ITesoreriaBD tesoreriaBD;

	private UtilizzaAccontoCommand utilizzaAccontoCommand;

	private ParametriRicercaAreaAccontoForm parametriAreaAccontoForm = null;

	private ParametriAccontoChangeListener parametriAccontoChangeListener = null;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaAreaAccontoTablePage() {
		super(PAGE_ID, new RisultatiRicercaAreaAccontoTableModel(PAGE_ID));
		this.parametriRicerca = new ParametriRicercaAcconti();

		this.getTable().setDelayForSelection(400);
		getTable().addSelectionObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				if (getTable() != null) {
					// se ho un accontoIVA (x ora se scelgo tipo entita azienda ho solo un acconto iva) non devo poter
					// utilizzare l'acconto
					getUtilizzaAccontoCommand()
							.setVisible(!TipoEntita.AZIENDA.equals(parametriRicerca.getTipoEntita()));
					getUtilizzaAccontoCommand().setEnabled(
							arg != null && ((AreaAcconto) arg).getResiduo().compareTo(BigDecimal.ZERO) > 0);
					RisultatiRicercaAreaAccontoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED,
							null, new AreaAccontoWrapper((AreaAcconto) arg));
				}
			}
		});
	}

	/**
	 * Attiva i propertyChange sul form dei parametriricercaacconto.
	 */
	private void activateParametriAreaAccontoChangeListeners() {
		getAreaAccontoForm().getFormModel().getValueModel("statoAcconto")
				.addValueChangeListener(getStatoAccontoChangeListener());
		getAreaAccontoForm().getFormModel().getValueModel("tipoEntita")
				.addValueChangeListener(getStatoAccontoChangeListener());
	}

	/**
	 * Esegue la ricerca delle aree acconto.
	 */
	private List<AreaAcconto> caricaAreeAcconti() {
		List<AreaAcconto> list = new ArrayList<AreaAcconto>();

		if (this.areaAccontoWrapper != null && this.areaAccontoWrapper.getAreaAcconto() != null
				&& this.areaAccontoWrapper.getAreaAcconto().getId() != null) {
			list.add(this.areaAccontoWrapper.getAreaAcconto());
		} else {
			((AreaAccontoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
					.setTipoEntita(this.parametriRicerca.getTipoEntita());
			list = tesoreriaBD.caricaAcconti(parametriRicerca);
		}

		return list;

		// Vorrei sapere se le due righe che seguono servono in qualche caso particolare...questo fire object changed
		// risetta il form object e fa caricare nella lista risultati un solo record invece di tutti i risultati trovati

		// --> RisultatiRicercaAreaAccontoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
		// --> new AreaAccontoWrapper(getTable().getSelectedObject()));
	}

	/**
	 * Disattiva i propertyChange sul form dei parametriricercaacconto.
	 */
	private void deactivateParametriAreaAccontoChangeListeners() {
		getAreaAccontoForm().getFormModel().getValueModel("statoAcconto")
				.removeValueChangeListener(getStatoAccontoChangeListener());
		getAreaAccontoForm().getFormModel().getValueModel("tipoEntita")
				.removeValueChangeListener(getStatoAccontoChangeListener());
	}

	/**
	 * @return {@link ParametriRicercaAreaAccontoForm}
	 */
	private ParametriRicercaAreaAccontoForm getAreaAccontoForm() {
		if (parametriAreaAccontoForm == null) {
			parametriAreaAccontoForm = new ParametriRicercaAreaAccontoForm();
			activateParametriAreaAccontoChangeListeners();
		}
		return parametriAreaAccontoForm;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getUtilizzaAccontoCommand() };
	}

	@Override
	public JComponent getHeaderControl() {
		return getAreaAccontoForm().getControl();
	}

	/**
	 * @return {@link ParametriAccontoChangeListener}
	 */
	private ParametriAccontoChangeListener getStatoAccontoChangeListener() {
		if (parametriAccontoChangeListener == null) {
			parametriAccontoChangeListener = new ParametriAccontoChangeListener();
		}
		return parametriAccontoChangeListener;
	}

	/**
	 * @return the utilizzaAccontoCommand
	 */
	public UtilizzaAccontoCommand getUtilizzaAccontoCommand() {
		if (utilizzaAccontoCommand == null) {
			utilizzaAccontoCommand = new UtilizzaAccontoCommand(tesoreriaBD);
			utilizzaAccontoCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand arg0) {
					loadData();
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					command.addParameter(UtilizzaAccontoCommand.PARAM_ACCONTO,
							RisultatiRicercaAreaAccontoTablePage.this.getTable().getSelectedObject());
					return true;
				}
			});
		}

		return utilizzaAccontoCommand;
	}

	@Override
	public List<AreaAcconto> loadTableData() {
		return caricaAreeAcconti();
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {

		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;

		if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
				&& panjeaEvent.getSource() instanceof AreaChiusure) {
			loadData();
		} else {
			super.onEditorEvent(event);
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<AreaAcconto> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		this.areaAccontoWrapper = null;

		if (object instanceof AreaAccontoWrapper) {
			this.areaAccontoWrapper = (AreaAccontoWrapper) object;
		} else if (object instanceof AreaAcconto) {
			this.areaAccontoWrapper = new AreaAccontoWrapper((AreaAcconto) object);
		} else if (object instanceof ParametriRicercaAcconti) {
			this.parametriRicerca = (ParametriRicercaAcconti) object;
			// devo disattivare i property change legati al value model dei parametri altrimenti viene chiamata la
			// ricerca più volte
			deactivateParametriAreaAccontoChangeListeners();
			getAreaAccontoForm().setFormObject(parametriRicerca);
			activateParametriAreaAccontoChangeListeners();
		}
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
