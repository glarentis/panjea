package it.eurotn.rich.dialog;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.CompositeDialogPage;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Raggruppa una serie di DialogPageEditor, gestisce il lifeCycle e la comunicazione fra le varie
 * pagine Il lyfecycle delle pages e' in ordine:<br>
 * <ul>
 * <li>preSetFormObject</li>
 * <li>setFormObject</li>
 * <li>postSetFormObject</li>
 * <li>onPrePageOpen</li>
 * <li>loadData o refreshData</li>
 * <li>onPostPageOpen</li>
 * </ul>
 * .
 *
 * @author giangi
 */
public abstract class JecCompositeDialogPage extends CompositeDialogPage implements Memento, InitializingBean {

    /**
     * In caso di form dirty ed errori chiedo se chiudere l'editor oppure di lasciarlo aperto per
     * continuare l'editazione.
     */
    private final class ConfirmationNotCommittableDialog extends ConfirmationDialog {

        private IPageEditor dialogPage;
        private boolean closeEditor;

        /**
         * Costruttore.
         *
         */
        private ConfirmationNotCommittableDialog() {
            super();
            String title = getApplicationContext().getMessage("panjea.title.confirm.error.save", null,
                    Locale.getDefault());
            this.setTitle(title);
        }

        /**
         * @return <code>true</code> se è possibile chiudere l'editor
         */
        public boolean isCloseEditor() {
            return closeEditor;
        }

        @Override
        protected void onCancel() {
            closeEditor = false;
            super.onCancel();
        }

        @Override
        protected void onConfirm() {
            closeEditor = true;
            // Rimuovo un eventuale lock sull'oggetto della pagina
            // Rimuovo il lock sull'oggetto
            if (dialogPage.isLocked()) {
                dialogPage.unLock();
            }
        }

        /**
         * @param dialogPage
         *            pagina da gestire
         */
        public void setDialogPage(IPageEditor dialogPage) {
            String title = PanjeaSwingUtil.removeHtml(((DialogPage) dialogPage).getTitle());
            String message = getApplicationContext().getMessage("panjea.message.confirm.error.save",
                    new Object[] { title }, Locale.getDefault());
            this.setConfirmationMessage(message);
            this.dialogPage = dialogPage;
        }
    }

    /**
     *
     * Sono stati cambiati i global command all'interno delal pagina attiva.<br/>
     * Devo avvisare l'editor per registrarli nuovamente.
     *
     * @author giangi
     * @version 1.0, 02/nov/2010
     *
     */
    private class GlobalCommandsChangedListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (GLOBAL_COMMAND_PROPERTY.equals(e.getPropertyName())) {
                firePropertyChange(PAGE_ACTIVE_PROPERTY, null, e.getNewValue());
            }
        }
    }

    /**
     * Chiama il metodo adeguato per indicare che e' stato cambiato l'oggetto all'interno di una
     * pagina.
     *
     * @author Leonardo
     */
    private class ObjectChangedListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            logger.debug("--> Property change event " + e);

            if (IPageLifecycleAdvisor.OBJECT_CHANGED.equals(e.getPropertyName())) {
                JecCompositeDialogPage.this.currentObject = e.getNewValue();
                logger.debug(
                        "--> Property change per la proprieta IPageLifecycleAdvisor.FORM_OBJECT_CHANGED lanciato da "
                                + e.getSource());
                objectChange(e.getNewValue(), (DialogPage) e.getSource());
                // lancio il fire per notificare all'editor che e' registrato
                // che il currentObject e' cambiato
                JecCompositeDialogPage.this.firePropertyChange(JecCompositeDialogPage.CURRENT_OBJECT_CHANGED_PROPERTY,
                        null, JecCompositeDialogPage.this.currentObject);
            }
        }
    }

    /**
     * In caso di form dirty chiedo se salvare e quindi chiudere, non salvare e quindi chiudere o
     * annullare l'operazione lasciando aperto l'editor.
     */
    private final class SaveDialog extends ConfirmationDialog {

        /**
         * Se selezionato l'abort command non chiudo l'editor e non rimuovo il lock.
         *
         * @author giangi
         */
        public final class AbortCommand extends ActionCommand {

            /**
             * Costruttore.
             *
             */
            public AbortCommand() {
                setId(DEFAULT_ABORT_COMMAND_ID);
            }

            @Override
            protected void doExecuteCommand() {
                closeEditor = false;
                dispose();
            }
        }

        protected static final String DEFAULT_ABORT_COMMAND_ID = "abortCommand";
        private IPageEditor dialogPage;
        private boolean closeEditor;
        private AbortCommand abortCommand;

        /**
         * Costruttore.
         *
         */
        private SaveDialog() {
            super();
            String title = getApplicationContext().getMessage("panjea.title.confirm.save", null, Locale.getDefault());
            this.setTitle(title);
        }

        /**
         * @return abortCommand
         */
        private AbstractCommand getAbortCommand() {
            if (abortCommand == null) {
                abortCommand = new AbortCommand();
            }
            return abortCommand;
        }

        /**
         * Restituisco i due comandi standard FinishCommand e CancelCommand ed iserisco un comando
         * per annullare la richiesta.
         *
         * @return comandi
         */
        @Override
        protected Object[] getCommandGroupMembers() {
            return new AbstractCommand[] { getFinishCommand(), getCancelCommand(), getAbortCommand() };
        }

        /**
         * @return <code>true</code> indica che è possibile chiudere l'editor
         */
        public boolean isCloseEditor() {
            return closeEditor;
        }

        @Override
        protected void onCancel() {
            super.onCancel();
            logger.debug("-->l'utente ha annullato il salvataggio dei dati alla chisura della pagina "
                    + dialogPage.getPageEditorId());
            closeEditor = true;
            // Rimuovo un eventuale lock sull'oggetto della pagina
            // Rimuovo il lock sull'oggetto
            if (dialogPage.isLocked()) {
                dialogPage.unLock();
            }
        }

        @Override
        protected void onConfirm() {
            logger.debug("-->l'utente ha confermato il salvataggio dei dati alla chiusura della pagina "
                    + dialogPage.getPageEditorId());
            dialogPage.onSave();
            // Rimuovo un eventuale lock sull'oggetto della pagina
            // Rimuovo il lock sull'oggetto
            if (dialogPage.isLocked()) {
                dialogPage.unLock();
            }
            logger.debug("-->Salvati i dati sulla chiusura dell'editor");
            closeEditor = true;
        }

        /**
         * @param dialogPage
         *            pagina da gestire
         */
        public void setDialogPage(IPageEditor dialogPage) {
            String title = PanjeaSwingUtil.removeHtml(((DialogPage) dialogPage).getTitle());
            String message = getMessage("panjea.message.confirm.save", new Object[] { title });
            this.setConfirmationMessage(message);
            this.dialogPage = dialogPage;
        }
    }

    public static final String PAGE_ACTIVE_PROPERTY = "activePage";

    public static final String GLOBAL_COMMAND_PROPERTY = "globalCommand";

    public static final String SHOW_INIT_PAGE = "showInitPage";
    public static final String CURRENT_OBJECT_CHANGED_PROPERTY = "currentObjectChanged";
    private Logger logger = Logger.getLogger(JecCompositeDialogPage.class);
    protected boolean enableOnOpen = false;
    protected Settings settings;

    /**
     * Pagine caricate.
     */
    protected Map<String, Boolean> pagesLoaded = new HashMap<String, Boolean>();

    /**
     * Pagine con i dati non aggiornati.
     */
    protected Map<String, Boolean> pagesDirty = new HashMap<String, Boolean>();
    protected Object currentObject = null;

    private ObjectChangedListener objectChangeListener;

    private PropertyChangeListener globalCommandsChangeListener;

    /**
     * linkedList per tenere traccia delle pagine e per gestire il passaggio previous/next tra di
     * esse.
     */
    private List<String> pagesLinkedList = null;

    private List<String> idPages;

    /**
     * Costruttore.
     *
     * @param pageId
     *            id della composite
     */
    public JecCompositeDialogPage(final String pageId) {
        super(pageId);
        logger.debug("---> Enter JecCompositeDialogPage");
    }

    /**
     * Sovrascrivo la addPage di CompositeDialogPage per aggiungere alla mia linkedList di id dialog
     * pages ogni page aggiunta sia dalla docked che dalla button composite.
     *
     * @param page
     *            pagina da aggiungere
     */
    @Override
    public void addPage(DialogPage page) {
        super.addPage(page);
        getPagesLinkedList().add(page.getId());
    }

    /**
     * Creo una pagina dal riferimento del bean.
     *
     * @param idPage
     *            id della pagina
     */
    public void addPage(String idPage) {
        if (idPage.startsWith("gruppo:")) {
            logger.debug("-->Salto il gruppo " + idPage + " sulla AddPage");
        } else {
            DialogPage pagina = (DialogPage) Application.instance().getApplicationContext().getBean(idPage);
            if (pagina != null) {
                logger.debug("-->Inserisco la pagina " + idPage);
                addPage(pagina);
            } else {
                logger.error("-->Non posso inserire la pagina " + idPage + " perche' il bean non esiste.");
                throw new RuntimeException(idPage + " non esiste");
            }
        }
    }

    /**
     * Deve essere sovrascritta per aggiungere una pagina manualmente.
     */
    @Override
    protected void addPages() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        // aggiungo tutte le eventuali pagine configurate
        if (idPages != null) {
            for (String pageId : idPages) {
                addPage(pageId);
            }
        }
    }

    /**
     * Indica se posso chiudere la pagina. Controlla le pagine contenute e se ci sono delle
     * modifiche da salvare chiede se salvarle.
     *
     * @return Se almeno una pagina non e' committabile ritorna false (es :errori nel form)
     */
    public boolean canClose() {
        boolean result = true;
        // npe da mail. se ho errore posso chiudere
        try {
            for (DialogPage page : getDialogPages()) {
                // Se e' una IPageEditor devo controllare se e' in stato dirty
                // NB. La DialogTablePage non e' una IPageEditor, infatti
                // non viene utilizzata per modificare i dati ma solo per
                // visualizzarli
                // quindi non puo' essere in stato dirty
                SaveDialog saveDialog = new SaveDialog();
                ConfirmationNotCommittableDialog confirmationDialog = new ConfirmationNotCommittableDialog();
                if (page instanceof IPageEditor) {
                    IPageEditor pageEditor = (IPageEditor) page;
                    // Controllo se la pagina e' caricata e dirty
                    if (isPageLoaded(page.getId()) && pageEditor.isDirty()) {
                        // controlla se committable quindi non ci sono errori
                        if (pageEditor.isCommittable()) {
                            saveDialog.setDialogPage(pageEditor);
                            saveDialog.setPreferredSize(new Dimension(250, 50));
                            saveDialog.setResizable(false);
                            saveDialog.setModal(true);
                            saveDialog.showDialog();
                            // sa result e' false deve rimanere false
                            result = result && saveDialog.isCloseEditor();
                        } else {
                            // ci sono errori nel form
                            confirmationDialog.setDialogPage(pageEditor);
                            confirmationDialog.setPreferredSize(new Dimension(250, 50));
                            confirmationDialog.setResizable(false);
                            confirmationDialog.setModal(true);
                            confirmationDialog.showDialog();
                            result = result && confirmationDialog.isCloseEditor();
                        }
                    } else if (isPageLoaded(page.getId()) && pageEditor.isLocked()) {
                        // se la pagina e' caricata e il lock risulta essere attivo (ho
                        // in modifica un oggetto)
                        // allora devo sbloccarlo pena ritrovarmi un lock imprevisto.
                        pageEditor.unLock();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("-->errore nella canclose ", e);
        }
        return result;
    }

    /**
     * Implementazione di default per la compositedialogPage contenuta nell'AbstractEditorDialogPage
     * il focus viene richiesto da this, e subito dopo viene inoltrata la richiesta da parte
     * dell'active page.
     */
    public void componentFocusGained() {
        logger.debug("--> componentFocusGained passo il focus alla page");
        // giro i
        // Se la pagina implementa IPageLifecycle posso chiamare la sua gestione
        // altrimenti mi limito
        // a richiedere il focus dal control dell'active page.
        if (getActivePage() instanceof Focussable) {
            Focussable page = (Focussable) getActivePage();
            page.grabFocus();
        } else {
            if (getActivePage() != null && getActivePage().getControl() != null) {
                getActivePage().getControl().requestFocusInWindow();
            }
        }
        logger.debug("--> Exit componentFocusGained");
    }

    @Override
    protected JComponent createControl() {
        logger.debug("-->ENTER createControl");
        addPages();
        JComponent controlli = createPageControl();
        if (getPages().size() != 0) {
            setActivePage(pagesLinkedList.get(0));
        }
        logger.debug("-->EXIT createControl");
        return controlli;
    }

    /**
     * Crea i controlli per la pagina. In questo metodo definisco come visualizzare le varie pagine.
     *
     * @return controlli della pagina
     */
    protected abstract JComponent createPageControl();

    /**
     * Non devo utilizzare la createPageControls nelle classi derivate perche' carico le pagine in
     * modalita' Lazy (compresi i controlli) e il metodo caricherebbe tutte le pagine.
     */
    @Override
    protected void createPageControls() {
        throw new UnsupportedOperationException("Implementare la createPageControl al posto della createPageControls");
    }

    /**
     * Esegue la dispose della composite.
     */
    public void dispose() {
        logger.debug("-->ENTER dispose");
        for (DialogPage page : getDialogPages()) {
            if (isPageLoaded(page.getId())) {
                logger.debug("-->Rimuovo objectChangeListener dalla pagina " + page.getId());
                page.removePropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, objectChangeListener);
                page.removePropertyChangeListener(GLOBAL_COMMAND_PROPERTY, globalCommandsChangeListener);
                page = null;
            }
        }
        objectChangeListener = null;
        logger.debug("-->EXIT dispose");
    }

    /**
     * Funzione utilizzata solamente per aggiungere la dichiarazione di tipizzazione sull'array di
     * DialogList getPages nella classe base (compositeDialogPage) ritorna una lista non tipizzata
     * ed e' contenuta nel package springRCP non modificabile.
     *
     * @return List<DialogPage>
     */
    @SuppressWarnings("unchecked")
    public List<DialogPage> getDialogPages() {
        return getPages();
    }

    /**
     * Recupera la pagina.
     *
     * @param idPage
     *            id della pagina
     * @return pagina recuperata
     */
    protected DialogPage getPage(String idPage) {
        logger.debug("-->ENTER getPage");
        for (DialogPage page : getDialogPages()) {
            if (page.getId().equals(idPage)) {
                logger.debug("-->EXIT getPage con la page " + page);
                return page;
            }
        }
        logger.debug("-->EXIT getPage con valore null");
        return null;
    }

    /**
     * Restituisce una linkedList contenente le dialogPages in ordine di inserimento.
     *
     * @return List<String> pagine configurate
     */
    protected List<String> getPagesLinkedList() {
        if (pagesLinkedList == null) {
            pagesLinkedList = new LinkedList<String>();
        }
        return pagesLinkedList;
    }

    /**
     * Metodo che definisce come cambiare la pagina attiva; il comportamento standard definisce che
     * viene chiamato il metodo setActivePage, funziona solo nel caso in cui i controlli delle
     * pagine sono creati e disponibili.
     *
     * @param idDialogPage
     *            id della pagina
     */
    protected void handleChangeActivePage(String idDialogPage) {
        setActivePage(idDialogPage);
        JecCompositeDialogPage.this.componentFocusGained();
    }

    /**
     * Metodo che attiva la prima pagina della lista ordinata di pagine.
     */
    public void handleGoToFirstPage() {
        if (getPagesLinkedList() != null && !getPagesLinkedList().isEmpty()) {
            String firstPage = ((LinkedList<String>) getPagesLinkedList()).getFirst();
            handleChangeActivePage(firstPage);
        }
    }

    /**
     * Metodo che data la pagina attiva trova la successiva, se isCycleLoopActive una volta arrivato
     * alla fine ricomincia il giro, altrimenti si ferma.
     */
    public void handleGoToNextPage() {
        String idActivePage = getActivePage().getId();
        String requestedNextPage = requestPage(idActivePage, true, isCycleLoopActive());
        handleChangeActivePage(requestedNextPage);
    }

    /**
     * Metodo che data la pagina attiva trova la precedente, se isCycleLoopActive una volta arrivato
     * alla fine ricomincia il giro, altrimenti si ferma.
     */
    public void handleGoToPreviousPage() {
        String idActivePage = getActivePage().getId();
        String requestedPreviousPage = requestPage(idActivePage, false, isCycleLoopActive());
        handleChangeActivePage(requestedPreviousPage);
    }

    /**
     * Definisce per le classi derivate se continuare a ciclare tra le pagine presenti o una volta
     * raggiunto l'ultimo elemento di interrompere.
     *
     * @return di default continua a ciclare tra le pagine
     */
    protected boolean isCycleLoopActive() {
        return true;
    }

    /**
     * @return <code>true</code> se almeno una pagina risulta essere dirty
     */
    public boolean isDirty() {
        boolean pageDirty = false;
        for (DialogPage page : getDialogPages()) {
            if (page instanceof IPageEditor) {
                IPageEditor pageEditor = (IPageEditor) page;

                // Alla prima pagina dirty ritorno true
                if (isPageLoaded(page.getId()) && pageEditor.isDirty()) {
                    pageDirty = true;
                    break;
                }
            }
        }
        return pageDirty;
    }

    /**
     * Definisce se l'editor supporta la modifica del layout. In caso verrà inserito sulla tab
     * dell'editor il command ripristinaLayout che chiama il restoreLayout dell'editor.
     *
     * @return <code>true</code> se la modifica è supportata
     */
    public boolean isLayoutSupported() {
        return false;
    }

    /**
     * @return isLocked
     */
    public boolean isLocked() {
        for (DialogPage page : getDialogPages()) {
            if (page instanceof IPageEditor && isPageLoaded(page.getId())) {
                if (((IPageEditor) page).isLocked()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return stato del blocco del layout
     */
    public boolean isLockLayout() {
        return false;
    }

    @Override
    public boolean isPageComplete() {
        for (DialogPage page : getDialogPages()) {
            IPageEditor pageEditor = (IPageEditor) page;
            // Alla prima pagina non commitabile ritorno false
            if (isPageLoaded(page.getId()) && !pageEditor.isCommittable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica la presenza nella mappa di pagine caricate l'id della pagina richiesta.
     *
     * @param pageId
     *            l'id della dialog page da verificare se caricata
     * @return true se la pagina e' stata caricata, false altrimenti
     */
    protected boolean isPageLoaded(String pageId) {
        return pagesLoaded.containsKey(pageId);
    }

    /**
     * crea effettivamente i controlli della dialogPage e aggiunge il property change listener per
     * ricevere notifica dei possibili aggiornamenti del form object che sono lanciati dalle dialog
     * pages tramite la chiamata
     * firePropertyChange(IPageLifecycleAdvisor.FORM_OBJECT_CHANGED,null,newObj).
     *
     * @param page
     *            la dialog page di cui creare i controlli
     */
    protected void loadPage(DialogPage page) {
        logger.debug("--> Enter loadpage per la pagina " + page);

        if (page instanceof FormBackedDialogPageEditor) {
            FormBackedDialogPageEditor formBackedDialogPage = (FormBackedDialogPageEditor) page;
            // Abilito o meno il form in relazione alla proprieta enableOnOpen
            if (formBackedDialogPage.getBackingFormPage().getFormModel() != null) {
                formBackedDialogPage.getBackingFormPage().getFormModel().setReadOnly(!this.enableOnOpen);
            }
        }

        // JComponent control = page.getControl();
        // serve per notificare i messaggi di errori dai figli a this, se non
        // viene registrato
        // i messaggi di errore non vengono visualizzati nell'header della
        // pagina.

        // page.addPropertyChangeListener(childChangeHandler);

        // serve per notificare il cambio dell'oggetto contenuto nell'editor
        // e quindi predisporre il suo aggiornamento a tutte le pagine presenti
        // in this
        if (objectChangeListener == null) {
            objectChangeListener = new ObjectChangedListener();
        }

        if (globalCommandsChangeListener == null) {
            globalCommandsChangeListener = new GlobalCommandsChangedListener();
        }
        // Rimuovo la property change perchè se viene richiamata la load e la pagina è aperta
        // registrerei il listener
        // più volte
        try {
            page.removePropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, objectChangeListener);
            page.removePropertyChangeListener(GLOBAL_COMMAND_PROPERTY, globalCommandsChangeListener);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("--> property change non esistente");
            }
        }
        page.addPropertyChangeListener(GLOBAL_COMMAND_PROPERTY, globalCommandsChangeListener);
        page.addPropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, objectChangeListener);
        pagesLoaded.put(page.getId(), true);

        // TODO non ripristinare nel caso in cui cambio oggetto qunado il
        // workspace non chiude e riapre l'editor
        // all'apertura del pageComponent verifico se implementa memento.
        // in caso affermativo chiamo il metodo restoreState() qui viene solo
        // chiamata la restoreState,
        // per quanto riguarda la chiusura della pagina il saveState viene
        // gestito nel workspaceview,
        // non distinguendo di quale classe sia il pageComponent da chiudere in
        // quale workspace.
        if (page instanceof Memento) {
            logger.debug(
                    "--> Apertura pageComponent nel jecbuttoncompositedialogpage " + page.getId() + " " + settings);
            Memento memento = (Memento) page;
            if (settings != null) {
                try {
                    memento.restoreState(settings);
                } catch (Exception e) {
                    logger.error("--> Errore nel ripristinare il memento di " + page.getId(), e);
                }
            }
        }
    }

    /**
     * Da sovrascrivere quando si deve bloccare/sbloccare la possibilità di modificare un layout se
     * supportato dalla composite.
     *
     * @param lock
     *            true o false per bloccare o sbloccare il layout
     */
    public void lockLayout(boolean lock) {
    }

    /**
     * Chiamata quando viene cambiato l'oggetto di dominio gestito dalla pagina. In questo metodo la
     * classe derivata deve decidere come invalidare le pagine Ad esempio la button invalida tutte
     * le pagine ma non lancia una process La docking invalida le pagine e lancia subito una process
     * a tutte perche' sono visualizzate tutte assieme
     *
     * @param domainObject
     *            nuovo oggetto ricevuto
     * @param pageSource
     *            la pagina che ha lanciato la changeObject
     */
    protected abstract void objectChange(Object domainObject, DialogPage pageSource);

    /**
     * definisce come viene processata la dialog page a seconda che sia gia stata caricata
     * (pagesLoaded) o se e' stata segnalata per un aggiornamento dei dati contenuti (pagesDirty).
     *
     * @param page
     *            pagina da processare
     * @return <code>true</code> se la pagina è stata processata
     */
    protected boolean processDialogPage(DialogPage page) {
        logger.debug("--> Enter processDialogPage per la page " + page.getId());
        // se non e' stata caricata la pagina la processo creando i controlli
        boolean newPage = false; // Indica se una pagina e' nuova ed e' stata
        // caricata

        if (!isPageLoaded(page.getId())) {
            logger.debug("-->Load della pagina " + page);
            newPage = true;
            loadPage(page);
            // Metto subito la pagina a dirty cosi' dopo la load gestisco il
            // lifecycle
            pagesDirty.put(page.getId(), true);
        }

        if (page instanceof IPageLifecycleAdvisor) {
            IPageLifecycleAdvisor pageLifecycleAdvisor = (IPageLifecycleAdvisor) page;

            if (pagesDirty.containsKey(page.getId())) {
                pageLifecycleAdvisor.preSetFormObject(currentObject);
                // Setto il formObject prima di chiamare la OnPrePageOpen
                // perche' sulla OnPrePageOpen devo controllare eventuali dati
                // del nuovo oggetto
                pageLifecycleAdvisor.setFormObject(PanjeaSwingUtil.cloneObject(currentObject));

                pageLifecycleAdvisor.postSetFormObject(currentObject);
            }
            // verifico se posso processare la pagina
            if (!pageLifecycleAdvisor.onPrePageOpen()) {
                System.out.println("-->Non processo la pagina perchè il prePageOpen ritorna false");
                return false;
            }

            if (pagesDirty.containsKey(page.getId())) {
                if (newPage) {
                    pageLifecycleAdvisor.loadData();
                } else {
                    pageLifecycleAdvisor.refreshData();
                }
                // rimuovo dalla mappa la pagina appena processata
                pagesDirty.remove(page.getId());
            }
            if (isControlCreated()) {
                pageLifecycleAdvisor.onPostPageOpen();
            }
        }
        logger.debug("--> Exit processDialogPage");
        return true;
    }

    /**
     * Dato l'id della pagina attiva richiede la dialogpage successiva o precedente a seconda del
     * parametro goToNext; con il parametro loop decido invece se ciclare in modo continuo tra le
     * pagine o se muovermi da inizio a fine.
     *
     * @param idActivePage
     *            pagina attiva corrente
     * @param goToNext
     *            true cerca la successiva, false la precedente
     * @param loop
     *            se true arrivato all'ultima richiede la prima, se false restituisce la stessa;
     *            invece se true dalla prima tornando indietro richiedo l'ultima, se false
     *            restituisco la stessa.
     * @return l'id della pagina successiva o precedente
     */
    private String requestPage(String idActivePage, boolean goToNext, boolean loop) {
        logger.debug("--> Enter requestPage pagina attiva " + idActivePage + " direzione avanti " + goToNext + " loop "
                + loop);
        int positionActivePage = ((LinkedList<String>) getPagesLinkedList()).indexOf(idActivePage);
        if (goToNext) {
            positionActivePage++;
        }
        String pageToGo = idActivePage;
        ListIterator<String> listIterator = ((LinkedList<String>) getPagesLinkedList())
                .listIterator(positionActivePage);

        // se cerco la pagina successiva
        if (goToNext) {
            // se ha un successivo lo ritorno
            if (listIterator.hasNext()) {
                pageToGo = listIterator.next();
            } else {
                // sono alla fine della lista quindi se loop true ritorno il primo elemento
                if (loop) {
                    pageToGo = ((LinkedList<String>) getPagesLinkedList()).getFirst();
                }
            }
        } else {
            // se cerco la pagina precedente
            // se ha un precedente lo ritorno
            if (listIterator.hasPrevious()) {
                pageToGo = listIterator.previous();
            } else {
                // sono all'inizio della lista quindi se loop true ritorno l'ultimo elemento
                if (loop) {
                    pageToGo = ((LinkedList<String>) getPagesLinkedList()).getLast();
                }
            }
        }
        logger.debug("--> Exit requestPage " + pageToGo);
        return pageToGo;
    }

    /**
     * Da sovrascrivere quando si deve ripristinare un layout se supportato dalla composite.
     */
    public void restoreLayout() {
    }

    /**
     * Salva le settings per utilizzarle quando carico le pagine la prima volta. Le pagine sono Lazy
     * qundi chiamo la restoreState delle pagine solamente quando le carico la prima volta
     *
     * @param paramSettings
     *            settings
     */
    @Override
    public void restoreState(Settings paramSettings) {
        this.settings = paramSettings;
    }

    /**
     * Richiama la saveSate per ogni pagina caricata.
     *
     * @param setting
     *            settings
     */
    @Override
    public void saveState(Settings setting) {
        for (DialogPage page : getDialogPages()) {
            IPageLifecycleAdvisor pageEditor = (IPageLifecycleAdvisor) page;
            if (isPageLoaded(page.getId())) {
                pageEditor.saveState(setting);
            }
        }
    }

    /**
     * Aggiungo il firePropertyChange per la proprieta' activePage. In questo modo un ascoltatore
     * capisce quando cambio pagina Ad esempio l'AbstractEditorDialogPage registra i commandExecutor
     * per la pagina.
     *
     * @param activePage
     *            pagina da attivare
     */
    @Override
    public void setActivePage(DialogPage activePage) {
        super.setActivePage(activePage);
        firePropertyChange(PAGE_ACTIVE_PROPERTY, null, activePage);
        JecCompositeDialogPage.this.componentFocusGained();
    }

    /**
     * Cambia la pagina attiva con la dialogpage che e' identificata dal parametro stringa ricevuto
     * recuperando da esso la DialogPage.
     *
     * @param idDialogPage
     *            id della pagina da attivare
     */
    public void setActivePage(String idDialogPage) {
        setActivePage(getPage(idDialogPage));
    }

    /**
     * Setta l'oggetto per questa jecCompositeDialogPage.
     *
     * @param currentObject
     *            object
     */
    public void setCurrentObject(Object currentObject) {
        logger.debug("--> setCurrentObject " + currentObject);
        this.currentObject = currentObject;
        // se i controlli sono creati e quindi il valore di closeAndReopenEditor
        // di WorkspaceView
        // e' false allora i controlli saranno gia' creati ed e' quindi
        // sufficiente notificare alle dialog
        // pages contenute in this che ho un nuovo oggetto, altrimenti viene
        // seguito il normale lifecycle di
        // creazione.
        if (this.isControlCreated()) {
            objectChange(currentObject, null);
        }
    }

    /**
     * @param pages
     *            id delle dialog page da aggingere alla composite
     */
    public void setDialogPages(List<String> pages) {
        this.idPages = pages;
    }

    /**
     * @param page
     *            pagina da visualizzare
     */
    protected abstract void showComponentPage(DialogPage page);

    /**
     * metodo che provvede ad attivare e a mostrare la Page iniziale della
     * {@link CompositeDialogPage}.
     */
    protected void showInitialPage() {
        logger.debug("--> Enter showInitialPage");
        DialogPage page = (DialogPage) getPages().get(0);
        showPage(page);
        componentFocusGained();
        logger.debug("--> Exit showInitialPage");
    }

    /**
     * @param page
     *            pagina da visualizzare
     */
    public void showPage(DialogPage page) {
        logger.debug("--> Enter showPage");
        setActivePage(page);
        if (this.isControlCreated() && processDialogPage(page)) {
            // non invertire showComponentPage con setActivePage altrimenti il
            // pulsante della
            // pagina attiva non risulta piu' disabilitato
            showComponentPage(page);

            if (page instanceof IPageLifecycleAdvisor) {
                ((IPageLifecycleAdvisor) page).onPostPageOpen();
            }
        }
    }

    public void showPage(String idPage) {
        handleChangeActivePage(idPage);
    }

    public void unLock() {
        for (DialogPage page : getDialogPages()) {
            if (page instanceof IPageEditor && isPageLoaded(page.getId())) {
                ((IPageEditor) page).unLock();
            }
        }
    }

    /**
     * Metodo chiamato quando ad una dialogPage contenuta in questa compositeDialogPage viene
     * cambiata la property pageComplete o enabled (setEnabled() agisce direttamente su
     * setPageComplete()) Nota: Se sovrascritta nelle classi derivate deve chiamare la super.
     *
     * @param page
     *            pagina
     */
    @Override
    protected void updatePageComplete(DialogPage page) {
        super.updatePageComplete(page);
        logger.debug("--> updatePageVisibility " + page);
    }

    @Override
    protected void updatePageVisibility(DialogPage page) {
        super.updatePageVisibility(page);
        logger.debug("--> updatePageVisibility " + page);
    }
}
