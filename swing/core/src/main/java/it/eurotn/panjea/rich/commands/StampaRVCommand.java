package it.eurotn.panjea.rich.commands;

import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.StampaCommand;

public class StampaRVCommand extends StampaCommand {

    public static final String COMMAND_ID = "stampaRVSelCommand";
    public static final String PARAM_DATI_GENERAZIONE = "paramDatiGenerazione";
    public static final String PARAM_AREE_MAGAZZINO = "paramAreeMagazzino";
    public static final String PARAM_LIST_RATE_ID = "rateId";

    protected ReportManager reportManager;

    /**
     * Costruttore.
     *
     */
    public StampaRVCommand() {
        this(COMMAND_ID);
    }

    /**
     * Costruttore
     *
     * @param id
     *            id del command
     */
    public StampaRVCommand(final String id) {
        super(id, id);
        RcpSupport.configure(this);
        reportManager = (ReportManager) Application.instance().getApplicationContext().getBean("reportManager");
        setEnabled(reportManager.reportExist(getReportFolder(), getReportNamePath()));
    }

    @Override
    protected Map<Object, Object> getParametri() {
        HashMap<Object, Object> parametri = new HashMap<Object, Object>();
        String dataGenerazione = (String) getParameter(PARAM_DATI_GENERAZIONE, null);
        if (dataGenerazione != null) {
            parametri.put("dataCreazione", dataGenerazione);
        }

        String idAree = (String) getParameter(PARAM_AREE_MAGAZZINO, null);
        parametri.put("areeMagazzinoId", idAree);

        String idRate = (String) getParameter(PARAM_LIST_RATE_ID, null);
        parametri.put("rateId", idRate);

        return parametri;
    }

    protected String getReportFolder() {
        return "Tesoreria";
    }

    @Override
    protected String getReportName() {
        return "Richiesta di versamento";
    }

    protected String getReportNamePath() {
        return "RichiestaVersamentoRate";
    }

    @Override
    protected String getReportPath() {
        return getReportFolder() + "/" + getReportNamePath();
    }

}
