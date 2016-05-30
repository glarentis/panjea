package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;

import foxtrot.Task;
import foxtrot.Worker;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile.EContoInsert;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaContabileRateiRisconti;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto.DocumentoPM;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto.RateiRiscontiTableWidget;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto.RigaRateoRiscontoModel;
import it.eurotn.panjea.contabilita.rich.search.SottoContoSearchObject;
import it.eurotn.panjea.contabilita.rich.search.SottoContoSearchTextField;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.converter.ColorConverter;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.control.table.JecHierarchicalTable;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.form.builder.support.JECOverlayHelper;

public class RigaContabileForm extends PanjeaAbstractForm {

    /**
     * Property change legato a conto per calcolare il saldo iniziale del conto alla data
     * areaContabile.dataRegistrazione.<br/>
     * NOTA: non uso il defaultController per questi listener per il fatto che devo accedere a componenti del form.
     *
     * @author Leonardo
     */
    private class ContoChangeListener implements PropertyChangeListener {

        private String propertyName = null;

        /**
         * Default constructor.
         *
         * @param propertyName
         *            imposta la proprieta' del form a cui e' collegato il property change
         */
        public ContoChangeListener(final String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            // il nuovo sottoconto scelto
            final SottoConto sottoConto = (SottoConto) evt.getNewValue();
            final RigaContabile rigaContabile = (RigaContabile) getFormObject();

            if (rigaContabile.getConto() != null) {
                updateTabbedPane();
            }

            if (getFormModel().isReadOnly()) {
                labelSaldo.setText("");
                return;
            }

            // per la rules RigheCentroCostoConstraint
            getFormModel().validate();

            // se il sottoconto esiste
            if (sottoConto != null) {
                final Date dataRegistrazioneCompresa = getDataSaldo();

                // l'importo della rigaContabile
                BigDecimal importo = rigaContabile.getImporto();

                // il saldo alla data di registrazione del conto scelto
                BigDecimal saldoConto = getSaldo(propertyName, rigaContabile, sottoConto, dataRegistrazioneCompresa,
                        importo, rigaContabile.isNew());

                // aggiorna le label per visualizzare il saldo
                updateSaldo(propertyName, saldoConto);
            } else {
                // se il sottoconto non esiste
                labelSaldo.setText("");
            }

            if (rigaContabile.getContoInsert() == EContoInsert.AVERE) {
                getValueModel("contoDare").setValueSilently(null, this);
            } else {
                getValueModel("contoAvere").setValueSilently(null, this);
            }
        }
    }

    /**
     * Nella modifica dell'importo, devo aggiornare il calcolo del saldo se ho selezionato un conto.
     *
     * @author leonardo
     */
    private class ImportoChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (getFormModel().isReadOnly()) {
                labelSaldo.setText("");
                return;
            }

            RigaContabile rigaContabile = (RigaContabile) getFormObject();

            // Se cambio i ratei cambio l'importo della riga ed in automatico le righe dei centro di costo. Le risetto
            // per notificare i
            // cambiamenti al valuemodel
            if (rigaContabile != null && rigaContabile.getConto() != null && rigaContabile.isRateiRiscontiAttivi()
                    && rigaContabile.getConto().isSoggettoCentroCosto()) {
                getValueModel("righeCentroCosto").setValue(rigaContabile.getRigheCentroCosto());
            }
            if (rigaContabile != null && !rigaContabile.isNew()) {
                BigDecimal importo = (BigDecimal) evt.getNewValue();
                if (importo != null) {
                    SottoConto contoPerSaldo = null;
                    String propertyNameConto = null;
                    if (rigaContabile.getContoDare() != null) {
                        contoPerSaldo = rigaContabile.getContoDare();
                        propertyNameConto = "contoDare";
                    }
                    if (rigaContabile.getContoAvere() != null) {
                        contoPerSaldo = rigaContabile.getContoAvere();
                        propertyNameConto = "contoAvere";
                    }
                    if (contoPerSaldo != null) {
                        // saldo alla data comprensiva del documento corrente se in stato confermato/verificato
                        BigDecimal saldo = getSaldo(propertyNameConto, rigaContabile, contoPerSaldo, getDataSaldo(),
                                importo, rigaContabile.isNew());

                        updateSaldo(propertyNameConto, saldo);
                    }
                } else {
                    labelSaldo.setText("");
                }
            }
        }
    }

    private class RateiRiscontiChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateTabbedPane();
            RigaContabile rc = (RigaContabile) getFormObject();
            // la prima volta che attivo i ratei la prima riga della tabella ratei è gia presente senza la riga
            // contabile. La setto
            RigaRateoRiscontoModel rigaRateoRiscontoModel = (RigaRateoRiscontoModel) TableModelWrapperUtils
                    .getActualTableModel(rateiRiscontiTableWidget.getTable().getModel(), RigaRateoRiscontoModel.class);
            rigaRateoRiscontoModel.setRigaContabile(rc);
            rigaRateoRiscontoModel.getElementAt(0).setRigaContabile(rc);
        }

    };

    public class ReadOnlyPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RigaContabile rc = (RigaContabile) getFormObject();
            getFormModel().getFieldMetadata("importo")
                    .setReadOnly(getFormModel().isReadOnly() && rc.isRateiRiscontiAttivi());
        }

    }

    public class RigheRateoRisChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RigaContabile rigaContabile = (RigaContabile) getFormObject();
            if (rigaContabile.getRigheRateoRisconto().size() == 0) {
                lblTitleRateoRisconto.setText("");
            } else if (rigaContabile.getRigheRateoRisconto().get(0).getRateiRiscontiAnno().size() > 0) {
                lblTitleRateoRisconto.setText(
                        rigaContabile.getRigheRateoRisconto().get(0).getRateiRiscontiAnno().get(0).getDescrizione());
            }
            if (!getFormModel().isReadOnly() && rigaContabile.isRateiRiscontiAttivi()) {
                SottoConto conto = rigaContabile.getConto();
                if (rigaContabile.getContoAvere() != null) {
                    // getValueModel("importo").setValue(rigaContabile.getImportoCostoRateoRiscontoAnnoDocumento());
                    getValueModel("contoAvere").setValue(conto);
                } else {
                    // getValueModel("importo").setValue(rigaContabile.getImportoCostoRateoRiscontoAnnoDocumento());
                    getValueModel("contoDare").setValue(conto);
                }
                // se ho assegnato un centro di costo cambio direttamente l'importo
                if (rigaContabile.getRigheCentroCosto().size() == 1) {
                    rigaContabile.getRigheCentroCosto().iterator().next()
                            .setImporto((BigDecimal) getValueModel("importo").getValue());
                    getFormModel().validate();
                }
            }
        }

    }

    private static final String TAB_TITLE_CENTRI_DI_COSTO = "Centri di costo";
    private static final int INDEX_CENTRI_DI_COSTO = 1;

    private static final String TAB_TITLE_RATEI_RISCONTI = "Ratei/Risconti";
    private static final int INDEX_RATEI_RISCONTI = 0;

    private static final String TAB_TITLE_DOCUMENTI_RISCONTI_COLLEGATI = "Documenti risconti collegati";
    private static final int INDEX_DOCUMENTI_RISCONTI_COLLEGATI = 2;

    private static Logger logger = Logger.getLogger(RigaContabileForm.class);

    private static final String FORM_ID = "rigaContabileForm";

    private SottoContoSearchTextField contoDareSearch = null;
    private SottoContoSearchTextField contoAvereSearch = null;
    private RigheContabiliTableModel righeContabiliTableModel;
    private AreaContabile areaContabile = null;
    private JLabel labelSaldo;

    private AziendaCorrente aziendaCorrente = null;
    private IContabilitaBD contabilitaBD = null;
    private final ColorConverter colorConverter = new ColorConverter();

    private CentroCostoInserimentoForm centroCostoInserimentoForm;

    private JideTableWidget<RigaRateoRisconto> rateiRiscontiTableWidget;

    private JTabbedPane tabPlugin;

    private JideTableWidget<DocumentoPM> listaDocumenti;

    private JPanel[] pannelliPlugin = new JPanel[3];
    private JLabel lblTitleRateoRisconto;

    /**
     * Default constructor.
     */
    public RigaContabileForm() {
        super(PanjeaFormModelHelper.createFormModel(
                RigaContabile.creaRigaContabile(null, null, true, BigDecimal.ZERO, null, false), false, FORM_ID),
                FORM_ID);

        // Aggiungo il value model che mi servirà solamente nella search text delle entità per cercare solo le entità
        // abilitate
        ValueModel sottocontoAbilitatoInRicercaValueModel = new ValueHolder(Boolean.TRUE);
        DefaultFieldMetadata sottocontoAbilitatoMetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(sottocontoAbilitatoInRicercaValueModel), Boolean.class, true, null);
        getFormModel().add("sottocontoAbilitatoInRicerca", sottocontoAbilitatoInRicercaValueModel,
                sottocontoAbilitatoMetaData);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:default,10dlu,right:pref,4dlu,fill:default,10dlu,right:pref,4dlu,fill:pref:grow",
                "2dlu,default,default,2dlu,default,2dlu,default,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanelNumbered());

        builder.setLabelAttributes("r, t");
        builder.setComponentAttributes("r,t");

        builder.setRow(2);
        JTextField importoTextField = (JTextField) builder.addPropertyAndLabel("importo")[1];
        importoTextField.setColumns(10);
        builder.setComponentAttributes("l,t");

        builder.addLabel("contoDare", 5);
        Binding contoDareBinding = bf.createBoundSearchText("contoDare", new String[] { "descrizione" },
                new String[] { "sottocontoAbilitatoInRicerca" },
                new String[] { SottoContoSearchObject.FILTRO_SOTTOCONTO_ABILITATO },
                SottoContoSearchTextField.class.getName());
        contoDareSearch = ((SottoContoSearchTextField) (builder.addBinding(contoDareBinding, 7)).getComponent(0));
        contoDareSearch.setColumns(14);

        builder.addLabel("contoAvere", 9);
        Binding contoAvereBinding = bf.createBoundSearchText("contoAvere", new String[] { "descrizione" },
                new String[] { "sottocontoAbilitatoInRicerca" },
                new String[] { SottoContoSearchObject.FILTRO_SOTTOCONTO_ABILITATO },
                SottoContoSearchTextField.class.getName());
        contoAvereSearch = ((SottoContoSearchTextField) (builder.addBinding(contoAvereBinding, 11)).getComponent(0));
        contoAvereSearch.setColumns(14);

        builder.nextRow();
        builder.setComponentAttributes("f,c");
        builder.addComponent(contoDareSearch.getLabelSottoConto(), 7, 3);
        builder.addComponent(contoAvereSearch.getLabelSottoConto(), 11, 3);

        builder.nextRow();
        // riga vuota x mostrare le informazioni sul conto
        builder.nextRow();

        builder.setLabelAttributes("r, c");
        builder.addPropertyAndLabel("note", 1, 5, 9, 1);
        builder.nextRow();

        PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
        if (pluginManager.isPresente(PluginManager.PLUGIN_RATEI_RISCONTI)) {
            if (tabPlugin == null) {
                tabPlugin = getComponentFactory().createTabbedPane();
            }
            addFormValueChangeListener("righeRateoRisconto", new RigheRateoRisChangeListener());
            getValueModel("rateiRiscontiAttivi").addValueChangeListener(new RateiRiscontiChangeListener());

            JPanel panelRatei = new JPanel(new VerticalLayout());
            pannelliPlugin[INDEX_RATEI_RISCONTI] = panelRatei;

            builder.nextRow();

            JPanel panelDocumentiRiscontiCollegati = new JPanel(new BorderLayout());
            listaDocumenti = new JideTableWidget<DocumentoPM>("documentiCollegati", new String[] { "documento" },
                    DocumentoPM.class);
            panelDocumentiRiscontiCollegati.add(listaDocumenti.getComponent(), BorderLayout.CENTER);
            pannelliPlugin[INDEX_DOCUMENTI_RISCONTI_COLLEGATI] = panelDocumentiRiscontiCollegati;

            RigaRateoRiscontoModel tableModel = new RigaRateoRiscontoModel();
            rateiRiscontiTableWidget = new RateiRiscontiTableWidget("rigaRateoRiscontoModel", tableModel);

            JTextField componentImportoRateo = (JTextField) getBindingFactory().createBinding("importoRateoRisconto")
                    .getControl();

            JLabel labelImportoRateo = getComponentFactory().createLabel("");
            getFormModel().getFieldFace("importoRateoRisconto").configure(labelImportoRateo);
            labelImportoRateo.setLabelFor(componentImportoRateo);

            JPanel panel = new JPanel(new HorizontalLayout(5));
            panel.add(labelImportoRateo);
            componentImportoRateo.setColumns(15);
            panel.add(componentImportoRateo);
            lblTitleRateoRisconto = new JLabel();
            panel.add(lblTitleRateoRisconto);

            panelRatei.add(panel);

            TableEditableBinding<RigaRateoRisconto> tableBinding = new TableEditableBinding<>(getFormModel(),
                    "righeRateoRisconto", Set.class, tableModel, rateiRiscontiTableWidget);

            panelRatei.add(tableBinding.getControl());
            rateiRiscontiTableWidget.getTable().getTableHeader().setReorderingAllowed(false);

            JecHierarchicalTable<RigaRateoRisconto> table = (JecHierarchicalTable<RigaRateoRisconto>) rateiRiscontiTableWidget
                    .getTable();
            rateiRiscontiTableWidget.setNumberRowVisible(false);
            table.setSingleExpansion(false);
            table.expandAllRows();

            SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils
                    .getActualTableModel(rateiRiscontiTableWidget.getTable().getModel(), SortableTableModel.class);
            sortableTableModel.sortColumn(2, true, false);
        }

        if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
            if (tabPlugin == null) {
                tabPlugin = getComponentFactory().createTabbedPane();
            }

            JPanel panelCentriDicosto = new JPanel(new VerticalLayout());
            centroCostoInserimentoForm = new CentroCostoInserimentoForm(getValueModel("righeCentroCosto"),
                    getValueModel("importo"));
            Binding centriCostoTableBinding = bf.createTableBinding("righeCentroCosto", 300,
                    new RigheCentroCostoTableModel(), centroCostoInserimentoForm);
            panelCentriDicosto.add(centriCostoTableBinding.getControl());
            pannelliPlugin[INDEX_CENTRI_DI_COSTO] = panelCentriDicosto;
        }

        if (tabPlugin != null) {
            builder.addComponent(tabPlugin, 1, 11, 11, 1);
        }
        addFormValueChangeListener("contoDare", new ContoChangeListener("contoDare"));
        addFormValueChangeListener("contoAvere", new ContoChangeListener("contoAvere"));
        addFormValueChangeListener("importo", new ImportoChangeListener());
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new ReadOnlyPropertyChangeListener());

        labelSaldo = getComponentFactory().createLabel("");
        JECOverlayHelper.attachOverlay(labelSaldo, importoTextField, 0, 0);
        builder.nextRow();
        return builder.getPanel();
    }

    /**
     * @return Returns the aziendaCorrente.
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     * @return Returns the contabilitaBD.
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    /**
     * @return data per il calcolo del saldo.
     */
    private Date getDataSaldo() {
        // data di registrazione del documento per trovare il saldo alla data del conto scelto
        Date dataRegistrazione = areaContabile.getDataRegistrazione();

        // il calcolo saldo da data viene fatto alla data di registrazione esclusa, incremento quindi di un
        // giorno la data per considerare anche il movimento corrente se in stato confermato/verificato
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataRegistrazione);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * Recupera il saldo del conto e aggiunge l'importo della riga in modifica/inserimento.
     *
     * @param propertyName
     *            se conto dare o avere
     * @param rigaContabile
     *            la riga in modifica/inserimento
     * @param sottoConto
     *            il conto in modifica/inserimento
     * @param dataRegistrazioneCompresa
     *            la data a cui calcolare il saldo
     * @param importo
     *            l'importo da aggiungere al saldo
     * @param isNuovaRiga
     *            se sto modificando o inserendo una riga
     * @return il saldo corrente (dei documenti in stato verificato o confermato) sommato all'importo corrente della
     *         riga contabile in modifica/inserimento
     */
    private BigDecimal getSaldo(String propertyName, RigaContabile rigaContabile, final SottoConto sottoConto,
            final Date dataRegistrazioneCompresa, BigDecimal importo, boolean isNuovaRiga) {
        BigDecimal saldoConto = null;
        try {
            saldoConto = (BigDecimal) Worker.post(new Task() {
                @Override
                public Object run() throws Exception {
                    return contabilitaBD.calcoloSaldo(sottoConto, dataRegistrazioneCompresa,
                            aziendaCorrente.getAnnoContabile());
                }
            });
        } catch (RuntimeException e) {
            if (TipoDocumentoBaseException.class.equals(e.getCause().getClass())) {
                logger.warn("Tipi documento base non presenti per il tipo conto");
            } else {
                logger.error("--> Errore nel calcolo del saldoDaData", e);
                throw e;
            }
        } catch (Exception e) {
            logger.error("--> Errore nel calcolo del saldoDaData", e);
            throw new RuntimeException(e);
        }

        if (saldoConto != null) {
            StatoAreaContabile statoAreaContabile = areaContabile.getStatoAreaContabile();
            // se lo stato del documento e' provvisorio non viene considerato nel calcolo del saldo, devo quindi sommare
            // le
            // righe per quel conto al saldo
            if (statoAreaContabile.compareTo(StatoAreaContabile.PROVVISORIO) == 0) {
                saldoConto = saldoConto.add(righeContabiliTableModel.getSaldoRighe(sottoConto));
            }

            // se ho un importo impostato per la riga contabile aggiungo il valore al saldo
            if (importo != null) {
                if (propertyName.equals("contoDare")) {
                    saldoConto = saldoConto.add(importo);
                }
                if (propertyName.equals("contoAvere")) {
                    saldoConto = saldoConto.add(importo.negate());
                }
            }

            // 1.se sono in moifica di una riga e lo stato e' confermato o verificato (rientra quindi nel calcolo del
            // saldo
            // eseguito prima) devo sottrarre l'importo della riga come e' priva di modifiche (la ricarico per evitare
            // possibili modifiche sospese)

            // 2.se lo stato e' provvisorio devo cmq rimuovere il valore della riga perche' al passo successivo calcolo
            // il
            // saldo delle righe recuperandolo dal model (vengono sommate tutte le righe di quel conto del documento)
            if (!isNuovaRiga) {
                RigaContabile rigaSalvata = contabilitaBD.caricaRigaContabileLazy(rigaContabile.getId());

                // per sottrarre sommo il valore dell'importo negato in caso di conto dare invece che di conto avere
                if (rigaSalvata.getContoDare() != null) {
                    saldoConto = saldoConto.add(rigaSalvata.getImporto().negate());
                }
                if (rigaSalvata.getContoAvere() != null) {
                    saldoConto = saldoConto.add(rigaSalvata.getImporto());
                }
            }
        }
        return saldoConto;
    }

    /**
     * @param areaContabile
     *            The areaContabile to set.
     */
    public void setAreaContabile(AreaContabile areaContabile) {
        this.areaContabile = areaContabile;
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaBD
     *            The contabilitaBD to set.
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
        updateTabbedPane();
    }

    /**
     * @param righeContabiliTableModel
     *            The righeContabiliTableModel to set.
     */
    public void setRigheContabiliTableModel(RigheContabiliTableModel righeContabiliTableModel) {
        this.righeContabiliTableModel = righeContabiliTableModel;
    }

    /**
     * Aggiorna la label del saldo.
     *
     * @param propertyName
     *            .
     * @param saldoConto
     *            .
     */
    private void updateSaldo(String propertyName, BigDecimal saldoConto) {
        // aggiorno le labels di saldo dare e avere formattando il bigdecimal
        Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
        String saldoLabel = getMessage("saldo");
        String eurSymbol = getMessage(Importo.KEY_MSG_EUR_SYMBOL);
        RigaContabile rigaContabile = (RigaContabile) getFormObject();
        if (rigaContabile.getConto().isStileSaldoEnabled(saldoConto)) {
            labelSaldo.setForeground((Color) colorConverter
                    .fromString(rigaContabile.getConto().getStileSaldo().getBackGroundColor(), null));
        } else {
            labelSaldo.setForeground((ColorUIResource) UIManager.get("Label.foreground"));
        }

        if (!propertyName.isEmpty() && saldoConto != null) {
            labelSaldo.setText(saldoLabel + ": " + eurSymbol + " " + format.format(saldoConto));
        } else {
            labelSaldo.setText("");
        }
    }

    private void updateTabbedPane() {
        RigaContabile rc = (RigaContabile) getFormObject();
        if (tabPlugin == null) {
            return;
        }
        tabPlugin.removeAll();
        if (rc == null) {
            return;
        }
        if (rc.isRateiRiscontiAttivi()) {
            tabPlugin.addTab(TAB_TITLE_RATEI_RISCONTI, pannelliPlugin[INDEX_RATEI_RISCONTI]);
            RigaRateoRiscontoModel rigaRateoRiscontoModel = (RigaRateoRiscontoModel) TableModelWrapperUtils
                    .getActualTableModel(rateiRiscontiTableWidget.getTable().getModel(), RigaRateoRiscontoModel.class);
            rigaRateoRiscontoModel.setRigaContabile(rc);
        }
        if (rc.getConto() != null && rc.getConto().isSoggettoCentroCosto()) {
            tabPlugin.addTab(TAB_TITLE_CENTRI_DI_COSTO, pannelliPlugin[INDEX_CENTRI_DI_COSTO]);
        }
        if (rc instanceof RigaContabileRateiRisconti) {
            listaDocumenti.setRows(new ArrayList<DocumentoPM>());
            List<Documento> documenti = ((RigaContabileRateiRisconti) rc).getDocumentiRiscontiCollegati();
            if (documenti != null) {
                for (Documento documento : documenti) {
                    listaDocumenti.addRowObject(new DocumentoPM(documento), null);
                }
            }
            tabPlugin.addTab(TAB_TITLE_DOCUMENTI_RISCONTI_COLLEGATI,
                    pannelliPlugin[INDEX_DOCUMENTI_RISCONTI_COLLEGATI]);
        }
    }
}
