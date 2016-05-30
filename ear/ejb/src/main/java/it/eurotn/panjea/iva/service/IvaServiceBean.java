package it.eurotn.panjea.iva.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.iva.service.exception.IvaException;
import it.eurotn.panjea.iva.service.interfaces.IvaService;
import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;

/**
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
@Stateless(name = "Panjea.IvaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.IvaService")
public class IvaServiceBean implements IvaService {

    /**
     * @uml.property name="areaIvaManager"
     * @uml.associationEnd
     */
    @EJB
    private AreaIvaManager areaIvaManager;

    /**
     * @uml.property name="areaIvaCancellaManager"
     * @uml.associationEnd
     */
    @EJB
    private AreaIvaCancellaManager areaIvaCancellaManager;

    @Override
    @RolesAllowed("gestioneRigheIva")
    public void cancellaRigaIva(RigaIva rigaIva) {
        areaIvaCancellaManager.cancellaRigaIva(rigaIva);
    }

    @Override
    public AreaIva caricaAreaIva(AreaContabile areaContabile) {
        return areaIvaManager.caricaAreaIva(areaContabile);
    }

    @Override
    public AreaIva caricaAreaIva(AreaIva areaIva) {
        return areaIvaManager.caricaAreaIva(areaIva);
    }

    @Override
    public RigaIva caricaRigaIva(Integer id) throws IvaException {
        return areaIvaManager.caricaRigaIva(id);
    }

    @Override
    public AreaIva invalidaAreaIva(AreaIva areaIva) {
        return areaIvaManager.invalidaAreaIva(areaIva);
    }

    @Override
    public List<RigaIvaRicercaDTO> ricercaRigheIva(
            ParametriRicercaRigheIva parametriRicercaRigheIva) {
        return areaIvaManager.ricercaRigheIva(parametriRicercaRigheIva);
    }

    @Override
    public AreaIva salvaAreaIva(AreaIva areaIva) {
        return areaIvaManager.salvaAreaIva(areaIva);
    }

    /*
     * (non-Javadoc)
     *
     * @see it.eurotn.panjea.iva.service.interfaces.IvaService#salvaRigaIva(it.eurotn
     * .panjea.iva.domain.RigaIva, it.eurotn.panjea.contabilita.domain.TipoAreaContabile)
     */
    @Override
    @RolesAllowed("gestioneRigheIva")
    public RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException {
        return areaIvaManager.salvaRigaIva(rigaIva, tipoAreaContabile);
    }

}
