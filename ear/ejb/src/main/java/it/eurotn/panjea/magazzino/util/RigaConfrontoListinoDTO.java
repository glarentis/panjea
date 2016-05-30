package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Categoria;

/**
 * @author fattazzo
 *
 */
public class RigaConfrontoListinoDTO implements Serializable {

    private static final long serialVersionUID = -1448563176526047747L;

    private ArticoloLite articolo;

    private BigDecimal prezzoBase;
    private Integer numeroDecimaliPrezzo;

    private int numeroConfronti;

    private ValoreConfronto valoriConfronto1;
    private ValoreConfronto valoriConfronto2;
    private ValoreConfronto valoriConfronto3;
    private ValoreConfronto valoriConfronto4;
    private ValoreConfronto valoriConfronto5;
    private ValoreConfronto valoriConfronto6;
    private ValoreConfronto valoriConfronto7;
    private ValoreConfronto valoriConfronto8;
    private ValoreConfronto valoriConfronto9;
    private ValoreConfronto valoriConfronto10;

    {
        this.articolo = new ArticoloLite();
        this.articolo.setCategoria(new Categoria());

        valoriConfronto1 = new ValoreConfronto();
        valoriConfronto2 = new ValoreConfronto();
        valoriConfronto3 = new ValoreConfronto();
        valoriConfronto4 = new ValoreConfronto();
        valoriConfronto5 = new ValoreConfronto();
        valoriConfronto6 = new ValoreConfronto();
        valoriConfronto7 = new ValoreConfronto();
        valoriConfronto8 = new ValoreConfronto();
        valoriConfronto9 = new ValoreConfronto();
        valoriConfronto10 = new ValoreConfronto();

        numeroConfronti = 0;
    }

    /**
     * Costruttore.
     */
    public RigaConfrontoListinoDTO() {
        super();
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the numeroConfronti
     */
    public int getNumeroConfronti() {
        return numeroConfronti;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the prezzoBase
     */
    public BigDecimal getPrezzoBase() {
        return prezzoBase;
    }

    /**
     * @return the valoriConfronto1
     */
    public ValoreConfronto getValoriConfronto1() {
        return valoriConfronto1;
    }

    /**
     * @return the valoriConfronto10
     */
    public ValoreConfronto getValoriConfronto10() {
        return valoriConfronto10;
    }

    /**
     * @return the valoriConfronto2
     */
    public ValoreConfronto getValoriConfronto2() {
        return valoriConfronto2;
    }

    /**
     * @return the valoriConfronto3
     */
    public ValoreConfronto getValoriConfronto3() {
        return valoriConfronto3;
    }

    /**
     * @return the valoriConfronto4
     */
    public ValoreConfronto getValoriConfronto4() {
        return valoriConfronto4;
    }

    /**
     * @return the valoriConfronto5
     */
    public ValoreConfronto getValoriConfronto5() {
        return valoriConfronto5;
    }

    /**
     * @return the valoriConfronto6
     */
    public ValoreConfronto getValoriConfronto6() {
        return valoriConfronto6;
    }

    /**
     * @return the valoriConfronto7
     */
    public ValoreConfronto getValoriConfronto7() {
        return valoriConfronto7;
    }

    /**
     * @return the valoriConfronto8
     */
    public ValoreConfronto getValoriConfronto8() {
        return valoriConfronto8;
    }

    /**
     * @return the valoriConfronto9
     */
    public ValoreConfronto getValoriConfronto9() {
        return valoriConfronto9;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articolo.setCodice(codiceArticolo);
    }

    /**
     * @param codiceCategoria
     *            the codiceCategoria to set
     */
    public void setCodiceCategoria(String codiceCategoria) {
        this.articolo.getCategoria().setCodice(codiceCategoria);
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articolo.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param descrizioneCategoria
     *            the descrizioneCategoria to set
     */
    public void setDescrizioneCategoria(String descrizioneCategoria) {
        this.articolo.getCategoria().setDescrizione(descrizioneCategoria);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto1(String descrizione) {
        this.valoriConfronto1.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto10(String descrizione) {
        this.valoriConfronto10.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto2(String descrizione) {
        this.valoriConfronto2.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto3(String descrizione) {
        this.valoriConfronto3.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto4(String descrizione) {
        this.valoriConfronto4.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto5(String descrizione) {
        this.valoriConfronto5.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto6(String descrizione) {
        this.valoriConfronto6.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto7(String descrizione) {
        this.valoriConfronto7.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto8(String descrizione) {
        this.valoriConfronto8.setDescrizione(descrizione);
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizioneConfronto9(String descrizione) {
        this.valoriConfronto9.setDescrizione(descrizione);
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articolo.setId(idArticolo);
    }

    /**
     * @param idCategoria
     *            the idCategoria to set
     */
    public void setIdCategoria(Integer idCategoria) {
        this.articolo.getCategoria().setId(idCategoria);
    }

    /**
     * @param numeroConfronti
     *            the numeroConfronti to set
     */
    public void setNumeroConfronti(int numeroConfronti) {
        this.numeroConfronti = numeroConfronti;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
        this.valoriConfronto1.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto2.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto3.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto4.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto5.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto6.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto7.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto8.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto9.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        this.valoriConfronto10.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
    }

    /**
     * @param prezzoBase
     *            the prezzoBase to set
     */
    public void setPrezzoBase(BigDecimal prezzoBase) {
        this.prezzoBase = prezzoBase;
        this.valoriConfronto1.setPrezzoBase(prezzoBase);
        this.valoriConfronto2.setPrezzoBase(prezzoBase);
        this.valoriConfronto3.setPrezzoBase(prezzoBase);
        this.valoriConfronto4.setPrezzoBase(prezzoBase);
        this.valoriConfronto5.setPrezzoBase(prezzoBase);
        this.valoriConfronto6.setPrezzoBase(prezzoBase);
        this.valoriConfronto7.setPrezzoBase(prezzoBase);
        this.valoriConfronto8.setPrezzoBase(prezzoBase);
        this.valoriConfronto9.setPrezzoBase(prezzoBase);
        this.valoriConfronto10.setPrezzoBase(prezzoBase);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto1(BigDecimal prezzo) {
        this.valoriConfronto1.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto10(BigDecimal prezzo) {
        this.valoriConfronto10.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto2(BigDecimal prezzo) {
        this.valoriConfronto2.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto3(BigDecimal prezzo) {
        this.valoriConfronto3.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto4(BigDecimal prezzo) {
        this.valoriConfronto4.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto5(BigDecimal prezzo) {
        this.valoriConfronto5.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto6(BigDecimal prezzo) {
        this.valoriConfronto6.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto7(BigDecimal prezzo) {
        this.valoriConfronto7.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto8(BigDecimal prezzo) {
        this.valoriConfronto8.setPrezzo(prezzo);
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzoConfronto9(BigDecimal prezzo) {
        this.valoriConfronto9.setPrezzo(prezzo);
    }

}
