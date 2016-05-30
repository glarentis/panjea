package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaChiusureManager;
import it.eurotn.panjea.tesoreria.service.exception.EntitaRateNonCoerentiException;
import it.eurotn.panjea.tesoreria.util.ParametriCreazionePagamento;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.PagamentiManager;

@Stateless(name = "Panjea.PagamentiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PagamentiManager")
public class PagamentiManagerBean implements PagamentiManager {
    private static final Logger LOGGER = Logger.getLogger(PagamentiManagerBean.class);
    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private AreaChiusureManager areaChiusureManager;

    @Override
    public void creaPagamenti(AreaMagazzino areaMagazzino, BigDecimal incassato) {
        AreaRate areaRate = areaRateManager.caricaAreaRate(areaMagazzino.getDocumento());
        if (areaRate != null && areaRate.getCodicePagamento() != null
                && areaRate.getCodicePagamento().getTipoAreaPartitaPredefinitaPerPagamenti() != null) {
            if (!CollectionUtils.isEmpty(areaRate.getRate()) && incassato != null) {
                ParametriCreazionePagamento params = new ParametriCreazionePagamento(
                        areaRate.getRate().iterator().next());
                params.setTipoAreaPartita(areaRate.getCodicePagamento().getTipoAreaPartitaPredefinitaPerPagamenti());
                Pagamento pagamento = params.getPagamento();
                pagamento.setDataCreazione(Calendar.getInstance().getTime());
                pagamento.getImporto().setImportoInValuta(incassato);
                pagamento.getImporto().calcolaImportoValutaAzienda(2);
                List<Pagamento> pagamenti = new ArrayList<Pagamento>();
                pagamenti.add(pagamento);
                try {
                    areaChiusureManager.creaAreaChiusure(params, pagamenti);
                } catch (DocumentoDuplicateException | CambioNonPresenteException | EntitaRateNonCoerentiException e) {
                    LOGGER.error("-->errore nel generare i pagamenti per la rata del rifornimento", e);
                    throw new GenericException("-->errore nel generare i pagamenti per la rata del rifornimento", e);
                }
            }
        }
    }

    @Override
    public void creaPagamenti(AreaRifornimento areaRifornimento) {
        if (areaRifornimento.getIncasso() != null) {
            creaPagamenti(areaRifornimento.getAreaMagazzino(), areaRifornimento.getIncasso());
        }
    }

}
