package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccontoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccontoManager;
import it.eurotn.panjea.tesoreria.service.exception.AccontoUtilizzatoException;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.Matcher;

/**
 * 
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaAccontoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAccontoManager")
public class AreaAccontoManagerBean implements AreaAccontoManager {

	private static Logger logger = Logger.getLogger(AreaAccontoManagerBean.class);

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private AreaAccontoContabilitaManager areaAccontoContabilitaManager;

	@EJB
	private DocumentiManager documentiManager;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		AreaAcconto areaAcconto = (AreaAcconto) areaTesoreria;

		if (areaAcconto.getImportoUtilizzato().compareTo(BigDecimal.ZERO) != 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Impossibile cancellare l'acconto perchè utilizzato.");
			}
			AccontoUtilizzatoException accontoUtilizzatoException = new AccontoUtilizzatoException(
					"Impossibile cancellare l'acconto perchè utilizzato.");
			throw new RuntimeException(accontoUtilizzatoException);
		}

		if (deleteAreeCollegate) {
			// cancello l'area contabile
			areaAccontoContabilitaManager.cancellaAreaContabileAcconto(areaAcconto);
		}
		try {
			panjeaDAO.delete(areaAcconto);
		} catch (Exception e) {
			logger.error("-->errore nel cancellare l'acconto con id " + areaAcconto.getId(), e);
			throw new RuntimeException("errore nel cancellare l'acconto con id " + areaAcconto.getId(), e);
		}
		if (deleteAreeCollegate) {
			// cancello il documento
			documentiManager.cancellaDocumento(areaTesoreria.getDocumento());
		}

		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaAcconto> caricaAccontiAutomaticiPerPagamenti(EntitaLite entita, String codiceValuta) {
		logger.debug("--> Enter caricaAccontiAutomaticiPerPagamenti");

		String hql = "select a from AreaAcconto a where a.documento.codiceAzienda=:paramCodiceAzienda and a.documento.entita=:paramEntita and a.documento.totale.codiceValuta = :paramCodiceValuta and a.automatico = true order by a.documento.dataDocumento";
		Query query = panjeaDAO.prepareQuery(hql);
		query.setParameter("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
		query.setParameter("paramEntita", entita);
		query.setParameter("paramCodiceValuta", codiceValuta);

		List<AreaAcconto> list = new ArrayList<AreaAcconto>();
		list = query.getResultList();
		if (logger.isDebugEnabled()) {
			logger.debug("--> Acconti utilizzabili trovati: " + list.size());
		}

		EventList<AreaAcconto> areeEvent = new BasicEventList<AreaAcconto>();
		areeEvent.addAll(list);
		FilterList<AreaAcconto> filterList = new FilterList<AreaAcconto>(areeEvent, new Matcher<AreaAcconto>() {

			@Override
			public boolean matches(AreaAcconto areaAcconto) {
				return areaAcconto.getResiduo().compareTo(BigDecimal.ZERO) > 0;
			}
		});
		if (logger.isDebugEnabled()) {
			logger.debug("--> Acconti con residuo > 0 trovati: " + filterList.size());
		}

		logger.debug("--> Exit caricaAccontiAutomaticiPerPagamenti");
		return filterList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaAcconto> caricaAreaAcconti(ParametriRicercaAcconti parametriRicercaAcconti) {
		logger.debug("--> Enter caricaAcconti");

		StringBuffer sb = new StringBuffer();
		sb.append("select a from AreaAcconto a ");
		sb.append("where a.documento.codiceAzienda=:paramCodiceAzienda ");

		switch (parametriRicercaAcconti.getStatoAcconto()) {
		case APERTO:
			sb.append(" and  importoUtilizzato < documento.totale.importoInValuta");
			break;
		case CHIUSO:
			sb.append(" and  importoUtilizzato >= documento.totale.importoInValuta");
			break;
		default:
			break;
		}

		if (parametriRicercaAcconti.getCodiceValuta() != null) {
			sb.append(" and a.documento.totale.codiceValuta = '" + parametriRicercaAcconti.getCodiceValuta() + "' ");
		}

		switch (parametriRicercaAcconti.getTipoEntita()) {
		case CLIENTE:
			sb.append(" and a.documento.entita.class = 'C' ");
			break;
		case FORNITORE:
			sb.append(" and a.documento.entita.class = 'F' ");
			break;
		case AZIENDA:
			sb.append(" and a.documento.entita is null ");
			break;
		default:
			break;
		}

		if (parametriRicercaAcconti.getDaDataDocumento() != null) {
			sb.append(" and a.documento.dataDocumento >= :paramDaData");
		}
		if (parametriRicercaAcconti.getADataDocumento() != null) {
			sb.append(" and a.documento.dataDocumento <= :paramAData");
		}

		if (parametriRicercaAcconti.getEntita() != null && parametriRicercaAcconti.getEntita().getId() != null) {
			sb.append(" and a.documento.entita = :paramEntita");
		}

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
		if (parametriRicercaAcconti.getEntita() != null && parametriRicercaAcconti.getEntita().getId() != null) {
			query.setParameter("paramEntita", parametriRicercaAcconti.getEntita());
		}
		if (parametriRicercaAcconti.getDaDataDocumento() != null) {
			query.setParameter("paramDaData", parametriRicercaAcconti.getDaDataDocumento());
		}
		if (parametriRicercaAcconti.getADataDocumento() != null) {
			query.setParameter("paramAData", parametriRicercaAcconti.getADataDocumento());
		}

		List<AreaAcconto> listResult = new ArrayList<AreaAcconto>();

		try {
			listResult = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento degli acconti.", e);
			throw new RuntimeException("errore durante il caricamento degli acconti.", e);
		}
		logger.debug("--> Exit caricaAcconti");
		return listResult;
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaAcconto result = null;
		try {
			result = panjeaDAO.load(AreaAcconto.class, idAreaTesoreria);
		} catch (Exception e) {
			logger.error("-->errore nel caricare l'area acconto", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pagamento> caricaPagamentiByAreaAcconto(AreaAcconto areaAcconto) {
		logger.debug("--> Enter caricaPagamentiByAreaAcconto");
		String hql = "select p from Pagamento p left join fetch p.effetto inner join fetch  p.areaChiusure where p.areaAcconto=:areaAcconto";
		Query query = panjeaDAO.prepareQuery(hql);
		query.setParameter("areaAcconto", areaAcconto);
		List<Pagamento> result = query.getResultList();
		logger.debug("--> Exit caricaPagamentiByAreaAcconto");
		return result;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		// l'area acconto non ha area emissione effetti quindi ritorno una lista vuota
		return new ArrayList<AreaEffetti>();
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return utente loggato
	 */
	private JecPrincipal getJecPrincipal() {
		logger.debug("--> Enter getJecPrincipal");
		return (JecPrincipal) context.getCallerPrincipal();
	}

	@Override
	public AreaAcconto salvaAcconto(AreaAcconto areaAcconto) throws TipoDocumentoBaseException {
		logger.debug("--> Enter salvaAcconto");

		// recupero il tipoOperazione per scegliere il tipoDocumento da associare e quale areaContabile creare
		TipoOperazione tipoOperazione = null;
		EntitaLite entitaAcconto = areaAcconto.getDocumento().getEntita();
		boolean entitaPresente = entitaAcconto != null && entitaAcconto.getId() != null;
		if (entitaPresente && "F".equals(entitaAcconto.getTipo())) {
			tipoOperazione = TipoOperazione.ACCONTO_FORNITORE;
		} else if (entitaPresente && "C".equals(entitaAcconto.getTipo())) {
			tipoOperazione = TipoOperazione.ACCONTO_CLIENTE;
		} else {
			tipoOperazione = TipoOperazione.ACCONTO_IVA;
		}

		// se sto salvando un nuovo documento acconto, carico il tipo documento base
		if (areaAcconto.isNew()) {
			areaAcconto.getDocumento().setCodiceAzienda(getJecPrincipal().getCodiceAzienda());

			// se è il primo salvataggio devo caricare il tipo documento da quelli base impostati
			if (tipoOperazione == null) {
				throw new IllegalArgumentException(
						"Entità su documento deve essere cliente o fornitore.Documento con id "
								+ areaAcconto.getDocumento().getId());
			}

			TipoDocumentoBasePartite tipoDocumentoBase = tipiAreaPartitaManager.caricaTipoDocumentoBase(tipoOperazione);
			if (tipoDocumentoBase == null) {
				throw new TipoDocumentoBaseException(new String[] { "Tipo operazione " + tipoOperazione.name() });
			}
			areaAcconto.getDocumento().setTipoDocumento(tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento());
			areaAcconto.setTipoAreaPartita(tipoDocumentoBase.getTipoAreaPartita());
		} else {
			// cancello l'area contabile
			areaAccontoContabilitaManager.cancellaAreaContabileAcconto(areaAcconto);
		}

		try {
			Documento documentoSalvato = documentiManager.salvaDocumento(areaAcconto.getDocumento());
			areaAcconto.setDocumento(documentoSalvato);
			areaAcconto = panjeaDAO.save(areaAcconto);
		} catch (Exception e) {
			logger.error("-->errore nel salvare l'area acconto", e);
			throw new RuntimeException(e);
		}
		// Creo l'area contabile
		// tutto il metodo di creazione delle aree contabili dovra' essere rifatto completamente appoggiandosi alle
		// strutture del tipodocumento, per ora, per ragioni di tempo non e' possibile eseguire il refactoring, mi
		// permetto quindi di verificare il tipoOperazione e di scegliere nel caso di TipoEntita AZIENDA il
		// tipoOperazione ACCONTOIVA, creando ad hoc una area contabile per acconto iva
		if (tipoOperazione != null && TipoOperazione.ACCONTO_IVA.equals(tipoOperazione)) {
			areaAccontoContabilitaManager.creaAreaContabileAccontoIva(areaAcconto);
		} else {
			areaAccontoContabilitaManager.creaAreaContabileAcconto(areaAcconto);
		}
		logger.debug("--> Exit salvaAcconto");
		return areaAcconto;
	}
}
