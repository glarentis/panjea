package it.eurotn.panjea.magazzino.util;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.Sconto;

/**
 * DTO di {@link RigaArticolo} .
 *
 * @author leonardo
 */
public class RigaArticoloDTO extends RigaMagazzinoDTO {

    private static final long serialVersionUID = -5995736582430171453L;

    private String codiceEntita;
    private Importo prezzoUnitario;
    private Importo prezzoUnitarioReale;
    private Double qta;
    private Importo prezzoNetto;
    private Sconto variazione;
    private Importo prezzoTotale;
    private Double qtaChiusa;

    private ArticoloLite articolo;

    private String noteRiga;

    private Integer idAreaMagazzinoCollegata;
    private Integer idAreaOrdineCollegata;

    private CodiceIva codiceIva;

    /**
     * Default contructor.
     */
    public RigaArticoloDTO() {
        super();
        articolo = new ArticoloLite();
        qtaChiusa = 0.0;
    }

    /**
     * @return articolo sulla riga
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the codiceEntita
     */
    public String getCodiceEntita() {
        return codiceEntita;
    }

    /**
     * @return Returns the codiceIva.
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return Returns the idAreaMagazzinoCollegata.
     */
    public Integer getIdAreaMagazzinoCollegata() {
        return idAreaMagazzinoCollegata;
    }

    /**
     * @return Returns the idAreaOrdineCollegata.
     */
    public Integer getIdAreaOrdineCollegata() {
        return idAreaOrdineCollegata;
    }

    /**
     * @return the noteRiga
     */
    public String getNoteRiga() {
        return noteRiga;
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
     * @return the prezzoUnitarioReale
     */
    public Importo getPrezzoUnitarioReale() {
        return prezzoUnitarioReale;
    }

    /**
     * @return the qta
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return the qtaChiusa
     */
    public double getQtaChiusa() {
        return qtaChiusa;
    }

    @Override
    public RigaMagazzino getRigaMagazzino() {
        RigaMagazzino riga = new RigaArticolo();

        // mi basta impostare l'id, i dati verranno ricaricati quando necessario
        riga.setId(getId());
        return riga;
    }

    /**
     * @return the variazione
     */
    public Sconto getVariazione() {
        return variazione;
    }

    /**
     * @return the chiusa
     */
    public boolean isChiusa() {
        boolean chiusa = false;
        if (qta != null) {
            chiusa = qta > 0.0 && (qta - qtaChiusa == 0.0);
        }
        return chiusa;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param barcodeArticolo
     *            barcode dell'articolo
     */
    public void setBarcodeArticolo(String barcodeArticolo) {
        this.articolo.setBarCode(barcodeArticolo);
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.articolo.setCodice(codice);
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(String codiceEntita) {
        this.codiceEntita = codiceEntita;
    }

    /**
     * @param codiceIva
     *            The codiceIva to set.
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param descrizioneArticolo
     *            descrizione dell'articolo
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articolo.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param idAreaMagazzinoCollegata
     *            The idAreaMagazzinoCollegata to set.
     */
    public void setIdAreaMagazzinoCollegata(Integer idAreaMagazzinoCollegata) {
        this.idAreaMagazzinoCollegata = idAreaMagazzinoCollegata;
    }

    /**
     * @param idAreaOrdineCollegata
     *            The idAreaOrdineCollegata to set.
     */
    public void setIdAreaOrdineCollegata(Integer idAreaOrdineCollegata) {
        this.idAreaOrdineCollegata = idAreaOrdineCollegata;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articolo.setId(idArticolo);
    }

    /**
     * @param noteRiga
     *            the noteRiga to set
     */
    public void setNoteRiga(String noteRiga) {
        this.noteRiga = noteRiga;
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
     * @param prezzoUnitarioReale
     *            the prezzoUnitarioReale to set
     */
    public void setPrezzoUnitarioReale(Importo prezzoUnitarioReale) {
        this.prezzoUnitarioReale = prezzoUnitarioReale;
    }

    /**
     * @param qta
     *            the qta to set
     */
    public void setQta(Double qta) {
        this.qta = qta;
    }

    /**
     * @param qtaChiusa
     *            the qtaChiusa to set
     */
    public void setQtaChiusa(double qtaChiusa) {
        this.qtaChiusa = qtaChiusa;
    }

    /**
     * @param sconto
     *            the variazione to set
     */
    public void setSconto(Sconto sconto) {
        this.variazione = sconto;
    }

}
