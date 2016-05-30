package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoAssegnoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoAssegnoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAssegnoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.security.JecPrincipal;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author leonardo
 */
@Stateless(name = "Panjea.AreaAccreditoAssegnoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAccreditoAssegnoManager")
public class AreaAccreditoAssegnoManagerBean implements AreaAccreditoAssegnoManager {

	private static Logger logger = Logger.getLogger(AreaAccreditoAssegnoManagerBean.class.getName());

	@Resource
	protected SessionContext context;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	@IgnoreDependency
	protected TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	@IgnoreDependency
	protected DocumentiManager documentiManager;

	@EJB
	@IgnoreDependency
	protected AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	protected AreaAssegnoContabilitaManager areaAssegnoContabilitaManager;

	@EJB
	protected AreaAccreditoAssegnoContabilitaManager areaAccreditoAssegnoContabilitaManager;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");
		try {
			AreaAccreditoAssegno areaAccreditoAssegno = panjeaDAO.load(AreaAccreditoAssegno.class,
					areaTesoreria.getId());

			if (deleteAreeCollegate) {
				areaAccreditoAssegnoContabilitaManager.cancellaAreaContabileAccreditoAssegno(areaAccreditoAssegno);
			}
			areaAssegnoContabilitaManager.cancellaAreeContabiliAssegni(areaAccreditoAssegno);

			// annullo data pagamento e link con l'areaAccreditoAssegno sul pagamento.
			StringBuffer updatePagamentiSQL = new StringBuffer();
			updatePagamentiSQL
					.append("update part_pagamenti set part_pagamenti.dataPagamento=null,part_pagamenti.areaAccreditoAssegno_id=null");
			updatePagamentiSQL.append(" where part_pagamenti.areaAccreditoAssegno_id=" + areaAccreditoAssegno.getId());

			Query updatePagamentiQuery = panjeaDAO.getEntityManager().createNativeQuery(updatePagamentiSQL.toString());
			updatePagamentiQuery.executeUpdate();

			// cancello l'area accredito assegno
			panjeaDAO.delete(areaAccreditoAssegno);

			if (deleteAreeCollegate) {
				// cancello il documento
				documentiManager.cancellaDocumento(areaAccreditoAssegno.getDocumento());
			}
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione dell'areaAccreditoAssegno", e);
			throw new RuntimeException("Errore durante la cancellazione dell'areaAccreditoAssegno", e);
		}
		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaAccreditoAssegno result = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaAccreditoAssegno.carica");
		query.setParameter("paramIdAreaAccreditoAssegno", idAreaTesoreria);
		try {
			result = (AreaAccreditoAssegno) panjeaDAO.getSingleResult(query);
			result.getPagamenti().size();
		} catch (ObjectNotFoundException e) {
			logger.error("--> area accredito assegno non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area accredito assegno", e);
			throw new RuntimeException("Errore durante il caricamento dell'area accredito assegno", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@Override
	public AreaAccreditoAssegno creaAreaAccreditoAssegno(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<AreaAssegno> assegni) {

		if (assegni == null || (assegni != null && assegni.size() == 0)) {
			// se non ho assegni esco senza creare nulla
			return null;
		}

		// carico il tipo documento base
		TipoDocumentoBasePartite tipoDocumentoBase;
		try {
			tipoDocumentoBase = tipiAreaPartitaManager
					.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.ACCREDITO_ASSEGNO);
		} catch (Exception e1) {
			logger.error("--> Errore, tipo documento base per area accredito assegno non trovato", e1);
			throw new RuntimeException("Errore, tipo documento base per area accredito assegno non trovato", e1);
		}

		// lista degli id di tutti gli assegni separati da virgola
		String listIdAssegni = "";

		// importo totale di tutti gli assegni
		Importo totaleDoc = null;
		for (AreaAssegno areaAssegno : assegni) {
			listIdAssegni = listIdAssegni + areaAssegno.getId() + ",";
			if (totaleDoc == null) {
				totaleDoc = areaAssegno.getDocumento().getTotale().clone();
			} else {
				totaleDoc = totaleDoc.add(areaAssegno.getDocumento().getTotale(), 2);
			}
		}
		// tolgo la virgola finale
		if (!listIdAssegni.isEmpty() && listIdAssegni.indexOf(",") != -1) {
			listIdAssegni = listIdAssegni.substring(0, listIdAssegni.length() - 1);
		}

		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(parametriCreazioneAreaChiusure.getDataDocumento());
		doc.setTipoDocumento(tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(parametriCreazioneAreaChiusure.getRapportoBancarioAzienda());
		doc.setTotale(totaleDoc);
		doc.setContrattoSpesometro(null);

		Documento docSalvato;
		try {
			docSalvato = documentiManager.salvaDocumento(doc);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del documento.", e);
			throw new RuntimeException("Errore durante il salvataggio del documento.", e);
		}

		AreaAccreditoAssegno areaAccreditoAssegno = new AreaAccreditoAssegno();
		areaAccreditoAssegno.setDocumento(docSalvato);
		areaAccreditoAssegno.setTipoAreaPartita(tipoDocumentoBase.getTipoAreaPartita());
		areaAccreditoAssegno.setCodicePagamento(null);

		AreaAccreditoAssegno accreditoAssegnoSalvato = (AreaAccreditoAssegno) areaTesoreriaManager
				.salvaAreaTesoreria(areaAccreditoAssegno);

		Format dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dataPagamento = dateFormat.format(accreditoAssegnoSalvato.getDocumento().getDataDocumento());

		// Imposto la data pagamento e il link con l'accredito assegno sui pagamenti della lista degli assegni scelti.
		StringBuffer updatePagamentiSQL = new StringBuffer();
		updatePagamentiSQL.append("update part_pagamenti set part_pagamenti.dataPagamento='" + dataPagamento
				+ "',part_pagamenti.areaAccreditoAssegno_id=" + accreditoAssegnoSalvato.getId());
		updatePagamentiSQL.append(" where part_pagamenti.areaChiusure_id in (" + listIdAssegni + ")");

		Query updatePagamentiQuery = panjeaDAO.getEntityManager().createNativeQuery(updatePagamentiSQL.toString());
		updatePagamentiQuery.executeUpdate();

		// creo l'area contabile dell'area accredito assegno
		areaAccreditoAssegnoContabilitaManager.creaAreaContabileAccreditoAssegno(accreditoAssegnoSalvato);

		// creo le aree contabili delle aree assegno legate all'area accredito assegno
		areaAssegnoContabilitaManager.creaAreeContabiliAssegni(accreditoAssegnoSalvato);

		return accreditoAssegnoSalvato;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		// l'area accredito assegno non ha area emissione effetti quindi ritorno una lista vuota
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

}
