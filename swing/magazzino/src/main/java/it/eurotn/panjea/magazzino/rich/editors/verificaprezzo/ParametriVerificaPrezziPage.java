package it.eurotn.panjea.magazzino.rich.editors.verificaprezzo;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.verificaprezzo.ParametriCalcoloPrezziForm;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ParametriVerificaPrezziPage extends FormBackedDialogPageEditor {

    /**
     * Command da inserire sulla toolbar per la verifica del prezzo.
     *
     * @author giangi
     *
     */
    private class VerificaPrezziCommand extends ActionCommand {

        private static final String COMMAND_ID = "verificaPrezziPageCommand";

        /**
         * Costruttore.
         */
        public VerificaPrezziCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriCalcoloPrezziPM parametriCalcoloPrezziPM = (ParametriCalcoloPrezziPM) getBackingFormPage()
                    .getFormObject();
            parametriCalcoloPrezziPM.setEffettuaRicerca(true);
            getForm().getFormModel().commit();
            ParametriVerificaPrezziPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriCalcoloPrezziPM);
        }
    }

    private static final String PAGE_ID = "parametriCalcoloPrezziPage";

    private VerificaPrezziCommand verificaPrezziCommand;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private ValutaAziendaCache valutaCache;

    /**
     * Costruttore di default.
     */
    public ParametriVerificaPrezziPage() {
        super(PAGE_ID, new ParametriCalcoloPrezziForm());
        this.valutaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] commands = new AbstractCommand[] { getVerificaPrezziCommand() };
        return commands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getVerificaPrezziCommand();
    }

    /**
     * @return the magazzinoDocumentoBD
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    /**
     *
     * @return command per lanciare la verifica prezzi.
     */
    private VerificaPrezziCommand getVerificaPrezziCommand() {
        if (verificaPrezziCommand == null) {
            verificaPrezziCommand = new VerificaPrezziCommand();
            new PanjeaFormGuard(getBackingFormPage().getFormModel(), verificaPrezziCommand);
        }
        return verificaPrezziCommand;
    }

    @Override
    public void grabFocus() {
        ((ParametriCalcoloPrezziForm) getForm()).requestFocus();
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
    }

    @Override
    public boolean onPrePageOpen() {
        return false;
    }

    @Override
    public void postSetFormObject(Object object) {
        super.postSetFormObject(object);
        getDefaultController().register();
    }

    @Override
    public void preSetFormObject(Object object) {
        super.preSetFormObject(object);
        getDefaultController().unregistrer();
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void setFormObject(Object object) {

        // se non ho importato una valuta setto quella dell'azienda
        ParametriCalcoloPrezziPM parametri = (ParametriCalcoloPrezziPM) object;
        if (parametri.getCodiceValuta() == null || parametri.getCodiceValuta().isEmpty()) {
            parametri.setCodiceValuta(valutaCache.caricaValutaAziendaCorrente().getCodiceValuta());
        }

        super.setFormObject(parametri);
    }

    /**
     * @param magazzinoDocumentoBD
     *            the magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }
}
