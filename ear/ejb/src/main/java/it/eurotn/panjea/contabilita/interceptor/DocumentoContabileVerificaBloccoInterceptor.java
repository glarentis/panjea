/**
 *
 */
package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import org.jboss.annotation.IgnoreDependency;

/**
 * @author fattazzo
 */
public class DocumentoContabileVerificaBloccoInterceptor {

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	@IgnoreDependency
	protected ContabilitaSettingsManager contabilitaSettingsManager;

	@EJB
	@IgnoreDependency
	protected AreaContabileManager areaContabileManager;

	/**
	 * Carica la data registrazione dell'area contabile.
	 * 
	 * @param idAreaContabile
	 *            idAreaContabile
	 * @return dataRegistrazione
	 */
	@SuppressWarnings("unchecked")
	protected Date caricaDataRegistrazioneByAreaContabile(Integer idAreaContabile) {
		Date date = null;
		if (idAreaContabile != null) {
			StringBuilder hql = new StringBuilder(150);
			hql = hql.append("select ac.dataRegistrazione from AreaContabile ac where ac.id=:paramIdAreaContabile");
			Query query = panjeaDAO.prepareQuery(hql.toString());
			query.setParameter("paramIdAreaContabile", idAreaContabile);
			List<Date> dates = query.getResultList();
			if (dates.size() > 0) {
				date = dates.get(0);
			}
		}
		return date;
	}

	/**
	 * Carica la data registrazione dell'area contabile dal documento.
	 * 
	 * @param idDocumento
	 *            idDocumento
	 * @return dataRegistrazione
	 */
	@SuppressWarnings("unchecked")
	protected Date caricaDataRegistrazioneByDocumento(Integer idDocumento) {
		Date date = null;
		if (idDocumento != null) {
			StringBuilder hql = new StringBuilder(150);
			hql = hql
					.append("select ac.dataRegistrazione from AreaContabile ac inner join ac.documento doc where doc.id=:paramIdDocumento");
			Query query = panjeaDAO.prepareQuery(hql.toString());
			query.setParameter("paramIdDocumento", idDocumento);
			List<Date> dates = query.getResultList();
			if (dates.size() > 0) {
				date = dates.get(0);
			}
		}
		return date;
	}

	/**
	 * Carica la lista di date registrazione dalla lista di aree contabili id.
	 * 
	 * @param idAreeContabili
	 *            idAreeContabili
	 * @return List<dataRegistrazione>
	 */
	@SuppressWarnings("unchecked")
	protected List<Date> caricaDateRegistrazioneByAreeContabili(List<Integer> idAreeContabili) {
		List<Date> date = null;
		if (idAreeContabili != null) {
			StringBuilder hql = new StringBuilder(150);
			hql = hql
					.append("select ac.dataRegistrazione from AreaContabile ac where ac.id in (:paramIdAreaContabile)");
			Query query = panjeaDAO.prepareQuery(hql.toString());
			query.setParameter("paramIdAreaContabile", idAreeContabili);
			date = query.getResultList();
		}
		return date;
	}

	/**
	 * @return data blocco contabilita'
	 */
	protected Date getDataBlocco() {
		ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();

		if (contabilitaSettings.getDataBloccoContabilita() == null) {
			return contabilitaSettings.getDataBloccoIva();
		} else if (contabilitaSettings.getDataBloccoIva() == null) {
			return contabilitaSettings.getDataBloccoContabilita();
		} else {
			if (contabilitaSettings.getDataBloccoContabilita().compareTo(contabilitaSettings.getDataBloccoIva()) >= 0) {
				return contabilitaSettings.getDataBloccoContabilita();
			} else {
				return contabilitaSettings.getDataBloccoIva();
			}
		}
	}

	/**
	 * Recupera le date di registrazione delle aree contabili.
	 * 
	 * @param arg
	 *            arg dell'interceptor
	 * @return List<Date>
	 */
	@SuppressWarnings("unchecked")
	protected List<Date> getDateRegistrazioniAreeContabili(Object arg) {
		List<Date> dateRegAreeContabili = new ArrayList<Date>();

		if (arg instanceof AreaContabile) {
			// se l'area contabile non è nuova devo inserire sia quella vecchia sia quella nuova perchè devo controllare
			// che entrambe le date siano valide
			AreaContabile areaContabile = (AreaContabile) arg;
			dateRegAreeContabili.add(areaContabile.getDataRegistrazione());

			if (!areaContabile.isNew()) {
				Date dataReg = caricaDataRegistrazioneByAreaContabile(areaContabile.getId());
				dateRegAreeContabili.add(dataReg);
			}
		} else if (arg instanceof List<?>) {
			List<Date> dateRegAree = caricaDateRegistrazioneByAreeContabili((List<Integer>) arg);
			dateRegAreeContabili.addAll(dateRegAree);
		} else if (arg instanceof Documento) {
			Date dataAreaByDoc = caricaDataRegistrazioneByDocumento(((Documento) arg).getId());
			dateRegAreeContabili.add(dataAreaByDoc);
		} else if (arg instanceof AreaIva) {
			Date dataAreaByDoc = caricaDataRegistrazioneByDocumento(((AreaIva) arg).getDocumento().getId());
			dateRegAreeContabili.add(dataAreaByDoc);
		}

		return dateRegAreeContabili;
	}

	/**
	 * Verifica se è possibile.
	 * 
	 * @param ctx
	 *            il context
	 * @return null
	 * @throws Exception
	 *             Exception
	 */
	@AroundInvoke
	public Object verificaBlocco(InvocationContext ctx) throws Exception {
		Date dataBlocco = getDataBlocco();

		if (dataBlocco != null) {
			Object arg = ctx.getParameters()[0];
			List<Date> dateAreeContabili = getDateRegistrazioniAreeContabili(arg);

			for (Date dataRegistrazioneAreaContabile : dateAreeContabili) {
				if (dataBlocco != null && dataRegistrazioneAreaContabile != null
						&& !dataBlocco.before(dataRegistrazioneAreaContabile)) {
					throw new GenericException(
							"Blocco documenti presente. Impossibile modificare o inserire il\ndocumento con data precedente o uguale a "
									+ new SimpleDateFormat("dd/MM/yyyy").format(dataBlocco));
				}
			}
		}

		return ctx.proceed();
	}
}
