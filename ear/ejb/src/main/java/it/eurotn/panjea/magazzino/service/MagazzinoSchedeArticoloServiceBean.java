/**
 * 
 */
package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoElaborazioniSchedeArticoloManager;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoSchedeArticoloManager;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoSchedeArticoloService;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.MagazzinoSchedeArticoloService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.MagazzinoSchedeArticoloService")
public class MagazzinoSchedeArticoloServiceBean implements MagazzinoSchedeArticoloService {

	@EJB
	private MagazzinoSchedeArticoloManager magazzinoSchedeArticoloManager;

	@EJB
	private MagazzinoElaborazioniSchedeArticoloManager magazzinoElaborazioniSchedeArticoloManager;

	@Override
	public List<ArticoloRicerca> caricaArticoliNonValidi(Integer anno, Integer mese) {
		return magazzinoSchedeArticoloManager.caricaArticoliNonValidi(anno, mese);
	}

	@Override
	public List<ArticoloRicerca> caricaArticoliRimanenti(Integer anno, Integer mese) {
		return magazzinoSchedeArticoloManager.caricaArticoliRimanenti(anno, mese);
	}

	@Override
	public List<ArticoloRicerca> caricaArticoliStampati(Integer anno, Integer mese) {
		return magazzinoSchedeArticoloManager.caricaArticoliStampati(anno, mese);
	}

	@Override
	public List<ElaborazioneSchedaArticoloDTO> caricaElaborazioniSchedeArticolo(ParametriRicercaElaborazioni parametri) {
		return magazzinoElaborazioniSchedeArticoloManager.caricaElaborazioniSchedeArticolo(parametri);
	}

	@Override
	public byte[] caricaFileSchedaArticolo(Integer anno, Integer mese, Integer idArticolo) throws FileNotFoundException {
		return magazzinoSchedeArticoloManager.caricaFileSchedaArticolo(anno, mese, idArticolo);
	}

	@Override
	public int caricaNumeroSchedeArticoloInCodaDiElaborazione() {
		return magazzinoElaborazioniSchedeArticoloManager.caricaNumeroSchedeArticoloInCodaDiElaborazione();
	}

	@Override
	public List<MovimentazioneArticolo> caricaSchedaArticolo(Map<Object, Object> params) {
		return magazzinoSchedeArticoloManager.caricaSchedaArticolo(params);
	}

	@Override
	public List<SituazioneSchedaArticoloDTO> caricaSituazioneSchedeArticolo(Integer anno) {
		return magazzinoSchedeArticoloManager.caricaSituazioneSchedeArticolo(anno);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void creaSchedeArticolo(ParametriCreazioneSchedeArticoli parametriCreazioneSchedeArticoli) {
		magazzinoSchedeArticoloManager.creaSchedeArticolo(parametriCreazioneSchedeArticoli);
	}

	@Override
	public void modificaDescrizioneElaborazione(String descrizioneOld, String descrizioneNew) {
		magazzinoElaborazioniSchedeArticoloManager.modificaDescrizioneElaborazione(descrizioneOld, descrizioneNew);
	}

}
