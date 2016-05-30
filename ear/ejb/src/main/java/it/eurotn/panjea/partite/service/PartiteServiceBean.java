package it.eurotn.panjea.partite.service;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.manager.interfaces.VariabiliFormulaManager;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione;
import it.eurotn.panjea.partite.manager.interfaces.StrutturaPartitaManager;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.partite.service.interfaces.PartiteService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Implementazione del SessionBean per il Service del modulo Pagamenti.
 * 
 * @author adriano
 * @version 1.0, 18/lug/08
 */
@Stateless(name = "Panjea.PartiteService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.PartiteService")
public class PartiteServiceBean implements PartiteService {

	/**
	 * @uml.property name="strutturaPartitaManager"
	 * @uml.associationEnd
	 */
	@EJB
	@IgnoreDependency
	private StrutturaPartitaManager strutturaPartitaManager;

	@EJB
	private VariabiliFormulaManager variabiliFormulaManager;
	/**
	 * @uml.property name="tipiAreaPartitaManager"
	 * @uml.associationEnd
	 */
	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@Override
	public void cancellaCategoriaRata(CategoriaRata categoriaRata) {
		strutturaPartitaManager.cancellaCategoriaRata(categoriaRata);

	}

	@Override
	public void cancellaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite) {
		strutturaPartitaManager.cancellaRigaStrutturaPartite(rigaStrutturaPartite);
	}

	@Override
	public void cancellaStrutturaPartita(StrutturaPartita strutturaPartite) {
		strutturaPartitaManager.cancellaStrutturaPartita(strutturaPartite);
	}

	@Override
	public void cancellaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		tipiAreaPartitaManager.cancellaTipoAreaPartita(tipoAreaPartita);
	}

	@Override
	public void cancellaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase) {
		tipiAreaPartitaManager.cancellaTipoDocumentoBase(tipoDocumentoBase);
	}

	@Override
	public CategoriaRata caricaCategoriaRata(Integer idCategoriaRata) {
		return strutturaPartitaManager.caricaCategoriaRata(idCategoriaRata);
	}

	@Override
	public List<CategoriaRata> caricaCategorieRata(String fieldSearch, String valueSearch) {
		return strutturaPartitaManager.caricaCategorieRata(fieldSearch, valueSearch);
	}

	@Override
	public StrutturaPartita caricaStrutturaPartita(Integer idStruttura) {
		return strutturaPartitaManager.caricaStrutturaPartita(idStruttura);
	}

	@Override
	public List<StrutturaPartitaLite> caricaStrutturePartita() {
		return strutturaPartitaManager.caricaStrutturePartita();
	}

	@Override
	public List<TipoAreaPartita> caricaTipiAreaPartitaPerPagamenti(String fieldSearch, String valueSearch,
			TipoPartita tipoPartita, boolean loadTipiDocumentoDisabilitati, boolean escludiTipiAreaPartiteDistinta)
			throws TipoDocumentoBaseException {
		return tipiAreaPartitaManager.caricaTipiAreaPartitaPerPagamenti(fieldSearch, valueSearch, tipoPartita,
				loadTipiDocumentoDisabilitati, escludiTipiAreaPartiteDistinta);
	}

	@Override
	public List<TipoDocumentoBasePartite> caricaTipiDocumentoBase() {
		List<TipoDocumentoBasePartite> lista = tipiAreaPartitaManager.caricaTipiDocumentoBase();
		return lista;
	}

	@Override
	public TipoAreaPartita caricaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		return tipiAreaPartitaManager.caricaTipoAreaPartita(tipoAreaPartita);
	}

	@Override
	public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento) {
		return tipiAreaPartitaManager.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
	}

	@Override
	public TipoDocumentoBasePartite caricaTipoDocumentoBase(TipoOperazione tipoOperazione)
			throws TipoDocumentoBaseException {
		return tipiAreaPartitaManager.caricaTipoDocumentoBase(tipoOperazione);
	}

	@Override
	public List<RigaStrutturaPartite> creaRigheStrutturaPartite(StrutturaPartita strutturaPartita, int numeroRate,
			int intervallo) {
		return strutturaPartitaManager.creaRigheStrutturaPartite(strutturaPartita, numeroRate, intervallo);
	}

	@Override
	public Map<String, BigDecimal> getVariabiliAreaPartita() {
		return variabiliFormulaManager.getMapVariabiliStrutturaPartite();
	}

	@Override
	public CategoriaRata salvaCategoriaRata(CategoriaRata categoriaRata) {
		return strutturaPartitaManager.salvaCategoriaRata(categoriaRata);
	}

	@Override
	public RigaStrutturaPartite salvaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite) {
		return strutturaPartitaManager.salvaRigaStrutturaPartite(rigaStrutturaPartite);
	}

	@Override
	public StrutturaPartita salvaStrutturaPartita(StrutturaPartita strutturaPartite) {
		return strutturaPartitaManager.salvaStrutturaPartita(strutturaPartite);
	}

	@Override
	public TipoAreaPartita salvaTipoAreaPartita(TipoAreaPartita tipoAreaPartita)
			throws ModificaTipoAreaConDocumentoException {
		return tipiAreaPartitaManager.salvaTipoAreaPartita(tipoAreaPartita);
	}

	@Override
	public TipoDocumentoBasePartite salvaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase) {
		TipoDocumentoBasePartite tipoDocumentoBaseSalvato = tipiAreaPartitaManager
				.salvaTipoDocumentoBase(tipoDocumentoBase);
		return tipoDocumentoBaseSalvato;
	}

}
