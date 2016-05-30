/**
 * 
 */
package it.eurotn.panjea.conai.service.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.manager.interfaces.ConaiManager;
import it.eurotn.panjea.conai.util.AnalisiConaiDTO;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 * 
 */
@Stateless(name = "Panjea.ConaiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ConaiService")
public class ConaiServiceBean implements ConaiService {

	@EJB
	private ConaiManager conaiManager;

	@Override
	public void cancellaArticoloConai(ConaiArticolo conaiArticolo) {
		conaiManager.cancellaArticoloConai(conaiArticolo);
	}

	@Override
	public void cancellaComponenteConai(ConaiComponente conaiComponente) {
		conaiManager.cancellaComponenteConai(conaiComponente);
	}

	@Override
	public List<AnalisiConaiDTO> caricaAnalisiConali(ParametriRicercaAnalisi parametri) {
		return conaiManager.caricaAnalisiConali(parametri);
	}

	@Override
	public List<ConaiArticolo> caricaArticoliConai() {
		return conaiManager.caricaArticoliConai();
	}

	@Override
	public List<ConaiComponente> caricaComponentiConai(ArticoloLite articolo) {
		return conaiManager.caricaComponentiConai(articolo);
	}

	@Override
	public List<TipoAreaMagazzino> caricaTipiAreaMagazzinoConGestioneConai() {
		return conaiManager.caricaTipiAreaMagazzinoConGestioneConai();
	}

	@Override
	public byte[] generaModulo(ConaiParametriCreazione conaiParametriCreazione) {
		return conaiManager.generaModulo(conaiParametriCreazione);
	}

	@Override
	public ConaiArticolo salvaArticoloConai(ConaiArticolo conaiArticolo) {
		return conaiManager.salvaArticoloConai(conaiArticolo);
	}

	@Override
	public ConaiComponente salvaComponenteConai(ConaiComponente conaiComponente) {
		return conaiManager.salvaComponenteConai(conaiComponente);
	}

}
