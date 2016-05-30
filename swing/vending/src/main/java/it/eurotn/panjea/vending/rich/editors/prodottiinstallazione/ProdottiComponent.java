package it.eurotn.panjea.vending.rich.editors.prodottiinstallazione;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.ProdottoInstallazione;
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
        return ProdottoInstallazione.class;
    }

    @Override
    public ProdottoCollegato onSave(ProdottoCollegato prodottoCollegato) {
        return null;
    }

}
