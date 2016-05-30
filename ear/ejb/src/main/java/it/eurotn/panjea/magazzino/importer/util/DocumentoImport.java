/**
 *
 */
package it.eurotn.panjea.magazzino.importer.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

/**
 * @author fattazzo
 *
 */
public class DocumentoImport extends AbstractValidationObjectImport implements Serializable {

    private static final long serialVersionUID = 8525916347426404864L;

    private String numeroDocumento;

    private Date dataDocumento;

    private Integer idEntita;
    private Integer codiceEntita;

    private Integer idSede;
    private String codiceSede;

    private BigDecimal totaleDocumento;

    private BigDecimal totaleImpostaDocumento;

    private TipoAreaMagazzino tipoAreaMagazzino;

    private BigDecimal pesoNetto;

    private BigDecimal pesoLordo;

    private Integer pallet;

    private Integer numeroColli;

    private BigDecimal volume;

    private DepositoLite depositoOrigine;

    private DepositoLite depositoDestinazione;

    private List<RigaImport> righe;

    {
        righe = new ArrayList<RigaImport>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DocumentoImport other = (DocumentoImport) obj;
        if (codiceEntita == null) {
            if (other.codiceEntita != null) {
                return false;
            }
        } else if (!codiceEntita.equals(other.codiceEntita)) {
            return false;
        }
        if (dataDocumento == null) {
            if (other.dataDocumento != null) {
                return false;
            }
        } else if (!dataDocumento.equals(other.dataDocumento)) {
            return false;
        }
        if (numeroDocumento == null) {
            if (other.numeroDocumento != null) {
                return false;
            }
        } else if (!numeroDocumento.equals(other.numeroDocumento)) {
            return false;
        }
        if (tipoAreaMagazzino == null) {
            if (other.tipoAreaMagazzino != null) {
                return false;
            }
        } else if (!tipoAreaMagazzino.equals(other.tipoAreaMagazzino)) {
            return false;
        }
        return true;
    }

    /**
     * @return the codiceEntita
     */
    public Integer getCodiceEntita() {
        return codiceEntita;
    }

    /**
     * @return the codiceSede
     */
    public String getCodiceSede() {
        return codiceSede;
    }

    /**
     * @return the dataDocumento
     */
    public Date getDataDocumento() {
        return dataDocumento;
    }

    /**
     * @return Returns the depositoDestinazione.
     */
    public DepositoLite getDepositoDestinazione() {
        return depositoDestinazione;
    }

    /**
     * @return Returns the depositoOrigine.
     */
    public DepositoLite getDepositoOrigine() {
        return depositoOrigine;
    }

    /**
     * @return the idEntita
     */
    public Integer getIdEntita() {
        return idEntita;
    }

    /**
     * @return the idSede
     */
    public Integer getIdSede() {
        return idSede;
    }

    /**
     * @return the numeroColli
     */
    public Integer getNumeroColli() {
        return numeroColli;
    }

    /**
     * @return the numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @return the pallet
     */
    public Integer getPallet() {
        return pallet;
    }

    /**
     * @return the pesoLordo
     */
    public BigDecimal getPesoLordo() {
        return pesoLordo;
    }

    /**
     * @return the pesoNetto
     */
    public BigDecimal getPesoNetto() {
        return pesoNetto;
    }

    /**
     * @return the righe
     */
    public List<RigaImport> getRighe() {
        return righe;
    }

    /**
     * @return the tipoAreaMagazzino
     */
    public TipoAreaMagazzino getTipoAreaMagazzino() {
        return tipoAreaMagazzino;
    }

    /**
     * @return the totaleDocumento
     */
    public BigDecimal getTotaleDocumento() {
        return totaleDocumento;
    }

    /**
     * @return the totaleImpostaDocumento
     */
    public BigDecimal getTotaleImpostaDocumento() {
        return totaleImpostaDocumento;
    }

    /**
     * @return the volume
     */
    public BigDecimal getVolume() {
        return volume;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codiceEntita == null) ? 0 : codiceEntita.hashCode());
        result = prime * result + ((dataDocumento == null) ? 0 : dataDocumento.hashCode());
        result = prime * result + ((numeroDocumento == null) ? 0 : numeroDocumento.hashCode());
        result = prime * result + ((tipoAreaMagazzino == null) ? 0 : tipoAreaMagazzino.hashCode());
        return result;
    }

    @Override
    public boolean isValid() {

        // un documento è valido se anche le sue righe sono tutte valide
        boolean validDoc = super.isValid();
        for (RigaImport riga : getRighe()) {
            validDoc = validDoc && riga.isValid();
        }

        return validDoc;
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(Integer codiceEntita) {
        this.codiceEntita = codiceEntita;
    }

    /**
     * @param codiceSede
     *            the codiceSede to set
     */
    public void setCodiceSede(String codiceSede) {
        this.codiceSede = codiceSede;
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param depositoDestinazione
     *            The depositoDestinazione to set.
     */
    public void setDepositoDestinazione(DepositoLite depositoDestinazione) {
        this.depositoDestinazione = depositoDestinazione;
    }

    /**
     * @param depositoOrigine
     *            The depositoOrigine to set.
     */
    public void setDepositoOrigine(DepositoLite depositoOrigine) {
        this.depositoOrigine = depositoOrigine;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    /**
     * @param idSede
     *            the idSede to set
     */
    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    /**
     * @param numeroColli
     *            the numeroColli to set
     */
    public void setNumeroColli(Integer numeroColli) {
        this.numeroColli = numeroColli;
    }

    /**
     * @param numeroDocumento
     *            the numeroDocumento to set
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @param pallet
     *            the pallet to set
     */
    public void setPallet(Integer pallet) {
        this.pallet = pallet;
    }

    /**
     * @param pesoLordo
     *            the pesoLordo to set
     */
    public void setPesoLordo(BigDecimal pesoLordo) {
        this.pesoLordo = pesoLordo;
    }

    /**
     * @param pesoNetto
     *            the pesoNetto to set
     */
    public void setPesoNetto(BigDecimal pesoNetto) {
        this.pesoNetto = pesoNetto;
    }

    /**
     * @param tipoAreaMagazzino
     *            the tipoAreaMagazzino to set
     */
    public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        this.tipoAreaMagazzino = tipoAreaMagazzino;
    }

    /**
     * @param totaleDocumento
     *            the totaleDocumento to set
     */
    public void setTotaleDocumento(BigDecimal totaleDocumento) {
        this.totaleDocumento = totaleDocumento;
    }

    /**
     * @param totaleImpostaDocumento
     *            the totaleImpostaDocumento to set
     */
    public void setTotaleImpostaDocumento(BigDecimal totaleImpostaDocumento) {
        this.totaleImpostaDocumento = totaleImpostaDocumento;
    }

    /**
     * @param volume
     *            the volume to set
     */
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public void valida() {
        Collection<String> validMessage = new ArrayList<String>();

        if (StringUtils.isBlank(getNumeroDocumento())) {
            validMessage.add("Numero documento non presente");
        }

        if (getDataDocumento() == null) {
            validMessage.add("Data documento non presente");
        }

        if (getTipoAreaMagazzino() == null) {
            validMessage.add("Tipo documento non presente");
        }
        if (getIdEntita() == null) {
            if (getCodiceEntita() == null) {
                validMessage.add("Entità non presente");
            } else {
                validMessage.add("Entità non valida");
            }
        }

        if (getIdSede() == null) {
            if (getCodiceEntita() == null) {
                validMessage.add("Sede entità non presente");
            } else {
                validMessage.add("Sede entità non valida");
            }
        }

        if (getRighe().isEmpty()) {
            validMessage.add("Righe documento non presenti");
        }

        setValidationMessage(StringUtils.join(validMessage.iterator(), "#"));

        for (RigaImport riga : getRighe()) {
            riga.valida();
        }
    }

}
