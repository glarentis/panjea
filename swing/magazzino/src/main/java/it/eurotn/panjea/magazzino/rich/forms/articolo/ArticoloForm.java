package it.eurotn.panjea.magazzino.rich.forms.articolo;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultFormatterFactory;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.DescrizioniEntityPanel;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form di Articolo.
 *
 * @author adriano
 * @version 1.0, 02/mag/08
 */
public class ArticoloForm extends PanjeaAbstractForm implements Focussable {

    private class ArticoloChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            String codiceCategoria = (String) getValue("categoria.codice");
            String descrizioneCategoria = (String) getValue("categoria.descrizione");

            if (articoloSeparator != null) {
                JLabel label = (JLabel) articoloSeparator.getComponent(0);
                label.setText(codiceCategoria + " - " + descrizioneCategoria);
            }
        }
    }

    private class CalcolaEanActionCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand command) {
            String ean = ((CalcolaEanCommand) command).getCodice();
            getValueModel("barCode").setValue(ean);
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            return true;
        }
    }

    public class NumeroDecimaliPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue().equals(evt.getOldValue())) {
                return;
            }
            Integer numeroDecimali = (Integer) evt.getNewValue();
            DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
                    BigDecimal.class);
            costoStandardControl.setFormatterFactory(factory);
            BigDecimal prezzo = (BigDecimal) getValueModel("costoStandard").getValue();
            if (prezzo != null) {
                prezzo = prezzo.setScale(numeroDecimali, RoundingMode.HALF_UP);
            }
            getValueModel("costoStandard").setValue(prezzo);
        }
    }

    /**
     * PropertyChange che si aspetta come nuovo valore dell' event una
     * {@link ProvenienzaPrezzoArticolo} e usa tale valore per nascondere o visualizzare il
     * componente dei tipi mezzo trasporto.
     *
     * @author Leonardo
     */
    private class ProvenienzaPrezzoPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            showListTipiMezzo(ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA.equals(evt.getNewValue()));
        }

    }

    private class ReadOnlyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            calcolaEanCommand.setEnabled(!getFormModel().isReadOnly());
        }

    }

    private class ServizioChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Servizio servizio = (Servizio) evt.getNewValue();
            showValoriArticoloIntraComponents(servizio);

            if (!getFormModel().isReadOnly()) {
                getValueModel("datiIntra.massaNetta").setValue(null);
                getValueModel("datiIntra.valoreUnitaMisuraSupplementare").setValue(null);
            }
        }

    }

    private class TipoArticoloChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ETipoArticolo nuovoTipoArticolo = (ETipoArticolo) evt.getNewValue();
            if (nuovoTipoArticolo != null && massaNettaIntraField != null) {
                showTipoArticoloIntraComponents(nuovoTipoArticolo);
            }
            if (!getFormModel().isReadOnly()) {
                getValueModel("datiIntra.servizio").setValue(null);
                getValueModel("datiIntra.nazione").setValue(null);
                getValueModel("datiIntra.massaNetta").setValue(null);
                getValueModel("datiIntra.valoreUnitaMisuraSupplementare").setValue(null);
                getValueModel("datiIntra.modalitaErogazione").setValue(null);
            }
        }

    }

    private class TipoMezzoListCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -7548610030027532210L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setText(((TipoMezzoTrasporto) value).getCodice() + " - " + ((TipoMezzoTrasporto) value).getDescrizione());
            component.setForeground(Color.BLACK);
            return component;
        }

    }

    private static final String FORM_ID = "articoloForm";

    private CalcolaEanCommand calcolaEanCommand = null;
    private AziendaCorrente aziendaCorrente = null;

    @SuppressWarnings("unused")
    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
    private JComponent tipiMezzoComponent = null;
    private JTextField massaNettaIntraField = null;
    private JTextField valoreUnitaMisuraSupplementareIntraField = null;
    private JComponent[] modalitaErogazioneIntraComponents = null;
    private JFormattedTextField costoStandardControl = null;
    private JLabel labelMassaNetta = null;

    private JLabel labelValoreUMS = null;
    private TipoArticoloChangeListener tipoArticoloChangeListener = null;
    private ProvenienzaPrezzoPropertyChangeListener provenienzaPrezzoPropertyChangeListener = null;
    private NumeroDecimaliPropertyChange numeroDecimaliPropertyChange = null;
    private ArticoloChangeListener articoloChangeListener = null;
    private ReadOnlyListener readOnlyListener;
    private CalcolaEanActionCommandInterceptor calcolaEanActionCommandInterceptor = null;

    private ServizioChangeListener servizioChangeListener = null;
    private JComponent articoloSeparator = null;

    private IAnagraficaTabelleBD anagraficaTabelleBD;

    /**
     * Costruttore di default.
     */
    public ArticoloForm() {
        super(PanjeaFormModelHelper.createFormModel(new Articolo(), false, FORM_ID), FORM_ID);
        calcolaEanCommand = new CalcolaEanCommand();
        calcolaEanCommand.addCommandInterceptor(getCalcolaEanActionCommandInterceptor());
        this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        this.anagraficaTabelleBD = RcpSupport.getBean("anagraficaTabelleBD");

        // Aggiungo il value model che mi servirà solamente nella search text delle entità
        // per cercare solo le entità abilitate
        ValueModel escludiFigliValueModel = new ValueHolder(Boolean.TRUE);
        DefaultFieldMetadata escludiFigliMetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(escludiFigliValueModel), Boolean.class, true, null);
        getFormModel().add("escludiFigli", escludiFigliValueModel, escludiFigliMetaData);
    }

    /**
     * Attiva i property change listeners di questo form.
     */
    public void activatePropertyChangeListeners() {
        addFormValueChangeListener("provenienzaPrezzoArticolo", getProvenienzaPrezzoPropertyChangeListener());
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, getReadOnlyListener());
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,f:90dlu,2dlu,12dlu,1dlu,90dlu,4dlu,90dlu,right:pref:grow",
                "default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        articoloSeparator = getComponentFactory().createLabeledSeparator("N.D.");
        builder.addComponent(articoloSeparator, 1, 10, 1);

        builder.setRow(3);
        builder.addPropertyAndLabel("codice", 1);
        JLabel descArtLabel = (JLabel) builder.addPropertyAndLabel("descrizione", 7, 2, 1)[0];
        descArtLabel.setIcon(RcpSupport.getIcon(aziendaCorrente.getLingua()));
        descArtLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        builder.nextRow();
        builder.addComponent(new DescrizioniEntityPanel(getFormModel(), "descrizioniLingua",
                "descrizioneLinguaAziendale", anagraficaTabelleBD, aziendaCorrente), 7, 4, 12);

        builder.addPropertyAndLabel("barCode", 1);
        builder.addComponent(calcolaEanCommand.createButton(), 5);
        builder.nextRow();

        builder.addPropertyAndLabel("codiceInterno", 1, 7);
        builder.nextRow();
        builder.nextRow();

        builder.addPropertyAndLabel("tipoArticolo", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("abilitato", 1);
        builder.nextRow();

        builder.addLabel("codiceIva", 1);
        builder.addBinding(bf.createBoundSearchText("codiceIva", new String[] { "codice" }), 3);
        builder.addPropertyAndLabel("ivaAlternativa", 7);
        builder.nextRow();

        builder.addLabel("categoriaContabileArticolo", 1);
        builder.addBinding(bf.createBoundSearchText("categoriaContabileArticolo", new String[] { "codice" }), 3);
        builder.addPropertyAndLabel("articoloLibero", 7);
        builder.nextRow();

        builder.addLabel("categoriaCommercialeArticolo", 1);
        builder.addBinding(bf.createBoundSearchText("categoriaCommercialeArticolo", new String[] { "codice" }), 3);
        builder.addLabel("categoriaCommercialeArticolo2", 7);
        builder.addBinding(bf.createBoundSearchText("categoriaCommercialeArticolo2", new String[] { "codice" }), 9);
        builder.nextRow();

        builder.addLabel("unitaMisura", 1);
        builder.addBinding(bf.createBoundSearchText("unitaMisura", new String[] { "codice" }), 3);
        builder.addLabel("unitaMisuraQtaMagazzino", 7);
        builder.addBinding(bf.createBoundSearchText("unitaMisuraQtaMagazzino", new String[] { "codice" }), 9);
        builder.nextRow();

        builder.addLabel("formulaTrasformazioneQta", 1);
        builder.addBinding(bf.createBoundSearchText("formulaTrasformazioneQta", new String[] { "codice" }), 3);
        builder.addLabel("formulaTrasformazioneQtaMagazzino", 7);
        builder.addBinding(bf.createBoundSearchText("formulaTrasformazioneQtaMagazzino", new String[] { "codice" }), 9);
        builder.nextRow();

        builder.addLabel("numeroDecimaliQta", 1);
        DefaultNumberFormatterFactory formatter = new DefaultNumberFormatterFactory("#,###", null, Integer.class);
        JFormattedTextField qtaDecTextField = (JFormattedTextField) builder
                .addBinding(bf.createBoundFormattedTextField("numeroDecimaliQta", formatter), 3);
        qtaDecTextField.setHorizontalAlignment(SwingConstants.RIGHT);

        builder.addLabel("numeroDecimaliPrezzo", 7);
        JFormattedTextField prezzoDecTextField = (JFormattedTextField) builder
                .addBinding(bf.createBoundFormattedTextField("numeroDecimaliPrezzo", formatter), 9);
        prezzoDecTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        builder.nextRow();

        builder.addPropertyAndLabel("gestioneSchedaArticolo", 1);

        builder.addLabel("meseAnnoSchedaArticolo", 7);
        JPanel meseAnnoPanel = getComponentFactory().createPanel(new HorizontalLayout(4));
        JTextField meseComponent = (JTextField) bf.createBinding("gestioneSchedaArticoloMese").getControl();
        meseComponent.setHorizontalAlignment(JTextField.RIGHT);
        meseComponent.setColumns(5);
        meseAnnoPanel.add(meseComponent);
        meseAnnoPanel.add(new JLabel("/"));
        JTextField annoComponent = (JTextField) bf.createBinding("gestioneSchedaArticoloAnno").getControl();
        annoComponent.setHorizontalAlignment(JTextField.RIGHT);
        annoComponent.setColumns(5);
        meseAnnoPanel.add(annoComponent);
        builder.addComponent(meseAnnoPanel, 9);
        builder.nextRow();

        builder.addPropertyAndLabel("produzione", 1);

        PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
        if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
            builder.addPropertyAndLabel("stampaLotti", 7);
            builder.nextRow();

            builder.addPropertyAndLabel("tipoLotto", 1);
            builder.addPropertyAndLabel("lottoFacoltativo", 7);
            builder.nextRow();
        } else {
            builder.nextRow();
        }

        builder.addPropertyAndLabel("provenienzaPrezzoArticolo", 1);
        tipiMezzoComponent = createTipiMezzoTrasportoBinding(bf);
        builder.addComponent(tipiMezzoComponent, 7, 3, 5);
        builder.nextRow();

        builder.addPropertyAndLabel("campoLibero", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("posizione", 1);
        builder.nextRow();

        builder.addLabel("costoStandard", 1);
        Binding boundFormattedTextFieldBinding = bf.createBoundFormattedTextField("costoStandard", getFactory(3));
        costoStandardControl = (JFormattedTextField) boundFormattedTextFieldBinding.getControl();
        builder.addBinding(boundFormattedTextFieldBinding, 3);
        costoStandardControl.setColumns(10);
        costoStandardControl.setHorizontalAlignment(SwingConstants.RIGHT);
        builder.addPropertyAndLabel("gestioneQuantitaZero", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("distinta", 1);
        builder.nextRow();

        if (pluginManager.isPresente("panjeaMrp")) {
            builder.addHorizontalSeparator("Dati Mrp", 1, 10);
            builder.nextRow();
            builder.addPropertyAndLabel("mrp");
            builder.nextRow();

            builder.addPropertyAndLabel("leadTime");
            builder.addLabel("tipoDocumentoOrdine", 7);
            Binding tipoDocumentoBinding = bf.createBoundSearchText("tipoAreaOrdine",
                    new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" });
            SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(tipoDocumentoBinding, 9, 2, 1);
            tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
            tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
            builder.nextRow();
        }

        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            builder.addHorizontalSeparator("Dati Vending", 1, 10);
            builder.nextRow();
            builder.addLabel("resa", 1);
            DefaultNumberFormatterFactory resaFormatter = new DefaultNumberFormatterFactory("###,##0", 2, Double.class);
            JFormattedTextField resaTextField = (JFormattedTextField) builder
                    .addBinding(bf.createBoundFormattedTextField("resa", resaFormatter), 3);
            resaTextField.setHorizontalAlignment(SwingConstants.RIGHT);
            builder.addPropertyAndLabel("somministrazione", 7);
            builder.nextRow();
        }

        if (pluginManager.isPresente("panjeaIntra")) {
            builder.addHorizontalSeparator("Dati Intra", 1, 10);
            builder.nextRow();
            builder.addLabel("datiIntra.nomenclatura");
            builder.addBinding(bf.createBoundSearchText("datiIntra.servizio", new String[] { "codice" },
                    new String[] { "tipoArticolo" }, new String[] { "tipoArticolo" }), 3);
            builder.nextRow();
            builder.addLabel("datiIntra.nazione");
            builder.addBinding(bf.createBoundSearchText("datiIntra.nazione", new String[] { "codice" }), 3);
            builder.nextRow();

            labelMassaNetta = builder.addLabel("datiIntra.massaNetta");
            massaNettaIntraField = (JTextField) builder
                    .addBinding(bf.createBoundFormattedTextField("datiIntra.massaNetta", getFactory(3)), 3);
            massaNettaIntraField.setColumns(10);
            massaNettaIntraField.setHorizontalAlignment(SwingConstants.RIGHT);
            builder.nextRow();

            labelValoreUMS = builder.addLabel("datiIntra.valoreUnitaMisuraSupplementare");
            valoreUnitaMisuraSupplementareIntraField = (JTextField) builder.addBinding(
                    bf.createBoundFormattedTextField("datiIntra.valoreUnitaMisuraSupplementare", getFactory(3)), 3);
            valoreUnitaMisuraSupplementareIntraField.setColumns(10);
            valoreUnitaMisuraSupplementareIntraField.setHorizontalAlignment(SwingConstants.RIGHT);
            builder.nextRow();

            modalitaErogazioneIntraComponents = builder.addPropertyAndLabel("datiIntra.modalitaErogazione", 1);
            builder.nextRow();

            showTipoArticoloIntraComponents(ETipoArticolo.FISICO);
            showValoriArticoloIntraComponents(null);

            // aggiungo tipoArticoloChangeListener solo se ho il plugin dell'intra abilitato
            addFormValueChangeListener("tipoArticolo", getTipoArticoloChangeListener());
            addFormValueChangeListener("datiIntra.servizio", getServizioChangeListener());
        }

        activatePropertyChangeListeners();
        addFormObjectChangeListener(getArticoloChangeListener());

        showListTipiMezzo(false);

        getFormModel().getValueModel("categoria.generazioneCodiceArticoloData.mascheraDescrizioneArticolo").getValue();
        // getFormModel().getValueModel("descrizione").getValue();

        getValueModel("numeroDecimaliPrezzo").addValueChangeListener(getNumeroDecimaliPropertyChange());

        return builder.getPanel();
    }

    /**
     * Crea il componente per la visualizzazione dei tipi mezzo trasporto legati all'articolo.
     *
     * @param bf
     *            {@link PanjeaSwingBindingFactory}
     * @return componente creato
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private JComponent createTipiMezzoTrasportoBinding(PanjeaSwingBindingFactory bf) {

        Binding bindingListTipiMezzo = bf.createBoundList("tipiMezzoTrasporto");
        JComponent componentListTipiMezzo = bindingListTipiMezzo.getControl();
        ((JList) componentListTipiMezzo).setLayoutOrientation(JList.HORIZONTAL_WRAP);
        ((JList) componentListTipiMezzo).setBackground(UIManager.getColor("JPanel.background"));

        ((JList) componentListTipiMezzo).setFocusable(false);
        ((JList) componentListTipiMezzo).setCellRenderer(new TipoMezzoListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(componentListTipiMezzo);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Tipi mezzo associati"));
        return scrollPane;
    }

    /**
     * Disattia i property change listeners di questo form.
     */
    public void deactivatePropertyChangeListeners() {
        removeFormValueChangeListener("provenienzaPrezzoArticolo", getProvenienzaPrezzoPropertyChangeListener());
    }

    @Override
    public void dispose() {
        if (tipoArticoloChangeListener != null) {
            removeFormValueChangeListener("tipoArticolo", getTipoArticoloChangeListener());
        }
        if (servizioChangeListener != null) {
            removeFormValueChangeListener("datiIntra.servizio", getServizioChangeListener());
        }
        if (numeroDecimaliPropertyChange != null) {
            getValueModel("numeroDecimaliPrezzo").removeValueChangeListener(getNumeroDecimaliPropertyChange());
        }
        if (calcolaEanCommand != null && calcolaEanActionCommandInterceptor != null) {
            calcolaEanCommand.removeCommandInterceptor(getCalcolaEanActionCommandInterceptor());
        }
        deactivatePropertyChangeListeners();

        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, getReadOnlyListener());
        removeFormObjectChangeListener(getArticoloChangeListener());
        super.dispose();
    }

    /**
     * @return ArticoloChangeListener
     */
    private PropertyChangeListener getArticoloChangeListener() {
        if (articoloChangeListener == null) {
            articoloChangeListener = new ArticoloChangeListener();
        }
        return articoloChangeListener;
    }

    /**
     * @return calcolaEanActionCommandInterceptor
     */
    public CalcolaEanActionCommandInterceptor getCalcolaEanActionCommandInterceptor() {
        if (calcolaEanActionCommandInterceptor == null) {
            calcolaEanActionCommandInterceptor = new CalcolaEanActionCommandInterceptor();
        }
        return calcolaEanActionCommandInterceptor;
    }

    /**
     * @param numeroDecimali
     *            numeroDecimali
     * @return DefaultFormatterFactory
     */
    private DefaultFormatterFactory getFactory(int numeroDecimali) {
        DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
                BigDecimal.class);
        return factory;
    }

    /**
     * @return numeroDecimaliPropertyChange
     */
    public NumeroDecimaliPropertyChange getNumeroDecimaliPropertyChange() {
        if (numeroDecimaliPropertyChange == null) {
            numeroDecimaliPropertyChange = new NumeroDecimaliPropertyChange();
        }
        return numeroDecimaliPropertyChange;
    }

    /**
     * @return ProvenienzaPrezzoPropertyChangeListener
     */
    private PropertyChangeListener getProvenienzaPrezzoPropertyChangeListener() {
        if (provenienzaPrezzoPropertyChangeListener == null) {
            provenienzaPrezzoPropertyChangeListener = new ProvenienzaPrezzoPropertyChangeListener();
        }
        return provenienzaPrezzoPropertyChangeListener;
    }

    /**
     * @return getReadOnlyListener
     */
    private ReadOnlyListener getReadOnlyListener() {
        if (readOnlyListener == null) {
            readOnlyListener = new ReadOnlyListener();
        }
        return readOnlyListener;
    }

    /**
     * @return ServizioChangeListener
     */
    private ServizioChangeListener getServizioChangeListener() {
        if (servizioChangeListener == null) {
            servizioChangeListener = new ServizioChangeListener();
        }
        return servizioChangeListener;
    }

    /**
     * @return TipoArticoloChangeListener
     */
    private TipoArticoloChangeListener getTipoArticoloChangeListener() {
        if (tipoArticoloChangeListener == null) {
            tipoArticoloChangeListener = new TipoArticoloChangeListener();
        }
        return tipoArticoloChangeListener;
    }

    @Override
    public void grabFocus() {
        costoStandardControl.requestFocus(false);
    }

    /**
     * @param magazzinoAnagraficaBD
     *            magazzinoAnagraficaBD
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
        calcolaEanCommand.setMagazzinoAnagraficaBD(magazzinoAnagraficaBD);
    }

    /**
     * Imposta il componente della lista di tipi mezzo visibile o invisibile.
     *
     * @param show
     *            boolean per visualizzare o nascondere il componente dei tipi mezzo.
     */
    private void showListTipiMezzo(boolean show) {
        tipiMezzoComponent.setVisible(show);
    }

    /**
     * Mostra i componenti a seconda del tipoArticolo selezionato.<br>
     * Massa netta viene visualizzato per articoli fisici e accessori, mentre la modalità di
     * erogazione viene visualizzata nel caso di servizi.
     *
     * @param tipoArticolo
     *            il tipo articolo per cui visualizzare i componenti variabili dell'intra
     */
    private void showTipoArticoloIntraComponents(ETipoArticolo tipoArticolo) {

        boolean isFisico = ETipoArticolo.FISICO.equals(tipoArticolo) || ETipoArticolo.ACCESSORI.equals(tipoArticolo);
        labelMassaNetta.setVisible(isFisico);
        massaNettaIntraField.setVisible(isFisico);
        labelValoreUMS.setVisible(isFisico);
        valoreUnitaMisuraSupplementareIntraField.setVisible(isFisico);

        boolean isServizio = ETipoArticolo.SERVIZI.equals(tipoArticolo);
        modalitaErogazioneIntraComponents[0].setVisible(isServizio);
        modalitaErogazioneIntraComponents[1].setVisible(isServizio);
    }

    /**
     * Nasconde/mostra i componenti della massa o valore u.m.suppl. a seconda del
     * servizio/nomenclatura selezionati.
     *
     * @param servizio
     *            se servizio non mostra nessun componente per il valore, se nomenclatura, nel caso
     *            in cui ci sia l'u.m.suppl. associata mostra il valore u.m.suppl. altrimenti la
     *            massa netta
     */
    private void showValoriArticoloIntraComponents(Servizio servizio) {
        boolean umsNotPresent = servizio != null && servizio instanceof Nomenclatura
                && ((Nomenclatura) servizio).getUmsupplementare() == null;
        boolean umsPresent = servizio != null && servizio instanceof Nomenclatura
                && ((Nomenclatura) servizio).getUmsupplementare() != null;
        labelMassaNetta.setVisible(umsNotPresent);
        massaNettaIntraField.setVisible(umsNotPresent);
        labelValoreUMS.setVisible(umsPresent);
        valoreUnitaMisuraSupplementareIntraField.setVisible(umsPresent);
    }
}
