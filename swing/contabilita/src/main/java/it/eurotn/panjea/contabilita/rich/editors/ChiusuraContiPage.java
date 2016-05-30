/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.forms.ParametriChiusuraContabileForm;
import it.eurotn.panjea.contabilita.util.ParametriChiusuraContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.util.Map;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * DialoPage dell'operazione di chisura contabile.
 * 
 * @author adriano
 * @version 1.0, 05/set/07
 */
public class ChiusuraContiPage extends FormBackedDialogPageEditor {

	private class EseguiChiusuraCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public EseguiChiusuraCommand() {
			super(getPageEditorId() + ESEGUI_CHIUSURA_COMMAND);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriChiusuraContabile chiusuraContabile = (ParametriChiusuraContabile) ChiusuraContiPage.this
					.getForm().getFormObject();
			ChiusuraContiPage.this.contabilitaBD.eseguiChiusuraContabile(chiusuraContabile);

			ChiusuraContiPage.this.parametriRicercaMovimentiContabili = new ParametriRicercaMovimentiContabili();
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.setEffettuaRicerca(true);
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.setAnnoCompetenza(chiusuraContabile.getAnno()
					+ "");
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.getDataRegistrazione().setDataFinale(
					chiusuraContabile.getDataMovimento());
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.getDataRegistrazione().setDataIniziale(
					chiusuraContabile.getDataMovimento());
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.setEscludiMovimentiStampati(false);

			Map<TipoOperazioneTipoDocumento, TipoAreaContabile> map = ChiusuraContiPage.this.contabilitaAnagraficaBD
					.caricaTipiOperazione();
			TipoDocumento tipoDocumento = map.get(TipoOperazioneTipoDocumento.CHIUSURA_CONTO_ECONOMICO)
					.getTipoDocumento();
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.getTipiDocumento().add(tipoDocumento);
			tipoDocumento = map.get(TipoOperazioneTipoDocumento.CHIUSURA_CONTO_ORDINE).getTipoDocumento();
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.getTipiDocumento().add(tipoDocumento);
			tipoDocumento = map.get(TipoOperazioneTipoDocumento.CHIUSURA_CONTO_PATRIMONIALE).getTipoDocumento();
			ChiusuraContiPage.this.parametriRicercaMovimentiContabili.getTipiDocumento().add(tipoDocumento);
			ChiusuraContiPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					parametriRicercaMovimentiContabili);
		}

	}

	private class ResetChiusuraCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public ResetChiusuraCommand() {
			super("resetParametriRicercaCommand");
		}

		@Override
		protected void doExecuteCommand() {
			ChiusuraContiPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriChiusuraContabile parametriChiusuraContabile = (ParametriChiusuraContabile) ChiusuraContiPage.this
					.getForm().getFormObject();
			parametriChiusuraContabile.setAnno(ChiusuraContiPage.this.aziendaCorrente.getAnnoContabile() - 1);
			ChiusuraContiPage.this.getForm().setFormObject(parametriChiusuraContabile);
		}
	}

	private static final String PAGE_ID = "chiusuraContiPage";
	private AbstractCommand eseguiChiusuraCommand;
	private static final String ESEGUI_CHIUSURA_COMMAND = ".eseguiChiusuraCommand";
	private AbstractCommand resetChiusuraCommand;
	private static final String RESET_CHIUSURA_COMMAND = ".resetChiusuraCommand";
	private ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili;
	private IContabilitaBD contabilitaBD;

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 */
	public ChiusuraContiPage() {
		super(PAGE_ID, new ParametriChiusuraContabileForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getEseguiChiusuraCommand());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetChiusuraCommand(),
				getEseguiChiusuraCommand() };
		return abstractCommands;
	}

	/**
	 * @return Returns the contabilitaAnagraficaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	/**
	 * @return Returns the contabilitaBD.
	 */
	public IContabilitaBD getContabilitaBD() {
		return contabilitaBD;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return getResetChiusuraCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getEseguiChiusuraCommand();
	}

	/**
	 * @return EseguiChiusuraCommand
	 */
	public AbstractCommand getEseguiChiusuraCommand() {
		if (eseguiChiusuraCommand == null) {
			eseguiChiusuraCommand = new EseguiChiusuraCommand();
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			eseguiChiusuraCommand.setSecurityControllerId(getPageEditorId() + ESEGUI_CHIUSURA_COMMAND);
			c.configure(eseguiChiusuraCommand);
		}
		return eseguiChiusuraCommand;

	}

	/**
	 * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
	 * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
	 * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
	 * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
	 * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand).
	 * 
	 * @return null
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
	}

	/**
	 * @return ResetChiusuraCommand
	 */
	public AbstractCommand getResetChiusuraCommand() {
		if (resetChiusuraCommand == null) {
			resetChiusuraCommand = new ResetChiusuraCommand();
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			resetChiusuraCommand.setSecurityControllerId(getPageEditorId() + RESET_CHIUSURA_COMMAND);
			c.configure(resetChiusuraCommand);
		}
		return resetChiusuraCommand;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
		// richiamo la execute del ResetChiusuraCommand perch� appena apro
		// la pagina posso subito inserire i parametri
		getResetChiusuraCommand().execute();
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
	 * @return true
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato di default nella form
	 * backed dialog page.
	 * 
	 * @return true
	 */
	@Override
	public boolean onUndo() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

	/**
	 * @param contabilitaBD
	 *            The contabilitaBD to set.
	 */
	public void setContabilitaBD(IContabilitaBD contabilitaBD) {
		this.contabilitaBD = contabilitaBD;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriChiusuraContabile) {
			super.setFormObject(object);
		}
	}
}
