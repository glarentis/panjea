package it.eurotn.panjea.contabilita.rich.commands;

import java.awt.Window;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

import org.springframework.binding.format.support.NumberFormatter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.constraint.Constraint;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.report.StampaCommand;

/**
 * Apre il report contenente il risultato delle verifiche contabili.
 *
 * @author giangi
 * @version 1.0, 29/ago/2012
 *
 */
public class OpenVerificheContabiliCommand extends ApplicationWindowAwareCommand {

    private class StampaVerificheContabiliCommand extends StampaCommand {

        /**
         * Costruttore.
         */
        public StampaVerificheContabiliCommand() {
            super("StampaVerificheContabiliCommand");
        }

        @Override
        protected Map<Object, Object> getParametri() {
            Map<Object, Object> parametri = new HashMap<Object, Object>();
            parametri.put("descAzienda", aziendaCorrente.getDenominazione());
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            parametri.put("anno", annoPartenza);
            return parametri;
        }

        @Override
        protected String getReportName() {
            return "Verifiche contabili";
        }

        @Override
        protected String getReportPath() {
            return "Contabilita/verifiche";
        }

    }

    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    private AziendaCorrente aziendaCorrente;

    private Integer annoPartenza;

    private StampaVerificheContabiliCommand stampaVerificheContabiliCommand;

    {
        stampaVerificheContabiliCommand = new StampaVerificheContabiliCommand();
    }

    /**
     *
     * Costruttore.
     *
     */
    public OpenVerificheContabiliCommand() {
        super("openVerificheContabiliCommand");
        setSecurityControllerId("openVerificheContabiliCommandController");
        RcpSupport.configure(this);
        this.contabilitaAnagraficaBD = RcpSupport.getBean(ContabilitaAnagraficaBD.BEAN_ID);
        this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        Integer anno = contabilitaAnagraficaBD.caricaContabilitaSettings().getAnnoCompetenza();
        anno--;

        annoPartenza = anno;

        InputApplicationDialog dialog = new InputApplicationDialog("Parametri verifiche", (Window) null);
        final JFormattedTextField annoTextField = new JFormattedTextField(
                new NumberFormatter(new DecimalFormat("0000"), false));
        annoTextField.setFocusLostBehavior(JFormattedTextField.COMMIT);

        dialog.setInputField(annoTextField);
        ((JFormattedTextField) dialog.getInputField()).setValue(anno);
        ((JFormattedTextField) dialog.getInputField()).setColumns(10);
        ((JFormattedTextField) dialog.getInputField()).setHorizontalAlignment(SwingConstants.RIGHT);
        Constraint annoConstraint = new Constraint() {

            @Override
            public boolean test(Object argument) {
                try {
                    annoTextField.commitEdit();
                } catch (Exception e) {
                    return false;
                }
                return annoTextField.getText() != null && !annoTextField.getText().isEmpty();
            }
        };
        dialog.setInputConstraint(annoConstraint);
        dialog.setInputLabelMessage("Anno di partenza");
        dialog.setFinishAction(new Closure() {

            @Override
            public Object call(Object arg0) {
                Integer val = (Integer) annoTextField.getValue();
                if (val != null) {
                    annoPartenza = val;
                }

                stampaVerificheContabiliCommand.execute();

                return null;
            }
        });
        dialog.showDialog();
    }

}
