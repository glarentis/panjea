package it.eurotn.panjea.rateirisconti.rich.editors.elenco;

import java.awt.FlowLayout;
import java.util.Calendar;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

import org.springframework.richclient.util.GuiStandardUtils;

import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoAnno;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;

public class RateiRiscontiHeaderComponent extends JPanel implements ChangeListener {

    private static final long serialVersionUID = -1090674419928362631L;

    private JSpinner annoSpinner;

    private JCheckBox rateiCheckBox;
    private JCheckBox riscontiCheckBox;

    private ElencoRiscontiTablePage elencoRiscontiTablePage;

    /**
     * Costruttore.
     *
     * @param elencoRiscontiTablePage
     *            pagina dell'elenco ratei/risconti
     */
    public RateiRiscontiHeaderComponent(final ElencoRiscontiTablePage elencoRiscontiTablePage) {
        super(new FlowLayout(FlowLayout.LEFT, 10, 0));
        this.elencoRiscontiTablePage = elencoRiscontiTablePage;
        GuiStandardUtils.attachBorder(this);

        init();
    }

    /**
     * @return anno selezionato
     */
    public Integer getAnno() {
        return (Integer) annoSpinner.getValue();
    }

    /**
     * @return tipo rateo/risconto selezionato
     */
    public Class<? extends RigaRiscontoAnno> getClasseRiga() {
        boolean rateiCheck = rateiCheckBox.isSelected();
        boolean riscontiCheck = riscontiCheckBox.isSelected();

        if (rateiCheck == riscontiCheck) {
            return null;
        }

        if (rateiCheck) {
            return RigaRateoAnno.class;
        }

        if (riscontiCheck) {
            return RigaRiscontoAnno.class;
        }

        return null;
    }

    private void init() {

        Calendar calendar = Calendar.getInstance();
        int annoCorrente = calendar.get(Calendar.YEAR);
        annoSpinner = new JSpinner(new SpinnerNumberModel(annoCorrente, annoCorrente - 20, annoCorrente + 20, 1));
        JComponent comp = annoSpinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        annoSpinner.addChangeListener(this);

        add(new JLabel("Anno"));
        add(annoSpinner);

        rateiCheckBox = new JCheckBox("Ratei", true);
        rateiCheckBox.addChangeListener(this);
        add(rateiCheckBox);

        riscontiCheckBox = new JCheckBox("Risconti", true);
        riscontiCheckBox.addChangeListener(this);
        add(riscontiCheckBox);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        elencoRiscontiTablePage.refreshData();
    }
}
