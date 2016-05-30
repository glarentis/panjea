package it.eurotn.panjea.compoli.rich.editors.entitacointestate;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.compoli.rich.bd.ComunicazionePolivalenteBD;
import it.eurotn.panjea.compoli.rich.bd.IComunicazionePolivalenteBD;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.EntitaCointestazione;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.PanjeaTitledApplicationDialog;

public class EntitaCointestateDialog extends PanjeaTitledApplicationDialog {

    private AreaContabile areaContabile;

    private EntitaCointestateTableModel tableModel = new EntitaCointestateTableModel();
    private JideTableWidget<EntitaCointestazione> entitaTable = new JideTableWidget<>("entitaCointestateTable",
            tableModel);

    private IComunicazionePolivalenteBD comunicazionePolivalenteBD;

    /**
     * Costruttore.
     */
    public EntitaCointestateDialog() {
        super();
        comunicazionePolivalenteBD = RcpSupport.getBean(ComunicazionePolivalenteBD.BEAN_ID);
    }

    @Override
    protected JComponent createTitledDialogContentPane() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(entitaTable.getComponent(), BorderLayout.CENTER);
        return rootPanel;
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[] { getFinishCommand() };
    }

    @Override
    protected String getFinishCommandId() {
        return "okCommand";
    }

    @Override
    protected String getTitle() {
        return "Entità cointestatarie";
    }

    @Override
    protected boolean isMessagePaneVisible() {
        return false;
    }

    @Override
    protected void onAboutToShow() {
        super.onAboutToShow();

        tableModel.setAreaContabile(areaContabile);
        entitaTable.setRows(new ArrayList<EntitaCointestazione>());
        if (areaContabile != null && !areaContabile.isNew()) {
            List<EntitaCointestazione> entitaArea = comunicazionePolivalenteBD
                    .caricaEntitaCointestazioneByAreaContabile(areaContabile.getId());
            entitaTable.setRows(entitaArea);
        }
    }

    @Override
    protected boolean onFinish() {
        return true;
    }

    /**
     * @param areaContabile
     *            the areaContabile to set
     */
    public void setAreaContabile(AreaContabile areaContabile) {
        this.areaContabile = areaContabile;
    }

}
