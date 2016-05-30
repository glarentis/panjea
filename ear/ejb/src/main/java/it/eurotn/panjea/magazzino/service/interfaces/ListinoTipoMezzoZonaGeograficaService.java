package it.eurotn.panjea.magazzino.service.interfaces;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ListinoTipoMezzoZonaGeograficaService {

	void cancellaListinoTipoMezzoZonaGeografica(ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica);

	List<ListinoTipoMezzoZonaGeografica> caricaListiniTipoMezzoZonaGeografica();

	ListinoTipoMezzoZonaGeografica salvaListinoTipoMezzoZonaGeografica(
			ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica);
}
