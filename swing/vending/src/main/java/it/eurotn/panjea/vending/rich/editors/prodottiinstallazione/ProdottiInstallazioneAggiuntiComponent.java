package it.eurotn.panjea.vending.rich.editors.prodottiinstallazione;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.ProdottoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.vending.rich.editors.prodotticollegati.ProdottiCollegatiComponent;

public class ProdottiInstallazioneAggiuntiComponent extends ProdottiCollegatiComponent {

    private static final long serialVersionUID = 8338345940128599070L;

    private Installazione installazione;

    /**
     * Costruttore.
     */
    public ProdottiInstallazioneAggiuntiComponent() {
        super("Prodotti aggiunti", ProdottoInstallazione.class, true);
    }

    @Override
    public ProdottoCollegato onSave(ProdottoCollegato prodottoCollegato) {
        prodottoCollegato.setTipo(TipoProdottoCollegato.AGGIUNTO);
        ((ProdottoInstallazione) prodottoCollegato).setInstallazione(installazione);
        return prodottoCollegato;
    }

    /**
     * @param installazione
     *            the installazione to set
     */
    public final void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
        setReadOnly(installazione == null || installazione.isNew());
    }

}
