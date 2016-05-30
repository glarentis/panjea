/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.stampa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.TableColumnGroup;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.report.StampaCommand;

/**
 * TablePage per la visualizzazione dei risultati della ricerca di {@link ContrattoStampaDTO}.
 *
 */
public class RisultatiRicercaStampaContrattiTablePage extends AbstractTablePageEditor<ContrattoStampaDTO> {

    private class CercaCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         *
         */
        public CercaCommand() {
            super("searchCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            formRicerca.getFormModel().commit();
            parametriRicercaStampaContratti = (ParametriRicercaStampaContratti) formRicerca.getFormObject();
            parametriRicercaStampaContratti.setEffettuaRicerca(true);
            loadData();
        }
    }

    private class ResetParametriRicercaCommand extends ActionCommand {

        private static final String COMMAND_ID = "resetCommand";

        /**
         * Costruttore.
         *
         */
        public ResetParametriRicercaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);

        }

        @Override
        protected void doExecuteCommand() {
            formRicerca.getValueModel("articolo").setValue(null);
            if (!formRicerca.getFormModel().getFieldMetadata("entita").isReadOnly()) {
                formRicerca.getValueModel("entita").setValue(null);
            }
            formRicerca.setFormObject(parametriRicercaStampaContratti);
            loadData();
        }

    }

    private class StampaContrattiCommand extends StampaCommand {

        private static final String CONTROLLER_ID = "stampaContrattiCommand";

        /**
         * Costruttore.
         */
        public StampaContrattiCommand() {
            super(CONTROLLER_ID);
        }

        @Override
        protected Map<Object, Object> getParametri() {
            HashMap<Object, Object> parametri = new HashMap<Object, Object>();
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            parametri.put("idEntita", parametriRicercaStampaContratti.getEntita() != null
                    ? parametriRicercaStampaContratti.getEntita().getId() : new Integer(-1));
            parametri.put("descEntita", parametriRicercaStampaContratti.getEntita() != null
                    ? parametriRicercaStampaContratti.getEntita().getAnagrafica().getDenominazione() : "");
            parametri.put("idArticolo", parametriRicercaStampaContratti.getArticolo() != null
                    ? parametriRicercaStampaContratti.getArticolo().getId() : new Integer(-1));
            parametri.put("descArticolo", parametriRicercaStampaContratti.getArticolo() != null
                    ? parametriRicercaStampaContratti.getArticolo().getDescrizione() : "");
            parametri.put("escludiContrattiNonAttivi", parametriRicercaStampaContratti.isEscludiContrattiNonAttivi());

            return parametri;
        }

        @Override
        protected String getReportName() {
            return "Stampa contratti";
        }

        @Override
        protected String getReportPath() {
            return "Magazzino/Anagrafica/contratti";
        }

    }

    public static final String PAGE_ID = "risultatiRicercaStampaContrattiTablePage";

    private CercaCommand cercaCommand;

    private ResetParametriRicercaCommand resetParametriRicercaCommand;

    private StampaContrattiCommand stampaContrattiCommand;

    protected ParametriRicercaStampaContratti parametriRicercaStampaContratti = null;

    private IContrattoBD contrattoBD;

    private ParametriRicercaStampaContrattiForm formRicerca;

    /**
     * Costruttore.
     */
    public RisultatiRicercaStampaContrattiTablePage() {
        super(PAGE_ID, new StampaContrattiTableModel());
    }

    @Override
    protected JComponent createTableWidget() {
        JComponent component = super.createTableWidget();

        JideTable table = (JideTable) getTable().getTable();
        table.setNestedTableHeader(true);
        ((NestedTableHeader) table.getTableHeader()).setUseNativeHeaderRenderer(true);

        TableColumnGroup contrattoGroup = new TableColumnGroup("Dati del contratto");
        contrattoGroup.add(table.getColumnModel().getColumn(1));
        contrattoGroup.add(table.getColumnModel().getColumn(2));
        contrattoGroup.add(table.getColumnModel().getColumn(3));
        contrattoGroup.add(table.getColumnModel().getColumn(4));

        TableColumnGroup entitaGroup = new TableColumnGroup("Entità interessate");
        entitaGroup.add(table.getColumnModel().getColumn(5));
        entitaGroup.add(table.getColumnModel().getColumn(6));
        entitaGroup.add(table.getColumnModel().getColumn(7));

        TableColumnGroup articoloGroup = new TableColumnGroup("Applicato a");
        articoloGroup.add(table.getColumnModel().getColumn(8));
        articoloGroup.add(table.getColumnModel().getColumn(9));

        TableColumnGroup stratPrezzoGroup = new TableColumnGroup("Strategia prezzo");
        stratPrezzoGroup.add(table.getColumnModel().getColumn(10));
        stratPrezzoGroup.add(table.getColumnModel().getColumn(11));
        stratPrezzoGroup.add(table.getColumnModel().getColumn(12));
        stratPrezzoGroup.add(table.getColumnModel().getColumn(13));

        TableColumnGroup stratScontoGroup = new TableColumnGroup("Strategia sconto");
        stratScontoGroup.add(table.getColumnModel().getColumn(14));
        stratScontoGroup.add(table.getColumnModel().getColumn(15));
        stratScontoGroup.add(table.getColumnModel().getColumn(16));
        stratScontoGroup.add(table.getColumnModel().getColumn(17));
        stratScontoGroup.add(table.getColumnModel().getColumn(18));
        stratScontoGroup.add(table.getColumnModel().getColumn(19));

        NestedTableHeader header = (NestedTableHeader) table.getTableHeader();
        header.addColumnGroup(contrattoGroup);
        header.addColumnGroup(entitaGroup);
        header.addColumnGroup(articoloGroup);
        header.addColumnGroup(stratPrezzoGroup);
        header.addColumnGroup(stratScontoGroup);

        getTable().getTable().getTableHeader().setReorderingAllowed(false);

        return component;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public AbstractCommand[] getCommands() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getStampaContrattiCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return resetParametriRicercaCommand;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return cercaCommand;
    }

    @Override
    public JComponent getHeaderControl() {
        cercaCommand = new CercaCommand();
        resetParametriRicercaCommand = new ResetParametriRicercaCommand();
        JECCommandGroup group = new JECCommandGroup();
        group.add(resetParametriRicercaCommand);
        group.add(cercaCommand);
        JPanel parametriPanel = new JPanel(new BorderLayout());
        JComponent toolbar = group.createToolBar();
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        parametriPanel.add(toolbar, BorderLayout.NORTH);

        formRicerca = new ParametriRicercaStampaContrattiForm();
        formRicerca.setFormObject(new ParametriRicercaStampaContratti());
        GuiStandardUtils.attachBorder(formRicerca.getControl());
        parametriPanel.add(formRicerca.getControl(), BorderLayout.CENTER);
        return parametriPanel;
    }

    /**
     * @return the stampaContrattiCommand
     */
    public StampaContrattiCommand getStampaContrattiCommand() {
        if (stampaContrattiCommand == null) {
            stampaContrattiCommand = new StampaContrattiCommand();
        }

        return stampaContrattiCommand;
    }

    @Override
    public List<ContrattoStampaDTO> loadTableData() {
        List<ContrattoStampaDTO> contrattiDTO = Collections.emptyList();

        if (parametriRicercaStampaContratti.isEffettuaRicerca()) {
            contrattiDTO = contrattoBD.caricaStampaContratti(parametriRicercaStampaContratti);
        }

        return contrattiDTO;
    }

    @Override
    public void onPostPageOpen() {
        // nothing to do
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public List<ContrattoStampaDTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
        // non salvo lo stato perchè la tabella prevede solo questo tipo di layout
    }

    /**
     * @param contrattoBD
     *            the contrattoBD to set
     */
    public void setContrattoBD(IContrattoBD contrattoBD) {
        this.contrattoBD = contrattoBD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaStampaContratti) {
            this.parametriRicercaStampaContratti = (ParametriRicercaStampaContratti) object;
        } else if (object instanceof Cliente) {
            this.parametriRicercaStampaContratti = new ParametriRicercaStampaContratti();
            parametriRicercaStampaContratti.setEntita(((Cliente) object).getEntitaLite());
            parametriRicercaStampaContratti.setEffettuaRicerca(true);
            formRicerca.getFormModel().getFieldMetadata("entita").setReadOnly(true);
            formRicerca.setFormObject(parametriRicercaStampaContratti);
        } else {
            this.parametriRicercaStampaContratti = new ParametriRicercaStampaContratti();
        }
    }
}
