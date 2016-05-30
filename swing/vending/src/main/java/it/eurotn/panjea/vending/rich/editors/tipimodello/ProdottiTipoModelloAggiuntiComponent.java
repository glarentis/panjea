package it.eurotn.panjea.vending.rich.editors.tipimodello;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.vending.domain.ProdottoTipoModello;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.rich.editors.prodotticollegati.ProdottiCollegatiComponent;

public class ProdottiTipoModelloAggiuntiComponent extends ProdottiCollegatiComponent {

    private static final long serialVersionUID = 8338345940128599070L;

    private TipoModello tipoModello;

    /**
     * Costruttore.
     */
    public ProdottiTipoModelloAggiuntiComponent() {
        super(null, ProdottoTipoModello.class, true);
    }

    @Override
    public ProdottoCollegato onSave(ProdottoCollegato prodottoCollegato) {
        prodottoCollegato.setTipo(TipoProdottoCollegato.AGGIUNTO);
        ((ProdottoTipoModello) prodottoCollegato).setTipoModello(tipoModello);
        return prodottoCollegato;
    }

    /**
     * @param tipoModello
     *            the tipoModello to set
     */
    public final void setTipoModello(TipoModello tipoModello) {
        this.tipoModello = tipoModello;
    }

}
