package it.eurotn.panjea.fatturepa.rich.editors.ricerca.statoarea;

import java.io.File;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.util.ExtensionFileFilter;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.NotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class AreaNotificaFatturaPaPage extends FormBackedDialogPageEditor {

    private class NuovoDaNotificaSdICommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public NuovoDaNotificaSdICommand() {
            super("nuovoDaNotificaSdICommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            ExtensionFileFilter eseguibileFileFilter = new ExtensionFileFilter(new String[] { "xml" }, "File XML");
            fc.addChoosableFileFilter(eseguibileFileFilter);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(eseguibileFileFilter);
            int returnVal = fc.showOpenDialog(AreaNotificaFatturaPaPage.this.getControl());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                String fileName = FilenameUtils.getName(file.getName());

                // Nome del file fattura ricevuto senza estensione (quindi IdpaeseIdentificativo_progressivo) _ Tipo di
                // messaggio _ Progressivo univoco
                try {
                    String tipoMessaggio = StringUtils.split(fileName, "_")[2];

                    NotificaFatturaPA notificaFatturaPA = new NotificaFatturaPA();
                    notificaFatturaPA.setDatiEsito(FileUtils.readFileToString(file));
                    notificaFatturaPA.setDatiEsitoDaSDI(true);
                    notificaFatturaPA.setStatoFattura(StatoFatturaPA.valueOf(tipoMessaggio));

                    AreaNotificaFatturaPA areaNotificaFatturaPANew = new AreaNotificaFatturaPA();
                    areaNotificaFatturaPANew.setNotificaFatturaPA(notificaFatturaPA);

                    onUndo();
                    onNew();
                    getBackingFormPage().getFormModel().getValueModel("notificaFatturaPA").setValue(notificaFatturaPA);

                } catch (Exception e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("--> Nome file non corretto o file danneggiato.", e);
                    }
                    new MessageDialog("ERRORE", "Nome file non corretto o file danneggiato.").showDialog();
                }
            }
        }

    }

    private static final Logger LOGGER = Logger.getLogger(AreaNotificaFatturaPaPage.class);

    private AreaNotificaFatturaPA areaNotificaFatturaPA;
    private Integer idAreaMagazzino;

    private IFatturePABD fatturePABD;

    private NuovoDaNotificaSdICommand nuovoDaNotificaSdICommand;

    /**
     * Costruttore.
     */
    public AreaNotificaFatturaPaPage() {
        super("areaNotificaFatturaPaPage", new AreaNotificaFatturaPAForm());
        this.fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);
    }

    @Override
    protected Object doSave() {

        getBackingFormPage().commit();
        areaNotificaFatturaPA = (AreaNotificaFatturaPA) getBackingFormPage().getFormObject();

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD.caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
        areaMagazzinoFatturaPA.setNotificaCorrente(areaNotificaFatturaPA.getNotificaFatturaPA());

        fatturePABD.salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);

        return areaNotificaFatturaPA;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getNuovoDaNotificaSdICommand(), toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand() };
    }

    /**
     * @return the nuovoDaNotificaSdICommand
     */
    private NuovoDaNotificaSdICommand getNuovoDaNotificaSdICommand() {
        if (nuovoDaNotificaSdICommand == null) {
            nuovoDaNotificaSdICommand = new NuovoDaNotificaSdICommand();
        }

        return nuovoDaNotificaSdICommand;
    }

    @Override
    public void loadData() {
        // non faccio niente
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public void refreshData() {
        // non faccio niente
    }

    @Override
    public void setFormObject(Object object) {
        this.areaNotificaFatturaPA = (AreaNotificaFatturaPA) object;
        super.setFormObject(object);
    }

    /**
     * @param idAreaMagazzino
     *            the idAreaMagazzino to set
     */
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        this.idAreaMagazzino = idAreaMagazzino;
    }

    @Override
    public void updateCommands() {
        super.updateCommands();

        if (this.areaNotificaFatturaPA != null
                && this.areaNotificaFatturaPA.getNotificaFatturaPA().isDatiEsitoDaSDI()) {
            toolbarPageEditor.getLockCommand().setEnabled(false);
        }
    }

}
