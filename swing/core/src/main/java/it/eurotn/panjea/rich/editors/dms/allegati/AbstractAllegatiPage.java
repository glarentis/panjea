package it.eurotn.panjea.rich.editors.dms.allegati;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.rich.bd.DmsBD;
import it.eurotn.panjea.rich.bd.IDmsBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public abstract class AbstractAllegatiPage extends AbstractDialogPage implements IPageLifecycleAdvisor {
    private static final Logger LOGGER = Logger.getLogger(AbstractAllegatiPage.class);

    protected IDmsBD dmsBD;
    private List<WSDocument> allegati;

    private JPanel rootPanel;
    private JPanel allegatiPanel;
    private AllegatiImagePreviewList allegatiPreviewList;
    private PubblicaAllegatoCommand pubblicaAllegatiCommand;

    private AllegatoDMS claseAllegato;

    protected IDefProperty currentFormObject;
    protected IDefProperty loadedFormObject;

    private AllegatiListTransferHandler transferHandler;

    private JLabel titleLabel;

    private boolean formObjectChanged = false;

    /**
     * Costruttore.
     */
    public AbstractAllegatiPage() {
        super("allegatiPage");
        dmsBD = RcpSupport.getBean(DmsBD.BEAN_ID);
        transferHandler = new AllegatiListTransferHandler(dmsBD, this);
    }

    private void caricaDati() {
        if (isVisible() && allegati == null && allegatiPreviewList != null && claseAllegato != null) {

            loadSnapshot();
        }
    }

    protected abstract AllegatoDMS createAttributoFromFormObject(Object object);

    @Override
    protected JComponent createControl() {
        LOGGER.debug("--> Enter createControl");
        GridBagLayout gridbag = new GridBagLayout();
        rootPanel = getComponentFactory().createPanel(gridbag);
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.0;
        constraint.weighty = 0.0;
        constraint.gridy = 0;
        rootPanel.add(createToolbarPanel(), constraint);

        allegatiPanel = getComponentFactory().createPanel(new BorderLayout());
        allegatiPanel.setBackground(Color.GRAY);
        titleLabel = new JLabel("ALLEGATI");

        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 0));
        allegatiPanel.add(titleLabel, BorderLayout.NORTH);
        allegatiPreviewList = new AllegatiImagePreviewList();
        allegatiPreviewList.setTransferHandler(transferHandler);

        allegatiPanel.add(new JScrollPane(allegatiPreviewList), BorderLayout.CENTER);

        caricaDati();

        constraint.weighty = 2.0;
        constraint.weightx = 2.0;
        constraint.gridy = 1;
        gridbag.setConstraints(allegatiPanel, constraint);
        rootPanel.add(allegatiPanel);

        constraint.weighty = 1.0;
        constraint.weightx = 1.0;
        constraint.gridy = 2;

        LOGGER.debug("--> Exit createControl");
        return rootPanel;
    }

    private JComponent createToolbarPanel() {
        pubblicaAllegatiCommand = new PubblicaAllegatoCommand(dmsBD);
        pubblicaAllegatiCommand.addCommandInterceptor(new ActionCommandInterceptor() {

            @Override
            public void postExecution(ActionCommand command) {
                loadSnapshot();
            }

            @Override
            public boolean preExecution(ActionCommand command) {
                return true;
            }
        });

        AbstractButton pubblicaButton = pubblicaAllegatiCommand.createButton();
        pubblicaButton.setFocusable(false);

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(pubblicaButton, BorderLayout.WEST);

        return panel;
    }

    @Override
    public void dispose() {
        LOGGER.debug("--> Enter dispose");
        transferHandler.dispose();
        transferHandler = null;
    }

    protected String getAllegatiDefaultPath() {
        return dmsBD.caricaDmsSettings().getFolder(currentFormObject);
    }

    /**
     * @return codice azienda dell'utente loggato
     */
    protected String getCodiceAzienda() {
        return PanjeaSwingUtil.getUtenteCorrente().getCodiceAzienda();
    }

    /**
     * @return Returns the dmsBD.
     */
    public IDmsBD getDmsBD() {
        return dmsBD;
    }

    protected abstract String getFolder(Object object);

    @Override
    public void loadData() {
        caricaDati();
    }

    private void loadSnapshot() {
        formObjectChanged = !Objects.equals(currentFormObject, loadedFormObject);
        if (formObjectChanged && claseAllegato != null) {
            pubblicaAllegatiCommand.setEnabled(true);
            allegatiPreviewList.setEnabled(true);
            allegatiPreviewList.setTransferHandler(transferHandler);
            titleLabel.setText("ALLEGATI");

            try {
                allegati = dmsBD.getAllegati(claseAllegato);
                loadedFormObject = currentFormObject;
            } catch (DMSLoginException e) {
                LOGGER.trace("Login exception per il dms", e);
                pubblicaAllegatiCommand.setEnabled(false);
                allegatiPreviewList.setEnabled(false);
                allegatiPreviewList.setTransferHandler(null);
                titleLabel.setText("Gestione documentale non disponibile.");
                allegati = new ArrayList<>();
            }
            allegatiPreviewList.setAllegati(allegati);
        }
    }

    @Override
    public void onPostPageOpen() {
        LOGGER.debug("--> Enter onPostPageOpen");
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        LOGGER.debug("--> Enter postSetFormObject");
    }

    @Override
    public void preSetFormObject(Object object) {
        LOGGER.debug("--> Enter preSetFormObject");
    }

    @Override
    public void refreshData() {
        loadSnapshot();
    }

    @Override
    public void restoreState(Settings arg0) {
        LOGGER.debug("--> Enter restoreState");
    }

    @Override
    public void saveState(Settings arg0) {
        LOGGER.debug("--> Enter saveState");
    }

    /**
     * @param dmsBD
     *            The dmsBD to set.
     */
    public void setDmsBD(IDmsBD dmsBD) {
        this.dmsBD = dmsBD;
    }

    @Override
    public void setFormObject(Object object) {
        currentFormObject = (IDefProperty) object;
        allegati = null;
        transferHandler.setAttributiAllegato(null);
        if (!currentFormObject.isNew()) {
            claseAllegato = createAttributoFromFormObject(object);
            transferHandler.setAttributiAllegato(claseAllegato);
            transferHandler.setDefaultPath(getFolder(object));
            if (pubblicaAllegatiCommand != null) {
                pubblicaAllegatiCommand.setAttributi(claseAllegato);
                pubblicaAllegatiCommand.setDefaultPath(getFolder(object));
            }
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        LOGGER.debug("--> Enter setReadOnly");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        caricaDati();
    }

}
