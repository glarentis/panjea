/**
 *
 */
package it.eurotn.panjea.tesoreria.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "part_effetti")
@NamedQueries({
        @NamedQuery(name = "Effetto.ricercaByAreaEffetti", query = " select e from Effetto e where e.areaEffetti.id = :paramIdAreaEffetti order by e.dataValuta "),
        @NamedQuery(name = "Effetto.ricercaByAccredito", query = " select e from Effetto e where e.areaEffetti.id = :paramIdAreaEffetti and e.dataValuta=:paramDataValuta order by e.dataValuta "),
        @NamedQuery(name = "Effetto.caricaNumeroAreeCollegate", query = "select count(eff) from Effetto eff join eff.areaEffetti ae where ae.id = :paramIdAreaEffetti and (eff.areaAccredito != null or eff.areaInsoluti != null)") })
public class Effetto extends EntityBase {

    /**
     * Enum che definisce lo stato dell'effetto.
     * <ul>
     * <li>PRESENTATO: equivalente allo stato IN_LAVORAZIONE di StatoRata, se l'effetto e' legato ad un pagmento che non
     * ha ancora associata la data di pagamento.</li>
     * <li>CHIUSO: equivalente allo stato CHIUSA di StatoRata, se l'effetto e' legato ad un pagamento che ha associata
     * la data di pagamento.</li>
     * <li>INSOLUTO: se l'effetto ha legato un pagamento insoluto.</li>
     * </ul>
     * 
     * @author Leonardo
     */
    public enum StatoEffetto {

        PRESENTATO, CHIUSO, INSOLUTO
    }

    private static final long serialVersionUID = 629201522096302620L;

    @Temporal(TemporalType.DATE)
    private Date dataValuta;

    @Column(length = 15)
    private String numeroEffetto;

    @ManyToOne(fetch = FetchType.LAZY)
    private AreaEffetti areaEffetti;

    @ManyToOne(fetch = FetchType.LAZY)
    private AreaInsoluti areaInsoluti;

    @ManyToOne(fetch = FetchType.LAZY)
    private AreaAccredito areaAccredito;

    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "effetto")
    private Set<Pagamento> pagamenti;

    @Column
    private BigDecimal speseInsoluto;

    @Column
    private BigDecimal spesePresentazione;

    @Temporal(TemporalType.DATE)
    private Date dataScadenza;

    /**
     * Mi serve per calcolare l'importo come somma dei pagamenti.
     */
    @Transient
    private Importo importo = null;

    /**
     * Giorno tra la data scadenza rata e la data valuta.
     */
    @Transient
    private Integer giorniBanca = null;

    @Transient
    private StatoEffetto statoEffetto = null;

    /**
     * Costruttore di default.
     */
    public Effetto() {
        super();
        initialize();
    }

    /**
     * 
     * Setta i giorno banca e li aggiunge alla data valuta.
     * 
     * @param paramGiorniBanca
     *            giorno banca da aggiungere e settare
     */
    public void addGiorniBanca(Integer paramGiorniBanca) {
        this.giorniBanca = paramGiorniBanca;
        Calendar calendarDataValuta = Calendar.getInstance();
        calendarDataValuta.setTime(dataScadenza);
        calendarDataValuta.add(Calendar.DAY_OF_MONTH, giorniBanca);
        dataValuta = calendarDataValuta.getTime();
    }

    /**
     * Valorizza l'importo transient dell'effetto.
     */
    private void aggiornaValoriTransient() {
        if (pagamenti == null) {
            // durante il deploy prova a creare una classe quindi mi trovo i
            // pagamenti a null.
            return;
        }
        // Un effetto deve sempre avere un pagamento.
        // i pagamenti dello stesso effetto hanno sempre lo stato uguale.
        // Infatti posso accreditare/presentare/rendere
        // insoluto solamente tutto l'effetto.
        // quindi per trovare lo stato dell'effetto posso testare solamente il
        // primo pagamenti.+
        Pagamento pagamento = pagamenti.iterator().next();
        if (pagamento.getDataPagamento() != null) {
            statoEffetto = StatoEffetto.CHIUSO;
        }
        if (pagamento.getDataPagamento() == null) {
            statoEffetto = StatoEffetto.PRESENTATO;
        }
        if (pagamento.isInsoluto()) {
            statoEffetto = StatoEffetto.INSOLUTO;
        }

        // Aggiorno il totale effetto.
        importo = new Importo(pagamento.getImporto().clone());
        importo.setImportoInValuta(BigDecimal.ZERO);
        importo.setImportoInValutaAzienda(BigDecimal.ZERO);

        for (Pagamento pagamentoPerImporto : getPagamenti()) {
            importo = importo.add(pagamentoPerImporto.getImporto(), 2);
        }
    }

    /**
     * @return the areaAccredito
     */
    public AreaAccredito getAreaAccredito() {
        return areaAccredito;
    }

    /**
     * @return the areaPartite
     */
    public AreaEffetti getAreaEffetti() {
        return areaEffetti;
    }

    /**
     * @return the areaPartiteInsoluto
     */
    public AreaInsoluti getAreaInsoluti() {
        return areaInsoluti;
    }

    /**
     * @return Returns the dataScadenza.
     */
    public Date getDataScadenza() {
        return dataScadenza;
    }

    /**
     * @return the dataValuta
     */
    public Date getDataValuta() {
        return dataValuta;
    }

    /**
     * 
     * @return Entità dell'effetto (l'entità del primo pagamento dell'effetto).
     */
    public EntitaLite getEntita() {
        return getPagamenti().iterator().next().getRata().getAreaRate().getDocumento().getEntita();
    }

    /**
     * @return the giorniBanca
     */
    public Integer getGiorniBanca() {
        return giorniBanca;
    }

    /**
     * @return the importo
     */
    public Importo getImporto() {
        if (importo == null) {
            aggiornaValoriTransient();
        }
        return importo;
    }

    /**
     * @return the numeroEffetto
     */
    public String getNumeroEffetto() {
        return numeroEffetto;
    }

    /**
     * @return the pagamento
     */
    public Set<Pagamento> getPagamenti() {
        return pagamenti;
    }

    /**
     * 
     * @return Ratadell'effetto (l'entità del primo pagamento dell'effetto).
     */
    public Rata getRata() {
        return getPagamenti().iterator().next().getRata();
    }

    /**
     *
     * @return Rate dell'effetto.
     */
    public Set<Rata> getRate() {
        Set<Rata> rate = new TreeSet<>();
        for (Iterator<Pagamento> pagamento = getPagamenti().iterator(); pagamento.hasNext();) {
            rate.add(pagamento.next().getRata());
        }
        return rate;
    }

    /**
     * @return the speseInsoluto
     */
    public BigDecimal getSpeseInsoluto() {
        return speseInsoluto;
    }

    /**
     * @return the spesePresentazione
     */
    public BigDecimal getSpesePresentazione() {
        return spesePresentazione;
    }

    /**
     * @return the statoEffetto
     */
    public StatoEffetto getStatoEffetto() {
        if (statoEffetto == null) {
            aggiornaValoriTransient();
        }
        return statoEffetto;
    }

    /**
     * Init delle proprieta' base di Effetto.
     */
    private void initialize() {
        this.pagamenti = new HashSet<Pagamento>();
        this.areaEffetti = new AreaEffetti();
        this.giorniBanca = new Integer(0);
    }

    /**
     * @param areaAccredito
     *            the areaAccredito to set
     */
    public void setAreaAccredito(AreaAccredito areaAccredito) {
        this.areaAccredito = areaAccredito;
    }

    /**
     * @param areaEffetti
     *            the AreaEffetti to set
     */
    public void setAreaEffetti(AreaEffetti areaEffetti) {
        this.areaEffetti = areaEffetti;
    }

    /**
     * @param areaInsoluti
     *            the AreaInsoluti to set
     */
    public void setAreaInsoluti(AreaInsoluti areaInsoluti) {
        this.areaInsoluti = areaInsoluti;
    }

    /**
     * @param dataScadenza
     *            The dataScadenza to set.
     */
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    /**
     * @param dataValuta
     *            the dataValuta to set
     */
    public void setDataValuta(Date dataValuta) {
        Date oldDate = this.dataValuta;
        this.dataValuta = dataValuta;
        if (oldDate != null && dataValuta != null && giorniBanca != null) {
            int diff = PanjeaEJBUtil.calculateDifference(oldDate, dataValuta);
            this.giorniBanca = giorniBanca + diff;
        }

    }

    /**
     * @param importo
     *            the importo to set
     */
    public void setImporto(Importo importo) {
        this.importo = importo;
    }

    /**
     * @param numeroEffetto
     *            the numeroEffetto to set
     */
    public void setNumeroEffetto(String numeroEffetto) {
        this.numeroEffetto = numeroEffetto;
    }

    /**
     * @param pagamenti
     *            the pagamenti to set
     */
    public void setPagamenti(Set<Pagamento> pagamenti) {
        this.pagamenti = pagamenti;
        if (pagamenti != null) {
            aggiornaValoriTransient();
        }
    }

    /**
     * @param speseInsoluto
     *            the speseInsoluto to set
     */
    public void setSpeseInsoluto(BigDecimal speseInsoluto) {
        this.speseInsoluto = speseInsoluto;
    }

    /**
     * @param spesePresentazione
     *            the spesePresentazione to set
     */
    public void setSpesePresentazione(BigDecimal spesePresentazione) {
        this.spesePresentazione = spesePresentazione;
    }

    /**
     * @param statoEffetto
     *            the statoEffetto to set
     */
    public void setStatoEffetto(StatoEffetto statoEffetto) {
        this.statoEffetto = statoEffetto;
    }

}
