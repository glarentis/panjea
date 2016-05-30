package it.eurotn.panjea.ordini.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticoloComponente;
import it.eurotn.panjea.ordini.domain.RigaArticoloDistinta;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

/**
 * @author leonardo
 */
public class RigaOrdineProduzioneDTOStampa implements Serializable {

    private static final long serialVersionUID = 7568363488094119472L;

    private AreaOrdine areaOrdine;

    private Integer idAreaOrdineCliente;
    private String codice;
    private String descrizione;
    private String unitaMisura;
    private String formula;
    private Double qta;
    private Double qtaAttrezzaggio;
    private Double qtaEvasione;
    private Double qtaOrdinatoFornitore;
    private Double qtaOrdinatoFornitoreArticolo;
    private Double qtaImpegnatoCliente;

    private Double giacenza;

    private boolean faseLavorazione;

    private Date dataProduzione;
    private Date dataConsegna;
    private Date dataEvasione;
    private boolean distinta;
    private Integer idDistinta;
    private Integer id;
    private Integer idArticolo;
    private String codiceDocumentoEvasione;
    private List<FaseLavorazioneArticolo> fasiLavorazioneArticolo;
    private List<Componente> componentiDistintaBase;
    private List<ConaiComponente> componentiConai;
    private String configurazioneDistinta;

    {
        distinta = false;
    }

    /**
     * Costruttore.
     */
    public RigaOrdineProduzioneDTOStampa() {
        super();
    }

    /**
     *
     * @param faseLavorazioneArticolo
     *            fase lav
     * @param paramIdDistinta
     *            id distinta
     */
    public RigaOrdineProduzioneDTOStampa(final FaseLavorazioneArticolo faseLavorazioneArticolo,
            final int paramIdDistinta) {
        setIdDistinta(paramIdDistinta);
        setCodice(faseLavorazioneArticolo.getFaseLavorazione().getCodice());
        setDescrizione(faseLavorazioneArticolo.getFaseLavorazione().getDescrizione());
        setDistinta(false);
        setFaseLavorazione(true);
    }

    /**
     * Costruttore.
     *
     * @param rigaOrdine
     *            riga da cui prendere i valori per riempire la riga produzione
     */
    public RigaOrdineProduzioneDTOStampa(final RigaOrdine rigaOrdine) {
        super();
        if (rigaOrdine instanceof RigaArticolo) {
            RigaArticolo rigaArticolo = (RigaArticolo) rigaOrdine;
            setId(rigaArticolo.getId());
            setIdArticolo(rigaArticolo.getArticolo().getId());
            setCodice(rigaArticolo.getArticolo().getCodice());
            setDescrizione(rigaArticolo.getDescrizione());
            setUnitaMisura(rigaArticolo.getUnitaMisura());
            setQta(rigaArticolo.getQta());
            setQtaAttrezzaggio(rigaArticolo.getQtaAttrezzaggio());
            setGiacenza(rigaArticolo.getGiacenza().getGiacenza());
            setDataConsegna(rigaArticolo.getDataConsegna());
            setDataProduzione(rigaArticolo.getDataProduzione());
            setFaseLavorazione(false);

            List<ConaiComponente> conaiComponenti = new ArrayList<ConaiComponente>();
            if (!rigaArticolo.getArticolo().getComponentiConai().isEmpty()) {
                conaiComponenti.addAll(rigaArticolo.getArticolo().getComponentiConai());
            } else {
                // dato che stampo il dettaglio e se non ci sono elementi non è possibile
                // fare stampare comunque un elemento, devo aggiungerne uno alla lista
                // nel caso non ce ne siano.
                conaiComponenti.add(new ConaiComponente());
            }
            setComponentiConai(conaiComponenti);

            if (rigaArticolo.getConfigurazioneDistinta() != null) {
                setConfigurazioneDistinta(rigaArticolo.getConfigurazioneDistinta().getNome());
            }
            if (rigaArticolo instanceof RigaArticoloDistinta) {
                setDistinta(true);
            }
            if (rigaArticolo instanceof RigaArticoloComponente) {
                String formulaComponente = ((RigaArticoloComponente) rigaArticolo).getFormulaComponente();
                setFormula(formulaComponente);
            }
        }
    }

    /**
     * @return the areaOrdine
     */
    public AreaOrdine getAreaOrdine() {
        return areaOrdine;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the codiceDocumentoEvasione
     */
    public String getCodiceDocumentoEvasione() {
        return codiceDocumentoEvasione;
    }

    /**
     * @return the componentiConai
     */
    public List<ConaiComponente> getComponentiConai() {
        return componentiConai;
    }

    /**
     * @return the componentiDistintaBase
     */
    public List<Componente> getComponentiDistintaBase() {
        return componentiDistintaBase;
    }

    /**
     * @return the configurazioneDistinta
     */
    public String getConfigurazioneDistinta() {
        return configurazioneDistinta;
    }

    /**
     * @return the dataConsegna
     */
    public Date getDataConsegna() {
        return dataConsegna;
    }

    /**
     * @return the dataEvasione
     */
    public Date getDataEvasione() {
        return dataEvasione;
    }

    /**
     * @return the dataProduzione
     */
    public Date getDataProduzione() {
        return dataProduzione;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the fasiLavorazioneArticolo
     */
    public List<FaseLavorazioneArticolo> getFasiLavorazioneArticolo() {
        fasiLavorazioneArticolo = ObjectUtils.defaultIfNull(fasiLavorazioneArticolo,
                new ArrayList<FaseLavorazioneArticolo>());
        return fasiLavorazioneArticolo;
    }

    /**
     * @return the formula
     */
    public String getFormula() {
        return formula;
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
     * @return the idAreaOrdineCliente
     */
    public Integer getIdAreaOrdineCliente() {
        return idAreaOrdineCliente;
    }

    /**
     * @return the idArticolo
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return the idDistinta
     */
    public Integer getIdDistinta() {
        return idDistinta;
    }

    /**
     * @return the qta
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return the qtaAttrezzaggio
     */
    public Double getQtaAttrezzaggio() {
        return qtaAttrezzaggio;
    }

    /**
     * @return the qtaEvasione
     */
    public Double getQtaEvasione() {
        return qtaEvasione;
    }

    /**
     * @return the qtaImpegnatoCliente
     */
    public Double getQtaImpegnatoCliente() {
        return qtaImpegnatoCliente;
    }

    /**
     * @return the qtaOrdinatoFornitore
     */
    public Double getQtaOrdinatoFornitore() {
        return qtaOrdinatoFornitore;
    }

    /**
     * @return Returns the qtaOrdinatoFornitoreArticolo.
     */
    public Double getQtaOrdinatoFornitoreArticolo() {
        return qtaOrdinatoFornitoreArticolo;
    }

    /**
     * @return the unitaMisura
     */
    public String getUnitaMisura() {
        return unitaMisura;
    }

    /**
     * @return the distinta
     */
    public boolean isDistinta() {
        return distinta;
    }

    /**
     * @param areaOrdine
     *            the areaOrdine to set
     */
    public void setAreaOrdine(AreaOrdine areaOrdine) {
        this.areaOrdine = areaOrdine;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceDocumentoEvasione
     *            the codiceDocumentoEvasione to set
     */
    public void setCodiceDocumentoEvasione(String codiceDocumentoEvasione) {
        this.codiceDocumentoEvasione = codiceDocumentoEvasione;
    }

    /**
     * @param componentiConai
     *            the componentiConai to set
     */
    public void setComponentiConai(List<ConaiComponente> componentiConai) {
        this.componentiConai = componentiConai;
    }

    /**
     * @param componentiDistintaBase
     *            the componentiDistintaBase to set
     */
    public void setComponentiDistintaBase(List<Componente> componentiDistintaBase) {
        this.componentiDistintaBase = componentiDistintaBase;
    }

    /**
     * @param configurazioneDistinta
     *            the configurazioneDistinta to set
     */
    public void setConfigurazioneDistinta(String configurazioneDistinta) {
        this.configurazioneDistinta = configurazioneDistinta;
    }

    /**
     * @param dataConsegna
     *            the dataConsegna to set
     */
    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    /**
     * @param dataEvasione
     *            the dataEvasione to set
     */
    public void setDataEvasione(Date dataEvasione) {
        this.dataEvasione = dataEvasione;
    }

    /**
     * @param dataProduzione
     *            the dataProduzione to set
     */
    public void setDataProduzione(Date dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param distinta
     *            the distinta to set
     */
    public void setDistinta(boolean distinta) {
        this.distinta = distinta;
    }

    /**
     * @param fasiLavorazioneArticolo
     *            the fasiLavorazioneArticolo to set
     */
    public void setFasiLavorazioneArticolo(List<FaseLavorazioneArticolo> fasiLavorazioneArticolo) {
        this.fasiLavorazioneArticolo = fasiLavorazioneArticolo;
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
     * @param idAreaOrdineCliente
     *            the idAreaOrdineCliente to set
     */
    public void setIdAreaOrdineCliente(Integer idAreaOrdineCliente) {
        this.idAreaOrdineCliente = idAreaOrdineCliente;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idDistinta
     *            the idDistinta to set
     */
    public void setIdDistinta(Integer idDistinta) {
        this.idDistinta = idDistinta;
    }

    /**
     * @param qta
     *            the qta to set
     */
    public void setQta(Double qta) {
        this.qta = qta;
    }

    /**
     * @param qtaAttrezzaggio
     *            the qtaAttrezzaggio to set
     */
    public void setQtaAttrezzaggio(Double qtaAttrezzaggio) {
        this.qtaAttrezzaggio = qtaAttrezzaggio;
    }

    /**
     * @param qtaEvasione
     *            the qtaEvasione to set
     */
    public void setQtaEvasione(Double qtaEvasione) {
        this.qtaEvasione = qtaEvasione;
    }

    /**
     * @param qtaImpegnatoCliente
     *            the qtaImpegnatoCliente to set
     */
    public void setQtaImpegnatoCliente(Double qtaImpegnatoCliente) {
        this.qtaImpegnatoCliente = qtaImpegnatoCliente;
    }

    /**
     * @param qtaOrdinatoFornitore
     *            the qtaOrdinatoFornitore to set
     */
    public void setQtaOrdinatoFornitore(Double qtaOrdinatoFornitore) {
        this.qtaOrdinatoFornitore = qtaOrdinatoFornitore;
    }

    /**
     * @param qtaOrdinatoFornitoreArticolo
     *            The qtaOrdinatoFornitoreArticolo to set.
     */
    public void setQtaOrdinatoFornitoreArticolo(Double qtaOrdinatoFornitoreArticolo) {
        this.qtaOrdinatoFornitoreArticolo = qtaOrdinatoFornitoreArticolo;
    }

    /**
     * @param unitaMisura
     *            the unitaMisura to set
     */
    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

	public boolean isFaseLavorazione() {
		return faseLavorazione;
	}

	public void setFaseLavorazione(boolean faseLavorazione) {
		this.faseLavorazione = faseLavorazione;
	}

}
