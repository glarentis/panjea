package it.eurotn.panjea.pagamenti.service;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoEsistenteException;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.pagamenti.service.interfaces.AnagraficaPagamentiService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author adriano
 * @version 1.0, 18/lug/08
 */
@Stateless(name = "Panjea.AnagraficaPagamentiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AnagraficaPagamentiService")
public class AnagraficaPagamentiServiceBean implements AnagraficaPagamentiService {

	/**
	 * @uml.property name="codicePagamentoManager"
	 * @uml.associationEnd
	 */
	@EJB
	private CodicePagamentoManager codicePagamentoManager;

	/**
	 * @uml.property name="sediPagamentoManager"
	 * @uml.associationEnd
	 */
	@EJB
	private SediPagamentoManager sediPagamentoManager;

	@Override
	public void cancellaCodicePagamento(CodicePagamento codicePagamento) {
		codicePagamentoManager.cancellaCodicePagamento(codicePagamento);
	}

	@Override
	public void cancellaSedePagamento(Integer sedePagamentoId) {
		SedePagamento sedePagamento = new SedePagamento();
		sedePagamento.setId(sedePagamentoId);
		sediPagamentoManager.cancellaSedePagamento(sedePagamento);
	}

	@Override
	public CodicePagamento caricaCodicePagamento(Integer id) {
		return codicePagamentoManager.caricaCodicePagamento(id);
	}

	@Override
	public CodicePagamento caricaCodicePagamento(String codice) {
		return codicePagamentoManager.caricaCodicePagamento(codice);
	}

	@Override
	public List<CodicePagamento> caricaCodiciPagamento(String filtro, TipoRicercaCodicePagamento tipoRicerca,
			boolean includiDisabilitati) {
		return codicePagamentoManager.caricaCodiciPagamento(filtro, tipoRicerca, includiDisabilitati);
	}

	@Override
	public SedePagamento caricaSedePagamento(Integer sedePagamentoId) throws PagamentiException {
		SedePagamento sedePagamento = new SedePagamento();
		sedePagamento.setId(sedePagamentoId);
		return sediPagamentoManager.caricaSedePagamento(sedePagamento);
	}

	@Override
	public SedePagamento caricaSedePagamentoBySedeEntita(Integer sedeEntitaId) {
		SedeEntita sedeEntita = new SedeEntita();
		sedeEntita.setId(sedeEntitaId);
		return sediPagamentoManager.caricaSedePagamentoBySedeEntita(sedeEntita, false);
	}

	@Override
	public SedePagamento caricaSedePagamentoPrincipaleEntita(Integer entitaId) {
		// istanziato di default ClienteLite
		EntitaLite entitaLite = new ClienteLite();
		entitaLite.setId(entitaId);
		return sediPagamentoManager.caricaSedePagamentoPrincipaleEntita(entitaLite);
	}

	@Override
	public CodicePagamento salvaCodicePagamento(CodicePagamento codicePagamento)
			throws CodicePagamentoEsistenteException {
		return codicePagamentoManager.salvaCodicePagamento(codicePagamento);
	}

	@Override
	public SedePagamento salvaSedePagamento(SedePagamento sedePagamento) {
		return sediPagamentoManager.salvaSedePagamento(sedePagamento);
	}

}
