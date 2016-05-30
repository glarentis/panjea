package it.eurotn.panjea.magazzino.rich.editors.listinoprezzi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.manager.listinoprezzi.ListinoPrezziDTO;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.verificaprezzo.ParametriCalcoloPrezziPM;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.report.StampaCommand;

public class ListinoPrezziPage extends AbstractTablePageEditor<ListinoPrezziDTO> {

    private class CaricaPaginaCommand extends ActionCommand {
        private static final String COMMAND_ID = "caricaPaginaCommand";

        public CaricaPaginaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            numeroPagina++;
            loadData();
        }
    }

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
            numeroPagina = 0;
            loadData();
        }
    }

    public class OpenVerificaPrezzoCommand implements ActionCommandExecutor {

        @Override
        public void execute() {
            if (getTable().getSelectedObject() == null) {
                return;
            }
            if (!listinoPrezziParametriForm.getFormModel().isCommittable()) {
                return;
            }
            listinoPrezziParametriForm.commit();
            ParametriListinoPrezzi parametriListinoPrezzi = (ParametriListinoPrezzi) listinoPrezziParametriForm
                    .getFormObject();
            ParametriCalcoloPrezziPM parametriCalcoloPrezzi = new ParametriCalcoloPrezziPM();
            parametriCalcoloPrezzi.setData(parametriListinoPrezzi.getData());
            parametriCalcoloPrezzi.setEntita(parametriListinoPrezzi.getEntita());
            parametriCalcoloPrezzi.setArticolo(parametriListinoPrezzi.getArticoloPartenza());
            parametriCalcoloPrezzi.setEffettuaRicerca(true);
            OpenEditorEvent event = new OpenEditorEvent(parametriCalcoloPrezzi);
            Application.instance().getApplicationContext().publishEvent(event);
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
            listinoPrezziParametriForm.getValueModel("articoloPartenza").setValue(null);
            listinoPrezziParametriForm.getValueModel("entita").setValue(null);
            numeroPagina = 0;
            loadData();
        }

    }

    private class StampaPrezziCommand extends StampaCommand {

        /**
         * Costruttore.
         */
        public StampaPrezziCommand() {
            super("printCommand");
        }

        @Override
        protected Map<Object, Object> getParametri() {
            HashMap<Object, Object> parametri = new HashMap<Object, Object>();
            // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            // parametri.put("data", sdf.format(parametriCalcoloPrezziPM.getData()));
            // if (parametriCalcoloPrezziPM.getListino().getId() != null) {
            // parametri.put("idListino", parametriCalcoloPrezziPM.getListino().getId());
            // }
            // if (parametriCalcoloPrezziPM.getListinoAlternativo().getId() != null) {
            // parametri.put("idListinoAlternativo",
            // parametriCalcoloPrezziPM.getListinoAlternativo().getId());
            // }
            // parametri.put("idSedeEntita", parametriCalcoloPrezziPM.getSedeEntita().getId());
            // parametri.put("htmlParameters", parametriCalcoloPrezziPM.getHtmlParameters());
            // parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            // AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
            // parametri.put("descAzienda", aziendaCorrente.getDenominazione());
            return parametri;
        }

        @Override
        protected String getReportName() {
            return "PREZZI FINALI";
        }

        @Override
        protected String getReportPath() {
            return "Magazzino/Anagrafica/listinoPrezziFinali";
        }
    }

    public static final String PAGE_ID = "verificaPrezziArticoliPage";
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private StampaCommand stampaCommand;
    private ListinoPrezziParametriForm listinoPrezziParametriForm;
    private CercaCommand cercaCommand;
    private ResetParametriRicercaCommand resetParametriRicercaCommand;
    private int numeroPagina;
    private CaricaPaginaCommand caricaPaginaCommand;

    /**
     *
     * Costruttore.
     */
    public ListinoPrezziPage() {
        super(PAGE_ID, new ListinoPrezziTableModel());
        numeroPagina = 0;
        SortableTableModel model = (SortableTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel(), SortableTableModel.class);
        model.setSortable(false);
        getTable().setPropertyCommandExecutor(new OpenVerificaPrezzoCommand());
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] {};
    }

    @Override
    public JComponent getHeaderControl() {
        cercaCommand = new CercaCommand();
        resetParametriRicercaCommand = new ResetParametriRicercaCommand();
        caricaPaginaCommand = new CaricaPaginaCommand();

        JECCommandGroup group = new JECCommandGroup();
        group.add(resetParametriRicercaCommand);
        group.add(cercaCommand);
        group.addSeparator();
        group.add(caricaPaginaCommand);

        JPanel parametriPanel = new JPanel(new BorderLayout());
        JComponent toolbar = group.createToolBar();
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        parametriPanel.add(toolbar, BorderLayout.NORTH);
        listinoPrezziParametriForm = new ListinoPrezziParametriForm();
        parametriPanel.add(listinoPrezziParametriForm.getControl(), BorderLayout.CENTER);
        return parametriPanel;
    }

    /**
     * @return Returns the magazzinoDocumentoBD.
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    /**
     *
     * @return command per la stampa
     */
    private StampaCommand getStampaCommand() {
        if (stampaCommand == null) {
            stampaCommand = new StampaPrezziCommand();

        }
        return stampaCommand;
    }

    @Override
    public Collection<ListinoPrezziDTO> loadTableData() {
        List<ListinoPrezziDTO> result = Collections.emptyList();
        if (listinoPrezziParametriForm.getFormModel().isCommittable()) {
            listinoPrezziParametriForm.getFormModel().commit();
            ParametriListinoPrezzi parametri = (ParametriListinoPrezzi) listinoPrezziParametriForm.getFormObject();
            parametri.setNumPagina(numeroPagina);
            result = magazzinoDocumentoBD.caricaListinoPrezzi(parametri);
            if (numeroPagina > 0) {
                List<ListinoPrezziDTO> righePresenti = getTable().getRows();
                righePresenti.addAll(result);
                return righePresenti;
            }
        }
        return result;
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<ListinoPrezziDTO> results) {
        super.processTableData(results);
        if (numeroPagina > 0) {
            int numRecord = ((ParametriListinoPrezzi) listinoPrezziParametriForm.getFormObject())
                    .getNumRecordInPagina();
            getTable().selectRow(((numeroPagina * numRecord) - 1), null);
        }
    }

    @Override
    public Collection<ListinoPrezziDTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {

    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

}
