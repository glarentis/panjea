/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.util.Arrays;
import java.util.HashSet;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.util.Assert;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.ParametriRicercaAreaMagazzinoForm;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Page per l'inserimento dei {@link ParametriRicercaAreaMagazzino} attraverso il Form
 * {@link ParametriRicercaAreaMagazzinoForm}.
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class ParametriRicercaAreaMagazzinoPage extends FormBackedDialogPageEditor implements InitializingBean {

    private class ResetParametriRicercaCommand extends ActionCommand {

        private static final String COMMAND_ID = "resetCommand";

        /**
         * Costruttore.
         *
         */
        public ResetParametriRicercaCommand() {
            super(COMMAND_ID);
            CommandConfigurer configurer = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
            configurer.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();
            parametriRicercaAreaMagazzino.getDataDocumento().setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
            parametriRicercaAreaMagazzino.setStatiAreaMagazzino(new HashSet<AreaMagazzino.StatoAreaMagazzino>(
                    Arrays.asList(AreaMagazzino.StatoAreaMagazzino.values())));
            parametriRicercaAreaMagazzino.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
            setFormObject(parametriRicercaAreaMagazzino);
            ParametriRicercaAreaMagazzinoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, null);
        }

    }

    private class SearchAreaMagazzinoCommand extends ActionCommand {

        private static final String COMMAND_ID = "searchCommand";

        /**
         * Costruttore.
         *
         */
        public SearchAreaMagazzinoCommand() {
            super(COMMAND_ID);
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
            c.configure(this);
        }

        @Override
        public void attach(AbstractButton button) {
            super.attach(button);
            button.setName("searchAreaMagazzinoCommand");
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaAreaMagazzinoPage.logger.debug("--> Enter doExecuteCommand");
            ParametriRicercaAreaMagazzino parametriRicerca = (ParametriRicercaAreaMagazzino) getBackingFormPage()
                    .getFormObject();
            parametriRicerca.setAreeMagazzino(null);
            parametriRicerca.setEffettuaRicerca(true);
            getForm().getFormModel().commit();
            ParametriRicercaAreaMagazzinoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriRicerca);
        }
    }

    private static final String PAGE_ID = "parametriRicercaAreaMagazzinoPage";
    private static Logger logger = Logger.getLogger(ParametriRicercaAreaMagazzinoPage.class);
    private AziendaCorrente aziendaCorrente;
    private ResetParametriRicercaCommand resetParametriRicercaCommand;
    private SearchAreaMagazzinoCommand searchAreaMagazzinoCommand;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private PanjeaFormGuard searchAreaMagazzinoFormGuard;

    /**
     * Costruttore.
     */
    public ParametriRicercaAreaMagazzinoPage() {
        super(PAGE_ID, new ParametriRicercaAreaMagazzinoForm(new ParametriRicercaAreaMagazzino()));
        searchAreaMagazzinoFormGuard = new PanjeaFormGuard(getBackingFormPage().getFormModel(),
                getSearchAreaMagazzinoCommand());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(magazzinoDocumentoBD, "magazzinoDocumentoBD deve essere assegnato ");
        Assert.notNull(aziendaCorrente, "aziendaCorrente deve essere assegnato");
        ParametriRicercaAreaMagazzinoForm parametriRicercaAreaMagazzinoForm = (ParametriRicercaAreaMagazzinoForm) getBackingFormPage();
        parametriRicercaAreaMagazzinoForm.setMagazzinoDocumentoBD(magazzinoDocumentoBD);
        parametriRicercaAreaMagazzinoForm.setAziendaCorrente(aziendaCorrente);
    }

    @Override
    public void dispose() {
        super.dispose();

        searchAreaMagazzinoFormGuard.clear();
        searchAreaMagazzinoFormGuard = null;

        resetParametriRicercaCommand = null;
        searchAreaMagazzinoCommand = null;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] commands = new AbstractCommand[] { getResetParametriRicercaCommand(),
                getSearchAreaMagazzinoCommand() };
        return commands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getSearchAreaMagazzinoCommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
     * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
     * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
     * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
     * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand)
     *
     * @return nuovo oggetto
     */
    @Override
    protected Object getNewEditorObject() {
        return null;
    }

    /**
     * @return resetParametriRicercaCommand
     */
    protected ResetParametriRicercaCommand getResetParametriRicercaCommand() {
        if (resetParametriRicercaCommand == null) {
            resetParametriRicercaCommand = new ResetParametriRicercaCommand();
        }
        return resetParametriRicercaCommand;
    }

    /**
     * @return searchAreaMagazzinoCommand
     */
    protected SearchAreaMagazzinoCommand getSearchAreaMagazzinoCommand() {
        if (searchAreaMagazzinoCommand == null) {
            searchAreaMagazzinoCommand = new SearchAreaMagazzinoCommand();
        }
        return searchAreaMagazzinoCommand;
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
        if (!((ParametriRicercaAreaMagazzino) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
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
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

}
