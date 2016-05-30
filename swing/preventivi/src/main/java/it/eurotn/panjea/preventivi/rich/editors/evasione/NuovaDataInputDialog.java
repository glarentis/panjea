package it.eurotn.panjea.preventivi.rich.editors.evasione;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

import org.apache.commons.collections4.Closure;
import org.springframework.richclient.dialog.ConfirmationDialog;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;

public class NuovaDataInputDialog extends ConfirmationDialog {

    private JDateChooser dataConsegnaChooser;
    private JSpinner qtaSpinner;

    private Closure closure;

    private RigaEvasione rigaEvasione;

    /**
     * Costruttore.
     * 
     * @param finishClosure
     *            azione eseguita sulla conferma del dialog se i dati sono corretti
     */
    public NuovaDataInputDialog(final Closure finishClosure) {
        super("Nuova data di consegna", "Inserire la nuova data di consegna e la relativa quantità.");
        setPreferredSize(new Dimension(400, 150));
        closure = finishClosure;
        IDateEditor dateEditor = new PanjeaTextFieldDateEditor("dd/MM/yyyy", "##/##/####", '_');
        dataConsegnaChooser = new JDateChooser(dateEditor);
        qtaSpinner = new JSpinner();
    }

    @Override
    protected JComponent createDialogContentPane() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        JComponent messagePane = super.createDialogContentPane();
        rootPanel.add(messagePane, BorderLayout.NORTH);

        FormLayout layout = new FormLayout("right:pref,4dlu,fill:65dlu", "p,2dlu,p,2dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Data consegna", cc.xy(1, 1));
        builder.add(dataConsegnaChooser, cc.xy(3, 1));
        builder.addLabel("Quantità", cc.xy(1, 3));
        builder.add(qtaSpinner, cc.xy(3, 3));
        rootPanel.add(builder.getPanel(), BorderLayout.CENTER);

        return rootPanel;
    }

    @Override
    protected String getCancelCommandId() {
        return "cancelCommand";
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return new Object[] { getFinishCommand(), getCancelCommand() };
    }

    @Override
    protected String getFinishCommandId() {
        return "okCommand";
    }

    @Override
    protected void onConfirm() {
        try {
            qtaSpinner.commitEdit();
        } catch (ParseException e) {
            // non faccio niente perchè non dò la possibilità di inserire valori errati quindi l'eccezione non sarà mai
            // sollevata.
            logger.error("-->errore durante il parse della quantità", e);
        }
        closure.execute(new Object[] { rigaEvasione, dataConsegnaChooser.getDate(), qtaSpinner.getValue() });
    }

    /**
     * @param rigaEvasione
     *            the rigaEvasione to set
     */
    public void setRigaEvasione(RigaEvasione rigaEvasione) {
        this.rigaEvasione = rigaEvasione;

        dataConsegnaChooser.setDate(rigaEvasione.getDataConsegna());

        // se non c'è ancora una quantità evasione lascio scegliere come nuova quantità la qta di riga -1 perchè
        // altrimenti la riga originale rimarrebbe con qta 0
        double qta = rigaEvasione.getQuantitaRiga()
                - (rigaEvasione.getQuantitaEvasione().compareTo(0.0) == 0 ? 1.0 : rigaEvasione.getQuantitaEvasione());

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(qta, 0, qta, 1);
        qtaSpinner.setModel(spinnerModel);
        JFormattedTextField txt = ((JSpinner.NumberEditor) qtaSpinner.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
    }
}