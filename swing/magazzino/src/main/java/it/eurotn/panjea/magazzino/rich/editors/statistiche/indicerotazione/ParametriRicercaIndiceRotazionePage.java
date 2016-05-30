package it.eurotn.panjea.magazzino.rich.editors.statistiche.indicerotazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.util.Assert;

public class ParametriRicercaIndiceRotazionePage extends
		FormBackedDialogPageEditor implements InitializingBean {

	private class LoadCommand extends ActionCommand {

		private static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 *
		 */
		public LoadCommand() {
			super(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator
					.services().getService(CommandConfigurer.class);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().getFormModel().commit();
			ParametriCalcoloIndiciRotazioneGiacenza parametriRicerca = (ParametriCalcoloIndiciRotazioneGiacenza) getBackingFormPage()
					.getFormModel().getFormObject();
			parametriRicerca.setEffettuaRicerca(true);
			getForm().getBindingFactory().getFormModel().commit();
			ParametriRicercaIndiceRotazionePage.this.firePropertyChange(
					IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicerca);
		}

	}

	public static final String PAGE_ID = "parametriRicercaValorizzazionePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private IAnagraficaBD anagraficaBD;

	private AziendaCorrente aziendaCorrente;

	private ParametriRicercaIndiceRotazioneForm form = null;

	private LoadCommand loadValorizzazioneCommand;

	/**
	 * Costruttore.
	 *
	 */
	public ParametriRicercaIndiceRotazionePage() {
		super(PAGE_ID, new ParametriRicercaIndiceRotazioneForm(
				new ParametriCalcoloIndiciRotazioneGiacenza()));
		new PanjeaFormGuard(getBackingFormPage().getFormModel(),
				getLoadValorizzazioneCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(magazzinoAnagraficaBD,
				"magazzinoAnagraficaBD deve essere assegnato ");
		form = (ParametriRicercaIndiceRotazioneForm) getBackingFormPage();
		form.setAnagraficaBD(anagraficaBD);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getLoadValorizzazioneCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getLoadValorizzazioneCommand();
	}

	/**
	 * @return loadValorizzazioneCommand
	 */
	protected LoadCommand getLoadValorizzazioneCommand() {
		if (loadValorizzazioneCommand == null) {
			loadValorizzazioneCommand = new LoadCommand();
		}
		return loadValorizzazioneCommand;
	}

	/**
	 * @return Returns the magazzinoDocumentoBD.
	 */
	public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
		return magazzinoDocumentoBD;
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
	public void setMagazzinoAnagraficaBD(
			IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(
			IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}
}
