package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.EccezioniProdottiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.EccezioniProdottiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.EccezioniProdottiSyncBuilder")
public class EccezioniProdottiSyncBuilderBean extends SqlGeneratorBean implements EccezioniProdottiSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new TreeMap<String, String>();
        fields.put("d.codice", "Matricola");
        fields.put("a.codice", "Prodotto");
        fields.put("IF(pt.tipo=0, IF(a.resa is null or a.resa = 0, 1, a.resa), 0)", "Coefficiente_resa");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE EccezioniProdotti(" +
			"Matricola nvarchar(13) NOT NULL," +
			"Prodotto nvarchar(13) NOT NULL," +
			"Coefficiente_resa float NOT NULL," +
			"PRIMARY KEY (Matricola,Prodotto))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return "from manu_installazioni i " +
				"join manu_prodotti_collegati pt on pt.installazione_id = i.id " +
				"join maga_articoli a on a.id = pt.articolo_id " +
				"join maga_articoli d on d.id = i.articolo_id";
	}

	@Override
	protected String getTableName() {
		return "EccezioniProdotti";
	}

}
