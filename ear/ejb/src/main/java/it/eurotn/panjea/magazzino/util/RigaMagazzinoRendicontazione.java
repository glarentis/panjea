package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.util.RigaLottoRendicontazione;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRiga;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.util.PanjeaEJBUtil;

public class RigaMagazzinoRendicontazione implements Serializable {

    private static final long serialVersionUID = -342015876339387605L;

    private String tipo;

    private int livello;

    private String nota;

    private ArticoloLite articolo;

    private boolean articoloLibero;

    private List<AttributoRiga> attributi;

    private CodiceIva codiceIva;

    private String descrizione;

    private String descrizioneLingua;

    private Integer numeroDecimaliPrezzo;

    private Integer numeroDecimaliQta;

    private AgenteLite agente;

    private BigDecimal percProvvigione;

    private Importo prezzoUnitario;

    private Importo prezzoNetto;

    private Importo prezzoTotale;

    private Double qta;

    private Double qtaMagazzino;

    private List<RigaLottoRendicontazione> righeLottoRendicontazione;

    private String unitaMisura;

    private String unitaMisuraQtaMagazzino;

    private BigDecimal variazione1;

    private BigDecimal variazione2;

    private BigDecimal variazione3;

    private BigDecimal variazione4;

    private int progressivoRiga;

    {
        righeLottoRendicontazione = new ArrayList<RigaLottoRendicontazione>();
    }

    /**
     * Costruttore.
     *
     * @param rigaArticolo
     *            riga
     * @param progressivoRiga
     *            progressivo riga
     */
    public RigaMagazzinoRendicontazione(final RigaArticolo rigaArticolo, final int progressivoRiga) {
        super();
        PanjeaEJBUtil.copyProperties(this, rigaArticolo);
        setTipo("A");
        setProgressivoRiga(progressivoRiga);

        int progressivo = 1;
        for (RigaLotto rigaLotto : rigaArticolo.getRigheLotto()) {
            RigaLottoRendicontazione rigaLottoRendicontazione = new RigaLottoRendicontazione();
            PanjeaEJBUtil.copyProperties(rigaLottoRendicontazione, rigaLotto);
            rigaLottoRendicontazione.setRigaMagazzino(this);
            rigaLottoRendicontazione.setProgressivo(progressivo);

            righeLottoRendicontazione.add(rigaLottoRendicontazione);
            progressivo++;
        }
    }

    /**
     * Costruttore.
     *
     * @param rigaNota
     *            riga
     * @param progressivoRiga
     *            progressivo riga
     */
    public RigaMagazzinoRendicontazione(final RigaNota rigaNota, final int progressivoRiga) {
        super();
        PanjeaEJBUtil.copyProperties(this, rigaNota);
        setTipo("N");
        setProgressivoRiga(progressivoRiga);
    }

    /**
     * Costruttore.
     *
     * @param rigaTestata
     *            riga
     * @param progressivoRiga
     *            progressivo riga
     */
    public RigaMagazzinoRendicontazione(final RigaTestata rigaTestata, final int progressivoRiga) {
        super();
        PanjeaEJBUtil.copyProperties(this, rigaTestata);
        setTipo("T");
        setProgressivoRiga(progressivoRiga);
    }

    /**
     * @return the agente
     */
    public AgenteLite getAgente() {
        return agente;
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
    public List<AttributoRiga> getAttributi() {
        return attributi;
    }

    /**
     * @return the codiceIva
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the descrizioneLingua
     */
    public String getDescrizioneLingua() {
        return descrizioneLingua;
    }

    /**
     * @return the livello
     */
    public int getLivello() {
        return livello;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDecimaliQta
     */
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     * @return the percProvvigione
     */
    public BigDecimal getPercProvvigione() {
        return percProvvigione;
    }

    /**
     * @return the prezzoNetto
     */
    public Importo getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return the prezzoTotale
     */
    public Importo getPrezzoTotale() {
        return prezzoTotale;
    }

    /**
     * @return the prezzoUnitario
     */
    public Importo getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return the progressivoRiga
     */
    public int getProgressivoRiga() {
        return progressivoRiga;
    }

    /**
     * @return the qta
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return the qtaMagazzino
     */
    public Double getQtaMagazzino() {
        return qtaMagazzino;
    }

    /**
     * @return the righeLottoRendicontazione
     */
    public List<RigaLottoRendicontazione> getRigheLottoRendicontazione() {
        return righeLottoRendicontazione;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the unitaMisura
     */
    public String getUnitaMisura() {
        return unitaMisura;
    }

    /**
     * @return the unitaMisuraQtaMagazzino
     */
    public String getUnitaMisuraQtaMagazzino() {
        return unitaMisuraQtaMagazzino;
    }

    /**
     * @return the variazione1
     */
    public BigDecimal getVariazione1() {
        return variazione1;
    }

    /**
     * @return the variazione2
     */
    public BigDecimal getVariazione2() {
        return variazione2;
    }

    /**
     * @return the variazione3
     */
    public BigDecimal getVariazione3() {
        return variazione3;
    }

    /**
     * @return the variazione4
     */
    public BigDecimal getVariazione4() {
        return variazione4;
    }

    /**
     * @return the articoloLibero
     */
    public boolean isArticoloLibero() {
        return articoloLibero;
    }

    /**
     * @param agente
     *            the agente to set
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param articoloLibero
     *            the articoloLibero to set
     */
    public void setArticoloLibero(boolean articoloLibero) {
        this.articoloLibero = articoloLibero;
    }

    /**
     * @param attributi
     *            the attributi to set
     */
    public void setAttributi(List<AttributoRiga> attributi) {
        this.attributi = attributi;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param descrizioneLingua
     *            the descrizioneLingua to set
     */
    public void setDescrizioneLingua(String descrizioneLingua) {
        this.descrizioneLingua = descrizioneLingua;
    }

    /**
     * @param livello
     *            the livello to set
     */
    public void setLivello(int livello) {
        this.livello = livello;
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }

    /**
     * @param percProvvigione
     *            the percProvvigione to set
     */
    public void setPercProvvigione(BigDecimal percProvvigione) {
        this.percProvvigione = percProvvigione;
    }

    /**
     * @param prezzoNetto
     *            the prezzoNetto to set
     */
    public void setPrezzoNetto(Importo prezzoNetto) {
        this.prezzoNetto = prezzoNetto;
    }

    /**
     * @param prezzoTotale
     *            the prezzoTotale to set
     */
    public void setPrezzoTotale(Importo prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    /**
     * @param prezzoUnitario
     *            the prezzoUnitario to set
     */
    public void setPrezzoUnitario(Importo prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    /**
     * @param progressivoRiga
     *            the progressivoRiga to set
     */
    public void setProgressivoRiga(int progressivoRiga) {
        this.progressivoRiga = progressivoRiga;
    }

    /**
     * @param qta
     *            the qta to set
     */
    public void setQta(Double qta) {
        this.qta = qta;
    }

    /**
     * @param qtaMagazzino
     *            the qtaMagazzino to set
     */
    public void setQtaMagazzino(Double qtaMagazzino) {
        this.qtaMagazzino = qtaMagazzino;
    }

    /**
     * @param righeLottoRendicontazione
     *            the righeLottoRendicontazione to set
     */
    public void setRigheLottoRendicontazione(List<RigaLottoRendicontazione> righeLottoRendicontazione) {
        this.righeLottoRendicontazione = righeLottoRendicontazione;
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @param unitaMisura
     *            the unitaMisura to set
     */
    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    /**
     * @param unitaMisuraQtaMagazzino
     *            the unitaMisuraQtaMagazzino to set
     */
    public void setUnitaMisuraQtaMagazzino(String unitaMisuraQtaMagazzino) {
        this.unitaMisuraQtaMagazzino = unitaMisuraQtaMagazzino;
    }

    /**
     * @param variazione1
     *            the variazione1 to set
     */
    public void setVariazione1(BigDecimal variazione1) {
        this.variazione1 = variazione1;
    }

    /**
     * @param variazione2
     *            the variazione2 to set
     */
    public void setVariazione2(BigDecimal variazione2) {
        this.variazione2 = variazione2;
    }

    /**
     * @param variazione3
     *            the variazione3 to set
     */
    public void setVariazione3(BigDecimal variazione3) {
        this.variazione3 = variazione3;
    }

    /**
     * @param variazione4
     *            the variazione4 to set
     */
    public void setVariazione4(BigDecimal variazione4) {
        this.variazione4 = variazione4;
    }

}
