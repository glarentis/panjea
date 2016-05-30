package it.eurotn.panjea.auvend.manager;

import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.manager.interfaces.AnagraficaAuVendManager;
import it.eurotn.panjea.auvend.manager.interfaces.CarichiManager;
import it.eurotn.panjea.auvend.service.interfaces.AuVendExtDAO;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.security.JecPrincipal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.CarichiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CarichiManager")
/*
 * Gestisce l'iportazione dei carichi.<br/> <b>NB:</b>AuvendDao mi restituisce dei resultset. Questo lega il manager
 * allo strato DAO. <br/> Da rimuovere e astrarre il dao.
 */
public class CarichiManagerBean implements CarichiManager {
	private static Logger logger = Logger.getLogger(CarichiManagerBean.class);

	@EJB
	private AuVendExtDAO auVendDAO;

	@Resource
	private SessionContext sessionContext;

	@EJB
	private AnagraficaAuVendManager anagraficaAuVendManager;

	@EJB
	private AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	/**
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return lista di articoli mancanti in panjea per poter eseguire le importazioni di carichi e rifornimenti
	 *         (pozzetti compresi)
	 */
	private List<Articolo> getArticoliMancanti(Date dataInizio, Date dataFine) {
		List<Articolo> articoliMancanti = new LinkedList<Articolo>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("''yyyyMMdd''");
		String dataFineSql = dateFormatter.format(dataFine);
		String dataInizioSql = dateFormatter.format(dataInizio);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("SELECT dbo.Prodotti.Descrizione, dbo.Prodotti.Prodotto\n")
				.append(" FROM dbo.InterventiRifornProdotti ip\n")
				.append(" INNER JOIN dbo.InterventiRifornimento ir ON ip.Progressivo = ir.Progressivo\n")
				.append(" INNER JOIN dbo.Prodotti ON ip.Prodotto = dbo.Prodotti.Prodotto\n")
				.append(" LEFT OUTER JOIN OPENQUERY(PANJEA, 'select a.id,a.codice from maga_articoli a') articoli ON\n")
				.append(" dbo.Prodotti.Prodotto COLLATE Latin1_General_CI_AS = articoli.codice COLLATE Latin1_General_CI_AS\n")
				.append(" WHERE articoli.id IS NULL and ir.Data_intervento between ")
				.append(dataInizioSql)
				.append(" and ")
				.append(dataFineSql)
				.append("\nUNION\n")
				.append("SELECT dbo.Prodotti.Descrizione, dbo.Prodotti.Prodotto\n")
				.append(" FROM dbo.BolleUscitaRighe br\n")
				.append(" INNER JOIN dbo.BolleUscitaTestata bt ON bt.Progressivo = br.Progressivo\n")
				.append(" INNER JOIN dbo.Prodotti ON br.Prodotto = dbo.Prodotti.Prodotto\n")
				.append(" LEFT OUTER JOIN OPENQUERY(PANJEA, 'select a.id,a.codice from maga_articoli a') articoli ON\n")
				.append(" dbo.Prodotti.Prodotto COLLATE Latin1_General_CI_AS = articoli.codice COLLATE Latin1_General_CI_AS\n")
				.append(" WHERE articoli.id IS NULL and bt.data_bolla between ").append(dataInizioSql).append(" and ")
				.append(dataFineSql).append("\nGROUP BY dbo.Prodotti.Prodotto, dbo.Prodotti.Descrizione");

		ResultSet resultSet = null;
		try {
			resultSet = auVendDAO.executeQuery(sqlBuilder.toString());

			while (resultSet.next()) {
				// Creo l'oggetto documento contenuto nel resultset
				Articolo articolo = new Articolo();
				articolo.setDescrizione(resultSet.getString(1));
				articolo.setCodice(resultSet.getString(2));
				articoliMancanti.add(articolo);
			}
		} catch (SQLException e) {
			logger.error("--> errore nel recuperare gli articoli mancanti", e);
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
		return articoliMancanti;
	}

	/**
	 * @return principal loggato
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	@Override
	@TransactionTimeout(value = 3600)
	public boolean importaCarichiERifornimenti(Date dataInizio, Date dataFine) {

		List<Object> parametri = new ArrayList<Object>();
		String azienda = getPrincipal().getCodiceAzienda();
		parametri.add(dataInizio);
		parametri.add(dataFine);
		parametri.add(azienda);

		try {
			auVendDAO.callProcedure("{ call panjea_carichi_ImportaTutto (?,?,?)}", parametri);
		} catch (Exception e) {
			logger.error("--> errore nell'eseguire panjea_carichi_ImportaTutto", e);
			throw new RuntimeException(e);
		}

		String deposito = "";
		try {
			List<LetturaFlussoAuVend> letture = anagraficaAuVendManager.caricaLettureFlussoAuVend();
			for (LetturaFlussoAuVend lettura : letture) {
				deposito = lettura.getDeposito().getCodice();
				lettura.setUltimaLetturaFlussoCarichi(dataFine);
				anagraficaAuVendManager.salvaLetturaFlussoAuVend(lettura);
			}
		} catch (Exception e) {
			logger.error("--> errore nell'aggiornare ultima data lettura deposito " + deposito, e);
			throw new RuntimeException(e);
		} finally {
			auVendDAO.cleanUp();
		}

		return true;
	}

	@Override
	public StatisticaImportazione verificaCarichi(Date dataInizio, Date dataFine) {
		StatisticaImportazione statistica = new StatisticaImportazione();
		List<Articolo> articoliMancanti = getArticoliMancanti(dataInizio, dataFine);
		statistica.setArticoliMancanti(articoliMancanti);
		return statistica;
	}
}
