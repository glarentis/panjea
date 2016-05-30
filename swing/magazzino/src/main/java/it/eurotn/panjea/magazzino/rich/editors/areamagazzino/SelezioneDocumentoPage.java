/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.editors.documento.DocumentiTablePage;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.SelezioneDocumentoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Page per la selezione di {@link Documento}.
 *
 * @author adriano
 * @version 1.0, 10/set/2008
 */
public class SelezioneDocumentoPage extends FormBackedDialogPageEditor {

    private class FindCommand extends ActionCommand {

        private static final String COMMAND_ID = "searchCommand";

        /**
         * Costruttore.
         *
         */
        public FindCommand() {
            super(COMMAND_ID);
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            setSecurityControllerId(getPageEditorId() + "." + COMMAND_ID);
            c.configure(this);

        }

        @Override
        protected void doExecuteCommand() {
            SelezioneDocumentoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    SelezioneDocumentoPage.this.getBackingFormPage().getFormObject());
        }

    }

    private class ResetCommand extends ActionCommand {

        private static final String COMMAND_ID = "resetCommand";

        /**
         * Costruttore.
         *
         */
        public ResetCommand() {
            super(COMMAND_ID);
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            setSecurityControllerId(getPageEditorId() + "." + COMMAND_ID);
            c.configure(this);

        }

        @Override
        protected void doExecuteCommand() {
            Documento documento = new Documento();
            SelezioneDocumentoPage.this.setFormObject(documento);
            SelezioneDocumentoPage.this.firePropertyChange(SelezioneDocumentoPage.RESET_CHANGED, null, documento);
        }

    }

    /**
     * TablePage per la visualizzazione e selezione dei risultati della ricerca.
     *
     * @author adriano
     * @version 1.0, 10/set/2008
     */
    public class RisultatiDocumentoTablePage extends DocumentiTablePage implements PropertyChangeListener {

        private class SelectDocumentoCommand extends ActionCommand {

            @Override
            protected void doExecuteCommand() {
                // recupera l'oggetto selezionato e lancia un PropertyChange per comunicare ai suoi
                // listeners la
                // selezione di object
                Object objectTable = getTable().getSelectedObject();
                RisultatiDocumentoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                        objectTable);
            }

        }

        private static final String PAGE_ID = "risultatiDocumentoTablePage";

        private Documento documentoToFind;
        private SelectDocumentoCommand selectDocumentoCommand = null;

        /**
         * Costruttore.
         */
        public RisultatiDocumentoTablePage() {
            super(PAGE_ID);
        }

        @Override
        public AbstractCommand[] getCommands() {
            getSelectDocumentoCommand();
            return super.getCommands();
        }

        /**
         * @return SelectDocumentoCommand
         */
        private SelectDocumentoCommand getSelectDocumentoCommand() {
            if (selectDocumentoCommand == null) {
                selectDocumentoCommand = new SelectDocumentoCommand();
                getTable().setPropertyCommandExecutor(selectDocumentoCommand);
            }
            return selectDocumentoCommand;
        }

        @Override
        public Collection<Documento> loadTableData() {
            return ricercaDocumentiSenzaAreaMagazzino();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (IPageLifecycleAdvisor.OBJECT_CHANGED.equals(evt.getPropertyName())) {
                documentoToFind = (Documento) evt.getNewValue();
                loadData();
                // richiama componentFocusGained per cedere il focus alla table
                // se presenta risultati
                grabFocus();
            } else if (SelezioneDocumentoPage.RESET_CHANGED.equals(evt.getPropertyName())) {
                // set di documentoToFind recuperando il nuovo object di event e
                // set della table con un Collection vuota
                documentoToFind = (Documento) evt.getNewValue();
                setRows(new ArrayList<Documento>());
            }
        }

        /**
         * @return lista di documenti senza area magazzino
         */
        private List<Documento> ricercaDocumentiSenzaAreaMagazzino() {
            logger.debug("--> Enter ricercaDocumentiSenzaAreaMagazzino");
            List<Documento> documenti = magazzinoDocumentoBD.ricercaDocumentiSenzaAreaMagazzino(documentoToFind, true);
            logger.debug("--> Exit ricercaDocumentiSenzaAreaMagazzino");
            return documenti;
        }

    }

    private static final String PAGE_ID = "selezioneDocumentoPage";

    // propertyName per la comunicazione del comando di reset parametri
    private static final String RESET_CHANGED = "resetChanged";

    private JPanel panel = null;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private RisultatiDocumentoTablePage risultatiDocumentoTablePage = null;

    private ResetCommand resetCommand;

    private FindCommand findCommand;

    /**
     * @param parentPageId
     * @param backingFormPage
     */
    public SelezioneDocumentoPage() {
        super(PAGE_ID, new SelezioneDocumentoForm());
        setShowTitlePane(false);
    }

    @Override
    public JComponent createControl() {
        if (panel == null) {
            panel = getComponentFactory().createPanel(new BorderLayout());
            panel.add(super.createControl(), BorderLayout.NORTH);
            panel.add(getRisultatiDocumentoTablePage().getControl(), BorderLayout.CENTER);
        }
        return panel;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetCommand(), getFindCommand() };
        return abstractCommands;
    }

    /**
     * @return findCommand
     */
    private FindCommand getFindCommand() {
        if (findCommand == null) {
            findCommand = new FindCommand();
        }
        return findCommand;
    }

    /**
     * @return resetCommand
     */
    private ResetCommand getResetCommand() {
        if (resetCommand == null) {
            resetCommand = new ResetCommand();
        }
        return resetCommand;
    }

    /**
     *
     * @return risultatiDocumentoTablePage.
     */
    public RisultatiDocumentoTablePage getRisultatiDocumentoTablePage() {
        if (risultatiDocumentoTablePage == null) {
            risultatiDocumentoTablePage = new RisultatiDocumentoTablePage();
            addPropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, risultatiDocumentoTablePage);
            risultatiDocumentoTablePage.getControl();
        }
        return risultatiDocumentoTablePage;
    }

    @Override
    public void grabFocus() {
        // cede il focus alla table se questa contiene dei record
        if ((risultatiDocumentoTablePage.getTable() != null) && risultatiDocumentoTablePage.getTable().getRows() != null
                && (risultatiDocumentoTablePage.getTable().getRows().size() != 0)) {
            risultatiDocumentoTablePage.grabFocus();
        } else {
            // altrimenti viene ceduto ai componenti all'interno del form di
            // ricerca
            super.grabFocus();
        }

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
    public void refreshData() {
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

}
