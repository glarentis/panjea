package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces.PreparaFatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneDifferitaGenerator;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneManager;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.PreparaFatturazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PreparaFatturazioneManager")
public class PreparaFatturazioneManagerBean implements PreparaFatturazioneManager {

	@EJB
	@IgnoreDependency
	private FatturazioneManager fatturazioneManager;

	@EJB(mappedName = "Panjea.FatturazioneDifferitaGenerator")
	@IgnoreDependency
	private FatturazioneDifferitaGenerator fatturazioneDifferitaGenerator;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	@Override
	public void cancellaMovimentiInFatturazione(String utente) {
		fatturazioneManager.cancellaMovimentiInFatturazione(utente);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public List<AreaMagazzinoFatturazione> caricaAreeMagazzinoFatturazione(List<AreaMagazzinoLite> areeDaFatturare) {
		return fatturazioneDifferitaGenerator.caricaAreeMagazzinoFatturazione(areeDaFatturare);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void ordinaFatturazione(String utente) {
		fatturazioneManager.ordinaFatturazioneCorrente(utente);
	}

}
