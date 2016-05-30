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
import it.eurotn.panjea.contabilita.rich.forms.ParametriAperturaContabileForm;
import it.eurotn.panjea.contabilita.util.ParametriAperturaContabile;
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
 * DialogPage per AperturaContabile.
 * 
 * @author adriano
 * @version 1.0, 05/set/07
 * 
 */
public class AperturaContiPage extends FormBackedDialogPageEditor {

	private class EseguiAperturaCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public EseguiAperturaCommand() {
			super(getPageEditorId() + ESEGUI_APERTURA_COMMAND);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriAperturaContabile aperturaContabile = (ParametriAperturaContabile) AperturaContiPage.this
					.getForm().getFormObject();
			AperturaContiPage.this.contabilitaBD.eseguiAperturaContabile(aperturaContabile);
			AperturaContiPage.this.parametriRicercaMovimentiContabili = new ParametriRicercaMovimentiContabili();
			AperturaContiPage.this.parametriRicercaMovimentiContabili.setEffettuaRicerca(true);
			AperturaContiPage.this.parametriRicercaMovimentiContabili.setAnnoCompetenza(aperturaContabile.getAnno()
					+ "");
			AperturaContiPage.this.parametriRicercaMovimentiContabili.getDataRegistrazione().setDataFinale(
					aperturaContabile.getDataMovimento());
			AperturaContiPage.this.parametriRicercaMovimentiContabili.getDataRegistrazione().setDataIniziale(
					aperturaContabile.getDataMovimento());
			AperturaContiPage.this.parametriRicercaMovimentiContabili.setEscludiMovimentiStampati(false);
			Map<TipoOperazioneTipoDocumento, TipoAreaContabile> map = AperturaContiPage.this.contabilitaAnagraficaBD
					.caricaTipiOperazione();
			TipoDocumento tipoDocumento = map.get(TipoOperazioneTipoDocumento.APERTURA_CONTO_ORDINE).getTipoDocumento();
			AperturaContiPage.this.parametriRicercaMovimentiContabili.getTipiDocumento().add(tipoDocumento);
			tipoDocumento = map.get(TipoOperazioneTipoDocumento.APERTURA_CONTO_PATRIMONIALE).getTipoDocumento();
			AperturaContiPage.this.parametriRicercaMovimentiContabili.getTipiDocumento().add(tipoDocumento);
			AperturaContiPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
					AperturaContiPage.this.parametriRicercaMovimentiContabili);
		}
	}

	private class ResetAperturaCommand extends ActionCommand {

		/**
		 * 
		 * Costruttore.
		 */
		public ResetAperturaCommand() {
			super("resetParametriRicercaCommand");
		}

		@Override
		protected void doExecuteCommand() {
			AperturaContiPage.this.toolbarPageEditor.getNewCommand().execute();
			ParametriAperturaContabile parametriAperturaContabile = (ParametriAperturaContabile) AperturaContiPage.this
					.getForm().getFormObject();
			parametriAperturaContabile.setAnno(AperturaContiPage.this.aziendaCorrente.getAnnoContabile());
			AperturaContiPage.this.getForm().setFormObject(parametriAperturaContabile);
		}
	}

	private static final String PAGE_ID = "aperturaContiPage";
	private AbstractCommand eseguiAperturaCommand;
	private static final String ESEGUI_APERTURA_COMMAND = ".eseguiAperturaCommand";
	private AbstractCommand resetAperturaCommand;
	private static final String RESET_APERTURA_COMMAND = ".resetAperturaCommand";
	private ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili;
	private AziendaCorrente aziendaCorrente;

	private IContabilitaBD contabilitaBD;

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public AperturaContiPage() {
		super(PAGE_ID, new ParametriAperturaContabileForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getEseguiAperturaCommand());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetAperturaCommand(),
				getEseguiAperturaCommand() };
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
		return getResetAperturaCommand();
	}

	@Override
	public AbstractCommand getEditorSaveCommand() {
		return getEseguiAperturaCommand();
	}

	/**
	 * @return EseguiAperturaCommand
	 */
	private AbstractCommand getEseguiAperturaCommand() {
		if (eseguiAperturaCommand == null) {
			eseguiAperturaCommand = new EseguiAperturaCommand();
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			eseguiAperturaCommand.setSecurityControllerId(getPageEditorId() + ESEGUI_APERTURA_COMMAND);
			c.configure(eseguiAperturaCommand);

		}
		return eseguiAperturaCommand;
	}

	/**
	 * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
	 * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
	 * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
	 * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
	 * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand)
	 * 
	 * @return Object
	 */
	@Override
	protected Object getNewEditorObject() {
		return null;
	}

	/**
	 * @return ResetAperturaCommand
	 */
	private AbstractCommand getResetAperturaCommand() {
		if (resetAperturaCommand == null) {
			resetAperturaCommand = new ResetAperturaCommand();
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			resetAperturaCommand.setSecurityControllerId(getPageEditorId() + RESET_APERTURA_COMMAND);
			c.configure(resetAperturaCommand);
		}
		return resetAperturaCommand;
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
		// richiamo la execute del ResetAperturaCommand perch� appena apro
		// la pagina posso subito inserire i parametri
		getResetAperturaCommand().execute();
	}

	@Override
	public boolean onPrePageOpen() {
		((PanjeaAbstractForm) getBackingFormPage()).getFormModel().setReadOnly(true);
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la combinazione ctrl + S che
	 * e' abilitata di default nella form backed dialog page.
	 * 
	 * @return true o false
	 */
	@Override
	public boolean onSave() {
		return true;
	}

	/**
	 * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che e' abilitato di default nella
	 * form backed dialog page.
	 * 
	 * @return true o false
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
		if (object instanceof ParametriAperturaContabile) {
			super.setFormObject(object);
		}
	}
}
