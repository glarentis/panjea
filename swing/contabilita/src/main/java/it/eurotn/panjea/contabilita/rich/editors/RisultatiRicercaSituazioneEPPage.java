package it.eurotn.panjea.contabilita.rich.editors;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.dialog.TabbedDialogPage;
import org.springframework.richclient.settings.Settings;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.SituazioneEpDTO;
import it.eurotn.rich.editors.IPageEditor;

/**
 * TabbedPage che presenta la situazione E/P che consiste in 3 istanze di SituazioneEPPage (extends
 * AbstractTreeTableDialogPageEditor) che rappresentano le tab.:
 * <ul>
 * <li>Situazione patrimoniale
 * <li>Profitti e perdite
 * <li>Conti d'ordine
 * </ul>
 * La gestione del ciclo di vita della page si ripercuote su ogni page contenuta.
 *
 * @author giangi,Leonardo
 */
public class RisultatiRicercaSituazioneEPPage extends TabbedDialogPage implements IPageEditor {

    private static final String PAGE_ID = "risultatiRicercaSituazioneEPPage";
    private SituazioneEPPage patrimonialePage = null;
    private SituazioneEPPage profittiPerditePage = null;
    private SituazioneEPPage contiOrdinePage = null;
    private IContabilitaBD contabilitaBD = null;
    private ParametriRicercaSituazioneEP parametriRicercaSituazioneEP = null;
    private AziendaCorrente aziendaCorrente = null;

    /**
     * Costruttore.
     */
    public RisultatiRicercaSituazioneEPPage() {
        super(PAGE_ID);
    }

    /**
     * Aggiungo manualmente le pages nella tabbed.
     */
    @Override
    public void addPages() {
        patrimonialePage = new SituazioneEPPage("situazionePatrimonialePage", aziendaCorrente);
        profittiPerditePage = new SituazioneEPPage("profittiPerditePage", aziendaCorrente);
        contiOrdinePage = new SituazioneEPPage("contiOrdinePage", aziendaCorrente);
        addPage(patrimonialePage);
        addPage(profittiPerditePage);
        addPage(contiOrdinePage);
    }

    @Override
    public void dispose() {
        patrimonialePage.dispose();
        profittiPerditePage.dispose();
        contiOrdinePage.dispose();
    }

    /**
     *
     * @return azienda correntemente loggata
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     *
     * @return bd contabilità
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
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

    @Override
    public String getPageEditorId() {
        return null;
    }

    @Override
    public Object getPageObject() {
        return null;
    }

    @Override
    public String getPageSecurityEditorId() {
        return null;
    }

    @Override
    public void grabFocus() {
        getControl().requestFocusInWindow();
    }

    @Override
    public boolean isCommittable() {
        return false;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void loadData() {
        SituazioneEpDTO situazioneEpDTO = new SituazioneEpDTO();
        if ((parametriRicercaSituazioneEP != null) && (parametriRicercaSituazioneEP.isEffettuaRicerca())) {
            // Carico i dati della ricerca e li passo alle tre pagine
            situazioneEpDTO = contabilitaBD.caricaSituazioneEconomicaPatrimoniale(parametriRicercaSituazioneEP);
        }
        patrimonialePage.setFormObject(situazioneEpDTO.getPatrimoniale());
        patrimonialePage.setParametriRicercaSituazioneEP(parametriRicercaSituazioneEP);
        patrimonialePage.loadData();

        profittiPerditePage.setFormObject(situazioneEpDTO.getEconomica());
        profittiPerditePage.setParametriRicercaSituazioneEP(parametriRicercaSituazioneEP);
        profittiPerditePage.loadData();

        contiOrdinePage.setFormObject(situazioneEpDTO.getOrdine());
        contiOrdinePage.setParametriRicercaSituazioneEP(parametriRicercaSituazioneEP);
        contiOrdinePage.loadData();
    }

    @Override
    public Object onDelete() {
        return null;
    }

    @Override
    public ILock onLock() {
        return null;
    }

    @Override
    public void onNew() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public boolean onSave() {
        return false;
    }

    @Override
    public boolean onUndo() {
        return false;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    /**
     * Sovrascrivo per evitare l'aggiunta del bordo alla tabbed.
     *
     * @param page
     *            pagina per il dialogo
     *
     * @see org.springframework.richclient.dialog.CompositeDialogPage#prepareDialogPage(org.springframework.richclient.dialog.DialogPage)
     */
    @Override
    protected void prepareDialogPage(DialogPage page) {
        // page.addPropertyChangeListener(childChangeHandler);
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings settings) {
        patrimonialePage.restoreState(settings);
        profittiPerditePage.restoreState(settings);
        contiOrdinePage.restoreState(settings);
    }

    @Override
    public void saveState(Settings settings) {
        patrimonialePage.saveState(settings);
        profittiPerditePage.saveState(settings);
        contiOrdinePage.saveState(settings);
    }

    /**
     *
     * @param aziendaCorrente
     *            azienda loggata
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     *
     * @param contabilitaBD
     *            bd contabilità
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaSituazioneEP) {
            parametriRicercaSituazioneEP = (ParametriRicercaSituazioneEP) object;
        } else {
            parametriRicercaSituazioneEP = new ParametriRicercaSituazioneEP();
        }

    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unLock() {
    }
}
