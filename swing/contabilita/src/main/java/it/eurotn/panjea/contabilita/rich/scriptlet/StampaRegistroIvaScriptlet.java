package it.eurotn.panjea.contabilita.rich.scriptlet;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 * Bean che rappresenta il report della stampa registro iva, descrive il comportamento in particolari momenti del
 * report.
 *
 * @author Leonardo
 */
public class StampaRegistroIvaScriptlet extends JRDefaultScriptlet {

    private static final Logger LOGGER = Logger.getLogger(StampaRegistroIvaScriptlet.class);

    private Integer rigaPrecedente = null;
    private Map<AreaContabileDTO, Integer> mapAreeContabiliDTO = null;

    private Map<TotaliCodiceIvaDTO, Integer> mapRigheIvaDTO = null;
    private Date ultimaDataMovimento = null;
    private String ultimoNumeroMovimento = null;

    private BigDecimal imponibileAttuale = null;
    private BigDecimal impostaAttuale = null;
    private BigDecimal totaleAttuale = null;

    private Integer idAreaPrecedente = null;

    /**
     * Costruttore.
     * 
     */
    public StampaRegistroIvaScriptlet() {
        super();
        this.mapAreeContabiliDTO = new HashMap<AreaContabileDTO, Integer>();
        this.mapRigheIvaDTO = new HashMap<TotaliCodiceIvaDTO, Integer>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, 1, 1);
        this.ultimaDataMovimento = calendar.getTime();

        this.ultimoNumeroMovimento = "";
        this.imponibileAttuale = BigDecimal.ZERO;
        this.impostaAttuale = BigDecimal.ZERO;
        this.totaleAttuale = BigDecimal.ZERO;
    }

    @Override
    public void afterDetailEval() throws JRScriptletException {
        // super.afterDetailEval();

        LOGGER.debug("--> Enter afterDetailEval");

        Integer paginaCorrente = (Integer) getVariableValue("paginaCorrente");
        Integer numeroRiga = (Integer) getVariableValue("COLUMN_COUNT");
        LOGGER.debug("--> numeroRiga = " + numeroRiga);

        BigDecimal imposta = (BigDecimal) getFieldValue("imposta");
        LOGGER.debug("--> imposta = " + imposta);

        BigDecimal imponibile = (BigDecimal) getFieldValue("imponibile");
        LOGGER.debug("--> imponibile = " + imponibile);

        Integer id = (Integer) getFieldValue("id");
        LOGGER.debug("--> id riga iva = " + id);

        AreaContabileDTO areaContabile = (AreaContabileDTO) getFieldValue("areaContabile");
        if (areaContabile.getId() != null && (idAreaPrecedente == null
                || (idAreaPrecedente != null && idAreaPrecedente.intValue() != areaContabile.getId().intValue()))) {
            this.mapAreeContabiliDTO.put(areaContabile, paginaCorrente);
            this.ultimaDataMovimento = areaContabile.getDataDocumento();
            this.ultimoNumeroMovimento = ((CodiceDocumento) getFieldValue("numeroProtocollo")).getCodice();
            this.idAreaPrecedente = areaContabile.getId();
        }
        TotaliCodiceIvaDTO rDTO = new TotaliCodiceIvaDTO();
        rDTO.setId(id);
        this.mapRigheIvaDTO.put(rDTO, numeroRiga);

        // HACK perchè l'afterDetailEval viene chiamato 2 volte per ogni riga di dettaglio
        if (rigaPrecedente == null || numeroRiga != rigaPrecedente) {
            this.imponibileAttuale = imponibileAttuale.add((imponibile != null) ? imponibile : BigDecimal.ZERO);
            this.impostaAttuale = impostaAttuale.add((imposta != null) ? imposta : BigDecimal.ZERO);
            this.totaleAttuale = totaleAttuale.add((imponibile != null) ? imponibile : BigDecimal.ZERO)
                    .add((imposta != null) ? imposta : BigDecimal.ZERO);
            rigaPrecedente = numeroRiga;
        }

        LOGGER.debug("--> Exit afterDetailEval");
    }

    @Override
    public void beforeReportInit() throws JRScriptletException {
        super.beforeReportInit();

        BigDecimal saldoImponibileRegistroIvaPrecedente = (BigDecimal) getParameterValue(
                "saldoImponibileRegistroIvaPrecedente");
        LOGGER.debug("--> saldoRegistroIvaPrecedente = " + saldoImponibileRegistroIvaPrecedente);
        this.imponibileAttuale = saldoImponibileRegistroIvaPrecedente;

        BigDecimal saldoImpostaRegistroIvaPrecedente = (BigDecimal) getParameterValue(
                "saldoImpostaRegistroIvaPrecedente");
        LOGGER.debug("--> saldoImpostaRegistroIvaPrecedente = " + saldoImponibileRegistroIvaPrecedente);
        this.impostaAttuale = saldoImpostaRegistroIvaPrecedente;

        BigDecimal saldoTotaleRegistroIvaPrecedente = (BigDecimal) getParameterValue(
                "saldoTotaleDocumentoRegistroIvaPrecedente");
        LOGGER.debug("--> saldoTotaleDocumentoRegistroIvaPrecedente = " + saldoImponibileRegistroIvaPrecedente);
        this.totaleAttuale = saldoTotaleRegistroIvaPrecedente;
    }

    @Override
    public void callAfterDetailEval() throws JRScriptletException {
        super.callAfterDetailEval();
    }

    /**
     * @return imponibileAttuale
     */
    public BigDecimal getImponibileAttuale() {
        return imponibileAttuale;
    }

    /**
     * @return the impostaAttuale
     */
    public BigDecimal getImpostaAttuale() {
        return impostaAttuale;
    }

    /**
     * @return the mapAreeContabiliDTO
     */
    public Map<AreaContabileDTO, Integer> getMapAreeContabili() {
        return mapAreeContabiliDTO;
    }

    /**
     * @return the mapRigheIvaDTO
     */
    public Map<TotaliCodiceIvaDTO, Integer> getMapRigheIva() {
        return mapRigheIvaDTO;
    }

    /**
     * @return the totaleAttuale
     */
    public BigDecimal getTotaleAttuale() {
        return totaleAttuale;
    }

    /**
     * @return the ultimaDataMovimento
     */
    public Date getUltimaDataMovimento() {
        return ultimaDataMovimento;
    }

    /**
     * @return the ultimoNumeroMovimento
     */
    public String getUltimoNumeroMovimento() {
        return ultimoNumeroMovimento;
    }

    /**
     * 
     * @return ultimo numero pagina del report
     */
    public Integer getUltimoNumeroPagina() {
        try {
            return (Integer) getVariableValue("paginaCorrente");
        } catch (JRScriptletException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Ultimo numero pagina non trovato, restituisco 0", e);
            }
            return 0;
        }
    }

}
