package it.eurotn.panjea.anagrafica.service;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa.NumeroLivelloAmministrativo;
import it.eurotn.panjea.anagrafica.manager.datigeografici.interfaces.DatiGeograficiManager;
import it.eurotn.panjea.anagrafica.service.interfaces.DatiGeograficiService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.DatiGeograficiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.DatiGeograficiService")
public class DatiGeograficiServiceBean implements DatiGeograficiService {

	@EJB
	protected DatiGeograficiManager datiGeograficiManager;

	@Override
	public void cancellaCap(Cap cap) {
		datiGeograficiManager.cancellaCap(cap);
	}

	@Override
	public void cancellaLocalita(Localita localita) {
		datiGeograficiManager.cancellaLocalita(localita);
	}

	@Override
	public void cancellaNazione(Nazione nazione) {
		datiGeograficiManager.cancellaNazione(nazione);
	}

	@Override
	public void cancellaSuddivisioneAmministrativa(SuddivisioneAmministrativa suddivisioneAmministrativa) {
		datiGeograficiManager.cancellaSuddivisioneAmministrativa(suddivisioneAmministrativa);
	}

	@Override
	public List<Cap> caricaCap(DatiGeografici datiGeografici) {
		return datiGeograficiManager.caricaCap(datiGeografici);
	}

	@Override
	public Cap caricaCap(Integer idCap) {
		return datiGeograficiManager.caricaCap(idCap);
	}

	@Override
	public List<LivelloAmministrativo1> caricaLivelloAmministrativo1(DatiGeografici datiGeografici) {
		return datiGeograficiManager.caricaLivelloAmministrativo1(datiGeografici, null);
	}

	@Override
	public List<LivelloAmministrativo2> caricaLivelloAmministrativo2(DatiGeografici datiGeografici) {
		return datiGeograficiManager.caricaLivelloAmministrativo2(datiGeografici, null);
	}

	@Override
	public List<LivelloAmministrativo3> caricaLivelloAmministrativo3(DatiGeografici datiGeografici) {
		return datiGeograficiManager.caricaLivelloAmministrativo3(datiGeografici, null);
	}

	@Override
	public List<LivelloAmministrativo4> caricaLivelloAmministrativo4(DatiGeografici datiGeografici) {
		return datiGeograficiManager.caricaLivelloAmministrativo4(datiGeografici, null);
	}

	@Override
	public List<Localita> caricaLocalita(DatiGeografici datiGeografici) {
		return datiGeograficiManager.caricaLocalita(datiGeografici);
	}

	@Override
	public Localita caricaLocalita(Integer idLocalita) {
		return datiGeograficiManager.caricaLocalita(idLocalita);
	}

	@Override
	public List<Nazione> caricaNazioni(String codice) {
		return datiGeograficiManager.caricaNazioni(codice);
	}

	@Override
	public List<SuddivisioneAmministrativa> caricaSuddivisioniAmministrative(DatiGeografici datiGeografici,
			NumeroLivelloAmministrativo lvl, String filtro) {
		return datiGeograficiManager.caricaSuddivisioniAmministrative(datiGeografici, lvl, filtro);
	}

	@Override
	public Cap salvaCap(Cap cap) {
		return datiGeograficiManager.salvaCap(cap);
	}

	@Override
	public Cap salvaCap(Cap cap, List<Localita> listLocalita) {
		return datiGeograficiManager.salvaCap(cap, listLocalita);
	}

	@Override
	public Cap salvaCap(Cap cap, List<Localita> localitaAggiunte, List<Localita> localitaRimosse) {
		return datiGeograficiManager.salvaCap(cap, localitaAggiunte, localitaRimosse);
	}

	@Override
	public Localita salvaLocalita(Localita localita) {
		return datiGeograficiManager.salvaLocalita(localita);
	}

	@Override
	public Localita salvaLocalita(Localita localita, List<Cap> caps) {
		return datiGeograficiManager.salvaLocalita(localita, caps);
	}

	@Override
	public Localita salvaLocalita(Localita localita, List<Cap> capsAggiunti, List<Cap> capsRimossi) {
		return datiGeograficiManager.salvaLocalita(localita, capsAggiunti, capsRimossi);
	}

	@Override
	public Nazione salvaNazione(Nazione nazione) {
		return datiGeograficiManager.salvaNazione(nazione);
	}

	@Override
	public SuddivisioneAmministrativa salvaSuddivisioneAmministrativa(
			SuddivisioneAmministrativa suddivisioneAmministrativa) {
		return datiGeograficiManager.salvaSuddivisioneAmministrativa(suddivisioneAmministrativa);
	}
}
