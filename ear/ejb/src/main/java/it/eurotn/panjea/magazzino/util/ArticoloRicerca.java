package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.util.PanjeaEJBUtil;

public class ArticoloRicerca implements Serializable, Comparable<ArticoloRicerca> {

    private static final Logger LOGGER = Logger.getLogger(ArticoloRicerca.class);

    private static final long serialVersionUID = -4555284003846959990L;

    private int hashCode = Integer.MIN_VALUE;

    private Integer id;
    private Integer version;
    private Integer numeroDecimaliQta;
    private Integer numeroDecimaliPrezzo;

    private Double giacenza;

    private String codice;
    private String codiceInterno;
    private String codiceEntita;
    private String descrizione;
    private String descrizioneLinguaAziendale;
    private String barCode;
    private String codiciAttributi;
    private String valoriAttributi;

    private boolean abilitato;
    private boolean padre;
    private boolean distinta;

    private TipoLotto tipoLotto;
    private ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo;
    private UnitaMisura unitaMisura;

    private BigDecimal percApplicazioneCodiceIva;

    /**
     * Costruttore.
     */
    public ArticoloRicerca() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param articoloLite
     *            articolo ite
     */
    public ArticoloRicerca(final ArticoloLite articoloLite) {
        super();
        PanjeaEJBUtil.copyProperties(this, articoloLite);
    }

    @Override
    public int compareTo(ArticoloRicerca other) {
        if (other == null) {
            return -1;
        }
        return this.getId().compareTo(other.getId());
    }

    /**
     * @return il proxy di Articolo con le proprietà presenti in this.
     */
    public Articolo createProxyArticolo() {
        Articolo articolo = new Articolo();
        PanjeaEJBUtil.copyProperties(articolo, this);
        return articolo;
    }

    /**
     * @return il proxy di ArticoloLite con le proprietà presenti in this.
     */
    public ArticoloLite createProxyArticoloLite() {
        ArticoloLite articoloLite = new ArticoloLite();
        PanjeaEJBUtil.copyProperties(articoloLite, this);
        if (articoloLite.getId() == 0) {
            articoloLite.setId(null);
        }
        return articoloLite;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getId() == null) {
            return false;
        }
        return this.getId().equals(((ArticoloRicerca) obj).getId());
    }

    /**
     * @return the barcode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the codiceEntita
     */
    public String getCodiceEntita() {
        return codiceEntita;
    }

    /**
     * @return the codiceInterno
     */
    public String getCodiceInterno() {
        return codiceInterno;
    }

    /**
     * @return the codiciAttributi
     */
    public String getCodiciAttributi() {
        return codiciAttributi;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the descrizioneLinguaAziendale
     */
    public String getDescrizioneLinguaAziendale() {
        return descrizioneLinguaAziendale;
    }

    /**
     * @return the giacenza
     */
    public Double getGiacenza() {
        return giacenza;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDecimaliQta
     */
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     * @return the percApplicazioneCodiceIva
     */
    public BigDecimal getPercApplicazioneCodiceIva() {
        return percApplicazioneCodiceIva;
    }

    /**
     * @return the provenienzaPrezzoArticolo
     */
    public ProvenienzaPrezzoArticolo getProvenienzaPrezzoArticolo() {
        return provenienzaPrezzoArticolo;
    }

    /**
     * @return the tipoLotto
     */
    public TipoLotto getTipoLotto() {
        return tipoLotto;
    }

    /**
     * @return the unitaMisura
     */
    public UnitaMisura getUnitaMisura() {
        return unitaMisura;
    }

    /**
     * @return the valoriAttributi
     */
    public String getValoriAttributi() {
        return valoriAttributi;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            String hashStr = this.getClass().getName();
            try {
                if (null != this.getId()) {
                    hashStr += ":" + this.getId().hashCode();
                }
            } catch (Exception ex) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> ");
                }
            }
            this.hashCode = hashStr.hashCode();
        }
        return this.hashCode;
    }

    /**
     * @return the abilitato
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @return the distinta
     */
    public boolean isDistinta() {
        return distinta;
    }

    /**
     * @return the padre
     */
    public boolean isPadre() {
        return padre;
    }

    /**
     * @param abilitato
     *            the abilitato to set
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param barCode
     *            the barcode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(String codiceEntita) {
        this.codiceEntita = codiceEntita;
    }

    /**
     * @param codiceInterno
     *            the codiceInterno to set
     */
    public void setCodiceInterno(String codiceInterno) {
        this.codiceInterno = codiceInterno;
    }

    /**
     * @param codiciAttributi
     *            the codiciAttributi to set
     */
    public void setCodiciAttributi(String codiciAttributi) {
        this.codiciAttributi = codiciAttributi;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param descrizioneLinguaAziendale
     *            the descrizioneLinguaAziendale to set
     */
    public void setDescrizioneLinguaAziendale(String descrizioneLinguaAziendale) {
        this.descrizioneLinguaAziendale = descrizioneLinguaAziendale;
    }

    /**
     * @param distinta
     *            the distinta to set
     */
    public void setDistinta(boolean distinta) {
        this.distinta = distinta;
    }

    /**
     * @param giacenza
     *            the giacenza to set
     */
    public void setGiacenza(Double giacenza) {
        this.giacenza = giacenza;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }

    /**
     * @param padre
     *            the padre to set
     */
    public void setPadre(boolean padre) {
        this.padre = padre;
    }

    /**
     * @param percApplicazioneCodiceIva
     *            the percApplicazioneCodiceIva to set
     */
    public void setPercApplicazioneCodiceIva(BigDecimal percApplicazioneCodiceIva) {
        this.percApplicazioneCodiceIva = percApplicazioneCodiceIva;
    }

    /**
     * @param provenienzaPrezzoArticolo
     *            the provenienzaPrezzoArticolo to set
     */
    public void setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo) {
        this.provenienzaPrezzoArticolo = provenienzaPrezzoArticolo;
    }

    /**
     * @param tipoLotto
     *            the tipoLotto to set
     */
    public void setTipoLotto(TipoLotto tipoLotto) {
        this.tipoLotto = tipoLotto;
    }

    /**
     * @param unitaMisura
     *            the unitaMisura to set
     */
    public void setUnitaMisura(UnitaMisura unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    /**
     * @param valoriAttributi
     *            the valoriAttributi to set
     */
    public void setValoriAttributi(String valoriAttributi) {
        this.valoriAttributi = valoriAttributi;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     *
     * @return a <code>String</code> representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("ArticoloRicerca[ ").append(" codice = ").append(this.codice);
        retValue.append(" descrizione = ").append(this.descrizione);
        retValue.append(" ]");
        return retValue.toString();
    }

}
