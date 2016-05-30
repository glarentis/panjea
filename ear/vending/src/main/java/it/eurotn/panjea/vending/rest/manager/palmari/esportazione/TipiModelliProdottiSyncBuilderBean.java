package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.TipiModelliProdottiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.TipiModelliProdottiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiModelliProdottiSyncBuilder")
public class TipiModelliProdottiSyncBuilderBean extends SqlGeneratorBean implements TipiModelliProdottiSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new TreeMap<String, String>();
        fields.put("m.codice", "Modello");
        fields.put("a.codice", "Prodotto");
        fields.put("IF(a.resa is null or a.resa = 0, 1, a.resa)", "Coefficiente_resa");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return "";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return 
			"from vend_modelli m " +
			"join manu_prodotti_collegati pt on pt.tipoModello_id = m.tipoModello_id " +
			"join maga_articoli a on a.id = pt.articolo_id " +
			"where pt.tipo = 0";
	}

	@Override
	protected String getTableName() {
		return "ModelliProdotti";
	}

}
