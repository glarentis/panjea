package it.eurotn.panjea.vending.rich.editors.distributore.distributore.prodotti;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.ProdottoDistributore;
import it.eurotn.panjea.vending.rich.editors.prodotticollegati.ProdottiCollegatiComponent;

public class ProdottiDistributoreAggiuntiComponent extends ProdottiCollegatiComponent {

    private static final long serialVersionUID = 8338345940128599070L;

    private Distributore distributore;

    /**
     * Costruttore.
     */
    public ProdottiDistributoreAggiuntiComponent() {
        super("Prodotti aggiunti", ProdottoDistributore.class, true);
    }

    @Override
    public ProdottoCollegato onSave(ProdottoCollegato prodottoCollegato) {
        prodottoCollegato.setTipo(TipoProdottoCollegato.AGGIUNTO);
        ((ProdottoDistributore) prodottoCollegato).setDatiVendingDistributore(distributore.getDatiVending());
        return prodottoCollegato;
    }

    /**
     * @param distributore
     *            the distributore to set
     */
    public final void setDistributore(Distributore distributore) {
        this.distributore = distributore;
        setReadOnly(distributore == null || distributore.isNew());
    }

}
