/**
 *
 */
package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.forms.areaordine.ParametriRicercaAreaOrdineForm;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine.STATO_ORDINE;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.util.Assert;

/**
 * Page per l'inserimento dei {@link ParametriRicercaAreaOrdine} attraverso il Form
 * {@link ParametriRicercaAreaOrdineForm}.
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class ParametriRicercaAreaOrdinePage extends FormBackedDialogPageEditor implements InitializingBean {

	private class ResetParametriRicercaCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
		 */
		public ResetParametriRicercaCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaAreaOrdinePage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriRicercaAreaOrdine parametri = ((ParametriRicercaAreaOrdine) getBackingFormPage().getFormObject());
			Calendar calendar = Calendar.getInstance();

			calendar.set(Calendar.YEAR, aziendaCorrente.getAnnoMagazzino());
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			parametri.getDataDocumento().setDataIniziale(calendar.getTime());
			calendar.set(Calendar.MONTH, 11);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			parametri.getDataDocumento().setDataFinale(calendar.getTime());
			parametri.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
			parametri.setStatoOrdine(STATO_ORDINE.TUTTI);
			ParametriRicercaAreaOrdinePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					getBackingFormPage().getFormObject());
		}

	}

	private class SearchAreaOrdineCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 */
		public SearchAreaOrdineCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaAreaOrdinePage.logger.debug("--> Enter doExecuteCommand");
			ParametriRicercaAreaOrdine parametriRicerca = (ParametriRicercaAreaOrdine) getBackingFormPage()
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getFormModel().commit();
			ParametriRicercaAreaOrdinePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}
	}

	private AziendaCorrente aziendaCorrente;
	private static final String PAGE_ID = "parametriRicercaAreaOrdinePage";
	private static Logger logger = Logger.getLogger(ParametriRicercaAreaOrdinePage.class);
	private ResetParametriRicercaCommand resetParametriRicercaCommand;
	private SearchAreaOrdineCommand searchAreaOrdineCommand;
	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAreaOrdinePage() {
		super(PAGE_ID, new ParametriRicercaAreaOrdineForm(new ParametriRicercaAreaOrdine()));
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getSearchAreaOrdineCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(ordiniDocumentoBD, "ordiniDocumentoBD deve essere assegnato ");
		Assert.notNull(aziendaCorrente, "aziendaCorrente deve essere assegnato");
		ParametriRicercaAreaOrdineForm parametriRicercaAreaOrdineForm = (ParametriRicercaAreaOrdineForm) getBackingFormPage();
		parametriRicercaAreaOrdineForm.setOrdiniDocumentoBD(ordiniDocumentoBD);
		parametriRicercaAreaOrdineForm.setAziendaCorrente(aziendaCorrente);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
				getSearchAreaOrdineCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetParametriRicercaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getSearchAreaOrdineCommand();
	}

	/**
	 * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
	 * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
	 * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
	 * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
	 * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand).
	 *
	 * @return new object
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
	}

	/**
	 * @return the resetParametriRicercaCommand
	 **/
	protected ResetParametriRicercaCommand getResetParametriRicercaCommand() {
		if (resetParametriRicercaCommand == null) {
			resetParametriRicercaCommand = new ResetParametriRicercaCommand();
		}
		return resetParametriRicercaCommand;
	}

	/**
	 * @return the searchAreaOrdineCommand
	 */
	protected SearchAreaOrdineCommand getSearchAreaOrdineCommand() {
		if (searchAreaOrdineCommand == null) {
			searchAreaOrdineCommand = new SearchAreaOrdineCommand();
		}
		return searchAreaOrdineCommand;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
		if (!((ParametriRicercaAreaOrdine) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
			getResetParametriRicercaCommand().execute();
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		((PanjeaAbstractForm) getBackingFormPage()).getFormModel().setReadOnly(false);
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la combinazione ctrl + S che
	 * è abilitata di default nella form backed dialog page.
	 *
	 * @return <code>true</code>
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato di default nella form
	 * backed dialog page.
	 *
	 * @return <code>true</code>
	 */
	@Override
	public boolean onUndo() {
		return true;
	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
