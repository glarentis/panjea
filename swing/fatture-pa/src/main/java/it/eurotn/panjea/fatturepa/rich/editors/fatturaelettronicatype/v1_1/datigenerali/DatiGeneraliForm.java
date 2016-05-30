package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali.DatiDDTTableModel;
import it.eurotn.panjea.fatturepa.rich.renderer.XMLGregorianCalendarCellRenderer;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDDTType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDocumentiCorrelatiType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiSALType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiGeneraliType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaBodyType;

public class DatiGeneraliForm extends PanjeaAbstractForm {

    private class CausaliListKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {

            if (!causaliList.getSelectedValuesList().isEmpty() && !getFormModel().isReadOnly()) {
                if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                    @SuppressWarnings("unchecked")
                    List<String> causali = (List<String>) getValueModel("datiGenerali.datiGeneraliDocumento.causale")
                            .getValue();
                    causali.removeAll(causaliList.getSelectedValuesList());
                    DatiGeneraliType dati = (DatiGeneraliType) getFormModel().getValueModel("datiGenerali").getValue();
                    dati.getDatiGeneraliDocumento().setCausale(new ArrayList<String>());
                    dati.getDatiGeneraliDocumento().getCausale().addAll(causali);
                    getFormModel().getValueModel("datiGenerali").setValue(
                            PanjeaSwingUtil.cloneObject(getFormModel().getValueModel("datiGenerali").getValue()));
                } else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    addNewCausale();
                }
            }
        }
    }

    private class CausaliListMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            if (!getFormModel().isReadOnly() && event.getClickCount() == 2 && !event.isConsumed()) {
                event.consume();

                addNewCausale();
            }
        }
    }

    private static final String FORM_ID = "datiGeneraliFormV1";

    private JList<String> causaliList;

    private DatiDocumentiCorrelatiTypeTableModel datiOrdineAcquistoTableModel = new DatiDocumentiCorrelatiTypeTableModel();
    private DatiDocumentiCorrelatiTypeTableModel datiContrattoTableModel = new DatiDocumentiCorrelatiTypeTableModel();
    private DatiDocumentiCorrelatiTypeTableModel datiConvenzioneTableModel = new DatiDocumentiCorrelatiTypeTableModel();
    private DatiDocumentiCorrelatiTypeTableModel datiRicezioneTableModel = new DatiDocumentiCorrelatiTypeTableModel();
    private DatiDocumentiCorrelatiTypeTableModel datiFattureCollegateTableModel = new DatiDocumentiCorrelatiTypeTableModel();

    /**
     * Costruttore.
     *
     */
    public DatiGeneraliForm() {
        super(PanjeaFormModelHelper.createFormModel(new FatturaElettronicaBodyType(), false, FORM_ID), FORM_ID);
    }

    /**
     * Apre il dialog per aggiungere una nuova causale.
     */
    public void addNewCausale() {
        InputApplicationDialog dialog = new InputApplicationDialog("Nuova causale", (Window) null);
        dialog.setPreferredSize(new Dimension(700, 100));
        dialog.setInputLabelMessage("Causale (max 200 caratteri)");
        dialog.setInputField(new JTextField());
        dialog.setFinishAction(new Closure() {

            @Override
            public Object call(Object paramObject) {
                String causale = (String) paramObject;

                if (!StringUtils.isBlank(causale)) {
                    @SuppressWarnings("unchecked")
                    List<String> causali = (List<String>) getValueModel("datiGenerali.datiGeneraliDocumento.causale")
                            .getValue();
                    causali.add(StringUtils.left(causale, 200));
                    DatiGeneraliType dati = (DatiGeneraliType) getFormModel().getValueModel("datiGenerali").getValue();
                    dati.getDatiGeneraliDocumento().setCausale(new ArrayList<String>());
                    dati.getDatiGeneraliDocumento().getCausale().addAll(causali);
                    getFormModel().getValueModel("datiGenerali").setValue(
                            PanjeaSwingUtil.cloneObject(getFormModel().getValueModel("datiGenerali").getValue()));
                }

                return null;
            }
        });
        dialog.showDialog();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "left:pref,4dlu,fill:90dlu,10dlu,left:pref,4dlu,fill:90dlu,10dlu,left:pref,4dlu,fill:90dlu",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,fill:80dlu,2dlu,default,2dlu,100dlu");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addHorizontalSeparator("Dati documento (2.1.1)", 7);
        builder.addHorizontalSeparator("Dati bollo (2.1.1.6)", 9, 3);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.tipoDocumento", 1);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.numero", 5);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.datiBollo.bolloVirtuale", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.data", 1);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.divisa", 5);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.datiBollo.importoBollo", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.importoTotaleDocumento", 1);
        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.arrotondamento", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("datiGenerali.datiGeneraliDocumento.art73", 1);
        builder.nextRow();
        builder.nextRow();

        builder.addHorizontalSeparator("Causali (2.1.1.11)", 1, 5);
        builder.addHorizontalSeparator("Dati SAL (2.1.7)", 7, 1);
        builder.addHorizontalSeparator("Dati ddt di riferimento (2.1.8)", 9, 3);
        builder.nextRow();

        causaliList = (JList<String>) builder
                .addBinding(bf.createBoundList("datiGenerali.datiGeneraliDocumento.causale"), 1, 16, 5, 1);
        causaliList.addMouseListener(new CausaliListMouseListener());
        causaliList.addKeyListener(new CausaliListKeyListener());
        builder.nextRow();

        TableEditableBinding<DatiSALType> datiSalBinding = new TableEditableBinding<DatiSALType>(getFormModel(),
                "datiGenerali.datiSAL", List.class, new DatiSALTableModel());
        builder.addComponent(datiSalBinding.getControl(), 7, 16);

        DatiDDTTableModel datiDDTTableModel = new DatiDDTTableModel();
        TableEditableBinding<DatiDDTType> datiDDTBinding = new TableEditableBinding<DatiDDTType>(getFormModel(),
                "datiGenerali.datiDDT", Set.class, datiDDTTableModel);
        builder.addComponent(datiDDTBinding.getControl(), 9, 16, 3, 1);
        datiDDTBinding.getTableWidget().getTable().getColumnModel().getColumn(1)
                .setCellRenderer(new XMLGregorianCalendarCellRenderer());

        TableEditableBinding<DatiDocumentiCorrelatiType> datiOrdineAcquistoBinding = new TableEditableBinding<DatiDocumentiCorrelatiType>(
                getFormModel(), "datiGenerali.datiOrdineAcquisto", Set.class, datiOrdineAcquistoTableModel);

        TableEditableBinding<DatiDocumentiCorrelatiType> datiContrattoBinding = new TableEditableBinding<DatiDocumentiCorrelatiType>(
                getFormModel(), "datiGenerali.datiContratto", Set.class, datiContrattoTableModel);

        TableEditableBinding<DatiDocumentiCorrelatiType> datiConvenzioneBinding = new TableEditableBinding<DatiDocumentiCorrelatiType>(
                getFormModel(), "datiGenerali.datiConvenzione", Set.class, datiConvenzioneTableModel);

        TableEditableBinding<DatiDocumentiCorrelatiType> datiRicezioneBinding = new TableEditableBinding<DatiDocumentiCorrelatiType>(
                getFormModel(), "datiGenerali.datiRicezione", Set.class, datiRicezioneTableModel);

        TableEditableBinding<DatiDocumentiCorrelatiType> datiFattureCollegateBinding = new TableEditableBinding<DatiDocumentiCorrelatiType>(
                getFormModel(), "datiGenerali.datiFattureCollegate", Set.class, datiFattureCollegateTableModel);

        JTabbedPane tabbedPane = getComponentFactory().createTabbedPane();
        tabbedPane.addTab("Dati ordine acquisto (2.1.2)", datiOrdineAcquistoBinding.getControl());
        tabbedPane.addTab("Dati contratto (2.1.3)", datiContrattoBinding.getControl());
        tabbedPane.addTab("Dati convenzione (2.1.4)", datiConvenzioneBinding.getControl());
        tabbedPane.addTab("Dati ricezione (2.1.5)", datiRicezioneBinding.getControl());
        tabbedPane.addTab("Dati fatture collegate (2.1.6)", datiFattureCollegateBinding.getControl());

        datiOrdineAcquistoBinding.getTableWidget().getTable().getColumnModel().getColumn(0)
                .setCellEditor(new RiferimentoNumLineaTableCellEditor());
        datiContrattoBinding.getTableWidget().getTable().getColumnModel().getColumn(0)
                .setCellEditor(new RiferimentoNumLineaTableCellEditor());
        datiConvenzioneBinding.getTableWidget().getTable().getColumnModel().getColumn(0)
                .setCellEditor(new RiferimentoNumLineaTableCellEditor());
        datiRicezioneBinding.getTableWidget().getTable().getColumnModel().getColumn(0)
                .setCellEditor(new RiferimentoNumLineaTableCellEditor());
        datiFattureCollegateBinding.getTableWidget().getTable().getColumnModel().getColumn(0)
                .setCellEditor(new RiferimentoNumLineaTableCellEditor());

        builder.addComponent(tabbedPane, 1, 18, 11, 3);

        return builder.getPanel();
    }

    /**
     * @param idAreaMagazzino
     *            the idAreaMagazzino to set
     */
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {

        datiOrdineAcquistoTableModel.setIdAreaMagazzino(idAreaMagazzino);
        datiContrattoTableModel.setIdAreaMagazzino(idAreaMagazzino);
        datiConvenzioneTableModel.setIdAreaMagazzino(idAreaMagazzino);
        datiRicezioneTableModel.setIdAreaMagazzino(idAreaMagazzino);
        datiFattureCollegateTableModel.setIdAreaMagazzino(idAreaMagazzino);

    }

}
