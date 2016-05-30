/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.Guarded;

import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.ParametriRicercaControlloMovimentoContabilitaForm;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Page che contiene i parametri per la ricerca controllo movimento contabilit?. La toolbar di
 * gestione della pagina prevede il reset dei parametri di ricerca a valori di default e la ricerca
 * effettiva.
 *
 * @author Leonardo
 * @see ParametriRicercaMovimentiContabili
 * @see _RisultatiRicercaControlloMovimentoContabilitaPage
 */
public class ParametriRicercaControlloMovimentoContabilitaPage extends FormBackedDialogPageEditor {

    private class CercaTipiDocumentoCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         *
         */
        public CercaTipiDocumentoCommand() {
            super("searchCommand");
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(getPageEditorId() + CERCA_TIPI_DOCUMENTO_COMMAND);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaControlloMovimentoContabilitaPage.logger.debug("--> Cerca tipi documento");
            ParametriRicercaMovimentiContabili parametriRicerca = (ParametriRicercaMovimentiContabili) getBackingFormPage()
                    .getFormObject();
            parametriRicerca.setEffettuaRicerca(true);

            getForm().getFormModel().commit();
            ParametriRicercaControlloMovimentoContabilitaPage.this.firePropertyChange(
                    IPageLifecycleAdvisor.OBJECT_CHANGED, null, getBackingFormPage().getFormObject());
        }
    }

    private static Logger logger = Logger.getLogger(ParametriRicercaControlloMovimentoContabilitaPage.class);
    private static final String ID_PAGE = "parametriRicercaControlloMovimentoContabilitaPage";
    private static final String CERCA_TIPI_DOCUMENTO_COMMAND = ".cercaTipiDocumentoCommand";
    private static final String RESET_PARAMETRI_RICERCA_COMMAND = ".resetParametriRicercaCommand";
    private AbstractCommand cercaTipiDocumentoCommand;
    private AbstractCommand resetRicercaCommand;
    private IDocumentiBD documentiBD;

    /**
     *
     * Costruttore.
     *
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     * @param documentiBD
     *            documentiBD
     * @param aziendaCorrente
     *            azienda corrente
     */
    public ParametriRicercaControlloMovimentoContabilitaPage(final IContabilitaAnagraficaBD contabilitaAnagraficaBD,
            final IDocumentiBD documentiBD, final AziendaCorrente aziendaCorrente) {
        super(ID_PAGE, new ParametriRicercaControlloMovimentoContabilitaForm(new ParametriRicercaMovimentiContabili(),
                contabilitaAnagraficaBD, documentiBD, aziendaCorrente));
        this.documentiBD = documentiBD;
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCercaTipiDocumentoCommand());
    }

    @Override
    public void dispose() {
        super.dispose();

        cercaTipiDocumentoCommand = null;

        resetRicercaCommand = null;
    }

    /**
     * Passo i parametriRicerca all'editor lanciando il firePropertyChange sulla propriet?
     * IPageLifecycleAdvisor.OBJECT_CHANGED Nota che in questo command viene solo lanciato il
     * property change, la ricerca effettiva viene eseguita nella page
     * RisultatiRicercaControlloMovimentoContabilitaPage, dove viene presentata la lista in una
     * treeTable.
     *
     * @return il command per la ricerca righe contabili
     */
    public AbstractCommand getCercaTipiDocumentoCommand() {
        if (cercaTipiDocumentoCommand == null) {
            cercaTipiDocumentoCommand = new CercaTipiDocumentoCommand();
        }
        return cercaTipiDocumentoCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetParametriRicercaCommand(),
                getCercaTipiDocumentoCommand() };
        return abstractCommands;
    }

    /**
     * @return the documentiBD
     */
    public IDocumentiBD getDocumentiBD() {
        return documentiBD;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getCercaTipiDocumentoCommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di
     * this (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il
     * valore di default (getBackingFormPage().getFormObject()) viene lanciata una propertychange e
     * quindi la page RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo
     * dopo viene lanciato il propertychange con oggetto a null per azzerare le righe visualizzate
     * (vedi doExecuteCommand di this.resetRicercaCommand)
     *
     * @return new object
     */
    @Override
    protected Object getNewEditorObject() {
        return null;
    }

    /**
     * Esegue il reset dei parametri ricerca, assegnando i valori di default per ogni campo
     * visualizzato.
     *
     * @return resetRicercaCommand
     */
    public AbstractCommand getResetParametriRicercaCommand() {
        if (resetRicercaCommand == null) {
            resetRicercaCommand = new ActionCommand("resetParametriRicercaCommand") {

                @Override
                protected void doExecuteCommand() {
                    ParametriRicercaControlloMovimentoContabilitaPage.logger.debug("--> Reset command");
                    ParametriRicercaControlloMovimentoContabilitaPage.this.toolbarPageEditor.getNewCommand().execute();
                    ParametriRicercaControlloMovimentoContabilitaPage.this.firePropertyChange(
                            IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                            ParametriRicercaControlloMovimentoContabilitaPage.this.getBackingFormPage()
                                    .getFormObject());
                }
            };
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            resetRicercaCommand.setSecurityControllerId(getPageEditorId() + RESET_PARAMETRI_RICERCA_COMMAND);
            c.configure(resetRicercaCommand);
        }
        return resetRicercaCommand;
    }

    /**
     * Sovrascritto per dare il focus alla data invece che al primo campo disponibile come avviene
     * di default sulla formbackeddialogpageeditor.
     */
    @Override
    public void grabFocus() {
        ((ParametriRicercaControlloMovimentoContabilitaForm) getBackingFormPage()).requestFocusForDate();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void loadData() {
        ParametriRicercaMovimentiContabili parametriRicercaAreePartite = (ParametriRicercaMovimentiContabili) getBackingFormPage()
                .getFormObject();
        if (!parametriRicercaAreePartite.isEffettuaRicerca()) {
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
     * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la
     * combinazione ctrl + S che e' abilitata di default nella form backed dialog page.
     *
     * @return onSave
     */
    @Override
    public boolean onSave() {
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che e' abilitato
     * di default nella form backed dialog page.
     *
     * @return onUndo
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
     * @param documentiBD
     *            the documentiBD to set
     */
    public void setDocumentiBD(IDocumentiBD documentiBD) {
        this.documentiBD = documentiBD;
    }

}
