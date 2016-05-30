/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.carrello;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.EffettiLayoutManager.Mode;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.rich.command.JECCommandGroup;

/**
 * @author Leonardo
 */
public class CarrelloEffettiPage extends AbstractDialogPage {

    public class CancellaEffettoCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "rimuoviEffettoCommand";

        /**
         * Costruttore.
         */
        public CancellaEffettoCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            setVisible(false);
        }

        @Override
        protected void doExecuteCommand() {
            if (currentTableComponent != null) {
                currentTableComponent.removeSelected();
            }
        }
    }

    public class GeneraCommand extends ActionCommand {

        public static final String COMMAND_ID = "generaCommand";
        public static final String PARAM_GENERATED = "generated";

        /**
         * Costruttore.
         */
        public GeneraCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (currentTableComponent != null) {

                final Boolean generated = currentTableComponent.generaAreeTesoreria(tesoreriaBD);
                this.addParameter(PARAM_GENERATED, generated);

                if (!generated) {
                    final MessageDialog dialog = new MessageDialog("ATTENZIONE",
                            "Impossibile generare i documenti. Inserire tutti i dati richiesti.");
                    dialog.showDialog();
                }
            }
        }

        @Override
        public Object getParameter(Object key) {
            return super.getParameter(key);
        }

    }

    public class SvuotaCarrelloCommand extends ApplicationWindowAwareCommand {

        /**
         * Costruttore.
         */
        public SvuotaCarrelloCommand() {
            super("svuotaCarrelloEffettiCommand");
            RcpSupport.configure(this);
            setVisible(false);
        }

        @Override
        protected void doExecuteCommand() {
            if (currentTableComponent != null) {
                currentTableComponent.removeAll();
            }
        }
    }

    public static final String ACCREDITI_PANEL = "accreditiPanel";

    public static final String ANTICIPI_PANEL = "anticipiPanel";

    public static final String INSOLUTI_PANEL = "InsolutiPanel";

    private static final String PAGE_ID = "risultatiRicercaEffettiPage";
    private AbstractCarrelloTableComponent accreditiTableComponent;
    private AbstractCarrelloTableComponent anticipiTableComponent;

    private CancellaEffettoCommand cancellaEffettoCommand;
    private final JPanel cardPanel = getComponentFactory().createPanel(new CardLayout());
    private AbstractCarrelloTableComponent currentTableComponent;
    private GeneraCommand generaCommand;

    private AbstractCarrelloTableComponent insolutiTableComponent;
    private Mode mode;
    private SvuotaCarrelloCommand svuotaCarrelloCommand;

    private ITesoreriaBD tesoreriaBD;

    /**
     * Costruttore.
     */
    public CarrelloEffettiPage() {
        super(PAGE_ID);
    }

    /**
     * Aggiunge una lista di situazioni effetto al carrello.
     * 
     * @param situazioneEffetti
     *            lista da aggiungere
     */
    public void addSituazioneEffetti(List<SituazioneEffetto> situazioneEffetti) {
        if (currentTableComponent != null) {
            currentTableComponent.addSituazioneEffetti(situazioneEffetti, tesoreriaBD);
        }
    }

    @Override
    protected JComponent createControl() {

        final JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        accreditiTableComponent = new AccreditiTableComponent();
        cardPanel.add(accreditiTableComponent.getControl(), ACCREDITI_PANEL);

        insolutiTableComponent = new InsolutiTableComponent();
        cardPanel.add(insolutiTableComponent.getControl(), INSOLUTI_PANEL);

        anticipiTableComponent = new AnticipiTableComponent();
        cardPanel.add(anticipiTableComponent.getControl(), ANTICIPI_PANEL);

        cardPanel.setPreferredSize(new Dimension(200, 100));

        rootPanel.add(cardPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
        final JECCommandGroup commandGroup = new JECCommandGroup();
        commandGroup.add(getSvuotaCarrelloCommand());
        commandGroup.add(getCancellaEffettoCommand());
        commandGroup.add(getGeneraCommand());
        buttonPanel.add(commandGroup.createToolBar(), BorderLayout.EAST);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        return rootPanel;
    }

    /**
     * @return the cancellaEffettoCommand
     */
    public CancellaEffettoCommand getCancellaEffettoCommand() {
        if (cancellaEffettoCommand == null) {
            cancellaEffettoCommand = new CancellaEffettoCommand();
        }
        return cancellaEffettoCommand;
    }

    /**
     * @return the generaCommand
     */
    public ActionCommand getGeneraCommand() {
        if (generaCommand == null) {
            generaCommand = new GeneraCommand();
        }
        return generaCommand;
    }

    /**
     * Restituisce la lista di tutte le {@link SituazioneEffetto} del carrello.
     * 
     * @return lista di {@link SituazioneEffetto}
     */
    public List<SituazioneEffetto> getSituazioneEffetti() {
        List<SituazioneEffetto> list = new ArrayList<>();
        if (currentTableComponent != null) {
            list = currentTableComponent.getSituazioniEffetti();
        }
        return list;
    }

    /**
     * @return the svuotaCarrelloCommand
     */
    public SvuotaCarrelloCommand getSvuotaCarrelloCommand() {
        if (svuotaCarrelloCommand == null) {
            svuotaCarrelloCommand = new SvuotaCarrelloCommand();
        }
        return svuotaCarrelloCommand;
    }

    /**
     * @param mode
     *            the mode to set
     */
    public void setMode(Mode mode) {
        this.mode = mode;

        this.setVisible(false);

        currentTableComponent = null;

        getSvuotaCarrelloCommand().setVisible(false);
        getCancellaEffettoCommand().setVisible(false);

        final CardLayout cl = (CardLayout) (cardPanel.getLayout());
        switch (this.mode) {
        case GENERALE:
            break;
        case ACCREDITI:
            accreditiTableComponent.removeAll();
            this.setVisible(true);
            cl.show(cardPanel, ACCREDITI_PANEL);
            currentTableComponent = accreditiTableComponent;
            getSvuotaCarrelloCommand().setVisible(true);
            getCancellaEffettoCommand().setVisible(true);
            break;
        case INSOLUTI:
            insolutiTableComponent.removeAll();
            this.setVisible(true);
            cl.show(cardPanel, INSOLUTI_PANEL);
            currentTableComponent = insolutiTableComponent;
            getSvuotaCarrelloCommand().setVisible(true);
            getCancellaEffettoCommand().setVisible(true);
            break;
        case ANTICIPI:
            anticipiTableComponent.removeAll();
            this.setVisible(true);
            cl.show(cardPanel, ANTICIPI_PANEL);
            currentTableComponent = anticipiTableComponent;
            getSvuotaCarrelloCommand().setVisible(true);
            getCancellaEffettoCommand().setVisible(true);
            break;
        default:
            throw new UnsupportedOperationException("Mode non supportato");
        }
    }

    /**
     * @param tesoreriaBD
     *            the tesoreriaBD to set
     */
    public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
        this.tesoreriaBD = tesoreriaBD;
    }
}
