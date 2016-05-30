package it.eurotn.panjea.rate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaService;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.rate.service.interfaces.RateService;
import it.eurotn.panjea.rate.util.RataRV;
import it.eurotn.panjea.tesoreria.solleciti.manager.interfaces.SollecitiManager;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

@Stateless(name = "Panjea.RateService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.RateService")
public class RateServiceBean implements RateService {

    @EJB
    protected AreaRateManager areaRateManager;

    @EJB
    protected RateManager rateManager;

    @EJB
    protected ContabilitaService contabilitaService;

    @EJB
    protected MagazzinoDocumentoService magazzinoDocumentoService;
    @EJB
    protected SollecitiManager sollecitiManager;
    @EJB
    protected RateGenerator rateGenerator;

    @Override
    public Rata associaRapportoBancario(Rata rata, IAreaDocumento areaDocumento, TipoPagamento tipoPagamento,
            boolean salvaRata) {
        return rateManager.associaRapportoBancario(rata, areaDocumento, tipoPagamento, salvaRata);
    }

    @Override
    public void cancellaAreaRate(Documento documento) {
        areaRateManager.cancellaAreaRate(documento);

    }

    @Override
    @RolesAllowed("gestioneRigheRate")
    public void cancellaRata(Rata rata) throws DocumentiCollegatiPresentiException, PagamentiException {
        rateManager.cancellaRata(rata);
        sollecitiManager.cancellaSollecitiOrphan();

    }

    @Override
    public void cancellaRataNoCheck(Rata rata) {
        rateManager.cancellaRataNoCheck(rata);

    }

    @Override
    public AreaContabileFullDTO caricaAreaContabileFullDTO(Integer idAreaContabile) {
        return contabilitaService.caricaAreaContabileFullDTO(idAreaContabile);
    }

    @Override
    public AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(Integer idAreaMagazzino) {
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(idAreaMagazzino);
        return magazzinoDocumentoService.caricaAreaMagazzinoFullDTO(areaMagazzino);
    }

    @Override
    public AreaRate caricaAreaRate(Documento documento) {
        return areaRateManager.caricaAreaRate(documento);
    }

    @Override
    public AreaRate caricaAreaRate(Integer idAreaRate) {
        return areaRateManager.caricaAreaRate(idAreaRate);
    }

    @Override
    public List<Rata> caricaRateCollegate(Rata rata) {
        return rateManager.caricaRateCollegate(rata);
    }

    @Override
    public List<RataRV> caricaRatePerRichiestaVersamento(Map<Object, Object> parametri) {
        return rateManager.caricaRatePerRichiestaVersamento(parametri);
    }

    @Override
    public List<RataRV> caricaRateRaggruppatePerRichiestaVersamento(Map<Object, Object> parametri) {
        return rateManager.caricaRateRaggruppatePerRichiestaVersamento(parametri);
    }

    @Override
    public List<Rata> generaRate(CodicePagamento codicePagamento, java.util.Date dataDocumento, BigDecimal imponibile,
            BigDecimal iva, CalendarioRate calendarioRate) {
        return rateGenerator.generaRate(codicePagamento, dataDocumento, imponibile, iva, calendarioRate);
    }

    @Override
    public AreaRate generaRate(IAreaDocumento areaDocumento) {
        return rateGenerator.generaRate(areaDocumento);
    }

    @Override
    public AreaRate generaRate(IAreaDocumento areaDocumento, AreaRate areaRate) {
        return rateGenerator.generaRate(areaDocumento, areaRate);
    }

    @Override
    @RolesAllowed("visualizaRate")
    public List<SituazioneRata> ricercaRate(ParametriRicercaRate parametriRicercaRate) {
        return rateManager.ricercaRate(parametriRicercaRate);
    }

    @Override
    public void riemettiRate(RataRiemessa rataRiemessa) {
        rateManager.riemettiRate(rataRiemessa);
    }

    @Override
    public AreaRate salvaAreaRate(AreaRate areaRate) {
        return areaRateManager.salvaAreaRate(areaRate);
    }

    @Override
    @RolesAllowed("gestioneRigheRate")
    public Rata salvaRata(Rata rata) {
        return rateManager.salvaRata(rata);
    }

    @Override
    public Rata salvaRataNoCheck(Rata rata) {
        return rateManager.salvaRataNoCheck(rata);
    }

    @Override
    public AreaRate validaAreaRate(AreaRate areaRate, IAreaDocumento areaDocumento) {
        if (areaDocumento instanceof AreaContabile) {
            return areaRateManager.validaAreaRate(areaRate, (AreaContabile) areaDocumento);
        }
        if (areaDocumento instanceof AreaMagazzino) {
            return areaRateManager.validaAreaRate(areaRate, (AreaMagazzino) areaDocumento);
        }
        throw new UnsupportedOperationException(
                "L'area rate può essere validata solamente da un'area magazzino o contabile");
    }

}
