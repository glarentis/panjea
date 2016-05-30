package it.eurotn.panjea.contabilita.rich.commands;

import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.util.Assert;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.StampaCommand;

public class OpenVerificheSaldiCommand extends StampaCommand {
    private String idCommand;
    private String sottotipoConto;

    /**
     *
     * Costruttore.
     *
     */
    public OpenVerificheSaldiCommand() {
        super("openVerificheSaldiCommand", "openVerificheSaldiCommand");
    }

    /**
     * @return Returns the idCommand.
     */
    public String getIdCommand() {
        return idCommand;
    }

    @Override
    protected Map<Object, Object> getParametri() {
        Assert.notNull(sottotipoConto);
        AziendaCorrente azienda = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        Map<Object, Object> parameters = new HashMap<Object, Object>();
        parameters.put("sottotipoConto", sottotipoConto);
        parameters.put("descAzienda", azienda.getDenominazione());
        parameters.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
        return parameters;
    }

    @Override
    protected String getReportName() {
        return "Verifiche saldi";
    }

    @Override
    protected String getReportPath() {
        return "Contabilita/verificaSaldi";
    }

    /**
     * @return Returns the sottotipoConto.
     */
    public String getSottotipoConto() {
        return sottotipoConto;
    }

    /**
     * @param idCommand
     *            The idCommand to set.
     */
    public void setIdCommand(String idCommand) {
        this.idCommand = idCommand;
        setId(idCommand);
    }

    /**
     * @param sottotipoConto
     *            The sottotipoConto to set.
     */
    public void setSottotipoConto(String sottotipoConto) {
        this.sottotipoConto = sottotipoConto;
    }

}
