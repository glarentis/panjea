package it.eurotn.panjea.compoli.rich.editors;

import java.io.File;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.compoli.rich.bd.IComunicazionePolivalenteBD;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.command.AbstractSearchCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.report.editor.export.EsportazioneStampaMessageAlert;

public class ParametriCreazioneComPolivalentePage extends FormBackedDialogPageEditor {

    private class CaricaComPolivalenteCommand extends AbstractSearchCommand {

        @Override
        protected void doExecuteCommand() {
            getBackingFormPage().getFormModel().commit();
            ParametriCreazioneComPolivalente parametriCreazione = (ParametriCreazioneComPolivalente) getBackingFormPage()
                    .getFormModel().getFormObject();
            parametriCreazione.setEffettuaRicerca(true);
            getForm().getBindingFactory().getFormModel().commit();
            ParametriCreazioneComPolivalentePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriCreazione);
        }

        @Override
        protected String getPrefixName() {
            return getPageEditorId();
        }

    }

    private class GeneraFileCommand extends ActionCommand {

        private static final String COMMAND_ID = "generaFileCommand";

        private final EsportazioneStampaMessageAlert alert = new EsportazioneStampaMessageAlert(
                "Generazione del file in corso...");

        /**
         * Costruttore.
         */
        public GeneraFileCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleziona il file per l'esportazione");
            File fileToExport = new File(System.getProperty("java.io.tmpdir") + "/ComunicazionePolivalente.txt");
            fileChooser.setSelectedFile(fileToExport);
            int returnVal = fileChooser.showSaveDialog(Application.instance().getActiveWindow().getControl());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                alert.showAlert();
                File localFile = fileChooser.getSelectedFile();

                try {
                    getBackingFormPage().getFormModel().commit();
                    ParametriCreazioneComPolivalente parametriCreazione = (ParametriCreazioneComPolivalente) getBackingFormPage()
                            .getFormModel().getFormObject();
                    byte[] byteFile = comunicazionePolivalenteBD.genera(parametriCreazione);

                    FileUtils.writeByteArrayToFile(localFile, byteFile);
                } catch (Exception e) {
                    LOGGER.error("--> errore durante la creazione del file.", e);
                    alert.errorExport();
                    return;
                }
                alert.finishExport(localFile);
            }
        }
    }

    private class ResetParametriCommand extends ActionCommand {

        private static final String COMMAND_ID = "resetParametriRicercaCommand";

        /**
         * Costruttore.
         */
        public ResetParametriCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriCreazioneComPolivalentePage.LOGGER.debug("--> Reset command");
            // lancio null Object_changed per ripulire i risultati
            ParametriCreazioneComPolivalentePage.this.toolbarPageEditor.getNewCommand().execute();
            ((ParametriCreazioneComPolivalenteForm) getBackingFormPage()).getFormModel().setReadOnly(false);
        }

    }

    private static final Logger LOGGER = Logger.getLogger(ParametriCreazioneComPolivalentePage.class);

    private static final String PAGE_ID = "parametriCreazioneComPolivalentePage";
    private IComunicazionePolivalenteBD comunicazionePolivalenteBD = null;
    private AziendaCorrente aziendaCorrente = null;

    private CaricaComPolivalenteCommand caricaComPolivalenteCommand = null;
    private ResetParametriCommand resetParametriCommand = null;
    private GeneraFileCommand generaFileCommand = null;

    /**
     * Costruttore.
     */
    public ParametriCreazioneComPolivalentePage() {
        super(PAGE_ID, new ParametriCreazioneComPolivalenteForm());
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCaricaComPolivalenteCommand());
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getGeneraFileCommand());
    }

    /**
     * @return the aziendaCorrente
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     * @return the caricaComPolivalenteCommand
     */
    public CaricaComPolivalenteCommand getCaricaComPolivalenteCommand() {
        if (caricaComPolivalenteCommand == null) {
            caricaComPolivalenteCommand = new CaricaComPolivalenteCommand();
        }

        return caricaComPolivalenteCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getResetParametriCommand(), getCaricaComPolivalenteCommand(),
                getGeneraFileCommand() };
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getCaricaComPolivalenteCommand();
    }

    /**
     * @return the generaFileCommand
     */
    public GeneraFileCommand getGeneraFileCommand() {
        if (generaFileCommand == null) {
            generaFileCommand = new GeneraFileCommand();
        }

        return generaFileCommand;
    }

    /**
     * @return the resetDatiSpesometroCommand to get
     */
    public ResetParametriCommand getResetParametriCommand() {
        if (resetParametriCommand == null) {
            resetParametriCommand = new ResetParametriCommand();
        }
        return resetParametriCommand;
    }

    @Override
    public void grabFocus() {
        ((ParametriCreazioneComPolivalenteForm) getBackingFormPage()).requestFocusForData();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void loadData() {
        // non faccio niente
    }

    @Override
    public void onPostPageOpen() {
        getResetParametriCommand().execute();
    }

    @Override
    public boolean onPrePageOpen() {
        ((PanjeaAbstractForm) getBackingFormPage()).getFormModel().setReadOnly(false);
        return true;
    }

    @Override
    public boolean onSave() {
        return true;
    }

    @Override
    public boolean onUndo() {
        return true;
    }

    @Override
    public void refreshData() {
        // non faccio niente
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
        ((ParametriCreazioneComPolivalenteForm) getBackingFormPage()).setAziendaCorrente(aziendaCorrente);
    }

    /**
     * @param comunicazionePolivalenteBD
     *            the comunicazionePolivalenteBD to set
     */
    public void setComunicazionePolivalenteBD(IComunicazionePolivalenteBD comunicazionePolivalenteBD) {
        this.comunicazionePolivalenteBD = comunicazionePolivalenteBD;
    }
}
