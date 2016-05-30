package it.eurotn.panjea.magazzino.service.interfaces;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;

import javax.ejb.Remote;

@Remote
public interface ModuloPrezzoService {

	public PoliticaPrezzo calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi);
}
