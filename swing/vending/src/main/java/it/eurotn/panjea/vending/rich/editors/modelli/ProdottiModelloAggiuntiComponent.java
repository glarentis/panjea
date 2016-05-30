package it.eurotn.panjea.vending.rich.editors.modelli;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.ProdottoModello;
import it.eurotn.panjea.vending.rich.editors.prodotticollegati.ProdottiCollegatiComponent;

public class ProdottiModelloAggiuntiComponent extends ProdottiCollegatiComponent {

    private static final long serialVersionUID = 8338345940128599070L;

    private Modello modello;

    /**
     * Costruttore.
     */
    public ProdottiModelloAggiuntiComponent() {
        super("Prodotti aggiunti", ProdottoModello.class, true);
    }

    @Override
    public ProdottoCollegato onSave(ProdottoCollegato prodottoCollegato) {
        prodottoCollegato.setTipo(TipoProdottoCollegato.AGGIUNTO);
        ((ProdottoModello) prodottoCollegato).setModello(modello);
        return prodottoCollegato;
    }

    /**
     * @param modello
     *            the modello to set
     */
    public final void setModello(Modello modello) {
        this.modello = modello;
        setReadOnly(modello == null || modello.isNew());
    }

}
