package it.eurotn.panjea.giroclienti.rich.editors.scheda;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;

import it.eurotn.panjea.giroclienti.rich.editors.scheda.header.HeaderPanel;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.table.RigheSchedaGiroTable;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class SchedeGiroClientiPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private RigheSchedaGiroTable righeGiroTable;

    private HeaderPanel headerPanel;

    protected SchedeGiroClientiPage() {
        super("schedeGiroClientiPage");
    }

    @Override
    protected JComponent createControl() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));
        headerPanel = new HeaderPanel(this);
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        righeGiroTable = new RigheSchedaGiroTable();
        rootPanel.add(righeGiroTable.getComponent(), BorderLayout.CENTER);

        GuiStandardUtils.attachBorder(rootPanel);
        return rootPanel;
    }

    @Override
    public void dispose() {
        righeGiroTable.dispose();
    }

    /**
     * @return the righeGiroTable
     */
    public RigheSchedaGiroTable getRigheGiroTable() {
        return righeGiroTable;
    }

    @Override
    public void loadData() {
        headerPanel.reloadData();
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        // non faccio niente
    }

    @Override
    public void preSetFormObject(Object object) {
        // non faccio niente
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings arg0) {
        // non faccio niente
    }

    @Override
    public void saveState(Settings arg0) {
        // non faccio niente
    }

    @Override
    public void setFormObject(Object object) {
        // non faccio niente
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        // non faccio niente
    }

}
