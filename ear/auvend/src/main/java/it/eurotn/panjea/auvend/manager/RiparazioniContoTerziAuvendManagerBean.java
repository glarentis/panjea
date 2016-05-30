package it.eurotn.panjea.auvend.manager;

import it.eurotn.panjea.auvend.domain.Cliente;
import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;
import it.eurotn.panjea.auvend.exception.TipoDocumentoBaseAuvendNotFoundException;
import it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager;
import it.eurotn.panjea.auvend.manager.interfaces.RiparazioniContoTerziAuvendManager;
import it.eurotn.panjea.auvend.service.interfaces.AuVendExtDAO;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.security.JecPrincipal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RiparazioniContoTerziAuvendManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RiparazioniContoTerziAuvendManager")
public class RiparazioniContoTerziAuvendManagerBean implements
		RiparazioniContoTerziAuvendManager {

	private static Logger logger = Logger.getLogger(MovimentiManagerBean.class);
	
	@Resource
	private SessionContext sessionContext;

	@EJB
	private AuVendExtDAO auVendDAO;

	@EJB
	private AnagraficaAuVendManager anagraficaAuVendManager;
	
	@EJB
	protected AreaMagazzinoManager areaMagazzinoManager;
	
	@EJB
	protected MagazzinoSettingsManager magazzinoSettingsManager;
	
	@Override
	public List<Articolo> getArticoliMancanti(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter getArticoliMancanti");

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String query = String
				.format("select distinct codice, descrizione from vw_panjea_ripterzi_ArticoliMancanti where Data_documento between '%s' and '%s'",
						df.format(dataInizio), df.format(dataFine));
		
		List<Articolo> articoliMancanti = new ArrayList<Articolo>(); 

		try {
			// auVendDAO.initConnection();
			ResultSet resultSet = auVendDAO.executeQuery(query);
			while (resultSet.next()) {
				// Creo l'oggetto documento contenuto nel resultset
				Articolo articolo = new Articolo();
				articolo.setCodice(resultSet.getString(1));
				articolo.setDescrizione(resultSet.getString(2));
				articoliMancanti.add(articolo);
			}
		} catch (Exception e) {
			logger.error("--> errore nel recuperare gli articoli mancanti ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit getArticoliMancanti");
		return articoliMancanti;
	}

	@Override
	public List<Cliente> getClientiMancanti(Date dataInizio, Date dataFine) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String query = String
				.format("select distinct cliente, ragione_sociale from vw_panjea_ripterzi_ClientiMancanti where Data_documento between '%s' and '%s'",
						df.format(dataInizio), df.format(dataFine));

		logger.debug("--> Enter getClientiMancanti");
		ResultSet resultSet = null;
		List<Cliente> clientiMancanti = new ArrayList<Cliente>();
		try {
			resultSet = auVendDAO.executeQuery(query);
			while (resultSet.next()) {
				String codiceAuvend = resultSet.getString(1);
				String ragioneSociale = resultSet.getString(2);
				Cliente cliente = new Cliente(codiceAuvend, "0", ragioneSociale, null);
				clientiMancanti.add(cliente);
			}
		} catch (Exception e) {
			logger.error("--> errore nel recuperare il numero di clienti mancanti ", e);
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
		return clientiMancanti;
	}
	
	@Override
	public boolean importaMovimenti(Date dataInizio, Date dataFine) {
		boolean result = true;
		try {
			List<Object> parametri = new ArrayList<Object>();
			parametri.add(dataInizio);
			parametri.add(dataFine);
			parametri.add(((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda());
			
			auVendDAO.callProcedure("{ call panjea_ripterzi_ImportaMovimenti (?,?,?)}", parametri);
			LetturaFlussoAuVend letturaFlusso = anagraficaAuVendManager.caricaLetturaFlussoRiparazioneContoTerzi();
			
			ricalcolaPrezzi(dataInizio, dataFine);
			
			letturaFlusso.setUltimaLetturaFlussoRiparazioneContoTerzi(dataFine);
			anagraficaAuVendManager.salvaLetturaFlussoAuVend(letturaFlusso);
			
		} catch (Exception e) {
			logger.error("-->errore nell 'importare i movimenti con datafine " + dataFine, e);
			result = false;
		} finally {
			auVendDAO.cleanUp();
		}
		return result;
	}

	private void ricalcolaPrezzi(Date dataInizio, Date dataFine) {
		TipoAreaMagazzino tipoAreaMagazzino = null;
		
		try {
			List<TipoDocumentoBaseAuVend> tipiDocumentiBase = anagraficaAuVendManager
					.caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione.RECUPERO_RIPARAZIONI_CONTO_TERZI);
			
			tipoAreaMagazzino = tipiDocumentiBase.get(0).getTipoAreaMagazzino();
			
			if (tipoAreaMagazzino == null) {
				throw new TipoDocumentoBaseAuvendNotFoundException(
						"Tipo documento base per recupero riparazioni conto terzi mancante");
			}
		} catch (AuVendException e1) {
			logger.error("--> errore nel caricare la lettura del flusso ", e1);
			auVendDAO.cleanUp();
			throw new RuntimeException(e1);
		}

		// Trovo le aree magazzino nelle date di importazione
		ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();
		parametriRicercaAreaMagazzino.getDataDocumento().setDataIniziale(dataInizio);
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
		
		for (AreaMagazzinoRicerca areaMagazzinoRicerca : aree) {
			AreaMagazzino areaMagazzino = areaMagazzinoManager.ricalcolaPrezziMagazzino(areaMagazzinoRicerca.getIdAreaMagazzino());
			try
			{
				// Il tipo di documento per questa operazione NON DEVE avere né area rate né area contabile
				areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, null, false, false);
			} catch (Exception e) {
				logger.error("--> errore durante validazione area magazzino.", e);
				throw new RuntimeException("errore durante validazione area magazzino.", e);
			}
			
			try {
				areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, false);
			} catch (Exception e) {
				logger.error("--> errore durante il salvataggio dell'area magazzino", e);
				throw new RuntimeException("errore durante il salvataggio dell'area magazzino", e);
			}
		}
	}
	
	@Override
	public StatisticaImportazione verifica(Date dataInizio, Date dataFine) {
		StatisticaImportazione statistica = new StatisticaImportazione();
		List<Articolo> articoliMancanti = getArticoliMancanti(dataInizio, dataFine);
		List<Cliente> clientiDaVerificare = getClientiMancanti(dataInizio, dataFine);
		statistica.setArticoliMancanti(articoliMancanti);
		statistica.setClientiDaVerificare(clientiDaVerificare);
		return statistica;
	}

}
