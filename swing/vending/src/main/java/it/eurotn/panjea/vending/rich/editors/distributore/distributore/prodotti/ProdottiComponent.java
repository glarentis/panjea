package it.eurotn.panjea.vending.rich.editors.distributore.distributore.prodotti;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.vending.domain.ProdottoDistributore;
import it.eurotn.panjea.vending.rich.editors.prodotticollegati.ProdottiCollegatiComponent;

public class ProdottiComponent extends ProdottiCollegatiComponent {

    private static final long serialVersionUID = -3384865302920955573L;

    /**
     * Costruttore.
     */
    public ProdottiComponent() {
        super("Elenco prodotti");
    }

    @Override
    public Class<? extends ProdottoCollegato> getProdottiRendererClass() {
        return ProdottoDistributore.class;
    }

    @Override
    public ProdottoCollegato onSave(ProdottoCollegato prodottoCollegato) {
        return null;
    }

}
