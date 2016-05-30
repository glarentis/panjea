package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public interface IRigaArticoloDocumento {

    /**
     * Dalla politica prezzo impostata sulla riga calcola il prezzoUnitario, gli sconti ed il numero decimali del prezzo
     * unitario.<br/>
     * Se non esiste una politica prezzo prezzo e sconti vengono impostati a zero ed il numero decimali a quello
     * impostato nell'anagarfica articolo
     */
    void applicaPoliticaPrezzo();

    /**
     * Dalla politica prezzo impostata sulla riga calcola il prezzoUnitario, gli sconti ed il numero decimali del prezzo
     * unitario basato sulla qta.
     *
     * @param qta
     *            qta da applicare
     */
    void applicaPoliticaPrezzo(double qta);

    /**
     *
     * @param attributoArticolo
     *            attributo articolo dal quale ricavare l'attributo riga
     * @return attributoRiga associato alla riga e con i parametri preimpostati dall'attributo articolo
     */
    AttributoRigaArticolo creaAttributoRiga(AttributoArticolo attributoArticolo);

    /**
     * @return the articolo
     */
    ArticoloLite getArticolo();

    /**
     *
     * @return attributi della riga
     */
    List<? extends AttributoRigaArticolo> getAttributi();

    /**
     * Restituisce la descrizione di tutti gli attributi generata con il seguente formato:<br>
     * Att1.codice:Att1.descrizione Att2.codice:Att2.descrizione ecc....
     *
     * @param lingua
     *            lingua della descrizione degli attributi. Se null viene usata quella aziendale
     *
     * @return Descrizione generata
     */
    String getAttributiDescrizione(String lingua);

    /**
     * @return the categoriaContabileArticolo
     */
    CategoriaContabileArticolo getCategoriaContabileArticolo();

    /**
     * @return the codiceIva
     */
    CodiceIva getCodiceIva();

    /**
     * Se la rigaArticolo gestisce componenti ritorna la lista dei componenti.
     *
     * @return lista di componenti legati alla riga
     */
    Set<IRigaArticoloDocumento> getComponenti();

    /**
     * @return the descrizione
     */
    String getDescrizione();

    /**
     * @return the descrizioneLingua
     */
    String getDescrizioneLingua();

    /**
     * @return the descrizionePoliticaPrezzo
     */
    DescrizionePoliticaPrezzo getDescrizionePoliticaPrezzo();

    /**
     * @param stampaAttributi
     *            true se devo stampare la riga con uno stile predefinito.
     * @param stampaNote
     *            true se voglio aggiungere le note alla descrizione.
     * @return descrizione della riga.
     */
    String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote);

    /**
     * @return formula conversione unita misura
     */
    String getFormulaConversioneUnitaMisura();

    /**
     * @return the formulaTrasformazione
     */
    FormulaTrasformazione getFormulaTrasformazione();

    /**
     * @return the formulaTrasformazioneQtaMagazzino
     */
    FormulaTrasformazione getFormulaTrasformazioneQtaMagazzino();

    /**
     * @return giacenza dell'articolo quado creo la riga.
     */
    Giacenza getGiacenza();

    /**
     * @return the noteLinguaRiga
     */
    String getNoteLinguaRiga();

    /**
     * @return the noteRiga
     */
    String getNoteRiga();

    /**
     * @return the numeroDecimaliPrezzo
     */
    Integer getNumeroDecimaliPrezzo();

    /**
     * @return the numeroDecimaliQta
     */
    Integer getNumeroDecimaliQta();

    /**
     * @return Returns the percProvvigione.
     */
    BigDecimal getPercProvvigione();

    /**
     * @return the politicaPrezzo
     */
    PoliticaPrezzo getPoliticaPrezzo();

    /**
     * @return prezzo iniziale dell'articolo
     */
    BigDecimal getPrezzoDeterminato();

    /**
     * Calcola il prezzo netto ivato.
     *
     * @return prezzo ivato
     */
    BigDecimal getPrezzoIvato();

    /**
     * Calcola il prezzo netto ivato in valuta azienda.
     *
     * @return prezzo ivato
     */
    BigDecimal getPrezzoIvatoInValutaAzienda();

    /**
     * @return the prezzoNetto
     */
    Importo getPrezzoNetto();

    /**
     * @return the prezzoTotale
     */
    Importo getPrezzoTotale();

    /**
     * @return the prezzoUnitario
     */
    Importo getPrezzoUnitario();

    /**
     * @return the qta
     */
    Double getQta();

    /**
     *
     * @return qtaMagazzino
     */
    Double getQtaMagazzino();

    /**
     * @return Returns the tipoOmaggio.
     */
    TipoOmaggio getTipoOmaggio();

    /**
     * @return the unitaMisura
     */
    String getUnitaMisura();

    /**
     * @return the unitaMisuraQtaMagazzino
     */
    String getUnitaMisuraQtaMagazzino();

    /**
     * Restituisce il codice del codice attributo passato ocme paramentro.
     *
     * @param codiceAttributo
     *            codice attributo
     * @return valore del codice attributo trovato, <code>null</code> altrimenti
     */
    String getValoreAttributo(String codiceAttributo);

    /**
     * @param codiceAttributo
     *            il codice dell'attributo da recuperare
     * @param returnType
     *            tipo richiesto
     * @param <T>
     *            tipo
     * @return the valore tipizzato
     */
    <T> T getValoreAttributoTipizzato(String codiceAttributo, Class<T> returnType);

    /**
     * @return the variazione1
     */
    BigDecimal getVariazione1();

    /**
     * @return the variazione2
     */
    BigDecimal getVariazione2();

    /**
     * @return the variazione3
     */
    BigDecimal getVariazione3();

    /**
     * @return the variazione4
     */
    BigDecimal getVariazione4();

    /**
     * @return the articoloLibero
     */
    boolean isArticoloLibero();

    /**
     * @return the noteSuDestinazione
     */
    boolean isNoteSuDestinazione();

    /**
     *
     * @return true se lo sconto 1 è bloccato
     */
    boolean isSconto1Bloccato();

    /**
     * @param articolo
     *            the articolo to set
     */
    void setArticolo(ArticoloLite articolo);

    /**
     * @param articoloLibero
     *            the articoloLibero to set
     */
    void setArticoloLibero(boolean articoloLibero);

    /**
     *
     * @param attributi
     *            atributi to set.
     */
    void setAttributi(List<? extends AttributoRigaArticolo> attributi);

    /**
     * @param categoriaContabileArticolo
     *            the categoriaContabileArticolo to set
     */
    void setCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo);

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    void setCodiceIva(CodiceIva codiceIva);

    /**
     * Se la rigaArticolo gestisce componenti setta la lista dei componenti.
     *
     * @param componenti
     *            componenti da settare.
     */
    void setComponenti(Set<IRigaArticoloDocumento> componenti);

    /**
     * @param descrizione
     *            the descrizione to set
     */
    void setDescrizione(String descrizione);

    /**
     * @param descrizioneLingua
     *            the descrizioneLingua to set
     */
    void setDescrizioneLingua(String descrizioneLingua);

    /**
     * @param formulaConversioneUnitaMisura
     *            the formulaConversioneUnitaMisura to set
     */
    void setFormulaConversioneUnitaMisura(String formulaConversioneUnitaMisura);

    /**
     * @param formulaTrasformazione
     *            the formulaTrasformazione to set
     */
    void setFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

    /**
     * @param formulaTrasformazioneQtaMagazzino
     *            the formulaTrasformazioneQtaMagazzino to set
     */
    void setFormulaTrasformazioneQtaMagazzino(FormulaTrasformazione formulaTrasformazioneQtaMagazzino);

    /**
     * @param giacenza
     *            setta la giacenza dell'articolo
     */
    void setGiacenza(Giacenza giacenza);

    /**
     * @param noteLinguaRiga
     *            noteLinguaRiga
     */
    void setNoteLinguaRiga(String noteLinguaRiga);

    /**
     * @param noteRiga
     *            the noteRiga to set
     */
    void setNoteRiga(String noteRiga);

    /**
     * @param noteSuDestinazione
     *            the noteSuDestinazione to set
     */
    void setNoteSuDestinazione(boolean noteSuDestinazione);

    /**
     * @param numeroDecimaliPrezzo
     *            numero decimali per il prezzo
     */
    void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo);

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    void setNumeroDecimaliQta(Integer numeroDecimaliQta);

    /**
     * @param percProvvigione
     *            The percProvvigione to set.
     */
    public void setPercProvvigione(BigDecimal percProvvigione);

    /**
     * @param politicaPrezzo
     *            the politicaPrezzo to set
     */
    void setPoliticaPrezzo(PoliticaPrezzo politicaPrezzo);

    /**
     * @param prezzoDeterminato
     *            setta il prezzo determinato
     */
    void setPrezzoDeterminato(BigDecimal prezzoDeterminato);

    /**
     * @param prezzoUnitarioNew
     *            the prezzoUnitario to set
     */
    void setPrezzoUnitario(Importo prezzoUnitarioNew);

    /**
     * @param qta
     *            the qta to set
     */
    void setQta(Double qta);

    /**
     * @param qtaMagazzino
     *            the qtaMagazzino to set
     */
    void setQtaMagazzino(Double qtaMagazzino);

    /**
     * @param resa
     *            the resa to set
     */
    void setResa(Double resa);

    /**
     * @param somministrazione
     *            The somministrazione to set.
     */
    public void setSomministrazione(boolean somministrazione);

    /**
     * @param strategiaTotalizzazioneDocumento
     *            the strategiaTotalizzazioneDocumento to set
     */
    void setStrategiaTotalizzazioneDocumento(StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento);

    /**
     * @param tipoOmaggio
     *            The tipoOmaggio to set.
     */
    void setTipoOmaggio(TipoOmaggio tipoOmaggio);

    /**
     * @param unitaMisura
     *            the unitaMisura to set
     */
    void setUnitaMisura(String unitaMisura);

    /**
     * @param unitaMisuraQtaMagazzino
     *            the unitaMisuraQtaMagazzino to set
     */
    void setUnitaMisuraQtaMagazzino(String unitaMisuraQtaMagazzino);

    /**
     * @param variazione1
     *            the variazione1 to set
     */
    void setVariazione1(BigDecimal variazione1);

    /**
     * @param variazione2
     *            the variazione2 to set
     */
    void setVariazione2(BigDecimal variazione2);

    /**
     * @param variazione3
     *            the variazione3 to set
     */
    void setVariazione3(BigDecimal variazione3);

    /**
     * @param variazione4
     *            the variazione4 to set
     */
    void setVariazione4(BigDecimal variazione4);

}