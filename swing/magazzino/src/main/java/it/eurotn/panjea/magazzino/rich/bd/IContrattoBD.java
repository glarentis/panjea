package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.panjea.magazzino.util.ContrattoStampaDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.magazzino.util.RigaContrattoCalcolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.Date;
import java.util.List;

public interface IContrattoBD {

	Contratto associaCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino);

	Contratto associaEntitaContratto(Contratto contratto, EntitaLite entitaLite);

	Contratto associaSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite);

	void cancellaContratto(Contratto contratto);

	void cancellaRigaContratto(RigaContratto rigaContratto);

	@AsyncMethodInvocation
	List<Contratto> caricaContratti(ParametriRicercaContratti parametriRicercaContratti);

	@AsyncMethodInvocation
	Contratto caricaContratto(Contratto contratto, boolean loadLazy);

	/**
	 * Carica tutti i contratti legati alla sede o a tutte le sedi se il parametro è uguale a <code>null</code>.
	 *
	 * @param idSedeEntita
	 *            id della sede, <code>null</code> per tutte le sedi
	 * @param data
	 *            data per la ricerca dei contratti
	 * @return contratti trovate
	 */
	@AsyncMethodInvocation
	List<ContrattoProspettoDTO> caricaContrattoProspetto(Integer idSedeEntita, Date data);

	RigaContratto caricaRigaContratto(RigaContratto rigaContratto);

	@AsyncMethodInvocation
	List<RigaContratto> caricaRigheContratto(Contratto contratto);

	@AsyncMethodInvocation
	List<RigaContrattoCalcolo> caricaRigheContrattoCalcolo(Integer idSedeEntita, Date data, String codiceValuta);

	/**
	 * Carica la stampa dei contratti.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return stampa contratti
	 */
	List<ContrattoStampaDTO> caricaStampaContratti(ParametriRicercaStampaContratti parametri);

	Contratto rimuoviCategoriaSedeContratto(Contratto contratto, CategoriaSedeMagazzino categoriaSedeMagazzino);

	Contratto rimuoviEntitaContratto(Contratto contratto, EntitaLite entitaLite);

	Contratto rimuoviSedeContratto(Contratto contratto, SedeMagazzinoLite sedeMagazzinoLite);

	Contratto salvaContratto(Contratto contratto, boolean loadCollection);

	RigaContratto salvaRigaContratto(RigaContratto rigaContratto);

}
