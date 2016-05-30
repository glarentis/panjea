/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.statistiche.valorizzazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.statistiche.valorizzazione.ParametriRicercaValorizzazioneForm;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.report.StampaCommand;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.util.Assert;

/**
 * @author fattazzo
 *
 */
public class ParametriRicercaValorizzazionePage extends FormBackedDialogPageEditor implements InitializingBean {

	private class LoadValorizzazioneCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 *
		 */
		public LoadValorizzazioneCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriRicercaValorizzazione parametriRicerca = (ParametriRicercaValorizzazione) getBackingFormPage()
					.getFormModel().getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaValorizzazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}

	}

	private class StampaValorizzazioneCommand extends StampaCommand {

		public static final String COMMAND_ID = "stampaValorizzazioneCommand";

		/**
		 * Costruttore.
		 */
		public StampaValorizzazioneCommand() {
			super(COMMAND_ID);
		}

		@Override
		protected Map<Object, Object> getParametri() {
			ParametriRicercaValorizzazione parametriRicercaValorizzazione = (ParametriRicercaValorizzazione) form
					.getFormObject();
			HashMap<Object, Object> parametri = new HashMap<Object, Object>();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
			parametri.put("descAzienda", aziendaCorrente.getDenominazione());
			parametri.put("data", dateFormat.format(parametriRicercaValorizzazione.getData()));
			parametri.put("consideraGiacenzaZero", parametriRicercaValorizzazione.isConsideraGiacenzaZero());
			parametri.put(
					"articoliSelezionati",
					StringUtils.join(parametriRicercaValorizzazione.getArticoliLiteId().toArray(
							new Integer[parametriRicercaValorizzazione.getArticoliLiteId().size()]),","));
			parametri.put("idDepositiSelezionati", parametriRicercaValorizzazione.getIdDepositiSelezionati());// idDepositiSelezionati);
			parametri.put("modalitaVisualizzazione", new Integer(parametriRicercaValorizzazione
					.getModalitaValorizzazione().ordinal()));
			parametri.put("consideraArticoliDisabilitati",
					parametriRicercaValorizzazione.isConsideraArticoliDisabilitati());

			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Stampa valorizzazione";
		}

		@Override
		protected String getReportPath() {
			return "Magazzino/valorizzazione";
		}

	}

	public static final String PAGE_ID = "parametriRicercaValorizzazionePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private IAnagraficaBD anagraficaBD;
	private AziendaCorrente aziendaCorrente;
	private ParametriRicercaValorizzazioneForm form = null;
	private LoadValorizzazioneCommand loadValorizzazioneCommand;

	private StampaValorizzazioneCommand stampaValorizzazioneCommand;

	/**
	 * Costruttore.
	 *
	 */
	public ParametriRicercaValorizzazionePage() {
		super(PAGE_ID, new ParametriRicercaValorizzazioneForm(new ParametriRicercaValorizzazione()));
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getLoadValorizzazioneCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(magazzinoAnagraficaBD, "magazzinoAnagraficaBD deve essere assegnato ");
		form = (ParametriRicercaValorizzazioneForm) getBackingFormPage();
		form.setAnagraficaBD(anagraficaBD);
		form.setAziendaCorrente(aziendaCorrente);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getLoadValorizzazioneCommand(),
				getStampaValorizzazioneCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getLoadValorizzazioneCommand();
	}

	/**
	 * @return loadValorizzazioneCommand
	 */
	protected LoadValorizzazioneCommand getLoadValorizzazioneCommand() {
		if (loadValorizzazioneCommand == null) {
			loadValorizzazioneCommand = new LoadValorizzazioneCommand();
		}
		return loadValorizzazioneCommand;
	}

	/**
	 * @return the stampaValorizzazioneCommand
	 */
	public StampaValorizzazioneCommand getStampaValorizzazioneCommand() {
		if (stampaValorizzazioneCommand == null) {
			stampaValorizzazioneCommand = new StampaValorizzazioneCommand();
		}

		return stampaValorizzazioneCommand;
	}

	@Override
	protected boolean insertControlInScrollPane() {
		return false;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
		// nothing to do
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
		// nothing to do
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
