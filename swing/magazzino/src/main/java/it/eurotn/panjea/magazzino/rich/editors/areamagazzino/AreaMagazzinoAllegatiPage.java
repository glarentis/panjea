package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDocumento;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.rich.editors.dms.allegati.AbstractAllegatiPage;

public class AreaMagazzinoAllegatiPage extends AbstractAllegatiPage {

    @Override
    protected AllegatoDMS createAttributoFromFormObject(Object object) {
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
        return new AllegatoDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento(), getCodiceAzienda());
    }

    @Override
    protected String getFolder(Object object) {
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
        Documento doc = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento();
        return dmsBD.caricaDmsSettings().getFolder(doc);
    }

}
