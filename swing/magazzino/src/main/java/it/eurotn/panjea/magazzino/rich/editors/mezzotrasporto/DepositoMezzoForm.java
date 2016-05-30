package it.eurotn.panjea.magazzino.rich.editors.mezzotrasporto;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class DepositoMezzoForm extends PanjeaAbstractForm implements ItemListener {

    public enum DepositoAction {
        NUOVO, ASSOCIA, RIMUOVI
    }

    private class DepositoRadioButton extends JRadioButton {

        private static final long serialVersionUID = 205618148413145795L;

        private DepositoAction depositoAction;

        /**
         * Costruttore.
         *
         * @param depositoAction
         *            azione
         * @param text
         *            testo
         */
        public DepositoRadioButton(final DepositoAction depositoAction, final String text) {
            super(text);
            this.depositoAction = depositoAction;
            setHorizontalTextPosition(SwingConstants.LEFT);
            addItemListener(DepositoMezzoForm.this);
        }

        /**
         * @return the depositoAction
         */
        public DepositoAction getDepositoAction() {
            return depositoAction;
        }
    }

    private static final String FORM_ID = "depositorMezzoForm";

    private DepositoRadioButton nuovoDepositoRadioButton;
    private DepositoRadioButton associaDepositoRadioButton;
    private DepositoRadioButton rimuoviDepositoRadioButton;

    private DepositoAction depositoAction;

    /**
     * Costruttore.
     *
     * @param mezzoTrasporto
     *            mezzo di trasporto
     */
    public DepositoMezzoForm(final MezzoTrasporto mezzoTrasporto) {
        super(PanjeaFormModelHelper.createFormModel(mezzoTrasporto, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        MezzoTrasporto mezzoTrasporto = (MezzoTrasporto) getFormObject();

        nuovoDepositoRadioButton = new DepositoRadioButton(DepositoAction.NUOVO, "Crea un nuovo deposito");
        nuovoDepositoRadioButton
                .setEnabled(mezzoTrasporto.getDeposito() == null || mezzoTrasporto.getDeposito().isNew());
        associaDepositoRadioButton = new DepositoRadioButton(DepositoAction.ASSOCIA, "Associa un deposito esistente");
        rimuoviDepositoRadioButton = new DepositoRadioButton(DepositoAction.RIMUOVI, "Rimuovi il deposito");
        rimuoviDepositoRadioButton
                .setEnabled(mezzoTrasporto.getDeposito() != null && !mezzoTrasporto.getDeposito().isNew());
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(nuovoDepositoRadioButton);
        buttonGroup.add(associaDepositoRadioButton);
        buttonGroup.add(rimuoviDepositoRadioButton);

        builder.addComponent(nuovoDepositoRadioButton);
        builder.nextRow();

        builder.addComponent(associaDepositoRadioButton);
        SearchPanel searchPanelDeposito = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("deposito", new String[] { "codice", "descrizione" }), 3);
        searchPanelDeposito.getTextFields().get("codice").setColumns(10);
        searchPanelDeposito.getTextFields().get("descrizione").setColumns(26);
        builder.nextRow();

        builder.addComponent(rimuoviDepositoRadioButton);
        builder.nextRow();

        return builder.getPanel();
    }

    /**
     * @return the depositoAction
     */
    public DepositoAction getDepositoAction() {
        return depositoAction;
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            depositoAction = ((DepositoRadioButton) event.getSource()).getDepositoAction();
        }
    }
}
