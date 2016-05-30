package it.eurotn.panjea.giroclienti.rich.commands;

import java.awt.Window;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.toedter.calendar.JDateChooser;

import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.report.StampaCommand;

public class StampaSchedeGiroClientiCommand extends StampaCommand {

    private static final String CONTROLLER_ID = "stampaSchedeGiroClientiCommand";

    private Date data = null;

    /**
     * Costruttore.
     *
     */
    public StampaSchedeGiroClientiCommand() {
        super(CONTROLLER_ID, CONTROLLER_ID);
    }

    @Override
    protected void doExecuteCommand() {
        data = null;
        Window window = null;
        InputApplicationDialog dialog = new InputApplicationDialog("Data schede da stampare", window) {
            @Override
            protected void onFinish(Object inputValue) {
                super.onFinish(inputValue);
                data = (Date) inputValue;
            }
        };
        JDateChooser dateChooser = new JDateChooser("dd/MM/yy", "00/00/00", '_');
        dateChooser.setDate(Calendar.getInstance().getTime());
        dialog.setInputField(dateChooser);
        dialog.setInputLabelMessage("Data");
        dialog.showDialog();

        if (data != null) {
            super.doExecuteCommand();
        }
    }

    @Override
    protected Map<Object, Object> getParametri() {

        Map<Object, Object> parametri = new HashMap<>();
        parametri.put("data", DateFormatUtils.format(data, "dd/MM/yyyy"));
        return parametri;
    }

    @Override
    protected String getReportName() {
        return "Stampa schede giro clienti";
    }

    @Override
    protected String getReportPath() {
        return "GiroClienti/schedeGiroClienti";
    }

}