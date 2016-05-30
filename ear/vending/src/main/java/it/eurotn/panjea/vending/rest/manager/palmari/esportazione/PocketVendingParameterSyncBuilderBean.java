package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.PocketVendingParameterSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.PocketVendingParameterSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PocketVendingParameterSyncBuilder")
public class PocketVendingParameterSyncBuilderBean extends SqlGeneratorBean
		implements PocketVendingParameterSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new TreeMap<String, String>();
        fields.put("0", "Stand_alone");
        fields.put("1", "Ultima_fattura");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE PocketVendingParameter("+
			"Stand_alone bit NOT NULL," +
			"Ultima_fattura int NOT NULL)";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return "";
	}

	@Override
	protected String getTableName() {
		return "PocketVendingParameter";
	}

}
