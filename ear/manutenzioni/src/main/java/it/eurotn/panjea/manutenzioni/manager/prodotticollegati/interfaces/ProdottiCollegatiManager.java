package it.eurotn.panjea.manutenzioni.manager.prodotticollegati.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;

@Local
public interface ProdottiCollegatiManager extends CrudManager<ProdottoCollegato> {

}