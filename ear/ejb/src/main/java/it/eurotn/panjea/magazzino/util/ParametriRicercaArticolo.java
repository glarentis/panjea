package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributo;

/**
 * Parametri per la ricerca degli articoli.
 *
 * @author fattazzo
 */
public class ParametriRicercaArticolo implements Serializable {

    public class CustomFilter implements Serializable {
        private static final long serialVersionUID = 1833405549273998271L;

        private String jndiFilterBeanName;

        private Map<String, Object> filterParameterMap;

        {
            filterParameterMap = new HashMap<String, Object>();
        }

        /**
         * Costruttore.
         */
        public CustomFilter() {
            super();
        }

        /**
         * @return the filterParameterMap
         */
        public Map<String, Object> getFilterParameterMap() {
            return filterParameterMap;
        }

        /**
         * @return the jndiFilterBeanName
         */
        public String getJndiFilterBeanName() {
            return jndiFilterBeanName;
        }

        /**
         * @param filterParameterMap
         *            the filterParameterMap to set
         */
        public void setFilterParameterMap(Map<String, Object> filterParameterMap) {
            this.filterParameterMap = filterParameterMap;
        }

        /**
         * @param jndiFilterBeanName
         *            the jndiFilterBeanName to set
         */
        public void setJndiFilterBeanName(String jndiFilterBeanName) {
            this.jndiFilterBeanName = jndiFilterBeanName;
        }
    }

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum StatoArticolo {
        ABILITATO, DISABILITATO, TUTTI
    }

    public static final String DELIMITER_CODENT = "@";
    public static final String DELIMITER_CODINTERNO = "£";
    public static final String DELIMITER_BARCODE = "#";
    public static final String DELIMITER_ATTRIBUTO = "$";

    public static final String PATTERN_OPERATORE = "(<=|>=|<>|=|<|>)";

    private static final long serialVersionUID = -8794618982027179738L;

    private Integer idCategoria = null;
    private Integer idCategoriaCommerciale = null;
    private Integer idEntita = null;
    /**
     * Se impostato la ricerca viene effettuata sugli attributi dell'articolo se gli attributi non hanno valore usata
     * per trovare gli articoli alternativi.
     */
    private Integer idArticolo = null;
    /**
     * Utilizzato per l'eventuale calcolo della giacenza.
     */
    private Integer idDeposito = null;

    private String filtro = null;
    private String codice = null;
    /**
     * Identificato nel filtro con £codiceInterno£.
     */
    private String codiceInterno = null;
    /**
     * Identificato nel filtro con #barcode#.
     */
    private String barCode = null;
    /**
     * Identificato nel filtro con @codEntita@.
     */
    private String codiceEntita = null;
    private String descrizione = null;
    private String codiceAzienda = null;
    private String lingua = null;

    private boolean includiArticoliCategorieFiglie = false;
    private boolean escludiDistinte = false;
    private boolean soloDistinte = false;
    private boolean isLinguaAzienda = true;
    private boolean calcolaGiacenza = false;
    private boolean ricercaCodiceOrDescrizione = false;
    private boolean assortimentoArticoli = false;
    private boolean ordinaPerAttributi = false;
    private ClienteLite cliente;

    private List<Integer> idCategorie = null;

    private List<ParametroRicercaAttributo> ricercaAttributi = null;

    private CustomFilter customFilter = null;
    private StatoArticolo statoArticolo = null;

    private Integer idInstallazione = null;

    /**
     * Costruttore.
     */
    public ParametriRicercaArticolo() {
        initialize();
    }

    /**
     * @return the barCode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @return Returns the cliente.
     */
    public ClienteLite getCliente() {
        return cliente;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
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
     * @return the customFilter
     */
    public CustomFilter getCustomFilter() {
        return customFilter;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the filtro
     */
    public String getFiltro() {
        return filtro;
    }

    /**
     * @return Returns the idArticolo.
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return the idCategoria
     */
    public Integer getIdCategoria() {
        return idCategoria;
    }

    /**
     * @return the idCategoriaCommerciale
     */
    public Integer getIdCategoriaCommerciale() {
        return idCategoriaCommerciale;
    }

    /**
     * @return the idCategorie
     */
    public List<Integer> getIdCategorie() {
        return idCategorie;
    }

    /**
     * @return the idDeposito
     */
    public Integer getIdDeposito() {
        return idDeposito;
    }

    /**
     * @return the idEntita
     */
    public Integer getIdEntita() {
        return idEntita;
    }

    /**
     * @return the idInstallazione
     */
    public final Integer getIdInstallazione() {
        return idInstallazione;
    }

    /**
     * @return the lingua
     */
    public String getLingua() {
        return lingua;
    }

    /**
     * @return the ricercaAttributi
     */
    public List<ParametroRicercaAttributo> getRicercaAttributi() {
        return ricercaAttributi;
    }

    /**
     * @return the statoArticolo
     */
    public StatoArticolo getStatoArticolo() {
        return statoArticolo;
    }

    /**
     * Init delle proprietà di this.
     */
    private void initialize() {
        statoArticolo = StatoArticolo.TUTTI;
        idInstallazione = null;
    }

    /**
     * @return Returns the assortimentoArticoli.
     */
    public boolean isAssortimentoArticoli() {
        return assortimentoArticoli;
    }

    /**
     * @return the calcolaGiacenza
     */
    public boolean isCalcolaGiacenza() {
        return calcolaGiacenza;
    }

    /**
     * @return Returns the escludiDistinte.
     */
    public boolean isEscludiDistinte() {
        return escludiDistinte;
    }

    /**
     * @return the includiArticoliCategorieFiglie
     */
    public boolean isIncludiArticoliCategorieFiglie() {
        return includiArticoliCategorieFiglie;
    }

    /**
     * @return the isLinguaAzienda
     */
    public boolean isLinguaAzienda() {
        return isLinguaAzienda;
    }

    /**
     * @return the ordinaPerAttributi
     */
    public boolean isOrdinaPerAttributi() {
        return ordinaPerAttributi;
    }

    /**
     * @return se sto cercando per un attributo
     */
    public boolean isRicercaAttributiPresente() {
        return filtro != null && filtro.indexOf("$") != -1;
    }

    /**
     * @return se voglio cercare per bar code
     */
    public boolean isRicercaBarCodePresente() {
        return barCode != null || (filtro != null && filtro.matches("#(.*?)#?"));
    }

    /**
     * @return se voglio cercare per codice entità o se voglio filtrare per entità la ricerca
     */
    public boolean isRicercaCodiceArticoloEntitaPresente() {
        return codiceEntita != null || (filtro != null && filtro.matches("@(.*?)@?"));
    }

    /**
     * @return se voglio cercare per bar code
     */
    public boolean isRicercaCodiceInternoPresente() {
        return codiceInterno != null || (filtro != null && filtro.matches("\\£(.*?)\\£?"));
    }

    /**
     * @return the ricercaCodiceOrDescrizione
     */
    public boolean isRicercaCodiceOrDescrizione() {
        return ricercaCodiceOrDescrizione;
    }

    /**
     * @return the soloDistinte
     */
    public boolean isSoloDistinte() {
        return soloDistinte;
    }

    /**
     * @param assortimentoArticoliParam
     *            true se devo limitare la ricerca all'assortimento
     */
    public void setAssortimento(boolean assortimentoArticoliParam) {
        this.assortimentoArticoli = assortimentoArticoliParam;

    }

    /**
     * @param assortimentoArticoli
     *            The assortimentoArticoli to set.
     */
    public void setAssortimentoArticoli(boolean assortimentoArticoli) {
        this.assortimentoArticoli = assortimentoArticoli;
    }

    /**
     * @param barCode
     *            the barCode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @param calcolaGiacenza
     *            the calcolaGiacenza to set
     */
    public void setCalcolaGiacenza(boolean calcolaGiacenza) {
        this.calcolaGiacenza = calcolaGiacenza;
    }

    /**
     * @param cliente
     *            The cliente to set.
     */
    public void setCliente(ClienteLite cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            idEntita = cliente.getId();
            assortimentoArticoli = true;
        } else {
            idEntita = null;
            assortimentoArticoli = false;
        }

    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
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
     * @param customFilter
     *            the customFilter to set
     */
    public void setCustomFilter(CustomFilter customFilter) {
        this.customFilter = customFilter;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param escludiDistinte
     *            the escludiDistinte to set
     */
    public void setEscludiDistinte(boolean escludiDistinte) {
        this.escludiDistinte = escludiDistinte;
    }

    /**
     * @param escludiDistinte
     *            .
     */
    public void setEscludiDistinte(Boolean escludiDistinte) {
        this.escludiDistinte = escludiDistinte;
    }

    /**
     * Viene eseguito il parse della stringa e vengono quindi valorizzate le proprietà associate ai caratteri usati; se
     * si vuole cercare per un campo specifico basta impostare le proprietà con il relativo set.
     *
     * @param filtro
     *            the filtro to set
     */
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    /**
     * @param idArticolo
     *            The idArticolo to set.
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
     * @param idCategoriaCommerciale
     *            the idCategoriaCommerciale to set
     */
    public void setIdCategoriaCommerciale(Integer idCategoriaCommerciale) {
        this.idCategoriaCommerciale = idCategoriaCommerciale;
    }

    /**
     * @param idCategorie
     *            the idCategorie to set
     */
    public void setIdCategorie(List<Integer> idCategorie) {
        this.idCategorie = idCategorie;
    }

    /**
     * @param idDeposito
     *            the idDeposito to set
     */
    public void setIdDeposito(Integer idDeposito) {
        this.idDeposito = idDeposito;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    /**
     * @param idInstallazione
     *            the idInstallazione to set
     */
    public final void setIdInstallazione(Integer idInstallazione) {
        this.idInstallazione = idInstallazione;
    }

    /**
     * @param includiArticoliCategorieFiglie
     *            the includiArticoliCategorieFiglie to set
     */
    public void setIncludiArticoliCategorieFiglie(boolean includiArticoliCategorieFiglie) {
        this.includiArticoliCategorieFiglie = includiArticoliCategorieFiglie;
    }

    /**
     * @param lingua
     *            the lingua to set
     */
    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    /**
     * @param isLinguaAziendaParam
     *            () the isLinguaAzienda to set
     */
    public void setLinguaAzienda(boolean isLinguaAziendaParam) {
        this.isLinguaAzienda = isLinguaAziendaParam;
    }

    /**
     * @param ordinaPerAttributi
     *            the ordinaPerAttributi to set
     */
    public void setOrdinaPerAttributi(boolean ordinaPerAttributi) {
        this.ordinaPerAttributi = ordinaPerAttributi;
    }

    /**
     * @param ricercaAttributi
     *            the ricercaAttributi to set
     */
    public void setRicercaAttributi(List<ParametroRicercaAttributo> ricercaAttributi) {
        this.ricercaAttributi = ricercaAttributi;
    }

    /**
     * @param ricercaCodiceOrDescrizione
     *            the ricercaCodiceOrDescrizione to set
     */
    public void setRicercaCodiceOrDescrizione(boolean ricercaCodiceOrDescrizione) {
        this.ricercaCodiceOrDescrizione = ricercaCodiceOrDescrizione;
    }

    /**
     * @param soloDistinte
     *            the soloDistinte to set
     */
    public void setSoloDistinte(boolean soloDistinte) {
        this.soloDistinte = soloDistinte;
    }

    /**
     * @param soloDistinte
     *            the soloDistinte to set
     */
    public void setSoloDistinte(Boolean soloDistinte) {
        this.soloDistinte = soloDistinte;
    }

    /**
     * @param statoArticolo
     *            the statoArticolo to set
     */
    public void setStatoArticolo(StatoArticolo statoArticolo) {
        this.statoArticolo = statoArticolo;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("ParametriRicercaArticolo[ ").append(super.toString()).append(" codice = ").append(this.codice)
                .append(" descrizione = ").append(this.descrizione).append(" ]");
        return retValue.toString();
    }

}
