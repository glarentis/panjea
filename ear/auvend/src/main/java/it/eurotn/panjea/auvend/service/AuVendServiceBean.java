package it.eurotn.panjea.auvend.service;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;
import it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager;
import it.eurotn.panjea.auvend.manager.interfaces.CarichiManager;
import it.eurotn.panjea.auvend.manager.interfaces.FatturazioneRifornimentiAuvendManager;
import it.eurotn.panjea.auvend.manager.interfaces.FattureManager;
import it.eurotn.panjea.auvend.manager.interfaces.MovimentiAuvendManager;
import it.eurotn.panjea.auvend.manager.interfaces.RiparazioniContoTerziAuvendManager;
import it.eurotn.panjea.auvend.service.interfaces.AuVendService;
import it.eurotn.panjea.magazzino.service.exception.SottoContiContabiliAssentiException;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * Service per il plugin di AuVend.
 * 
 * @author adriano
 * @version 1.0, 05/gen/2009
 * 
 */
@Stateless(name = "Panjea.AuVendService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AuVendService")
public class AuVendServiceBean implements AuVendService {

	@EJB
	private AnagraficaAuVendManager anagraficaAuVendManager;

	@EJB
	private CarichiManager carichiManager;

	@EJB
	private FattureManager fattureManager;

	@EJB
	private MovimentiAuvendManager movimentiAuvendManager;

	@EJB
	private FatturazioneRifornimentiAuvendManager fatturazioneRifornimentiManager;

	@EJB
	private RiparazioniContoTerziAuvendManager riparazioniContoTerziManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.service.interfaces.AuVendService#cancellaCodiceIvaAuVend(java.lang.Integer)
	 */
	@Override
	public void cancellaCodiceIvaAuVend(Integer id) {
		anagraficaAuVendManager.cancellaCodiceIvaAuVend(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeit.eurotn.panjea.auvend.service.AuVendService#cancellaLetturaFlussoAuVend(it.eurotn.panjea.auvend.domain.
	 * LetturaFlussoAuVend)
	 */
	@Override
	public void cancellaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) {
		anagraficaAuVendManager.cancellaLetturaFlussoAuVend(letturaFlussoAuVend);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.service.AuVendService#cancellaTipoDocumentoBaseAuVend(it.eurotn.panjea.auvend.domain.
	 * TipoDocumentoBaseAuVend)
	 */
	@Override
	public void cancellaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
		anagraficaAuVendManager.cancellaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.service.interfaces.AuVendService#caricaCaricatori()
	 */
	@Override
	public List<Deposito> caricaCaricatori() throws AuVendException {
		return anagraficaAuVendManager.caricaCaricatori();
	}

	@Override
	public List<String> caricaCausaliNonAssociateAuvend() {
		return anagraficaAuVendManager.caricaCausaliNonAssociateAuvend();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.service.interfaces.AuVendService#caricaCodiceIvaAuVend(java.lang.Integer)
	 */
	@Override
	public CodiceIvaAuVend caricaCodiceIvaAuVend(Integer id) throws AuVendException {
		return anagraficaAuVendManager.caricaCodiceIvaAuVend(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.service.interfaces.AuVendService#caricaCodiciIvaAuVend()
	 */
	@Override
	public List<CodiceIvaAuVend> caricaCodiciIvaAuVend() throws AuVendException {
		return anagraficaAuVendManager.caricaCodiciIvaAuVend();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeit.eurotn.panjea.auvend.service.AuVendService#caricaLetturaFlussoAuVend(it.eurotn.panjea.auvend.domain.
	 * LetturaFlussoAuVend)
	 */
	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend)
			throws AuVendException {
		return anagraficaAuVendManager.caricaLetturaFlussoAuVend(letturaFlussoAuVend);
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoFatturazioneRifornimenti() throws AuVendException {
		return anagraficaAuVendManager.caricaLetturaFlussoFattuazioneRifornimenti();
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoMovimenti() throws AuVendException {
		return anagraficaAuVendManager.caricaLetturaFlussoMovimenti();
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoRiparazioniContoTerzi() throws AuVendException {
		return anagraficaAuVendManager.caricaLetturaFlussoFattuazioneRifornimenti();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.service.AuVendService#caricaLettureFlussoAuVend()
	 */
	@Override
	public List<LetturaFlussoAuVend> caricaLettureFlussoAuVend() throws AuVendException {
		return anagraficaAuVendManager.caricaLettureFlussoAuVend();
	}

	@Override
	public Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoCarichi() throws AuVendException {
		return anagraficaAuVendManager.caricaLettureFlussoCarichi();
	}

	@Override
	public Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoFatture() throws AuVendException {
		return anagraficaAuVendManager.caricaLettureFlussoFatture();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.auvend.service.AuVendService#caricaTipiDocumentoBaseAuVend()
	 */
	@Override
	public List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVend() throws AuVendException {
		return anagraficaAuVendManager.caricaTipiDocumentoBaseAuVend();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.service.interfaces.AuVendService#caricaTipiDocumentoBaseAuVendPerTipoOperazione(it.eurotn
	 * .panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione)
	 */
	@Override
	public List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione)
			throws AuVendException {
		return anagraficaAuVendManager.caricaTipiDocumentoBaseAuVendPerTipoOperazione(tipoOperazione);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeit.eurotn.panjea.auvend.service.AuVendService#caricaTipoDocumentoBaseAuVend(it.eurotn.panjea.auvend.domain.
	 * TipoDocumentoBaseAuVend)
	 */
	@Override
	public TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend)
			throws AuVendException {
		return anagraficaAuVendManager.caricaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.service.AuVendService#caricaTipoDocumentoBaseAuVendPerTipoOperazione(it.eurotn.panjea
	 * .auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione)
	 */
	@Override
	public TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione) {
		return anagraficaAuVendManager.caricaTipoDocumentoBaseAuVendPerTipoOperazione(tipoOperazione);
	}

	@Override
	@TransactionTimeout(value = 7200)
	public List<Integer> chiudiFatture(String deposito, Date dataFine) {
		return fattureManager.chiudiFatture(deposito, dataFine);
	}

	@Override
	public boolean importa(TipoDocumentoBaseAuVend.TipoOperazione tipoOperazione, Date dataInizio, Date dataFine) {
		switch (tipoOperazione) {
		case RECUPERO_GENERICO:
			return importaMovimenti(dataInizio, dataFine);
		case RECUPERO_RIFORNIMENTI_DA_FATTURARE:
			return importaFatturazioneRifornimenti(dataInizio, dataFine);
		default:
			throw new RuntimeException("Tipo operazione non supportato.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.service.AuVendService#recuperaCarichi(it.eurotn.panjea.auvend.utils.ParametriRecuperoCarichi
	 * )
	 */
	@Override
	@TransactionTimeout(value = 7200)
	public boolean importaCarichiERifornimenti(Date dataInizio, Date dataFine) {
		return carichiManager.importaCarichiERifornimenti(dataInizio, dataFine);
	}

	@Override
	public boolean importaFatturazioneRifornimenti(Date dataInizio, Date dataFine) {
		return fatturazioneRifornimentiManager.importaMovimenti(dataInizio, dataFine);
	}

	@Override
	@TransactionTimeout(value = 7200)
	public boolean importaFatture(String deposito, Date dataFine) throws SottoContiContabiliAssentiException {
		return fattureManager.importaFatture(deposito, dataFine);
	}

	@Override
	public boolean importaMovimenti(Date dataInizio, Date dataFine) {
		return movimentiAuvendManager.importaMovimenti(dataInizio, dataFine);
	}

	@Override
	public boolean importaRiparazioniContoTerzi(Date dataInizio, Date dataFine) {
		return riparazioniContoTerziManager.importaMovimenti(dataInizio, dataFine);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.auvend.service.interfaces.AuVendService#salvaCodiceIvaAuVend(it.eurotn.panjea.auvend.domain.
	 * CodiceIvaAuVend)
	 */
	@Override
	public CodiceIvaAuVend salvaCodiceIvaAuVend(CodiceIvaAuVend codiceIvaAuVend) {
		return anagraficaAuVendManager.salvaCodiceIvaAuVend(codiceIvaAuVend);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeit.eurotn.panjea.auvend.service.AuVendService#salvaLetturaFlussoAuVend(it.eurotn.panjea.auvend.domain.
	 * LetturaFlussoAuVend)
	 */
	@Override
	public LetturaFlussoAuVend salvaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) {
		return anagraficaAuVendManager.salvaLetturaFlussoAuVend(letturaFlussoAuVend);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeit.eurotn.panjea.auvend.service.AuVendService#salvaTipoDocumentoBaseAuVend(it.eurotn.panjea.auvend.domain.
	 * TipoDocumentoBaseAuVend)
	 */
	@Override
	public TipoDocumentoBaseAuVend salvaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
		return anagraficaAuVendManager.salvaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
	}

	@Override
	public StatisticaImportazione verifica(TipoDocumentoBaseAuVend.TipoOperazione tipoOperazione, Date dataInizio,
			Date dataFine) {

		switch (tipoOperazione) {
		case RECUPERO_GENERICO:
			return verificaMovimenti(dataInizio, dataFine).get(null);
		case RECUPERO_RIFORNIMENTI_DA_FATTURARE:
			return verificaFatturazioneRifornimenti(dataInizio, dataFine);
		case RECUPERO_CARICO:
			return verificaCarichi(dataInizio, dataFine);
		default:
			throw new RuntimeException("Tipo operazione non supportato.");
		}
	}

	@Override
	public StatisticaImportazione verificaCarichi(Date dataInizio, Date dataFine) {
		return carichiManager.verificaCarichi(dataInizio, dataFine);
	}

	@Override
	public StatisticaImportazione verificaFatturazioneRifornimenti(Date dataInizio, Date dataFine) {
		return fatturazioneRifornimentiManager.verifica(dataInizio, dataFine);
	}

	@Override
	public Map<String, StatisticaImportazione> verificaFatture(List<String> depositi, Date dataFine) {
		return fattureManager.verificaFatture(depositi, dataFine);
	}

	@Override
	public Map<String, StatisticaImportazione> verificaMovimenti(Date dataInizio, Date dataFine) {
		return movimentiAuvendManager.verifica(dataInizio, dataFine);
	}

	@Override
	public StatisticaImportazione verificaRiparazioniContoTerzi(Date dataInizio, Date dataFine) {
		return riparazioniContoTerziManager.verifica(dataInizio, dataFine);
	}
}
