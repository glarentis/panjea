package it.eurotn.panjea.contabilita.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.TipologiaCodiceIvaSpesometro;

/**
 * @author fattazzo
 *
 */
public class DocumentoSpesometroAggregato {

    private List<DocumentoSpesometro> documentiSpesometro;

    private int numeroOperazioniAttive;
    private int numeroOperazioniPassive;

    private BigDecimal imponibileFattureAttive;
    private BigDecimal impostaFattureAttive;
    private BigDecimal imponibileNoteCreditoAttive;
    private BigDecimal impostaNoteCreditoAttive;

    private BigDecimal imponibileFatturePassive;
    private BigDecimal impostaFatturePassive;
    private BigDecimal imponibileNoteCreditoPassive;
    private BigDecimal impostaNoteCreditoPassive;

    private boolean riepilogativo;

    private Anagrafica anagrafica;

    private TipologiaCodiceIvaSpesometro tipologiaCodiceIvaSpesometro;

    {
        documentiSpesometro = new ArrayList<DocumentoSpesometro>();

        numeroOperazioniAttive = 0;
        numeroOperazioniPassive = 0;

        imponibileFattureAttive = BigDecimal.ZERO;
        impostaFattureAttive = BigDecimal.ZERO;
        imponibileNoteCreditoAttive = BigDecimal.ZERO;
        impostaNoteCreditoAttive = BigDecimal.ZERO;

        imponibileFatturePassive = BigDecimal.ZERO;
        impostaFatturePassive = BigDecimal.ZERO;
        imponibileNoteCreditoPassive = BigDecimal.ZERO;
        impostaNoteCreditoPassive = BigDecimal.ZERO;

        riepilogativo = false;

        tipologiaCodiceIvaSpesometro = TipologiaCodiceIvaSpesometro.MERCE;
    }

    /**
     * Aggiunge un documento da aggregare.
     *
     * @param doc
     *            documento da aggiungere
     */
    public void addDocumento(DocumentoSpesometro doc) {

        this.documentiSpesometro.add(doc);

        // se almeno un'entità ( cliente o fornitore dell'anagrafica ) è riepilogativo, il documento aggregato è
        // considerato riepilogativo
        this.riepilogativo = this.riepilogativo || doc.getEntita().isRiepilogativo();
        this.tipologiaCodiceIvaSpesometro = doc.getTipologiaCodiceIvaSpesometro();
        this.anagrafica = doc.getEntita().getAnagrafica();

        if (doc.isAttivo()) {
            numeroOperazioniAttive = numeroOperazioniAttive + doc.getDocumentiAggregati();
            if (!doc.isNotaCredito()) {
                imponibileFattureAttive = imponibileFattureAttive.add(doc.getImponibile());
                impostaFattureAttive = impostaFattureAttive.add(doc.getImposta());
            } else {
                imponibileNoteCreditoAttive = imponibileNoteCreditoAttive.add(doc.getImponibile());
                impostaNoteCreditoAttive = impostaNoteCreditoAttive.add(doc.getImposta());
            }
        } else {
            numeroOperazioniPassive = numeroOperazioniPassive + doc.getDocumentiAggregati();
            if (!doc.isNotaCredito()) {
                imponibileFatturePassive = imponibileFatturePassive.add(doc.getImponibile());
                impostaFatturePassive = impostaFatturePassive.add(doc.getImposta());
            } else {
                imponibileNoteCreditoPassive = imponibileNoteCreditoPassive.add(doc.getImponibile());
                impostaNoteCreditoPassive = impostaNoteCreditoPassive.add(doc.getImposta());
            }
        }
    }

    /**
     * @return the anagrafica
     */
    public Anagrafica getAnagrafica() {
        return anagrafica;
    }

    /**
     * @return the imponibileFattureAttive
     */
    public BigDecimal getImponibileFattureAttive() {
        return imponibileFattureAttive;
    }

    /**
     * @return the imponibileFatturePassive
     */
    public BigDecimal getImponibileFatturePassive() {
        return imponibileFatturePassive;
    }

    /**
     * @return the imponibileNoteCreditoAttive
     */
    public BigDecimal getImponibileNoteCreditoAttive() {
        return imponibileNoteCreditoAttive;
    }

    /**
     * @return the imponibileNoteCreditoPassive
     */
    public BigDecimal getImponibileNoteCreditoPassive() {
        return imponibileNoteCreditoPassive;
    }

    /**
     * @return the impostaFattureAttive
     */
    public BigDecimal getImpostaFattureAttive() {
        return impostaFattureAttive;
    }

    /**
     * @return the impostaFatturePassive
     */
    public BigDecimal getImpostaFatturePassive() {
        return impostaFatturePassive;
    }

    /**
     * @return the impostaNoteCreditoAttive
     */
    public BigDecimal getImpostaNoteCreditoAttive() {
        return impostaNoteCreditoAttive;
    }

    /**
     * @return the impostaNoteCreditoPassive
     */
    public BigDecimal getImpostaNoteCreditoPassive() {
        return impostaNoteCreditoPassive;
    }

    /**
     * @return the numeroOperazioniAttive
     */
    public int getNumeroOperazioniAttive() {
        return numeroOperazioniAttive;
    }

    /**
     * @return the numeroOperazioniPassive
     */
    public int getNumeroOperazioniPassive() {
        return numeroOperazioniPassive;
    }

    /**
     * @return the tipologiaCodiceIvaSpesometro
     */
    public TipologiaCodiceIvaSpesometro getTipologiaCodiceIvaSpesometro() {
        return tipologiaCodiceIvaSpesometro;
    }

    /**
     * @return the riepilogativo
     */
    public boolean isRiepilogativo() {
        return riepilogativo;
    }
}
