package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.GuiStandardUtils;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.fatturepa.rich.renderer.XMLGregorianCalendarCellRenderer;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DettaglioPagamentoType;

public class DatiPagamentoForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "datiPagamentoFormV1_1";

    /**
     * Costruttore.
     *
     */
    public DatiPagamentoForm() {
        super(PanjeaFormModelHelper.createFormModel(new DatiPagamentoType(), false, FORM_ID), FORM_ID);
        getFormModel().setReadOnly(true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref,4dlu,fill:90dlu,fill:default:grow",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,100dlu");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addHorizontalSeparator("Condizioni pagamento (2.4.1)", 4);
        builder.nextRow();

        builder.addPropertyAndLabel("condizioniPagamento");
        builder.nextRow();

        builder.addHorizontalSeparator("Dettagli pagamento (2.4.2)", 4);

        TableEditableBinding<DettaglioPagamentoType> dettagliPagamentiBinding = new TableEditableBinding<DettaglioPagamentoType>(
                getFormModel(), "dettaglioPagamento", List.class, new DettaglioPagamentoTypeTableModel());
        builder.addComponent(dettagliPagamentiBinding.getControl(), 1, 8, 4, 1);
        dettagliPagamentiBinding.getTableWidget().getTable().getColumnModel().getColumn(1)
                .setCellRenderer(new XMLGregorianCalendarCellRenderer());
        dettagliPagamentiBinding.getTableWidget().getTable().getColumnModel().getColumn(3)
                .setCellRenderer(new XMLGregorianCalendarCellRenderer());

        return GuiStandardUtils.attachBorder(builder.getPanel());
    }
}
