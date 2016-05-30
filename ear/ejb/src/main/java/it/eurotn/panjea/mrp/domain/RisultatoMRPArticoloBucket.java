package it.eurotn.panjea.mrp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import it.eurotn.panjea.mrp.util.ArticoloDepositoConfigurazioneKey;

public class RisultatoMRPArticoloBucket implements Serializable {

    private static final long serialVersionUID = -5460517892538952235L;

    private String codiciAttributo;
    private String valoriAttributo;
    private String formula;
    private String ordiniDaCollegare;

    private int ordinamento;
    private int tipoRiga;
    private int idArticolo;

    private int idDeposito;

    private int idRigaOrdine;
    private int idOrdine;
    private Integer idFornitore;

    private int idOrdineCliente;

    private Integer idTipoDocumento;

    private Integer idConfigurazioneDistinta;

    private Integer idComponente;

    private Integer leadTime;
    private Date dataDocumento;
    private Date dataConsegna;
    private Date dataProduzione;
    private Double scorta;
    private Double minOrdinabile;
    private Double lottoRiordino;

    private double qtaR;
    private double qtaInArrivo;
    private double qtaPor;

    private double qtarRimanente;
    private double giacenza;
    private double disponibilita;
    private double disponibilitaDopoUtilizzo;
    private UUID uuid;
    private RisultatoMRPArticoloBucket risultatoArticoloDistinta;

    private ArticoloDepositoConfigurazioneKey key;

    private Double qtaAttrezzaggioDistinta;

    /**
     * Costruttore.
     */
    public RisultatoMRPArticoloBucket() {
        uuid = UUID.randomUUID();
        qtarRimanente = -1;
        ordiniDaCollegare = "";
        ordinamento = -1;
    }

    public void aggiungiQtaAttrezzaggio(Double qtaAttrezzaggioDistinta) {
        if (this.qtaAttrezzaggioDistinta == null) {
            this.qtaAttrezzaggioDistinta = qtaAttrezzaggioDistinta;
            this.qtaR += qtaAttrezzaggioDistinta;
        }
    }

    public void collegaRigaOrdineFornitore(int idRiga) {
        if (StringUtils.isBlank(ordiniDaCollegare)) {
            ordiniDaCollegare = String.valueOf(idRiga);
        } else {
            ordiniDaCollegare = ordiniDaCollegare.concat(",").concat(String.valueOf(idRiga));
        }
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
        RisultatoMRPArticoloBucket other = (RisultatoMRPArticoloBucket) obj;
        if (dataConsegna == null) {
            if (other.dataConsegna != null) {
                return false;
            }
        } else if (!dataConsegna.equals(other.dataConsegna)) {
            return false;
        }
        if (dataDocumento == null) {
            if (other.dataDocumento != null) {
                return false;
            }
        } else if (!dataDocumento.equals(other.dataDocumento)) {
            return false;
        }
        if (dataProduzione == null) {
            if (other.dataProduzione != null) {
                return false;
            }
        } else if (!dataProduzione.equals(other.dataProduzione)) {
            return false;
        }
        if (Double.doubleToLongBits(giacenza) != Double.doubleToLongBits(other.giacenza)) {
            return false;
        }
        if (idArticolo != other.idArticolo) {
            return false;
        }
        if (idComponente == null) {
            if (other.idComponente != null) {
                return false;
            }
        } else if (!idComponente.equals(other.idComponente)) {
            return false;
        }
        if (idConfigurazioneDistinta == null) {
            if (other.idConfigurazioneDistinta != null) {
                return false;
            }
        } else if (!idConfigurazioneDistinta.equals(other.idConfigurazioneDistinta)) {
            return false;
        }
        if (idDeposito != other.idDeposito) {
            return false;
        }
        if (idFornitore == null) {
            if (other.idFornitore != null) {
                return false;
            }
        } else if (!idFornitore.equals(other.idFornitore)) {
            return false;
        }
        if (idRigaOrdine != other.idRigaOrdine) {
            return false;
        }
        if (idTipoDocumento == null) {
            if (other.idTipoDocumento != null) {
                return false;
            }
        } else if (!idTipoDocumento.equals(other.idTipoDocumento)) {
            return false;
        }
        if (leadTime == null) {
            if (other.leadTime != null) {
                return false;
            }
        } else if (!leadTime.equals(other.leadTime)) {
            return false;
        }
        if (minOrdinabile == null) {
            if (other.minOrdinabile != null) {
                return false;
            }
        } else if (!minOrdinabile.equals(other.minOrdinabile)) {
            return false;
        }
        if (lottoRiordino == null) {
            if (other.lottoRiordino != null) {
                return false;
            }
        } else if (!lottoRiordino.equals(other.lottoRiordino)) {
            return false;
        }
        if (ordinamento != other.ordinamento) {
            return false;
        }
        if (Double.doubleToLongBits(qtaInArrivo) != Double.doubleToLongBits(other.qtaInArrivo)) {
            return false;
        }
        if (Double.doubleToLongBits(qtaPor) != Double.doubleToLongBits(other.qtaPor)) {
            return false;
        }
        if (Double.doubleToLongBits(qtaR) != Double.doubleToLongBits(other.qtaR)) {
            return false;
        }
        return true;
    }

    /**
     * @param dataInizioCalcolo
     *            dataInizio del calcolo dell'mrp
     * @return num. giorni tra dataConsegna (data r) e data inizio calcolo
     */
    public int getBucket(Date dataInizioCalcolo) {
        return Days.daysBetween(new DateTime(dataInizioCalcolo), new DateTime(dataConsegna))
                .getDays();
    }

    /**
     * @return Returns the codiciAttributo.
     */
    public String getCodiciAttributo() {
        return codiciAttributo;
    }

    /**
     * @return Returns the dataConsegna.
     */
    public Date getDataConsegna() {
        return dataConsegna;
    }

    /**
     * @return Returns the dataDocumento.
     */
    public Date getDataDocumento() {
        return dataDocumento;
    }

    /**
     * @return the dataProduzione
     */
    public Date getDataProduzione() {
        return dataProduzione;
    }

    /**
     * @return Returns the disponibilita.
     */
    public double getDisponibilita() {
        return disponibilita;
    }

    /**
     * @return Returns the disponibilitaDopoUtilizzo.
     */
    public double getDisponibilitaDopoUtilizzo() {
        return disponibilitaDopoUtilizzo;
    }

    /**
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * @return Returns the giacenza.
     */
    public double getGiacenza() {
        return giacenza;
    }

    /**
     * @return Returns the idArticolo.
     */
    public int getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return Returns the idComponente.
     */
    public Integer getIdComponente() {
        return idComponente;
    }

    /**
     * @return Returns the idConfigurazioneDistinta.
     */
    public Integer getIdConfigurazioneDistinta() {
        return idConfigurazioneDistinta;
    }

    /**
     * @return Returns the idDeposito.
     */
    public int getIdDeposito() {
        return idDeposito;
    }

    /**
     * @return Returns the idFornitore.
     */
    public Integer getIdFornitore() {
        return idFornitore;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    /**
     * @return Returns the idOrdineCliente.
     */
    public int getIdOrdineCliente() {
        return idOrdineCliente;
    }

    /**
     * @return Returns the idRigaOrdine.
     */
    public int getIdRigaOrdine() {
        return idRigaOrdine;
    }

    /**
     * @return Returns the idTipoDocumento.
     */
    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    /**
     * @return Returns the key.
     */
    public ArticoloDepositoConfigurazioneKey getKey() {
        if (key == null) {
            key = new ArticoloDepositoConfigurazioneKey(idComponente, idDeposito,
                    idConfigurazioneDistinta);
        }
        return key;
    }

    /**
     * @return Returns the leadTime.
     */
    public Integer getLeadTime() {
        return leadTime;
    }

    /**
     * @return the lottoRiordino
     */
    public Double getLottoRiordino() {
        return lottoRiordino;
    }

    /**
     * @return Returns the minOrdinabile.
     */
    public Double getMinOrdinabile() {
        return minOrdinabile;
    }

    /**
     * @return the ordinamento
     */
    public int getOrdinamento() {
        return ordinamento;
    }

    public String getOrdiniDaCollegare() {
        return ordiniDaCollegare;
    }

    /**
     * @return the qtaInArrivo
     */
    public double getQtaInArrivo() {
        return qtaInArrivo;
    }

    /**
     * @return Returns the qtaPor.
     */
    public double getQtaPor() {
        return qtaPor;
    }

    /**
     * @return Returns the qtaR.
     */
    public double getQtaR() {
        return qtaR;
    }

    public double getQtarRimanente() {
        if (qtarRimanente == -1) {
            return qtaR;
        }
        return qtarRimanente;
    }

    /**
     * @return Returns the risultatoArticoloDistinta.
     */
    public RisultatoMRPArticoloBucket getRisultatoArticoloDistinta() {
        return risultatoArticoloDistinta;
    }

    /**
     * @return Returns the scorta.
     */
    public Double getScorta() {
        return scorta;
    }

    /**
     * @return Returns the tipoRiga.
     */
    public int getTipoRiga() {
        return tipoRiga;
    }

    /**
     * @return Returns the uuid.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @return Returns the valoriAttributo.
     */
    public String getValoriAttributo() {
        return valoriAttributo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataConsegna == null) ? 0 : dataConsegna.hashCode());
        result = prime * result + ((dataDocumento == null) ? 0 : dataDocumento.hashCode());
        result = prime * result + ((dataProduzione == null) ? 0 : dataProduzione.hashCode());
        long temp;
        temp = Double.doubleToLongBits(giacenza);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + idArticolo;
        result = prime * result + ((idComponente == null) ? 0 : idComponente.hashCode());
        result = prime * result
                + ((idConfigurazioneDistinta == null) ? 0 : idConfigurazioneDistinta.hashCode());
        result = prime * result + idDeposito;
        result = prime * result + ((idFornitore == null) ? 0 : idFornitore.hashCode());
        result = prime * result + idRigaOrdine;
        result = prime * result + ((idTipoDocumento == null) ? 0 : idTipoDocumento.hashCode());
        result = prime * result + ((leadTime == null) ? 0 : leadTime.hashCode());
        result = prime * result + ((minOrdinabile == null) ? 0 : minOrdinabile.hashCode());
        result = prime * result + ((lottoRiordino == null) ? 0 : lottoRiordino.hashCode());
        result = prime * result + ordinamento;
        temp = Double.doubleToLongBits(qtaInArrivo);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(qtaPor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(qtaR);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Copia grli attributi di articolo anagrafica nelal riga del risultato.
     *
     * @param articoloAnagrafica
     *            oggetto con gli attributi da copiare.
     */
    public void setArticoloAnagrafica(ArticoloAnagrafica articoloAnagrafica) {
        idFornitore = articoloAnagrafica.getIdFornitore();
        idTipoDocumento = articoloAnagrafica.getIdTipoDocumento();
        leadTime = articoloAnagrafica.getLeadTime();
        minOrdinabile = articoloAnagrafica.getMinOrdinabile();
        lottoRiordino = articoloAnagrafica.getLottoRiordino();
        idArticolo = articoloAnagrafica.getIdArticolo();
    }

    /**
     * @param codiciAttributo
     *            The codiciAttributo to set.
     */
    public void setCodiciAttributo(String codiciAttributo) {
        this.codiciAttributo = codiciAttributo;
    }

    /**
     * @param dataConsegna
     *            The dataConsegna to set.
     */
    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    /**
     * @param dataDocumento
     *            The dataDocumento to set.
     */
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param dataProduzione
     *            the dataProduzione to set
     */
    public void setDataProduzione(Date dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    /**
     * @param disponibilita
     *            The disponibilita to set.
     */
    public void setDisponibilita(double disponibilita) {
        this.disponibilita = disponibilita;
    }

    /**
     * @param disponibilitaDopoUtilizzo
     *            The disponibilitaDopoUtilizzo to set.
     */
    public void setDisponibilitaDopoUtilizzo(double disponibilitaDopoUtilizzo) {
        this.disponibilitaDopoUtilizzo = disponibilitaDopoUtilizzo;
    }

    /**
     * @param formula
     *            the formula to set
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * @param giacenza
     *            The giacenza to set.
     */
    public void setGiacenza(double giacenza) {
        this.giacenza = giacenza;
    }

    /**
     * @param idArticolo
     *            The idArticolo to set.
     */
    public void setIdArticolo(int idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idComponente
     *            the idComponente to set
     */
    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    /**
     * @param idConfigurazioneDistinta
     *            The idConfigurazioneDistinta to set.
     */
    public void setIdConfigurazioneDistinta(Integer idConfigurazioneDistinta) {
        this.idConfigurazioneDistinta = idConfigurazioneDistinta;
    }

    /**
     * @param idDeposito
     *            The idDeposito to set.
     */
    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }

    /**
     * @param idFornitore
     *            The idFornitore to set.
     */
    public void setIdFornitore(Integer idFornitore) {
        this.idFornitore = idFornitore;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    /**
     * @param idOrdineCliente
     *            The idOrdineCliente to set.
     */
    public void setIdOrdineCliente(int idOrdineCliente) {
        this.idOrdineCliente = idOrdineCliente;
    }

    /**
     * @param idRigaOrdine
     *            The idRigaOrdine to set.
     */
    public void setIdRigaOrdine(int idRigaOrdine) {
        this.idRigaOrdine = idRigaOrdine;
    }

    /**
     * @param idTipoDocumento
     *            The idTipoDocumento to set.
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    /**
     * @param leadTime
     *            The leadTime to set.
     */
    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    /**
     * @param lottoRiordino
     *            the lottoRiordino to set
     */
    public void setLottoRiordino(Double lottoRiordino) {
        this.lottoRiordino = lottoRiordino;
    }

    /**
     * @param minOrdinabile
     *            The minOrdinabile to set.
     */
    public void setMinOrdinabile(Double minOrdinabile) {
        this.minOrdinabile = minOrdinabile;
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(int ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param qtaInArrivo
     *            the qtaInArrivo to set
     */
    public void setQtaInArrivo(double qtaInArrivo) {
        this.qtaInArrivo = qtaInArrivo;
    }

    /**
     * @param qtaPor
     *            The qtaPor to set.
     */
    public void setQtaPor(double qtaPor) {
        this.qtaPor = qtaPor;
    }

    /**
     * @param qtaR
     *            The qtaR to set.
     */
    public void setQtaR(double qtaR) {
        this.qtaR = qtaR;
    }

    public void setQtarRimanente(double qtarRimanente) {
        this.qtarRimanente = qtarRimanente;
    }

    /**
     * @param risultatoArticoloDistinta
     *            The risultatoArticoloDistinta to set.
     */
    public void setRisultatoArticoloDistinta(RisultatoMRPArticoloBucket risultatoArticoloDistinta) {
        this.risultatoArticoloDistinta = risultatoArticoloDistinta;
    }

    /**
     * @param scorta
     *            The scorta to set.
     */
    public void setScorta(Double scorta) {
        this.scorta = scorta;
    }

    /**
     * @param tipoRiga
     *            The tipoRiga to set.
     */
    public void setTipoRiga(int tipoRiga) {
        this.tipoRiga = tipoRiga;
    }

    /**
     * @param valoriAttributo
     *            The valoriAttributo to set.
     */
    public void setValoriAttributo(String valoriAttributo) {
        this.valoriAttributo = valoriAttributo;
    }

    @Override
    public String toString() {
        return "RisultatoMRPArticoloBucket [idArticolo=" + idArticolo + ", idDeposito=" + idDeposito
                + ", idRigaOrdine=" + idRigaOrdine + ", idFornitore=" + idFornitore
                + ", idTipoDocumento=" + idTipoDocumento + ", idConfigurazioneDistinta="
                + idConfigurazioneDistinta + ", idComponente=" + idComponente + ", leadTime="
                + leadTime + ", lottoRiordino=" + lottoRiordino + ", dataDocumento=" + dataDocumento
                + ", dataConsegna=" + dataConsegna + ", dataProduzione=" + dataProduzione
                + ", scorta=" + scorta + ", minOrdinabile=" + minOrdinabile + ", qtaR=" + qtaR
                + ", qtaInArrivo=" + qtaInArrivo + ", qtaPor=" + qtaPor + ", giacenza=" + giacenza
                + "]";
    }

}
