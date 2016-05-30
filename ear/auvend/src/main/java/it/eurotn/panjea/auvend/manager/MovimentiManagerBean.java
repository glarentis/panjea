package it.eurotn.panjea.auvend.manager;

import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager;
import it.eurotn.panjea.auvend.manager.interfaces.MovimentiAuvendManager;
import it.eurotn.panjea.auvend.service.interfaces.AuVendExtDAO;
import it.eurotn.security.JecPrincipal;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.MovimentiAuvendManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MovimentiAuvendManager")
public class MovimentiManagerBean implements MovimentiAuvendManager {
	private static Logger logger = Logger.getLogger(MovimentiManagerBean.class);

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
	 * @param statistica
	 *            mappa da aggiornare.
	 * @return statistiche per gli articoli mancanti.<b>NB:</B>i movimenti li recupero per tutti i depositi quindi la
	 *         chiave della mappa è null
	 */
	private Map<String, StatisticaImportazione> getArticoliMancanti(Date dataInizio, Date dataFine,
			Map<String, StatisticaImportazione> statistica) {
		logger.debug("--> Enter getArticoliMancanti");
		StringBuilder sqlBuilder = new StringBuilder("select * from vw_panjea_movimenti_ArticoliMancanti");
		sqlBuilder.append(" WHERE Data_movimento>='");
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		sqlBuilder.append(df.format(dataInizio));
		sqlBuilder.append("' AND Data_movimento<='");
		sqlBuilder.append(df.format(dataFine));
		sqlBuilder.append("'");

		try {
			// auVendDAO.initConnection();
			ResultSet resultSet = auVendDAO.executeQuery(sqlBuilder.toString());
			while (resultSet.next()) {
				// Recupero la lista di movimenti nella mappa delle statistiche
				StatisticaImportazione statisticaDeposito = statistica.get(null);
				if (statisticaDeposito == null) {
					statisticaDeposito = new StatisticaImportazione();
				}

				List<Articolo> articoliMancanti = statisticaDeposito.getArticoliMancanti();
				if (articoliMancanti == null) {
					articoliMancanti = new ArrayList<Articolo>();
				}

				// Creo l'oggetto documento contenuto nel resultset
				Articolo articolo = new Articolo();
				articolo.setCodice(resultSet.getString(1));
				articoliMancanti.add(articolo);
				statisticaDeposito.setArticoliMancanti(articoliMancanti);
				statistica.put(null, statisticaDeposito);
			}
		} catch (Exception e) {
			logger.error("--> errore nel recuperare il numero di carichi da inserire ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit getArticoliMancanti");
		return statistica;
	}

	@Override
	public boolean importaMovimenti(Date dataInizio, Date dataFine) {
		boolean result = true;
		try {
			List<Object> parametri = new ArrayList<Object>();
			parametri.add(dataInizio);
			parametri.add(dataFine);
			parametri.add(((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda());
			auVendDAO.callProcedure("{ call panjea_movimenti_ImportaMovimenti (?,?,?)}", parametri);
			LetturaFlussoAuVend letturaFlusso = anagraficaAuVendManager.caricaLetturaFlussoMovimenti();
			letturaFlusso.setUltimaLetturaFlussoMovimenti(dataFine);
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
	public Map<String, StatisticaImportazione> verifica(Date dataInizio, Date dataFine) {
		return getArticoliMancanti(dataInizio, dataFine, new HashMap<String, StatisticaImportazione>());
	}

}
