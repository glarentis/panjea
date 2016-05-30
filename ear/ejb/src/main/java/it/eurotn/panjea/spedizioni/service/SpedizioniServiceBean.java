package it.eurotn.panjea.spedizioni.service;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.spedizioni.exception.SpedizioniVettoreException;
import it.eurotn.panjea.spedizioni.manager.interfaces.SpedizioniManager;
import it.eurotn.panjea.spedizioni.service.interfaces.SpedizioniService;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.SpedizioniService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.SpedizioniService")
public class SpedizioniServiceBean implements SpedizioniService {

	@EJB
	private SpedizioniManager spedizioniManager;

	@Override
	public byte[] generaEtichette(AreaMagazzino areaMagazzino, ParametriCreazioneEtichette parametriCreazioneEtichette)
			throws FileCreationException, SpedizioniVettoreException {
		return spedizioniManager.generaEtichette(areaMagazzino, parametriCreazioneEtichette);
	}

	@Override
	public byte[] generaRendicontazione(List<AreaMagazzinoRicerca> areeMagazzinoRicerca, Vettore vettore)
			throws SpedizioniVettoreException, FileCreationException {
		return spedizioniManager.generaRendicontazione(areeMagazzinoRicerca, vettore);
	}

	@Override
	public void leggiRisultatiEtichette(AreaMagazzino areaMagazzino, byte[] data) throws SpedizioniVettoreException,
			FileCreationException {
		spedizioniManager.leggiRisultatiEtichette(areaMagazzino, data);
	}

	@Override
	public void rendicontaAreeMagazzino(List<AreaMagazzinoRicerca> areeMagazzino) throws SpedizioniVettoreException {
		spedizioniManager.rendicontaAreeMagazzino(areeMagazzino);
	}

}
