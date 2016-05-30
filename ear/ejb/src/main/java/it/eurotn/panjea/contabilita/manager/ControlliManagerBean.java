package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.BilancioManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ControlliManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.DocumentoEntitaDTO;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.RisultatoControlloProtocolli;
import it.eurotn.panjea.contabilita.util.RisultatoControlloRateSaldoContabile;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ContabilitaControlliManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ContabilitaControlliManagerBean")
public class ControlliManagerBean implements ControlliManager {
	private static Logger logger = Logger.getLogger(ControlliManagerBean.class);
	@EJB
	private PanjeaDAO panjeaDAO;
	@EJB
	private BilancioManager bilancioManager;
	@EJB
	private RateManager rateManager;

	@EJB
	private ContabilitaSettingsManager contabilitaSettingsManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentoEntitaDTO> caricaRiepilogoDocumentiBlacklist(Date dataIniziale, Date dataFinale) {
		logger.debug("--> Enter caricaRiepilogoDocumentiBlacklist");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("ent.TIPO_ANAGRAFICA as tipoEntita, ");
		sb.append("ent.id as idEntita, ");
		sb.append("ent.codice as codiceEntita, ");
		sb.append("anag.denominazione as denominazioneEntita, ");
		sb.append("anag.codice_fiscale as codiceFiscale, ");
		sb.append("anag.partita_iva as partitaIva, ");
		sb.append("naz.descrizione as nazione, ");
		sb.append("tipoDoc.codice as codiceTipoDocumento, ");
		sb.append("tipoDoc.descrizione as descrizioneTipoDocumento, ");
		sb.append("doc.dataDocumento as dataDocumento, ");
		sb.append("doc.codice as numeroDocumento, ");
		sb.append("sum(righeIva.importoInValutaAziendaImponibile) as totaleImponibile, ");
		sb.append("codIva.codice as codiceIva ");
		sb.append("from cont_area_contabile ac inner join docu_documenti doc on ac.documento_id=doc.id ");
		sb.append("					   inner join docu_tipi_documento tipoDoc on doc.tipo_documento_id=tipoDoc.id ");
		sb.append("					   inner join anag_entita ent on doc.entita_id=ent.id ");
		sb.append("					   inner join anag_anagrafica anag on ent.anagrafica_id = anag.id ");
		sb.append("					   inner join anag_sedi_entita sedeEnt on doc.sedeEntita_id=sedeEnt.id ");
		sb.append("					   inner join anag_sedi_anagrafica sedeAnag on sedeEnt.sede_anagrafica_id=sedeAnag.id ");
		sb.append("					   inner join cont_aree_iva ai on ai.documento_id=ac.documento_id ");
		sb.append("					   inner join cont_righe_iva righeIva on ai.id=righeIva.areaIva_id ");
		sb.append("					   inner join anag_codici_iva codIva on righeIva.codiceIva_id=codIva.id ");
		sb.append("					   inner join geog_nazioni naz on sedeAnag.nazione_id=naz.id and naz.blacklist=1 ");
		sb.append("where doc.dataDocumento>= '" + dateFormat.format(dataIniziale) + "' ");
		sb.append("and doc.dataDocumento<= '" + dateFormat.format(dataFinale) + "' ");
		sb.append("group by ent.id , doc.id , righeIva.codiceIva_id ");
		sb.append("order by ent.TIPO_ANAGRAFICA, ent.codice, doc.codiceOrder ");

		Query query = panjeaDAO.prepareSQLQuery(
				sb.toString(),
				DocumentoEntitaDTO.class,
				Arrays.asList(new String[] { "tipoEntita", "idEntita", "codiceEntita", "denominazioneEntita",
						"codiceFiscale", "partitaIva", "nazione", "codiceTipoDocumento", "descrizioneTipoDocumento",
						"dataDocumento", "numeroDocumento", "totaleImponibile", "codiceIva" }));

		List<DocumentoEntitaDTO> documenti = new ArrayList<DocumentoEntitaDTO>();
		try {
			documenti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante la ricerca dei documenti.", e);
			throw new RuntimeException("errore durante la ricerca dei documenti.", e);
		}

		return documenti;
	}

	/**
	 * Trova i protocolli mancanti per un determinato protocollo e per i documenti con anno>=annoVerifica.
	 *
	 * @param annoVerifica
	 *            l'anno da cui iniziare la ricerca
	 * @param protocollo
	 *            il protocollo di cui trovare i codici mancanti
	 * @return List<RisultatoControlloProtocolli>
	 */
	@SuppressWarnings("unchecked")
	private List<RisultatoControlloProtocolli> trovaProtocolliMancanti(Integer annoVerifica, String protocollo) {
		logger.debug("--> Enter verificaProtocolliMancanti");

		List<RisultatoControlloProtocolli> protocolliMancantiList = new ArrayList<RisultatoControlloProtocolli>();

		// carico per ogni anno il valore protocollo massimo che trovo
		StringBuilder sb = new StringBuilder(500);
		sb.append("select ac.annoMovimento as anno, max(ac.valoreProtocollo) as valoreProtocollo ");
		sb.append("from cont_area_contabile ac inner join docu_documenti doc on ac.documento_id = doc.id ");
		sb.append("					   inner join docu_tipi_documento tipoDoc on doc.tipo_documento_id = tipoDoc.id ");
		sb.append("					   inner join cont_tipi_area_contabile tac on ac.tipoAreaContabile_id = tac.id ");
		sb.append("where ac.annoMovimento >= " + annoVerifica + " and ac.statoAreaContabile < 2 and ");
		sb.append("	 ((tac.protocolloLikeNumDoc = true and tipoDoc.registroProtocollo = '");
		sb.append(protocollo);
		sb.append("') or tac.registroProtocollo = '");
		sb.append(protocollo);
		sb.append("') ");
		sb.append("group by ac.annoMovimento ");
		sb.append("order by ac.annoMovimento ");
		SQLQuery queryMaxProtocolli = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb
				.toString());
		queryMaxProtocolli.addScalar("anno", Hibernate.INTEGER);
		queryMaxProtocolli.addScalar("valoreProtocollo", Hibernate.INTEGER);
		List<Object[]> maxProtocolli = queryMaxProtocolli.list();

		// nella mappa inserisco come chiave l'anno e come valore un array di grandezza pari al valore massimo del
		// protocollo
		Map<Integer, Integer[]> mapProtocolli = new HashMap<Integer, Integer[]>();
		for (Object[] objects : maxProtocolli) {
			if (objects[1] != null) {
				mapProtocolli.put((Integer) objects[0], new Integer[(Integer) objects[1]]);
			}
		}

		// carico il valore dei protocolli esistenti
		sb = new StringBuilder(500);
		sb.append("select ac.annoMovimento as anno, ");
		sb.append("	  ac.valoreProtocollo as valoreProtocollo ");
		sb.append("from cont_area_contabile ac inner join docu_documenti doc on ac.documento_id = doc.id ");
		sb.append("					   inner join docu_tipi_documento tipoDoc on doc.tipo_documento_id = tipoDoc.id ");
		sb.append("					   inner join cont_tipi_area_contabile tac on ac.tipoAreaContabile_id = tac.id ");
		sb.append("where ac.annoMovimento >= " + annoVerifica + " and ac.statoAreaContabile < 2 and ");
		sb.append("	 ((tac.protocolloLikeNumDoc = true and tipoDoc.registroProtocollo = '");
		sb.append(protocollo);
		sb.append("') or tac.registroProtocollo = '");
		sb.append(protocollo);
		sb.append("') ");
		sb.append("order by ac.annoMovimento,ac.valoreProtocollo ");
		SQLQuery queryProtocolli = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());
		queryProtocolli.addScalar("anno", Hibernate.INTEGER);
		queryProtocolli.addScalar("valoreProtocollo", Hibernate.INTEGER);
		List<Object[]> protocolliEsistenti = queryProtocolli.list();

		// avvaloro i relativi valori nell'array
		for (Object[] prot : protocolliEsistenti) {
			Integer anno = (Integer) prot[0];
			Integer valoreProt = (Integer) prot[1];
			if (valoreProt != null) {
				mapProtocolli.get(anno)[valoreProt - 1] = valoreProt;
			}
		}

		for (Entry<Integer, Integer[]> entry : mapProtocolli.entrySet()) {
			int anno = entry.getKey();
			Integer[] protocolliArray = entry.getValue();

			for (int i = 0; i < protocolliArray.length; i++) {
				if (protocolliArray[i] == null) {
					protocolliMancantiList.add(new RisultatoControlloProtocolli(protocollo, i + 1, anno));
				}
			}
		}

		return protocolliMancantiList;
	}

	@Override
	public List<RisultatoControlloProtocolli> verificaDataProtocolli(Integer annoVerifica) {
		logger.debug("--> Enter verificaDataProtocolli");
		// Eseguo la query che mi ritorna la lista di tutti i protocolli per anno ordinati per data
		StringBuilder sb = new StringBuilder(1000);
		sb.append("select ");
		sb.append("(case when tac.protocolloLikeNumDoc = true then tipoDoc.registroProtocollo else  tac.registroProtocollo end) as regProtocollo, ");
		sb.append("ac.valoreProtocollo, ");
		sb.append("ac.dataRegistrazione, ");
		sb.append("ac.annoMovimento ");
		sb.append("from AreaContabile ac ");
		sb.append("join ac.tipoAreaContabile tac ");
		sb.append("inner join ac.documento doc ");
		sb.append("inner join doc.tipoDocumento tipoDoc ");
		sb.append("where ");
		sb.append("(");
		sb.append("(tac.registroProtocollo is not null and tac.registroProtocollo != '') ");
		sb.append(" or (tac.protocolloLikeNumDoc = true) ");
		sb.append(")");
		sb.append(" and ac.valoreProtocollo is not null ");
		sb.append(" and ac.statoAreaContabile < 2 ");
		sb.append(" and ac.annoMovimento >= :paramAnno ");
		sb.append("order by ");
		sb.append("(case when tac.protocolloLikeNumDoc = true then tipoDoc.registroProtocollo else  tac.registroProtocollo end), ");
		sb.append("ac.dataRegistrazione, ");
		sb.append("ac.valoreProtocollo ");

		Query queryProtocolli = panjeaDAO.prepareQuery(sb.toString());
		queryProtocolli.setParameter("paramAnno", annoVerifica);
		List<RisultatoControlloProtocolli> result = new ArrayList<RisultatoControlloProtocolli>();
		try {
			@SuppressWarnings("unchecked")
			List<Object[]> protocolli = panjeaDAO.getResultList(queryProtocolli);
			String registroProtocolloPrecedente = "";
			int codicePrecedente = -1;
			Date dataPrecedente = null;
			int annoPrecedente = -1;
			for (Object[] protocollo : protocolli) {
				String registroProtocollo = (String) protocollo[0];
				int codice = (Integer) protocollo[1];
				Date data = (Date) protocollo[2];
				Integer anno = (Integer) protocollo[3];
				if (codice < codicePrecedente && data.after(dataPrecedente)
						&& registroProtocolloPrecedente.equals(registroProtocollo) && anno == annoPrecedente) {
					result.add(new RisultatoControlloProtocolli(registroProtocollo, codice, data, anno));
				} else {
					// se il codice è sballato non lo setto come codice precedente, altrimenti diventa il codice per il
					// prossimo
					// ciclo
					codicePrecedente = codice;
					dataPrecedente = data;
				}
				registroProtocolloPrecedente = registroProtocollo;
				annoPrecedente = anno;
			}
		} catch (DAOException e) {
			logger.error("-->errore nel recuparare la lista dei protocolli ordinati per data regiatrazione", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit verificaDataProtocolli");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RisultatoControlloProtocolli> verificaProtocolliMancanti(Integer annoVerifica) {
		logger.debug("--> Enter verificaProtocolliMancanti");
		// carico la lista di protocolli utilizzati nei documenti dall'anno di verifica in poi
		StringBuffer sb = new StringBuffer(550);
		sb.append("select ");
		sb.append("(case when tac1.protocolloLikeNumDoc=true then td1.registroProtocollo else tac1.registroProtocollo end) as protocollo ");
		sb.append("from cont_area_contabile ac1 ");
		sb.append("inner join cont_tipi_area_contabile tac1 on ac1.tipoAreaContabile_id=tac1.id ");
		sb.append("inner join docu_tipi_documento td1 on tac1.tipoDocumento_id=td1.id ");
		sb.append("where  ( tac1.registroProtocollo is not null and tac1.registroProtocollo<>'' ) or (tac1.protocolloLikeNumDoc) ");
		sb.append("and ac1.annoMovimento>=:paramAnno and ac1.statoAreaContabile < 2 ");
		sb.append("group by case when tac1.protocolloLikeNumDoc=true then td1.registroProtocollo else tac1.registroProtocollo end");

		SQLQuery queryProtocolliUtilizzati = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb
				.toString());
		queryProtocolliUtilizzati.setParameter("paramAnno", annoVerifica);
		queryProtocolliUtilizzati.addScalar("protocollo");
		List<String> protocolliUtilizzati = queryProtocolliUtilizzati.list();

		List<RisultatoControlloProtocolli> codiciMancanti = new ArrayList<RisultatoControlloProtocolli>();
		for (String protocollo : protocolliUtilizzati) {
			List<RisultatoControlloProtocolli> codiciMancantiPerProtocollo = trovaProtocolliMancanti(annoVerifica,
					protocollo);
			codiciMancanti.addAll(codiciMancantiPerProtocollo);
		}

		logger.debug("--> Exit verificaProtocolliMancanti");
		return codiciMancanti;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaContabileDTO> verificaRigheSenzaCentriDiCosto() {
		List<AreaContabileDTO> result = new ArrayList<AreaContabileDTO>();

		ContabilitaSettings settings = contabilitaSettingsManager.caricaContabilitaSettings();

		StringBuffer sb = new StringBuffer(550);
		sb.append("select distinct ");
		sb.append("ac.id as id, ");
		sb.append("ac.documento.id as idDocumento, ");
		sb.append("ac.documento.dataDocumento as dataDocumento, ");
		sb.append("ac.dataRegistrazione as dataRegistrazione, ");
		sb.append("ac.documento.codice as numeroDocumento, ");
		sb.append("ac.documento.codiceAzienda as codiceAzienda, ");
		sb.append("ac.codice as numeroProtocollo, ");
		sb.append("ac.documento.totale as totale, ");
		sb.append("ent.id as idEntita, ");
		sb.append("ent.codice as codiceEntita, ");
		sb.append("anag.denominazione as ragioneSocialeEntita, ");
		sb.append("rb.descrizione as descrizioneRapportoBancario, ");
		sb.append("rb.id as idRapportoBancario, ");
		sb.append("ac.documento.tipoDocumento.id as idTipoDocumento, ");
		sb.append("ac.documento.tipoDocumento.codice as codiceTipoDocumento, ");
		sb.append("ac.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
		sb.append("ac.documento.tipoDocumento.tipoEntita as tipoEntita ");
		sb.append("from RigaContabile r join r.areaContabile ac ");
		sb.append("left join ac.documento.rapportoBancarioAzienda rb ");
		sb.append("left join ac.documento.entita ent ");
		sb.append("left join ent.anagrafica anag ");
		sb.append("where r.conto.soggettoCentroCosto=true and r.righeCentroCosto.size=0 ");

		if (settings.getDataControlloCentriDiCosto() != null) {
			sb.append(" and ac.dataRegistrazione >= :dataControllo ");
		}

		Query query = panjeaDAO.prepareQuery(sb.toString(), AreaContabileDTO.class, new ArrayList<String>());

		if (settings.getDataControlloCentriDiCosto() != null) {
			query.setParameter("dataControllo", settings.getDataControlloCentriDiCosto());
		}

		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore nel caricare le righe contabili senza centri di costo", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public List<RisultatoControlloRateSaldoContabile> verificaSaldi(SottotipoConto sottotipoConto) {
		// Carico il bilancio con clienti e fornitori

		ContabilitaSettings settings = contabilitaSettingsManager.caricaContabilitaSettings();

		Calendar calendarDataInizio = Calendar.getInstance();
		calendarDataInizio.set(settings.getAnnoCompetenza(), 0, 1);

		Calendar calendarDataFine = Calendar.getInstance();
		calendarDataFine.set(settings.getAnnoCompetenza(), 11, 31);

		ParametriRicercaBilancio parametriRicercaBilancio = new ParametriRicercaBilancio();
		parametriRicercaBilancio.setAnnoCompetenza(settings.getAnnoCompetenza());
		parametriRicercaBilancio.getDataRegistrazione().setDataIniziale(null);
		parametriRicercaBilancio.getDataRegistrazione().setDataFinale(calendarDataFine.getTime());
		parametriRicercaBilancio.setStampaClienti(true);
		parametriRicercaBilancio.setStampaFornitori(true);
		Set<StatoAreaContabile> stati = new HashSet<StatoAreaContabile>();
		stati.add(StatoAreaContabile.CONFERMATO);
		stati.add(StatoAreaContabile.VERIFICATO);
		parametriRicercaBilancio.setStatiAreaContabile(stati);

		// Calcolo il bilancio e creo la lista dei risultati.
		SaldoConti saldi = null;
		Map<Integer, RisultatoControlloRateSaldoContabile> risultatoControlloRate = new HashMap<Integer, RisultatoControlloRateSaldoContabile>(
				200);
		try {
			saldi = bilancioManager.caricaBilancio(parametriRicercaBilancio);
		} catch (TipoDocumentoBaseException e) {
			logger.error("-->errore. Tipo documento base Exception nel calcolo del bilancio ", e);
			throw new RuntimeException(e);
		} catch (ContabilitaException e) {
			logger.error("-->errore ", e);
			throw new RuntimeException(e);
		}
		// Aggiungo alla mappa i saldi
		for (SaldoConto saldo : saldi.values()) {
			if (saldo.getSottoTipoConto() == sottotipoConto) {
				RisultatoControlloRateSaldoContabile controllo = new RisultatoControlloRateSaldoContabile(saldo);
				risultatoControlloRate.put(controllo.getCodiceEntita(), controllo);
			}
		}

		TipoPartita tipoPartita = sottotipoConto == SottotipoConto.CLIENTE ? TipoPartita.ATTIVA : TipoPartita.PASSIVA;

		// Calcolo il saldo rate e lo aggiungo (o modifico) alla lista dei risultati.
		List<SituazioneRata> rateAperte = rateManager.ricercaRate(ParametriRicercaRate
				.creaParametriRicercaRateApertePerEntita(tipoPartita));
		// Aggiungo le rate ai risultati
		for (SituazioneRata situazioneRata : rateAperte) {

			boolean rataValida = situazioneRata.getEntita() != null;
			if (rataValida) {
				RisultatoControlloRateSaldoContabile controllo = risultatoControlloRate.get(situazioneRata.getEntita()
						.getCodice());
				if (controllo == null) {
					// non ho registrazioni contabili per l'entità, aggiungo solamente il residuo delle rate
					controllo = new RisultatoControlloRateSaldoContabile(situazioneRata);
					risultatoControlloRate.put(situazioneRata.getEntita().getCodice(), controllo);
				}

				controllo.setTotaleRate(controllo.getTotaleRate().add(
						situazioneRata.getResiduoRata().getImportoInValutaAzienda()));
				risultatoControlloRate.put(controllo.getCodiceEntita(), controllo);
			}
		}

		// Mantengo solamente quelli con il saldo diverso da 0
		List<RisultatoControlloRateSaldoContabile> result = new ArrayList<RisultatoControlloRateSaldoContabile>(
				risultatoControlloRate.size());
		for (RisultatoControlloRateSaldoContabile risultatoControlloRateSaldoContabile : risultatoControlloRate
				.values()) {
			if (risultatoControlloRateSaldoContabile.getDifferenza().compareTo(BigDecimal.ZERO) != 0) {
				result.add(risultatoControlloRateSaldoContabile);
			}
		}
		return result;
	}
}
