/**
 *
 */
package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.RigaContabile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.proxy.HibernateProxy;

/**
 * @author fattazzo
 */
public class RigheContabiliVerificaBloccoInterceptor extends DocumentoContabileVerificaBloccoInterceptor {

	/**
	 * Carica la data registrazione dell'area contabile partendo dalla riga contabile.
	 * 
	 * @param idRigaContabile
	 *            idRigaContabile
	 * @return data registrazione
	 */
	@SuppressWarnings("unchecked")
	private Date caricaDataRegistrazioneByRiga(Integer idRigaContabile) {
		Date date = null;
		if (idRigaContabile != null) {
			StringBuilder hql = new StringBuilder(150);
			hql = hql
					.append("select ac.dataRegistrazione from RigaContabile rc join rc.areaContabile ac where rc.id=:paramIdRigaContabile");
			Query query = panjeaDAO.prepareQuery(hql.toString());
			query.setParameter("paramIdRigaContabile", idRigaContabile);
			List<Date> dates = query.getResultList();
			if (dates.size() > 0) {
				date = dates.get(0);
			}
		}
		return date;
	}

	@Override
	protected Date getDataBlocco() {
		ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();
		return contabilitaSettings.getDataBloccoContabilita();
	}

	/**
	 * Restituisce la dataRegistrazione dell'area contabile dalla riga contabile.
	 * 
	 * @param rigaContabile
	 *            rigaContabile
	 * @return Date
	 */
	private Date getDataRegistrazioneRiga(RigaContabile rigaContabile) {
		if (rigaContabile.getId() != null && rigaContabile.getAreaContabile() instanceof HibernateProxy) {
			Date dataRegArea = caricaDataRegistrazioneByRiga(rigaContabile.getId());
			return dataRegArea;
		} else {
			return rigaContabile.getAreaContabile().getDataRegistrazione();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Date> getDateRegistrazioniAreeContabili(Object arg) {
		List<Date> areeContabili = new ArrayList<Date>();

		if (arg instanceof RigaContabile) {
			RigaContabile rigaContabile = (RigaContabile) arg;
			Date dataRegistrazioneRiga = getDataRegistrazioneRiga(rigaContabile);
			areeContabili.add(dataRegistrazioneRiga);
		} else if (arg instanceof List<?>) {
			for (RigaContabile rigaContabile : ((List<RigaContabile>) arg)) {
				Date dataRegistrazioneRiga = getDataRegistrazioneRiga(rigaContabile);
				areeContabili.add(dataRegistrazioneRiga);
			}
		} else {
			List<Date> dateRegistrazioniDocumenti = super.getDateRegistrazioniAreeContabili(arg);
			areeContabili.addAll(dateRegistrazioniDocumenti);
		}

		return areeContabili;
	}
}
