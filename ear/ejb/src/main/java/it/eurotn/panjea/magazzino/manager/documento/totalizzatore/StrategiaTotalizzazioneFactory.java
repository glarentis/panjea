package it.eurotn.panjea.magazzino.manager.documento.totalizzatore;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Factory per ritornare un {@link Totalizzatore} adeguato alla {@link StrategiaTotalizzazioneDocumento} scelta.
 * 
 * @author Leonardo
 */
@Stateless(name = "Panjea.StrategiaTotalizzazioneFactory")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StrategiaTotalizzazioneFactory")
public class StrategiaTotalizzazioneFactory implements StrategiaTotalizzazione {

	private static Logger logger = Logger.getLogger(StrategiaTotalizzazioneFactory.class);
	@Resource
	private SessionContext context;

	@Override
	public Totalizzatore getTotalizzatore(StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento) {
		logger.debug("--> Enter getTotalizzatore " + strategiaTotalizzazioneDocumento);
		String totalizzatoreBeanString = "";
		switch (strategiaTotalizzazioneDocumento) {
		case NORMALE:
			totalizzatoreBeanString = "Panjea.TotalizzazioneNormale";
			break;
		case SCONTRINO:
			totalizzatoreBeanString = "Panjea.TotalizzazioneScontrino";
			break;
		default:
			throw new UnsupportedOperationException("Strategia di totalizzazione non prevista!");
		}
		Totalizzatore totalizzatore = (Totalizzatore) context.lookup(totalizzatoreBeanString);
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit getTotalizzatore " + totalizzatore.getClass().getName());
		}
		return totalizzatore;
	}

}
