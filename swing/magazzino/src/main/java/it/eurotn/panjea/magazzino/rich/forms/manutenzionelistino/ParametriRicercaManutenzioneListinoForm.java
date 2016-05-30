package it.eurotn.panjea.magazzino.rich.forms.manutenzionelistino;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.CheckBoxTreeSelectionModel;
import com.jidesoft.swing.SearchableUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino.ProvenienzaPrezzoManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino.TipoVariazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.FileChooser.FileChooserMode;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ParametriRicercaManutenzioneListinoForm extends PanjeaAbstractForm {

    /**
     * Tree selection listener per la selezione dei tipi documento.
     *
     * @author Leonardo
     */
    private class CheckBoxTreeSelectionListener implements TreeSelectionListener {

        /**
         * Aggiunge ricorsivamente i figli alla lista di categorie passata per riferimento.
         *
         * @param cat
         *            la categoria da ispezionare
         * @param cats
         *            la lista di categorie da riempire
         */
        private void addChildren(CategoriaLite cat, List<CategoriaLite> cats) {
            for (CategoriaLite kitten : cat.getCategorieFiglie()) {
                cats.add(kitten);
                addChildren(kitten, cats);
            }
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath[] selected = ((CheckBoxTreeSelectionModel) e.getSource()).getSelectionPaths();

            List<CategoriaLite> listCategorieSelezionate = new ArrayList<CategoriaLite>();

            if (selected != null) {
                getValueModel("tutteCategorie").setValue(true);
                for (TreePath selectionTreePath : selected) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionTreePath.getLastPathComponent();

                    if (node.getUserObject() instanceof CategoriaLite) {
                        CategoriaLite cat = (CategoriaLite) node.getUserObject();
                        if (cat.getId().equals(-1)) {
                            listCategorieSelezionate.clear();
                            ((ParametriRicercaManutenzioneListino) getFormObject()).setTutteCategorie(true);
                        } else {
                            ((ParametriRicercaManutenzioneListino) getFormObject()).setTutteCategorie(false);
                            listCategorieSelezionate.add(cat);
                            addChildren(cat, listCategorieSelezionate);
                        }
                    }
                }
                getValueModel("categorie").setValue(listCategorieSelezionate);
            } else {
                ((ParametriRicercaManutenzioneListino) getFormObject()).setTutteCategorie(false);
                getValueModel("categorie").setValue(new ArrayList<CategoriaLite>());
            }

        }
    }

    private class ProvenienzaPrezzoValueChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ProvenienzaPrezzoManutenzioneListino provenienzaPrezzoManutenzioneListino = (ProvenienzaPrezzoManutenzioneListino) evt
                    .getNewValue();

            if (provenienzaPrezzoManutenzioneListino == ProvenienzaPrezzoManutenzioneListino.FILE_ESTERNO) {
                getValueModel("variazione").setValue(BigDecimal.ZERO);
                parametriControl.setVisible(false);
                setProvenienzafileEsternoComponentVisible(true);
            } else {
                getValueModel("tipoVariazione").setValue(TipoVariazione.PERCENTUALE);
                parametriControl.setVisible(true);
                setProvenienzafileEsternoComponentVisible(false);
            }

            boolean depositoVisible = isDepositoVisible(provenienzaPrezzoManutenzioneListino);

            labelDeposito.setVisible(depositoVisible);
            searchPanelDeposito.setVisible(depositoVisible);
            if (!depositoVisible) {
                getValueModel("deposito").setValue(null);
            }

            // se cambio la provenienza prezzo devo allineare la validazione per l'obbligatorieta'
            // della versione
            // listino
            getFormModel().validate();
        }

    }

    private class SvuotaRicercaArticoliCommand extends ActionCommand {
        public SvuotaRicercaArticoliCommand() {
            super("svuotaRicercaArticoliCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getValueModel("articoli").setValue(new ArrayList<ArticoloLite>());
            labelArticoli.setText("Tutti");
        }
    }

    private static final String FORM_ID = "parametriRicercaManutenzioneListinoForm";

    private SearchPanel searchPanelDeposito;
    private JLabel labelDeposito;
    private CheckBoxTree treeCategorie = null;
    private DefaultTreeModel treeModel;

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
    private JScrollPane scrollPaneCategorieComponent;

    private CheckBoxTreeSelectionListener treeSelectionListener;

    private JLabel labelArticoli;

    private RicercaAvanzataArticoliCommand ricercaAvanzataArticoliCommand;

    private JComponent parametriControl;

    private JComponent[] parametroFileControl;

    /**
     * Default constructor.
     */
    public ParametriRicercaManutenzioneListinoForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaManutenzioneListino(), false, FORM_ID),
                FORM_ID);
        this.getFormModel().addPropertyChangeListener("readOnly", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (treeCategorie != null) {
                    boolean readOnly = ((Boolean) evt.getNewValue()).booleanValue();

                    treeCategorie.setCheckBoxEnabled(!readOnly);

                    if (!readOnly) {
                        treeCategorie.setBackground((Color) UIManager.getDefaults().get("JPanel.background"));
                    } else {
                        treeCategorie.setBackground(UIManager.getDefaults().getColor("TextField.inactiveBackground"));
                    }
                }
            }
        });

        // aggiungo la finta proprietà tipiEntita per far si che la search text dell'entità mi
        // selezioni solo clienti e
        // fornitori
        List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
        tipiEntita.add(TipoEntita.FORNITORE);
        tipiEntita.add(TipoEntita.CLIENTE);

        ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
        DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(tipiEntitaValueModel), List.class, true, null);
        getFormModel().add("tipiEntita", tipiEntitaValueModel, tipiEntitaData);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default,right:pref,4dlu,left:default,right:pref,4dlu,left:default:grow",
                "default,2dlu,default,2dlu,fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new
                                                                              // FormDebugPanelNumbered());

        builder.addPropertyAndLabel("provenienzaPrezzoManutenzioneListino", 1);
        builder.nextRow();
        parametroFileControl = new JComponent[4];
        parametroFileControl[0] = builder.addLabel("file");
        parametroFileControl[1] = builder
                .addBinding(bf.createBoundFileChooseBinding("file", true, FileChooserMode.FILE), 3, 3, 3, 1);
        JComponent[] compsNumDecimali = builder.addPropertyAndLabel("numeroDecimali", 7, 3);
        parametroFileControl[2] = compsNumDecimali[0];
        parametroFileControl[3] = compsNumDecimali[1];
        ((JTextField) compsNumDecimali[1]).setColumns(2);
        setProvenienzafileEsternoComponentVisible(false);
        builder.nextRow();

        parametriControl = createProvenienzaListinoDefaultControl();
        builder.addComponent(parametriControl, 1, 5, 9, 1);
        return builder.getPanel();

    }

    /**
     * Crea il nodo principale dell'albero ed inserisci i figli.
     *
     * @return rootNode
     */
    public DefaultMutableTreeNode createNode() {
        List<CategoriaLite> list = magazzinoAnagraficaBD.caricaCategorie();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        // aggiungo una categoriaDTO con id-1 er indicare che è la categoria
        // "root"
        CategoriaLite categoriaRoot = new CategoriaLite();
        categoriaRoot.setId(-1);
        root.setUserObject(categoriaRoot);
        for (CategoriaLite categoriaDTO : list) {
            root.add(createNodeForCategoria(categoriaDTO));
        }
        return root;
    }

    /**
     * Metodo ricorsivo che data una categoria crea il nodo ed aggiunge i figli.<br>
     * . <b>NB</b>: i figli posso essere categorie.
     *
     * @param categoriaLite
     *            per il quale creare il nodo
     * @return nodo per la categoria con i suoi figli
     */
    public DefaultMutableTreeNode createNodeForCategoria(CategoriaLite categoriaLite) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(categoriaLite);
        for (CategoriaLite categoriaDTOFiglia : categoriaLite.getCategorieFiglie()) {
            node.add(createNodeForCategoria(categoriaDTOFiglia));
        }
        return node;
    }

    private JComponent createProvenienzaListinoDefaultControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default,8dlu,right:default, left:4dlu,left:default,left:default, left:default,left:default,4dlu,left:50 dlu, fill:default:grow",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default, fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new
                                                                               // FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);
        builder.nextRow();

        labelDeposito = builder.addLabel("deposito", 1);
        Binding bindDeposito = bf.createBoundSearchText("deposito", new String[] { "codice", "descrizione" });
        searchPanelDeposito = (SearchPanel) builder.addBinding(bindDeposito, 3, 4, 5, 1);
        searchPanelDeposito.getTextFields().get("codice").setColumns(8);
        searchPanelDeposito.getTextFields().get("descrizione").setColumns(20);
        builder.nextRow();

        builder.addLabel("variazione", 1);

        JPanel variazionePanel = new JPanel(new BorderLayout(8, 0));
        Binding bindingVariazionePercentuale = bf.createBoundFormattedTextField("variazione", getFactory(6));
        JTextField formattedTextField = (JTextField) bindingVariazionePercentuale.getControl();
        formattedTextField.setColumns(7);
        variazionePanel.add(formattedTextField, BorderLayout.CENTER);
        variazionePanel.add(bf.createEnumRadioButtonBinding(TipoVariazione.class, "tipoVariazione").getControl(),
                BorderLayout.EAST);

        builder.addComponent(variazionePanel, 3);

        JComponent[] compsNumDecimali = builder.addPropertyAndLabel("numeroDecimali", 5);
        ((JTextField) compsNumDecimali[1]).setColumns(2);
        builder.nextRow();

        builder.addLabel("versioneListino", 1);
        Binding bindVersioneListino = bf.createBoundSearchText("versioneListino",
                new String[] { "listino.codice", "listino.descrizione", "codice" });
        SearchPanel searchPanelVersioneListino = (SearchPanel) builder.addBinding(bindVersioneListino, 3, 8, 5, 1);
        searchPanelVersioneListino.getTextFields().get("listino.codice").setColumns(8);
        searchPanelVersioneListino.getTextFields().get("listino.descrizione").setColumns(15);
        searchPanelVersioneListino.getTextFields().get("codice").setColumns(5);
        builder.nextRow();

        builder.addLabel("entita", 1);
        Binding bindFornitore = bf.createBoundSearchText("entita",
                new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntita" },
                new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
        SearchPanel searchPanelFornitore = (SearchPanel) builder.addBinding(bindFornitore, 3, 10, 5, 1);
        searchPanelFornitore.getTextFields().get("codice").setColumns(8);
        searchPanelFornitore.getTextFields().get("anagrafica.denominazione").setColumns(20);

        builder.addProperty("entitaTipoRicerca", 8, 10);
        builder.nextRow();

        builder.addHorizontalSeparator("Articoli selezionati", 3);
        builder.nextRow();

        JPanel commandsPanel = new JPanel(new FlowLayout());
        commandsPanel.setBorder(null);
        labelArticoli = new JLabel("Tutti");
        commandsPanel.add(labelArticoli);
        commandsPanel.add(getRicercaAvanzataArticoliCommand().createButton());
        commandsPanel.add(new SvuotaRicercaArticoliCommand().createButton());
        builder.addComponent(commandsPanel, 3, 13);

        // di default nascondo il deposito che viene visualizzato solo con provenienza anagrafica e
        // provenienza prezzo
        // ultimo costo
        labelDeposito.setVisible(false);
        searchPanelDeposito.setVisible(false);
        builder.nextRow();

        builder.addComponent(createTreeCategorieComponent(), 11, 2, 3, 12);

        builder.nextRow();
        addFormValueChangeListener("provenienzaPrezzoManutenzioneListino", new ProvenienzaPrezzoValueChangeListener());
        return builder.getPanel();
    }

    /**
     * @return JComponent
     */
    public JComponent createTreeCategorieComponent() {
        treeModel = new DefaultTreeModel(createNode());

        treeCategorie = new CheckBoxTree(treeModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(250, 100);
            }
        };
        treeCategorie.setRootVisible(true);
        treeCategorie.setShowsRootHandles(false);
        treeSelectionListener = new CheckBoxTreeSelectionListener();
        treeCategorie.getCheckBoxTreeSelectionModel().addTreeSelectionListener(treeSelectionListener);
        treeCategorie.setCellRenderer(new CategoriaTreeCellRenderer());

        SearchableUtils.installSearchable(treeCategorie);
        scrollPaneCategorieComponent = getComponentFactory().createScrollPane(treeCategorie);
        return scrollPaneCategorieComponent;
    }

    /**
     * @param numeroDecimali
     *            numeroDecimali
     * @return DefaultFormatterFactory
     */
    private DefaultFormatterFactory getFactory(Integer numeroDecimali) {
        DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
                BigDecimal.class);
        return factory;
    }

    /**
     * @return the ricercaAvanzataArticoliCommand
     */
    private RicercaAvanzataArticoliCommand getRicercaAvanzataArticoliCommand() {
        if (ricercaAvanzataArticoliCommand == null) {
            ricercaAvanzataArticoliCommand = new RicercaAvanzataArticoliCommand(
                    "aggiungiRicercaAvanzataArticoliCommand");
            ricercaAvanzataArticoliCommand.addCommandInterceptor(new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand command) {
                    List<ArticoloRicerca> articoliRicerca = ((RicercaAvanzataArticoliCommand) command)
                            .getArticoliSelezionati();
                    if (articoliRicerca != null && articoliRicerca.size() > 0) {
                        List<ArticoloLite> articoli = new ArrayList<>(
                                ((ParametriRicercaManutenzioneListino) getFormObject()).getArticoli());
                        for (ArticoloRicerca articoloRicerca : articoliRicerca) {
                            articoli.add(articoloRicerca.createProxyArticoloLite());
                        }
                        getValueModel("articoli").setValue(articoli);
                        labelArticoli.setText(new Integer(articoli.size()).toString());
                    }

                }

                @Override
                public boolean preExecution(ActionCommand command) {
                    return true;
                }
            });
        }

        return ricercaAvanzataArticoliCommand;
    }

    /**
     * Restituisce il valore booleano che indica se il deposito deve essere presente.
     *
     * @param provenienzaPrezzoManutenzioneListino
     *            provenienzaPrezzoManutenzioneListino
     * @return true o false
     */
    private boolean isDepositoVisible(ProvenienzaPrezzoManutenzioneListino provenienzaPrezzoManutenzioneListino) {
        return provenienzaPrezzoManutenzioneListino.equals(ProvenienzaPrezzoManutenzioneListino.ULTIMO_COSTO_DEPOSITO)
                || provenienzaPrezzoManutenzioneListino
                        .equals(ProvenienzaPrezzoManutenzioneListino.COSTO_MEDIO_PONDERATO);
    }

    /**
     * @param magazzinoAnagraficaBD
     *            the magazzinoAnagraficaBD to set
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    private void setProvenienzafileEsternoComponentVisible(boolean visible) {
        for (JComponent componenti : parametroFileControl) {
            componenti.setVisible(visible);
        }
    }

}
