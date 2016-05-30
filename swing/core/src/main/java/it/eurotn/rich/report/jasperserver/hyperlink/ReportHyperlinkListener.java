/**
 * 
 */
package it.eurotn.rich.report.jasperserver.hyperlink;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.view.JRHyperlinkListener;

import org.springframework.rules.closure.Closure;

/**
 * Listener che si occupa di intercettare gli eventi legati agli hyperlink e di attivare i corretti executor.<BR>
 * Il tipo di executor è selezionato in base al valore del parametro <code>_report</code>. Se il valore contiene la
 * stringa <code>action</code> verrà utilizzata la classe {@link ActionsExecutor} altrimenti la classe
 * {@link ReportExecutor}.
 * 
 * @author fattazzo
 * 
 */
public class ReportHyperlinkListener implements JRHyperlinkListener {

    private final Map<String, Closure> actionsHyperlinkMap;

    public ReportHyperlinkListener(Map<String, Closure> actionsHyperlinkMap) {
        super();
        this.actionsHyperlinkMap = actionsHyperlinkMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.view.JRHyperlinkListener#gotoHyperlink(net.sf.jasperreports.engine.JRPrintHyperlink)
     */
    @Override
    public void gotoHyperlink(JRPrintHyperlink jrPrintHyperlink) throws JRException {

        JRPrintHyperlinkParameters parameters = jrPrintHyperlink.getHyperlinkParameters();

        JRPrintHyperlinkParameter reportParameter = null;
        List<JRPrintHyperlinkParameter> listaParametri = new ArrayList<JRPrintHyperlinkParameter>();

        // scorro la lista di parametri per trovare il parametro "_report".
        // Avvaloro anche la lista "listaParametri" per poter avere una lista tipizzata da poter passare agli executor.
        for (Iterator iterator = parameters.getParameters().iterator(); iterator.hasNext();) {
            JRPrintHyperlinkParameter par = (JRPrintHyperlinkParameter) iterator.next();

            if ("_report".equals(par.getName())) {
                reportParameter = par;
            }

            listaParametri.add(par);
        }

        IHyperlinkExecutor hyperlinkExecutor = null;

        // se non ho trovato il parametro _report lancio un'eccezione
        if (reportParameter == null) {
            throw new RuntimeException("Impossibile trovare il parametro _report.");
        }

        // controllo se il parametro _report deve aprire un nuovo report oppure rappresenta un'azione
        if (((String) reportParameter.getValue()).toLowerCase().contains("action")) {
            hyperlinkExecutor = new ActionsExecutor(actionsHyperlinkMap);
        } else {
            // Instanzio la classe che aprirà il report linkato in un nuovo editor
            hyperlinkExecutor = new ReportExecutor();
        }

        hyperlinkExecutor.execute(listaParametri);
    }
}
