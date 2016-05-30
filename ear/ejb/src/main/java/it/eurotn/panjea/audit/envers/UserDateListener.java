package it.eurotn.panjea.audit.envers;

import it.eurotn.security.utils.SecurityUtils;

import org.hibernate.envers.RevisionListener;

public class UserDateListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		RevInf revEntityUserDate = (RevInf) revisionEntity;
		revEntityUserDate.setUsername(SecurityUtils.getCallerPrincipal().getUserName());
	}
}
