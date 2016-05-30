package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDocumento;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.rich.editors.dms.allegati.AbstractAllegatiPage;

public class AreaOrdineAllegatiPage extends AbstractAllegatiPage {

    @Override
    protected AllegatoDMS createAttributoFromFormObject(Object object) {
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) object;
        return new AllegatoDocumento(areaOrdineFullDTO.getAreaOrdine().getDocumento(), getCodiceAzienda());
    }

    @Override
    protected String getFolder(Object object) {
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) object;
        Documento doc = areaOrdineFullDTO.getAreaOrdine().getDocumento();
        return dmsBD.caricaDmsSettings().getFolder(doc);
    }

}
