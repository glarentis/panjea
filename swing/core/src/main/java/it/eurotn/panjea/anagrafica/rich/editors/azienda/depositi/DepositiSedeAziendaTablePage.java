/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda.depositi;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author Leonardo
 *
 */
public class DepositiSedeAziendaTablePage extends AbstractTablePageEditor<Deposito>
        implements Observer, InitializingBean {

    protected static final String PAGE_ID = "depositiSedeAziendaTablePage";

    private static final String ACTION_COMMAND_REFRESH_COMBO_SEDI_ID = ".actionCommandRefreshComboSediCommand";
    private IAnagraficaBD anagraficaBD;
    private IAnagraficaTabelleBD anagraficaTabelleBD;
    private JecCompositeDialogPage depositoCompositePage;
    private Azienda azienda;
    private SedeAzienda sedeAzienda;

    private EntitaLite entita;

    private SediAziendaComboBox comboBoxSedi = null;

    private boolean caricaDepositiInstallazione = false;

    private final ActionCommandRefreshComboSedi actionCommandRefreshComboSedi = new ActionCommandRefreshComboSedi(
            PAGE_ID + ACTION_COMMAND_REFRESH_COMBO_SEDI_ID, this);

    private JPanel sediPanel;

    /**
     * construttore.
     */
    protected DepositiSedeAziendaTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "datiGeografici.localita.descrizione", "indirizzo",
                "attivo", "sedeDeposito.sede.descrizione", "tipoDeposito.codice" }, Deposito.class);
        getTable().setAggregatedColumns(new String[] { "tipoDeposito.codice" });
    }

    /**
     * Construttore.
     *
     * @param columns
     *            colonne da visualizzare
     */
    protected DepositiSedeAziendaTablePage(final String[] columns) {
        super(PAGE_ID, columns, Deposito.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        depositoCompositePage.addPropertyChangeListener(this);
    }

    @Override
    protected JComponent createControl() {
        JComponent component = super.createControl();
        sediPanel = getComponentFactory().createPanel(new BorderLayout());

        // label per la selezione della sede caricando il testo dal file di properties
        sediPanel.add(getComponentFactory().createLabel("depositiAzienda.label.selectSede"), BorderLayout.WEST);

        comboBoxSedi = new SediAziendaComboBox(this);
        // combobox per la selezione della sede
        sediPanel.add(comboBoxSedi, BorderLayout.CENTER);

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        JPanel topPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(sediPanel);

        PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            final JCheckBox checkBoxDepInstallazione = getComponentFactory()
                    .createCheckBox("Visualizza depositi installazione");
            checkBoxDepInstallazione.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    caricaDepositiInstallazione = checkBoxDepInstallazione.isSelected();
                }
            });
            checkBoxDepInstallazione.setText("Visualizza depositi installazione");
            topPanel.add(checkBoxDepInstallazione);
        }
        topPanel.add(actionCommandRefreshComboSedi.createButton());

        rootPanel.add(GuiStandardUtils.attachBorder(topPanel), BorderLayout.NORTH);
        // aggiungo la tabella dei depositi
        rootPanel.add(component, BorderLayout.CENTER);
        getTable().addSelectionObserver(this);
        rootPanel.add(getComponentFactory().createScrollPane(depositoCompositePage.getControl()), BorderLayout.SOUTH);

        return rootPanel;
    }

    /**
     * @return the anagraficaBD
     */
    public IAnagraficaBD getAnagraficaBD() {
        return anagraficaBD;
    }

    /**
     * @return the anagraficaTabelleBD
     */
    public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
        return anagraficaTabelleBD;
    }

    /**
     * @return the azienda
     */
    public Azienda getAzienda() {
        return azienda;
    }

    public SediAziendaComboBox getComboBoxSedi() {
        return comboBoxSedi;
    }

    public JecCompositeDialogPage getDepositoCompositePage() {
        return depositoCompositePage;
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return ((DepositoSedeAziendaPage) depositoCompositePage.getDialogPages().get(0)).getEditorLockCommand();
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return ((DepositoSedeAziendaPage) depositoCompositePage.getDialogPages().get(0)).getNewCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return ((DepositoSedeAziendaPage) depositoCompositePage.getDialogPages().get(0)).getEditorSaveCommand();
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        return ((DepositoSedeAziendaPage) depositoCompositePage.getDialogPages().get(0)).getUndoCommand();
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the sedeAzienda
     */
    public SedeAzienda getSedeAzienda() {
        return sedeAzienda;
    }

    public boolean isCaricaDepositiInstallazione() {
        return caricaDepositiInstallazione;
    }

    @Override
    public List<Deposito> loadTableData() {
        return new ArrayList<>();
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (azienda.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("azienda.null.depositiAzienda.messageDialog.title",
                    new Object[] {}, Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "azienda.null.depositiAzienda.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(azienda.getDenominazione(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

    @Override
    public void processTableData(Collection<Deposito> results) {
        super.processTableData(results);
        actionCommandRefreshComboSedi.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        if (JecCompositeDialogPage.CURRENT_OBJECT_CHANGED_PROPERTY.equals(event.getPropertyName())
                && event.getNewValue() instanceof Deposito && !((Deposito) event.getNewValue()).isNew()) {
            getTable().replaceOrAddRowObject((Deposito) event.getNewValue(), (Deposito) event.getNewValue(), this);
        }
    }

    @Override
    public List<Deposito> refreshTableData() {
        return new ArrayList<>();
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param anagraficaTabelleBD
     *            the anagraficaTabelleBD to set
     */
    public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
        this.anagraficaTabelleBD = anagraficaTabelleBD;
    }

    /**
     * @param azienda
     *            the azienda to set
     */
    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    /**
     *
     * @param depositoCompositePage
     *            coomposite per le prop del deposito
     */
    public void setDepositoCompositePage(JecCompositeDialogPage depositoCompositePage) {
        this.depositoCompositePage = depositoCompositePage;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;

        // se l'entità è presente nascondo la combo per la selezione delle sedi entità
        sediPanel.setVisible(false);
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof Azienda) {
            this.azienda = (Azienda) object;
        } else {
            this.azienda = ((AziendaAnagraficaDTO) object).getAzienda();
        }

        ((DepositoSedeAziendaPage) depositoCompositePage.getDialogPages().get(0))
                .setSediAzienda(anagraficaBD.caricaSediAzienda(azienda));
    }

    /**
     * @param sedeAzienda
     *            the sedeAzienda to set
     */
    public void setSedeAzienda(SedeAzienda sedeAzienda) {
        this.sedeAzienda = sedeAzienda;
        ((DepositoSedeAziendaPage) depositoCompositePage.getDialogPages().get(0)).setSedeAzienda(sedeAzienda);
    }

    @Override
    public void update(Observable observable, Object obj) {
        depositoCompositePage.setCurrentObject(obj);
    }

}
