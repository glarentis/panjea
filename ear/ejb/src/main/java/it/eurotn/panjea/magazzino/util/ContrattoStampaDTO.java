package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto.Azione;
import it.eurotn.panjea.magazzino.domain.RigaContrattoStrategiaPrezzo.TipoValore;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 *
 */
public class ContrattoStampaDTO implements Serializable {

    public enum PesoArticolo {
        TUTTI, CATEGORIA, ARTICOLO
    }

    public enum PesoEntita {
        TUTTI, CATEGORIA, CATEGORIA_SEDE_NON_ERADITATA, CATEGORIA_SEDE_EREDITATA, SEDE, ENTITA
    }

    private static final Format[] DECIMAL_FORMAT = new Format[7];

    static {
        DECIMAL_FORMAT[0] = new DecimalFormat("###,###,###,##0");
        DECIMAL_FORMAT[1] = new DecimalFormat("###,###,###,##0.0");
        DECIMAL_FORMAT[2] = new DecimalFormat("###,###,###,##0.00");
        DECIMAL_FORMAT[3] = new DecimalFormat("###,###,###,##0.000");
        DECIMAL_FORMAT[4] = new DecimalFormat("###,###,###,##0.0000");
        DECIMAL_FORMAT[5] = new DecimalFormat("###,###,###,##0.00000");
        DECIMAL_FORMAT[6] = new DecimalFormat("###,###,###,##0.000000");
    }

    private static final long serialVersionUID = 323130386229800463L;

    private Contratto contratto;
    private String simboloValuta;

    private PesoEntita pesoEntita;
    private PesoArticolo pesoArticolo;

    private SedeEntita sedeEntita;
    private EntitaLite entita;
    private CategoriaSedeMagazzino categoriaSedeMagazzinoEntita;

    private CategoriaSedeMagazzino categoriaSedeMagazzino;
    private CategoriaCommercialeArticolo categoriaCommercialeContratto;

    private ArticoloLite articolo;
    private CategoriaCommercialeArticolo categoriaCommercialeArticolo;
    private CategoriaCommercialeArticolo categoriaCommercialeArticolo2;

    private boolean strategiaPrezzoAbilitata;
    private Azione azionePrezzo;
    private Double quantitaSogliaPrezzo;
    private BigDecimal valorePrezzo;
    private Integer numeroDecimaliPrezzo;
    private TipoValore tipoValorePrezzo;

    private boolean strategiaScontoAbilitata;
    private Azione azioneSconto;
    private Double quantitaSogliaSconto;
    private BigDecimal sconto1;
    private BigDecimal sconto2;
    private BigDecimal sconto3;
    private BigDecimal sconto4;

    {
        strategiaPrezzoAbilitata = false;
        strategiaScontoAbilitata = false;
    }

    /**
     * @return Categoria e/o Articolo a cui viene applicata la strategia prezzo/sconto
     */
    public String getApplicatoA() {
        String result = "";

        switch (pesoArticolo) {
        case TUTTI:
        case CATEGORIA:
            result = getCategoriaCommercialeContratto().getCodice();
            break;
        case ARTICOLO:
            result = getArticolo().getCodice() + " - " + getArticolo().getDescrizione();
            break;
        default:
            result = "";
            break;
        }

        return result;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the azionePrezzo
     */
    public Azione getAzionePrezzo() {
        return strategiaPrezzoAbilitata ? azionePrezzo : null;
    }

    /**
     * @return the azioneSconto
     */
    public Azione getAzioneSconto() {
        return strategiaScontoAbilitata ? azioneSconto : null;
    }

    /**
     * @return the categoriaCommercialeArticolo
     */
    public CategoriaCommercialeArticolo getCategoriaCommercialeArticolo() {
        return categoriaCommercialeArticolo;
    }

    /**
     * @return the categoriaCommercialeArticolo2
     */
    public CategoriaCommercialeArticolo getCategoriaCommercialeArticolo2() {
        return categoriaCommercialeArticolo2;
    }

    /**
     * @return the categoriaCommercialeContratto
     */
    public CategoriaCommercialeArticolo getCategoriaCommercialeContratto() {
        if (categoriaCommercialeContratto.isNew() && articolo.isNew()) {
            categoriaCommercialeContratto = new CategoriaCommercialeArticolo();
            categoriaCommercialeContratto.setCodice("Tutti gli articoli");
        }
        return categoriaCommercialeContratto;
    }

    /**
     * @return the categoriaSedeMagazzino
     */
    public CategoriaSedeMagazzino getCategoriaSedeMagazzino() {
        return categoriaSedeMagazzino;
    }

    /**
     * @return the categoriaSedeMagazzinoEntita
     */
    public CategoriaSedeMagazzino getCategoriaSedeMagazzinoEntita() {
        return categoriaSedeMagazzinoEntita;
    }

    /**
     * @return the contratto
     */
    public Contratto getContratto() {
        return contratto;
    }

    /**
     * @return descrizione della politica prezzo
     */
    public String getDescrizioneStrategiaPrezzo() {
        if (!strategiaPrezzoAbilitata) {
            return "";
        }

        StringBuilder sb = new StringBuilder(200);
        sb.append("<b>Prezzo: </b>");
        sb.append(azionePrezzo == Azione.SOSTITUZIONE ? "Sostituzione " : "Variazione ");
        sb.append("<b>");
        sb.append(DECIMAL_FORMAT[numeroDecimaliPrezzo].format(valorePrezzo));
        sb.append("</b>");
        sb.append(tipoValorePrezzo == TipoValore.IMPORTO ? simboloValuta : "% ");
        sb.append(" Soglia: <b>");
        sb.append(quantitaSogliaPrezzo);
        sb.append("</b>");

        return sb.toString();
    }

    /**
     * @return descrizione della politica sconto
     */
    public String getDescrizioneStrategiaSconto() {
        if (!strategiaScontoAbilitata) {
            return "";
        }

        StringBuilder sb = new StringBuilder(200);
        sb.append("<b>Sconto: </b>");
        sb.append(azioneSconto == Azione.SOSTITUZIONE ? "Sostituzione " : "Variazione ");
        sb.append(" sconto");
        sb.append("<b>");

        Sconto sconto = new Sconto(sconto1, sconto2, sconto3, sconto4);
        if (sconto.getSconto1() != null) {

            if (BigDecimal.ZERO.compareTo(sconto.getSconto1()) != 0) {
                sb.append(" ");
                sb.append(sconto.getSconto1());
            }
            if (sconto.getSconto2() != null) {
                if (BigDecimal.ZERO.compareTo(sconto.getSconto2()) != 0) {
                    sb.append(", ");
                    sb.append(sconto.getSconto2());
                }
                if (sconto.getSconto3() != null) {
                    if (BigDecimal.ZERO.compareTo(sconto.getSconto3()) != 0) {
                        sb.append(", ");
                        sb.append(sconto.getSconto3());
                    }
                    if (sconto.getSconto4() != null) {
                        if (BigDecimal.ZERO.compareTo(sconto.getSconto4()) != 0) {
                            sb.append(", ");
                            sb.append(sconto.getSconto4());
                        }
                    }
                }
            }
        }

        sb.append("</b>");
        sb.append(" Soglia: ");
        sb.append("<b>");
        sb.append(quantitaSogliaSconto);
        sb.append("</b>");

        return sb.toString();
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return entita interessata all'applicazione della strategia prezzo/articolo
     */
    public String getEntitaInteressata() {
        String result = "";

        switch (pesoEntita) {
        case TUTTI:
        case CATEGORIA:
            result = getCategoriaSedeMagazzino().getDescrizione();
            break;
        case CATEGORIA_SEDE_NON_ERADITATA:
        case CATEGORIA_SEDE_EREDITATA:
            result = getCategoriaSedeMagazzino().getDescrizione() + " (" + getSedeEntita().getSede().getDescrizione()
                    + ")";
            break;
        case SEDE:
            result = getEntita().getCodice() + " - " + getEntita().getAnagrafica().getDenominazione() + " ("
                    + getSedeEntita().getSede().getDescrizione() + ") ";
            break;
        case ENTITA:
            result = getEntita().getCodice() + " - " + getEntita().getAnagrafica().getDenominazione();
            break;
        default:
            result = "";
            break;
        }

        return result;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return ordinamento in base al peso dei contratti
     */
    public String getOrdinamento() {
        String pesoEntitaString = String.valueOf(pesoEntita.ordinal());
        String pesoArticoloString = String.valueOf(pesoArticolo.ordinal());
        return pesoEntitaString + pesoArticoloString;
    }

    /**
     * @return the pesoArticolo
     */
    public PesoArticolo getPesoArticolo() {
        return pesoArticolo;
    }

    /**
     * @return the pesoEntita
     */
    public PesoEntita getPesoEntita() {
        return pesoEntita;
    }

    /**
     * @return the quantitaSogliaPrezzo
     */
    public Double getQuantitaSogliaPrezzo() {
        return strategiaPrezzoAbilitata ? quantitaSogliaPrezzo : null;
    }

    /**
     * @return the quantitaSogliaSconto
     */
    public Double getQuantitaSogliaSconto() {
        return strategiaScontoAbilitata ? quantitaSogliaSconto : null;
    }

    /**
     * @return the sconto1
     */
    public BigDecimal getSconto1() {
        return strategiaScontoAbilitata ? sconto1 : null;
    }

    /**
     * @return the sconto2
     */
    public BigDecimal getSconto2() {
        return strategiaScontoAbilitata ? sconto2 : null;
    }

    /**
     * @return the sconto3
     */
    public BigDecimal getSconto3() {
        return strategiaScontoAbilitata ? sconto3 : null;
    }

    /**
     * @return the sconto4
     */
    public BigDecimal getSconto4() {
        return strategiaScontoAbilitata ? sconto4 : null;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the simboloValuta
     */
    public String getSimboloValuta() {
        return simboloValuta;
    }

    /**
     * @return the tipoValorePrezzo
     */
    public TipoValore getTipoValorePrezzo() {
        return strategiaPrezzoAbilitata ? tipoValorePrezzo : null;
    }

    /**
     * @return the valorePrezzo
     */
    public BigDecimal getValorePrezzo() {
        return strategiaPrezzoAbilitata ? valorePrezzo : null;
    }

    /**
     * @return <code>true</code> se il contratto risulta essere attivo alla data odierna
     */
    public boolean isContrattoAttivo() {
        Date today = PanjeaEJBUtil.getDateTimeToZero(Calendar.getInstance().getTime());
        return today.compareTo(contratto.getDataInizio()) >= 0 && today.compareTo(contratto.getDataFine()) <= 0;
    }

    /**
     * @return the strategiaPrezzoAbilitata
     */
    public boolean isStrategiaPrezzoAbilitata() {
        return strategiaPrezzoAbilitata;
    }

    /**
     * @return the strategiaScontoAbilitata
     */
    public boolean isStrategiaScontoAbilitata() {
        return strategiaScontoAbilitata;
    }

    /**
     *
     * @param articoloCodice
     *            articoloCodice
     */
    public void setArticoloCodice(String articoloCodice) {
        this.articolo.setCodice(articoloCodice);
    }

    /**
     *
     * @param articoloDescrizione
     *            articoloDescrizione
     */
    public void setArticoloDescrizione(String articoloDescrizione) {
        this.articolo.setDescrizione(articoloDescrizione);
    }

    /**
     *
     * @param articoloId
     *            articoloId
     */
    public void setArticoloId(Integer articoloId) {
        this.articolo = new ArticoloLite();
        this.articolo.setId(articoloId);
    }

    /**
     * @param azionePrezzoNum
     *            the azionePrezzoNum to set
     */
    public void setAzionePrezzo(Integer azionePrezzoNum) {
        this.azionePrezzo = Azione.values()[azionePrezzoNum];
    }

    /**
     * @param azioneScontoNum
     *            the azioneScontoNum to set
     */
    public void setAzioneSconto(Integer azioneScontoNum) {
        this.azioneSconto = Azione.values()[azioneScontoNum];
    }

    /**
     *
     * @param categoriaCommercialeArticolo2Codice
     *            categoriaCommercialeArticolo2Codice
     */
    public void setCategoriaCommercialeArticolo2Codice(String categoriaCommercialeArticolo2Codice) {
        this.categoriaCommercialeArticolo2.setCodice(categoriaCommercialeArticolo2Codice);
    }

    /**
     *
     * @param categoriaCommercialeArticolo2Id
     *            categoriaCommercialeArticolo2Id
     */
    public void setCategoriaCommercialeArticolo2Id(Integer categoriaCommercialeArticolo2Id) {
        this.categoriaCommercialeArticolo2 = new CategoriaCommercialeArticolo();
        this.categoriaCommercialeArticolo2.setId(categoriaCommercialeArticolo2Id);
    }

    /**
     *
     * @param categoriaCommercialeArticoloCodice
     *            categoriaCommercialeArticoloCodice
     */
    public void setCategoriaCommercialeArticoloCodice(String categoriaCommercialeArticoloCodice) {
        this.categoriaCommercialeArticolo.setCodice(categoriaCommercialeArticoloCodice);
    }

    /**
     *
     * @param categoriaCommercialeArticoloId
     *            categoriaCommercialeArticoloId
     */
    public void setCategoriaCommercialeArticoloId(Integer categoriaCommercialeArticoloId) {
        this.categoriaCommercialeArticolo = new CategoriaCommercialeArticolo();
        this.categoriaCommercialeArticolo.setId(categoriaCommercialeArticoloId);
    }

    /**
     *
     * @param categoriaCommercialeContrattoCodice
     *            categoriaCommercialeContrattoCodice
     */
    public void setCategoriaCommercialeContrattoCodice(String categoriaCommercialeContrattoCodice) {
        this.categoriaCommercialeContratto.setCodice(categoriaCommercialeContrattoCodice);
    }

    /**
     *
     * @param categoriaCommercialeContrattoId
     *            categoriaCommercialeContrattoId
     */
    public void setCategoriaCommercialeContrattoId(Integer categoriaCommercialeContrattoId) {
        this.categoriaCommercialeContratto = new CategoriaCommercialeArticolo();
        this.categoriaCommercialeContratto.setId(categoriaCommercialeContrattoId);
    }

    /**
     *
     * @param categoriaSedeDescrizione
     *            categoriaSedeDescrizione
     */
    public void setCategoriaSedeDescrizione(String categoriaSedeDescrizione) {

        // Se carico i contratti legati a tutte le categorie non setto mai l'id e quindi devo istanziare la categoria
        if (this.categoriaSedeMagazzino == null) {
            this.categoriaSedeMagazzino = new CategoriaSedeMagazzino();
        }
        this.categoriaSedeMagazzino.setDescrizione(categoriaSedeDescrizione);
    }

    /**
     *
     * @param categoriaSedeEntitaDescrizione
     *            categoriaSedeEntitaDescrizione
     */
    public void setCategoriaSedeEntitaDescrizione(String categoriaSedeEntitaDescrizione) {
        this.categoriaSedeMagazzinoEntita.setDescrizione(categoriaSedeEntitaDescrizione);
    }

    /**
     *
     * @param categoriaSedeEntitaId
     *            categoriaSedeEntitaId
     */
    public void setCategoriaSedeEntitaId(Integer categoriaSedeEntitaId) {
        this.categoriaSedeMagazzinoEntita = new CategoriaSedeMagazzino();
        this.categoriaSedeMagazzinoEntita.setId(categoriaSedeEntitaId);
    }

    /**
     *
     * @param categoriaSedeId
     *            categoriaSedeId
     */
    public void setCategoriaSedeId(Integer categoriaSedeId) {
        this.categoriaSedeMagazzino = new CategoriaSedeMagazzino();
        this.categoriaSedeMagazzino.setId(categoriaSedeId);
    }

    /**
     * @param classEntita
     *            classEntita
     */
    public void setClassEntita(String classEntita) {
        if ("C".equals(classEntita)) {
            entita = new ClienteLite();
        } else if ("F".equals(classEntita)) {
            entita = new FornitoreLite();
        } else if ("V".equals(classEntita)) {
            entita = new FornitoreLite();
        } else if ("A".equals(classEntita)) {
            entita = new AgenteLite();
        } else {
            entita = new EntitaLite();
        }
    }

    /**
     * @param codiceContratto
     *            codice della Contratto
     */
    public void setContrattoCodice(String codiceContratto) {
        contratto.setCodice(codiceContratto);
    }

    /**
     * @param dataFine
     *            data fine contatto
     */
    public void setContrattoDataFine(Date dataFine) {
        contratto.setDataFine(dataFine);
    }

    /**
     *
     * @param dataInizio
     *            data inizio contratto
     */
    public void setContrattoDataInizio(Date dataInizio) {
        contratto.setDataInizio(dataInizio);
    }

    /**
     *
     * @param descrizioneContratto
     *            descrizione Contratto
     */
    public void setContrattoDescrizione(String descrizioneContratto) {
        contratto.setDescrizione(descrizioneContratto);
    }

    /**
     *
     * @param idContratto
     *            id Contratto
     */
    public void setContrattoId(Integer idContratto) {
        contratto = new Contratto();
        contratto.setId(idContratto);
    }

    /**
     * @param codiceEntita
     *            codice della Entita
     */
    public void setEntitaCodice(Integer codiceEntita) {
        entita.setCodice(codiceEntita);
    }

    /**
     *
     * @param descrizioneEntita
     *            descrizione Entita
     */
    public void setEntitaDescrizione(String descrizioneEntita) {
        entita.getAnagrafica().setDenominazione(descrizioneEntita);
    }

    /**
     *
     * @param idEntita
     *            id Entita
     */
    public void setEntitaId(int idEntita) {
        entita.setId(idEntita);
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param pesoArticolo
     *            the pesoArticolo to set
     */
    public void setPesoArticolo(Integer pesoArticolo) {
        this.pesoArticolo = PesoArticolo.values()[pesoArticolo.intValue()];
    }

    /**
     * @param pesoEntita
     *            the pesoEntita to set
     */
    public void setPesoEntita(BigInteger pesoEntita) {
        this.pesoEntita = PesoEntita.values()[pesoEntita.intValue()];
    }

    /**
     * @param quantitaSogliaPrezzo
     *            the quantitaSogliaPrezzo to set
     */
    public void setQuantitaSogliaPrezzo(Double quantitaSogliaPrezzo) {
        this.quantitaSogliaPrezzo = quantitaSogliaPrezzo;
    }

    /**
     * @param quantitaSogliaSconto
     *            the quantitaSogliaSconto to set
     * @uml.property name="quantitaSogliaSconto"
     */
    public void setQuantitaSogliaSconto(Double quantitaSogliaSconto) {
        this.quantitaSogliaSconto = quantitaSogliaSconto;
    }

    /**
     * @param sconto1
     *            the sconto1 to set
     */
    public void setSconto1(BigDecimal sconto1) {
        this.sconto1 = ObjectUtils.defaultIfNull(sconto1, BigDecimal.ZERO);
    }

    /**
     * @param sconto2
     *            the sconto2 to set
     */
    public void setSconto2(BigDecimal sconto2) {
        this.sconto2 = ObjectUtils.defaultIfNull(sconto2, BigDecimal.ZERO);
    }

    /**
     * @param sconto3
     *            the sconto3 to set
     */
    public void setSconto3(BigDecimal sconto3) {
        this.sconto3 = ObjectUtils.defaultIfNull(sconto3, BigDecimal.ZERO);
    }

    /**
     * @param sconto4
     *            the sconto4 to set
     */
    public void setSconto4(BigDecimal sconto4) {
        this.sconto4 = ObjectUtils.defaultIfNull(sconto4, BigDecimal.ZERO);
    }

    /**
     * @param codiceSede
     *            codice della sede
     */
    public void setSedeCodice(String codiceSede) {
        sedeEntita.setCodice(codiceSede);
    }

    /**
     *
     * @param descrizioneSede
     *            descrizione sede
     */
    public void setSedeDescrizione(String descrizioneSede) {
        sedeEntita.getSede().setDescrizione(descrizioneSede);
    }

    /**
     *
     * @param idSede
     *            id sede
     */
    public void setSedeId(Integer idSede) {
        sedeEntita = new SedeEntita();
        sedeEntita.setId(idSede);
    }

    /**
     * @param simboloValuta
     *            simboloValuta
     */
    public void setSimboloValuta(String simboloValuta) {
        this.simboloValuta = simboloValuta;
    }

    /**
     * @param strategiaPrezzoAbilitata
     *            the strategiaPrezzoAbilitata to set
     */
    public void setStrategiaPrezzoAbilitata(boolean strategiaPrezzoAbilitata) {
        this.strategiaPrezzoAbilitata = strategiaPrezzoAbilitata;
    }

    /**
     * @param strategiaScontoAbilitata
     *            the strategiaScontoAbilitata to set
     */
    public void setStrategiaScontoAbilitata(boolean strategiaScontoAbilitata) {
        this.strategiaScontoAbilitata = strategiaScontoAbilitata;
    }

    /**
     * @param tipoValorePrezzoParam
     *            the tipoValorePrezzoParam to set
     */
    public void setTipoValorePrezzoNum(Integer tipoValorePrezzoParam) {
        this.tipoValorePrezzo = TipoValore.values()[tipoValorePrezzoParam];
    }

    /**
     * @param valorePrezzo
     *            the valorePrezzo to set
     */
    public void setValorePrezzo(BigDecimal valorePrezzo) {
        this.valorePrezzo = valorePrezzo;
    }

    @Override
    public String toString() {
        return "ContrattoStampaDTO [contratto=" + contratto.getCodice() + ", sedeEntita=" + sedeEntita.getCodice()
                + ", entita=" + entita.getCodice() + ", categoriaSedeMagazzino=" + categoriaSedeMagazzino
                + ", categoriaCommercialeContratto=" + categoriaCommercialeContratto + ", articolo=" + articolo
                + ", categoriaCommercialeArticolo=" + categoriaCommercialeArticolo + ", categoriaCommercialeArticolo2="
                + categoriaCommercialeArticolo2 + ", strategiaPrezzoAbilitata=" + strategiaPrezzoAbilitata
                + ", strategiaScontoAbilitata=" + strategiaScontoAbilitata + ", sconto1=" + sconto1 + ", sconto2="
                + sconto2 + ", sconto3=" + sconto3 + ", sconto4=" + sconto4 + "]";
    }
}
