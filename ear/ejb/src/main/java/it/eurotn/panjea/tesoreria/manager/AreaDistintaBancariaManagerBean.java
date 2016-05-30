package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaDistintaBancariaContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaDistintaBancariaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaDistintaBancariaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaDistintaBancariaManager")
public class AreaDistintaBancariaManagerBean implements AreaDistintaBancariaManager {

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	private DocumentiManager documentiManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private AreaDistintaBancariaContabilitaManager areaDistintaBancariaContabilitaManager;

	private static Logger logger = Logger.getLogger(AreaDistintaBancariaManagerBean.class.getName());

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		AreaDistintaBancaria areaDistintaBancaria = (AreaDistintaBancaria) areaTesoreria;

		// se esiste più di 1 area collegata non posso cancellare la distinta e lancio una VincoloException
		Long numeroAreeCollegate = areaTesoreriaManager.caricaNumeroAreeCollegate(areaDistintaBancaria
				.getAreaEffettiCollegata());

		if (numeroAreeCollegate > 1) {
			DocumentiCollegatiPresentiException documentoCollegatoException = new DocumentiCollegatiPresentiException(
					"Impossibile cancellare la distinta, esistono altre aree collegate all'area effetti");
			throw new RuntimeException(
					"Impossibile cancellare la distinta, esistono altre aree collegate all'area effetti",
					documentoCollegatoException);
		}

		String updateEffettiSQL = "update part_effetti set dataValuta = null where areaEffetti_id = "
				+ areaDistintaBancaria.getAreaEffettiCollegata().getId();

		SQLQuery updateEffettiQuery = ((Session) panjeaDAO.getEntityManager().getDelegate())
				.createSQLQuery(updateEffettiSQL);

		try {
			if (deleteAreeCollegate) {
				// cancello l'area contabile se esiste
				AreaContabileLite areaContabileLite = areaContabileManager
						.caricaAreaContabileLiteByDocumento(areaDistintaBancaria.getDocumento());
				if (areaContabileLite != null) {
					areaContabileCancellaManager.cancellaAreaContabile(areaDistintaBancaria.getDocumento(), true);
				}
			}
			// annullo la data valuta sugli effetti
			updateEffettiQuery.executeUpdate();

			// cancello l'area distinta
			panjeaDAO.delete(areaDistintaBancaria);

			if (deleteAreeCollegate) {
				// cancello il documento
				documentiManager.cancellaDocumento(areaDistintaBancaria.getDocumento());
			}
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione dell'area distinta.", e);
			throw new RuntimeException("Errore durante la cancellazione dell'area distinta.", e);
		}

		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@Override
	public AreaTesoreria caricaAreaDistinta(AreaEffetti areaEffetti) {
		logger.debug("--> Enter caricaAreaDistinta");

		Query query = panjeaDAO.prepareNamedQuery("AreaDistintaBancaria.caricaByAreaCollegata");
		query.setParameter("paramIdAreaEffettiCollegata", areaEffetti.getId());

		AreaDistintaBancaria areaDistinta = null;
		try {
			areaDistinta = (AreaDistintaBancaria) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento della distinta bancaria collegata all'area effetti", e);
			throw new RuntimeException(
					"errore durante il caricamento della distinta bancaria collegata all'area effetti", e);
		}

		logger.debug("--> Exit caricaAreaDistinta");
		return areaDistinta;
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaDistintaBancaria result = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaDistintaBancaria.carica");
		query.setParameter("paramIdAreaEffetti", idAreaTesoreria);
		try {
			result = (AreaDistintaBancaria) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.error("--> area distinta non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area distinta", e);
			throw new RuntimeException("Errore durante il caricamento dell'area distinta", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@Override
	public AreaDistintaBancaria creaAreaDistintaBancaria(Date dataDocumento, String numeroDocumento,
			AreaEffetti areaEffetti, BigDecimal spese, BigDecimal speseDistinta, Set<Effetto> effetti)
					throws TipoDocumentoBaseException {
		logger.debug("--> Enter creaAreaDistintaBancaria");
		TipoDocumentoBasePartite tipoDocumentoBase = tipiAreaPartitaManager
				.caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.DISTINTA_BANCARIA);
		// documento
		Documento doc = new Documento();
		doc.setCodiceAzienda(areaEffetti.getDocumento().getCodiceAzienda());
		doc.setDataDocumento(dataDocumento);
		doc.getCodice().setCodice(numeroDocumento);
		doc.setTipoDocumento(tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(areaEffetti.getDocumento().getRapportoBancarioAzienda());
		doc.setTotale(areaEffetti.getDocumento().getTotale());
		doc.setContrattoSpesometro(null);
		Documento docSalvato;
		try {
			docSalvato = documentiManager.salvaDocumento(doc);
		} catch (DocumentoDuplicateException e1) {
			throw new RuntimeException(e1);
		}

		// area partite
		AreaDistintaBancaria areaDistintaBancaria = new AreaDistintaBancaria();
		areaDistintaBancaria.setDocumento(docSalvato);
		areaDistintaBancaria.setTipoAreaPartita(tipoDocumentoBase.getTipoAreaPartita());
		areaDistintaBancaria.setCodicePagamento(areaEffetti.getCodicePagamento());
		areaDistintaBancaria.setAreaEffettiCollegata(areaEffetti);
		areaDistintaBancaria.setSpeseIncasso(ObjectUtils.defaultIfNull(spese, BigDecimal.ZERO).add(
				ObjectUtils.defaultIfNull(speseDistinta, BigDecimal.ZERO)));
		areaDistintaBancaria = (AreaDistintaBancaria) areaTesoreriaManager.salvaAreaTesoreria(areaDistintaBancaria);

		// calcolo spesa unitaria
		BigDecimal spesaUnitaria = spese.divide(new BigDecimal(effetti.size()), 2, RoundingMode.HALF_UP);
		// Effetti
		for (Effetto effetto : effetti) {
			try {
				// salvo la spesa di presentazione dell'effetto
				effetto.setSpesePresentazione(spesaUnitaria);
				panjeaDAO.saveWithoutFlush(effetto);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		panjeaDAO.getEntityManager().flush();
		// ricarico la distinta per ricaricare gli effetti con le modifiche apportate
		try {
			areaDistintaBancaria = (AreaDistintaBancaria) caricaAreaTesoreria(areaDistintaBancaria.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			areaDistintaBancariaContabilitaManager.creaAreaContabileDistintaBancaria(areaDistintaBancaria);
		} catch (ContoRapportoBancarioAssenteException e) {
			logger.error("--> Errore durante la creazione dell'area contabile.", e);
			throw new RuntimeException("Errore durante la creazione dell'area contabile.", e);
		}

		logger.debug("--> Exit creaAreaDistintaBancaria");
		return areaDistintaBancaria;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		logger.debug("--> Enter getAreeEmissioneEffetti");

		AreaDistintaBancaria areaDistintaBancaria = (AreaDistintaBancaria) areaTesoreria;

		List<AreaEffetti> list = new ArrayList<AreaEffetti>();
		// se la distinta ha un'area collegata questa è sicuramente l'area emissione effetti
		if (areaDistintaBancaria.getAreaEffettiCollegata() != null
				&& areaDistintaBancaria.getAreaEffettiCollegata().getId() != null) {
			list.add(areaDistintaBancaria.getAreaEffettiCollegata());
		}

		logger.debug("--> Exit getAreeEmissioneEffetti");
		return list;
	}

}
