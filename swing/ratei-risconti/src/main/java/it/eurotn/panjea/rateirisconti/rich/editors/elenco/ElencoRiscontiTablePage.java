package it.eurotn.panjea.rateirisconti.rich.editors.elenco;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.swing.JComponent;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.constraint.Constraint;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.TableColumnGroup;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaElencoRiscontiDTO;
import it.eurotn.panjea.rateirisconti.rich.bd.IRateiRiscontiBD;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class ElencoRiscontiTablePage extends AbstractTablePageEditor<RigaElencoRiscontiDTO> {

    private class CreaAreeChiusureCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public CreaAreeChiusureCommand() {
            super("creaAreeChiusureRiscontiCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            if (rateiRiscontiHeaderComponent.getAnno() != null) {
                InputApplicationDialog dialog = new InputApplicationDialog("Data movimento di chiusura", (Window) null);
                Calendar dataCal = Calendar.getInstance();
                dataCal.set(Calendar.YEAR, rateiRiscontiHeaderComponent.getAnno());
                IDateEditor dateEditor = new PanjeaTextFieldDateEditor("dd/MM/yy", "##/##/##", '_');
                JDateChooser dateChooser = new JDateChooser(dateEditor);
                dateChooser.setDate(dataCal.getTime());
                dialog.setInputConstraint(new Constraint() {

                    @Override
                    public boolean test(Object arg0) {
                        return arg0 != null;
                    }
                });
                dialog.setInputField(dateChooser);
                dialog.setInputLabelMessage("Data");
                dialog.setFinishAction(new Closure() {

                    @Override
                    public Object call(Object paramObject) {
                        Date dataMovimento = (Date) paramObject;
                        ParametriRicercaMovimentiContabili parametri = rateiRiscontiBD
                                .creaMovimentiChiusureRisconti(rateiRiscontiHeaderComponent.getAnno(), dataMovimento);

                        if (parametri != null) {
                            LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
                            Application.instance().getApplicationContext().publishEvent(event);
                        }

                        refreshData();
                        return null;
                    }
                });
                dialog.showDialog();
            }
        }
    }

    private class OpenRiscontiAnnoCommand extends ActionCommand {

        private RateiRiscontiAnnoDialog rateiRiscontiAnnoDialog;

        /**
         * Costruttore.
         */
        public OpenRiscontiAnnoCommand() {
            super();
            rateiRiscontiAnnoDialog = new RateiRiscontiAnnoDialog();
        }

        @Override
        protected void doExecuteCommand() {

            RigaElencoRiscontiDTO rigaElencoRiscontiDTO = getTable().getSelectedObject();
            if (rigaElencoRiscontiDTO != null) {
                rateiRiscontiAnnoDialog.setRigaRateoRisconto(rigaElencoRiscontiDTO.getIdRateoRisconto());
                rateiRiscontiAnnoDialog.showDialog();
            }
        }

    }

    private IRateiRiscontiBD rateiRiscontiBD;

    private CreaAreeChiusureCommand creaAreeChiusureCommand;
    private OpenRiscontiAnnoCommand openRiscontiAnnoCommand;

    private RateiRiscontiHeaderComponent rateiRiscontiHeaderComponent;

    /**
     * Costruttore.
     */
    protected ElencoRiscontiTablePage() {
        super("ElencoRiscontiTablePage", new ElencoRiscontiTableModel());

        this.openRiscontiAnnoCommand = new OpenRiscontiAnnoCommand();
        getTable().setPropertyCommandExecutor(openRiscontiAnnoCommand);
    }

    @Override
    protected JComponent createTableWidget() {

        JideTable table = (JideTable) getTable().getTable();
        table.setNestedTableHeader(true);
        ((NestedTableHeader) table.getTableHeader()).setUseNativeHeaderRenderer(true);

        TableColumnGroup rigaRateoRiscontoGroup = new TableColumnGroup("");
        rigaRateoRiscontoGroup.add(table.getColumnModel().getColumn(0));
        rigaRateoRiscontoGroup.add(table.getColumnModel().getColumn(1));
        rigaRateoRiscontoGroup.add(table.getColumnModel().getColumn(2));
        rigaRateoRiscontoGroup.add(table.getColumnModel().getColumn(3));
        rigaRateoRiscontoGroup.add(table.getColumnModel().getColumn(4));

        TableColumnGroup rigaAnnoSelezionatoGroup = new TableColumnGroup("Anno selezionato");
        rigaAnnoSelezionatoGroup.add(table.getColumnModel().getColumn(5));
        rigaAnnoSelezionatoGroup.add(table.getColumnModel().getColumn(6));
        rigaAnnoSelezionatoGroup.add(table.getColumnModel().getColumn(7));

        TableColumnGroup rigaAnniSuccessiviGroup = new TableColumnGroup("Anni successivi");
        rigaAnniSuccessiviGroup.add(table.getColumnModel().getColumn(8));
        rigaAnniSuccessiviGroup.add(table.getColumnModel().getColumn(9));

        NestedTableHeader header = (NestedTableHeader) table.getTableHeader();
        header.addColumnGroup(rigaRateoRiscontoGroup);
        header.addColumnGroup(rigaAnnoSelezionatoGroup);
        header.addColumnGroup(rigaAnniSuccessiviGroup);

        return super.createTableWidget();
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getCreaAreeChiusureCommand(), getRefreshCommand() };
    }

    /**
     * @return the creaAreeChiusureCommand
     */
    public CreaAreeChiusureCommand getCreaAreeChiusureCommand() {
        if (creaAreeChiusureCommand == null) {
            creaAreeChiusureCommand = new CreaAreeChiusureCommand();
        }

        return creaAreeChiusureCommand;
    }

    @Override
    public JComponent getHeaderControl() {
        rateiRiscontiHeaderComponent = new RateiRiscontiHeaderComponent(this);
        return rateiRiscontiHeaderComponent;
    }

    @Override
    public Collection<RigaElencoRiscontiDTO> loadTableData() {
        Collection<RigaElencoRiscontiDTO> elenco = new ArrayList<RigaElencoRiscontiDTO>();

        if (rateiRiscontiHeaderComponent.getAnno() != null) {
            elenco = rateiRiscontiBD.caricaElencoRisconti(rateiRiscontiHeaderComponent.getAnno(),
                    rateiRiscontiHeaderComponent.getClasseRiga());
        }

        return elenco;
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public Collection<RigaElencoRiscontiDTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
        // non faccio niente
    }

    @Override
    public void setFormObject(Object object) {
        // non faccio niente
    }

    /**
     * @param rateiRiscontiBD
     *            the rateiRiscontiBD to set
     */
    public void setRateiRiscontiBD(IRateiRiscontiBD rateiRiscontiBD) {
        this.rateiRiscontiBD = rateiRiscontiBD;
    }

}
