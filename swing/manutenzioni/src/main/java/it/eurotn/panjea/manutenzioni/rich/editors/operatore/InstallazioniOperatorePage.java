package it.eurotn.panjea.manutenzioni.rich.editors.operatore;

import java.util.ArrayList;
import java.util.Collection;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class InstallazioniOperatorePage extends AbstractTablePageEditor<Installazione>implements IPageLifecycleAdvisor {

    private Operatore operatore;

    private IManutenzioniBD manutenzioniBD;

    protected InstallazioniOperatorePage() {
        super("installazioniOperatorePage", new String[] { "codice", "descrizione", "articolo", "deposito.sedeEntita",
                "ubicazione", "listino", "datiInstallazione.caricatore", "datiInstallazione.tecnico" },
                Installazione.class);
    }

    @Override
    public Collection<Installazione> loadTableData() {
        Collection<Installazione> installazioni = new ArrayList<>();

        if (operatore != null && !operatore.isNew()) {
            ParametriRicercaInstallazioni parametri = new ParametriRicercaInstallazioni();
            parametri.setIdCaricatore(operatore.getId());
            parametri.setIdTecnico(operatore.getId());
            installazioni = manutenzioniBD.ricercaInstallazioniByParametri(parametri);
        }

        return installazioni;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public Collection<Installazione> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        operatore = (Operatore) object;
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
