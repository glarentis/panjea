package it.eurotn.panjea.ordini.rich.editors.righeinserimento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableUtils;

import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.righeinserimento.parametri.ParametriRigheInserimentoComponent;
import it.eurotn.panjea.ordini.rich.editors.righeinserimento.table.RigheInserimentoTable;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento.TipoRicerca;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class RigheInserimentoPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class AnnullaRigheInserimentoCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public AnnullaRigheInserimentoCommand() {
            super("annullaRigheInserimentoCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            RigheInserimentoPage.this.firePropertyChange(RigheInserimentoPage.ATTIVAZIONE_RIGHE_INSERIMENTO, true,
                    false);
        }

    }

    private class GeneraRigheOrdineInserceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            if (((GeneraRigheOrdineCommand) command).isRigheGenerate()) {
                RigheInserimentoPage.this.firePropertyChange(RigheInserimentoPage.ATTIVAZIONE_RIGHE_INSERIMENTO, true,
                        false);
                // ricarico l'area ordine perchè potrebbe essere cambiato lo stato a causa
                // dell'inserimento delle nuove
                // righe
                areaOrdineFullDTO = ordiniDocumentoBD.caricaAreaOrdineFullDTO(areaOrdineFullDTO.getAreaOrdine());
                RigheInserimentoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                        areaOrdineFullDTO);
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {

            if (righeInserimentoTable != null) {
                command.addParameter(GeneraRigheOrdineCommand.PARAM_RIGHE_INSERIMENTO, righeInserimentoTable.getRows());
                command.addParameter(GeneraRigheOrdineCommand.PARAM_AREA_ORDINE, areaOrdineFullDTO.getAreaOrdine());
            }

            return righeInserimentoTable != null;
        }

    }

    public static final String PAGE_ID = "righeInserimentoPage";

    public static final String ATTIVAZIONE_RIGHE_INSERIMENTO = "attivazioneRigheInserimento";

    private AreaOrdineFullDTO areaOrdineFullDTO;

    private AnnullaRigheInserimentoCommand annullaRigheInserimentoCommand;
    private GeneraRigheOrdineCommand generaRigheOrdineCommand;

    private RigheInserimentoController righeInserimentoController = new RigheInserimentoController();

    private ParametriRigheInserimentoComponent parametriComponent;

    private RigheInserimentoTable righeInserimentoTable;
    private JLabel caricamentoLabel = new JLabel("CARICAMENTO IN CORSO...", SwingConstants.CENTER);

    private IOrdiniDocumentoBD ordiniDocumentoBD;

    private JPanel panel;

    private List<RigaOrdineInserimento> righeOrdine = new ArrayList<>();

    /**
     * Costruttore.
     */
    public RigheInserimentoPage() {
        super(PAGE_ID);
        righeInserimentoController.setRigheInserimentoPage(this);
        ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
    }

    private RigheOrdineInserimento aggiornaValoriRighe(RigheOrdineInserimento righeInserimento) {

        // copio le righe dell'ordine perchè dalle righe inserimento caricate vado a scalarle mano a
        // mano
        List<RigaOrdineInserimento> righeCopy = new ArrayList<>();
        righeCopy.addAll(righeOrdine);

        for (RigaOrdineInserimento rigaIns : righeInserimento.getRighe()) {
            Iterator<RigaOrdineInserimento> iterator = righeCopy.iterator();
            while (iterator.hasNext()) {
                RigaOrdineInserimento rigaOrdine = iterator.next();
                if (rigaOrdine.getArticolo().equals(rigaIns.getArticolo())) {
                    rigaIns.setPresenteInOrdine(true);
                    // rigaIns.setQtaInserimento(rigaOrdine.getQta());
                    // for (Entry<String, AttributoRigaArticolo> attributiOrdine :
                    // rigaOrdine.getAttributi().entrySet())
                    // {
                    // AttributoRigaArticolo attrIns =
                    // rigaIns.getAttributiInserimento().get(attributiOrdine.getKey());
                    // if (attrIns != null) {
                    // attrIns.setValore(attributiOrdine.getValue().getValore());
                    // }
                    // }
                    iterator.remove();
                }
            }
        }

        return righeInserimento;
    }

    @Override
    protected JComponent createControl() {
        panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(createParametriControl(), BorderLayout.WEST);
        panel.add(createToolbar(), BorderLayout.SOUTH);
        return panel;
    }

    private JComponent createParametriControl() {
        parametriComponent = new ParametriRigheInserimentoComponent();
        parametriComponent.setRigheInserimentoController(righeInserimentoController);
        return parametriComponent;
    }

    private JComponent createToolbar() {
        JPanel toolbarPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        toolbarPanel.add(getGeneraRigheOrdineCommand().createButton());
        toolbarPanel.add(getAnnullaRigheInserimentoCommand().createButton());

        GuiStandardUtils.attachBorder(toolbarPanel);
        return toolbarPanel;
    }

    @Override
    public void dispose() {
    }

    /**
     * @return the annullaRigheInserimentoCommand
     */
    private AnnullaRigheInserimentoCommand getAnnullaRigheInserimentoCommand() {
        if (annullaRigheInserimentoCommand == null) {
            annullaRigheInserimentoCommand = new AnnullaRigheInserimentoCommand();
        }

        return annullaRigheInserimentoCommand;
    }

    /**
     * @return the generaRigheOrdineCommand
     */
    private GeneraRigheOrdineCommand getGeneraRigheOrdineCommand() {
        if (generaRigheOrdineCommand == null) {
            generaRigheOrdineCommand = new GeneraRigheOrdineCommand();
            generaRigheOrdineCommand.addCommandInterceptor(new GeneraRigheOrdineInserceptor());
        }

        return generaRigheOrdineCommand;
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

    @Override
    public void setFormObject(Object object) {
        this.areaOrdineFullDTO = (AreaOrdineFullDTO) object;

        parametriComponent.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());

        setVisible(this.areaOrdineFullDTO.isInserimentoRigheMassivo());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (righeInserimentoTable != null) {
            panel.remove(righeInserimentoTable.getComponent());
            righeInserimentoTable.dispose();
            righeInserimentoTable = null;
        }

        // quando vado a visualizzare la pagina per inserire le righe mi carico quelle dell'ordine
        // che mi servono poi
        // per indicare quali articoli sono già presenti
        if (areaOrdineFullDTO != null) {
            ParametriRigheOrdineInserimento parametri = new ParametriRigheOrdineInserimento();
            parametri.setTipoRicerca(TipoRicerca.ORDINE);
            parametri.setIdAreaOrdine(areaOrdineFullDTO.getId());
            righeOrdine = ordiniDocumentoBD.caricaRigheOrdineInserimento(parametri).getRighe();
        }
    }

    /**
     * Aggiorna la pagina con i nuovi parametri di inserimento.
     *
     * @param parametri
     *            parametri
     */
    public void update(ParametriRigheOrdineInserimento parametri) {

        if (righeInserimentoTable != null) {
            panel.remove(righeInserimentoTable.getComponent());
            righeInserimentoTable.dispose();
            righeInserimentoTable = null;
        }

        panel.add(caricamentoLabel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();

        // carico le righe inserimento in base ai parametri specificati
        RigheOrdineInserimento righeOrdineInserimento = ordiniDocumentoBD.caricaRigheOrdineInserimento(parametri);

        // delle righe caricate vado ad avvalorare quantità e attributi di tutte quelle già presenti
        // nell'ordine
        righeOrdineInserimento = aggiornaValoriRighe(righeOrdineInserimento);

        righeInserimentoTable = new RigheInserimentoTable(righeOrdineInserimento);
        righeInserimentoTable.setRows(righeOrdineInserimento.getRighe());
        panel.remove(caricamentoLabel);
        panel.add(righeInserimentoTable.getComponent(), BorderLayout.CENTER);
        TableUtils.autoResizeAllColumns(righeInserimentoTable.getTable());
    }
}
