package it.eurotn.panjea.contabilita.rich.editors.areacontabile;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDocumento;
import it.eurotn.panjea.rich.editors.dms.allegati.AbstractAllegatiPage;

public class AreaContabileAllegatiPage extends AbstractAllegatiPage {

    @Override
    protected AllegatoDMS createAttributoFromFormObject(Object object) {
        AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) object;
        return new AllegatoDocumento(areaContabileFullDTO.getAreaContabile().getDocumento(), getCodiceAzienda());
    }

    @Override
    protected String getFolder(Object object) {
        AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) object;
        Documento doc = areaContabileFullDTO.getAreaContabile().getDocumento();
        return dmsBD.caricaDmsSettings().getFolder(doc);
    }

}
