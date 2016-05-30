package it.eurotn.panjea.magazzino.manager;

import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneDepositoManager;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.MagazzinoValorizzazioneDepositoUltimoCostoAziendaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoValorizzazioneDepositoUltimoCostoAziendaManager")
public class MagazzinoValorizzazioneDepositoUltimoCostoAziendaManagerBean extends
MagazzinoValorizzazioneDepositoManagerBean implements MagazzinoValorizzazioneDepositoManager {

	@Override
	protected String getCostoFinale(ParametriRicercaValorizzazione parametri) {
		StringBuilder sb = new StringBuilder(300);
		sb.append("coalesce((select rm.importoInValutaAziendaNetto ");
		sb.append("from maga_area_magazzino am inner join maga_righe_magazzino rm on rm.areaMagazzino_id = am.id ");
		sb.append("where rm.importoInValutaAziendaNetto <> 0 and ");
		sb.append(" am.aggiornaCostoUltimo = true and ");
		sb.append(" am.dataRegistrazione <= :dataValorizzazione and ");
		sb.append(" rm.articolo_id = idArticolo ");
		sb.append("order by am.dataRegistrazione desc, am.timestamp desc limit 1),0) as costo,");
		return sb.toString();
	}
}
