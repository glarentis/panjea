package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.GuiStandardUtils;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.forms.AltriDatiEntitaForm;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class AltriDatiEntitaPage extends FormBackedDialogPageEditor {

    public static final String PAGE_ID = "altriDatiEntitaPage";

    private IAnagraficaBD anagraficaBD;
    private List<FormBackedDialogPageEditor> pagine = new ArrayList<FormBackedDialogPageEditor>();

    private Entita entita;

    /**
     * 
     * @param pluginManager
     *            pluginManager
     * @param entita
     *            entita per la pagina
     */
    public AltriDatiEntitaPage(final PluginManager pluginManager, final Entita entita) {
        super(PAGE_ID, new AltriDatiEntitaForm(pluginManager, entita));
    }

    @Override
    public JComponent createControl() {

        JPanel pannello = getComponentFactory().createPanel(new VerticalLayout(0));

        JComponent altriDatiPageComponent = super.createControl();
        GuiStandardUtils.attachBorder(altriDatiPageComponent, BorderFactory.createEmptyBorder(0, 0, -10, 10));
        pannello.add(altriDatiPageComponent);
        for (FormBackedDialogPageEditor pagina : pagine) {

            JComponent pageComponent = pagina.getForm().getControl();
            GuiStandardUtils.attachBorder(pageComponent, BorderFactory.createEmptyBorder(0, 10, 10, 10));
            pannello.add(pageComponent);
            this.getForm().getFormModel().addChild(pagina.getForm().getFormModel());
        }
        return pannello;
    }

    @Override
    protected Object doSave() {
        for (FormBackedDialogPageEditor pagina : pagine) {
            if (pagina.getForm().isDirty()) {
                pagina.onSave();
            }
        }

        Entita entitaDaSalvare = ((Entita) getBackingFormPage().getFormObject());
        Entita entitaResult = null;
        // FIXME rimuovere l'inizializzazione dei valori a null e spostarli all'interno del metodo restoreNullValue e
        // removeNullValue
        try {
            entitaResult = anagraficaBD.salvaEntita(entitaDaSalvare);
        } catch (AnagraficheDuplicateException e) {
            e.printStackTrace();
        }

        getBackingFormPage().setFormObject(entitaResult);

        return entitaResult;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
        return commands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    /**
     * 
     * @return List di pagine iniettate da xml.
     */
    public List<FormBackedDialogPageEditor> getPagine() {
        return pagine;
    }

    @Override
    public void loadData() {
        // i dati della sede magazzino sono gia settati
        for (IPageLifecycleAdvisor pagina : pagine) {
            pagina.loadData();
        }
    }

    @Override
    public ILock onLock() {
        ILock lock = super.onLock();
        // HACK devo ciclare le page perche' all'interno perche' come child sembra
        // che non ricevano la variazione del read only da verificare
        for (FormBackedDialogPageEditor page : pagine) {
            page.getForm().getFormModel().setReadOnly(false);
        }
        return lock;
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
    public void refreshData() {
        for (IPageLifecycleAdvisor pagina : pagine) {
            pagina.refreshData();
        }
        loadData();
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.eurotn.rich.editors.FormBackedDialogPageEditor#setFormObject(java.lang.Object)
     */
    @Override
    public void setFormObject(Object object) {
        super.setFormObject(object);

        this.entita = (Entita) object;

        for (IPageLifecycleAdvisor pagina : pagine) {
            pagina.setFormObject(object);
        }
    }

    /**
     * 
     * @param pagine
     *            pagine da inserire
     */
    public void setPagine(List<FormBackedDialogPageEditor> pagine) {
        this.pagine = pagine;
    }

}
