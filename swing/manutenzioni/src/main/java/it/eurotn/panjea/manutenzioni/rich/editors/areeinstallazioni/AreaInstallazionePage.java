package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.areamagazzino.StampaAreaMagazzinoSplitCommand;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.command.SeparatorActionCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class AreaInstallazionePage extends FormBackedDialogPageEditor {

    private class AreaMagazzinoCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            AreaInstallazione ai = (AreaInstallazione) getPageObject();
            command.addParameter(AreaMagazzinoCommand.AREA_MAGAZZINO_ID_PARAM, ai.getIdAreaMagazzino());
            return true;
        }
    }

    private class CancellaMagazzinoCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            Integer idAreaMagazzino = ((CancellaAreaMagazzinoCommand) command).getIdAreaMagazzino();
            getForm().getFormModel().getValueModel("idAreaMagazzino").setValue(idAreaMagazzino);
            // Risetto per non lasciare a distry il form model
            getForm().setFormObject(getForm().getFormObject());
            updateCommands();

            AreaInstallazionePage.this.firePropertyChange(AREA_MAGAZZINO_PRESENTE, idAreaMagazzino == null,
                    idAreaMagazzino != null);
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            AreaInstallazione ai = (AreaInstallazione) getPageObject();
            command.addParameter(CancellaAreaMagazzinoCommand.AREA_MAGAZZINO_ID_PARAM, ai.getIdAreaMagazzino());
            return true;
        }
    }

    private class CreaAreaMagazzinoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            Integer idAreaMagazzino = ((CreaAreaMagazzinoCommand) command).getIdAreaMagazzinoCreata();
            getForm().getFormModel().getValueModel("idAreaMagazzino").setValue(idAreaMagazzino);
            // Risetto per non lasciare a distry il form model
            getForm().setFormObject(getForm().getFormObject());
            updateCommands();

            AreaInstallazionePage.this.firePropertyChange(AREA_MAGAZZINO_PRESENTE, idAreaMagazzino == null,
                    idAreaMagazzino != null);
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            AreaInstallazione ai = (AreaInstallazione) getPageObject();
            command.addParameter(CreaAreaMagazzinoCommand.PARAM_ID_AREA_INSTALLAZIONE_KEY, ai.getId());
            return true;
        }

    }

    public static final String PAGE_ID = "areaInstallazionePage";
    public static final String AREA_MAGAZZINO_PRESENTE = "areaMagazzinoPresente";

    private IManutenzioniBD manutenzioniBD = null;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private CreaAreaMagazzinoCommand creaAreaMagazzinoCommand;

    private AreaMagazzinoCommandInterceptor areaMagazzinoCommandInterceptor;
    private AreaMagazzinoCommand amCommand;
    private ModificaAreaMagazzinoCommand modificaAreaMagazzinoCommand;
    private StampaAreaMagazzinoSplitCommand stampaAreaMagazzinoSplitCommand;

    private CancellaAreaMagazzinoCommand cancellaAreaMagazzinoCommand;

    /**
     * Costruttore.
     */
    public AreaInstallazionePage() {
        super(PAGE_ID, new AreaInstallazioneForm());
        areaMagazzinoCommandInterceptor = new AreaMagazzinoCommandInterceptor();
        amCommand = new AreaMagazzinoCommand();
        amCommand.addCommandInterceptor(areaMagazzinoCommandInterceptor);
        modificaAreaMagazzinoCommand = new ModificaAreaMagazzinoCommand();
        modificaAreaMagazzinoCommand.addCommandInterceptor(areaMagazzinoCommandInterceptor);

        creaAreaMagazzinoCommand = new CreaAreaMagazzinoCommand();
        creaAreaMagazzinoCommand.addCommandInterceptor(new CreaAreaMagazzinoCommandInterceptor());

        cancellaAreaMagazzinoCommand = new CancellaAreaMagazzinoCommand();
        cancellaAreaMagazzinoCommand.addCommandInterceptor(new CancellaMagazzinoCommandInterceptor());

        stampaAreaMagazzinoSplitCommand = new StampaAreaMagazzinoSplitCommand(
                "stampaAreaMagazzinoInstallazioneCommand");

        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    @Override
    public JComponent createControl() {
        ComponentFactory componentFactory = (ComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        JPanel panelStatus = componentFactory.createPanel(new HorizontalLayout());
        panelStatus.add(stampaAreaMagazzinoSplitCommand.createButton());
        panelStatus.add(modificaAreaMagazzinoCommand.createButton());
        panelStatus.add(cancellaAreaMagazzinoCommand.createButton());
        panelStatus.add(amCommand.createButton());

        JComponent component = super.createControl();
        getErrorBar().add(panelStatus, BorderLayout.LINE_END);

        return component;
    }

    @Override
    protected Object doDelete() {
        manutenzioniBD.cancellaAreaInstallazione(((AreaInstallazione) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        AreaInstallazione areaInstallazione = (AreaInstallazione) this.getForm().getFormObject();
        return manutenzioniBD.salvaAreaInstallazione(areaInstallazione);
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getNewCommand(), toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
                toolbarPageEditor.getDeleteCommand(), new SeparatorActionCommand(), creaAreaMagazzinoCommand };
    }

    @Override
    public void loadData() {
        // Non utilizzato
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        super.postSetFormObject(object);

    }

    @Override
    public void refreshData() {
        // Non utilizzato
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

    @Override
    public void updateCommands() {
        super.updateCommands();
        AreaInstallazione areaInstallazione = (AreaInstallazione) getPageObject();
        Integer idAreaMagazzino = areaInstallazione.getIdAreaMagazzino();

        creaAreaMagazzinoCommand.setVisible(idAreaMagazzino == null);
        amCommand.setVisible(idAreaMagazzino != null);
        modificaAreaMagazzinoCommand.setVisible(idAreaMagazzino != null);
        cancellaAreaMagazzinoCommand.setVisible(idAreaMagazzino != null);
        stampaAreaMagazzinoSplitCommand.setVisible(idAreaMagazzino != null);

        if (idAreaMagazzino != null) {
            AreaMagazzino areaMagazzino = new AreaMagazzino();
            areaMagazzino.setId(idAreaMagazzino);
            areaMagazzino = magazzinoDocumentoBD.caricaAreaMagazzino(areaMagazzino);
            stampaAreaMagazzinoSplitCommand.updateCommand(areaMagazzino);
        }
    }
}
