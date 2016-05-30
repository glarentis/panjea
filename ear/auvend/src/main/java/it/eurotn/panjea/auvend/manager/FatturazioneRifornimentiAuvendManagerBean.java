package it.eurotn.panjea.auvend.manager;

import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.Cliente;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager;
import it.eurotn.panjea.auvend.manager.interfaces.FatturazioneRifornimentiAuvendManager;
import it.eurotn.panjea.auvend.service.interfaces.AuVendExtDAO;
import it.eurotn.security.JecPrincipal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.FatturazioneRifornimentiAuvendManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FatturazioneRifornimentiAuvendManager")
public class FatturazioneRifornimentiAuvendManagerBean implements FatturazioneRifornimentiAuvendManager {
	private static Logger logger = Logger.getLogger(FatturazioneRifornimentiAuvendManager.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private AuVendExtDAO auVendDAO;

	@EJB
	private AnagraficaAuVendManager anagraficaAuVendManager;

	/**
	 * 
	 * @param dataInizio
	 *            inizio statistica
	 * @param dataFine
	 *            fine statistica
	 * @return statistiche per gli articoli mancanti.
	 */
	private List<Articolo> getArticoliMancanti(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter getArticoliMancanti");
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String query = String
				.format("select distinct prodotto, descrizione from vw_panjea_fatturazioneRifornimenti_ArticoliMancanti where Data_intervento between '%s' and '%s'",
						df.format(dataInizio), df.format(dataFine));

		List<Articolo> articoliMancanti = new ArrayList<Articolo>();
		ResultSet resultSet = null;
		try {
			// auVendDAO.initConnection();

			resultSet = auVendDAO.executeQuery(query);
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
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error("--> errore nel chiudere il recordset per getClientiMancanti", e);
				}
			}
		}
		logger.debug("--> Exit getArticoliMancanti");
		return articoliMancanti;
	}

	/**
	 * 
	 * @param dataInizio
	 *            inizio statistica
	 * @param dataFine
	 *            fine statistica
	 * @return lista dei clienti mancanti in PanJea per cui esistono dei rifornimenti da fatturare
	 */
	private List<Cliente> getClientiMancanti(Date dataInizio, Date dataFine) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String query = String
				.format("select * from vw_panjea_fatturazioneRifornimenti_ClientiMancanti where Data_intervento between '%s' and '%s'",
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
			// parametri.add(deposito);
			auVendDAO.callProcedure("{ call panjea_fatturazioneRifornimenti_ImportaMovimenti (?,?,?)}", parametri);
			LetturaFlussoAuVend letturaFlusso = anagraficaAuVendManager.caricaLetturaFlussoFattuazioneRifornimenti();
			letturaFlusso.setUltimaLetturaFlussoFatturazioneRifornimenti(dataFine);
			anagraficaAuVendManager.salvaLetturaFlussoAuVend(letturaFlusso);
		} catch (Exception e) {
			logger.error("-->errore nell 'importare i movimenti con datafine " + dataFine, e);
			result = false;
		} finally {
			auVendDAO.cleanUp();
		}
		return result;
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
