package it.eurotn.panjea.vending.rich.editors.prodotticollegati;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.manutenzioni.rich.bd.ManutenzioniBD;
import it.eurotn.panjea.vending.domain.ProdottoTipoModello;
import it.eurotn.panjea.vending.rich.editors.tipimodello.ProdottoCollegatoInsertForm;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

public abstract class ProdottiCollegatiComponent extends JPanel {

    private class AggiungiProdottoCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public AggiungiProdottoCommand() {
            super("aggiungiProdottoCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ProdottoCollegato prodotto = (ProdottoCollegato) prodottoForm.getFormObject();
            prodotto = onSave(prodotto);
            prodotto = manutenzioniBD.salvaProdottoCollegato(prodotto);
            prodottiTable.addRowObject(prodotto, null);
            for (PropertyChangeListener listener : prodottiListener) {
                PropertyChangeEvent event = new PropertyChangeEvent(prodottiTable, PRODOTTO_AGGIUNTO_EVENT, null,
                        prodotto);
                listener.propertyChange(event);
            }
            prodottoForm.getNewFormObjectCommand().execute();
            prodottoForm.grabFocus();
        }

    }

    private class DeleteProdottoKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            if (ProdottiCollegatiComponent.this.isEnabled() && event.getKeyCode() == KeyEvent.VK_DELETE) {
                event.consume();
                for (ProdottoCollegato prodotto : prodottiTable.getSelectedObjects()) {
                    manutenzioniBD.cancellaProdottoCollegato(prodotto.getId());
                    prodottiTable.removeRowObject(prodotto);
                    for (PropertyChangeListener listener : prodottiListener) {
                        PropertyChangeEvent prodottoEvent = new PropertyChangeEvent(prodottiTable,
                                PRODOTTO_RIMOSSO_EVENT, null, prodotto);
                        listener.propertyChange(prodottoEvent);
                    }
                }
            }
        }
    }

    private class ProdottiTableModel extends DefaultBeanTableModel<ProdottoCollegato> {

        private static final long serialVersionUID = -8787310467339808159L;

        /**
         * Costruttore.
         *
         * @param prototypeClass
         *            classe concreta del prodotto
         */
        public ProdottiTableModel(final Class<?> prototypeClass) {
            super("prodottiTableModel", new String[] { "articolo" }, ProdottoCollegato.class, prototypeClass);
        }
    }

    public static final String PRODOTTO_AGGIUNTO_EVENT = "prodottoAggiuntoEvent";

    public static final String PRODOTTO_RIMOSSO_EVENT = "prodottoRimossoEvent";

    private static final long serialVersionUID = -6922019078634542180L;

    private IManutenzioniBD manutenzioniBD;

    private ProdottoCollegatoInsertForm prodottoForm;
    private JideTableWidget<ProdottoCollegato> prodottiTable;

    private String title;
    private Class<? extends ProdottoCollegato> prodottoClass;

    private boolean addInsertControls;

    private DeleteProdottoKeyListener deleteProdottoKeyListener = new DeleteProdottoKeyListener();

    private List<PropertyChangeListener> prodottiListener = new ArrayList<>();

    private AggiungiProdottoCommand aggiungiProdottoCommand = null;

    /**
     * Costruttore.
     *
     * @param title
     *            titolo del componente
     */
    public ProdottiCollegatiComponent(final String title) {
        this(title, ProdottoTipoModello.class, false);
    }

    /**
     * Costruttore.
     *
     * @param title
     *            titolo del componente
     * @param prodottoClass
     *            classe concreta del prodotto da gestire
     * @param addInsertControls
     *            inserisce i controllo per aggiunge i prodotti
     */
    public ProdottiCollegatiComponent(final String title, final Class<? extends ProdottoCollegato> prodottoClass,
            final boolean addInsertControls) {
        super();
        this.title = title;
        this.prodottoClass = prodottoClass;
        this.addInsertControls = addInsertControls;
        this.manutenzioniBD = RcpSupport.getBean(ManutenzioniBD.BEAN_ID);

        initControls();
    }

    /**
     * @param listener
     *            listener da aggiungere
     */
    public void addProdottiListener(PropertyChangeListener listener) {
        this.prodottiListener.add(listener);
    }

    /**
     * Esegue il dispose.
     */
    public void dispose() {
        if (prodottoForm != null) {
            prodottoForm.dispose();
        }
        prodottiTable.dispose();
    }

    private FormLayout getComponentLayout() {

        StringBuilder rowSpec = new StringBuilder();
        if (addInsertControls) {
            rowSpec.append("4dlu,bottom:default,");
        }
        rowSpec.append("4dlu,fill:default:grow");

        return new FormLayout("fill:200dlu,left:pref", rowSpec.toString());
    }

    /**
     * @return classe su cui applicare il renderer dei prodotti aggiunti/rimossi
     */
    public Class<? extends ProdottoCollegato> getProdottiRendererClass() {
        return null;
    }

    /**
     * @return table widget dei prodotti.
     */
    public JideTableWidget<ProdottoCollegato> getTableWidget() {
        return prodottiTable;
    }

    private void initControls() {
        setLayout(getComponentLayout());
        CellConstraints cc = new CellConstraints();

        int rowAdded = 0;
        if (StringUtils.isNotBlank(title)) {
            setBorder(BorderFactory.createTitledBorder(title));
        }

        if (addInsertControls) {
            prodottoForm = new ProdottoCollegatoInsertForm(
                    (ProdottoCollegato) BeanUtils.instantiateClass(prodottoClass));
            add(prodottoForm.getControl(), cc.xy(1, 2 + rowAdded));
            aggiungiProdottoCommand = new AggiungiProdottoCommand();
            add(aggiungiProdottoCommand.createButton(), cc.xy(2, 2 + rowAdded));
            rowAdded += 2;
        }

        prodottiTable = new JideTableWidget<>("prodottiTable", new ProdottiTableModel(prodottoClass));
        if (getProdottiRendererClass() != null) {
            prodottiTable.getTable().getColumnModel().getColumn(0)
                    .setCellRenderer(new ProdottiCollegatiRenderer(getProdottiRendererClass()));
        }
        add(prodottiTable.getComponent(), cc.xyw(1, 2 + rowAdded, 2));
        prodottiTable.setNumberRowVisible(false);
        if (addInsertControls) {
            prodottiTable.getTable().addKeyListener(deleteProdottoKeyListener);
        }
    }

    /**
     * Motodo richiamato settare tutte le proprietà al prodotto prima di salvarlo
     *
     * @param prodottoCollegato
     *            prodotto da salvare
     * @return prodotto
     */
    public abstract ProdottoCollegato onSave(ProdottoCollegato prodottoCollegato);

    /**
     * @param readOnly
     *            read only
     */
    public void setReadOnly(boolean readOnly) {
        if (aggiungiProdottoCommand != null) {
            aggiungiProdottoCommand.setEnabled(!readOnly);
        }
        prodottiTable.setEditable(!readOnly);

        setEnabled(!readOnly);
    }

}
