package it.eurotn.panjea.contabilita.service;

import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.AreaChiusuraRiscontiManager;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.RateoRiscontoManager;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.interfaces.RateiRiscontiService;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaElencoRiscontiDTO;

@Stateless(name = "Panjea.RateiRiscontiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.RateiRiscontiService")
public class RateiRiscontiServiceBean implements RateiRiscontiService {

    @EJB
    private RateoRiscontoManager rateoRiscontoManager;

    @EJB
    private AreaChiusuraRiscontiManager areaChiusuraRiscontiManager;

    @Override
    public List<RigaElencoRiscontiDTO> caricaElencoRisconti(int anno, Class<? extends RigaRiscontoAnno> clazz) {
        return rateoRiscontoManager.caricaElencoRisconti(anno, clazz);
    }

    @Override
    public RigaRateoRisconto caricaRigaRateoRisconto(Integer idRigaRateoRisconto) {
        return rateoRiscontoManager.caricaRigaRateoRisconto(idRigaRateoRisconto);
    }

    @Override
    public ParametriRicercaMovimentiContabili creaMovimentiChiusureRisconti(int anno, Date dataMovimenti)
            throws ContiBaseException, TipoDocumentoBaseException {
        return areaChiusuraRiscontiManager.creaMovimentiChiusureRisconti(anno, dataMovimenti);
    }

}
