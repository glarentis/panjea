package it.eurotn.panjea.rich.editors.email;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.RolloverTableUtils;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.util.MailDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;
import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;
import it.eurotn.panjea.rich.bd.IMailBD;
import it.eurotn.panjea.rich.components.JecSplitPane;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.command.ParametriRicercaCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author leonardo
 */
public class MailTablePage extends AbstractTablePageEditor<MailDTO>implements InitializingBean {

    /**
     * Command per lanciare la ricerca aree partite lanciando solo firePropertyChange(OBJECT_CHANGED,formObj).
     *
     * @author Leonardo
     */
    private class CercaMailCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         */
        public CercaMailCommand() {
            super("searchCommand");
            this.setSecurityControllerId(CERCA_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            parametriRicercaMail = (ParametriRicercaMail) formRicerca.getFormObject();
            parametriRicercaMail.setEffettuaRicerca(true);
            refreshData();
        }

    }

    private class ResetParametriRicercaMailCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public ResetParametriRicercaMailCommand() {
            super("resetParametriRicercaCommand");
            setSecurityControllerId(RESET_PARAMETRI_RICERCA_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            parametriRicercaMail = new ParametriRicercaMail();
            formRicerca.getNewFormObjectCommand().execute();
            refreshData();
        }
    }

    public static final String PAGE_ID = "mailTablePage";
    private static final String CERCA_COMMAND = PAGE_ID + ".cercaCommand";
    private static final String RESET_PARAMETRI_RICERCA_COMMAND = PAGE_ID + ".resetParametriRicercaCommand";
    private IMailBD mailBD = null;
    private ParametriRicercaMail parametriRicercaMail = null;

    private ParametriRicercaCommand parametriRicercaCommand;
    private ResetParametriRicercaMailCommand resetParametriRicercaMailCommand;
    private ParametriRicercaMailForm formRicerca;
    private CercaMailCommand cercaMailCommand;

    private MailContentComponent mailContentComponent;

    private JProgressBar importazioneMailProgressBar = new JProgressBar();
    private ImportaMailCommand importaMailCommand;
    private JPanel importazioneMailPanel;

    /**
     * Costruttore.
     */
    protected MailTablePage() {
        super(PAGE_ID, new MailTableModel());
        RolloverTableUtils.install(getTable().getTable());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ImportazioneMailMessageDelegate messageDelegate = RcpSupport
                .getBean(ImportazioneMailMessageDelegate.DELEGATE_ID);
        messageDelegate.addPropertyChangeListener(this);
    }

    @Override
    protected JComponent createControl() {
        JPanel tablePanel = getComponentFactory().createPanel(new BorderLayout());
        tablePanel.add(super.createControl(), BorderLayout.CENTER);
        tablePanel.add(createImportazioneMailPanel(), BorderLayout.SOUTH);

        mailContentComponent = new MailContentComponent();

        PanjeaComponentFactory componentFactory = (PanjeaComponentFactory) ((ComponentFactory) Application.services()
                .getService(ComponentFactory.class));
        JecSplitPane splitPanel = (JecSplitPane) componentFactory.createSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPanel.setLeftComponent(tablePanel);
        splitPanel.setRightComponent(mailContentComponent);

        getTable().addSelectionObserver(this);
        return splitPanel;
    }

    private JComponent createImportazioneMailPanel() {
        importazioneMailPanel = getComponentFactory().createPanel(new BorderLayout(10, 0));

        importaMailCommand = new ImportaMailCommand();
        importazioneMailPanel.add(importaMailCommand.createButton(), BorderLayout.WEST);
        importazioneMailProgressBar.setStringPainted(true);
        importazioneMailProgressBar.setVisible(false);
        importazioneMailPanel.add(importazioneMailProgressBar, BorderLayout.CENTER);

        GuiStandardUtils.attachBorder(importazioneMailPanel);
        return importazioneMailPanel;
    }

    /**
     * @return the cercaAreeTesoreriaCommand
     */
    public CercaMailCommand getCercaMailCommand() {
        if (cercaMailCommand == null) {
            cercaMailCommand = new CercaMailCommand();
        }

        return cercaMailCommand;
    }

    @Override
    public JComponent getHeaderControl() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 5));
        formRicerca = new ParametriRicercaMailForm();
        rootPanel.add(formRicerca.getControl(), BorderLayout.CENTER);

        JECCommandGroup commandGroup = new JECCommandGroup();
        commandGroup.add(getParametriRicercaCommand());
        commandGroup.add(getResetRicercaMailCommand());
        commandGroup.add(getCercaMailCommand());
        rootPanel.add(commandGroup.createToolBar(), BorderLayout.NORTH);

        rootPanel.add(new JLabel("La ricerca è limitata ai primi 500 risultati"), BorderLayout.SOUTH);

        GuiStandardUtils.attachBorder(rootPanel);
        return rootPanel;
    }

    /**
     *
     * @return ParametriRicercaCommand
     */
    protected ParametriRicercaCommand getParametriRicercaCommand() {
        if (parametriRicercaCommand == null) {
            parametriRicercaCommand = new ParametriRicercaCommand(formRicerca.getFormModel(), this);
        }
        return parametriRicercaCommand;
    }

    /**
     * @return the ResetParametriRicercaMailCommand
     */
    public ResetParametriRicercaMailCommand getResetRicercaMailCommand() {
        if (resetParametriRicercaMailCommand == null) {
            resetParametriRicercaMailCommand = new ResetParametriRicercaMailCommand();
        }
        return resetParametriRicercaMailCommand;
    }

    @Override
    public List<MailDTO> loadTableData() {
        List<MailDTO> mail = Collections.emptyList();
        if (parametriRicercaMail.isEffettuaRicerca()) {
            mail = mailBD.caricaMails(parametriRicercaMail);
        }
        return mail;
    }

    @Override
    public void onPostPageOpen() {
        // non faccio nulla
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if (ImportazioneMailMessageDelegate.MESSAGE_CHANGE.equals(event.getPropertyName())) {
            final AbstractStateDescriptor stateDescriptor = (AbstractStateDescriptor) event.getNewValue();

            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        importaMailCommand.setEnabled(
                                stateDescriptor.getCurrentJobOperation() >= stateDescriptor.getTotalJobOperation());

                        importazioneMailProgressBar.setVisible(
                                stateDescriptor.getCurrentJobOperation() < stateDescriptor.getTotalJobOperation());
                        importazioneMailProgressBar.setMaximum(stateDescriptor.getTotalJobOperation());
                        importazioneMailProgressBar.setValue(stateDescriptor.getCurrentJobOperation());
                        importazioneMailProgressBar
                                .setString("Importazione mail " + stateDescriptor.getCurrentJobOperation() + " di "
                                        + stateDescriptor.getTotalJobOperation());

                        importazioneMailPanel.setVisible(
                                stateDescriptor.getCurrentJobOperation() < stateDescriptor.getTotalJobOperation());
                    } catch (Exception e) {
                        // try catch introdotto per un errore che non si riesce a riprodurre
                    }
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                task.run();
            } else {
                SwingUtilities.invokeLater(task);
            }
        } else {
            super.propertyChange(event);
        }
    }

    @Override
    public List<MailDTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaMail) {
            this.parametriRicercaMail = (ParametriRicercaMail) object;
        } else if (object instanceof Entita) {
            this.parametriRicercaMail = new ParametriRicercaMail();
            Entita entita = (Entita) object;
            parametriRicercaMail.setEntita(entita.getEntitaLite());
            parametriRicercaMail.setEffettuaRicerca(true);
            formRicerca.setFormObject(parametriRicercaMail);
            formRicerca.setEntitaReadOnly(true);
            getParametriRicercaCommand().setVisible(false);
            getResetRicercaMailCommand().setVisible(false);
        } else {
            this.parametriRicercaMail = new ParametriRicercaMail();
        }

        importazioneMailPanel.setVisible(mailBD.caricaNumeroMailDaImportare() > 0);
    }

    /**
     * @param mailBD
     *            the mailBD to set
     */
    public void setMailBD(IMailBD mailBD) {
        this.mailBD = mailBD;
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);

        MailDTO mail = (MailDTO) obj;
        mailContentComponent.load(mail != null ? mail.getWsDocument().getId() : null);
    }

}
