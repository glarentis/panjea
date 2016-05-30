package it.eurotn.panjea.vending.rich.editors.movimenticassa;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class MovimentoCassaForm extends PanjeaAbstractForm implements PropertyChangeListener {

    private class CassaDestinazioneListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            MovimentoCassa movimento = (MovimentoCassa) getFormObject();
            boolean cassaDestPresente = movimento.getCassaDestinazione() != null
                    && !movimento.getCassaDestinazione().isNew();
            righeMovimentoCassaTableModel.setCassaDestinazionePresente(cassaDestPresente);

            if (cassaDestPresente) {
                for (RigaMovimentoCassa riga : righeMovimentoCassaBinding.getTableWidget().getRows()) {
                    // brutto, viene aggiornata per riferimento
                    riga.setQuantitaEntrata(0);
                }
            }
        }

    }

    private static final String FORM_ID = "movimentoCassaForm";

    private RigheMovimentoCassaTableModel righeMovimentoCassaTableModel;

    private TableEditableBinding<RigaMovimentoCassa> righeMovimentoCassaBinding;

    private JLabel cassaDestinazioneLabel;
    private SearchPanel searchCassaDest;

    private CassaDestinazioneListener cassaDestinazioneListener = new CassaDestinazioneListener();

    /**
     * Costruttore.
     */
    public MovimentoCassaForm() {
        super(PanjeaFormModelHelper.createFormModel(new MovimentoCassa(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "10dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addLabel("cassa");
        SearchPanel searchCassa = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("cassa", new String[] { "codice", "descrizione" }), 3);
        searchCassa.getTextFields().get("codice").setColumns(10);
        builder.nextRow();

        builder.addPropertyAndLabel("data");
        builder.nextRow();

        cassaDestinazioneLabel = builder.addLabel("cassaDestinazione");
        searchCassaDest = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("cassaDestinazione", new String[] { "codice", "descrizione" }), 3);
        searchCassaDest.getTextFields().get("codice").setColumns(10);
        builder.nextRow();

        righeMovimentoCassaTableModel = new RigheMovimentoCassaTableModel();
        righeMovimentoCassaBinding = new TableEditableBinding<>(getFormModel(), "righe", Set.class,
                righeMovimentoCassaTableModel);
        builder.addBinding(righeMovimentoCassaBinding, 1, 3, 1);

        getFormModel().addPropertyChangeListener(this);
        getFormModel().addPropertyChangeListener(cassaDestinazioneListener);
        getValueModel("cassaDestinazione").addValueChangeListener(cassaDestinazioneListener);

        setCassaDestinazioneVisible(((MovimentoCassa) getFormObject()).isNew());

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getFormModel().removePropertyChangeListener(this);
        getFormModel().removePropertyChangeListener(cassaDestinazioneListener);
        getValueModel("cassaDestinazione").removeValueChangeListener(cassaDestinazioneListener);
        cassaDestinazioneListener = null;

        super.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        setCassaDestinazioneVisible(((MovimentoCassa) getFormObject()).isNew());
    }

    private void setCassaDestinazioneVisible(boolean visible) {
        cassaDestinazioneLabel.setVisible(visible);
        searchCassaDest.setVisible(visible);
    }

}