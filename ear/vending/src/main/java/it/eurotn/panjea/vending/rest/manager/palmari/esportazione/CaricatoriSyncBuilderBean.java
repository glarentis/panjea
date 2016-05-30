package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.CaricatoriSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.CaricatoriSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CaricatoriSyncBuilder")
public class CaricatoriSyncBuilderBean extends SqlGeneratorBean implements CaricatoriSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new TreeMap<String, String>();
        fields.put("codice", "Caricatore");
        fields.put("CONCAT(nome, ' ', cognome)", "Nome");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE Caricatori("+
			"Caricatore nvarchar(10) NOT NULL," +
			"Nome nvarchar(255) NULL," +
			"PRIMARY KEY (Caricatore))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return "from manu_operatori where caricatore = 1";
	}

	@Override
	protected String getTableName() {
		return "Caricatori";
	}

}
