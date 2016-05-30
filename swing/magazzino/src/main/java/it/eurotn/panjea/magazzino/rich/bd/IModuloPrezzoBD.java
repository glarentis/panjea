package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

public interface IModuloPrezzoBD {

	@AsyncMethodInvocation
	PoliticaPrezzo calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi);
}
