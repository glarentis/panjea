package it.eurotn.panjea.onroad.importer.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.onroad.domain.RigaIvaOnRoad;
import it.eurotn.panjea.onroad.importer.manager.interfaces.OnroadRigaIvaTransformer;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.OnroadRigaIvaTransformer")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnroadRigaIvaTransformer")
public class OnroadRigaIvaTransformerBean implements OnroadRigaIvaTransformer {

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AreaIvaManager areaIvaManager;

	private CodiceIva caricaCodiceIva(String codiceCodiceIva) {
		CodiceIva codiceIva = null;
		try {
			Query queryCodiceIva = panjeaDAO.prepareQuery("select c from CodiceIva c where c.codice=:paramCodiceIva");
			queryCodiceIva.setParameter("paramCodiceIva", codiceCodiceIva);
			codiceIva = (CodiceIva) panjeaDAO.getSingleResult(queryCodiceIva);
		} catch (ObjectNotFoundException e) {
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return codiceIva;
	}

	@Override
	public RigaIva trasforma(RigaIvaOnRoad rigaIvaOnRoad, AreaIva areaIva) {
		String codiceCodiceIva = rigaIvaOnRoad.getCodiceIva();
		CodiceIva codiceIva = caricaCodiceIva(codiceCodiceIva);

		BigDecimal imponibile = rigaIvaOnRoad.getImponibile();
		BigDecimal imposta = rigaIvaOnRoad.getImposta();
		// BigDecimal imponibileScontoNatura = rigaIvaOnRoad.getImponibileScontoNatura();

		String codiceValuta = areaIva.getDocumento().getTotale().getCodiceValuta();

		Importo imponibileImp = new Importo();
		imponibileImp.setImportoInValuta(imponibile);
		imponibileImp.setImportoInValutaAzienda(imponibile);
		imponibileImp.setCodiceValuta(codiceValuta);

		Importo impostaImp = new Importo();
		impostaImp.setImportoInValuta(imposta);
		impostaImp.setImportoInValutaAzienda(imposta);
		impostaImp.setCodiceValuta(codiceValuta);

		RigaIva rigaIva = new RigaIva();
		rigaIva.setAreaIva(areaIva);
		rigaIva.setCodiceIva(codiceIva);
		rigaIva.setImponibileVisualizzato(imponibileImp);
		rigaIva.setImpostaVisualizzata(impostaImp);

		try {
			rigaIva = areaIvaManager.salvaRigaIvaNoCkeck(rigaIva, null);
		} catch (CodiceIvaCollegatoAssenteException e) {
			throw new RuntimeException(e);
		}
		return rigaIva;
	}

}
