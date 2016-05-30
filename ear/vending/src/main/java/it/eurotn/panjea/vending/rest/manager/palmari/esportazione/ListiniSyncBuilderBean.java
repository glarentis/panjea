package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ListiniSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ListiniSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListiniSyncBuilder")
public class ListiniSyncBuilderBean extends SqlGeneratorBean implements ListiniSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new TreeMap<String, String>();
        fields.put("lis.codice", "Listino");
        fields.put("art.codice", "Prodotto");
        fields.put("sc.prezzo", "Prezzo_moneta");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return "CREATE TABLE Listini("+
				"Listino nvarchar(20) NOT NULL,"+
				"Prodotto nvarchar(13) NOT NULL,"+
				"Prezzo_moneta float NOT NULL," +
				"Prezzo_chiave float NOT NULL DEFAULT 0," +
				"PRIMARY KEY (Listino,Prodotto))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return 
			"from maga_listini lis " +
			"join maga_versioni_listino ver on ver.listino_id = lis.id " +
			"join ( " +
			"select max(version) as vers, v.listino_id " +
			"from maga_versioni_listino v " +
			"where dataVigore <= curdate() " +
			"group by v.listino_id) as T " + 
			"on T.vers = ver.version and ver.listino_id = T.listino_id " +
			"join maga_righe_listini righe on righe.versioneListino_id = ver.id " +
			"join maga_scaglioni_listini sc on sc.rigaListino_id = righe.id " +
			"join maga_articoli art on art.id = righe.articolo_id " +
			"where lis.tipoListino = 0 " +
			"order by lis.codice, art.codice";
	}

	@Override
	protected String getTableName() {
		return "Listini";
	}

}
