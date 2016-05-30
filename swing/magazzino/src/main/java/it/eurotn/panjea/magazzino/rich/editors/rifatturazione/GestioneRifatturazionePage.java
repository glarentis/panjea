/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.rifatturazione;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Classe che gestisce le associazioni fra le sedi magazzino e le sedi magazzino di rifatturazione.
 *
 * @author fattazzo
 *
 */
public class GestioneRifatturazionePage extends AbstractDialogPage implements IEditorCommands, IPageLifecycleAdvisor {

    public static final String PAGE_ID = "gestioneRifatturazionePage";

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    private SediMagazzinoPerRifatturazioneAssociateTablePage sediMagazzinoPerRifatturazioneAssociateTablePage;

    /**
     * Costruttore.
     */
    protected GestioneRifatturazionePage() {
        super(PAGE_ID);
    }

    @Override
    protected JComponent createControl() {

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        AssociazioneRifatturazioneControl associazioneRifatturazioneControl = new AssociazioneRifatturazioneControl(
                magazzinoAnagraficaBD, getSediMagazzinoPerRifatturazioneAssociateTablePage());
        rootPanel.add(associazioneRifatturazioneControl.getControl(), BorderLayout.NORTH);
        rootPanel.add(getSediMagazzinoPerRifatturazioneAssociateTablePage().getControl(), BorderLayout.CENTER);
        getSediMagazzinoPerRifatturazioneAssociateTablePage().loadData();

        return rootPanel;
    }

    @Override
    public void dispose() {

    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        return null;
    }

    /**
     * @return the sediMagazzinoPerRifatturazioneAssociateTablePage
     */
    public SediMagazzinoPerRifatturazioneAssociateTablePage getSediMagazzinoPerRifatturazioneAssociateTablePage() {
        if (sediMagazzinoPerRifatturazioneAssociateTablePage == null) {
            sediMagazzinoPerRifatturazioneAssociateTablePage = new SediMagazzinoPerRifatturazioneAssociateTablePage(
                    this.magazzinoAnagraficaBD);
        }

        return sediMagazzinoPerRifatturazioneAssociateTablePage;
    }

    @Override
    public void loadData() {

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

    }

    @Override
    public void preSetFormObject(Object object) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void restoreState(Settings settings) {
        getSediMagazzinoPerRifatturazioneAssociateTablePage().restoreState(settings);
    }

    @Override
    public void saveState(Settings settings) {
        getSediMagazzinoPerRifatturazioneAssociateTablePage().saveState(settings);
    }

    @Override
    public void setFormObject(Object object) {

    }

    /**
     * @param magazzinoAnagraficaBD
     *            the magazzinoAnagraficaBD to set
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

}
