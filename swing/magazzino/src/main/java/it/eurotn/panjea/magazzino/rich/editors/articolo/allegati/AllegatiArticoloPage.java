package it.eurotn.panjea.magazzino.rich.editors.articolo.allegati;

import it.eurotn.panjea.dms.manager.allegati.AllegatoArticolo;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.rich.editors.dms.allegati.AbstractAllegatiPage;

public class AllegatiArticoloPage extends AbstractAllegatiPage {

    @Override
    protected AllegatoDMS createAttributoFromFormObject(Object object) {
        Articolo articolo = (Articolo) object;
        return new AllegatoArticolo(articolo.getCodice(), articolo.getDescrizione(), articolo.getId(),
                getCodiceAzienda());
    }

    @Override
    protected String getFolder(Object object) {
        Articolo articolo = (Articolo) object;
        return dmsBD.caricaDmsSettings().getFolder(articolo);
    }
}