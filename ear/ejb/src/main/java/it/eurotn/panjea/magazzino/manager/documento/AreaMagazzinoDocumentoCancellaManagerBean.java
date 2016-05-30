/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoDocumentoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author Leonardo
 */
@Stateless(name = "Panjea.AreaMagazzinoDocumentoCancellaManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaMagazzinoDocumentoCancellaManagerBean")
public class AreaMagazzinoDocumentoCancellaManagerBean implements AreaMagazzinoDocumentoCancellaManager {

	private static Logger logger = Logger.getLogger(AreaMagazzinoDocumentoCancellaManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	@Override
	public void cancellaAreaMagazzino(Documento documento) throws DocumentiCollegatiPresentiException,
			TipoDocumentoBaseException, AreeCollegatePresentiException {
		cancellaAreaMagazzino(documento, false);
	}

	@Override
	public void cancellaAreaMagazzino(Documento documento, boolean forceDeleteAreaCollegata)
			throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException {
		logger.debug("--> Enter cancellaAreaMagazzino");
		AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(documento);
		if (areaMagazzino != null && areaMagazzino.getId() != null && areaMagazzino.getId().intValue() != -1) {
			if (!forceDeleteAreaCollegata) {
				AreeCollegatePresentiException areeCollegatePresentiException = new AreeCollegatePresentiException();
				areeCollegatePresentiException.addAreaCollegata(areaMagazzino);
				sessionContext.setRollbackOnly();
				throw areeCollegatePresentiException;
			}
			areaMagazzinoCancellaManager.cancellaAreaMagazzino(areaMagazzino, false);
		}
	}

}
