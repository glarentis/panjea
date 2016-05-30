/**
 * 
 */
package it.eurotn.rich.report.jasperserver.hyperlink;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;

import org.springframework.rules.closure.Closure;

/**
 * @author fattazzo
 * 
 */
public class ActionsExecutor implements IHyperlinkExecutor {

    private final Map<String, Closure> actionsHyperlinkMap;

    public ActionsExecutor(Map<String, Closure> actionsHyperlinkMap) {
        super();
        this.actionsHyperlinkMap = actionsHyperlinkMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.eurotn.rich.report.jasperserver.hyperlink.IHyperlinkExecutor#execute(java.util.List)
     */
    @Override
    public void execute(List<JRPrintHyperlinkParameter> list) {

        String action = null;
        for (JRPrintHyperlinkParameter parametro : list) {

            if ("_report".equals(parametro.getName())) {
                action = (String) parametro.getValue();
                break;
            }
        }

        // Il parametro action è composto in questo modo:
        // action:nomeazione:descrizione
        // quindi isolo il nomeazione
        int idxbarra = action.indexOf(":", 2);

        if (idxbarra != -1) {
            action = action.split(":")[1];
        }

        if (actionsHyperlinkMap.containsKey(action)) {
            actionsHyperlinkMap.get(action).call(list);
        }
    }

}
