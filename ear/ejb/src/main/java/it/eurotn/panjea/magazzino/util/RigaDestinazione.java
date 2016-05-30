package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.Date;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;

public class RigaDestinazione implements Serializable {

    private static final long serialVersionUID = -7620105949679507259L;

    private boolean canDelete;
    private Integer idTipoDocumento;

    private String codiceTipoDocumento;

    private String descrizioneTipoDocumento;
    private CodiceDocumento numeroDocumento;

    private Date dataDocumento;
    private Integer idDocumento;

    private Documento documento;
    private Integer idArticolo;
    private String codiceArticolo;
    private String descrizioneArticolo;

    private Integer numeroDecimaliQta;
    private ArticoloLite articolo;
    private Double quantita;
    private Integer idRiga;
    private Integer idAreaMagazzino;

    private AreaMagazzino areaMagazzino;

    private Integer idAreaOrdine;
    private AreaOrdine areaOrdine;

    private Integer idAreaPreventivo;
    private AreaPreventivo areaPreventivo;

    /**
     * Costruttore.
     *
     */
    public RigaDestinazione() {
        super();
        canDelete = false;
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
        RigaDestinazione other = (RigaDestinazione) obj;
        if (idRiga == null) {
            if (other.idRiga != null) {
                return false;
            }
        } else if (!idRiga.equals(other.idRiga)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return Iareadocumento collegata
     */
    public IAreaDocumento getAreaDocumento() {
        IAreaDocumento areaResult = null;
        if (getAreaMagazzino() != null) {
            areaResult = getAreaMagazzino();
        } else if (getAreaOrdine() != null) {
            areaResult = getAreaOrdine();
        } else if (getAreaPreventivo() != null) {
            areaResult = getAreaPreventivo();
        }
        return areaResult;
    }

    /**
     * @return the areaMagazzino
     */
    public AreaMagazzino getAreaMagazzino() {
        if (areaMagazzino == null && idAreaMagazzino != null) {
            areaMagazzino = new AreaMagazzino();
            areaMagazzino.setId(idAreaMagazzino);
        }
        return areaMagazzino;
    }

    /**
     * @return Returns the areaOrdine.
     */
    public AreaOrdine getAreaOrdine() {
        if (areaOrdine == null && idAreaOrdine != null) {
            areaOrdine = new AreaOrdine();
            areaOrdine.setId(idAreaOrdine);
        }
        return areaOrdine;
    }

    /**
     * @return Returns the areaPreventivo.
     */
    public AreaPreventivo getAreaPreventivo() {
        if (areaPreventivo == null && idAreaPreventivo != null) {
            areaPreventivo = new AreaPreventivo();
            areaPreventivo.setId(idAreaPreventivo);
        }
        return areaPreventivo;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        if (articolo == null) {
            articolo = new ArticoloLite();
            articolo.setId(idArticolo);
            articolo.setCodice(codiceArticolo);
            articolo.setDescrizione(descrizioneArticolo);
            articolo.setNumeroDecimaliQta(numeroDecimaliQta);
        }

        return articolo;
    }

    /**
     * @return the documento
     */
    public Documento getDocumento() {
        if (documento == null) {
            documento = new Documento();
            documento.setId(idDocumento);
            documento.setDataDocumento(dataDocumento);
            documento.setCodice(numeroDocumento);
            documento.getTipoDocumento().setCodice(codiceTipoDocumento);
            documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
            documento.getTipoDocumento().setId(idTipoDocumento);
        }

        return documento;
    }

    /**
     * @return Returns the idAreaOrdine.
     */
    public Integer getIdAreaOrdine() {
        return idAreaOrdine;
    }

    /**
     * @return the idAreaPreventivo
     */
    public Integer getIdAreaPreventivo() {
        return idAreaPreventivo;
    }

    /**
     * @return the idRiga
     */
    public Integer getIdRiga() {
        return idRiga;
    }

    /**
     * @return the quantita
     */
    public Double getQuantita() {
        return quantita;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idRiga == null) ? 0 : idRiga.hashCode());
        return result;
    }

    /**
     * @return Returns the canDelete.
     */
    public boolean isCanDelete() {
        return canDelete;
    }

    /**
     * @param canDelete
     *            The canDelete to set.
     */
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public void setCanDeleteInt(Integer canDeleteInt) {
        canDelete = canDeleteInt == 1;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * @param codiceTipoDocumento
     *            the codiceTipoDocumento to set
     */
    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
        this.codiceTipoDocumento = codiceTipoDocumento;
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    /**
     * @param descrizioneTipoDocumento
     *            the descrizioneTipoDocumento to set
     */
    public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
        this.descrizioneTipoDocumento = descrizioneTipoDocumento;
    }

    /**
     * @param idAreaMagazzino
     *            the idAreaMagazzino to set
     */
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        this.idAreaMagazzino = idAreaMagazzino;
    }

    /**
     * @param idAreaOrdine
     *            The idAreaOrdine to set.
     */
    public void setIdAreaOrdine(Integer idAreaOrdine) {
        this.idAreaOrdine = idAreaOrdine;
    }

    /**
     * @param idAreaPreventivo
     *            the idAreaPreventivo to set
     */
    public void setIdAreaPreventivo(Integer idAreaPreventivo) {
        this.idAreaPreventivo = idAreaPreventivo;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idDocumento
     *            the idDocumento to set
     */
    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    /**
     * @param idRiga
     *            the idRiga to set
     */
    public void setIdRiga(Integer idRiga) {
        this.idRiga = idRiga;
    }

    /**
     * @param idTipoDocumento
     *            the idTipoDocumento to set
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }

    /**
     * @param numeroDocumento
     *            the numeroDocumento to set
     */
    public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @param quantita
     *            the quantita to set
     */
    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }
}
