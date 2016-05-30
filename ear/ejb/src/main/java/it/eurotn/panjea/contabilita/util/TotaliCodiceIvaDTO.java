/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;

/**
 * @author Leonardo
 */
public class TotaliCodiceIvaDTO implements Serializable {

    private static final long serialVersionUID = -2979471972821425746L;

    // id della riga iva mi serve per il report registro iva
    private Integer id;
    private BigDecimal imponibile;
    private BigDecimal imponibileDetraibile;
    private BigDecimal imponibileIndetraibile;
    private BigDecimal imposta;
    private BigDecimal impostaDetraibile;
    private BigDecimal impostaIndetraibile;

    // dati Codice iva
    private Integer idCodiceIva;
    private String codiceIva;
    private String descrizioneRegistro;
    private BigDecimal percApplicazione;
    private BigDecimal percIndetraibilita;
    private boolean splitPayment;

    // area contabile collegata
    private AreaContabileDTO areaContabile;
    private RegistroIva registroIva;
    private BigDecimal totale;
    private BigDecimal totDaVentilazione;
    private BigDecimal totAliquotaNota;
    private BigDecimal totRicevutaFiscale;
    private BigDecimal totDaFattura;

    // ventilazione
    private BigDecimal percentualePesoVentilazione;
    private BigDecimal importoPesoVentilazione;
    private boolean filler;

    // mi serve per ordinare la lista di risultati per il report registro iva
    private Long ordinamento;

    // mi serve per sapere se il valore va condiderato per il calcolo della liquidazione oppure no
    private boolean consideraPerLiquidazione;

    /**
     * Costruttore.
     */
    public TotaliCodiceIvaDTO() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null);
    }

    /**
     * Costruttore utilizzato per il report registro iva.
     * 
     * @param id
     *            id
     * @param ordinamento
     *            ordinamento
     * @param imponibile
     *            imponibile
     * @param imposta
     *            imposta
     * @param idCodiceIva
     *            idCodiceIva
     * @param descrizioneRegistro
     *            descrizioneRegistro
     * @param percApplicazione
     *            percApplicazione
     * @param percIndetraibilita
     *            percIndetraibilita
     * @param idAreaContabile
     *            idAreaContabile
     * @param dataDocumento
     *            dataDocumento
     * @param numeroDocumento
     *            numeroDocumento
     * @param numeroProtocollo
     *            numeroProtocollo
     * @param totale
     *            totale
     * @param idEntita
     *            idEntita
     * @param codiceEntita
     *            codiceEntita
     * @param ragioneSocialeEntita
     *            ragioneSocialeEntita
     * @param numeroPaginaGiornale
     *            numeroPaginaGiornale
     * @param codice
     *            codice/**
     * 
     */
    public TotaliCodiceIvaDTO(final Integer id, final Long ordinamento, final BigDecimal imponibile,
            final BigDecimal imposta, final Integer idCodiceIva, final String codice, final String descrizioneRegistro,
            final BigDecimal percApplicazione, final BigDecimal percIndetraibilita, final Integer idAreaContabile,
            final Date dataDocumento, final String numeroDocumento, final String numeroProtocollo, final Importo totale,
            final Integer idEntita, final Integer codiceEntita, final String ragioneSocialeEntita,
            final Integer numeroPaginaGiornale) {
        this.id = id;
        this.ordinamento = ordinamento;
        this.imponibile = imponibile;
        this.imposta = imposta;
        this.idCodiceIva = idCodiceIva;
        this.codiceIva = codice;
        this.descrizioneRegistro = descrizioneRegistro;
        this.percApplicazione = percApplicazione;
        this.percIndetraibilita = percIndetraibilita;

        CodiceDocumento codiceDocumento = null;
        if (numeroDocumento != null) {
            codiceDocumento = new CodiceDocumento();
            codiceDocumento.setCodice(numeroDocumento);
        }
        CodiceDocumento codiceProtocollo = null;
        if (numeroProtocollo != null) {
            codiceProtocollo = new CodiceDocumento();
            codiceProtocollo.setCodice(numeroProtocollo);
        }

        this.areaContabile = new AreaContabileDTO(idAreaContabile, dataDocumento, codiceDocumento, codiceProtocollo,
                totale, idEntita, codiceEntita, ragioneSocialeEntita, numeroPaginaGiornale);
        this.filler = Boolean.FALSE;
        this.consideraPerLiquidazione = true;
    }

    /**
     * Costruttore utilizzato per il report registro iva.
     * 
     * @param id
     *            id
     * @param ordinamento
     *            ordinamento
     * @param imponibile
     *            imponibile
     * @param imposta
     *            imposta
     * @param idCodiceIva
     *            idCodiceIva
     * @param descrizioneRegistro
     *            descrizioneRegistro
     * @param percApplicazione
     *            percApplicazione
     * @param percIndetraibilita
     *            percIndetraibilita
     * @param idAreaContabile
     *            idAreaContabile
     * @param dataDocumento
     *            dataDocumento
     * @param numeroDocumento
     *            numeroDocumento
     * @param numeroProtocollo
     *            numeroProtocollo
     * @param totale
     *            totale
     * @param idEntita
     *            idEntita
     * @param codiceEntita
     *            codiceEntita
     * @param ragioneSocialeEntita
     *            ragioneSocialeEntita
     * @param numeroPaginaGiornale
     *            numeroPaginaGiornale
     * @param codice
     *            codice
     * @param notaCreditoEnable
     *            notaCreditoEnable
     */
    public TotaliCodiceIvaDTO(final Integer id, final Long ordinamento, final BigDecimal imponibile,
            final BigDecimal imposta, final Integer idCodiceIva, final String codice, final String descrizioneRegistro,
            final BigDecimal percApplicazione, final BigDecimal percIndetraibilita, final Integer idAreaContabile,
            final Date dataDocumento, final String numeroDocumento, final String numeroProtocollo, final Importo totale,
            final Integer idEntita, final Integer codiceEntita, final String ragioneSocialeEntita,
            final Integer numeroPaginaGiornale, final boolean notaCreditoEnable) {
        this.id = id;
        this.ordinamento = ordinamento;
        this.imponibile = imponibile;
        this.imposta = imposta;
        this.idCodiceIva = idCodiceIva;
        this.codiceIva = codice;
        this.descrizioneRegistro = descrizioneRegistro;
        this.percApplicazione = percApplicazione;
        this.percIndetraibilita = percIndetraibilita;

        CodiceDocumento codiceDocumento = null;
        if (numeroDocumento != null) {
            codiceDocumento = new CodiceDocumento();
            codiceDocumento.setCodice(numeroDocumento);
        }
        CodiceDocumento codiceProtocollo = null;
        if (numeroProtocollo != null) {
            codiceProtocollo = new CodiceDocumento();
            codiceProtocollo.setCodice(numeroProtocollo);
        }

        this.areaContabile = new AreaContabileDTO(idAreaContabile, dataDocumento, codiceDocumento, codiceProtocollo,
                totale, idEntita, codiceEntita, ragioneSocialeEntita, numeroPaginaGiornale, notaCreditoEnable);
        this.filler = Boolean.FALSE;
        this.consideraPerLiquidazione = true;
    }

    /**
     * Costruttore per le righe ventilazione per il report liquidazione iva.
     * 
     * @param idCodiceIva
     *            idCodiceIva
     * @param codiceIva
     *            codiceIva
     * @param descrizioneRegistro
     *            descrizioneRegistro
     * @param importoTotale
     *            importoTotale
     * @param percentualePesoVentilazione
     *            percentualePesoVentilazione
     * @param importoPesoVentilazione
     *            importoPesoVentilazione
     */
    public TotaliCodiceIvaDTO(final Integer idCodiceIva, final String codiceIva, final String descrizioneRegistro,
            final BigDecimal importoTotale, final BigDecimal percentualePesoVentilazione,
            final BigDecimal importoPesoVentilazione) {
        super();
        this.idCodiceIva = idCodiceIva;
        this.codiceIva = codiceIva;
        this.descrizioneRegistro = descrizioneRegistro;
        this.totale = importoTotale;
        this.percentualePesoVentilazione = percentualePesoVentilazione;
        this.importoPesoVentilazione = importoPesoVentilazione;
        this.filler = Boolean.FALSE;
    }

    /**
     * Costruttore.
     * 
     * @param codiceIva
     *            codiceIva
     * @param descrizioneRegistro
     *            descrizioneRegistro
     * @param totale
     *            totale
     */
    public TotaliCodiceIvaDTO(final String codiceIva, final String descrizioneRegistro, final BigDecimal totale) {
        super();
        this.codiceIva = codiceIva;
        this.descrizioneRegistro = descrizioneRegistro;
        this.totale = totale;
        this.filler = Boolean.FALSE;
    }

    /**
     * Se ho un registro iva acquisto devo calcolare imposta e imponibile detraibile e indetraibile.
     * 
     * @param registroIvaParam
     *            registroIva per sapere se valorizzare valori detraibile e indetraibile
     */
    public void calcolaValoriDetraibili(RegistroIva registroIvaParam) {
        if (registroIvaParam != null && registroIvaParam.getId() != null
                && registroIvaParam.getTipoRegistro().equals(TipoRegistro.ACQUISTO)) {
            BigDecimal imponibileIndetraibileTmp = getImponibile().multiply(getPercIndetraibilita())
                    .divide(Importo.HUNDRED).setScale(2, RoundingMode.HALF_UP);
            BigDecimal imponibileDetraibileTmp = getImponibile().subtract(imponibileIndetraibileTmp);

            BigDecimal impostaIndetraibileTmp = getImposta().multiply(getPercIndetraibilita()).divide(Importo.HUNDRED)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal impostaDetraibileTmp = getImposta().subtract(impostaIndetraibileTmp);

            setImponibileDetraibile(imponibileDetraibileTmp);
            setImponibileIndetraibile(imponibileIndetraibileTmp);
            setImpostaDetraibile(impostaDetraibileTmp);
            setImpostaIndetraibile(impostaIndetraibileTmp);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getCodiceIva() == null) {
            return false;
        }
        if (!(obj instanceof TotaliCodiceIvaDTO)) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return (this.getCodiceIva().equals(((TotaliCodiceIvaDTO) obj).getCodiceIva()));
    }

    /**
     * @return the areaContabile
     */
    public AreaContabileDTO getAreaContabile() {
        return areaContabile;
    }

    /**
     * @return the codice
     */
    public String getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return the descrizioneRegistro
     */
    public String getDescrizioneRegistro() {
        return descrizioneRegistro;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the idCodiceIva
     */
    public Integer getIdCodiceIva() {
        return idCodiceIva;
    }

    /**
     * @return the imponibile
     */
    public BigDecimal getImponibile() {
        return imponibile;
    }

    /**
     * @return the imponibileDetraibile
     */
    public BigDecimal getImponibileDetraibile() {
        return imponibileDetraibile;
    }

    /**
     * @return the imponibileIndetraibile
     */
    public BigDecimal getImponibileIndetraibile() {
        return imponibileIndetraibile;
    }

    /**
     * @return the importoPesoVentilazione
     */
    public BigDecimal getImportoPesoVentilazione() {
        return importoPesoVentilazione;
    }

    /**
     * @return the imposta
     */
    public BigDecimal getImposta() {
        return imposta;
    }

    /**
     * @return the impostaDetraibile
     */
    public BigDecimal getImpostaDetraibile() {
        return impostaDetraibile;
    }

    /**
     * @return the impostaIndetraibile
     */
    public BigDecimal getImpostaIndetraibile() {
        return impostaIndetraibile;
    }

    /**
     * @return the ordinamento
     */
    public Long getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return the percApplicazione
     */
    public BigDecimal getPercApplicazione() {
        return percApplicazione;
    }

    /**
     * @return the percentualePesoVentilazione
     */
    public BigDecimal getPercentualePesoVentilazione() {
        return percentualePesoVentilazione;
    }

    /**
     * @return the percIndetraibilita
     */
    public BigDecimal getPercIndetraibilita() {
        return percIndetraibilita;
    }

    /**
     * @return the registroIva
     */
    public RegistroIva getRegistroIva() {
        return registroIva;
    }

    /**
     * @return the totale
     */
    public BigDecimal getTotale() {
        return totale;
    }

    /**
     * @return the totAliquotaNota
     */
    public BigDecimal getTotAliquotaNota() {
        return totAliquotaNota;
    }

    /**
     * @return the totDaFattura
     */
    public BigDecimal getTotDaFattura() {
        return totDaFattura;
    }

    /**
     * @return the totDaVentilazione
     */
    public BigDecimal getTotDaVentilazione() {
        return totDaVentilazione;
    }

    /**
     * @return the totRicevutaFiscale
     */
    public BigDecimal getTotRicevutaFiscale() {
        return totRicevutaFiscale;
    }

    @Override
    public int hashCode() {
        if (codiceIva != null) {
            String hashStr = this.getClass().getName() + ":" + codiceIva;
            return hashStr.hashCode();
        }
        return super.hashCode();
    }

    /**
     * @return consideraPerLiquidazione
     */
    public boolean isConsideraPerLiquidazione() {
        return consideraPerLiquidazione;
    }

    /**
     * @return the filler
     */
    public boolean isFiller() {
        return filler;
    }

    /**
     * @return the splitPayment
     */
    public boolean isSplitPayment() {
        return splitPayment;
    }

    /**
     * @param areaContabile
     *            the areaContabile to set
     */
    public void setAreaContabile(AreaContabileDTO areaContabile) {
        this.areaContabile = areaContabile;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(String codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param consideraPerLiquidazione
     *            the consideraPerLiquidazione to set
     */
    public void setConsideraPerLiquidazione(boolean consideraPerLiquidazione) {
        this.consideraPerLiquidazione = consideraPerLiquidazione;
    }

    /**
     * @param descrizioneRegistro
     *            the descrizioneRegistro to set
     */
    public void setDescrizioneRegistro(String descrizioneRegistro) {
        this.descrizioneRegistro = descrizioneRegistro;
    }

    /**
     * @param filler
     *            the filler to set
     */
    public void setFiller(boolean filler) {
        this.filler = filler;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param idCodiceIva
     *            the idCodiceIva to set
     */
    public void setIdCodiceIva(Integer idCodiceIva) {
        this.idCodiceIva = idCodiceIva;
    }

    /**
     * @param imponibile
     *            the imponibile to set
     */
    public void setImponibile(BigDecimal imponibile) {
        this.imponibile = imponibile;
    }

    /**
     * @param imponibileDetraibile
     *            the imponibileDetraibile to set
     */
    public void setImponibileDetraibile(BigDecimal imponibileDetraibile) {
        this.imponibileDetraibile = imponibileDetraibile;
    }

    /**
     * @param imponibileIndetraibile
     *            the imponibileIndetraibile to set
     */
    public void setImponibileIndetraibile(BigDecimal imponibileIndetraibile) {
        this.imponibileIndetraibile = imponibileIndetraibile;
    }

    /**
     * @param importoPesoVentilazione
     *            the importoPesoVentilazione to set
     */
    public void setImportoPesoVentilazione(BigDecimal importoPesoVentilazione) {
        this.importoPesoVentilazione = importoPesoVentilazione;
    }

    /**
     * @param imposta
     *            the imposta to set
     */
    public void setImposta(BigDecimal imposta) {
        this.imposta = imposta;
    }

    /**
     * @param impostaDetraibile
     *            the impostaDetraibile to set
     */
    public void setImpostaDetraibile(BigDecimal impostaDetraibile) {
        this.impostaDetraibile = impostaDetraibile;
    }

    /**
     * @param impostaIndetraibile
     *            the impostaIndetraibile to set
     */
    public void setImpostaIndetraibile(BigDecimal impostaIndetraibile) {
        this.impostaIndetraibile = impostaIndetraibile;
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(Long ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param percApplicazione
     *            the percApplicazione to set
     */
    public void setPercApplicazione(BigDecimal percApplicazione) {
        this.percApplicazione = percApplicazione;
    }

    /**
     * @param percentualePesoVentilazione
     *            the percentualePesoVentilazione to set
     */
    public void setPercentualePesoVentilazione(BigDecimal percentualePesoVentilazione) {
        this.percentualePesoVentilazione = percentualePesoVentilazione;
    }

    /**
     * @param percIndetraibilita
     *            the percIndetraibilita to set
     */
    public void setPercIndetraibilita(BigDecimal percIndetraibilita) {
        this.percIndetraibilita = percIndetraibilita;
    }

    /**
     * @param registroIva
     *            the registroIva to set
     */
    public void setRegistroIva(RegistroIva registroIva) {
        this.registroIva = registroIva;
    }

    /**
     * @param splitPayment
     *            the splitPayment to set
     */
    public void setSplitPayment(boolean splitPayment) {
        this.splitPayment = splitPayment;
    }

    /**
     * @param totale
     *            the totale to set
     */
    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    /**
     * @param totAliquotaNota
     *            the totAliquotaNota to set
     */
    public void setTotAliquotaNota(BigDecimal totAliquotaNota) {
        this.totAliquotaNota = totAliquotaNota;
    }

    /**
     * @param totDaFattura
     *            the totDaFattura to set
     */
    public void setTotDaFattura(BigDecimal totDaFattura) {
        this.totDaFattura = totDaFattura;
    }

    /**
     * @param totDaVentilazione
     *            the totDaVentilazione to set
     */
    public void setTotDaVentilazione(BigDecimal totDaVentilazione) {
        this.totDaVentilazione = totDaVentilazione;
    }

    /**
     * @param totRicevutaFiscale
     *            the totRicevutaFiscale to set
     */
    public void setTotRicevutaFiscale(BigDecimal totRicevutaFiscale) {
        this.totRicevutaFiscale = totRicevutaFiscale;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TotaliCodiceIvaDTO[");
        buffer.append(" id = ").append(id);
        buffer.append(" imponibile = ").append(imponibile);
        buffer.append(" imposta = ").append(imposta);
        buffer.append(" idCodiceIva = ").append(idCodiceIva);
        buffer.append(" codiceIva = ").append(codiceIva);
        buffer.append(" descrizioneRegistro = ").append(descrizioneRegistro);
        buffer.append(" percApplicazione = ").append(percApplicazione);
        buffer.append(" percIndetraibilita = ").append(percIndetraibilita);
        buffer.append(" areaContabile = ").append(areaContabile != null ? areaContabile.getId() : null);
        buffer.append(" totDaFattura = ").append(totDaFattura);
        buffer.append(" totAliquotaNota = ").append(totAliquotaNota);
        buffer.append(" totRicevutaFiscale = ").append(totRicevutaFiscale);
        buffer.append(" totDaVentilazione = ").append(totDaVentilazione);
        buffer.append("]");
        return buffer.toString();
    }

}
