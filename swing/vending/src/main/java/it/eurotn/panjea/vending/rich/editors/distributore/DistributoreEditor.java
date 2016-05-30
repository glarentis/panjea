package it.eurotn.panjea.vending.rich.editors.distributore;

import it.eurotn.panjea.rich.editors.DockedEditor;
import it.eurotn.panjea.vending.domain.Distributore;

public class DistributoreEditor extends DockedEditor {

    @Override
    public void initialize(Object editorObject) {
        if (editorObject instanceof Distributore) {
            Distributore distributore = (Distributore) editorObject;
            DistributorePM distributorePM = new DistributorePM(
                    distributore.getDatiVending().getModello().getTipoModello(),
                    distributore.getDatiVending().getModello(), distributore, null);
            super.initialize(distributorePM);
        }
        super.initialize(editorObject);
    }

}
