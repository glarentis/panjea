package it.eurotn.panjea.ordini.manager;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.manager.interfaces.OrdineMovimentazioneManager;
import it.eurotn.panjea.ordini.manager.sqlbuilder.MovimentazioneQueryBuilder;
import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.OrdineMovimentazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OrdineMovimentazioneManager")
public class OrdineMovimentazioneManagerBean implements OrdineMovimentazioneManager {

	private static Logger logger = Logger.getLogger(OrdineMovimentazioneManagerBean.class);

	@Resource
	protected SessionContext context;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage) {
		Date dataInizio = PanjeaEJBUtil
				.getDateTimeToZero(parametriRicercaMovimentazione.getDataRegistrazione().getDataIniziale());
		Date dataFine = PanjeaEJBUtil
				.getDateTimeToZero(parametriRicercaMovimentazione.getDataRegistrazione().getDataFinale());
		ArticoloLite articoloLite = parametriRicercaMovimentazione.getArticoloLite();
		DepositoLite depositoLite = parametriRicercaMovimentazione.getDepositoLite();
		EntitaLite entitaLite = parametriRicercaMovimentazione.getEntitaLite();
		String noteRiga = parametriRicercaMovimentazione.getNoteRiga();
		AgenteLite agente = parametriRicercaMovimentazione.getAgente();
		Set<TipoAreaOrdine> tipiAreaOrdine = parametriRicercaMovimentazione.getTipiAreaOrdine();

		Date dataConsegnaInizio = null;
		if (parametriRicercaMovimentazione.getDataConsegna().getDataIniziale() != null) {
			dataConsegnaInizio = PanjeaEJBUtil
					.getDateTimeToZero(parametriRicercaMovimentazione.getDataConsegna().getDataIniziale());
		}
		Date dataConsegnaFine = null;
		if (parametriRicercaMovimentazione.getDataConsegna().getDataFinale() != null) {
			dataConsegnaFine = PanjeaEJBUtil
					.getDateTimeToZero(parametriRicercaMovimentazione.getDataConsegna().getDataFinale());
		}

		String sqlStringQuery = MovimentazioneQueryBuilder.getSqlMovimentazione(articoloLite, depositoLite, entitaLite,
				dataInizio, dataFine, getAzienda(), false, tipiAreaOrdine, noteRiga, dataConsegnaInizio,
				dataConsegnaFine, parametriRicercaMovimentazione.getStatoRiga(), agente,
				parametriRicercaMovimentazione.isRigheOmaggio());

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sqlStringQuery);
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setFirstResult((page - 1) * sizeOfPage).setMaxResults(sizeOfPage);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaMovimentazione.class));
		List<RigaMovimentazione> righeMovimentazione = null;
		try {
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("codiceArticolo");
			sqlQuery.addScalar("descrizioneArticolo");
			sqlQuery.addScalar("idDeposito");
			sqlQuery.addScalar("codiceDeposito");
			sqlQuery.addScalar("descrizioneDeposito");
			sqlQuery.addScalar("areaOrdineId");
			sqlQuery.addScalar("dataRegistrazione");
			sqlQuery.addScalar("dataDocumento");
			sqlQuery.addScalar("numeroDocumento");
			sqlQuery.addScalar("idTipoDocumento");
			sqlQuery.addScalar("codiceTipoDocumento");
			sqlQuery.addScalar("descrizioneTipoDocumento");
			sqlQuery.addScalar("idEntita");
			sqlQuery.addScalar("codiceEntita");
			sqlQuery.addScalar("descrizioneEntita");
			sqlQuery.addScalar("tipoEntita");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("numeroDecimaliQuantita");
			sqlQuery.addScalar("prezzoUnitario");
			sqlQuery.addScalar("prezzoNetto");
			sqlQuery.addScalar("PrezzoTotale");
			sqlQuery.addScalar("variazione1");
			sqlQuery.addScalar("variazione2");
			sqlQuery.addScalar("variazione3");
			sqlQuery.addScalar("variazione4");
			sqlQuery.addScalar("quantita");
			sqlQuery.addScalar("quantitaEvasa");
			sqlQuery.addScalar("evasioneForzata");
			sqlQuery.addScalar("noteRiga");
			sqlQuery.addScalar("dataOrdine");
			sqlQuery.addScalar("numeroOrdineRicezione");
			
			Properties params = new Properties();
			params.put("enumClass", "it.eurotn.panjea.ordini.domain.documento.evasione.RiferimentiOrdine$ModalitaRicezione");
			params.put("type", "12");
			sqlQuery.addScalar("modalitaRicezione", Hibernate.custom(org.hibernate.type.EnumType.class, params));
			
			Properties paramsO = new Properties();			
			paramsO.put("enumClass", "it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio");
			paramsO.put("type", "12");
			sqlQuery.addScalar("tipoOmaggio", Hibernate.custom(org.hibernate.type.EnumType.class, paramsO));
			
			sqlQuery.addScalar("dataConsegna");
			sqlQuery.addScalar("rigaOrdineId");
			righeMovimentazione = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in caricaRigheArticoloMovimentazione", e);
			throw new RuntimeException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit caricaMovimentazione " + righeMovimentazione.size());
		}
		return righeMovimentazione;
	}

	/**
	 *
	 * @return codice Azienda loggata
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

}
