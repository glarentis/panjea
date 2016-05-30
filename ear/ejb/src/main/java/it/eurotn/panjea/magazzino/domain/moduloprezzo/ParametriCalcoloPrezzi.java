package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;

/**
 * Rappresenta l'insieme dei parametri che verranno utilizzati da ogni <code>ModuloPrezzo</code> per calcolare prezzo e
 * sconto per l'articolo. Oltre i parametri di calcolo contiene anche i risultati (<code>politicaPrezzo</code>) che
 * verranno aggiornati da ogni modulo durante il calcolo dei valori.
 *
 * @author fattazzo
 */
public class ParametriCalcoloPrezzi implements Serializable {

    private static final long serialVersionUID = 8717892140037361042L;
    public static final String PROVENIENZA_LISTINO = "LISTINO";
    public static final String PROVENIENZA_ULTIMO_COSTO = "ULTIMOCOSTO";
    public static final String PROVENIENZA_LISTINO_TIPO_MEZZO_ZONA_GEOGRAFICA = "LISTINOTIPOMEZZOZONAGEOGRAFICA";

    /**
     * La classe contiene l'id articolo solamente per un fatto di prestazioni in quanto è il solo dato utile per il
     * calcolo dei prezzi sui vari moduli.
     */
    private Integer idArticolo;

    private Date data;

    private Integer idListino;

    private Integer idListinoAlternativo;

    private boolean bloccoPrezzo;

    private BigDecimal incrementoPrezzo;

    private boolean bloccoSconto;

    private Integer idSedeEntita;

    private PoliticaPrezzo politicaPrezzo;

    private final ProvenienzaPrezzo provenienzaPrezzo;

    private Integer idTipoMezzoTrasporto;

    private Integer idZonaGeografica;

    private Integer idAgente;

    private boolean bloccoProvvigioni;

    private ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo;

    private String codiceValuta;

    private BigDecimal percentualeScontoCommerciale;

    private Integer idCategoriaSedeMagazzino;

    private Integer idCategoriaCommercialeArticolo;

    private Integer idEntita;

    /**
     * Costruttore di default.
     * 
     * @param idArticolo
     *            id articolo
     * @param data
     *            data per il calcolo del prezzo
     * @param idListino
     *            listino
     * @param idListinoAlternativo
     *            listino alternativo
     * @param idEntita
     *            entità
     * @param idSedeEntita
     *            sede entità
     * @param idCategoriaSedeMagazzino
     *            categoria sede magazzino
     * @param idCategoriaCommercialeArticolo
     *            cat commerciale articolo
     * @param provenienzaPrezzo
     *            proveninza del prezzo. Ad esempio ultimo costo o listino
     * @param idTipoMezzoTrasporto
     *            id del tipo mezzo
     * @param idZonaGeografica
     *            id della zona geografica
     * @param provenienzaPrezzoArticolo
     *            provenienza prezzo dell'articolo. ( documento o tipomezzozona)
     * @param codiceValuta
     *            codice valuta di riferimento
     * @param idAgente
     *            id agente di riferimento
     * @param percentualeScontoCommerciale
     *            percentuale sconto commerciale
     */
    public ParametriCalcoloPrezzi(final Integer idArticolo, final Date data, final Integer idListino,
            final Integer idListinoAlternativo, final Integer idEntita, final Integer idSedeEntita,
            final Integer idCategoriaSedeMagazzino, final Integer idCategoriaCommercialeArticolo,
            final ProvenienzaPrezzo provenienzaPrezzo, final Integer idTipoMezzoTrasporto,
            final Integer idZonaGeografica, final ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo,
            final String codiceValuta, final Integer idAgente, final BigDecimal percentualeScontoCommerciale) {
        super();
        this.idArticolo = idArticolo;
        this.data = data;
        this.idListino = idListino;
        this.idListinoAlternativo = idListinoAlternativo;
        this.idSedeEntita = idSedeEntita;

        this.bloccoPrezzo = false;
        this.bloccoSconto = false;
        this.bloccoProvvigioni = false;

        this.provenienzaPrezzo = provenienzaPrezzo;

        this.politicaPrezzo = new PoliticaPrezzo();

        this.idTipoMezzoTrasporto = idTipoMezzoTrasporto;
        this.idZonaGeografica = idZonaGeografica;

        this.provenienzaPrezzoArticolo = provenienzaPrezzoArticolo;

        this.codiceValuta = codiceValuta;

        this.idAgente = idAgente;

        this.percentualeScontoCommerciale = percentualeScontoCommerciale;

        this.idCategoriaSedeMagazzino = idCategoriaSedeMagazzino;
        this.idCategoriaCommercialeArticolo = idCategoriaCommercialeArticolo;
    }

    /**
     * Costruttore.
     * 
     * @param parametriCreazioneArticolo
     *            parametri per la creazione dell'articolo
     */
    public ParametriCalcoloPrezzi(final ParametriCreazioneRigaArticolo parametriCreazioneArticolo) {
        super();
        this.idArticolo = parametriCreazioneArticolo.getIdArticolo();
        this.data = parametriCreazioneArticolo.getData();
        this.idListino = parametriCreazioneArticolo.getIdListino();
        this.idListinoAlternativo = parametriCreazioneArticolo.getIdListinoAlternativo();
        this.idEntita = parametriCreazioneArticolo.getIdEntita();
        this.idSedeEntita = parametriCreazioneArticolo.getIdSedeEntita();

        this.bloccoPrezzo = false;
        this.bloccoSconto = false;
        this.bloccoProvvigioni = false;

        this.provenienzaPrezzo = parametriCreazioneArticolo.getProvenienzaPrezzo();

        this.politicaPrezzo = new PoliticaPrezzo();

        this.idTipoMezzoTrasporto = parametriCreazioneArticolo.getIdTipoMezzo();
        this.idZonaGeografica = parametriCreazioneArticolo.getIdZonaGeografica();

        this.provenienzaPrezzoArticolo = parametriCreazioneArticolo.getProvenienzaPrezzoArticolo();

        this.codiceValuta = parametriCreazioneArticolo.getCodiceValuta();

        this.idAgente = parametriCreazioneArticolo.getIdAgente();

        this.percentualeScontoCommerciale = parametriCreazioneArticolo.getPercentualeScontoCommerciale();
    }

    /**
     * Costruttore che prende una riga contratto; le liste della riga contratto (sedi, entità, agenti) non vengono
     * considerate.
     * 
     * @param rigaContratto
     *            la riga contratto da cui recuperare le informazioni
     */
    public ParametriCalcoloPrezzi(final RigaContratto rigaContratto) {
        super();

        this.idArticolo = rigaContratto.getArticolo() != null ? rigaContratto.getArticolo().getId() : null;
        this.data = new Date();
        this.idListino = null;
        this.idListinoAlternativo = null;
        this.idEntita = null;
        this.idSedeEntita = null;

        this.bloccoPrezzo = false;
        this.bloccoSconto = false;
        this.bloccoProvvigioni = false;
        this.provenienzaPrezzo = ProvenienzaPrezzo.LISTINO;

        this.politicaPrezzo = new PoliticaPrezzo();

        this.idTipoMezzoTrasporto = null;
        this.idZonaGeografica = null;

        this.provenienzaPrezzoArticolo = ProvenienzaPrezzoArticolo.DOCUMENTO;

        this.codiceValuta = "EUR";

        this.idAgente = null;

        this.percentualeScontoCommerciale = BigDecimal.ZERO;

        this.idCategoriaSedeMagazzino = null;
        this.idCategoriaCommercialeArticolo = rigaContratto.getCategoriaCommercialeArticolo() != null
                ? rigaContratto.getCategoriaCommercialeArticolo().getId() : null;
    }

    /**
     * @return the codiceValuta
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the idAgente
     */
    public Integer getIdAgente() {
        return idAgente;
    }

    /**
     * @return the idArticolo
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return the idCategoriaCommercialeArticolo
     */
    public Integer getIdCategoriaCommercialeArticolo() {
        return idCategoriaCommercialeArticolo;
    }

    /**
     * @return the idCategoriaSedeMagazzino
     */
    public Integer getIdCategoriaSedeMagazzino() {
        return idCategoriaSedeMagazzino;
    }

    /**
     * @return the idEntita
     */
    public Integer getIdEntita() {
        return idEntita;
    }

    /**
     * @return the idListino
     */
    public Integer getIdListino() {
        return idListino;
    }

    /**
     * @return the idListinoAlternativo
     */
    public Integer getIdListinoAlternativo() {
        return idListinoAlternativo;
    }

    /**
     * @return the idSedeEntita
     */
    public Integer getIdSedeEntita() {
        return idSedeEntita;
    }

    /**
     * @return the idTipoMezzoTrasporto
     */
    public Integer getIdTipoMezzoTrasporto() {
        return idTipoMezzoTrasporto;
    }

    /**
     * @return the idZonaGeografica
     */
    public Integer getIdZonaGeografica() {
        return idZonaGeografica;
    }

    /**
     * @return the incrementoPrezzo
     */
    public BigDecimal getIncrementoPrezzo() {
        return incrementoPrezzo;
    }

    /**
     * @return the percentualeScontoCommerciale
     */
    public BigDecimal getPercentualeScontoCommerciale() {
        return percentualeScontoCommerciale;
    }

    /**
     * @return the politicaPrezzo
     */
    public PoliticaPrezzo getPoliticaPrezzo() {
        return politicaPrezzo;
    }

    /**
     * @return the provenienza
     */
    public ProvenienzaPrezzo getProvenienza() {
        return provenienzaPrezzo;
    }

    /**
     * Provenienza articolo può essere {@link ProvenienzaPrezzoArticolo#DOCUMENTO},<br/>
     * {@link ProvenienzaPrezzoArticolo#TIPOMEZZOZONAGEOGRAFICA}<br/>
     * nel caso di provenienza DOCUMENTO, provenienza prezzo deve essere valorizzata;<br/>
     * nel caso di provenienza TIPOMEZZOZONAGEOGRAFICA ho solo una possibilita'.<br/>
     * 
     * @return the provenienza
     */
    public String getProvenienzaDescrizione() {
        if (getProvenienzaPrezzoArticolo() != null
                && getProvenienzaPrezzoArticolo() == ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA) {
            return PROVENIENZA_LISTINO_TIPO_MEZZO_ZONA_GEOGRAFICA;
        } else {
            if (provenienzaPrezzo != null) {
                switch (provenienzaPrezzo) {
                case LISTINO:
                    return PROVENIENZA_LISTINO;
                case ULTIMO_COSTO:
                    return PROVENIENZA_ULTIMO_COSTO;
                default:
                    throw new UnsupportedOperationException("provenienza prezzo sconosciuta " + provenienzaPrezzo);
                }
            } else {
                throw new IllegalArgumentException(
                        "Errore nel calcolo del prezzo per la riga! provenienza prezzo articolo: "
                                + getProvenienzaPrezzoArticolo() + ", provenienza prezzo: " + provenienzaPrezzo);
            }
        }
    }

    /**
     * @return the provenienzaPrezzoArticolo
     */
    public ProvenienzaPrezzoArticolo getProvenienzaPrezzoArticolo() {
        return provenienzaPrezzoArticolo;
    }

    /**
     * @return the bloccoPrezzo
     */
    public boolean isBloccoPrezzo() {
        return bloccoPrezzo;
    }

    /**
     * @return the bloccoProvvigioni
     */
    public boolean isBloccoProvvigioni() {
        return bloccoProvvigioni;
    }

    /**
     * @return the bloccoSconto
     */
    public boolean isBloccoSconto() {
        return bloccoSconto;
    }

    /**
     * @param bloccoPrezzo
     *            the bloccoPrezzo to set
     */
    public void setBloccoPrezzo(boolean bloccoPrezzo) {
        this.bloccoPrezzo = bloccoPrezzo;
    }

    /**
     * @param bloccoProvvigioni
     *            the bloccoProvvigioni to set
     */
    public void setBloccoProvvigioni(boolean bloccoProvvigioni) {
        this.bloccoProvvigioni = bloccoProvvigioni;
    }

    /**
     * @param bloccoSconto
     *            the bloccoSconto to set
     */
    public void setBloccoSconto(boolean bloccoSconto) {
        this.bloccoSconto = bloccoSconto;
    }

    /**
     * @param codiceValuta
     *            the codiceValuta to set
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param idAgente
     *            the idAgente to set
     */
    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idCategoriaCommercialeArticolo
     *            the idCategoriaCommercialeArticolo to set
     */
    public void setIdCategoriaCommercialeArticolo(Integer idCategoriaCommercialeArticolo) {
        this.idCategoriaCommercialeArticolo = idCategoriaCommercialeArticolo;
    }

    /**
     * @param idCategoriaSedeMagazzino
     *            the idCategoriaSedeMagazzino to set
     */
    public void setIdCategoriaSedeMagazzino(Integer idCategoriaSedeMagazzino) {
        this.idCategoriaSedeMagazzino = idCategoriaSedeMagazzino;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    /**
     * @param idListino
     *            the idListino to set
     */
    public void setIdListino(Integer idListino) {
        this.idListino = idListino;
    }

    /**
     * @param idListinoAlternativo
     *            the idListinoAlternativo to set
     */
    public void setIdListinoAlternativo(Integer idListinoAlternativo) {
        this.idListinoAlternativo = idListinoAlternativo;
    }

    /**
     * @param idSedeEntita
     *            the idSedeEntita to set
     */
    public void setIdSedeEntita(Integer idSedeEntita) {
        this.idSedeEntita = idSedeEntita;
    }

    /**
     * @param idTipoMezzoTrasporto
     *            the idTipoMezzoTrasporto to set
     */
    public void setIdTipoMezzoTrasporto(Integer idTipoMezzoTrasporto) {
        this.idTipoMezzoTrasporto = idTipoMezzoTrasporto;
    }

    /**
     * @param idZonaGeografica
     *            the idZonaGeografica to set
     */
    public void setIdZonaGeografica(Integer idZonaGeografica) {
        this.idZonaGeografica = idZonaGeografica;
    }

    /**
     * @param incrementoPrezzo
     *            the incrementoPrezzo to set
     */
    public void setIncrementoPrezzo(BigDecimal incrementoPrezzo) {
        this.incrementoPrezzo = incrementoPrezzo;
    }

    /**
     * @param percentualeScontoCommerciale
     *            the percentualeScontoCommerciale to set
     */
    public void setPercentualeScontoCommerciale(BigDecimal percentualeScontoCommerciale) {
        this.percentualeScontoCommerciale = percentualeScontoCommerciale;
    }

    /**
     * @param politicaPrezzo
     *            the politicaPrezzo to set
     */
    public void setPoliticaPrezzo(PoliticaPrezzo politicaPrezzo) {
        this.politicaPrezzo = politicaPrezzo;
    }

    /**
     * @param provenienzaPrezzoArticolo
     *            the provenienzaPrezzoArticolo to set
     */
    public void setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo) {
        this.provenienzaPrezzoArticolo = provenienzaPrezzoArticolo;
    }

    @Override
    public String toString() {
        return "ParametriCalcoloPrezzi [bloccoPrezzo=" + bloccoPrezzo + ", bloccoSconto=" + bloccoSconto + ", data="
                + data + ", idArticolo=" + idArticolo + ", idListino=" + idListino + ", idListinoAlternativo="
                + idListinoAlternativo + ", idSedeEntita=" + idSedeEntita + ", incrementoPrezzo=" + incrementoPrezzo
                + ", politicaPrezzo=" + politicaPrezzo + "]";
    }

}
