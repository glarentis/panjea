package it.eurotn.panjea.contabilita.manager.interfaces.querybuilder;

import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.Query;

@Local
public interface SaldoContiQueryBuilderInterface {

	public Query getQuery(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio, Date dataFine,
			Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici) throws ContabilitaException, TipoDocumentoBaseException;

}