package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDocumento;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.rich.editors.dms.allegati.AbstractAllegatiPage;

public class AreaPreventivoAllegatiPage extends AbstractAllegatiPage {

    @Override
    protected AllegatoDMS createAttributoFromFormObject(Object object) {
        AreaPreventivoFullDTO areaPreventivoFullDTO = (AreaPreventivoFullDTO) object;
        return new AllegatoDocumento(areaPreventivoFullDTO.getAreaPreventivo().getDocumento(), getCodiceAzienda());
    }

    @Override
    protected String getFolder(Object object) {
        AreaPreventivoFullDTO areaPreventivoFullDTO = (AreaPreventivoFullDTO) object;
        Documento doc = areaPreventivoFullDTO.getAreaPreventivo().getDocumento();
        return dmsBD.caricaDmsSettings().getFolder(doc);
    }

}
