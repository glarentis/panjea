package it.eurotn.rich.binding.searchtext;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.QuickFilterField;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.weaklistener.WeakKeyListener;

/**
 *
 */
public class SearchTextController
        implements KeyListener, PropertyChangeListener, ActionCommandInterceptor, ActionCommandExecutor {

    private class InputVerifySearch extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                if (searchPanel.getPopUp().isClosingPopup()) {
                    currentSearchTextField.revert();
                    return true;
                }
                if (searchPanel.getPopUp().isPopupVisible()) {
                    selectTableObject();
                    return false;
                }
                if (!currentSearchTextField.isAdjustingMode()) {
                    lastSearchForVerify = true;
                    searchThread = new SearchThread(currentSearchTextField.getPropertyRenderer(),
                            currentSearchTextField.getText(), searchPanel.getMapParameters());
                    searchThread.run();
                    return false;
                }
            } catch (Exception e) {
                logger.error("-->errore nel verify searchTextcontroller", e);
            }
            return true;
        }
    }

    private class ResetSearchPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            SearchTextController.this.clearData();
        }

    }

    private class SearchThread extends SwingWorker<Object, Void> {
        private final String propertyRender;
        private final String value;

        /**
         * Costruttore.
         *
         * @param propertyRender
         *            proprietà da renderizzare
         * @param value
         *            valore
         * @param parameter
         *            parametri
         */
        public SearchThread(final String propertyRender, final String value, final Map<String, Object> parameter) {
            this.propertyRender = propertyRender;
            this.value = value;
            searchPanel.getPopUp().setText(messageRicercaInCorso);
        }

        @Override
        protected Object doInBackground() {
            String valueSearch = value;
            if (value != null) {
                valueSearch = valueSearch.replace("*", "%");
                if (!valueSearch.endsWith("%")) {
                    valueSearch += "%";
                }
            }
            return searchPanel.getSearchObject().getData(propertyRender, valueSearch);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void done() {
            try {
                if (isCancelled()) {
                    return;
                }
                Collection<Object> collection = (Collection<Object>) get();
                if (collection == null) {
                    return;
                }
                if (collection != null && collection.size() == 1 && currentSearchTextField.isSelectAuto()) {
                    Object obj = collection.iterator().next();
                    hidePopup();
                    selectObject(obj);
                } else {
                    searchPanel.getTableSearchText().setRows(collection, true);
                    searchPanel.getPopUp().setText("");

                    if (currentSearchTextField.hasFocus()) {
                        // se uso la searchPanel in un editor di tabella il focus viene dato alla searchTextField...
                        // lo forzo sulla text.
                        currentSearchTextField.getTextField().requestFocusInWindow();
                    }
                    if (currentSearchTextField.getTextField().hasFocus() && !searchPanel.getPopUp().isPopupVisible()) {
                        showPopupMenu();
                    } else if (!currentSearchTextField.getTextField().hasFocus()) {
                        currentSearchTextField.revert();
                    }
                }
            } catch (InterruptedException e) {
                logger.info("Ricerca interrotta");
            } catch (ExecutionException e) {
                logger.error("-->errore ", e);
                searchPanel.getPopUp().setText("Errore nella ricerca");
                searchPanel.selectObject(null);
            }
        }
    }

    private static Logger logger = Logger.getLogger(SearchTextController.class);

    private final SearchPanel searchPanel;
    private SearchTextField currentSearchTextField;
    private String messageRicercaInCorso;
    private SearchThread searchThread;
    private boolean lastSearchForVerify = false;

    /**
     * Costruttore.
     *
     * @param searchPanel
     *            {@link SearchPanel}
     * @param parameters
     *            parametri
     */
    public SearchTextController(final SearchPanel searchPanel, final Map<String, String> parameters) {
        this.searchPanel = searchPanel;
        currentSearchTextField = searchPanel.getTextFields().values().iterator().next();
        searchPanel.getCaricaTuttoCommand().addCommandInterceptor(this);
        searchPanel.getClearCommand().addCommandInterceptor(this);
        searchPanel.setPropertyCommandExecutor(this);
        messageRicercaInCorso = RcpSupport.getMessage("searchText.ricercaincorso.label");

        InputVerifier inputVerifier = new InputVerifySearch();
        for (SearchTextField searchTextField : searchPanel.getTextFields().values()) {
            searchTextField.getTextField().addKeyListener(new WeakKeyListener(this, searchTextField));
            searchTextField.getTextField().setInputVerifier(inputVerifier);
            searchTextField.addPropertyChangeListener(QuickFilterField.PROPERTY_SEARCH_TEXT, this);
        }

        for (Entry<String, String> entry : parameters.entrySet()) {
            searchPanel.getFormModel().getValueModel(entry.getValue())
                    .addValueChangeListener(new ResetSearchPropertyChange());
        }
    }

    /**
     * Cancella i dati della tabella dei risultati e la stringa di ricerca.
     */
    private void clearData() {
        if (!searchPanel.isControlCreated()) {
            return;
        }
        searchPanel.getTableSearchText().clearData();
    }

    @Override
    public void execute() {
        selectTableObject();
    }

    /**
     * Chiude il popup aperto e restituisce il focus alla text field.
     */
    private void hidePopup() {
        if (searchPanel.getPopUp().isPopupVisible()) {
            searchPanel.getPopUp().hidePopupImmediately(true);
            // NPE mail: currentsearchtext null
            if (currentSearchTextField != null) {
                currentSearchTextField.getTextField().requestFocusInWindow();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (lastSearchForVerify) {
            lastSearchForVerify = false;
        }
        SearchTextField searchTextField = (SearchTextField) ((JComponent) e.getSource())
                .getClientProperty(SearchTextField.CLIENT_PROPERTY);
        if (searchTextField != currentSearchTextField) {
            if (logger.isDebugEnabled()) {
                logger.debug("-->rendo attiva una searchText diversa ");
            }
            setSearchTextFieldActive(searchTextField);
        }
        try {
            if (searchPanel.getFormModel().isReadOnly()) {
                return;
            }
            if (e.isControlDown() || e.isAltDown() || e.isShiftDown() || e.isAltGraphDown() || e.isMetaDown()) {
                // premuto tasto funzione, non eseguo nessuna operazione sulla
                // searchtext");
                return;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            if (searchPanel.isControlCreated() && searchPanel.getPopUp().isPopupVisible()) {
                // revert di un eventuale search
                if (currentSearchTextField != null) {
                    currentSearchTextField.revert();
                }
                hidePopup();
                e.consume();
            }
            break;
        case KeyEvent.VK_DOWN:
            if (searchPanel.isControlCreated() && searchPanel.getPopUp().isPopupVisible()) {
                searchPanel.getTableSearchText().getNavigationCommands()[JideTableWidget.NAVIGATE_NEXT].execute();
                e.consume();
            }
            break;
        case KeyEvent.VK_UP:
            if (searchPanel.isControlCreated() && searchPanel.getPopUp().isPopupVisible()) {
                searchPanel.getTableSearchText().getNavigationCommands()[JideTableWidget.NAVIGATE_PREVIOUS].execute();
                e.consume();
            }
            break;
        case KeyEvent.VK_ENTER:
            e.consume();
            selectTableObject();
            break;
        default:
            return;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void postExecution(ActionCommand command) {
        if (command.getId().equals(CaricaTuttoCommand.CARICATUTTOCOMMAND_ID)) {
            searchPanel.getTableSearchText().getOverlayTable().setMessage(null);
            showPopupMenu();
            // NPE da mail. Probabilmente viene lanciata una ricerca e chiuso l'editor o qualcosa di simile.
            if (currentSearchTextField != null) {
                searchThread = new SearchThread(currentSearchTextField.getPropertyRenderer(), null,
                        searchPanel.getMapParameters());
                searchThread.run();
            }
        }
    }

    @Override
    public boolean preExecution(ActionCommand command) {
        return true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (lastSearchForVerify) {
            lastSearchForVerify = false;
            return;
        }
        if (!searchPanel.getFormModel().isReadOnly()) {
            searchPanel.getTableSearchText().setRows(Collections.emptySet());
            if (evt.getNewValue().toString().length() == 0) {
                searchPanel.getTableSearchText().getOverlayTable()
                        .setMessage("INSERIRE ALMENO 1 CARATTERE (ESCLUSO *)\n F4 PER VISUALIZZARE TUTTI I RECORD");
                return;
            }

            if (evt.getNewValue().toString().equals("*")) {
                searchPanel.getTableSearchText().getOverlayTable()
                        .setMessage("INSERIRE ALMENO 1 CARATTERE (ESCLUSO *)\n F4 PER VISUALIZZARE TUTTI I RECORD");
                showPopupMenu();
            } else {
                searchPanel.getTableSearchText().getOverlayTable().setMessage(null);
                if (searchThread != null && searchThread.getState() != SwingWorker.StateValue.DONE) {
                    searchThread.cancel(true);
                }
                searchThread = new SearchThread(currentSearchTextField.getPropertyRenderer(),
                        evt.getNewValue().toString(), searchPanel.getMapParameters());
                searchThread.run();
            }
        }
    }

    /**
     *
     * @param objectToSelect
     *            objectToSelect
     */
    public void selectObject(Object objectToSelect) {
        lastSearchForVerify = false;
        if (objectToSelect != null) {
            if (searchPanel.isObjectChanged(objectToSelect)) {
                searchPanel.selectObject(objectToSelect);
            } else {
                for (SearchTextField searchTextField : searchPanel.getTextFields().values()) {
                    searchTextField.revert();
                }
            }
        } else {
            currentSearchTextField.revert();
        }
    }

    /**
     * Seleziona l'oggetto dalla tabella dei risultati.
     */
    public void selectTableObject() {
        if (currentSearchTextField == null) {
            return;
        }
        Object objectToSelect = null;
        int selectedRow = searchPanel.getTableSearchText().getSelectedRow();
        if (currentSearchTextField != null && selectedRow == -1
                && searchPanel.getTableSearchText().getRows().size() > 0) {
            selectedRow = 0;
        }
        selectedRow = TableModelWrapperUtils.getActualRowAt(searchPanel.getTableSearchText().getTableModel(),
                selectedRow);
        if (selectedRow >= 0 && searchPanel.getTableSearchText().getRows().size() > 0) {
            objectToSelect = searchPanel.getObjectAt(selectedRow);
        }
        hidePopup();
        selectObject(objectToSelect);
        searchPanel.getTableSearchText().setRows(Collections.emptyList());
    }

    /**
     * Setta il {@link SearchTextField} come attivo.
     *
     * @param searchTextField
     *            {@link SearchTextField}
     */
    private void setSearchTextFieldActive(SearchTextField searchTextField) {
        // sistemo il popup e la colonna da ordinare
        currentSearchTextField = searchTextField;
        if (searchTextField != null) {
            searchPanel.getPopUp().setOwner(searchTextField);
        }
    }

    /**
     * apre il popup e setta il tempo di ricerca a 100ms.
     */
    private void showPopupMenu() {
        searchPanel.getPopUp().setOwner(currentSearchTextField);
        searchPanel.getPopUp().showPopup();
    }
}
