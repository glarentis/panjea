package it.eurotn.panjea.vending.rich.editors.casse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.bd.VendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.editors.casse.movimentazione.MovimentazioneCassaTable;
import it.eurotn.panjea.vending.rich.editors.casse.situazione.SituazioneCassaTable;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class CassaForm extends PanjeaAbstractForm implements PropertyChangeListener {

    private static final String FORM_ID = "cassaForm";

    private JideTabbedPane tabbedPane;

    private SituazioneCassaTable situazioneCassaTable = new SituazioneCassaTable();

    private MovimentazioneCassaTable movimentazioneCassaTable = new MovimentazioneCassaTable();

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore.
     */
    public CassaForm() {
        super(PanjeaFormModelHelper.createFormModel(new Cassa(), false, FORM_ID), FORM_ID);
        this.vendingAnagraficaBD = RcpSupport.getBean(VendingAnagraficaBD.BEAN_ID);

        movimentazioneCassaTable.addMovimentazioneCassaListener(this);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:60dlu, 10dlu, right:pref,4dlu,fill:pref, 10dlu, right:pref,4dlu,fill:pref",
                "1dlu,default,10dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("codice");
        builder.addPropertyAndLabel("descrizione", 5);
        builder.addPropertyAndLabel("tipologia", 9);
        builder.nextRow();

        tabbedPane = new JideTabbedPane();
        tabbedPane.addTab("Situazione cassa", RcpSupport.getIcon(Cassa.class.getName()),
                situazioneCassaTable.getComponent());
        tabbedPane.addTab("Movimentazione", RcpSupport.getIcon("movimentiCassaEditor.image"),
                movimentazioneCassaTable.getComponent());
        builder.addComponent(tabbedPane, 1, 11, 1);

        return builder.getPanel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!((Cassa) getFormObject()).isNew()) {
            Cassa cassa = vendingAnagraficaBD.caricaCassaById(((Cassa) getFormObject()).getId());
            setFormObject(cassa);
        }
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);

        Cassa cassa = (Cassa) formObject;
        situazioneCassaTable.setCassa(cassa);
        movimentazioneCassaTable.setCassa(cassa);
    }

}