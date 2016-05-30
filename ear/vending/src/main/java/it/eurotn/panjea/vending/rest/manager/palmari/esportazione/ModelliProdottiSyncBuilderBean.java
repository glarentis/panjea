package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ModelliProdottiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ModelliProdottiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ModelliProdottiSyncBuilder")
public class ModelliProdottiSyncBuilderBean extends SqlGeneratorBean implements ModelliProdottiSyncBuilder {

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
		return 
			"CREATE TABLE ModelliProdotti(" +
			"Modello nvarchar(10) NOT NULL," +
			"Prodotto nvarchar(13) NOT NULL," +
			"Coefficiente_resa float NOT NULL," +
			"PRIMARY KEY (Modello,Prodotto))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return 
			"from vend_modelli m " +
			"join manu_prodotti_collegati pm on pm.modello_id = m.id " +
			"join maga_articoli a on a.id = pm.articolo_id " +
			"where pm.tipo = 0";
	}

	@Override
	protected String getTableName() {
		return "ModelliProdotti";
	}

}
