package it.eurotn.panjea.cauzioni.rich.editors.entita.situazionecauzioni;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.cauzioni.rich.bd.ICauzioniBD;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniEntitaDTO;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class SituazioneCauzioniEntitaTablePage extends AbstractTablePageEditor<SituazioneCauzioniEntitaDTO>
        implements InitializingBean {

    private class OpenMovimentazioneArticoloCommand extends ApplicationWindowAwareCommand {

        @Override
        protected void doExecuteCommand() {

            // SituazioneCauzioniEntitaDTO situazioneCauzioniEntitaDTO = getTable().getSelectedObject();

            // if (situazioneCauzioniEntitaDTO != null) {
            // ParametriRicercaMovimentazioneArticolo parametri = new ParametriRicercaMovimentazioneArticolo();
            // parametri.setArticoloLite(situazioneCauzioniEntitaDTO.getArticolo());
            // parametri.setEntitaLite(entita.getEntitaLite());
            // parametri.setSedeEntita(situazioneCauzioniEntitaDTO.getSedeEntita());
            //
            // LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
            // Application.instance().getApplicationContext().publishEvent(event);
            // }
        }

    }

    private class RaggruppaSediActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SituazioneCauzioniEntitaTableModel model = (SituazioneCauzioniEntitaTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel());
            model.setFiltraSediEntita(usaRaggruppamentoSediCheckBox.isSelected());

            refreshTableData();
        }

    }

    public static final String PAGE_ID = "situazioneCauzioniEntitaTablePage";
    private static final String USA_RAGGRUPPAMENTO_SEDI = "usaRaggruppamentoSediCauzioni";

    private static SituazioneCauzioniEntitaTableModel tableModel = new SituazioneCauzioniEntitaTableModel();

    private ICauzioniBD cauzioniBD;

    private Entita entita;
    private JCheckBox usaRaggruppamentoSediCheckBox;

    private RaggruppaSediActionListener raggruppaSediActionListener;

    /**
     * 
     * Costruttore.
     */
    protected SituazioneCauzioniEntitaTablePage() {
        super(PAGE_ID, tableModel);
        getTable().setPropertyCommandExecutor(new OpenMovimentazioneArticoloCommand());
        getTable().setTableType(TableType.HIERARCHICAL);
        getTable().setHierarchicalTableComponentFactory(new SituazioneCauzioniEntitaHierarchilacComponent());

        usaRaggruppamentoSediCheckBox = new JCheckBox();
        usaRaggruppamentoSediCheckBox.setText(RcpSupport.getMessage(USA_RAGGRUPPAMENTO_SEDI));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tableModel.setCauzioniBD(cauzioniBD);
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getRefreshCommand() };
    }

    @Override
    public JComponent getHeaderControl() {
        JPanel headPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));
        headPanel.add(usaRaggruppamentoSediCheckBox, BorderLayout.NORTH);
        GuiStandardUtils.attachBorder(headPanel);
        return headPanel;
    }

    @Override
    public Collection<SituazioneCauzioniEntitaDTO> loadTableData() {

        List<SituazioneCauzioniEntitaDTO> situazioni = new ArrayList<SituazioneCauzioniEntitaDTO>();

        if (entita != null && entita.getId() != null) {
            situazioni = cauzioniBD.caricaSituazioneCauzioniEntita(entita.getId(),
                    usaRaggruppamentoSediCheckBox.isSelected());
        }

        return situazioni;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (entita.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "entita.null.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(entita.getDomainClassName(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

    @Override
    public Collection<SituazioneCauzioniEntitaDTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
        super.restoreState(settings);
        usaRaggruppamentoSediCheckBox.setSelected(settings.getBoolean(USA_RAGGRUPPAMENTO_SEDI));

        raggruppaSediActionListener = new RaggruppaSediActionListener();
        usaRaggruppamentoSediCheckBox.addActionListener(raggruppaSediActionListener);
    }

    @Override
    public void saveState(Settings settings) {
        super.saveState(settings);
        settings.setBoolean(USA_RAGGRUPPAMENTO_SEDI, usaRaggruppamentoSediCheckBox.isSelected());

        // rimuovo il listener dalla check
        usaRaggruppamentoSediCheckBox.removeActionListener(raggruppaSediActionListener);

        SituazioneCauzioniEntitaTableModel model = (SituazioneCauzioniEntitaTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel());
        model.setFiltraSediEntita(usaRaggruppamentoSediCheckBox.isSelected());
    }

    /**
     * @param cauzioniBD
     *            the cauzioniBD to set
     */
    public void setCauzioniBD(ICauzioniBD cauzioniBD) {
        this.cauzioniBD = cauzioniBD;
    }

    @Override
    public void setFormObject(Object object) {
        this.entita = (Entita) object;
    }

}
