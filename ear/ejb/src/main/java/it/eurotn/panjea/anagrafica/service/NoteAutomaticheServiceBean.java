/**
 *
 */
package it.eurotn.panjea.anagrafica.service;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.manager.noteautomatiche.interfaces.NoteAutomaticheManager;
import it.eurotn.panjea.anagrafica.service.interfaces.NoteAutomaticheService;

import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.NoteAutomaticheService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.NoteAutomaticheService")
public class NoteAutomaticheServiceBean implements NoteAutomaticheService {

	@EJB(beanName = "NoteAutomaticheManagerBean")
	private NoteAutomaticheManager noteAutomaticheManagerBean;

	@Override
	public void cancellaNotaAutomatica(NotaAutomatica notaAutomatica) {
		noteAutomaticheManagerBean.cancellaNotaAutomatica(notaAutomatica);
	}

	@Override
	public NotaAutomatica caricaNotaAutomatica(Integer idNotaAutomatica) {
		return noteAutomaticheManagerBean.caricaNotaAutomatica(idNotaAutomatica);
	}

	@Override
	public List<NotaAutomatica> caricaNoteAutomatiche(Date data, Documento documento) {
		return noteAutomaticheManagerBean.caricaNoteAutomatiche(data, documento);
	}

	@Override
	public List<NotaAutomatica> caricaNoteAutomatiche(Integer idEntita, Integer idSedeEntita) {
		return noteAutomaticheManagerBean.caricaNoteAutomatiche(idEntita, idSedeEntita);
	}

	@Override
	public NotaAutomatica salvaNotaAutomatica(NotaAutomatica notaAutomatica) {
		return noteAutomaticheManagerBean.salvaNotaAutomatica(notaAutomatica);
	}
}
