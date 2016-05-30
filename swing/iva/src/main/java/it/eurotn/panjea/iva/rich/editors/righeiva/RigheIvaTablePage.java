package it.eurotn.panjea.iva.rich.editors.righeiva;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.rich.commads.righeiva.CloseRigheIvaCommand;
import it.eurotn.panjea.iva.rich.forms.righeiva.AbstractAreaIvaModel;
import it.eurotn.panjea.iva.service.exception.AreaIvaPresenteException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * Property change che intercetta la validazione dell'areaIva e rilancia il firepropertyChange per
 * l'editor.
 *
 * @author Leonardo
 */
public class RigheIvaTablePage extends AbstractTablePageEditor<RigaIva>implements InitializingBean {

    /**
     * Property change che intercetta quando la lista delle righe iva viene modificata
     * preoccupandosi di lanciare se necessario il fireObjectChanged all'editor.
     *
     * @author Leonardo
     */
    private class AreaModelAggiornataChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(AbstractAreaIvaModel.AREA_MODEL_AGGIORNATA)) {
                RigheIvaTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                        evt.getNewValue());
                updateControl();
            }
        }
    }

    private class CloseRigheIvaCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            // Validando l'iva ritotalizzo il documento, quindi l'area iva non risulta più squadrata
            // (se lo era). Quindi
            // riaggiorno i totali e la label squadratura
            totaleRigheIvaPanel.update();
            RigheIvaTablePage.this.firePropertyChange(VALIDA_AREA_IVA, false, true);
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            return true;
        }

    }

    private class DeleteRigaIvaActionCommandInterceptor implements ActionCommandInterceptor {

        private EditFrame<RigaIva> editFrame;

        @Override
        public void postExecution(ActionCommand command) {
            boolean isQuadrata = areaIvaModel.isAreaIvaQuadrata();
            if (isQuadrata) {
                if (!areaIvaModel.isRigheIvaValide() && areaIvaModel.isValidazioneAreaIvaAutomatica()) {
                    getCloseRigheIvaCommand().execute();
                }
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            editFrame.getQuickActionCommand(EditFrame.QUICK_ACTION_DEFAULT).setSelected(true);
            return true;
        }

        /**
         * @param editFrame
         *            the editFrame to set
         */
        public void setEditFrame(EditFrame<RigaIva> editFrame) {
            this.editFrame = editFrame;
        }
    }

    private class SaveRigaIvaActionCommandInterceptor implements ActionCommandInterceptor {

        private EditFrame<RigaIva> editFrame;

        @Override
        public void postExecution(ActionCommand arg0) {
            boolean isQuadrata = areaIvaModel.isAreaIvaQuadrata();
            if (isQuadrata) {

                editFrame.getDockingManager().hideFrame(EditFrame.EDIT_FRAME_ID);
                if (!areaIvaModel.isRigheIvaValide() && areaIvaModel.isValidazioneAreaIvaAutomatica()) {
                    getCloseRigheIvaCommand().execute();
                }
            } else {
                // qui riabilito l'inserimento continuo solo se l'area iva non è quadrata
                editFrame.getQuickActionCommand(EditFrame.QUICK_ACTION_INSERT).setSelected(true);
            }

        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            // di default blocco l'inserimento continuo sul pre save, nel post save rimetto
            // l'inserimento continuo solo
            // se l'area iva non è quadrata
            editFrame.getQuickActionCommand(EditFrame.QUICK_ACTION_DEFAULT).setSelected(true);
            return true;
        }

        /**
         * @param editFrame
         *            the editFrame to set
         */
        public void setEditFrame(EditFrame<RigaIva> editFrame) {
            this.editFrame = editFrame;
        }
    }

    public static final String PAGE_ID = "righeIvaTablePage";
    public static final String VALIDA_AREA_IVA = "validaAreaIva";
    private AbstractAreaIvaModel areaIvaModel = null;
    private TotaleRigheIvaPanel totaleRigheIvaPanel = null;
    private AreaModelAggiornataChangeListener areaModelAggiornataChangeListener = null;

    private CloseRigheIvaCommand closeRigheIvaCommand;

    /**
     * Costruttore di default.
     */
    protected RigheIvaTablePage() {
        super(PAGE_ID, new RigheIvaTableModel(PAGE_ID));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        org.springframework.util.Assert.notNull(areaIvaModel, "areaIvaModel non puo' essere null ");
        // inizializzazione di AreaIvaModel in RigheIvaCommands
        totaleRigheIvaPanel = new TotaleRigheIvaPanel();

        ((RigaIvaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setAreaIvaModel(areaIvaModel);
        // aggiorno l'areaIva al pannello dei totali
        totaleRigheIvaPanel.setAreaIvaModel(this.areaIvaModel);
    }

    @Override
    protected EditFrame<RigaIva> createEditFrame() {
        final EditFrame<RigaIva> editFrame = new EditFrame<RigaIva>(EEditPageMode.POPUP, this,
                EditFrame.QUICK_ACTION_DEFAULT);
        editFrame.getEditViewTypeCommand().setVisible(false);

        SaveRigaIvaActionCommandInterceptor saveRigaIvaActionCommandInterceptor = new SaveRigaIvaActionCommandInterceptor();
        saveRigaIvaActionCommandInterceptor.setEditFrame(editFrame);
        DeleteRigaIvaActionCommandInterceptor deleteRigaIvaActionCommandInterceptor = new DeleteRigaIvaActionCommandInterceptor();
        deleteRigaIvaActionCommandInterceptor.setEditFrame(editFrame);

        ((ActionCommand) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).getEditorSaveCommand())
                .addCommandInterceptor(saveRigaIvaActionCommandInterceptor);
        ((ActionCommand) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).getEditorDeleteCommand())
                .addCommandInterceptor(deleteRigaIvaActionCommandInterceptor);
        return editFrame;
    }

    /**
     * @return AreaModelAggiornataChangeListener
     */
    private PropertyChangeListener getAreaIvaChangeListener() {
        if (areaModelAggiornataChangeListener == null) {
            areaModelAggiornataChangeListener = new AreaModelAggiornataChangeListener();
        }
        return areaModelAggiornataChangeListener;
    }

    /**
     *
     * @return area Iva model
     */
    public AbstractAreaIvaModel getAreaIvaModel() {
        return areaIvaModel;
    }

    /**
     * @return CloseRigheIvaCommand
     */
    public CloseRigheIvaCommand getCloseRigheIvaCommand() {
        logger.debug("--> Enter getCloseRigheIvaCommand");
        if (closeRigheIvaCommand == null) {
            closeRigheIvaCommand = new CloseRigheIvaCommand();
            closeRigheIvaCommand.setAreaIvaModel(areaIvaModel);
            closeRigheIvaCommand.addCommandInterceptor(new CloseRigheIvaCommandInterceptor());
        }
        return closeRigheIvaCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getCloseRigheIvaCommand() };
    }

    @Override
    public JComponent getFooterControl() {
        return totaleRigheIvaPanel;
    }

    @Override
    public Object getManagedObject(Object pageObject) {
        return areaIvaModel.getObject();
    }

    @Override
    public void grabFocus() {
        // se le righe non sono quadrate oppure sono quadrate ma nonsono valide allora il focus deve
        // essere preso da
        // questa page (righe iva)
        boolean righeQuadrate = areaIvaModel.isAreaIvaQuadrata();
        if (!righeQuadrate || (righeQuadrate && !areaIvaModel.isRigheIvaValide())) {
            super.grabFocus();
        }
    }

    @Override
    public void loadData() {

        // nel caso in cui l'area magazzino sia nuova o non sia provvisoria mostro il messaggio
        // caricamento in corso
        // altrimenti no. Questo perchè durante l'inserimento del documento più volte può essere
        // chiamato il metodo (
        // salvataggio della riga magazzino, totalizzazione, ecc...) e siccome le rate vengono
        // ricalcolate solo alla
        // conferma del documento non faccio vedere all'utente il messaggio di caricamento.
        boolean showRefreshMessage = false;
        if (getAreaIvaModel().getAreaDocumento() instanceof AreaMagazzino) {
            AreaMagazzino areaMagazzino = (AreaMagazzino) getAreaIvaModel().getAreaDocumento();
            showRefreshMessage = areaMagazzino.isNew()
                    || areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO;
        }

        if (showRefreshMessage) {
            super.loadData();
        } else {
            processTableData(null);
        }
    }

    @Override
    public Collection<RigaIva> loadTableData() {
        return new ArrayList<RigaIva>();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        super.postSetFormObject(object);
        this.areaIvaModel.setObject(object);

        if (this.areaIvaModel.isAreaIvaPresente()) {
            getTable().getOverlayTable().setMessage(null);
        } else {
            getTable().getOverlayTable().setStyledMessage(
                    "{" + RcpSupport.getMessage(AbstractAreaIvaModel.AREA_IVA_NON_PRESENTE) + ":f:gray}");
        }
    }

    @Override
    public void processTableData(Collection<RigaIva> results) {
        // controllo se il tipo documento non prevede le righe iva
        if ((this.areaIvaModel.getAreaDocumento().getDocumento().getTipoDocumento().getId() != null)
                && (!this.areaIvaModel.getAreaDocumento().getDocumento().getTipoDocumento().isRigheIvaEnable())) {
            if (this.areaIvaModel.isAreaIvaPresente()) {
                // se non e' null lancio una eccezione per notificare l'errore
                throw new AreaIvaPresenteException(
                        "Esiste un'area iva per il documento anche se il tipo documento non la prevede.");
            }
        }

        List<RigaIva> righeIva = new ArrayList<RigaIva>();
        if (this.areaIvaModel.isAreaIvaPresente()) {
            righeIva = this.areaIvaModel.getAreaIva().getRigheIva();
        }

        setRows(righeIva);
        areaIvaModel.ricaricaAreaIva();
        totaleRigheIvaPanel.update();
        updateControl();
    }

    @Override
    public void refreshData() {

        // nel caso in cui l'area magazzino sia nuova o non sia provvisoria mostro il messaggio
        // caricamento in corso
        // altrimenti no. Questo perchè durante l'inserimento del documento più volte può essere
        // chiamato il metodo (
        // salvataggio della riga magazzino, totalizzazione, ecc...) e siccome le rate vengono
        // ricalcolate solo alla
        // conferma del documento non faccio vedere all'utente il messaggio di caricamento.
        boolean showRefreshMessage = false;
        if (getAreaIvaModel().getAreaDocumento() instanceof AreaMagazzino) {
            AreaMagazzino areaMagazzino = (AreaMagazzino) getAreaIvaModel().getAreaDocumento();
            showRefreshMessage = areaMagazzino.isNew()
                    || areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO;
        }

        if (showRefreshMessage) {
            super.refreshData();
        } else {
            processTableData(null);
        }
    }

    @Override
    public Collection<RigaIva> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param areaIvaModel
     *            The areaIvaModel to set.
     */
    public void setAreaIvaModel(AbstractAreaIvaModel areaIvaModel) {
        this.areaIvaModel = areaIvaModel;
        this.areaIvaModel.addPropertyChangeListener(AbstractAreaIvaModel.AREA_MODEL_AGGIORNATA,
                getAreaIvaChangeListener());
    }

    @Override
    public void setFormObject(Object object) {

    }

    /**
     * Aggiorna lo stato readOnly di questa tablePage verificando se il tipoDocumento prevede l'area
     * iva e se l'areaIva corrente e' nuova o esiste; se la parte iva di questa area iva e' gia'
     * valida il pulsante di conferma risulta essere disabilitato.
     */
    private void updateControl() {
        logger.debug("--> Enter updateControl");
        boolean pageReadOnly = !areaIvaModel.isEnabled();
        this.setReadOnly(pageReadOnly);
        getCloseRigheIvaCommand().setEnabled(!pageReadOnly);
    }
}
