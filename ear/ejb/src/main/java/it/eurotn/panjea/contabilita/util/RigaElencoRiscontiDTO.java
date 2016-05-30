package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

public class RigaElencoRiscontiDTO implements Serializable {

    private static final long serialVersionUID = -4270293844651444959L;

    // RigaRateoRisconto
    private Integer idRateoRisconto;
    private BigDecimal importo;
    private Date inizio;
    private Date fine;
    private Integer giorni;
    private String nota;

    // RigaRateoRiscontoAnno
    private int anno;
    private BigDecimal importoAnno;
    private int giorniAnno;
    private BigDecimal importoSuccessivoAnno;
    private int giorniSuccessivoAnno;

    private Documento documentoAnno;
    private Integer idDocumentoAnno;
    private Date dataDocumentoAnno;
    private CodiceDocumento codiceDocumentoAnno;
    private String tipoDocumentoCodiceAnno;

    /**
     * @return the anno
     */
    public int getAnno() {
        return anno;
    }

    /**
     * @return the documento
     */
    public Documento getDocumentoAnno() {
        if (documentoAnno == null && idDocumentoAnno != null) {
            documentoAnno = new Documento();
            documentoAnno.setId(idDocumentoAnno);
            documentoAnno.setDataDocumento(dataDocumentoAnno);
            documentoAnno.setCodice(codiceDocumentoAnno);
            documentoAnno.getTipoDocumento().setCodice(tipoDocumentoCodiceAnno);
        }
        return documentoAnno;
    }

    /**
     * @return the fine
     */
    public Date getFine() {
        return fine;
    }

    /**
     * @return the giorni
     */
    public Integer getGiorni() {
        if (giorni == null) {
            Calendar calInizio = Calendar.getInstance();
            calInizio.setTime(inizio);

            Calendar calFine = Calendar.getInstance();
            calFine.setTime(fine);
            calFine.set(Calendar.HOUR, 24);
            calFine.set(Calendar.HOUR_OF_DAY, 24);
            calFine.clear(Calendar.MINUTE);
            calFine.clear(Calendar.SECOND);
            calFine.clear(Calendar.MILLISECOND);

            giorni = Days.daysBetween(new DateTime(calInizio), new DateTime(calFine)).getDays();
        }

        return giorni;
    }

    /**
     * @return the giorniAnno
     */
    public int getGiorniAnno() {
        return giorniAnno;
    }

    /**
     * @return the giorniSuccessivoAnno
     */
    public int getGiorniSuccessivoAnno() {
        return giorniSuccessivoAnno;
    }

    /**
     * @return the idRateoRisconto
     */
    public Integer getIdRateoRisconto() {
        return idRateoRisconto;
    }

    /**
     * @return the importo
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * @return the importoAnno
     */
    public BigDecimal getImportoAnno() {
        return importoAnno;
    }

    /**
     * @return the importoSuccessivoAnno
     */
    public BigDecimal getImportoSuccessivoAnno() {
        return importoSuccessivoAnno;
    }

    /**
     * @return the inizio
     */
    public Date getInizio() {
        return inizio;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }

    /**
     * @param codiceDocumentoAnno
     *            the codiceDocumentoAnno to set
     */
    public void setCodiceDocumentoAnno(CodiceDocumento codiceDocumentoAnno) {
        this.codiceDocumentoAnno = codiceDocumentoAnno;
    }

    /**
     * @param dataDocumentoAnno
     *            the dataDocumentoAnno to set
     */
    public void setDataDocumentoAnno(Date dataDocumentoAnno) {
        this.dataDocumentoAnno = dataDocumentoAnno;
    }

    /**
     * @param fine
     *            the fine to set
     */
    public void setFine(Date fine) {
        this.fine = fine;
    }

    /**
     * @param giorniAnno
     *            the giorniAnno to set
     */
    public void setGiorniAnno(int giorniAnno) {
        this.giorniAnno = giorniAnno;
    }

    /**
     * @param giorniSuccessivoAnno
     *            the giorniSuccessivoAnno to set
     */
    public void setGiorniSuccessivoAnno(int giorniSuccessivoAnno) {
        this.giorniSuccessivoAnno = giorniSuccessivoAnno;
    }

    /**
     * @param idDocumentoAnno
     *            the idDocumentoAnno to set
     */
    public void setIdDocumentoAnno(Integer idDocumentoAnno) {
        this.idDocumentoAnno = idDocumentoAnno;
    }

    /**
     * @param idRateoRisconto
     *            the idRateoRisconto to set
     */
    public void setIdRateoRisconto(Integer idRateoRisconto) {
        this.idRateoRisconto = idRateoRisconto;
    }

    /**
     * @param importo
     *            the importo to set
     */
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    /**
     * @param importoAnno
     *            the importoAnno to set
     */
    public void setImportoAnno(BigDecimal importoAnno) {
        this.importoAnno = importoAnno;
    }

    /**
     * @param importoSuccessivoAnno
     *            the importoSuccessivoAnno to set
     */
    public void setImportoSuccessivoAnno(BigDecimal importoSuccessivoAnno) {
        this.importoSuccessivoAnno = importoSuccessivoAnno;
    }

    /**
     * @param inizio
     *            the inizio to set
     */
    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param tipoDocumentoCodiceAnno
     *            the tipoDocumentoCodiceAnno to set
     */
    public void setTipoDocumentoCodiceAnno(String tipoDocumentoCodiceAnno) {
        this.tipoDocumentoCodiceAnno = tipoDocumentoCodiceAnno;
    }

}
