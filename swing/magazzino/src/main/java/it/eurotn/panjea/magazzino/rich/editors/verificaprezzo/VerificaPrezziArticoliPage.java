package it.eurotn.panjea.magazzino.rich.editors.verificaprezzo;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.report.StampaCommand;

public class VerificaPrezziArticoliPage extends AbstractTablePageEditor<RigaArticoloLite> {

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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            parametri.put("data", sdf.format(parametriCalcoloPrezziPM.getData()));
            if (parametriCalcoloPrezziPM.getListino().getId() != null) {
                parametri.put("idListino", parametriCalcoloPrezziPM.getListino().getId());
            }
            if (parametriCalcoloPrezziPM.getListinoAlternativo().getId() != null) {
                parametri.put("idListinoAlternativo", parametriCalcoloPrezziPM.getListinoAlternativo().getId());
            }
            parametri.put("idSedeEntita", parametriCalcoloPrezziPM.getSedeEntita().getId());
            parametri.put("htmlParameters", parametriCalcoloPrezziPM.getHtmlParameters());
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
            parametri.put("descAzienda", aziendaCorrente.getDenominazione());
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

    private ParametriCalcoloPrezziPM parametriCalcoloPrezziPM;
    private StampaCommand stampaCommand;

    /**
     *
     * Costruttore.
     */
    public VerificaPrezziArticoliPage() {
        super(PAGE_ID, new VerificaPrezziArticoliTableModel());
        getTable().setDelayForSelection(300);
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getStampaCommand() };
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
    public Collection<RigaArticoloLite> loadTableData() {
        return null;
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void processTableData(Collection<RigaArticoloLite> results) {
        super.processTableData(results);

        setVisible(parametriCalcoloPrezziPM.isEffettuaRicerca() && parametriCalcoloPrezziPM.getArticolo() == null);
    }

    @Override
    public Collection<RigaArticoloLite> refreshTableData() {
        List<RigaArticoloLite> righe = Collections.emptyList();
        if (parametriCalcoloPrezziPM.isEffettuaRicerca() && parametriCalcoloPrezziPM.getArticolo() == null) {
            // List<ListinoPrezziDTO> prezzi = magazzinoDocumentoBD.caricaListinoPrezzi(1, 0,
            // Integer.MAX_VALUE,
            // parametriCalcoloPrezziPM.getEntita().getId());
        }

        return righe;
    }

    @Override
    public void setFormObject(Object object) {
        parametriCalcoloPrezziPM = (ParametriCalcoloPrezziPM) object;
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);
        // parametriCalcoloPrezziPM.setArticolo(((RigaArticoloLite) obj).getArticolo());
        // firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, parametriCalcoloPrezziPM);
    }

}
