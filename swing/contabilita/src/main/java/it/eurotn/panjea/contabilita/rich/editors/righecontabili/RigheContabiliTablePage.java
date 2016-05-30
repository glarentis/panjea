/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.AggregateTableModel;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.editors.areacontabile.DatiRitenutaAccontoCommand;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rate.rich.editors.rate.RateTablePage;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;
import it.eurotn.rich.control.table.style.FocusCellStyle;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * Pagina di gestione delle righe contabili dell'area contabile.
 *
 * @author fattazzo,Leonardo
 * @version 1.0, 23/mag/07
 */
public class RigheContabiliTablePage extends AbstractTablePageEditor<RigaContabile>
        implements InitializingBean, Observer {

    /**
     * Command interceptor per settare al command prima dell'esecuzione l'areaContabileFullDTO
     * corrente della table page.
     *
     * @author Leonardo
     */
    private class CloseRigheContabiliCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            // lancio l'evento solo se le righe contabili sono valide. Questo
            // per evitare, nel caso l'utente abbia
            // scelto di non confermare le righe, che il controllo venga passato
            // su di un'altra area o che venga
            // proposto un nuovo documento.
            AreaContabileFullDTO areaContabileFullDTOValidata = ((CloseRigheContabiliCommand) command)
                    .getAreaContabileFullDTO();
            if (areaContabileFullDTOValidata.getAreaContabile().isValidRigheContabili()) {
                RigheContabiliTablePage.this.firePropertyChange(OBJECT_CHANGED, null, areaContabileFullDTOValidata);
                areaContabileFullDTO = areaContabileFullDTOValidata;
                updateControl();
                RigheContabiliTablePage.this.firePropertyChange(VALIDA_RIGHE_CONTABILI, false, true);
            }
            if (areaContabileFullDTOValidata.getAreaRate() != null
                    && areaContabileFullDTOValidata.getAreaRate().getDatiValidazione().isValid()) {
                RigheContabiliTablePage.this.firePropertyChange(RateTablePage.VALIDA_AREA_RATE, false, true);
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            ((CloseRigheContabiliCommand) command).setAreaContabileFullDTO(areaContabileFullDTO);
            ((RigaContabilePage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).getEditorUndoCommand()
                    .execute();
            return true;
        }
    }

    private class RateiRiscontiCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public RateiRiscontiCommand() {
            super("rateiRiscontiCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).getEditorLockCommand().execute();
            RigaContabile rc = (RigaContabile) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).getPageObject();
            rc.setRateiRiscontiAttivi(!rc.isRateiRiscontiAttivi());
            rc.aggiornaImportoRateoRisconto();
            // rc.setImporto(null);
            ((IFormPageEditor) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).getForm().setFormObject(rc);
        }
    }

    private class RicreaRigheContabiliCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public RicreaRigheContabiliCommand() {
            super("ricreaRigheContabiliCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ConfirmationDialog dialog = new ConfirmationDialog("Attenzione", "Ricreare le tutte le righe contabili?") {

                @Override
                protected void onConfirm() {
                    List<RigaContabile> righePresenti = getTable().getRows();
                    if (righePresenti != null && !righePresenti.isEmpty()) {
                        AreaContabile areaContabile = contabilitaBD
                                .cancellaRigheContabili(areaContabileFullDTO.getAreaContabile());
                        getTable().setRows(new ArrayList<RigaContabile>());

                        areaContabileFullDTO.setAreaContabile(areaContabile);
                        RigheContabiliTablePage.this.firePropertyChange(OBJECT_CHANGED, null, areaContabileFullDTO);
                    }
                    RigheContabiliTablePage.this.creaRigheContabili();
                }
            };
            dialog.showDialog();

        }
    }

    private class RigheContabiliTableCellStyleProvider extends DefaultCellStyleProvider {
        @Override
        public CellStyle getCellStyleAt(TableModel model, int row, int col) {
            RigheContabiliTableModel tableModel = (RigheContabiliTableModel) TableModelWrapperUtils
                    .getActualTableModel(model);
            CellStyle result = null;
            int actualRow = ((AggregateTableModel) model).getActualRowAt(row);

            if (actualRow != -1 && !(tableModel.getObject(actualRow)).isValid()) {
                result = INVALID_STYLE;
            } else {
                result = super.getCellStyleAt(model, row, col);
            }
            return result;
        }
    }

    private class SaveCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            // Se ho salvato una riga nuova verifico se sono
            // sbilanciato, in caso ne preparo un'altra nuova.
            RigaContabile rigaContabile = (RigaContabile) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)
                    .getPageObject();
            try {
                if (rigaContabile.getVersion() == 0 && getTableModel() != null && getTableModel().getSbilancio() != null
                        && getTableModel().getSbilancio().intValue() != 0) {
                    ((RigaContabilePage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).onNew();
                }
            } catch (NullPointerException e) {
                // arrivata una null pointer traminte mail, ma non capisco il perchè,
                // provo a loggare
                logger.error("-->errore nel preparare la nuova riga.Questo log non viene visualizzato all'utente,", e);
            }

            // Se ho i ratei attivi devo aggiungere/modificare la riga contabile del rateo/risconto
            if (rigaContabile.isRateiRiscontiAttivi()) {
                // recupero la riga collegata
                RigaContabile rigaCollegata = rigaContabile.getRigaRateoRiscontoDocumento();
                if (rigaCollegata != null && rigaCollegata.getVersion() == 0) {
                    // ricarico le righe
                    RigaContabile rigaSelezionata = getTable().getSelectedObject();
                    onRefresh();
                    if (rigaSelezionata != null) {
                        getTable().selectRowObject(rigaSelezionata, null);
                    }
                } else {
                    rigaCollegata = contabilitaBD.caricaRigaContabile(rigaCollegata.getId()); // Per
                                                                                              // ricaricare
                                                                                              // i
                                                                                              // valori
                    // lazy //
                    getTable().replaceOrAddRowObject(rigaCollegata, rigaCollegata, null);
                }
            }
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            return true;
        }

    }

    public static final CellStyle INVALID_STYLE = new CellStyle();

    static {
        INVALID_STYLE.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    public static final String PAGE_ID = "righeContabiliTablePage";
    public static final String VALIDA_RIGHE_CONTABILI = "validaRigheContabili";

    private IContabilitaBD contabilitaBD = null;
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;

    private CloseRigheContabiliCommand closeRigheContabiliCommand = null;
    private DatiRitenutaAccontoCommand datiRitenutaAccontoCommand = null;
    private RicreaRigheContabiliCommand ricreaRigheContabiliCommand = null;

    private TotaleRigheContabiliPanel totaleRigheContabiliPanel = null;
    private AreaContabileFullDTO areaContabileFullDTO;
    private RateiRiscontiCommand rateRiscontiCommand;

    /**
     * Costruttore.
     */
    protected RigheContabiliTablePage() {
        super(PAGE_ID, new RigheContabiliTableModel(PAGE_ID));
        // getTable().setTableCustomizer(new RigheContabiliTableCustomizer());
        ((AggregateTable) getTable().getTable()).getAggregateTableModel()
                .setCellStyleProvider(new RigheContabiliTableCellStyleProvider());
        ((CellStyleTable) getTable().getTable()).setFocusCellStyle(new FocusCellStyle());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.totaleRigheContabiliPanel = new TotaleRigheContabiliPanel();
        totaleRigheContabiliPanel.setRigheContabiliTableModel(getTableModel());

        ((RigaContabilePage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
                .setRigheContabiliTableModel(getTableModel());
    }

    /**
     * Verifica se i dati della ritenuta d'acconto sono cambiati.
     *
     * @param areaContabileFullDTOOld
     *            .
     * @param areaContabileFullDTOparam
     *            .
     * @return .
     */
    private boolean checkCambioDatiRitenuta(AreaContabileFullDTO areaContabileFullDTOOld,
            AreaContabileFullDTO areaContabileFullDTOparam) {
        boolean result = false;

        if (areaContabileFullDTOOld != null && areaContabileFullDTOparam != null) {
            result = areaContabileFullDTOparam.getAreaContabile().getDatiRitenutaAccontoAreaContabile()
                    .isChanged(areaContabileFullDTOOld.getAreaContabile().getDatiRitenutaAccontoAreaContabile());
        }

        return result;
    }

    /**
     * Crea le righe contabili in base alla stuttura impostata su area contabile.
     */
    public void creaRigheContabili() {

        // apro i dati della ritenuta d'acconto se richiesti dal tipo area contabile
        if (areaContabileFullDTO.getAreaContabile().getTipoAreaContabile().isRitenutaAcconto()) {
            getDatiRitenutaAccontoCommand().setAreaContabile(areaContabileFullDTO.getAreaContabile());
            getDatiRitenutaAccontoCommand().execute();
        }

        // verifico se e' necessario generare le righe contabili in automatico;
        // in caso affermativo genera le righe
        RigheContabiliBuilder righeContabiliBuilder = new RigheContabiliBuilder(contabilitaAnagraficaBD, contabilitaBD,
                areaContabileFullDTO, getTableModel());
        List<RigaContabile> righeGenerate = righeContabiliBuilder.generaRigheContabili();
        if (!righeGenerate.isEmpty()) {
            setRows(righeGenerate);
        }

        // Se non ho righe o se ho sbilanci preparo la riga vuota
        if (getTable().getRows().isEmpty() || getTableModel().getSbilancio().intValue() != 0) {
            this.getNewCommands().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).execute();
            this.getControl().requestFocus();
        }
        updateControl();
    }

    @Override
    protected EditFrame<RigaContabile> createEditFrame() {

        final EditFrame<RigaContabile> editFrame = new RigheContabiliEditFrame(EEditPageMode.DETAIL, this,
                EditFrame.QUICK_ACTION_DEFAULT);

        ((ActionCommand) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).getEditorSaveCommand())
                .addCommandInterceptor(new SaveCommandInterceptor());
        return editFrame;
    }

    /**
     * @return CloseRigheContabiliCommand
     */
    public CloseRigheContabiliCommand getCloseRigheContabiliCommand() {
        if (closeRigheContabiliCommand == null) {
            closeRigheContabiliCommand = new CloseRigheContabiliCommand(contabilitaBD, getTableModel());
            closeRigheContabiliCommand.addCommandInterceptor(new CloseRigheContabiliCommandInterceptor());
        }
        return closeRigheContabiliCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        PluginManager plugin = RcpSupport.getBean(PluginManager.BEAN_ID);
        if (plugin.isPresente(PluginManager.PLUGIN_RATEI_RISCONTI)) {
            rateRiscontiCommand = new RateiRiscontiCommand();
            return new AbstractCommand[] { rateRiscontiCommand, getRicreaRigheContabiliCommand(),
                    getCloseRigheContabiliCommand() };
        } else {
            return new AbstractCommand[] { getRicreaRigheContabiliCommand(), getCloseRigheContabiliCommand() };
        }
    }

    /**
     * @return the datiRitenutaAccontoCommand
     */
    public DatiRitenutaAccontoCommand getDatiRitenutaAccontoCommand() {
        if (datiRitenutaAccontoCommand == null) {
            datiRitenutaAccontoCommand = new DatiRitenutaAccontoCommand();
            datiRitenutaAccontoCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                @Override
                public void postExecution(ActionCommand command) {
                    AreaContabile areaContabile = ((DatiRitenutaAccontoCommand) command).getAreaContabile();
                    // controllo se l'area contabile è cambiata ( modifica dei dati della ritenuta
                    // d'acconto)
                    if (areaContabileFullDTO.getAreaContabile().getId().compareTo(areaContabile.getId()) == 0
                            && areaContabileFullDTO.getAreaContabile().getVersion()
                                    .compareTo(areaContabile.getVersion()) != 0) {
                        areaContabileFullDTO.setAreaContabile(areaContabile);
                        RigheContabiliTablePage.this.firePropertyChange(OBJECT_CHANGED, null, areaContabileFullDTO);
                    }
                    super.postExecution(command);
                }
            });
        }
        return datiRitenutaAccontoCommand;
    }

    @Override
    public JComponent getFooterControl() {
        return totaleRigheContabiliPanel;
    }

    @Override
    public Object getManagedObject(Object rigaContabile) {
        AreaContabile areaContabileOld = areaContabileFullDTO.getAreaContabile();
        areaContabileFullDTO.setAreaContabile(((RigaContabile) rigaContabile).getAreaContabile());
        AreaContabile areaContabileNew = areaContabileFullDTO.getAreaContabile();
        if (areaContabileOld.getStatoAreaContabile() != StatoAreaContabile.PROVVISORIO
                && areaContabileNew.getStatoAreaContabile() == StatoAreaContabile.PROVVISORIO) {
            RigheContabiliTablePage.this.firePropertyChange(OBJECT_CHANGED, null, areaContabileFullDTO);
        }
        return areaContabileFullDTO;
    }

    /**
     * @return the ricreaRigheContabiliCommand
     */
    public RicreaRigheContabiliCommand getRicreaRigheContabiliCommand() {
        if (ricreaRigheContabiliCommand == null) {
            ricreaRigheContabiliCommand = new RicreaRigheContabiliCommand();
        }
        return ricreaRigheContabiliCommand;
    }

    /**
     *
     * @return table Model della pagina
     */
    public RigheContabiliTableModel getTableModel() {
        return (RigheContabiliTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable().getModel());
    }

    @Override
    public Collection<RigaContabile> loadTableData() {
        return areaContabileFullDTO.getRigheContabili();
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    protected void onRefresh() {
        postSetFormObject(contabilitaBD.caricaAreaContabileFullDTO(areaContabileFullDTO.getId()));
        firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaContabileFullDTO);
        setRows(areaContabileFullDTO.getRigheContabili());
        updateControl();
    }

    @Override
    public void postSetFormObject(Object object) {
        AreaContabileFullDTO nuovaAreaContabile = (AreaContabileFullDTO) object;
        boolean ricaricaRighe = object != null && this.areaContabileFullDTO != null
                && nuovaAreaContabile.getAreaContabile().getDocumento().getEntita() != null
                && !nuovaAreaContabile.getAreaContabile().getDocumento().getEntita()
                        .equals(this.areaContabileFullDTO.getAreaContabile().getDocumento().getEntita());

        ricaricaRighe = ricaricaRighe || checkCambioDatiRitenuta(areaContabileFullDTO, nuovaAreaContabile);

        this.areaContabileFullDTO = nuovaAreaContabile;
        ((RigaContabilePage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
                .setAreaContabile(areaContabileFullDTO.getAreaContabile());
        getCloseRigheContabiliCommand().setAreaContabileFullDTO(areaContabileFullDTO);
        if (ricaricaRighe) {
            // loadData();
            setRows(areaContabileFullDTO.getRigheContabili());
        }
        updateControl();
    }

    @Override
    public void processTableData(Collection<RigaContabile> results) {
        super.processTableData(results);
        updateControl();
    }

    @Override
    public Collection<RigaContabile> refreshTableData() {
        Collection<RigaContabile> result = null;
        if (areaContabileFullDTO.getAreaContabile().isNew()) {
            result = new ArrayList<RigaContabile>();
        }

        return result;
    }

    /**
     * Metodo per ricaricare le righe e l'area contabile collegata.
     */
    public void ricaricaRighe() {
        onRefresh();
    }

    /**
     * @param contabilitaAnagraficaBD
     *            the contabilitaAnagraficaBD to set
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);
        updateControl();
    }

    /**
     * Aggiorna lo stato readonly della pagina.
     *
     */
    private void updateControl() {
        logger.debug("--> Enter updateEnablePageAndCommand");
        boolean readOnly = false;

        if (areaContabileFullDTO.getAreaContabile().isNew()) {
            readOnly = true;
        }

        getRicreaRigheContabiliCommand().setEnabled(!readOnly);

        // se l'area iva e' abilitata e presente e non confermata
        if (areaContabileFullDTO.isAreaIvaEnable() && !areaContabileFullDTO.getAreaContabile().isValidRigheContabili()
                && !areaContabileFullDTO.isAreaIvaValid()) {
            readOnly = true;
        }

        if (this.areaContabileFullDTO.getAreaContabile().getStatoAreaContabile()
                .equals(StatoAreaContabile.VERIFICATO)) {
            readOnly = true;
        }

        this.setReadOnly(readOnly);
        getCloseRigheContabiliCommand().setEnabled(!readOnly);
        if (getTable().getSelectedObject() != null && rateRiscontiCommand != null) {
            rateRiscontiCommand.setEnabled(!getTable().getSelectedObject().isRateiRiscontiAttivi());
            logger.debug("--> Exit updateEnablePageAndCommand");
        }
    }
}
