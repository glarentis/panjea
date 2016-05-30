package it.eurotn.entity;

import it.eurotn.security.utils.SecurityUtils;

import java.util.Calendar;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Aggiorna i campi dell'Audit alle classe che derivano da EntityBase.
 * 
 * @author giangi
 * 
 */
public class AuditEntityBaseListener {

	/**
	 * Inserisce le informazioni di audit su inserimento dell'oggetto.
	 * 
	 * @param object
	 *            entity da inserire
	 */
	@PrePersist
	void prePersist(EntityBase object) {
		object.setTimeStamp(Calendar.getInstance().getTimeInMillis());
		object.setUserInsert(SecurityUtils.getCallerPrincipal().getUserName());

	}

	/**
	 * Inserisce le informazioni di audit su aggiornamento dell'oggetto.
	 * 
	 * @param object
	 *            entity da aggiornare
	 */
	@PreUpdate
	void preUpdate(EntityBase object) {
		object.setTimeStamp(Calendar.getInstance().getTimeInMillis());
	}
}
