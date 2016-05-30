package it.eurotn.panjea.rich.editors.update;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

public class DelayUpdateChooserDialog extends ConfirmationDialog {

    public static final String DIALOG_ID = "delayUpdateChooserDialog";
    public static final String DIALOG_TITLE = "delayUpdateChooserDialog.title";
    public static final String DIALOG_MESSAGE = "delayUpdateChooserDialog.message";

    public static final String RADIO_30_SEC = DIALOG_ID + ".radio30sec";
    public static final String RADIO_1_MIN = DIALOG_ID + ".radio1min";
    public static final String RADIO_2_MIN = DIALOG_ID + ".radio2min";
    public static final String RADIO_5_MIN = DIALOG_ID + ".radio5min";
    public static final String RADIO_10_MIN = DIALOG_ID + ".radio10min";
    public static final String RADIO_CUSTOM = DIALOG_ID + ".radioCustomDelay";

    private long delay = 60;

    /**
     * Costruttore.
     *
     */
    public DelayUpdateChooserDialog() {
        super(RcpSupport.getMessage(DIALOG_TITLE), RcpSupport.getMessage(DIALOG_MESSAGE));
        setPreferredSize(new Dimension(400, 200));
    }

    /**
     * Crea i componenti per gestire il ritardo custom dell'aggiormanto.
     *
     * @return componenti creati
     */
    private JComponent createCustomDelayComponent() {

        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        SpinnerModel oreSpinnerModel = new SpinnerNumberModel(0, 0, 12, 1);
        final JSpinner oreSpinner = new JSpinner(oreSpinnerModel);

        SpinnerModel minutiSpinnerModel = new SpinnerNumberModel(0, 0, 59, 1);
        final JSpinner minutiSpinner = new JSpinner(minutiSpinnerModel);

        ChangeListener spinnerChangeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Integer ore = (Integer) oreSpinner.getValue();
                Integer minuti = (Integer) minutiSpinner.getValue();

                delay = ore * 60 * 60 + minuti * 60;
            }
        };
        oreSpinner.addChangeListener(spinnerChangeListener);
        minutiSpinner.addChangeListener(spinnerChangeListener);

        panel.add(new JLabel("ore"));
        panel.add(new JSpinner(oreSpinnerModel));
        panel.add(new JLabel("minuti"));
        panel.add(new JSpinner(minutiSpinnerModel));

        return panel;
    }

    @Override
    protected JComponent createDialogContentPane() {

        final JComponent customDelayComponent = createCustomDelayComponent();

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 20));
        rootPanel.add(super.createDialogContentPane(), BorderLayout.NORTH);

        JRadioButton radio30sec = new JRadioButton(RcpSupport.getMessage(RADIO_30_SEC));
        radio30sec.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delay = 30;
                customDelayComponent.setVisible(false);
            }
        });

        JRadioButton radio1minuto = new JRadioButton(RcpSupport.getMessage(RADIO_1_MIN));
        radio1minuto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delay = 60;
                customDelayComponent.setVisible(false);
            }
        });

        JRadioButton radio2minuti = new JRadioButton(RcpSupport.getMessage(RADIO_2_MIN));
        radio2minuti.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delay = 120;
                customDelayComponent.setVisible(false);
            }
        });

        JRadioButton radio5minuti = new JRadioButton(RcpSupport.getMessage(RADIO_5_MIN));
        radio5minuti.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delay = 300;
                customDelayComponent.setVisible(false);
            }
        });

        JRadioButton radio10minuti = new JRadioButton(RcpSupport.getMessage(RADIO_10_MIN));
        radio10minuti.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delay = 600;
                customDelayComponent.setVisible(false);
            }
        });

        JRadioButton radioCustomDealy = new JRadioButton(RcpSupport.getMessage(RADIO_CUSTOM));
        radioCustomDealy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delay = 0;
                customDelayComponent.setVisible(true);
            }
        });
        radioCustomDealy.setName("customDelayRadioButton");
        radioCustomDealy.doClick();
        delay = 0;
        customDelayComponent.setVisible(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radio30sec);
        buttonGroup.add(radio1minuto);
        buttonGroup.add(radio2minuti);
        buttonGroup.add(radio5minuti);
        buttonGroup.add(radioCustomDealy);

        JPanel delayPanel = getComponentFactory().createPanel(new org.jdesktop.swingx.VerticalLayout(4));
        delayPanel.add(radio30sec);
        delayPanel.add(radio1minuto);
        delayPanel.add(radio2minuti);
        delayPanel.add(radio5minuti);

        JPanel customPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        customPanel.setBorder(BorderFactory.createEmptyBorder());
        customPanel.add(radioCustomDealy);
        customPanel.add(customDelayComponent);
        delayPanel.add(customPanel);
        rootPanel.add(delayPanel, BorderLayout.CENTER);

        customDelayComponent.setVisible(false);

        return rootPanel;
    }

    @Override
    protected String getCancelCommandId() {
        return "cancelCommand";
    }

    /**
     * @return the delay
     */
    public long getDelay() {
        return delay;
    }

    @Override
    protected String getFinishCommandId() {
        return "okCommand";
    }

    @Override
    protected void onCancel() {
        delay = -1;
        super.onCancel();
    }

    @Override
    protected void onConfirm() {

    }

}
