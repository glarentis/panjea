package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

/**
 * @author fattazzo
 */
public class ValorizzazioneArticolo implements Serializable {

    private static final long serialVersionUID = 9063841707142133222L;

    private ArticoloLite articolo;

    private Integer codiceFornitoreAbituale;

    private CategoriaLite categoria;

    private DepositoLite deposito;

    private Date dataInventario;

    private Integer inventarioId;

    private Double scorta;

    protected Double qtaInventario = 0.0;

    protected Double giacenza = 0.0;

    protected Double qtaMagazzinoCarico = 0.0;

    protected Double qtaMagazzinoScarico = 0.0;

    protected Double qtaMagazzinoCaricoAltro = 0.0;

    protected Double qtaMagazzinoScaricoAltro = 0.0;

    protected BigDecimal costo = BigDecimal.ZERO;

    protected BigDecimal valore = BigDecimal.ZERO;

    protected BigDecimal importoCarico = BigDecimal.ZERO;

    protected BigDecimal importoScarico = BigDecimal.ZERO;

    protected Integer costoUltimoMovId;

    private String numeroPezzi;

    private String campoLibero;

    private String codiceArticolo;

    private String descrizioneArticolo;

    private Integer idArticolo;

    private Integer idDeposito;

    private String descrizioneDeposito;

    private String codiceDeposito;

    private Integer idCategoria;

    private String codiceCategoria;

    private String descrizioneCategoria;

    /**
     * Costruttore.
     */
    public ValorizzazioneArticolo() {
        super();
        initialize();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        ValorizzazioneArticolo other = (ValorizzazioneArticolo) obj;
        if (idArticolo == null) {
            if (other.getIdArticolo() != null) {
                return false;
            }
        } else if (!idArticolo.equals(other.getIdArticolo())) {
            return false;
        }
        if (idCategoria == null) {
            if (other.idCategoria != null) {
                return false;
            }
        } else if (!idCategoria.equals(other.idCategoria)) {
            return false;
        }
        if (codiceFornitoreAbituale == null) {
            if (other.codiceFornitoreAbituale != null) {
                return false;
            }
        } else if (!codiceFornitoreAbituale.equals(other.codiceFornitoreAbituale)) {
            return false;
        }
        if (costo == null) {
            if (other.costo != null) {
                return false;
            }
        } else if (!costo.equals(other.costo)) {
            return false;
        }
        if (costoUltimoMovId == null) {
            if (other.costoUltimoMovId != null) {
                return false;
            }
        } else if (!costoUltimoMovId.equals(other.costoUltimoMovId)) {
            return false;
        }
        if (dataInventario == null) {
            if (other.dataInventario != null) {
                return false;
            }
        } else if (!dataInventario.equals(other.dataInventario)) {
            return false;
        }
        if (idDeposito == null) {
            if (other.idDeposito != null) {
                return false;
            }
        } else if (!idDeposito.equals(other.idDeposito)) {
            return false;
        }
        if (giacenza == null) {
            if (other.giacenza != null) {
                return false;
            }
        } else if (!giacenza.equals(other.giacenza)) {
            return false;
        }
        if (importoCarico == null) {
            if (other.importoCarico != null) {
                return false;
            }
        } else if (!importoCarico.equals(other.importoCarico)) {
            return false;
        }
        if (importoScarico == null) {
            if (other.importoScarico != null) {
                return false;
            }
        } else if (!importoScarico.equals(other.importoScarico)) {
            return false;
        }
        if (inventarioId == null) {
            if (other.inventarioId != null) {
                return false;
            }
        } else if (!inventarioId.equals(other.inventarioId)) {
            return false;
        }
        if (qtaInventario == null) {
            if (other.qtaInventario != null) {
                return false;
            }
        } else if (!qtaInventario.equals(other.qtaInventario)) {
            return false;
        }
        if (qtaMagazzinoCarico == null) {
            if (other.qtaMagazzinoCarico != null) {
                return false;
            }
        } else if (!qtaMagazzinoCarico.equals(other.qtaMagazzinoCarico)) {
            return false;
        }
        if (qtaMagazzinoCaricoAltro == null) {
            if (other.qtaMagazzinoCaricoAltro != null) {
                return false;
            }
        } else if (!qtaMagazzinoCaricoAltro.equals(other.qtaMagazzinoCaricoAltro)) {
            return false;
        }
        if (qtaMagazzinoScarico == null) {
            if (other.qtaMagazzinoScarico != null) {
                return false;
            }
        } else if (!qtaMagazzinoScarico.equals(other.qtaMagazzinoScarico)) {
            return false;
        }
        if (qtaMagazzinoScaricoAltro == null) {
            if (other.qtaMagazzinoScaricoAltro != null) {
                return false;
            }
        } else if (!qtaMagazzinoScaricoAltro.equals(other.qtaMagazzinoScaricoAltro)) {
            return false;
        }
        if (valore == null) {
            if (other.valore != null) {
                return false;
            }
        } else if (!valore.equals(other.valore)) {
            return false;
        }
        return true;
    }

    /**
     * @return the articolo
     * @uml.property name="articolo"
     */
    public ArticoloLite getArticolo() {
        if (articolo == null) {
            articolo = new ArticoloLite();
            articolo.setCampoLibero(campoLibero);
            articolo.setCodice(codiceArticolo);
            articolo.setDescrizione(descrizioneArticolo);
            articolo.setId(idArticolo);
        }
        return articolo;
    }

    /**
     * @return the categoria
     * @uml.property name="categoria"
     */
    public CategoriaLite getCategoria() {
        if (categoria == null) {
            categoria = new CategoriaLite();
            categoria.setId(idCategoria);
            categoria.setCodice(codiceCategoria);
            categoria.setDescrizione(descrizioneCategoria);
        }
        return categoria;
    }

    /**
     * @return Returns the codiceFornitoreAbituale.
     */
    public Integer getCodiceFornitoreAbituale() {
        return codiceFornitoreAbituale;
    }

    /**
     * @return the costoUltimo
     * @uml.property name="costo"
     */
    public BigDecimal getCosto() {
        return costo;
    }

    /**
     * @return the costoUltimoMovId
     * @uml.property name="costoUltimoMovId"
     */
    public Integer getCostoUltimoMovId() {
        return costoUltimoMovId;
    }

    /**
     * @return the dataInventario
     * @uml.property name="dataInventario"
     */
    public Date getDataInventario() {
        return dataInventario;
    }

    /**
     * @return the deposito
     * @uml.property name="deposito"
     */
    public DepositoLite getDeposito() {
        if (deposito == null) {
            deposito = new DepositoLite();
            deposito.setId(idDeposito);
            deposito.setDescrizione(descrizioneDeposito);
            deposito.setCodice(codiceDeposito);
        }
        return deposito;
    }

    /**
     * @return the giacenza
     * @uml.property name="giacenza"
     */
    public Double getGiacenza() {
        return qtaInventario + qtaMagazzinoCarico + qtaMagazzinoCaricoAltro - qtaMagazzinoScarico
                - qtaMagazzinoScaricoAltro;
    }

    /**
     * @return Returns the idArticolo.
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return Returns the idDeposito.
     */
    public Integer getIdDeposito() {
        return idDeposito;
    }

    /**
     * @return Returns the importoCarico.
     */
    public BigDecimal getImportoCarico() {
        return importoCarico;
    }

    /**
     * @return Returns the importoScarico.
     */
    public BigDecimal getImportoScarico() {
        return importoScarico;
    }

    /**
     * @return the inventarioId
     * @uml.property name="inventarioId"
     */
    public Integer getInventarioId() {
        return inventarioId;
    }

    /**
     * @return the numeroPezzi
     */
    public String getNumeroPezzi() {
        return numeroPezzi;
    }

    /**
     * @return the qtaInventario
     * @uml.property name="qtaInventario"
     */
    public Double getQtaInventario() {
        return qtaInventario;
    }

    /**
     * @return the qtaMagazzinoCarico
     * @uml.property name="qtaMagazzinoCarico"
     */
    public Double getQtaMagazzinoCarico() {
        return qtaMagazzinoCarico;
    }

    /**
     * @return the qtaMagazzinoCaricoAltro
     * @uml.property name="qtaMagazzinoCaricoAltro"
     */
    public Double getQtaMagazzinoCaricoAltro() {
        return qtaMagazzinoCaricoAltro;
    }

    /**
     * @return the qtaMagazzinoScarico
     * @uml.property name="qtaMagazzinoScarico"
     */
    public Double getQtaMagazzinoScarico() {
        return qtaMagazzinoScarico;
    }

    /**
     * @return the qtaMagazzinoScaricoAltro
     * @uml.property name="qtaMagazzinoScaricoAltro"
     */
    public Double getQtaMagazzinoScaricoAltro() {
        return qtaMagazzinoScaricoAltro;
    }

    /**
     * @return Returns the scorta.
     */
    public Double getScorta() {
        return scorta;
    }

    /**
     * @return the valoreCostoUltimo
     * @uml.property name="valore"
     */
    public BigDecimal getValore() {
        return costo.multiply(new BigDecimal(getGiacenza()));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idArticolo == null) ? 0 : idArticolo.hashCode());
        result = prime * result + ((idCategoria == null) ? 0 : idCategoria.hashCode());
        result = prime * result + ((codiceFornitoreAbituale == null) ? 0 : codiceFornitoreAbituale.hashCode());
        result = prime * result + ((costo == null) ? 0 : costo.hashCode());
        result = prime * result + ((costoUltimoMovId == null) ? 0 : costoUltimoMovId.hashCode());
        result = prime * result + ((dataInventario == null) ? 0 : dataInventario.hashCode());
        result = prime * result + ((idDeposito == null) ? 0 : idDeposito.hashCode());
        result = prime * result + ((giacenza == null) ? 0 : giacenza.hashCode());
        result = prime * result + ((importoCarico == null) ? 0 : importoCarico.hashCode());
        result = prime * result + ((importoScarico == null) ? 0 : importoScarico.hashCode());
        result = prime * result + ((inventarioId == null) ? 0 : inventarioId.hashCode());
        result = prime * result + ((qtaInventario == null) ? 0 : qtaInventario.hashCode());
        result = prime * result + ((qtaMagazzinoCarico == null) ? 0 : qtaMagazzinoCarico.hashCode());
        result = prime * result + ((qtaMagazzinoCaricoAltro == null) ? 0 : qtaMagazzinoCaricoAltro.hashCode());
        result = prime * result + ((qtaMagazzinoScarico == null) ? 0 : qtaMagazzinoScarico.hashCode());
        result = prime * result + ((qtaMagazzinoScaricoAltro == null) ? 0 : qtaMagazzinoScaricoAltro.hashCode());
        result = prime * result + ((valore == null) ? 0 : valore.hashCode());
        return result;
    }

    /**
     * init valori.
     */
    private void initialize() {
    }

    /**
     *
     * @return true se l'articolo è sottoScorta
     */
    public boolean isSottoScorta() {
        return BigDecimal.valueOf(getGiacenza()).compareTo(BigDecimal.valueOf(scorta.doubleValue())) < 0;
    }

    /**
     * @param articolo
     *            the articolo to set
     * @uml.property name="articolo"
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     *
     * @param campoLibero
     *            informazioni add. articolo.
     */
    public void setCampoLibero(String campoLibero) {
        this.campoLibero = campoLibero;

    }

    /**
     * @param categoria
     *            the categoria to set
     * @uml.property name="categoria"
     */
    public void setCategoria(CategoriaLite categoria) {
        this.categoria = categoria;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * @param codiceCategoria
     *            the codiceCategoria to set
     */
    public void setCodiceCategoria(String codiceCategoria) {

        this.codiceCategoria = codiceCategoria;
    }

    /**
     * @param codiceDeposito
     *            the codiceDeposito to set
     */
    public void setCodiceDeposito(String codiceDeposito) {
        this.codiceDeposito = codiceDeposito;
    }

    /**
     * @param codiceFornitoreAbituale
     *            The codiceFornitoreAbituale to set.
     */
    public void setCodiceFornitoreAbituale(Integer codiceFornitoreAbituale) {
        this.codiceFornitoreAbituale = codiceFornitoreAbituale;
    }

    /**
     * @param costo
     *            the costo to set
     * @uml.property name="costo"
     */
    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    /**
     * @param costoUltimoMovId
     *            the costoUltimoMovId to set
     * @uml.property name="costoUltimoMovId"
     */
    public void setCostoUltimoMovId(Integer costoUltimoMovId) {
        this.costoUltimoMovId = costoUltimoMovId;
    }

    /**
     * @param dataInventario
     *            the dataInventario to set
     * @uml.property name="dataInventario"
     */
    public void setDataInventario(Date dataInventario) {
        this.dataInventario = dataInventario;
    }

    /**
     * @param deposito
     *            the deposito to set
     * @uml.property name="deposito"
     */
    public void setDeposito(DepositoLite deposito) {
        this.deposito = deposito;
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    /**
     * @param descrizioneCategoria
     *            the descrizioneCategoria to set
     */
    public void setDescrizioneCategoria(String descrizioneCategoria) {
        this.descrizioneCategoria = descrizioneCategoria;
    }

    /**
     * @param descrizioneDeposito
     *            the descrizioneDeposito to set
     */
    public void setDescrizioneDeposito(String descrizioneDeposito) {
        this.descrizioneDeposito = descrizioneDeposito;
    }

    /**
     * @param giacenza
     *            the giacenza to set
     * @uml.property name="giacenza"
     */
    public void setGiacenza(Double giacenza) {
        this.giacenza = giacenza;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idCategoria
     *            the idCategoria to set
     */
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * @param idDeposito
     *            the idDeposito to set
     */
    public void setIdDeposito(Integer idDeposito) {
        this.idDeposito = idDeposito;
    }

    /**
     * @param importoCarico
     *            The importoCarico to set.
     */
    public void setImportoCarico(BigDecimal importoCarico) {
        this.importoCarico = ObjectUtils.defaultIfNull(importoCarico, BigDecimal.ZERO);
    }

    /**
     * @param importoScarico
     *            The importoScarico to set.
     */
    public void setImportoScarico(BigDecimal importoScarico) {
        this.importoScarico = ObjectUtils.defaultIfNull(importoScarico, BigDecimal.ZERO);
    }

    /**
     * @param inventarioId
     *            the inventarioId to set
     * @uml.property name="inventarioId"
     */
    public void setInventarioId(Integer inventarioId) {
        this.inventarioId = inventarioId;
    }

    /**
     * @param numeroPezzi
     *            the numeroPezzi to set
     */
    public void setNumeroPezzi(String numeroPezzi) {
        this.numeroPezzi = numeroPezzi;
    }

    /**
     * @param qtaInventario
     *            the qtaInventario to set
     * @uml.property name="qtaInventario"
     */
    public void setQtaInventario(Double qtaInventario) {
        this.qtaInventario = qtaInventario == null ? 0 : qtaInventario;
    }

    /**
     * @param qtaMagazzinoCarico
     *            the qtaMagazzinoCarico to set
     * @uml.property name="qtaMagazzinoCarico"
     */
    public void setQtaMagazzinoCarico(Double qtaMagazzinoCarico) {
        this.qtaMagazzinoCarico = qtaMagazzinoCarico == null ? 0 : qtaMagazzinoCarico;
    }

    /**
     * @param qtaMagazzinoCaricoAltro
     *            the qtaMagazzinoCaricoAltro to set
     * @uml.property name="qtaMagazzinoCaricoAltro"
     */
    public void setQtaMagazzinoCaricoAltro(Double qtaMagazzinoCaricoAltro) {
        this.qtaMagazzinoCaricoAltro = qtaMagazzinoCaricoAltro == null ? 0 : qtaMagazzinoCaricoAltro;
    }

    /**
     * @param qtaMagazzinoScarico
     *            the qtaMagazzinoScarico to set
     * @uml.property name="qtaMagazzinoScarico"
     */
    public void setQtaMagazzinoScarico(Double qtaMagazzinoScarico) {
        this.qtaMagazzinoScarico = qtaMagazzinoScarico == null ? 0 : qtaMagazzinoScarico;
    }

    /**
     * @param qtaMagazzinoScaricoAltro
     *            the qtaMagazzinoScaricoAltro to set
     * @uml.property name="qtaMagazzinoScaricoAltro"
     */
    public void setQtaMagazzinoScaricoAltro(Double qtaMagazzinoScaricoAltro) {
        this.qtaMagazzinoScaricoAltro = qtaMagazzinoScaricoAltro == null ? 0 : qtaMagazzinoScaricoAltro;
    }

    /**
     * @param scorta
     *            The scorta to set.
     */
    public void setScorta(Double scorta) {
        this.scorta = ObjectUtils.defaultIfNull(scorta, 0.0);
    }

    /**
     * @param valore
     *            the valore to set
     * @uml.property name="valore"
     */
    public void setValore(BigDecimal valore) {
        this.valore = valore;
    }

}
