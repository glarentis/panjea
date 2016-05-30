package it.eurotn.rich.report;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.binding.convert.ConversionService;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;
import org.springframework.util.Assert;

import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.bd.IPreferenceBD;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.PreferenceBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager.TipoLayoutPrefefinito;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.panjea.stampe.manager.FormulaNumeroCopieCalculator;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import net.sf.jasperreports.engine.JRParameter;

public class JecReportDocumento extends JecReport {

    public enum TipoLingua {
        ENTITA, AZIENDA
    }

    protected IAreaDocumento areaDocumento;
    protected IPreferenceBD preferenceBD;
    protected AziendaCorrente aziendaCorrente;

    private TipoLayoutPrefefinito tipoLayoutPrefefinito = TipoLayoutPrefefinito.PREDEFINITO;

    /**
     * Costruttore.
     *
     * @param areaMagazzino
     *            areamagazzino da stampare
     */
    public JecReportDocumento(final IAreaDocumento areaMagazzino) {
        this(areaMagazzino, TipoLayoutPrefefinito.PREDEFINITO);
    }

    /**
     * Costruttore.
     *
     * @param areaMagazzino
     *            areamagazzino da stampare
     * @param tipoLayoutPrefefinito
     *            tipo layout predefinito da utilizzare
     */
    public JecReportDocumento(final IAreaDocumento areaMagazzino, final TipoLayoutPrefefinito tipoLayoutPrefefinito) {
        this.areaDocumento = areaMagazzino;
        this.preferenceBD = RcpSupport.getBean(PreferenceBD.BEAN_ID);
        this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        this.tipoLayoutPrefefinito = tipoLayoutPrefefinito;
        configuraParametri();
        configuraParametriMail();
        initReportName();
    }

    @Override
    public JecReport call() throws Exception {
        parameters.put("id", areaDocumento.getId());
        initParametriMail();
        initReportPath();
        return super.call();
    }

    /**
     */
    @Override
    public void caricaLayoutStampa() {
        // se non è stato importato nessun layout prendo quello di default sul tipo area magazzino
        if (layoutStampa == null) {
            List<LayoutStampaDocumento> layoutsTAM = layoutStampeManager.caricaLayoutStampe(
                    areaDocumento.getTipoAreaDocumento(), areaDocumento.getDocumento().getEntita(),
                    areaDocumento.getDocumento().getSedeEntita());
            layoutStampa = layoutStampeManager.getLayoutStampaPredefinito(layoutsTAM, tipoLayoutPrefefinito);
        }
    }

    @Override
    protected void configuraParametri() {
        super.configuraParametri();
        setTipoLinguaReport(TipoLingua.AZIENDA);
    }

    /**
     * Configura parametri mail.
     */
    private void configuraParametriMail() {
        // ParametriMail parametri = new ParametriMail();

        ConversionService conversionService = (ConversionService) ApplicationServicesLocator.services()
                .getService(ConversionService.class);

        String reportName = (String) conversionService.getConversionExecutor(Documento.class, String.class)
                .execute(areaDocumento.getDocumento());

        parametriMail.setNomeAllegato(reportName.replace(" ", "").replace("/", "-").replace("\\", "-"));
        parametriMail.setOggetto(reportName);
        parametriMail.setNota("report " + reportName);

        ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

        if (areaDocumento.getDocumento().getEntita() != null) {
            parametriMail.setFiltroRubrica(areaDocumento.getDocumento().getEntita().getAnagrafica().getDenominazione());

            String indirizzoMail = areaDocumento.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
                    .getIndirizzoMail();
            String indirizzoPEC = areaDocumento.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
                    .getIndirizzoPEC();

            String indirizzoParametri = "";
            if (utente.getDatiMailPredefiniti().isPec()) {
                indirizzoParametri = indirizzoPEC;
            }
            if (StringUtils.isEmpty(indirizzoParametri)) {
                indirizzoParametri = indirizzoMail;
            }

            if (StringUtils.isNotEmpty(indirizzoParametri)) {
                Destinatario dest = new Destinatario();
                dest.setEmail(indirizzoParametri);
                dest.setEntita(areaDocumento.getDocumento().getEntita());
                parametriMail.getDestinatari().add(dest);
            }
        }

    }

    @Override
    public void execute(Closure postAction) {
        super.execute(postAction);
    }

    @Override
    public void generaReport(Closure postAction) {
        parameters.put("id", areaDocumento.getId());
        initParametriMail();
        initReportPath();
        super.generaReport(postAction);
    }

    /**
     * @return Returns the areaDocumento.
     */
    public IAreaDocumento getAreaDocumento() {
        return areaDocumento;
    }

    @Override
    public String getDisplayName() {
        if (areaDocumento != null && areaDocumento.getDocumento() != null && !forceUseReportName) {
            Documento documento = areaDocumento.getDocumento();
            ObjectConverter converter = ObjectConverterManager.getConverter(documento.getClass());
            if (converter != null && converter.supportToString(documento, null)) {
                String convertedString = converter.toString(documento, null);
                return convertedString;
            }
        }
        return super.getDisplayName();
    }

    /**
     * Restituisce il numero copie per la stampa.
     *
     * @return numero copie
     */
    @Override
    public int getNumeroCopie() {
        if (tipoLayoutPrefefinito == null || tipoLayoutPrefefinito == TipoLayoutPrefefinito.INTERNO) {
            return 1;
        }
        return FormulaNumeroCopieCalculator.calcolaNumeroCopie(layoutStampa.getFormulaNumeroCopie(),
                areaDocumento.fillVariables());
    }

    @Override
    public String getReportExportPath() {
        ConversionService conversionService = (ConversionService) ApplicationServicesLocator.services()
                .getService(ConversionService.class);
        return (String) conversionService.getConversionExecutor(Documento.class, String.class)
                .execute(areaDocumento.getDocumento());
    }

    /**
     * Inizializza i parametri mail.
     */
    protected void initParametriMail() {
        ParametriMail parametri = super.getParametriMail();
        parametri = ObjectUtils.defaultIfNull(parametri, new ParametriMail());
        if (areaDocumento.getDocumento().getEntita() != null
                && areaDocumento.getDocumento().getEntita().getId() != null) {
            parametri.setFiltroRubrica(areaDocumento.getDocumento().getEntita().getAnagrafica().getDenominazione());

            ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
            Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

            String indirizzoMail = areaDocumento.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
                    .getIndirizzoMail();
            String indirizzoPEC = areaDocumento.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica()
                    .getIndirizzoPEC();

            String indirizzoParametri = "";
            if (utente.getDatiMailPredefiniti().isPec()) {
                indirizzoParametri = indirizzoPEC;
            }
            if (indirizzoParametri.isEmpty()) {
                indirizzoParametri = indirizzoMail;
            }

            if (!indirizzoParametri.isEmpty()) {
                Destinatario d = new Destinatario();
                d.setEmail(indirizzoParametri);
                d.setEntita(areaDocumento.getDocumento().getEntita());
                parametri.getDestinatari().add(d);
            }

        }
        setParametriMail(parametri);
    }

    /**
     * Inizializza report name.
     */
    protected void initReportName() {
        ConversionService conversionService = (ConversionService) ApplicationServicesLocator.services()
                .getService(ConversionService.class);
        setReportName((String) conversionService.getConversionExecutor(Documento.class, String.class)
                .execute(areaDocumento.getDocumento()));
    }

    /**
     * Inizializza report path.
     */
    protected void initReportPath() {
        caricaLayoutStampa();
        if (getLayoutStampa() != null) {
            String reportLayoutName = getLayoutStampa().getReportName();
            Assert.notNull(reportLayoutName, "Nessun layout impostato per la stampa del report.");
            setReportName(reportLayoutName);
            reportPath = ((LayoutStampaDocumento) getLayoutStampa()).getTipoAreaDocumento().getReportPath() + "/"
                    + reportLayoutName;
        }
    }

    /**
     * @param layoutStampa
     *            The layoutStampa to set.
     */
    @Override
    public void setLayoutStampa(LayoutStampa layoutStampa) {
        this.layoutStampa = layoutStampa;
    }

    /**
     * @param stampaPrezzi
     *            the stampaPrezzi to set
     */
    public void setStampaPrezzi(boolean stampaPrezzi) {
        parameters.remove("stampaPrezzi");
        parameters.put("stampaPrezzi", stampaPrezzi);
    }

    /**
     * @param tipoLinguaReport
     *            the tipoLinguaReport to set
     */
    public void setTipoLinguaReport(TipoLingua tipoLinguaReport) {
        String lingua = aziendaCorrente.getLingua();
        if (tipoLinguaReport == TipoLingua.ENTITA && areaDocumento.getDocumento().getSedeEntita() != null
                && areaDocumento.getDocumento().getSedeEntita().getLingua() != null
                && !areaDocumento.getDocumento().getSedeEntita().getLingua().isEmpty()) {
            lingua = areaDocumento.getDocumento().getSedeEntita().getLingua();
        }
        parameters.remove("JRParameter.REPORT_LOCALE");
        parameters.put(JRParameter.REPORT_LOCALE, lingua);
    }
}
