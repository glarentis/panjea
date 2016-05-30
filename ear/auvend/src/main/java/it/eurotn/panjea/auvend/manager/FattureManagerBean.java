package it.eurotn.panjea.auvend.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.Cliente;
import it.eurotn.panjea.auvend.domain.Documento;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;
import it.eurotn.panjea.auvend.exception.TipoDocumentoBaseAuvendNotFoundException;
import it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager;
import it.eurotn.panjea.auvend.manager.interfaces.FattureManager;
import it.eurotn.panjea.auvend.service.interfaces.AuVendExtDAO;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoContabilizzazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.service.exception.SottoContiContabiliAssentiException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;
import it.eurotn.panjea.partite.service.exception.AreaPartitaNonPrevistaException;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.FattureManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FattureManager")
public class FattureManagerBean implements FattureManager {
	protected static Logger logger = Logger.getLogger(FattureManagerBean.class);
	private static final String V_CLIENTIMANCANTI = "select * from vw_panjea_fatture_verificaCodicePagamentoClienti";

	@Resource
	protected SessionContext sessionContext;

	@EJB
	protected TipiAreaContabileManager tipiAreaContabileManager;

	@EJB
	protected AuVendExtDAO auVendDAO;

	@EJB
	protected AreaMagazzinoManager areaMagazzinoManager;

	@EJB
	protected AnagraficaAuVendManager anagraficaAuVendManager;

	@EJB
	protected MagazzinoContabilizzazioneManager contabilizzazioneManager;

	@EJB
	protected SediEntitaManager sediEntitaManager;

	@EJB
	protected SediPagamentoManager sediPagamentoManager;

	@EJB
	protected AreaRateManager areaRateManager;

	@EJB
	protected RateGenerator rateGenerator;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected MagazzinoSettingsManager magazzinoSettingsManager;

	@Override
	@TransactionTimeout(value = 7200)
	public List<Integer> chiudiFatture(String deposito, Date dataFine) {

		LetturaFlussoAuVend lettura = null;
		TipoAreaMagazzino tipoAreaMagazzino = null;
		Date dataInizio;
		try {
			List<TipoDocumentoBaseAuVend> tipiDocumentiBase = anagraficaAuVendManager
					.caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione.RECUPERO_FATTURA);
			for (TipoDocumentoBaseAuVend tipoDocumentoBase : tipiDocumentiBase) {
				if (tipoDocumentoBase.getTipoAreaMagazzino().getDepositoOrigine().getCodice().equals(deposito)) {
					tipoAreaMagazzino = tipoDocumentoBase.getTipoAreaMagazzino();
					// carico l'area contabile per il registro iva
				}
			}

			if (tipoAreaMagazzino == null) {
				throw new TipoDocumentoBaseAuvendNotFoundException(
						"Tipo documento base per rifornimento assente per il recupero delle fatture");
			}

			lettura = anagraficaAuVendManager.caricaLetturaFlussoAuVend(deposito);
			dataInizio = lettura.getUltimaLetturaFlussoFatture();
			lettura.setUltimaLetturaFlussoFatture(dataFine);
			anagraficaAuVendManager.salvaLetturaFlussoAuVend(lettura);
		} catch (AuVendException e1) {
			logger.error("--> errore nel caricare la lettura del flusso ", e1);
			auVendDAO.cleanUp();
			throw new RuntimeException(e1);
		}

		// Trovo le aree magazzino nelle date di importazione
		ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();

		// devo aggiungere 1 gg alla data di inizio per escludere i documenti
		// che rientrano nella data
		// dell'ultima lettura flusso.
		Calendar calDataInizio = Calendar.getInstance();
		calDataInizio.setTime(dataInizio);
		calDataInizio.add(Calendar.DAY_OF_MONTH, 1);

		parametriRicercaAreaMagazzino.getDataDocumento().setDataIniziale(calDataInizio.getTime());
		parametriRicercaAreaMagazzino.getDataDocumento().setDataFinale(dataFine);

		Set<TipoAreaMagazzino> tipiAreaMagazzino = new HashSet<TipoAreaMagazzino>();
		tipiAreaMagazzino.add(tipoAreaMagazzino);
		parametriRicercaAreaMagazzino.setTipiAreaMagazzino(tipiAreaMagazzino);
		Set<TipoGenerazione> tipiGenerazione = new HashSet<TipoGenerazione>();
		tipiGenerazione.add(TipoGenerazione.ESTERNO);
		parametriRicercaAreaMagazzino.setTipiGenerazione(tipiGenerazione);

		parametriRicercaAreaMagazzino.setAnnoCompetenza(magazzinoSettingsManager.caricaMagazzinoSettings()
				.getAnnoCompetenza());
		List<AreaMagazzinoRicerca> aree = areaMagazzinoManager.ricercaAreeMagazzino(parametriRicercaAreaMagazzino);
		logger.debug("--> numero aree trovate " + aree.size());

		List<Integer> idAreeDaContabilizzare = new ArrayList<Integer>();
		for (AreaMagazzinoRicerca areaMagazzinoRicerca : aree) {
			logger.debug("--> chiudo la fattura numero " + areaMagazzinoRicerca.getDocumento().getCodice());
			idAreeDaContabilizzare.add(areaMagazzinoRicerca.getIdAreaMagazzino());
			// Carico subito l'area di magazzino. Nei vari manager viene
			// ricaricata ma
			// essendo già in session non mi lancia la query
			AreaMagazzino areaMagazzino = new AreaMagazzino();
			areaMagazzino.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
			areaMagazzino = areaMagazzinoManager.caricaAreaMagazzino(areaMagazzino);

			areaMagazzino.getDatiGenerazione().setDataCreazione(Calendar.getInstance().getTime());
			areaMagazzino.getDatiGenerazione().setDataGenerazione(Calendar.getInstance().getTime());
			areaMagazzino.getDatiGenerazione().setTipoGenerazione(TipoGenerazione.ESTERNO);
			areaMagazzino.getDatiGenerazione().setUtente(getPrincipal().getUserName());

			// Aggiorno la SedeAnagrafica con la sedePrincipale del cliente
			SedeEntita sedeEntita = null;
			try {
				logger.debug("--> aggiorno la sedeEntita per il cliente "
						+ areaMagazzinoRicerca.getEntitaDocumento().getId());
				sedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(areaMagazzinoRicerca.getEntitaDocumento()
						.getId());
				areaMagazzino.getDocumento().setSedeEntita(sedeEntita);
				it.eurotn.panjea.anagrafica.documenti.domain.Documento documento = panjeaDAO
						.saveWithoutFlush(areaMagazzino.getDocumento());
				areaMagazzino.setDocumento(documento);
				panjeaDAO.saveWithoutFlush(areaMagazzino);

			} catch (Exception e) {
				logger.error("--> errore nel caricare la sede dell'entita'", e);
				throw new RuntimeException(e);
			}

			// Creo l'area partite per il documento (che associa la condizione
			// di pagamento standard della sede
			// principale del cliente)
			// Se l'area partite e' gia' presente non viene creata ma ritorna
			// l'area partite presente
			AreaRate areaRate = null;
			try {
				it.eurotn.panjea.anagrafica.documenti.domain.Documento doc = areaMagazzinoRicerca.getDocumento();
				// l'areaRate viene salvata e se e' senza version viene lanciata
				// una TransientObjectException
				// imposto di default la version a 0
				doc.setVersion(0);
				logger.debug("--> Creo l'area partite");
				areaRate = creaAreaRatePerDocumento(doc, areaMagazzinoRicerca.getEntitaDocumento().getId());
				if (areaRate.isNew()) {
					areaRate = areaRateManager.salvaAreaRate(areaRate);
					// Se l'area partite e' nuova devo creare le rate
					areaRate = rateGenerator.generaRate(areaMagazzino, areaRate);
					areaRateManager.validaAreaRate(areaRate, areaMagazzino);
				}
			} catch (AreaPartitaNonPrevistaException e) {
				logger.error("--> l'area partite per il documento non è prevista ", e);
				auVendDAO.cleanUp();
				throw new RuntimeException(e);
			}

			// confermo l'area magazzino.
			// avendo l'area contabile se l'iva calcolata non e' uguale all'iva
			// inserita
			// mi mette l'area magazzino in stato forzato
			try {
				logger.debug("--> Valido le righe magazzino");
				areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, areaRate, false, true);
			} catch (TotaleDocumentoNonCoerenteException e) {
				// Non gestito. Avendo passato a true il forza stato non viene
				// rilanciata questa eccezione.
				// La rilancio cmq per sicurezza
				auVendDAO.cleanUp();
				throw new RuntimeException(e);
			}
		}
		auVendDAO.cleanUp();
		return idAreeDaContabilizzare;
	}

	/**
	 * Crea l'area partita per il documento.
	 * 
	 * @param documento
	 *            documento
	 * @param idEntita
	 *            id dell'entità del documento
	 * @return areapartita creata
	 * @throws AreaPartitaNonPrevistaException
	 *             rilanciata se per il tipo documento non è prevista un area rata
	 */
	public AreaRate creaAreaRatePerDocumento(it.eurotn.panjea.anagrafica.documenti.domain.Documento documento,
			Integer idEntita) throws AreaPartitaNonPrevistaException {
		logger.debug("--> Enter creaAreaPartitePerDocumento");
		// ricerca di area partite
		AreaRate areaRate = areaRateManager.caricaAreaRate(documento);
		if (areaRate.isNew()) {
			logger.debug("--> area partita non trovata : inizializzazione dei valori");
			areaRate.setDocumento(documento);
			// il tipoAreaPartita viene settato in automatico sulla save
			// dell'area

			// creo una entita' con l'id, mi basta quello per caricare la sede
			// principale
			FornitoreLite fornitoreLite = new FornitoreLite();
			fornitoreLite.setId(idEntita);
			// valorizzazione condizione pagamento
			SedePagamento sedePagamento = sediPagamentoManager.caricaSedePagamentoPrincipaleEntita(fornitoreLite);
			if (logger.isDebugEnabled()) {
				logger.debug("--> entita " + documento.getEntita().getCodice() + " trovata sede pagamento  "
						+ sedePagamento);
				logger.debug("--> trovato codice pagamento " + sedePagamento.getCodicePagamento());
			}
			areaRate.setCodicePagamento(sedePagamento.getCodicePagamento());
		}
		logger.debug("--> Exit creaAreaPartitePerDocumento");
		return areaRate;
	}

	/**
	 * 
	 * @param depositi
	 *            depositi da filtrare
	 * @return stringa sql per filtrare i depositi
	 */
	private String creaWhereSqlDepositi(List<String> depositi) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("Caricatore in ( ");
		for (int i = 0; i < depositi.size(); i++) {
			sqlBuilder.append("'");
			sqlBuilder.append(depositi.get(i));
			sqlBuilder.append("'");
			if (i < depositi.size() - 1) {
				sqlBuilder.append(",");
			}
		}
		sqlBuilder.append(")");
		return sqlBuilder.toString();
	}

	/**
	 * 
	 * @param depositi
	 *            deposito da analizzare
	 * @param dataFine
	 *            data finale di importazione
	 * @param statistica
	 *            statistica con i dati delle precedenti verifiche
	 * @return articoli mancanti per l'importazione
	 */
	private Map<String, StatisticaImportazione> getArticoliMancanti(List<String> depositi, Date dataFine,
			Map<String, StatisticaImportazione> statistica) {
		logger.debug("--> Enter getArticoliMancanti");
		StringBuilder sqlBuilder = new StringBuilder();

		// Imposto la data di fine in formato stringa per la query
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
		String dataFineSql = dateFormatter.format(dataFine);

		sqlBuilder.append("SELECT 	TOP 100 PERCENT ip.Prodotto, ir.Caricatore, dbo.Prodotti.Descrizione");
		sqlBuilder.append(" FROM OPENQUERY(PANJEA, 'select a.id,a.codice from maga_articoli a') articoli");
		sqlBuilder.append(" RIGHT OUTER JOIN dbo.InterventiRifornProdotti ip");
		sqlBuilder.append(" INNER JOIN dbo.InterventiRifornimento ir ON ip.Progressivo = ir.Progressivo");
		sqlBuilder.append(" INNER JOIN OPENQUERY	(  PANJEA,");
		sqlBuilder
				.append(" 'select dep.codice  from anag_depositi dep inner join avend_letturaflusso letture on letture.deposito_id=dep.id where ultimaLetturaFlussoFatture is not null'	)");
		sqlBuilder.append(" depositi ON ir.Caricatore = depositi.codice");
		sqlBuilder.append(" INNER JOIN dbo.Prodotti ON ip.Prodotto = dbo.Prodotti.Prodotto");
		sqlBuilder.append(" INNER JOIN dbo.Installazioni inst ON ir.Installazione = inst.Installazione");
		sqlBuilder.append(" INNER JOIN dbo.Fatture");
		sqlBuilder
				.append(" INNER JOIN dbo.FattureRighe ON dbo.Fatture.Numero_fattura = dbo.FattureRighe.Numero_fattura");
		sqlBuilder.append(" AND dbo.Fatture.Data_fattura = dbo.FattureRighe.Data_fattura");
		sqlBuilder
				.append("	AND dbo.Fatture.Terminale = dbo.FattureRighe.Terminale ON ip.Prodotto = dbo.FattureRighe.Prodotto");
		sqlBuilder
				.append(" AND ir.Numero_fattura = dbo.Fatture.Numero_fattura AND ir.Data_fattura = dbo.Fatture.Data_fattura");
		sqlBuilder
				.append(" AND inst.Cliente = dbo.Fatture.Cliente ON articoli.codice COLLATE Latin1_General_CI_AS = dbo.Prodotti.Prodotto");
		sqlBuilder.append(" WHERE (articoli.id IS NULL) AND ir.");
		sqlBuilder.append(creaWhereSqlDepositi(depositi)).append(" and ir.Data_Intervento<'").append(dataFineSql)
				.append("'");
		sqlBuilder
				.append(" GROUP BY ip.Prodotto,articoli.id,ir.Caricatore,dbo.Prodotti.Descrizione,dbo.Prodotti.Categoria,dbo.Prodotti.Unita_misura ORDER BY ir.Caricatore");
		logger.debug("--> Query da eseguire " + sqlBuilder.toString());
		ResultSet resultSet = null;
		try {
			resultSet = auVendDAO.executeQuery(sqlBuilder.toString());
			while (resultSet.next()) {
				String deposito = resultSet.getString(1);
				// Recupero la lista di movimenti nella mappa delle statistiche
				StatisticaImportazione statisticaDeposito = statistica.get(deposito);
				if (statisticaDeposito == null) {
					statisticaDeposito = new StatisticaImportazione();
					statisticaDeposito.setDeposito(deposito);
				}

				List<Articolo> articoliMancanti = statisticaDeposito.getArticoliMancanti();
				if (articoliMancanti == null) {
					articoliMancanti = new ArrayList<Articolo>();
				}

				// Creo l'oggetto documento contenuto nel resultset
				Articolo articolo = new Articolo();
				articolo.setDescrizione(resultSet.getString(1));
				articolo.setCodice(resultSet.getString(2));
				articoliMancanti.add(articolo);
				statisticaDeposito.setArticoliMancanti(articoliMancanti);

				statistica.put(deposito, statisticaDeposito);
			}
		} catch (Exception e) {
			logger.error("--> errore nel recuperare il numero di carichi da inserire ", e);
			throw new RuntimeException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error("--> errore nel chiudere il recordset per getArticoloMancanti", e);
				}
			}
		}
		logger.debug("--> Exit getArticoliMancanti");
		return statistica;
	}

	/**
	 * 
	 * @param depositi
	 *            deposito da analizzare
	 * @param statistica
	 *            statistica con i dati delle precedenti verifiche
	 * @return clienti mancanti per l'importazione
	 */
	private Map<String, StatisticaImportazione> getClientiMancanti(List<String> depositi,
			Map<String, StatisticaImportazione> statistica) {
		StringBuilder sqlBuilder = new StringBuilder(V_CLIENTIMANCANTI);
		sqlBuilder.append(" WHERE ");
		sqlBuilder.append(creaWhereSqlDepositi(depositi));

		logger.debug("--> Query da eseguire " + sqlBuilder.toString());
		ResultSet resultSet = null;
		try {
			resultSet = auVendDAO.executeQuery(sqlBuilder.toString());
			while (resultSet.next()) {
				String deposito = resultSet.getString(5);
				StatisticaImportazione statisticaDeposito = statistica.get(deposito);
				if (statisticaDeposito == null) {
					statisticaDeposito = new StatisticaImportazione();
					statisticaDeposito.setDeposito(deposito);
				}

				List<Cliente> clientiDaVerificare = statisticaDeposito.getClientiDaVerificare();
				if (clientiDaVerificare == null) {
					clientiDaVerificare = new ArrayList<Cliente>();
				}

				// Creo l'oggetto documento contenuto nel resultset
				String codiceAuvend = resultSet.getString(1);
				String ragioneSociale = resultSet.getString(2);
				Integer codicePanjea = resultSet.getInt(3);
				Integer idPanjea = resultSet.getInt(4);
				// la getInt mi ritorna 0 se è null. Non potendo avere un
				// cliente con codice 0
				// setto a null i campi, indicando così che il cliente non
				// esiste

				Cliente cliente = new Cliente(codiceAuvend, codicePanjea.toString(), ragioneSociale, idPanjea);
				clientiDaVerificare.add(cliente);
				statisticaDeposito.setClientiDaVerificare(clientiDaVerificare);

				statistica.put(deposito, statisticaDeposito);
			}
			return statistica;
		} catch (Exception e) {
			logger.error("--> errore nel recuperare il numero di carichi da aggiornare ", e);
			throw new RuntimeException(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error("--> errore nel chiudere il recordset per getClientiMancanti", e);
				}
			}
		}

	}

	/**
	 * 
	 * @param depositi
	 *            deposito da analizzare
	 * @param dataFine
	 *            data finale di importazione
	 * @param statistica
	 *            statistica con i dati delle precedenti verifiche
	 * @return documenti mancanti per l'importazione
	 */
	private Map<String, StatisticaImportazione> getDocumenti(List<String> depositi, Date dataFine,
			Map<String, StatisticaImportazione> statistica) {
		List<TipoDocumentoBaseAuVend> tipiDocumentiBase = null;
		TipoDocumento tipoDocumento = null;

		ResultSet resultSet = null;
		for (String deposito : depositi) {
			try {
				List<Object> parametri = new ArrayList<Object>();
				tipiDocumentiBase = anagraficaAuVendManager
						.caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione.RECUPERO_FATTURA);
				for (TipoDocumentoBaseAuVend tipoDocumentoBase : tipiDocumentiBase) {
					if (tipoDocumentoBase.getTipoAreaMagazzino().getDepositoOrigine().getCodice().equals(deposito)) {
						tipoDocumento = tipoDocumentoBase.getTipoAreaMagazzino().getTipoDocumento();
					}
				}
				if (tipoDocumento == null) {
					throw new TipoDocumentoBaseAuvendNotFoundException(
							"Tipo documento base per rifornimento assente per il recupero delle fatture");
				}

				LetturaFlussoAuVend lettura = anagraficaAuVendManager.caricaLetturaFlussoAuVend(deposito);
				Date dataInizio = lettura.getUltimaLetturaFlussoFatture();

				parametri.add(dataInizio);
				parametri.add(dataFine);
				parametri.add(tipoDocumento.getId());
				parametri.add(deposito);
				resultSet = auVendDAO.callQuery("{ call panjea_fatture_VerificaFatture (?,?,?,?)}", parametri);
				while (resultSet.next()) {
					StatisticaImportazione statisticaDeposito = statistica.get(deposito);
					if (statisticaDeposito == null) {
						statisticaDeposito = new StatisticaImportazione();
						statisticaDeposito.setDeposito(deposito);
					}

					Integer numeroFattura = resultSet.getInt(1);
					Date dataFattura = resultSet.getDate(2);
					Integer idDocumento = resultSet.getInt(7);

					// se nel campo codice(=numero documento panjea) ho un null
					// il movimento è da inserire
					List<Documento> documenti = null;
					if (idDocumento != null) {
						documenti = statisticaDeposito.getDocumentiDaAggiornare();
					} else {
						documenti = statisticaDeposito.getDocumentiDaInserire();
					}

					if (documenti == null) {
						documenti = new ArrayList<Documento>();
					}
					Documento documento = new Documento();
					documento.setData(dataFattura);
					documento.setId(idDocumento);
					documento.setNumero(numeroFattura);
					documenti.add(documento);
					if (idDocumento != null) {
						statisticaDeposito.setDocumentiDaAggiornare(documenti);
					} else {
						statisticaDeposito.setDocumentiDaInserire(documenti);
					}
					statistica.put(deposito, statisticaDeposito);
				}
			} catch (Exception e) {
				logger.error("--> errore nel verificare i documenti per le fatture", e);
				throw new RuntimeException(e);
			} finally {
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						logger.error("--> errore nel chiudere il recordset per getDocumenti", e);
					}
				}
			}
		}
		return statistica;
	}

	/**
	 * 
	 * @return jecprincipal loggato
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	@Override
	@TransactionTimeout(value = 1800)
	public boolean importaFatture(String deposito, Date dataFine) throws SottoContiContabiliAssentiException {
		List<Object> parametri = new ArrayList<Object>();
		String azienda = getPrincipal().getCodiceAzienda();
		List<TipoDocumentoBaseAuVend> tipiDocumentiBase = null;
		TipoDocumento tipoDocumento = null;
		TipoAreaMagazzino tipoAreaMagazzino = null;
		TipoAreaContabile tipoAreaContabile = null;

		LetturaFlussoAuVend lettura;
		try {
			tipiDocumentiBase = anagraficaAuVendManager
					.caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione.RECUPERO_FATTURA);
			for (TipoDocumentoBaseAuVend tipoDocumentoBase : tipiDocumentiBase) {
				if (tipoDocumentoBase.getTipoAreaMagazzino().getDepositoOrigine().getCodice().equals(deposito)) {
					tipoDocumento = tipoDocumentoBase.getTipoAreaMagazzino().getTipoDocumento();
					tipoAreaMagazzino = tipoDocumentoBase.getTipoAreaMagazzino();
					// carico l'area contabile per il registro iva
					if (tipoDocumento != null) {
						try {
							tipoAreaContabile = tipiAreaContabileManager
									.caricaTipoAreaContabilePerTipoDocumento(tipoDocumento.getId());
						} catch (ContabilitaException e) {
							throw new RuntimeException("Area Contabile non definita per il tipo documento "
									+ tipoDocumento.getCodice());
						}
						// se non ho un tipoAreaContabile legata al
						// tipoDocumento non posso importare le fatture
						if (tipoAreaContabile.isNew()) {
							throw new RuntimeException("Area Contabile non definita per il tipo documento "
									+ tipoDocumento.getCodice());
						}
					}
				}
			}
			if (tipoDocumento == null) {
				throw new TipoDocumentoBaseAuvendNotFoundException(
						"Tipo documento base per rifornimento assente per il recupero delle fatture");
			}

			lettura = anagraficaAuVendManager.caricaLetturaFlussoAuVend(deposito);
			Date dataInizio = lettura.getUltimaLetturaFlussoFatture();

			parametri.add(dataInizio);
			parametri.add(dataFine);
			parametri.add(tipoAreaMagazzino.getId());
			parametri.add(tipoDocumento.getId());
			parametri.add(tipoAreaContabile.getRegistroIva().getId());
			parametri.add(deposito);
			parametri.add(azienda);
			auVendDAO.callProcedure("{ call panjea_fatture_ImportaFatture (?,?,?,?,?,?,?)}", parametri);
			return true;
		} catch (AuVendException e) {
			logger.error("--> errore nell'importare le fatture", e);
			return false;
		} finally {
			auVendDAO.cleanUp();
		}
	}

	@Override
	public Map<String, StatisticaImportazione> verificaFatture(List<String> depositi, Date dataFine) {
		Map<String, StatisticaImportazione> statistica = null;
		try {
			statistica = new HashMap<String, StatisticaImportazione>();
			statistica = getClientiMancanti(depositi, statistica);
			statistica = getDocumenti(depositi, dataFine, statistica);
			statistica = getArticoliMancanti(depositi, dataFine, statistica);
		} catch (Exception e) {
			logger.error("--> errore nel verificare le fatture ", e);
			throw new RuntimeException(e);
		} finally {
			auVendDAO.cleanUp();
		}
		return statistica;
	}

}
