package it.eurotn.panjea.preventivi.manager.documento;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.AreaPreventivoManager;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.RigaPreventivoDAO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto.RigaPreventivoDTOBuilder;
import it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto.RigaPreventivoDTOFactoryBuilder;
import it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto.RigaPreventivoDTOResult;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;

public class RigaPreventivoAbstractDAOBean implements RigaPreventivoDAO {

	private static Logger logger = Logger.getLogger(RigaPreventivoAbstractDAOBean.class);

	@EJB
	protected RigaDocumentoManager rigaDocumentoManager;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@IgnoreDependency
	@EJB
	protected AreaPreventivoManager areaPreventivoManager;

	@Override
	public AreaPreventivo cancellaRigaPreventivo(RigaPreventivo rigaPreventivo) {
		logger.debug("--> Enter cancellaRigaPreventivo");

		try {
			panjeaDAO.delete(rigaPreventivo);
		} catch (Exception e) {
			logger.error("--> errore in cancellaRigaPreventivo.", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaRigaPreventivo");
		return areaPreventivoManager.checkInvalidaAreaPreventivo(rigaPreventivo.getAreaPreventivo());
	}

	@Override
	public RigaPreventivo caricaRigaPreventivo(RigaPreventivo rigaPreventivo) {
		logger.debug("--> Enter caricaRigaPreventivo");
		RigaPreventivo rigaPreventivoResult = null;
		try {
			rigaPreventivoResult = panjeaDAO.load(RigaPreventivo.class, rigaPreventivo.getId());
			// Init dell'area preventivo lazy
			rigaPreventivoResult.getAreaPreventivo().getVersion();

			if (rigaPreventivoResult instanceof RigaArticolo) {
				((RigaArticolo) rigaPreventivoResult).getAttributi().size();
			}
		} catch (ObjectNotFoundException e) {
			logger.error("--> riga preventivo non trovata ", e);
		}
		logger.debug("--> Exit caricaRigaPreventivo");
		return rigaPreventivoResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaPreventivo> caricaRighePreventivo(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter caricaRighePreventivo");

		Query query = panjeaDAO.prepareNamedQuery("RigaPreventivo.caricaByAreaPreventivo");
		query.setParameter("paramAreaPreventivo", areaPreventivo.getId());
		List<RigaPreventivo> righePreventivo = query.getResultList();

		logger.debug("--> Exit caricaRighePreventivo");
		return righePreventivo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaPreventivoDTO> caricaRighePreventivoDTO(AreaPreventivo areaPreventivo) {
		Integer entitaId = null;
		if (areaPreventivo.getDocumento().getEntita() != null
				&& areaPreventivo.getDocumento().getEntita().getId() != null) {
			entitaId = areaPreventivo.getDocumento().getEntita().getId();
		}

		String query = getSQLRighePreventivo(areaPreventivo.getId(), entitaId);

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(query);
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaPreventivoDTOResult.class));
		List<RigaPreventivoDTOResult> righePreventivoDTOResults = null;
		try {
			sqlQuery.addScalar("id");
			sqlQuery.addScalar("idAreaPreventivo");
			sqlQuery.addScalar("tipoRiga");
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("codiceArticolo");
			sqlQuery.addScalar("codiceArticoloEntita");
			sqlQuery.addScalar("descrizioneRiga");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("codiceValutaPrezzoUnitario");
			sqlQuery.addScalar("importoInValutaPrezzoUnitario");
			sqlQuery.addScalar("importoInValutaAziendaPrezzoUnitario");
			sqlQuery.addScalar("tassoDiCambioPrezzoUnitario");
			sqlQuery.addScalar("qtaRiga");
			sqlQuery.addScalar("qtaChiusa");
			sqlQuery.addScalar("codiceValutaPrezzoNetto");
			sqlQuery.addScalar("importoInValutaPrezzoNetto");
			sqlQuery.addScalar("importoInValutaAziendaPrezzoNetto");
			sqlQuery.addScalar("tassoDiCambioPrezzoNetto");
			sqlQuery.addScalar("variazione1");
			sqlQuery.addScalar("variazione2");
			sqlQuery.addScalar("variazione3");
			sqlQuery.addScalar("variazione4");
			sqlQuery.addScalar("codiceValutaPrezzoTotale");
			sqlQuery.addScalar("importoInValutaPrezzoTotale");
			sqlQuery.addScalar("importoInValutaAziendaPrezzoTotale");
			sqlQuery.addScalar("tassoDiCambioPrezzoTotale");
			sqlQuery.addScalar("rigaNota", Hibernate.STRING);
			sqlQuery.addScalar("noteRiga", Hibernate.STRING);
			sqlQuery.addScalar("livello");
			sqlQuery.addScalar("rigaAutomatica");

			righePreventivoDTOResults = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in caricaRighePreventivo per area preventivo con id " + areaPreventivo.getId(), e);
			throw new RuntimeException(e);
		}

		List<RigaPreventivoDTO> righeOrdineDTO = convertResultToDTO(righePreventivoDTOResults);

		return righeOrdineDTO;
	}

	@Override
	public List<RigaPreventivo> caricaRighePreventivoStampa(AreaPreventivo areaPreventivo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Si preoccupa di convertire ed eventualmente raggruppare eventuali righe dove l'articolo padre e il prezzo sono
	 * gli stessi.<br>
	 * Nota che per il raggruppamento vengono unite le righe contigue con padre e prezzo uguali.
	 * 
	 * @param righeBuilder
	 *            la lista di RigaPreventivoDTOResult da scorrere e raggruppare
	 * @return List<RigaPreventivoDTO>
	 */
	private List<RigaPreventivoDTO> convertResultToDTO(List<RigaPreventivoDTOResult> righeBuilder) {
		Map<String, RigaPreventivoDTO> righeComposte = new HashMap<String, RigaPreventivoDTO>();
		List<RigaPreventivoDTO> result = new ArrayList<RigaPreventivoDTO>();

		RigaPreventivoDTOFactoryBuilder factoryBuilder = new RigaPreventivoDTOFactoryBuilder();

		for (RigaPreventivoDTOResult rigaPreventivoDTOResult : righeBuilder) {
			RigaPreventivoDTOBuilder dtoBuilder = factoryBuilder.getBuilder(rigaPreventivoDTOResult);
			dtoBuilder.fillResult(rigaPreventivoDTOResult, result, righeComposte);
		}
		return result;
	}

	@Override
	public IRigaArticoloDocumento creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
		RigaArticolo rigaArticolo = (RigaArticolo) rigaDocumentoManager.creaRigaArticoloDocumento(new RigaArticolo(),
				parametriCreazioneRigaArticolo);
		return rigaArticolo;
	}

	/**
	 * 
	 * @param idAreaPreventivo
	 *            idAreaPreventivo
	 * @param idEntita
	 *            idEntita
	 * @return sql
	 */
	protected String getSQLRighePreventivo(final Integer idAreaPreventivo, final Integer idEntita) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("righe.TIPO_RIGA as tipoRiga, ");
		sb.append("righe.id as id, ");
		sb.append("righe.areaPreventivo_id as idAreaPreventivo, ");
		sb.append("art.id as idArticolo, ");
		sb.append("art.codice as codiceArticolo, ");
		sb.append("(select codiciarti3_.codice from maga_articoli articolo2_ left outer join maga_codici_articolo_entita codiciarti3_ on articolo2_.id=codiciarti3_.articolo_id where codiciarti3_.entita_id=5011 and art.id=articolo2_.id) as codiceArticoloEntita, ");
		sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("righe.descrizione as descrizioneRiga, ");
		sb.append("righe.codiceValuta as codiceValutaPrezzoUnitario, ");
		sb.append("righe.importoInValuta as importoInValutaPrezzoUnitario, ");
		sb.append("righe.importoInValutaAzienda as importoInValutaAziendaPrezzoUnitario, ");
		sb.append("righe.tassoDiCambio as tassoDiCambioPrezzoUnitario, ");
		sb.append("righe.qta as qtaRiga, ");
		sb.append("(select ");
		sb.append("		sum(righeOrd.qta) ");
		sb.append("		from ordi_righe_ordine righeOrd ");
		sb.append("		where ");
		sb.append("		righeOrd.rigaPreventivoCollegata_Id=righe.id) as qtaChiusa, ");
		sb.append("righe.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("righe.codiceValutaNetto as codiceValutaPrezzoNetto, ");
		sb.append("righe.importoInValutaNetto as importoInValutaPrezzoNetto, ");
		sb.append("righe.importoInValutaAziendaNetto as importoInValutaAziendaPrezzoNetto, ");
		sb.append("righe.tassoDiCambioNetto as tassoDiCambioPrezzoNetto, ");
		sb.append("righe.variazione1 as variazione1, ");
		sb.append("righe.variazione2 as variazione2, ");
		sb.append("righe.variazione3 as variazione3, ");
		sb.append("righe.variazione4 as variazione4, ");
		sb.append("righe.codiceValutaTotale as codiceValutaPrezzoTotale, ");
		sb.append("righe.importoInValutaTotale as importoInValutaPrezzoTotale, ");
		sb.append("righe.importoInValutaAziendaTotale as importoInValutaAziendaPrezzoTotale, ");
		sb.append("righe.tassoDiCambioTotale as tassoDiCambioPrezzoTotale, ");
		sb.append("righe.nota as rigaNota, ");
		sb.append("righe.noteRiga as noteRiga, ");
		sb.append("righe.rigaAutomatica as rigaAutomatica, ");
		sb.append("righe.livello as livello ");
		sb.append("from prev_righe_preventivo righe left outer ");
		sb.append("join maga_articoli art on righe.articolo_id=art.id ");
		sb.append("where righe.areaPreventivo_id= ").append(idAreaPreventivo);
		sb.append(" order by righe.ordinamento ");
		return sb.toString();
	}

	@Override
	public RigaPreventivo salvaRigaPreventivo(RigaPreventivo rigaPreventivo) {
		logger.debug("--> Enter salvaRigaPreventivo");

		RigaPreventivo rigaPreventivoSalvata = null;
		rigaPreventivoSalvata = salvaRigaPreventivoNoCheck(rigaPreventivo);
		AreaPreventivo areaPreventivo = areaPreventivoManager.checkInvalidaAreaPreventivo(rigaPreventivoSalvata
				.getAreaPreventivo());
		rigaPreventivoSalvata.setAreaPreventivo(areaPreventivo);

		logger.debug("--> Exit salvaRigaPreventivo");
		return rigaPreventivoSalvata;
	}

	@Override
	public RigaPreventivo salvaRigaPreventivoNoCheck(RigaPreventivo rigaPreventivo) {
		logger.debug("--> Enter salvaRigaPreventivoNoCheck ");
		RigaPreventivo rigaPreventivoResult = null;
		try {
			rigaPreventivoResult = panjeaDAO.save(rigaPreventivo);
			rigaPreventivoResult.getAreaPreventivo().getVersion();
		} catch (Exception e) {
			logger.error("--> errore nel salvare la rigaOrdine", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaRigaPreventivoNoCheck ");
		return rigaPreventivoResult;
	}

}
