/**
 *
 */
package it.eurotn.panjea.magazzino.importer.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.magazzino.domain.Sconto;

/**
 * @author fattazzo
 *
 */
public class RigaImport extends AbstractValidationObjectImport implements Serializable {

    private static final long serialVersionUID = 4125013238100996330L;

    private int numeroRiga;

    private Integer idArticolo;
    private String codiceArticolo;
    private String codiceArticoloEntita;

    private String codiceLotto;

    private Date dataScadenzaLotto;

    private Double qta;

    private BigDecimal prezzoUnitario;
    private BigDecimal prezzoNetto;

    private BigDecimal sconto1;
    private BigDecimal sconto2;
    private BigDecimal sconto3;
    private BigDecimal sconto4;

    private Sconto sconto;

    boolean omaggio;

    /**
     * @return the codiceArticolo
     */
    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    /**
     * @return the codiceArticoloEntita
     */
    public String getCodiceArticoloEntita() {
        return codiceArticoloEntita;
    }

    /**
     * @return the codiceLotto
     */
    public String getCodiceLotto() {
        return codiceLotto;
    }

    /**
     * @return the dataScadenzaLotto
     */
    public Date getDataScadenzaLotto() {
        return dataScadenzaLotto;
    }

    /**
     * @return the idArticolo
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return the numeroRiga
     */
    public int getNumeroRiga() {
        return numeroRiga;
    }

    /**
     * @return the prezzoNetto
     */
    public BigDecimal getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return the prezzoUnitario
     */
    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return the qta
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return the sconto
     */
    public Sconto getSconto() {
        if (sconto == null) {
            sconto = new Sconto(sconto1, sconto2, sconto3, sconto4);
        }

        return sconto;
    }

    /**
     * @return the sconto1
     */
    public BigDecimal getSconto1() {
        return sconto1;
    }

    /**
     * @return the sconto2
     */
    public BigDecimal getSconto2() {
        return sconto2;
    }

    /**
     * @return the sconto3
     */
    public BigDecimal getSconto3() {
        return sconto3;
    }

    /**
     * @return the sconto4
     */
    public BigDecimal getSconto4() {
        return sconto4;
    }

    /**
     * @return Returns the omaggio.
     */
    public boolean isOmaggio() {
        return omaggio;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * @param codiceArticoloEntita
     *            the codiceArticoloEntita to set
     */
    public void setCodiceArticoloEntita(String codiceArticoloEntita) {
        this.codiceArticoloEntita = codiceArticoloEntita;
    }

    /**
     * @param codiceLotto
     *            the codiceLotto to set
     */
    public void setCodiceLotto(String codiceLotto) {
        this.codiceLotto = codiceLotto;
    }

    /**
     * @param dataScadenzaLotto
     *            the dataScadenzaLotto to set
     */
    public void setDataScadenzaLotto(Date dataScadenzaLotto) {
        this.dataScadenzaLotto = dataScadenzaLotto;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param numeroRiga
     *            the numeroRiga to set
     */
    public void setNumeroRiga(int numeroRiga) {
        this.numeroRiga = numeroRiga;
    }

    /**
     * @param omaggio
     *            The omaggio to set.
     */
    public void setOmaggio(boolean omaggio) {
        this.omaggio = omaggio;
    }

    /**
     * @param prezzoNetto
     *            the prezzoNetto to set
     */
    public void setPrezzoNetto(BigDecimal prezzoNetto) {
        this.prezzoNetto = prezzoNetto;
    }

    /**
     * @param prezzoUnitario
     *            the prezzoUnitario to set
     */
    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    /**
     * @param qta
     *            the qta to set
     */
    public void setQta(Double qta) {
        this.qta = qta;
    }

    /**
     * @param sconto1
     *            the sconto1 to set
     */
    public void setSconto1(BigDecimal sconto1) {
        this.sconto1 = sconto1;
        sconto = null;
    }

    /**
     * @param sconto2
     *            the sconto2 to set
     */
    public void setSconto2(BigDecimal sconto2) {
        this.sconto2 = sconto2;
        sconto = null;
    }

    /**
     * @param sconto3
     *            the sconto3 to set
     */
    public void setSconto3(BigDecimal sconto3) {
        this.sconto3 = sconto3;
        sconto = null;
    }

    /**
     * @param sconto4
     *            the sconto4 to set
     */
    public void setSconto4(BigDecimal sconto4) {
        this.sconto4 = sconto4;
        sconto = null;
    }

    /**
     * Valida la riga
     */
    @Override
    public void valida() {
        Collection<String> validMessage = new ArrayList<String>();

        if (getIdArticolo() == null) {
            if (getCodiceArticolo() != null) {
                validMessage.add("Codice articolo non valido");
            } else if (getCodiceArticoloEntita() != null) {
                validMessage.add("Codice articolo entita non valido");
            } else {
                validMessage.add("Articolo non presente");
            }
        }

        if (getQta() == null) {
            validMessage.add("Quantità non valida");
        }

        setValidationMessage(StringUtils.join(validMessage.iterator(), "#"));
    }
}
