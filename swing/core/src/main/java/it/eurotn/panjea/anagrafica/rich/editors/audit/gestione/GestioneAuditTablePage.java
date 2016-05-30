/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.audit.gestione;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.rich.bd.IAuditBD;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * @author fattazzo
 *
 */
public class GestioneAuditTablePage extends AbstractDialogPage implements IPageLifecycleAdvisor, InitializingBean {

    private class AnnoMeseChangeListener implements ItemListener, ChangeListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateInfoLabel();
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            updateInfoLabel();
        }

    }

    private class DeleteRevsCommand extends ApplicationWindowAwareCommand {

        /**
         * Costruttore.
         */
        public DeleteRevsCommand() {
            super("deleteRevsCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            setEnabled(false);
            new SwingWorker<Boolean, Void>() {

                @Override
                protected Boolean doInBackground() throws Exception {
                    auditBD.cancellaAuditPrecedente(getSelectedDate());
                    return true;
                }

                @Override
                protected void done() {
                    try {
                        get();
                    } catch (Exception e) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("--> Errore durante la cancellazione dell'audit." + e.getMessage());
                        }
                        // non faccio niente
                    } finally {
                        numeroRevPresenti = auditBD.caricaNumeroRevInf();
                        updateInfoLabel();
                        setEnabled(true);
                    }
                }
            }.execute();
        }

    }

    public static final String INFO_LABEL_TEXT = "<html>Selezionare l'ultimo periodo valido da mantenere.<br><br>Audit presenti <b>$revPresenti$</b><br><br><b>Attenzione</b><br>Procedendo con la cancellazione verranno mantenuti tutti i dati di audit successivi alla data <b>$data$</b></html>";

    private final DateFormat dateFormat;
    private final DecimalFormat decimalFormat;

    private IAuditBD auditBD;

    private JLabel infoLabel;

    private DeleteRevsCommand deleteRevsCommand;

    private JSpinner annoSpinner;

    private JComboBox<String> meseComboBox;

    private AnnoMeseChangeListener annoMeseChangeListener;

    private Integer numeroRevPresenti;

    {
        annoMeseChangeListener = new AnnoMeseChangeListener();
        numeroRevPresenti = 0;

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        decimalFormat = new DecimalFormat("#,##0");
    }

    /**
     * Costruttore.
     * 
     * @param pageId
     */
    protected GestioneAuditTablePage() {
        super("gestioneAuditTablePage");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        numeroRevPresenti = auditBD.caricaNumeroRevInf();
    }

    @Override
    protected JComponent createControl() {
        JPanel rootPanel = getComponentFactory().createPanel(new VerticalLayout(10));

        initializeAnnoControl();
        initializeMeseControl();

        JPanel panel = getComponentFactory().createPanel(new HorizontalLayout(5));
        panel.add(getComponentFactory().createLabel("anno"));
        panel.add(annoSpinner);
        panel.add(getComponentFactory().createLabel("mese"));
        panel.add(meseComboBox);
        rootPanel.add(panel);

        rootPanel.add(createRightComponent());

        GuiStandardUtils.attachBorder(rootPanel);

        return rootPanel;
    }

    /**
     * Crea il ocmponente che verrà visualizzato a destra della tabella.
     * 
     * @return componente
     */
    private JComponent createRightComponent() {
        JPanel panel = getComponentFactory().createPanel(new VerticalLayout(10));

        infoLabel = new JLabel();
        updateInfoLabel();
        panel.add(infoLabel);

        JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
        buttonPanel.add(getDeleteRevsCommand().createButton(), BorderLayout.WEST);
        panel.add(buttonPanel);

        return panel;
    }

    @Override
    public void dispose() {
    }

    /**
     * @return the deleteRevsCommand
     */
    public DeleteRevsCommand getDeleteRevsCommand() {
        if (deleteRevsCommand == null) {
            deleteRevsCommand = new DeleteRevsCommand();
        }

        return deleteRevsCommand;
    }

    /**
     * @return data selezionata in base ai parametri (anno e mese) scelti.
     */
    private Date getSelectedDate() {

        Integer anno = (Integer) annoSpinner.getValue();
        Integer mese = meseComboBox.getSelectedIndex();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, mese);
        calendar.set(Calendar.YEAR, anno);

        return calendar.getTime();
    }

    /**
     * Inizializza i controlli per la selezione dell'anno.
     */
    private void initializeAnnoControl() {
        Calendar calendar = Calendar.getInstance();

        annoSpinner = new JSpinner(
                new SpinnerNumberModel(calendar.get(Calendar.YEAR), 1900, calendar.get(Calendar.YEAR), 1));

        annoSpinner.getModel().addChangeListener(annoMeseChangeListener);
    }

    /**
     * Inizializza i controlli per la selezione dell'anno.
     */
    private void initializeMeseControl() {
        Calendar calendar = Calendar.getInstance();

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.getDefault());
        String[] months = dateFormatSymbols.getMonths();
        String[] mesi = new String[12];
        System.arraycopy(months, 0, mesi, 0, 12);

        meseComboBox = new JComboBox<String>(mesi);
        meseComboBox.setSelectedIndex(calendar.get(Calendar.MONTH));

        meseComboBox.addItemListener(annoMeseChangeListener);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    /**
     * @param auditBD
     *            the auditBD to set
     */
    public void setAuditBD(IAuditBD auditBD) {
        this.auditBD = auditBD;
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    /**
     * Aggiorna la label con la data ottenuta dai parametri selezionati.
     */
    private void updateInfoLabel() {
        String text = INFO_LABEL_TEXT;
        text = text.replace("$data$", dateFormat.format(getSelectedDate())).replace("$revPresenti$",
                decimalFormat.format(numeroRevPresenti));
        infoLabel.setText(text);
    }

}
