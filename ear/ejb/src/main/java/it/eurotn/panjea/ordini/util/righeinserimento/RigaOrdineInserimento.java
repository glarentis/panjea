package it.eurotn.panjea.ordini.util.righeinserimento;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.util.PanjeaEJBUtil;

public class RigaOrdineInserimento implements Serializable {

    private static final long serialVersionUID = 5915803038515047502L;

    private ArticoloLite articolo;

    private Date dataIniziale;

    private Date dataFinale;

    private Double qta;
    private Map<String, AttributoRigaArticolo> attributi;

    private Double qtaInserimento;
    private Map<String, AttributoRigaArticolo> attributiInserimento;

    private Integer numeroDecimaliQuantita;

    private String idRigheOrdine;

    private boolean presenteInOrdine;

    {
        qta = 0.0;
        qtaInserimento = 0.0;
        idRigheOrdine = "";

        numeroDecimaliQuantita = 2;

        presenteInOrdine = false;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the attributi
     */
    public Map<String, AttributoRigaArticolo> getAttributi() {
        return attributi;
    }

    /**
     * @return the attributiInserimento
     */
    public Map<String, AttributoRigaArticolo> getAttributiInserimento() {
        return attributiInserimento;
    }

    /**
     * @return the dataFinale
     */
    public Date getDataFinale() {
        return dataFinale;
    }

    /**
     * @return the dataIniziale
     */
    public Date getDataIniziale() {
        return dataIniziale;
    }

    /**
     * @return the idRigheOrdine
     */
    public String getIdRigheOrdine() {
        return idRigheOrdine;
    }

    /**
     * @return the numeroDecimaliQuantita
     */
    public Integer getNumeroDecimaliQuantita() {
        return numeroDecimaliQuantita;
    }

    /**
     * @return the qta
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return the qtaInserimento
     */
    public Double getQtaInserimento() {
        return qtaInserimento;
    }

    /**
     * Controlla se la riga non ha valori impostati per quantità e attributi.<br>
     * Per gli attributi numerici verrà testato il valore diverso da 0, per gli altri verrà testato il valore diverso da
     * quello presente negli attributi originali.
     *
     * @return <code>true</code> se non è stato impostato alcun valore
     */
    public boolean isEmpty() {

        boolean empty = true;

        // controllo subito la quantità
        empty = (qtaInserimento == null || qtaInserimento.compareTo(0.0) == 0);

        // verifico ad uno ad uno tutti gli attributi
        if (empty && attributiInserimento != null) {
            for (Entry<String, AttributoRigaArticolo> attrIns : attributiInserimento.entrySet()) {
                switch (attrIns.getValue().getTipoAttributo().getTipoDato()) {
                case NUMERICO:
                    // dato numerico quindi controllo solamente che sia diverso da 0
                    if (!StringUtils.isBlank(attrIns.getValue().getValore())
                            && attrIns.getValue().getValoreTipizzato(Double.class).compareTo(0.0) != 0) {
                        empty = false;
                        break;
                    }
                    break;
                default:
                    // il dato non è numerico quindi controllo solamente se è cambiato dall'originale
                    AttributoRigaArticolo attr = attributi.get(attrIns.getKey());
                    if (!Objects.equals(attrIns.getValue().getValore(), attr.getValore())) {
                        empty = false;
                        break;
                    }
                    break;
                }
            }
        }

        return empty;
    }

    /**
     * @return the presenteInOrdine
     */
    public boolean isPresenteInOrdine() {
        return presenteInOrdine;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param attributi
     *            the attributi to set
     */
    public void setAttributi(Map<String, AttributoRigaArticolo> attributi) {
        this.attributi = attributi;

        this.attributiInserimento = new HashMap<String, AttributoRigaArticolo>();
        for (Entry<String, AttributoRigaArticolo> attributo : attributi.entrySet()) {
            AttributoRigaArticolo attributoInserimento = new AttributoRiga();
            PanjeaEJBUtil.copyProperties(attributoInserimento, attributo.getValue());
            if (attributo.getValue().getTipoAttributo().getTipoDato() == ETipoDatoTipoAttributo.NUMERICO) {
                attributoInserimento.setValore("0");
            }
            attributiInserimento.put(attributo.getKey(), attributoInserimento);
        }
    }

    /**
     * @param attributiInserimento
     *            the attributiInserimento to set
     */
    public void setAttributiInserimento(Map<String, AttributoRigaArticolo> attributiInserimento) {
        this.attributiInserimento = attributiInserimento;
    }

    /**
     * @param codiceArticolo
     *            the codice articolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articolo.setCodice(codiceArticolo);
    }

    /**
     * @param dataFinale
     *            the dataFinale to set
     */
    public void setDataFinale(Date dataFinale) {
        this.dataFinale = dataFinale;
    }

    /**
     * @param dataIniziale
     *            the dataIniziale to set
     */
    public void setDataIniziale(Date dataIniziale) {
        this.dataIniziale = dataIniziale;
    }

    /**
     * @param descrizioneArticolo
     *            the descrizione articolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articolo.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param idArticolo
     *            the id articolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articolo = new ArticoloLite();
        this.articolo.setId(idArticolo);
    }

    /**
     * @param idRigheOrdine
     *            the idRigheOrdine to set
     */
    public void setIdRigheOrdine(String idRigheOrdine) {
        this.idRigheOrdine = idRigheOrdine;
    }

    /**
     * @param numeroDecimaliQuantita
     *            the numeroDecimaliQuantita to set
     */
    public void setNumeroDecimaliQuantita(Integer numeroDecimaliQuantita) {
        this.numeroDecimaliQuantita = numeroDecimaliQuantita;
    }

    /**
     * @param presenteInOrdine
     *            the presenteInOrdine to set
     */
    public void setPresenteInOrdine(boolean presenteInOrdine) {
        this.presenteInOrdine = presenteInOrdine;
    }

    /**
     * @param qta
     *            the qta to set
     */
    public void setQta(Double qta) {
        this.qta = qta;
    }

    /**
     * @param qtaInserimento
     *            the qtaInserimento to set
     */
    public void setQtaInserimento(Double qtaInserimento) {
        this.qtaInserimento = qtaInserimento;
    }
}
