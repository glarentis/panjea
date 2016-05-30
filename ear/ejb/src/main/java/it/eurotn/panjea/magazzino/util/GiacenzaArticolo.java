package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

/**
 * Contiene i dati relativi alla giacenza di un articolo.<br/>
 * Contiene anche altri dati di utilità per visualizzare la giacenza, ad esempio la categoria di un articolo.
 *
 * @author Leonardo
 */
public class GiacenzaArticolo implements Serializable {

    private static final long serialVersionUID = 2991047777672069660L;

    protected ArticoloLite articoloLite = null;

    protected CategoriaLite categoriaLite = null;

    protected DepositoLite depositoLite = null;

    protected Double qtaMagazzinoCaricoTotale = 0.0;

    protected Double qtaMagazzinoScaricoTotale = 0.0;

    protected Double qtaMagazzinoCarico = 0.0;

    protected Double qtaMagazzinoScarico = 0.0;

    protected Double qtaMagazzinoCaricoAltro = 0.0;

    protected Double qtaMagazzinoScaricoAltro = 0.0;

    protected Double qtaMovimentata = 0.0;

    protected Double qtaInventario = 0.0;

    /**
     * Costruttore di default.
     */
    public GiacenzaArticolo() {
        initialize();
    }

    /**
     * 
     * @param idArticolo
     *            idArticolo per la giacenza
     * @param codiceArticolo
     *            codiceArticolo per la giacenza
     * @param descrizioneArticolo
     *            descrizioneArticolo per la giacenza
     * @param idCategoria
     *            idCategoria per giacenza
     * @param codiceCategoria
     *            codiceCategoria per la giacenza
     * @param descrizioneCategoria
     *            descrizioneCategoria per la giacenza
     * @param idDeposito
     *            idDeposito per giacenza
     * @param codiceDeposito
     *            codiceDeposito per la giacenza
     * @param descrizioneDeposito
     *            descrizioneDeposito per la giacenza
     */
    public GiacenzaArticolo(final Integer idArticolo, final String codiceArticolo, final String descrizioneArticolo,
            final Integer idCategoria, final String codiceCategoria, final String descrizioneCategoria,
            final Integer idDeposito, final String codiceDeposito, final String descrizioneDeposito) {
        super();
        initialize();
        this.setCodiceArticolo(codiceArticolo);
        this.setDescrizioneArticolo(descrizioneArticolo);
        this.setIdArticolo(idArticolo);

        this.setIdCategoria(idCategoria);
        this.setCodiceCategoria(codiceCategoria);
        this.setDescrizioneCategoria(descrizioneCategoria);

        this.setIdDeposito(idDeposito);
        this.setCodiceDeposito(codiceDeposito);
        this.setDescrizioneDeposito(descrizioneDeposito);
    }

    /**
     * Creo una giacenza articolo passando la riga di un documento.<br/>
     * Utilizzata quando devo creare una riga di giacenza dall'inventario. Unico caso dove calcolo la giacenza da righe
     * magazzino <br.> e non dal datawarehouse. La qta è sempre di carico ed il magazzino della giacenza è sempre quello
     * di origine.
     * 
     * @param rigaArticolo
     *            rigaArticolo dell'inventario
     */
    public GiacenzaArticolo(final RigaArticolo rigaArticolo) {
        this.setCodiceArticolo(rigaArticolo.getArticolo().getCodice());
        this.setDescrizioneArticolo(rigaArticolo.getArticolo().getDescrizione());
        this.setIdArticolo(rigaArticolo.getArticolo().getId());

        this.setIdCategoria(rigaArticolo.getArticolo().getCategoria().getId());
        this.setCodiceCategoria(rigaArticolo.getArticolo().getCategoria().getCodice());
        this.setDescrizioneCategoria(rigaArticolo.getArticolo().getCategoria().getDescrizione());
    }

    /**
     * Incrementa la qta di inventario.
     * 
     * @param qta
     *            la qta da aggiungere
     */
    public void aggiungiQtaInventario(Double qta) {
        if (qta != null) {
            qtaInventario += qta;
        }
    }

    /**
     * Incrementa la qta di carico.
     * 
     * @param qta
     *            la qta da aggiungere
     */
    public void aggiungiQtaMagazzinoCarico(Double qta) {
        if (qta != null) {
            qtaMagazzinoCarico += qta;
        }
    }

    /**
     * Incrementa la qta di carico altro.
     * 
     * @param qta
     *            la qta da aggiungere
     */
    public void aggiungiQtaMagazzinoCaricoAltro(Double qta) {
        if (qta != null) {
            qtaMagazzinoCaricoAltro += qta;
        }
    }

    /**
     * Incrementa la qta di carico totale (carico + carico altro).
     * 
     * @param qta
     *            la qta da aggiungere
     */
    public void aggiungiQtaMagazzinoCaricoTotale(Double qta) {
        if (qta != null) {
            qtaMagazzinoCaricoTotale += qta;
        }
    }

    /**
     * Incrementa la qta di scarico.
     * 
     * @param qta
     *            la qta da aggiungere
     */
    public void aggiungiQtaMagazzinoScarico(Double qta) {
        if (qta != null) {
            qtaMagazzinoScarico += qta;
        }
    }

    /**
     * Incrementa la qta di scarico altro.
     * 
     * @param qta
     *            la qta da aggiungere
     */
    public void aggiungiQtaMagazzinoScaricoAltro(Double qta) {
        if (qta != null) {
            qtaMagazzinoScaricoAltro += qta;
        }
    }

    /**
     * Incrementa la qta di scarico totale (scarico + scarico altro).
     * 
     * @param qta
     *            la qta da aggiungere
     */
    public void aggiungiQtaMagazzinoScaricoTotale(Double qta) {
        if (qta != null) {
            qtaMagazzinoScaricoTotale += qta;
        }
    }

    /**
     * @return the articoloLite
     */
    public ArticoloLite getArticoloLite() {
        return articoloLite;
    }

    /**
     * @return the categoriaLite
     */
    public CategoriaLite getCategoriaLite() {
        return categoriaLite;
    }

    /**
     * @return the depositoLite
     */
    public DepositoLite getDepositoLite() {
        return depositoLite;
    }

    /**
     * La differenza tra qta di carico e qta di scarico.
     * 
     * @return the giacenza
     */
    public Double getGiacenza() {
        return getQtaMagazzinoCaricoTotale() - getQtaMagazzinoScaricoTotale();
    }

    /**
     * @return the qtaInventario
     */
    public Double getQtaInventario() {
        return qtaInventario;
    }

    /**
     * @return the qtaMagazzinoCarico
     */
    public Double getQtaMagazzinoCarico() {
        return qtaMagazzinoCarico;
    }

    /**
     * @return the qtaMagazzinoCaricoAltro
     */
    public Double getQtaMagazzinoCaricoAltro() {
        return qtaMagazzinoCaricoAltro;
    }

    /**
     * @return the qtaMagazzinoCaricoTotale
     */
    public Double getQtaMagazzinoCaricoTotale() {
        return qtaMagazzinoCaricoTotale;
    }

    /**
     * @return the qtaMagazzinoScarico
     */
    public Double getQtaMagazzinoScarico() {
        return qtaMagazzinoScarico;
    }

    /**
     * @return the qtaMagazzinoSCaricoAltro
     */
    public Double getQtaMagazzinoScaricoAltro() {
        return qtaMagazzinoScaricoAltro;
    }

    /**
     * @return the qtaMagazzinoScarico
     */
    public Double getQtaMagazzinoScaricoTotale() {
        return qtaMagazzinoScaricoTotale;
    }

    /**
     * @return the qtaMovimentata
     */
    public Double getQtaMovimentata() {
        return qtaMovimentata;
    }

    /**
     * init degli attributi che non devono essere null.
     */
    private void initialize() {
        this.articoloLite = new ArticoloLite();
        this.articoloLite.setUnitaMisura(new UnitaMisura());
        this.categoriaLite = new CategoriaLite();
        this.depositoLite = new DepositoLite();
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articoloLite.setCodice(codiceArticolo);
    }

    /**
     * @param codiceCategoria
     *            the codiceCategoria to set
     */
    public void setCodiceCategoria(String codiceCategoria) {
        this.categoriaLite.setCodice(codiceCategoria);
    }

    /**
     * @param codiceDeposito
     *            the codiceDeposito to set
     */
    public void setCodiceDeposito(String codiceDeposito) {
        this.depositoLite.setCodice(codiceDeposito);
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articoloLite.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param descrizioneCategoria
     *            the descrizioneCategoria to set
     */
    public void setDescrizioneCategoria(String descrizioneCategoria) {
        this.categoriaLite.setDescrizione(descrizioneCategoria);
    }

    /**
     * @param descrizioneDeposito
     *            the descrizioneDeposito to set
     */
    public void setDescrizioneDeposito(String descrizioneDeposito) {
        this.depositoLite.setDescrizione(descrizioneDeposito);
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articoloLite.setId(idArticolo);
    }

    /**
     * @param idCategoria
     *            the idCategoria to set
     */
    public void setIdCategoria(Integer idCategoria) {
        this.categoriaLite.setId(idCategoria);
    }

    /**
     * @param idDeposito
     *            the idDeposito to set
     */
    public void setIdDeposito(Integer idDeposito) {
        this.depositoLite.setId(idDeposito);
    }

    /**
     * @param qtaInventario
     *            the qtaInventario to set
     */
    public void setQtaInventario(Double qtaInventario) {
        this.qtaInventario = qtaInventario;
    }

    /**
     * @param qtaMagazzinoCarico
     *            the qtaMagazzinoCarico to set
     */
    public void setQtaMagazzinoCarico(Double qtaMagazzinoCarico) {
        this.qtaMagazzinoCarico = qtaMagazzinoCarico;
    }

    /**
     * @param qtaMagazzinoCaricoAltro
     *            the qtaMagazzinoCaricoAltro to set
     */
    public void setQtaMagazzinoCaricoAltro(Double qtaMagazzinoCaricoAltro) {
        this.qtaMagazzinoCaricoAltro = qtaMagazzinoCaricoAltro;
    }

    /**
     * @param qtaMagazzinoCaricoTotale
     *            the qtaMagazzinoCaricoTotale to set
     */
    public void setQtaMagazzinoCaricoTotale(Double qtaMagazzinoCaricoTotale) {
        this.qtaMagazzinoCaricoTotale = qtaMagazzinoCaricoTotale;
    }

    /**
     * @param qtaMagazzinoScarico
     *            the qtaMagazzinoScarico to set
     */
    public void setQtaMagazzinoScarico(Double qtaMagazzinoScarico) {
        this.qtaMagazzinoScarico = qtaMagazzinoScarico;
    }

    /**
     * @param qtaMagazzinoScaricoAltro
     *            the qtaMagazzinoScaricoAltro to set
     */
    public void setQtaMagazzinoScaricoAltro(Double qtaMagazzinoScaricoAltro) {
        this.qtaMagazzinoScaricoAltro = qtaMagazzinoScaricoAltro;
    }

    /**
     * @param qtaMagazzinoScaricoTotale
     *            the qtaMagazzinoScaricoTotale to set
     */
    public void setQtaMagazzinoScaricoTotale(Double qtaMagazzinoScaricoTotale) {
        this.qtaMagazzinoScaricoTotale = qtaMagazzinoScaricoTotale;
    }

    /**
     * @param qtaMovimentata
     *            the qtaMovimentata to set
     */
    public void setQtaMovimentata(Double qtaMovimentata) {
        this.qtaMovimentata = qtaMovimentata;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GiacenzaArticolo [codiceArticolo=");
        builder.append(articoloLite.getCodice());
        builder.append(", descrizioneArticolo=");
        builder.append(articoloLite.getDescrizione());
        builder.append(", idArticolo=");
        builder.append(articoloLite.getId());
        builder.append(", qtaMagazzinoCaricoTotale=");
        builder.append(qtaMagazzinoCaricoTotale);
        builder.append(", qtaMagazzinoScaricoTotale=");
        builder.append(qtaMagazzinoScaricoTotale);
        builder.append("]");
        return builder.toString();
    }

}
