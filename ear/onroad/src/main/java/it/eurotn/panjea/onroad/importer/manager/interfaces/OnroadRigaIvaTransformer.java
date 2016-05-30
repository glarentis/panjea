package it.eurotn.panjea.onroad.importer.manager.interfaces;

import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.onroad.domain.RigaIvaOnRoad;

import javax.ejb.Local;

@Local
public interface OnroadRigaIvaTransformer {

	RigaIva trasforma(RigaIvaOnRoad rigaIva, AreaIva areaIva);
}
