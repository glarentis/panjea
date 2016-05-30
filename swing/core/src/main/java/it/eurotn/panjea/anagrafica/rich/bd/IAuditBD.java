package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.audit.envers.RevInf;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAuditBD {

	/**
	 * Cancella tutti i dati di audit e revinf precedenti alla data indicata.
	 * 
	 * @param data
	 *            di riferimento
	 */
	void cancellaAuditPrecedente(Date data);

	/**
	 * Restituisce il numero di tutte le {@link RevInf}presenti.
	 * 
	 * @return numero di {@link RevInf} presenti
	 */
	Integer caricaNumeroRevInf();

	/**
	 * @param auditBean
	 *            bean per il quale recuperare le versioni o con proprietà per le quali recuperare le versioni (tramite
	 *            l'attribute {@link AuditableProperties}
	 * 
	 * @return mappa contenente le varie revisioni per gli oggetti auditable<br/>
	 */
	@AsyncMethodInvocation
	Map<RevInf, List<Object>> getVersioni(Object auditBean);
}
