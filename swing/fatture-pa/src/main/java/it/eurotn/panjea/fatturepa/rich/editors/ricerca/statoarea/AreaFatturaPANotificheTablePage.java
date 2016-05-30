package it.eurotn.panjea.fatturepa.rich.editors.ricerca.statoarea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class AreaFatturaPANotificheTablePage extends AbstractTablePageEditor<AreaNotificaFatturaPADTO> {

    private class SaveCommandInterceptor extends ActionCommandInterceptorAdapter {
        @Override
        public void postExecution(ActionCommand command) {
            refreshData();
        }
    }

    private IFatturePABD fatturePABD;

    private Integer idAreaMagazzino;
    private AreaMagazzinoFatturaPA areaMagazzinoFatturaPA;
    private AreaNotificaFatturaPaPage areaNotificaFatturaPaPage;

    private SaveCommandInterceptor saveCommandInterceptor;

    protected AreaFatturaPANotificheTablePage() {
        super("areaFatturaPANotificheTablePage", new String[] { "data", "statoFatturaPA" },
                AreaNotificaFatturaPADTO.class);
        this.fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);
        this.areaNotificaFatturaPaPage = new AreaNotificaFatturaPaPage();

        this.saveCommandInterceptor = new SaveCommandInterceptor();
        ((it.eurotn.rich.editors.ToolbarPageEditor.SaveCommand) this.areaNotificaFatturaPaPage.getEditorSaveCommand())
                .addCommandInterceptor(saveCommandInterceptor);
    }

    private JComponent createEditNotificaComponent() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        rootPanel.add(areaNotificaFatturaPaPage.getControl(), BorderLayout.CENTER);
        rootPanel.setPreferredSize(new Dimension(200, 300));

        GuiStandardUtils.attachBorder(rootPanel);

        return rootPanel;
    }

    @Override
    public void dispose() {
        ((it.eurotn.rich.editors.ToolbarPageEditor.SaveCommand) this.areaNotificaFatturaPaPage.getEditorSaveCommand())
                .removeCommandInterceptor(saveCommandInterceptor);
        saveCommandInterceptor = null;

        areaNotificaFatturaPaPage.dispose();

        super.dispose();
    }

    @Override
    public JComponent getControl() {
        JComponent tableComponent = super.getControl();
        tableComponent.setPreferredSize(new Dimension(300, 200));

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(tableComponent, BorderLayout.WEST);
        panel.add(createEditNotificaComponent(), BorderLayout.CENTER);

        return panel;
    }

    @Override
    public Collection<AreaNotificaFatturaPADTO> loadTableData() {
        return fatturePABD.caricaAreaNotificheFatturaPA(areaMagazzinoFatturaPA.getId());
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public Collection<AreaNotificaFatturaPADTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        this.idAreaMagazzino = (Integer) object;

        this.areaMagazzinoFatturaPA = fatturePABD.caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
        areaNotificaFatturaPaPage.setIdAreaMagazzino(idAreaMagazzino);
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);

        areaNotificaFatturaPaPage.onUndo();

        AreaNotificaFatturaPADTO areaNotificaFatturaPADTO = (AreaNotificaFatturaPADTO) obj;
        areaNotificaFatturaPaPage
                .setFormObject(fatturePABD.caricaAreaNotificaFatturaPA(areaNotificaFatturaPADTO.getId()));
    }

}
