package it.eurotn.panjea.magazzino.rich.editors.rendicontazione;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.forms.rendicontazione.ParametriRicercaRendicontazioneForm;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ParametriRicercaRendicontazionePage extends FormBackedDialogPageEditor {

    private class ResetParametriRicercaCommand extends ActionCommand {

        private static final String COMMAND_ID = "resetCommand";

        /**
         * Costruttore.
         *
         */
        public ResetParametriRicercaCommand() {
            super(COMMAND_ID);
            this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
            RcpSupport.configure(this);

        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaRendicontazionePage.this.toolbarPageEditor.getNewCommand().execute();
            ParametriRicercaRendicontazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    null);
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
            this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaAreaMagazzino parametriRicerca = (ParametriRicercaAreaMagazzino) getBackingFormPage()
                    .getFormObject();
            List<TipoAreaMagazzino> tipiAree = ((ParametriRicercaRendicontazioneForm) getBackingFormPage())
                    .getTipiAreaMagazzinoExport();

            if (!tipiAree.isEmpty()) {
                parametriRicerca.setEffettuaRicerca(true);

                getForm().getFormModel().commit();
                parametriRicerca.getTipiAreaMagazzino().clear();
                parametriRicerca.getTipiAreaMagazzino().addAll(tipiAree);
                parametriRicerca.getNumeroDocumentoIniziale().setCodice(null);
                parametriRicerca.getNumeroDocumentoFinale().setCodice(null);
                parametriRicerca.setAnnoCompetenza(-1);
                parametriRicerca.setEntita(null);
                parametriRicerca.setUtente(null);
                parametriRicerca.setDepositiDestinazione(null);
                parametriRicerca.setDepositiSorgente(null);
                parametriRicerca.setTipiGenerazione(null);
                parametriRicerca.getStatiAreaMagazzino().clear();
                parametriRicerca.getStatiAreaMagazzino().add(StatoAreaMagazzino.CONFERMATO);

                ParametriRicercaRendicontazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                        parametriRicerca);
            }
        }
    }

    public static final String PAGE_ID = "parametriRendicontazionePage";

    private SearchAreaMagazzinoCommand searchAreaMagazzinoCommand;
    private ResetParametriRicercaCommand resetParametriRicercaCommand;

    /**
     * Costruttore.
     *
     */
    public ParametriRicercaRendicontazionePage() {
        super(PAGE_ID, new ParametriRicercaRendicontazioneForm());
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
     * @return the searchAreaMagazzinoCommand
     */
    public SearchAreaMagazzinoCommand getSearchAreaMagazzinoCommand() {
        if (searchAreaMagazzinoCommand == null) {
            searchAreaMagazzinoCommand = new SearchAreaMagazzinoCommand();
        }
        return searchAreaMagazzinoCommand;
    }

    @Override
    public void loadData() {
        // this.toolbarPageEditor.getNewCommand().execute();
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
    }

    @Override
    public void setFormObject(Object object) {
        ParametriRicercaAreaMagazzino parametri = new ParametriRicercaAreaMagazzino();
        super.setFormObject(parametri);
    }

}
